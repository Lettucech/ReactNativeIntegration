if [ ! -d "./build" ]; then mkdir ./build; fi
if [ ! -d "./build/android" ]; then mkdir ./build/android; fi
react-native ram-bundle --entry-file ./index.js --bundle-output ./build/android/index.android.bundle --platform android