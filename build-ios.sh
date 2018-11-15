if [ ! -d "./build" ]; then mkdir ./build; fi
if [ ! -d "./build/ios" ]; then mkdir ./build/ios; fi
react-native ram-bundle --entry-file ./index.js --bundle-output ./build/ios/index.ios.bundle