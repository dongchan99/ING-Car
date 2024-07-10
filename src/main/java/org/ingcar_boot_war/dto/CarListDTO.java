package org.ingcar_boot_war.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CarListDTO {
    private Integer car_registration_id; // 추가된 필드
    private String car_id;
    private Integer car_year;
    private Integer car_mileage;
    private String car_brand;
    private String car_model;
    private String car_color;
    private String car_fuel_type;
    private Integer car_price;
    private String car_accident_history;
    private String car_description;
    private Date car_registration_date;
    private String car_price2;
    private String car_existed;

    // Getters and setters
    public Integer getCar_registration_id() {
        return car_registration_id;
    }

    public void setCar_registration_id(Integer car_registration_id) {
        this.car_registration_id = car_registration_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public Integer getCar_year() {
        return car_year;
    }

    public void setCar_year(Integer car_year) {
        this.car_year = car_year;
    }

    public Integer getCar_mileage() {
        return car_mileage;
    }

    public void setCar_mileage(Integer car_mileage) {
        this.car_mileage = car_mileage;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }

    public String getCar_fuel_type() {
        return car_fuel_type;
    }

    public void setCar_fuel_type(String car_fuel_type) {
        this.car_fuel_type = car_fuel_type;
    }

    public Integer getCar_price() {
        return car_price;
    }

    public void setCar_price(Integer car_price) {
        this.car_price = car_price;
    }

    public String getCar_accident_history() {
        return car_accident_history;
    }

    public void setCar_accident_history(String car_accident_history) {
        this.car_accident_history = car_accident_history;
    }

    public String getCar_description() {
        return car_description;
    }

    public void setCar_description(String car_description) {
        this.car_description = car_description;
    }

    public Date getCar_registration_date() {
        return car_registration_date;
    }

    public void setCar_registration_date(Date car_registration_date) {
        this.car_registration_date = car_registration_date;
    }

    public String getCar_price2() {
        return car_price2;
    }
}