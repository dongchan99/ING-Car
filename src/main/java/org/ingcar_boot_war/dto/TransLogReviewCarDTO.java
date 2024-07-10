package org.ingcar_boot_war.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TransLogReviewCarDTO {

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

    private int log_id;
    private int car_registration_id;
    private Integer current_translog;
    private Date log_date;
    private String translog_status;
    private String translog_price;

    int board_id;
    String board_subject;
    String board_content;
    int board_count;
    java.sql.Date board_regdate;
    java.sql.Date board_updatedate;
    int board_like;
    String board_star;

    private int val; // 별칭
}
