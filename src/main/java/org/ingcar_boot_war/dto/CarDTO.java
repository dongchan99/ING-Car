package org.ingcar_boot_war.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CarDTO {
    private int car_registration_id;
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
    private String user_id;
    private String car_price2;
    private Integer car_views;
    private String car_existed;


}