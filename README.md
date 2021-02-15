# :sunny: ClearSkyes
## An Android weather app with rich and sleek UI

<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/drawer_open.png" width="360" height="680"/>
</p>


## Feature Overview 
- Intuitive and neatly stacked UI, implementing some ```Material Design``` guidelines
- [View current weather conditions](#viewing-current-weather),
-  [Weather forecasts](#weather-forecasts) up to ```10``` days,
- [```Search```, ```add``` and ```delete```](#search-add-delete-favourite-a-location) a location to view its weather details, and also cast it as the favourite location,
- Receive daily weather [notifications](#daily-notifications) (for the favourite location),
- Send [feedback](#sending-feedback),
- Apply preferences through [settings](#settings).
- Error handling in case of [server error](#server-error) or [network loss](#network-loss-failure)

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/device-2021-02-15-142648.png" width="360" height="680" />
</p>

## Viewing current weather
A ```Volley``` RequestQueue is used to fetch data from the ```WeatherAPI``` and is loaded into the views.
Astronomical data is loaded into the bottom view, e.g. ```Sunrise```. The hours of the day are loaded as a fragment over the astronomical view.

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/Animated%20GIF-downsized_large.gif" height="500"/>
</p>

## Weather forecasts
The app provides up to ```10``` days of weather forecasting, the number of forecast days to show may be tweaked from the settings page of the app.
The app allows navigation from one forecast day to another through a ```ViewPager2``` or attached ```TabLayout```. The weather data may be refreshed using the reload button.

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/Animated%20GIF-downsized_large%20(1).gif" width="360" height="600" />
</p>

## ```Search```, ```add```, ```delete```, ```favourite``` a location
- ```Search``` and ```Add``` : ```Search / Autocomplete``` complete feature of the ```WeatherAPI``` is utilized in finding new locations to add to the location manager.  
- ```Delete``` : A location can also deleted, and if that directive is issued a ```SnackBar``` is displayed to the user with an ```Undo``` prompt if the location should be restored into the list.
- ```Favourite``` : The location in the list can be clicked in order to enable it as the favourite location, where background colour changeds to yellow. What this means is that it's initially open the app opens, and this is the location used in delivering daily [notifications](#daily-notifications).

<br/>
<br/>
<br/>
<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/Animated%20GIF-downsized_large%20(2).gif" width="360" height="600" />
</p>

## Daily notifications
Daily notifications are sent to user as a user-defined or default time, notifications can be turned on or off in the settings page. Notification displays the minimal weather information and clicking on it opens the app and displays the current weather information.

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/Animated%20GIF-downsized_large%20(3).gif" width="360" height="600" />
</p>

## Sending feedback
The app allows users to send feedback of their own review. The feedbacks are sent successfully using the ```FapiMail``` API and a ```Toast``` is sent to the user notifying them of the sent feedback.

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/settings.png" width="360" height="680" />
</p>

## Settings
- ```Notification``` : Turns notifications ```on``` or ```off```, and also set the delivery time
- ```Degree``` : Set to either ```celcius``` or ```fahrenheit```,
- ```Measurements``` : Use either ```metric``` or ```imperial``` measurement system,
- ```Forecast``` : Set the number of forecast days to show for the weather forecast, with a max of ```10``` and min of ```1```
- ```Refresh data cache``` : Refreshes the data cache after a certain time interval

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/Animated%20GIF-downsized_large%20(4).gif" width="360" height="600" />
</p>

## Network loss/failure
In the event of a network failure a custom extension of the Volley library's ```ErrorListener``` is implemented. If the reload button is clicked an attempt to fetch the data is made again.

<br/>
<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/Animated%20GIF-downsized_large%20(5).gif" width="360" height="600" />
</p>

## Server error
If the request to the server returns an error code, the error screen is shown and the reload button is clicked as the day is set back to the current one and an attempt to fetch the data is made again.

<br/>
## New things learnt
- Animations
- Creating custom asynchoronous classes
- DialogFragments
- Preferences
- TabLayout
- BroadcastReceivers and Services
- NavigationDrawer
- Custom Views
- Notifications
- JavaDocs

### Credit to:

- [WeatherAPI](https://www.weatherapi.com), providing the essential weather data to run the app,
- [Glide](https://github.com/bumptech/glide), loading images into the app,
- [Flaticon](https://www.flaticon.com/), for using base weather icons which I further customized,
- [FapiMail](https://rapidapi.com/fapi/api/fapimail), sending feedback to developer email,
- [Mock Video](https://www.mock.video/) and [Giphy](https://giphy.com/), in using their tools to create demonstration gifs.
