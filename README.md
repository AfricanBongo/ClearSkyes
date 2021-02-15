# :sunny: ClearSkyes
## An UI-rich Android weather app

<br/>
<br/>

<p align="center">
  <img src="https://github.com/AfricanBongo/ClearSkyes/blob/master/github-markdown/drawer_open.png" width="360" height="680"/>
</p>


## Feature Overview 
- Intuitive and neatly stacked UI, implementing some ```Material Design``` guidelines
- [View current weather conditions](#viewing-current-weather),
- Weather forecasts up to ```10``` days,
- ```Search```, ```add``` and ```delete``` a location to view its weather details, and also cast it as the favourite location,
- Receive daily weather notifications (for the favourite location),
- Apply preferences through settings,
- Send feedback

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
- ```Favourite``` : The location in the list can be clicked in order to enable it as the favourite location, where background colour changeds to yellow. What this means is that it's initially open the app opens, and this is the location used in delivering daily notifications.

<p>
Credit to:
</p>

- [Glide](https://github.com/bumptech/glide)
- [WeatherAPI](https://www.weatherapi.com)
- [FapiMail](https://rapidapi.com/fapi/api/fapimail)
