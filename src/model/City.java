/* 
 * 
 * 
 * 
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author Jordan Durant
 */
public class City {
    
    private int cityId;
    private String city;
    private int countryId;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String LastUpdateBy;

    public City(int cityId, String city, int coundtryId, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String LastUpdateBy) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = coundtryId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.LastUpdateBy = LastUpdateBy;
    }

    public City(int cityId, String city, int countryId) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int coundtryId) {
        this.countryId = coundtryId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return LastUpdateBy;
    }

    public void setLastUpdateBy(String LastUpdateBy) {
        this.LastUpdateBy = LastUpdateBy;
    }
    
}
