package com.quickblox.quickblox_sdk.conference;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import com.quickblox.videochat.webrtc.util.Logger;

import org.webrtc.ThreadUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Injoit on 2020-09-09.
 * Copyright © 2020 Quickblox. All rights reserved.
 */
public class ConferenceAudioManager {
    private static final String TAG = ConferenceAudioManager.class.getSimpleName();
    private static final Logger LOGGER = Logger.getInstance(TAG);
    private ConferenceAudioManager.OnWiredHeadsetStateListener wiredHeadsetStateListener;
    private ConferenceAudioManager.BluetoothAudioDeviceStateListener bluetoothAudioDeviceStateListener;
    private boolean manageHeadsetByDefault;
    private boolean manageBluetoothByDefault;
    private boolean manageSpeakerPhoneByProximity;
    private Context apprtcContext;
    private AudioManager audioManager;
    private ConferenceAudioManager.AudioManagerEvents audioManagerEvents;
    private ConferenceAudioManager.AudioManagerState amState;
    private boolean savedIsSpeakerPhoneOn;
    private boolean savedIsMicrophoneMute;
    private boolean hasWiredHeadset;
    private ConferenceAudioManager.AudioDevice defaultAudioDevice;
    private ConferenceAudioManager.AudioDevice selectedAudioDevice;
    private ConferenceAudioManager.AudioDevice userSelectedAudioDevice;
    private QBProximitySensor proximitySensor;
    private QBBluetoothManager bluetoothManager;
    private Set<AudioDevice> audioDevices;
    private BroadcastReceiver wiredHeadsetReceiver;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;

    private AudioFocusRequest audioFocusRequest;

    private void onProximitySensorChangedState() {
        if (manageSpeakerPhoneByProximity) {
            if (audioDevices.size() == 2 && audioDevices.contains(ConferenceAudioManager.AudioDevice.EARPIECE) && audioDevices.contains(ConferenceAudioManager.AudioDevice.SPEAKER_PHONE)) {
                if (proximitySensor.sensorReportsNearState()) {
                    userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.EARPIECE;
                    setAudioDeviceInternal(userSelectedAudioDevice);
                } else {
                    userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.SPEAKER_PHONE;
                    setAudioDeviceInternal(userSelectedAudioDevice);
                }

                if (audioManagerEvents != null) {
                    audioManagerEvents.onAudioDeviceChanged(selectedAudioDevice, audioDevices);
                }
            }
        }
    }

    public static ConferenceAudioManager create(Context context) {
        return new ConferenceAudioManager(context);
    }

    private ConferenceAudioManager(Context context) {
        manageHeadsetByDefault = true;
        manageBluetoothByDefault = true;
        manageSpeakerPhoneByProximity = false;
        savedIsSpeakerPhoneOn = false;
        savedIsMicrophoneMute = false;
        hasWiredHeadset = false;
        defaultAudioDevice = ConferenceAudioManager.AudioDevice.SPEAKER_PHONE;
        proximitySensor = null;
        audioDevices = new HashSet<>();
        ThreadUtils.checkIsOnMainThread();
        apprtcContext = context.getApplicationContext();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        bluetoothManager = QBBluetoothManager.create(context, this);
        wiredHeadsetReceiver = new ConferenceAudioManager.WiredHeadsetReceiver();
        amState = ConferenceAudioManager.AudioManagerState.UNINITIALIZED;
        proximitySensor = QBProximitySensor.create(context, new Runnable() {
            @Override
            public void run() {
                onProximitySensorChangedState();
            }
        });
        LOGGER.d(TAG, "defaultAudioDevice: " + defaultAudioDevice);
        QBAudioUtils.logDeviceInfo(TAG);
    }

    public void start(ConferenceAudioManager.AudioManagerEvents audioManagerEvents) {
        LOGGER.d(TAG, "start");
        ThreadUtils.checkIsOnMainThread();
        if (amState == ConferenceAudioManager.AudioManagerState.RUNNING) {
            LOGGER.e(TAG, "AudioManager is already active");
        } else {
            LOGGER.d(TAG, "AudioManager starts...");
            this.audioManagerEvents = audioManagerEvents;
            amState = ConferenceAudioManager.AudioManagerState.RUNNING;
            savedIsSpeakerPhoneOn = audioManager.isSpeakerphoneOn();
            savedIsMicrophoneMute = audioManager.isMicrophoneMute();
            hasWiredHeadset = hasWiredHeadset();

            audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    String typeOfChange;
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            typeOfChange = "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK";
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            typeOfChange = "AUDIOFOCUS_LOSS_TRANSIENT";
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            typeOfChange = "AUDIOFOCUS_LOSS";
                            break;
                        case AudioManager.ADJUST_SAME:
                        default:
                            typeOfChange = "AUDIOFOCUS_INVALID";
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            typeOfChange = "AUDIOFOCUS_GAIN";
                            break;
                        case AudioManager.AUDIOFOCUS_REQUEST_DELAYED:
                            typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT";
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                            typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK";
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE:
                            typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE";
                    }

                    LOGGER.d(TAG, "onAudioFocusChange: " + typeOfChange);
                }
            };

            int result;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();

                audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
                        .setAcceptsDelayedFocusGain(true)
                        .setWillPauseWhenDucked(true)
                        .setAudioAttributes(audioAttributes)
                        .build();

                result = audioManager.requestAudioFocus(audioFocusRequest);
                Log.e(TAG + "_AUDIO_MANAGER", "requestAudioFocus");
            } else {
                result = audioManager.requestAudioFocus(audioFocusChangeListener, 0, 2);
            }

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                LOGGER.d(TAG, "Audio focus request granted");
            } else {
                LOGGER.e(TAG, "Audio focus request failed");
            }

            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            setMicrophoneMute(false);
            userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.NONE;
            selectedAudioDevice = ConferenceAudioManager.AudioDevice.NONE;
            audioDevices.clear();
            bluetoothManager.start();
            proximitySensor.start();
            updateAudioDeviceState();
            registerReceiver(wiredHeadsetReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
            LOGGER.d(TAG, "AudioManager started");
        }
    }

    public AudioManager getAndroidAudioManager() {
        return audioManager;
    }

    public void setManageHeadsetByDefault(boolean manageHeadsetByDefault) {
        this.manageHeadsetByDefault = manageHeadsetByDefault;
    }

    public void setOnWiredHeadsetStateListener(ConferenceAudioManager.OnWiredHeadsetStateListener wiredHeadsetStateListener) {
        this.wiredHeadsetStateListener = wiredHeadsetStateListener;
    }

    private void notifyWiredHeadsetListener(boolean plugged, boolean hasMicrophone) {
        if (wiredHeadsetStateListener != null) {
            wiredHeadsetStateListener.onWiredHeadsetStateChanged(plugged, hasMicrophone);
        }
    }

    public void setManageSpeakerPhoneByProximity(boolean manageSpeakerPhoneByProximity) {
        this.manageSpeakerPhoneByProximity = manageSpeakerPhoneByProximity;
    }

    public void setManageBluetoothByDefault(boolean manageBluetoothByDefault) {
        this.manageBluetoothByDefault = manageBluetoothByDefault;
    }

    public void setBluetoothAudioDeviceStateListener(ConferenceAudioManager.BluetoothAudioDeviceStateListener bluetoothAudioDeviceStateListener) {
        this.bluetoothAudioDeviceStateListener = bluetoothAudioDeviceStateListener;
    }

    private void notifyBluetoothAudioDeviceStateListener(boolean connected) {
        if (bluetoothAudioDeviceStateListener != null) {
            bluetoothAudioDeviceStateListener.onStateChanged(connected);
        }
    }

    public void stop() {
        LOGGER.d(TAG, "stop");
        ThreadUtils.checkIsOnMainThread();
        if (amState != ConferenceAudioManager.AudioManagerState.RUNNING) {
            LOGGER.e(TAG, "Trying to stop AudioManager in incorrect state: " + amState);
        } else {
            amState = ConferenceAudioManager.AudioManagerState.UNINITIALIZED;
            unregisterReceiver(wiredHeadsetReceiver);
            bluetoothManager.stop();
            setSpeakerphoneOn(savedIsSpeakerPhoneOn);
            setMicrophoneMute(savedIsMicrophoneMute);
            audioManager.setMode(AudioManager.MODE_NORMAL);

            int result;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                result = audioManager.abandonAudioFocusRequest(audioFocusRequest);
                Log.e(TAG + "_AUDIO_MANAGER", "releaseAudioManager: NEW FOCUS RELEASED");
            } else {
                result = audioManager.abandonAudioFocus(audioFocusChangeListener);
            }

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                LOGGER.d(TAG, "Audio focus abandon success");
            } else {
                LOGGER.e(TAG, "Audio focus abandon failed");
            }

            audioFocusChangeListener = null;
            LOGGER.d(TAG, "Abandoned audio focus for VOICE_CALL streams");
            if (proximitySensor != null) {
                proximitySensor.stop();
                proximitySensor = null;
            }

            audioManagerEvents = null;
            LOGGER.d(TAG, "AudioManager stopped");
        }
    }

    public ConferenceAudioManager.AudioDevice getDefaultAudioDevice() {
        return defaultAudioDevice;
    }

    private void setAudioDeviceInternal(ConferenceAudioManager.AudioDevice device) {
        LOGGER.d(TAG, "setAudioDeviceInternal(device=" + device + ")");
        if (!audioDevices.contains(device)) {
            LOGGER.e(TAG, "Invalid audio device selection");
        } else {
            switch (device) {
                case SPEAKER_PHONE:
                    setSpeakerphoneOn(true);
                    break;
                case EARPIECE:
                case WIRED_HEADSET:
                case BLUETOOTH:
                    setSpeakerphoneOn(false);
                    break;
                default:
                    LOGGER.e(TAG, "Invalid audio device selection");
            }

            if (ConferenceAudioManager.AudioDevice.EARPIECE == device && hasWiredHeadset) {
                selectedAudioDevice = ConferenceAudioManager.AudioDevice.WIRED_HEADSET;
            } else {
                selectedAudioDevice = device;
            }

        }
    }

    public void setDefaultAudioDevice(ConferenceAudioManager.AudioDevice defaultDevice) {
        ThreadUtils.checkIsOnMainThread();
        switch (defaultDevice) {
            case SPEAKER_PHONE:
                defaultAudioDevice = defaultDevice;
                break;
            case EARPIECE:
                if (hasEarpiece()) {
                    defaultAudioDevice = defaultDevice;
                } else {
                    defaultAudioDevice = ConferenceAudioManager.AudioDevice.SPEAKER_PHONE;
                }
                break;
            default:
                LOGGER.e(TAG, "Invalid default audio device selection");
        }

        LOGGER.d(TAG, "setDefaultAudioDevice(device=" + defaultAudioDevice + ")");
        updateAudioDeviceState();
    }

    public void selectAudioDevice(ConferenceAudioManager.AudioDevice device) {
        ThreadUtils.checkIsOnMainThread();
        if (!audioDevices.contains(device)) {
            LOGGER.e(TAG, "Can not select " + device + " from available " + audioDevices);
        } else {
            userSelectedAudioDevice = device;
            updateAudioDeviceState();
        }
    }

    public Set<AudioDevice> getAudioDevices() {
        ThreadUtils.checkIsOnMainThread();
        return Collections.unmodifiableSet(new HashSet<>(audioDevices));
    }

    public ConferenceAudioManager.AudioDevice getSelectedAudioDevice() {
        ThreadUtils.checkIsOnMainThread();
        return selectedAudioDevice;
    }

    private void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        apprtcContext.registerReceiver(receiver, filter);
    }

    private void unregisterReceiver(BroadcastReceiver receiver) {
        apprtcContext.unregisterReceiver(receiver);
    }

    private void setSpeakerphoneOn(boolean on) {
        boolean wasOn = audioManager.isSpeakerphoneOn();
        if (wasOn != on) {
            audioManager.setSpeakerphoneOn(on);
        }
    }

    private void setMicrophoneMute(boolean on) {
        boolean wasMuted = audioManager.isMicrophoneMute();
        if (wasMuted != on) {
            audioManager.setMicrophoneMute(on);
        }
    }

    private boolean hasEarpiece() {
        return apprtcContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    private boolean hasWiredHeadset() {
        if (Build.VERSION.SDK_INT < 23) {
            return audioManager.isWiredHeadsetOn();
        } else {
            AudioDeviceInfo[] devices = audioManager.getDevices(3);
            for (AudioDeviceInfo device : devices) {
                int type = device.getType();
                if (type == 3) {
                    LOGGER.d(TAG, "hasWiredHeadset: found wired headset");
                    return true;
                }

                if (type == 11) {
                    LOGGER.d(TAG, "hasWiredHeadset: found USB audio device");
                    return true;
                }
            }

            return false;
        }
    }

    void updateAudioDeviceState() {
        ThreadUtils.checkIsOnMainThread();
        LOGGER.d(TAG, "--- updateAudioDeviceState: wired headset=" + hasWiredHeadset + ", BT state="
                + bluetoothManager.getState());

        LOGGER.d(TAG, "Device status: available=" + audioDevices + ", selected=" + selectedAudioDevice
                + ", user selected=" + userSelectedAudioDevice);

        if (bluetoothManager.getState() == QBBluetoothManager.State.HEADSET_AVAILABLE
                || bluetoothManager.getState() == QBBluetoothManager.State.HEADSET_UNAVAILABLE
                || bluetoothManager.getState() == QBBluetoothManager.State.SCO_DISCONNECTING) {
            bluetoothManager.updateDevice();
        }

        Set<AudioDevice> newAudioDevices = new HashSet<>();
        if (bluetoothManager.getState() == QBBluetoothManager.State.SCO_CONNECTED
                || bluetoothManager.getState() == QBBluetoothManager.State.SCO_CONNECTING
                || bluetoothManager.getState() == QBBluetoothManager.State.HEADSET_AVAILABLE) {
            newAudioDevices.add(ConferenceAudioManager.AudioDevice.BLUETOOTH);

            if (!audioDevices.isEmpty() && !audioDevices.contains(ConferenceAudioManager.AudioDevice.BLUETOOTH)) {
                if (manageBluetoothByDefault) {
                    userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.BLUETOOTH;
                }

                notifyBluetoothAudioDeviceStateListener(true);
            }
        }

        if (hasWiredHeadset) {
            newAudioDevices.add(ConferenceAudioManager.AudioDevice.WIRED_HEADSET);
        }

        newAudioDevices.add(ConferenceAudioManager.AudioDevice.SPEAKER_PHONE);
        if (hasEarpiece()) {
            newAudioDevices.add(ConferenceAudioManager.AudioDevice.EARPIECE);
        }

        boolean audioDeviceSetUpdated = !audioDevices.equals(newAudioDevices);
        audioDevices = newAudioDevices;
        if (bluetoothManager.getState() == QBBluetoothManager.State.HEADSET_UNAVAILABLE
                && userSelectedAudioDevice == ConferenceAudioManager.AudioDevice.BLUETOOTH) {
            userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.NONE;
        }

        if (!hasWiredHeadset && userSelectedAudioDevice == ConferenceAudioManager.AudioDevice.WIRED_HEADSET) {
            userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.NONE;
        }

        boolean needBluetoothAudioStart = bluetoothManager.getState() == QBBluetoothManager.State.HEADSET_AVAILABLE
                && (userSelectedAudioDevice == ConferenceAudioManager.AudioDevice.NONE
                || userSelectedAudioDevice == ConferenceAudioManager.AudioDevice.BLUETOOTH);

        boolean needBluetoothAudioStop = (bluetoothManager.getState() == QBBluetoothManager.State.SCO_CONNECTED
                || bluetoothManager.getState() == QBBluetoothManager.State.SCO_CONNECTING)
                && userSelectedAudioDevice != ConferenceAudioManager.AudioDevice.NONE
                && userSelectedAudioDevice != ConferenceAudioManager.AudioDevice.BLUETOOTH;

        if (bluetoothManager.getState() == QBBluetoothManager.State.HEADSET_AVAILABLE
                || bluetoothManager.getState() == QBBluetoothManager.State.SCO_CONNECTING
                || bluetoothManager.getState() == QBBluetoothManager.State.SCO_CONNECTED) {
            LOGGER.d(TAG, "Need BT audio: start=" + needBluetoothAudioStart + ", stop=" +
                    needBluetoothAudioStop + ", BT state=" + bluetoothManager.getState());
        }

        if (needBluetoothAudioStop) {
            bluetoothManager.stopScoAudio();
            bluetoothManager.updateDevice();
        }

        if (needBluetoothAudioStart && !needBluetoothAudioStop && !bluetoothManager.startScoAudio()) {
            audioDevices.remove(ConferenceAudioManager.AudioDevice.BLUETOOTH);
            notifyBluetoothAudioDeviceStateListener(false);
            audioDeviceSetUpdated = true;
        }

        ConferenceAudioManager.AudioDevice newAudioDevice;
        if (userSelectedAudioDevice != ConferenceAudioManager.AudioDevice.NONE) {
            newAudioDevice = userSelectedAudioDevice;
        } else {
            newAudioDevice = defaultAudioDevice;
        }

        if (newAudioDevice != selectedAudioDevice || audioDeviceSetUpdated) {
            setAudioDeviceInternal(newAudioDevice);
            LOGGER.d(TAG, "New device status: available=" + audioDevices + ", selected=" + selectedAudioDevice);
            if (audioManagerEvents != null) {
                audioManagerEvents.onAudioDeviceChanged(selectedAudioDevice, audioDevices);
            }
        }

        LOGGER.d(TAG, "--- updateAudioDeviceState done");
    }

    public interface BluetoothAudioDeviceStateListener {
        void onStateChanged(boolean var1);
    }

    public interface AudioManagerEvents {
        void onAudioDeviceChanged(ConferenceAudioManager.AudioDevice var1, Set<AudioDevice> var2);
    }

    public interface OnWiredHeadsetStateListener {
        void onWiredHeadsetStateChanged(boolean var1, boolean var2);
    }

    private class WiredHeadsetReceiver extends BroadcastReceiver {
        private static final int STATE_UNPLUGGED = 0;
        private static final int STATE_PLUGGED = 1;
        private static final int HAS_NO_MIC = 0;
        private static final int HAS_MIC = 1;

        private WiredHeadsetReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("state", 0);
            int microphone = intent.getIntExtra("microphone", 0);
            String name = intent.getStringExtra("name");

            ConferenceAudioManager.LOGGER.d(ConferenceAudioManager.TAG, "WiredHeadsetReceiver.onReceive" +
                    QBAudioUtils.getThreadInfo() + ": a=" + intent.getAction() + ", s=" + (state == 0
                    ? "unplugged" : "plugged") + ", m=" + (microphone == 1 ? "mic" : "no mic")
                    + ", n=" + name + ", sb=" + isInitialStickyBroadcast());

            hasWiredHeadset = state == 1;
            notifyWiredHeadsetListener(state == 1, microphone == 1);
            if (manageHeadsetByDefault) {
                if (hasWiredHeadset) {
                    userSelectedAudioDevice = ConferenceAudioManager.AudioDevice.WIRED_HEADSET;
                }
                updateAudioDeviceState();
            }
        }
    }

    public enum AudioManagerState {
        UNINITIALIZED,
        PREINITIALIZED,
        RUNNING
    }

    public enum AudioDevice {
        SPEAKER_PHONE,
        WIRED_HEADSET,
        EARPIECE,
        BLUETOOTH,
        NONE
    }

    static class QBProximitySensor implements SensorEventListener {
        private final String TAG = QBProximitySensor.class.getSimpleName();
        private final ThreadUtils.ThreadChecker threadChecker = new ThreadUtils.ThreadChecker();
        private final Runnable onSensorStateListener;
        private final SensorManager sensorManager;
        private Sensor proximitySensor = null;
        private boolean lastStateReportIsNear = false;

        static QBProximitySensor create(Context context, Runnable sensorStateListener) {
            return new QBProximitySensor(context, sensorStateListener);
        }

        private QBProximitySensor(Context context, Runnable sensorStateListener) {
            LOGGER.d(TAG, QBAudioUtils.getThreadInfo());
            onSensorStateListener = sensorStateListener;
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }

        boolean start() {
            threadChecker.checkIsOnValidThread();
            LOGGER.d(TAG, "start" + QBAudioUtils.getThreadInfo());
            if (!initDefaultSensor()) {
                return false;
            } else {
                sensorManager.registerListener(this, proximitySensor, 3);
                return true;
            }
        }

        void stop() {
            threadChecker.checkIsOnValidThread();
            LOGGER.d(TAG, "stop" + QBAudioUtils.getThreadInfo());
            if (proximitySensor != null) {
                sensorManager.unregisterListener(this, proximitySensor);
            }
        }

        boolean sensorReportsNearState() {
            threadChecker.checkIsOnValidThread();
            return lastStateReportIsNear;
        }

        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
            threadChecker.checkIsOnValidThread();
            if (sensor.getType() != 8) {
                LOGGER.e(TAG, "Accuracy changed for unexpected sensor");
            } else {
                if (accuracy == 0) {
                    LOGGER.e(TAG, "The values returned by this sensor cannot be trusted");
                }
            }
        }

        public final void onSensorChanged(SensorEvent event) {
            threadChecker.checkIsOnValidThread();
            if (event.sensor.getType() != 8) {
                LOGGER.e(TAG, "Sensor changed for unexpected sensor");
            } else {
                float distanceInCentimeters = event.values[0];
                if (distanceInCentimeters < proximitySensor.getMaximumRange()) {
                    LOGGER.d(TAG, "Proximity sensor => NEAR state");
                    lastStateReportIsNear = true;
                } else {
                    LOGGER.d(TAG, "Proximity sensor => FAR state");
                    lastStateReportIsNear = false;
                }

                if (onSensorStateListener != null) {
                    onSensorStateListener.run();
                }

                LOGGER.d(TAG, "onSensorChanged" + QBAudioUtils.getThreadInfo() + ": accuracy="
                        + event.accuracy + ", timestamp=" + event.timestamp + ", distance=" + event.values[0]);
            }
        }

        private boolean initDefaultSensor() {
            if (proximitySensor != null) {
                return true;
            } else {
                proximitySensor = sensorManager.getDefaultSensor(8);
                if (proximitySensor == null) {
                    return false;
                } else {
                    logProximitySensorInfo();
                    return true;
                }
            }
        }

        private void logProximitySensorInfo() {
            if (proximitySensor != null) {
                StringBuilder info = new StringBuilder("Proximity sensor: ");
                info.append("name=").append(proximitySensor.getName());
                info.append(", vendor: ").append(proximitySensor.getVendor());
                info.append(", power: ").append(proximitySensor.getPower());
                info.append(", resolution: ").append(proximitySensor.getResolution());
                info.append(", max range: ").append(proximitySensor.getMaximumRange());
                info.append(", min delay: ").append(proximitySensor.getMinDelay());
                if (Build.VERSION.SDK_INT >= 20) {
                    info.append(", type: ").append(proximitySensor.getStringType());
                }

                if (Build.VERSION.SDK_INT >= 21) {
                    info.append(", max delay: ").append(proximitySensor.getMaxDelay());
                    info.append(", reporting mode: ").append(proximitySensor.getReportingMode());
                    info.append(", isWakeUpSensor: ").append(proximitySensor.isWakeUpSensor());
                }

                LOGGER.d(TAG, info.toString());
            }
        }
    }

    static class QBBluetoothManager {
        private static final String TAG = QBBluetoothManager.class.getSimpleName();
        private static final int BLUETOOTH_SCO_TIMEOUT_MS = 4000;
        private static final int MAX_SCO_CONNECTION_ATTEMPTS = 2;
        private final Context apprtcContext;
        private final ConferenceAudioManager apprtcAudioManager;
        private final AudioManager audioManager;
        private final Handler handler;
        int scoConnectionAttempts;
        private QBBluetoothManager.State bluetoothState;
        private final BluetoothProfile.ServiceListener bluetoothServiceListener;
        private BluetoothAdapter bluetoothAdapter;
        private BluetoothHeadset bluetoothHeadset;
        private BluetoothDevice bluetoothDevice;
        private final BroadcastReceiver bluetoothHeadsetReceiver;
        private final Runnable bluetoothTimeoutRunnable = new Runnable() {
            public void run() {
                bluetoothTimeout();
            }
        };

        static QBBluetoothManager create(Context context, ConferenceAudioManager audioManager) {
            LOGGER.d(QBBluetoothManager.TAG, "create" + QBAudioUtils.getThreadInfo());
            return new QBBluetoothManager(context, audioManager);
        }

        private QBBluetoothManager(Context context, ConferenceAudioManager audioManager) {
            ThreadUtils.checkIsOnMainThread();
            apprtcContext = context;
            apprtcAudioManager = audioManager;
            this.audioManager = getAudioManager(context);
            bluetoothState = QBBluetoothManager.State.UNINITIALIZED;
            bluetoothServiceListener = new QBBluetoothServiceListener();
            bluetoothHeadsetReceiver = new QBBluetoothHeadsetBroadcastReceiver();
            handler = new Handler(Looper.getMainLooper());
        }

        QBBluetoothManager.State getState() {
            ThreadUtils.checkIsOnMainThread();
            return bluetoothState;
        }

        @SuppressLint({"MissingPermission"})
        void start() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "start");
            if (!hasPermission(apprtcContext, "android.permission.BLUETOOTH")) {
                LOGGER.w(TAG, "Process (pid=" + Process.myPid() + ") lacks BLUETOOTH permission");
            } else if (bluetoothState != QBBluetoothManager.State.UNINITIALIZED) {
                LOGGER.w(TAG, "Invalid BT state");
            } else {
                bluetoothHeadset = null;
                bluetoothDevice = null;
                scoConnectionAttempts = 0;
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    LOGGER.w(TAG, "Device does not support Bluetooth");
                } else if (!audioManager.isBluetoothScoAvailableOffCall()) {
                    LOGGER.e(TAG, "Bluetooth SCO audio is not available off call");
                } else {
                    logBluetoothAdapterInfo(bluetoothAdapter);
                    if (!getBluetoothProfileProxy(apprtcContext, bluetoothServiceListener, 1)) {
                        LOGGER.e(TAG, "BluetoothAdapter.getProfileProxy(HEADSET) failed");
                    } else {
                        IntentFilter bluetoothHeadsetFilter = new IntentFilter();
                        bluetoothHeadsetFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                        bluetoothHeadsetFilter.addAction("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED");
                        registerReceiver(bluetoothHeadsetReceiver, bluetoothHeadsetFilter);
                        LOGGER.d(TAG, "HEADSET profile state: " + stateToString(bluetoothAdapter.getProfileConnectionState(1)));
                        LOGGER.d(TAG, "Bluetooth proxy for headset profile has started");
                        bluetoothState = QBBluetoothManager.State.HEADSET_UNAVAILABLE;
                        LOGGER.d(TAG, "start done: BT state=" + bluetoothState);
                    }
                }
            }
        }

        void stop() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "stop: BT state=" + bluetoothState);
            if (bluetoothAdapter != null) {
                stopScoAudio();
                if (bluetoothState != QBBluetoothManager.State.UNINITIALIZED) {
                    unregisterReceiver(bluetoothHeadsetReceiver);
                    cancelTimer();
                    if (bluetoothHeadset != null) {
                        bluetoothAdapter.closeProfileProxy(1, bluetoothHeadset);
                        bluetoothHeadset = null;
                    }

                    bluetoothAdapter = null;
                    bluetoothDevice = null;
                    bluetoothState = QBBluetoothManager.State.UNINITIALIZED;
                    LOGGER.d(TAG, "stop done: BT state=" + bluetoothState);
                }
            }
        }

        boolean startScoAudio() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "startSco: BT state=" + bluetoothState + ", attempts: "
                    + scoConnectionAttempts + ", SCO is on: " + isScoOn());

            if (scoConnectionAttempts >= 2) {
                LOGGER.e(TAG, "BT SCO connection fails - no more attempts");
                return false;
            } else if (bluetoothState != QBBluetoothManager.State.HEADSET_AVAILABLE) {
                LOGGER.e(TAG, "BT SCO connection fails - no headset available");
                return false;
            } else {
                LOGGER.d(TAG, "Starting Bluetooth SCO and waits for ACTION_AUDIO_STATE_CHANGED...");
                bluetoothState = QBBluetoothManager.State.SCO_CONNECTING;
                audioManager.startBluetoothSco();
                audioManager.setBluetoothScoOn(true);
                ++scoConnectionAttempts;
                startTimer();
                LOGGER.d(TAG, "startScoAudio done: BT state=" + bluetoothState + ", SCO is on: " + isScoOn());
                return true;
            }
        }

        void stopScoAudio() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "stopScoAudio: BT state=" + bluetoothState + ", SCO is on: " + isScoOn());
            if (bluetoothState == QBBluetoothManager.State.SCO_CONNECTING
                    || bluetoothState == QBBluetoothManager.State.SCO_CONNECTED) {
                cancelTimer();
                audioManager.stopBluetoothSco();
                audioManager.setBluetoothScoOn(false);
                bluetoothState = QBBluetoothManager.State.SCO_DISCONNECTING;
                LOGGER.d(TAG, "stopScoAudio done: BT state=" + bluetoothState + ", SCO is on: " + isScoOn());
            }
        }

        @SuppressLint({"MissingPermission"})
        void updateDevice() {
            if (bluetoothState != QBBluetoothManager.State.UNINITIALIZED && bluetoothHeadset != null) {
                LOGGER.d(TAG, "updateDevice");
                List<BluetoothDevice> devices = bluetoothHeadset.getConnectedDevices();
                if (devices.isEmpty()) {
                    bluetoothDevice = null;
                    bluetoothState = QBBluetoothManager.State.HEADSET_UNAVAILABLE;
                    LOGGER.d(TAG, "No connected bluetooth headset");
                } else {
                    bluetoothDevice = devices.get(0);
                    bluetoothState = QBBluetoothManager.State.HEADSET_AVAILABLE;
                    LOGGER.d(TAG, "Connected bluetooth headset: name=" + bluetoothDevice.getName()
                            + ", state=" + stateToString(bluetoothHeadset.getConnectionState(bluetoothDevice))
                            + ", SCO audio=" + bluetoothHeadset.isAudioConnected(bluetoothDevice));
                }

                LOGGER.d(TAG, "updateDevice done: BT state=" + bluetoothState);
            }
        }

        private AudioManager getAudioManager(Context context) {
            return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        private void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
            apprtcContext.registerReceiver(receiver, filter);
        }

        private void unregisterReceiver(BroadcastReceiver receiver) {
            apprtcContext.unregisterReceiver(receiver);
        }

        private boolean getBluetoothProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile) {
            return bluetoothAdapter.getProfileProxy(context, listener, profile);
        }

        @SuppressLint("WrongConstant")
        private boolean hasPermission(Context context, String permission) {
            return apprtcContext.checkPermission(permission, Process.myPid(), Process.myUid()) == 0;
        }

        @SuppressLint({"MissingPermission"})
        private void logBluetoothAdapterInfo(BluetoothAdapter localAdapter) {
            LOGGER.d(TAG, "BluetoothAdapter: enabled=" + localAdapter.isEnabled() + ", state="
                    + stateToString(localAdapter.getState()) + ", name=" + localAdapter.getName()
                    + ", address=" + localAdapter.getAddress());

            Set<BluetoothDevice> pairedDevices = localAdapter.getBondedDevices();
            if (!pairedDevices.isEmpty()) {
                LOGGER.d(TAG, "paired devices:");

                for (BluetoothDevice device : pairedDevices) {
                    LOGGER.d(TAG, " name=" + device.getName() + ", address=" + device.getAddress());
                }
            }
        }

        private void updateAudioDeviceState() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "updateAudioDeviceState");
            apprtcAudioManager.updateAudioDeviceState();
        }

        private void startTimer() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "startTimer");
            handler.postDelayed(bluetoothTimeoutRunnable, 4000L);
        }

        private void cancelTimer() {
            ThreadUtils.checkIsOnMainThread();
            LOGGER.d(TAG, "cancelTimer");
            handler.removeCallbacks(bluetoothTimeoutRunnable);
        }

        @SuppressLint({"MissingPermission"})
        private void bluetoothTimeout() {
            ThreadUtils.checkIsOnMainThread();
            if (bluetoothState != QBBluetoothManager.State.UNINITIALIZED && bluetoothHeadset != null) {
                LOGGER.d(TAG, "bluetoothTimeout: BT state=" + bluetoothState + ", attempts: "
                        + scoConnectionAttempts + ", SCO is on: " + isScoOn());

                if (bluetoothState == QBBluetoothManager.State.SCO_CONNECTING) {
                    boolean scoConnected = false;
                    List<BluetoothDevice> devices = bluetoothHeadset.getConnectedDevices();
                    if (devices.size() > 0) {
                        bluetoothDevice = devices.get(0);
                        if (bluetoothHeadset.isAudioConnected(bluetoothDevice)) {
                            LOGGER.d(TAG, "SCO connected with " + bluetoothDevice.getName());
                            scoConnected = true;
                        } else {
                            LOGGER.d(TAG, "SCO is not connected with " + bluetoothDevice.getName());
                        }
                    }

                    if (scoConnected) {
                        bluetoothState = QBBluetoothManager.State.SCO_CONNECTED;
                        scoConnectionAttempts = 0;
                    } else {
                        LOGGER.w(TAG, "BT failed to connect after timeout");
                        stopScoAudio();
                    }

                    updateAudioDeviceState();
                    LOGGER.d(TAG, "bluetoothTimeout done: BT state=" + bluetoothState);
                }
            }
        }

        private boolean isScoOn() {
            return audioManager.isBluetoothScoOn();
        }

        private String stateToString(int state) {
            switch (state) {
                case 0:
                    return "DISCONNECTED";
                case 1:
                    return "CONNECTING";
                case 2:
                    return "CONNECTED";
                case 3:
                    return "DISCONNECTING";
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                default:
                    return "INVALID";
                case 10:
                    return "OFF";
                case 11:
                    return "TURNING_ON";
                case 12:
                    return "ON";
                case 13:
                    return "TURNING_OFF";
            }
        }

        private class QBBluetoothHeadsetBroadcastReceiver extends BroadcastReceiver {
            private QBBluetoothHeadsetBroadcastReceiver() {
            }

            public void onReceive(Context context, Intent intent) {
                if (bluetoothState != QBBluetoothManager.State.UNINITIALIZED) {
                    String action = intent.getAction();
                    int state;
                    if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                        state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                        LOGGER.d(TAG, "QBBluetoothHeadsetBroadcastReceiver.onReceive: a=ACTION_CONNECTION_STATE_CHANGED, s="
                                + stateToString(state) + ", sb=" + isInitialStickyBroadcast() + ", BT state: " + bluetoothState);

                        if (state == 2) {
                            scoConnectionAttempts = 0;
                            updateAudioDeviceState();
                        } else if (state == 0) {
                            stopScoAudio();
                            updateAudioDeviceState();
                        }

                    } else if (action.equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
                        state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 10);
                        LOGGER.d(TAG, "QBBluetoothHeadsetBroadcastReceiver.onReceive: a=ACTION_AUDIO_STATE_CHANGED, s="
                                + stateToString(state) + ", sb=" + isInitialStickyBroadcast()
                                + ", BT state: " + bluetoothState);

                        if (state == 12) {
                            cancelTimer();
                            if (bluetoothState == QBBluetoothManager.State.SCO_CONNECTING) {
                                LOGGER.d(TAG, "+++ Bluetooth audio SCO is now connected");
                                bluetoothState = QBBluetoothManager.State.SCO_CONNECTED;
                                scoConnectionAttempts = 0;
                                updateAudioDeviceState();
                            } else {
                                LOGGER.w(TAG, "Unexpected state BluetoothHeadset.STATE_AUDIO_CONNECTED");
                            }
                        } else if (state == 11) {
                            LOGGER.d(TAG, "+++ Bluetooth audio SCO is now connecting...");
                        } else if (state == 10) {
                            LOGGER.d(TAG, "+++ Bluetooth audio SCO is now disconnected");
                            if (isInitialStickyBroadcast()) {
                                LOGGER.d(TAG, "Ignore STATE_AUDIO_DISCONNECTED initial sticky broadcast.");
                                return;
                            }

                            updateAudioDeviceState();
                        }
                    }

                    LOGGER.d(TAG, "onReceive done: BT state=" + bluetoothState);
                }
            }
        }

        private class QBBluetoothServiceListener implements BluetoothProfile.ServiceListener {
            private QBBluetoothServiceListener() {
            }

            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == 1 && bluetoothState != QBBluetoothManager.State.UNINITIALIZED) {
                    LOGGER.d(TAG, "QBBluetoothServiceListener.onServiceConnected: BT state=" + bluetoothState);
                    bluetoothHeadset = (BluetoothHeadset) proxy;
                    updateAudioDeviceState();
                    LOGGER.d(TAG, "onServiceConnected done: BT state=" + bluetoothState);
                }
            }

            public void onServiceDisconnected(int profile) {
                if (profile == 1 && bluetoothState != QBBluetoothManager.State.UNINITIALIZED) {
                    LOGGER.d(TAG, "QBBluetoothServiceListener.onServiceDisconnected: BT state=" + bluetoothState);
                    stopScoAudio();
                    bluetoothHeadset = null;
                    bluetoothDevice = null;
                    bluetoothState = QBBluetoothManager.State.HEADSET_UNAVAILABLE;
                    updateAudioDeviceState();
                    LOGGER.d(TAG, "onServiceDisconnected done: BT state=" + bluetoothState);
                }
            }
        }

        public enum State {
            UNINITIALIZED,
            ERROR,
            HEADSET_UNAVAILABLE,
            HEADSET_AVAILABLE,
            SCO_DISCONNECTING,
            SCO_CONNECTING,
            SCO_CONNECTED;

            private State() {
            }
        }
    }

    static class QBAudioUtils {
        private QBAudioUtils() {
        }

        public static void assertIsTrue(boolean condition) {
            if (!condition) {
                throw new AssertionError("Expected condition to be true");
            }
        }

        public static String getThreadInfo() {
            return "@[name=" + Thread.currentThread().getName() + ", id=" + Thread.currentThread().getId() + "]";
        }

        public static void logDeviceInfo(String tag) {
            Log.d(tag, "Android SDK: " + Build.VERSION.SDK_INT + ", Release: " + Build.VERSION.RELEASE + ", " +
                    "Brand: " + Build.BRAND + ", Device: " + Build.DEVICE + ", Id: " + Build.ID + ", " +
                    "Hardware: " + Build.HARDWARE + ", Manufacturer: " + Build.MANUFACTURER + ", " +
                    "Model: " + Build.MODEL + ", Product: " + Build.PRODUCT);
        }

        public static class NonThreadSafe {
            private final Long threadId = Thread.currentThread().getId();

            public NonThreadSafe() {
            }

            public boolean calledOnValidThread() {
                return threadId.equals(Thread.currentThread().getId());
            }
        }
    }
}