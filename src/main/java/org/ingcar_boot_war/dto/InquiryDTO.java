package org.ingcar_boot_war.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class InquiryDTO {
    int inquiry_id;
    String inquiry_title;
    String inquiry_content;
    Date inquiry_date;
    String inquiry_status;
    String user_id;
    String response_id;
    String response_title;
    String response_content;
    Date response_date;

}
