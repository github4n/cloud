package com.iot.space.domain;

import java.io.Serializable;

public class SpaceDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.id
     *
     * @mbggenerated
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.create_time
     *
     * @mbggenerated
     */
    private Long createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.last_update_date
     *
     * @mbggenerated
     */
    private Long lastUpdateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.device_id
     *
     * @mbggenerated
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.space_id
     *
     * @mbggenerated
     */
    private String spaceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.location_id
     *
     * @mbggenerated
     */
    private String locationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column space_device.device_category_id
     *
     * @mbggenerated
     */
    private String deviceCategoryId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.id
     *
     * @return the value of space_device.id
     * @mbggenerated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.id
     *
     * @param id the value for space_device.id
     * @mbggenerated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.create_time
     *
     * @return the value of space_device.create_time
     * @mbggenerated
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.create_time
     *
     * @param createTime the value for space_device.create_time
     * @mbggenerated
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.last_update_date
     *
     * @return the value of space_device.last_update_date
     * @mbggenerated
     */
    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.last_update_date
     *
     * @param lastUpdateDate the value for space_device.last_update_date
     * @mbggenerated
     */
    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.device_id
     *
     * @return the value of space_device.device_id
     * @mbggenerated
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.device_id
     *
     * @param deviceId the value for space_device.device_id
     * @mbggenerated
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.space_id
     *
     * @return the value of space_device.space_id
     * @mbggenerated
     */
    public String getSpaceId() {
        return spaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.space_id
     *
     * @param spaceId the value for space_device.space_id
     * @mbggenerated
     */
    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId == null ? null : spaceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.location_id
     *
     * @return the value of space_device.location_id
     * @mbggenerated
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.location_id
     *
     * @param locationId the value for space_device.location_id
     * @mbggenerated
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column space_device.device_category_id
     *
     * @return the value of space_device.device_category_id
     * @mbggenerated
     */
    public String getDeviceCategoryId() {
        return deviceCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column space_device.device_category_id
     *
     * @param deviceCategoryId the value for space_device.device_category_id
     * @mbggenerated
     */
    public void setDeviceCategoryId(String deviceCategoryId) {
        this.deviceCategoryId = deviceCategoryId == null ? null : deviceCategoryId.trim();
    }
}