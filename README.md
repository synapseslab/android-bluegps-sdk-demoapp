# BlueGPS SDK Examples for Android
Official Android Demo App showcases the BlueGPS SDK features and acts as reference implementation for many of the basic SDK features.
Getting started requires you setup a **license**.


## Installation

Before you add BlueGPS depencencies, update your repositories in the `settings.gradle` file to include these two repositories

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url "https://maven.pkg.github.com/synapseslab/android-bluegps-sdk" }
    }
}
```

Or if you're using an older project setup, add these repositories  in your project level `build.gradle` file:

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url "https://maven.pkg.github.com/synapseslab/android-bluegps-sdk" }
    }
}
```

Then add the dependency for BlueGPS-SDK in the `build.gradle` file for your app or module:

```gradle
dependencies {
    implementation 'com.synapseslab:android-bluegps-sdk:X.Y.Z'
}
```

## Getting Started

Your first step is initializing the BlueGPSLib, which is the main entry point for all operations in the library. BlueGPSLib is a singleton: you'll create it once and re-use it across your application.

A best practice is to initialize BlueGPSLib in the Application class:

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        BlueGPSLib.instance.initSDK(
            sdkEnvironment = Environment.sdkEnvironment,
            context = applicationContext,
            enabledNetworkLogs = true
        )
    }
}
```

The BlueGSP-SDK use an `Environment` where integrator have to put SDK data for register the SDK and for create a communication with the BlueGPS Server [see the demo app for detail](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/utils/Environment.kt). The management of the environment is demanded to the app.

## Sample App

To run the sample app, start by cloning this repo:

 ```shell
git clone git@github.com:synapseslab/android-bluegps-sdk-demoapp.git
```

and play with it.

## Examples

- [Map navigation and interaction](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/map/MapActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#5-bluegpsmapview)
- [Search objects](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/search_object/SearchObjectsActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#8-search-object-api)
- [IOT Controllable elements](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/controllable_elements/ControllableElementsActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#9-controllable-items-api)
- [JWT Login](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/login/MainActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#63-notify-position-changes)
- [Keycloak Authentication](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/keycloak/KeycloakActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#12-oauth-client-for-keycloak-authentication)
- [Area API](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/area/AreaActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#10-area-api)
- [BlueGPS Advertising Service](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/login/MainActivity.kt#L62) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#4-use-bluegps-advertising-service)
- [SSE Notify region changes](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/sse/NotifyRegionActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#62-notify-region-changes)
- [SSE Notify position changes](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/sse/NotifyPositionActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#63-notify-position-changes)
- [SSE Diagnostic Tag](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/sse/DiagnosticTagActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#61-diagnostic-sse)

## SDK Changelog

[https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/CHANGELOG.md](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/CHANGELOG.md)

## Documentation

[https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md)


## License

```
Copyright (c) 2020-2023 Synapses S.r.l.s. All rights reserved.

Licensed under......

```
