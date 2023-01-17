package com.quickblox.quickblox_sdk.file;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

///Created by Injoit on 2019-12-27.
///Copyright © 2019 Quickblox. All rights reserved.
class FileConstants {

    private FileConstants() {
        //empty
    }

    ///////////////////////////////////////////////////////////////////////////
    // UPLOAD PROGRESS
    ///////////////////////////////////////////////////////////////////////////
    @StringDef({
            UploadProgress.FILE_UPLOAD_PROGRESS
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface UploadProgress {
        String FILE_UPLOAD_PROGRESS = FileModule.CHANNEL_NAME + "/FILE_UPLOAD_PROGRESS";
    }
}