package com.africanbongo.clearskyes.model;

import com.africanbongo.clearskyes.util.WeatherLocationUtil;

import java.util.Objects;

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

    /**
     * <p>
     *     A {@link String} representing the information about the location in short,
     *     consisting of the city name and country code.
     * </p>
     * @return {@link String} representing the information about location in short, e.g. Harare, ZW.
     */
    public String getShortStringLocation() {
        return getCity() + WeatherLocationUtil.SEPARATOR + getCountryCode();
    }

    /**
     * <p>
     *     A {@link String} representing the information about the location in full.
     * </p>
     * <p>
     *     If region is returned from api it returns e.g. Harare, Mashonaland West, Zimbabwe
     * </p>
     * <p>
     *     If region is not returned from api it returns e.g. Harare, Zimbabwe
     * </p>
     * @return {@link String} representing the information about location in full.
     */
    public String getLongStringLocation() {
        if (region.equals(WeatherLocationUtil.NOT_APPLICABLE)) {
            return city + WeatherLocationUtil.SEPARATOR + country;
        }

        return city + WeatherLocationUtil.SEPARATOR + region + WeatherLocationUtil.SEPARATOR + country;
    }

    /**
     * <p>
     * A {@link String} representing the information about the location in standard form.
     * </p>
     *<p>
     * If region is returned from api it returns e.g. Mashonaland West, Zimbabwe
     * </p>
     * <p>
     * If region is not returned from api it returns e.g. Zimbabwe
     * </p>
     * @return {@link String} representing the information about location in standard form.
     */
    public String getStandardStringLocation() {
        if (region.equals(WeatherLocationUtil.NOT_APPLICABLE)) {
            return country;
        }

        return region + WeatherLocationUtil.SEPARATOR + country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherLocation)) return false;
        WeatherLocation location = (WeatherLocation) o;
        return Objects.equals(urlLocation, location.urlLocation) &&
                Objects.equals(city, location.city) &&
                Objects.equals(country, location.country) &&
                Objects.equals(countryCode, location.countryCode) &&
                Objects.equals(region, location.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlLocation, city, country, countryCode, region);
    }
}
