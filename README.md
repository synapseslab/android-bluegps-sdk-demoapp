# BlueGPS SDK Examples for Android
Official Android Demo App showcases the BlueGPS SDK features and acts as reference implementation for many of the basic SDK features.
Getting started requires you setup a **license**.


## Installation and Getting Started

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

## Sample App

To run the sample app, start by cloning this repo:

 ```shell
git clone git@github.com:synapseslab/android-bluegps-sdk-demoapp.git
```

## Supported features

- Map navigation and interaction
- Search objects
- IOT Controllable elements
- JWT Login
- Keycloak Authentication
- SSE Notify region changes
- SSE Notify position changes
- SSE Diagnostic Tag