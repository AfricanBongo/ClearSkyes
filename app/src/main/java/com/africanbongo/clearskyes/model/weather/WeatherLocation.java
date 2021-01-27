package com.africanbongo.clearskyes.model.weather;

/**
 * A class that represents the data of a certain geolocation, that is:
 *
 * <ul>
 *     <li>URL location (used in forming the url to get the location)</li>
 *     <li>City</li>
 *     <li>Country</li>
 *     <li>Country code, eg. ZW or US</li>
 *     <li>Region, such as provinces or states</li>
 * </ul>
 */
public class WeatherLocation {

    public static final String SEPARATOR = ", ";
    private final String urlLocation;
    private final String city;
    private final String country;
    private final String countryCode;
    private final String region;

    public WeatherLocation(String urlLocation, String city, String country, String countryCode, String region) {
        this.urlLocation = urlLocation;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        this.region = region;
    }

    public String getUrlLocation() {
        return urlLocation;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {return countryCode;}

    public String getRegion() {
        return region;
    }
}
