# BlueGPS SDK Examples for Android
Official Android Demo App showcases the BlueGPS SDK features and acts as reference implementation for many of the basic SDK features.
Getting started requires you setup a **license**.


## Installation

The BlueGPS Android SDK is distributed through Jitpack. It can be integrated through gradle, maven as following:

### Gradle

Before you add BlueGPS depencencies, add it in your `settings.gradle` at the end of repositories

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Or if you're using an older project setup, add it in your `build.gradle` at the end of repositories

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then add the dependency for BlueGPS-SDK in the `build.gradle` file for your app or module:

```gradle
dependencies {
    implementation 'com.github.synapseslab:android-bluegps-sdk-demoapp:<version>'
}
```

The `version` corresponds to release version, for example:

```gradle
dependencies {
    implementation 'com.github.synapseslab:android-bluegps-sdk-demoapp:1.4.2-rc4'
}
```

### Maven

Add the JitPack repository to your build file

```maven
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the dependency

```maven
dependency>
    <groupId>com.github.synapseslab</groupId>
    <artifactId>android-bluegps-sdk-demoapp</artifactId>
    <version>Tag</version>
</dependency>
```

The `Tag` corresponds to release version, for example: `1.4.2-rc4`

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

    private val SDK_ENDPOINT = "{{provided-bluegps-endpoint}}"
    private val SDK_KEY = "{{provided-sdk-key}}"
    private val SDK_SECRET = "{{provided-sdk-secret}}"
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

[LICENSE](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/LICENSE.md)
