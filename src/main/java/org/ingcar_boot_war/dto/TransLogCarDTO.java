package org.ingcar_boot_war.dto;

import lombok.Data;

import java.util.Date;

@Data
// @Builder 쓰면 안되는 이유 아나요?
public class TransLogCarDTO {

    //  TransLog columns
    private int log_id;
    private Integer current_translog;
    private Date log_date;
    private String translog_status;
    private String translog_price;

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
    private String car_price2;

}