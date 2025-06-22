/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.red.purple.model;

public class ClinicLocation {
    private int locationId;
    private String name;
    private String address;
    private String phone;

    public ClinicLocation() {}

    public ClinicLocation(int locationId, String name, String address, String phone) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
