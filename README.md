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
        maven { url "https://maven.pkg.github.com/synapseslab/android-bluegps-sdk-demoapp" }
    }
}
```

Or if you're using an older project setup, add these repositories  in your project level `build.gradle` file:

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url "https://maven.pkg.github.com/synapseslab/android-bluegps-sdk-demoapp" }
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

```kotlin
object Environment {

    private val SDK_ENDPOINT = "{{bluegps-provided-endpoint}}"
    private val SDK_KEY = "{{sdk-provided-key}}"
    private val SDK_SECRET = "{{sdk-provided-secret}}"
    private val APP_ID = "com.synapseslab.demosdk"

    val sdkEnvironment = SdkEnvironment(
        sdkEndpoint = SDK_ENDPOINT,
        appId = APP_ID,
        sdkKey = SDK_KEY,
        sdkSecret = SDK_SECRET,
    )
}
```

## Sample App

To run the sample app, start by cloning this repo:

 ```shell
git clone git@github.com:synapseslab/android-bluegps-sdk-demoapp.git
```

and play with it.

## Examples

#### Authentication

- [JWT Login](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/login/MainActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#63-notify-position-changes)

BlueGPS_SDK provides a client for manage authentication and authorization inside your app.


- [Keycloak Authentication](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/keycloak/KeycloakActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#12-oauth-client-for-keycloak-authentication)

#### BlueGPS MapView
- [Map navigation and interaction](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/map/MapActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#5-bluegpsmapview)

#### Search objects
BlueGPSSDK provides some built-in capabilities to search for resources and objects within the backend.
- [Search objects](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/search_object/SearchObjectsActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#8-search-object-api)

#### Controllable items API
BlueGPSSDK provides a logic to interact with controllable items exposed by the backend. Controllable items could be anything that can be remote controlled by the application.

- [IOT Controllable elements](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/controllable_elements/ControllableElementsActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#9-controllable-items-api)

#### Area API
BlueGPSSDK provides some built-in capabilities for rooms and areas.


- [Area API](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/area/AreaActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#10-area-api)

#### Use BlueGPS Advertising Service
- [BlueGPS Advertising Service](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/login/MainActivity.kt#L62) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#4-use-bluegps-advertising-service)

#### Server Sent Events
The purpose of diagnostic API is to give an indication to the integrator of the status of the BlueGPS system.


- [SSE Notify region changes](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/sse/NotifyRegionActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#62-notify-region-changes)
- [SSE Notify position changes](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/sse/NotifyPositionActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#63-notify-position-changes)
- [SSE Diagnostic Tag](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/demo-app/app/src/main/java/com/synapseslab/bluegpssdkdemo/sse/DiagnosticTagActivity.kt) - [(documentation)](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#61-diagnostic-sse)

## SDK Changelog

[Changelog link](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/CHANGELOG.md)

## Documentation
Let's see how you can get started with the Android BlueGPS SDK after adding the required dependencies.

[Documentation link](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md)


## License

```
Copyright (c) 2020-2023 Synapses S.r.l.s. All rights reserved.

Licensed under......

```
