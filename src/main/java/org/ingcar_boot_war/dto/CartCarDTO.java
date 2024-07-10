package org.ingcar_boot_war.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CartCarDTO {

    private int list_id;
    private int car_registration_id;
    private String user_id;
    private String car_id;
    private int car_year;
    private int car_mileage;
    private String car_brand;
    private String car_model;
    private String car_color;
    private String car_fuel_type;
    private int car_price;
    private String car_accident_history;
    private Date car_registration_date;
    private String car_existed;
    private String cart_status;
    private String car_price2;

}
