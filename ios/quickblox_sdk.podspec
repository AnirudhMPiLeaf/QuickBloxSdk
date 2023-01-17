#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html
#
Pod::Spec.new do |s|
  s.name             = 'quickblox_sdk'
  s.version          = '0.0.1-alpha'
  s.summary          = 'A new flutter plugin project.'
  s.description      = <<-DESC
A new flutter plugin project.
                       DESC
  s.homepage         = 'https://quickblox.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { "Injoit LTD" => "sales@quickblox.com" }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  
  s.dependency 'QuickBlox', '~> 2.18'
  s.dependency 'Quickblox-WebRTC', '~> 2.7'

  s.ios.deployment_target = '12.0'
end

