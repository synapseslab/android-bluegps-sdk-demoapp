# December 13, 2023 - 1.5.2
## android-bluegps-sdk

### 🐞 Fixed
- Fix on BlueGPSAuthManager class
- On Keycloak class some params now are optional
### ⬆️ Improved
### ✅ Added
- Add Marker API
  - activatePositionMarker() to activate/deactivate position marker layer to the map
  - clearPositionMarker() clear and remove position marker
  - getPositionMarker() retrive position marker
### ⚠️ Changed

# November 10, 2023 - 1.5.1
## android-bluegps-sdk

### 🐞 Fixed
### ⬆️ Improved
### ✅ Added
### ⚠️ Changed
- Changed `startNotifyEventChanges()`. Now the function receive a `streamType`, 
`outputEvents` a list of events to be notified for the specific types of stream and a 
`tagIdList` a list of tag id to monitoring. If empty receive notifications for all tags.

# November 8, 2023 - 1.5.0
## android-bluegps-sdk

### 🐞 Fixed
- Fixed registerReceiver
### ⬆️ Improved
### ✅ Added
- Add `setCompass(deg: Double)` on MapView
- Add `followUserTag` callback on MapView
- Add `FollowUserTag` class
- Add `nearJump` and `nearDestination` callback on MapView
- Add Notifications API
  - `allNotifications` Returns all available notifications
  - `countNotifications` Returns the number of notifications to read
  - `deleteNotifications` Delete the notifications
  - `updateNotifications` For update the status of the notifications
  - `registerDeviceForNotifications` For register the device and the push token for receive notifications
- Add SSE for generic events
- Add onStop callback on all SSE API
### ⚠️ Changed
- Changed BlueGPSAuthManager, removed SCHEDULE_ALARM permission
- Removed BlueGPSAlarmManager

# October 4, 2023 - 1.4.10
## android-bluegps-sdk

### 🐞 Fixed
### ⬆️ Improved
### ✅ Added
### ⚠️ Changed
- `getDeviceConfiguration(tagId: String? = null)` API now has `tagId` as an optional property

# October 2, 2023 - 1.4.9
## android-bluegps-sdk

### 🐞 Fixed
### ⬆️ Improved
### ✅ Added
- Add `tagid` property to `AdvDeviceConfiguration` class
### ⚠️ Changed

# September 21, 2023 - 1.4.8
## android-bluegps-sdk
### 🐞 Fixed
- Fix `startNotifyRegionChanges()` function
### ⬆️ Improved
- Change `getRegionListInWhichIsContained()` function that in some cases return a not valid id or a null value
### ✅ Added
### ⚠️ Changed

# August 29, 2023 - 1.4.7
## android-bluegps-sdk
### 🐞 Fixed
- Fix `startNotifyRegionChanges()` function
### ⬆️ Improved
### ✅ Added
### ⚠️ Changed

# August 10, 2023 - 1.4.6
## android-bluegps-sdk
### 🐞 Fixed
### ⬆️ Improved
### ✅ Added
- Add Ticket API
  - `getTicketTypes()` Returns the types for tickets ("INTERNAL", "IVIVA")
  - `getTicketMy()` Return all tickets for the logged user
  - `getTicketFormManager()` Return the entire form that the interface will have to build
  - `saveTicket()` save a ticket
  - `getTicketById()` Return the ticket detail
  - `deleteTicket()` Delete a ticket
- Add `getUUID()` function that return the UUID if set, null otherwise
### ⚠️ Changed

# July 27, 2023 - 1.4.5
## android-bluegps-sdk
### 🐞 Fixed
- Fix on `silentLogout()`
### ⬆️ Improved
### ✅ Added
- Do not disturb and Out of Office API
  - Add `getDndOooDayTime()` to get the filter to set a do not disturb or out of office element
  - Add `setDndOoo()` to set to do not disturb or out of office and element
- Add navigation resource API `getNavigationResource()`
- Add `authError` and `pathRecalculation` callback on MapView
- Add `initAuth` function on MapView
- Add `getUserProfile()` API that return all associated profiles to the logged user
- Add `getBuildingList()` API that return a list of Buildings
- Add `buildings: List<Int>` param to `ConfigurationMap` class to load the maps of the selected building
### ⚠️ Changed

# May 23, 2023 - 1.4.4
For a sync problem there is a jump version from 1.4.2 to 1.4.4
## android-bluegps-sdk
### 🐞 Fixed
- Minor fix on BlueGPSAuthManager if `useOAuthAuthentication` attribute is `true`
### ⬆️ Improved
### ✅ Added
- Language API section
  - Add function for get all available dictionaries `getLanguages()`
  - Add function for get a dictionary for a language code `getLanguage()`
- Search Object API section
  - Add a new function `getSearchableTrackTag()` to get a searchable track tag list filtering also by NFC code [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#86-getsearchabletracktag)
### ⚠️ Changed

# April 26, 2023 - 1.4.2
## android-bluegps-sdk
### 🐞 Fixed
- Fix on `startNotifyRegionChanges()` that now return a map that contains a list regions where the tags are currently located. [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#62-notify-region-changes)
- Fix on `deviceConfiguration()` save the UUID in shared preferences.
### ⬆️ Improved
### ✅ Added
- Booking API Section 
  - Add function `getAgendaNextMy()`
  - Add function `scheduleCheck()`
  - Add function `deleteSchedule()`
- Add Home API Section
  - Add function `getHomeMy()`
- Add Locker API section
  - Add function `unlockLocker()`
  - Add function `releaseLocker()`
- Search API Section
  - Add function `getFilterResource()`
### ⚠️ Changed

# March 7, 2023 - 1.4.1
## android-bluegps-sdk
### 🐞 Fixed
### ⬆️ Improved
### ✅ Added
- Add `BlueGPSLocationManager` to start and stop the system location services.
### ⚠️ Changed
- Changed `floorLevel` and `floorLevelPercentageConfidence` to optional attributes.

# February 20, 2023 - 1.4.0-alpha05
## android-bluegps-sdk
### ✅ Added
- Add function `getFilter()` [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#86-getfilter)
- Add function `search()` [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#87-search)
- Add Booking API
  - Add function `getAgendaDay()` [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#111-getagendaday)
  - Add function `getAgendaMy()` [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#112-getagendamy)
  - Add function `schedule()` [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#114-schedule)
  - Add function `agendaFind()` [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#113-agendafind)
- Add OAuth client for keycloak authentication [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#12-oauth-client-for-keycloak-authentication)
- Add notify position changes [documentation](https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/documentation/bluegps_android_sdk.md#63-notify-position-changes)
### ⚠️ Changed
- Add `tagid` attribute to `BGPGpsPosition` class


# February 02, 2023 - 1.3.1
## android-bluegps-sdk
### ✅ Added
- Add function `getAreasList()`
- Add function `getAreaListRealtimeElement()`


# November 25, 2022 - 1.2.0
## android-bluegps-sdk
### ✅ Added
- Add support for Rooms and Areas
- Add function `getRoomsCoordinates()`
- Add function `getMapsList()`
- Add function `getAreasWithTagsInside()`


# November 11, 2022 - 1.1.3
## android-bluegps-sdk
### ✅ Added
- Add notify region changes
- Add function `setDarkMode(darkMode: Boolean)`

# October 27, 2022 - 1.1.1
## android-bluegps-sdk
### ✅ Added
- Add support for Controllable items API
- Add function `initAllBookingLayerBy(bookFilter)`


# October 12, 2022 - 1.1.0
## android-bluegps-sdk
### ✅ Added
- Change Blue GPS SDK name from `bluegps_sdk-release-1.0.3.aar` to `bluegps-sdk-release-1.1.0.aar` 
check the example app (**breakpoint!!**)
- Update gradle libraries
- updated target Sdk Version to 33 (Android 13)
- updated compiled sdk version to 33 (Android 13)
- Add support for SearchObject


# December 01, 2021 - 1.0.3
## android-bluegps-sdk
### ✅ Added
- Changed the name of the BlueGPS_SDK lib to `bluegps_sdk-release-1.0.3`
- Add new callback `initSDKCompleted`
- Add new network call to `findResources()`

### ⚠️ Changed
- Changed the home screen example `HomeActivity.kt`
- Removed deprecated plugin `kotlin-android-extensions` on demo app


# October 29, 2021 - 1.0.2-alpha
## android-bluegps-sdk
### ✅ Added
- Add `changeFloor` parameter
- Add Server Sent Events diagnostic
- Add function `centerToRoom(roomId)`
- Add function `centerToPosition(mapPosition, zoom)`
- Add new callback `roomEnter` 
- Add new callback `roomExit` 
- Add new callback `floorChange`
### ⚠️ Changed
- Removed callback `roomClick`
- `PaylodadResponse` class is deprecated. Use the new `GenericInfo` class
  for `TypeMapCallback.SUCCESS` and `TypeMapCallback.ERROR`
- Changed the return type of `TypeMapCallback.PARK_CONF` callback from `PaylodResponse`
  to `BookingConfiguration`
- Removed the callback `TypeMapCallback.INIT_SDK_END` now the init sdk event is managed
  on `TypeMapCallback.SUCCESS` for success or in `TypeMapCallback.ERROR` otherwise


# October 14, 2021 - 1.0.1
## android-bluegps-sdk
### ✅ Added
- Add function `loadGenericResource(search, type, subtype)`
- Add function `selectPoi(poi)`
- Add function `selectPoiById(poiId)`
- Add function `drawPin(position, icon)` 
- Add function `getCurrentFloor()`
- Add new callback `resource` 
- Add new callback `tagVisibility`


# October 16, 2021 - 1.0.0
## android-bluegps-sdk
### ✅ Added
- updated target Sdk Version to 31 (Android 12)
- updated compiled sdk version to 31 (Android 12)
- add `BLUETOOTH_ADVERTISE` and `BLUETOOTH_CONNECT` runtime permissions for Android 12 support
- Add function `removeNavigation()` 
- Add new callback `navigation stats`
- Add new callback `navigation info`
- Add new callback `success` info 
- Add new callback `error` info
- Add show park and desk on conf object
- Add new method on MapView SDK (getStyle(), setStyle(), setStartBookingDate(), setBookingDate())
- Add Map Web View Component
- Add map view interaction
- SDK Init
- Guest Authentication
- JWT Authentication
- Advertising
### ⚠️ Changed
- removed `androidx.security:security-crypto` library
- Update [5. BlueGPSMapView](#5-bluegpsmapview) section with a configuration for navigation
- Update the Path model
- Update the callback from web view click (room click, map click, tag click)
- Change initSDK method