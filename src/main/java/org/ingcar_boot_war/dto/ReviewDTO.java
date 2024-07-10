package org.ingcar_boot_war.dto;


import lombok.Data;
import lombok.Getter;

import java.sql.Date;

@Data
@Getter
public class ReviewDTO {

    int board_id;
    int log_id;
    String board_subject;
    String board_content;
    int board_count;
    Date board_regdate;
    Date board_updatedate;
    int board_like;
    String board_star;
    String seller; // 판매자 이름
    String buyer; // 구매자 이름
    String model;  // 자동차 모델


}