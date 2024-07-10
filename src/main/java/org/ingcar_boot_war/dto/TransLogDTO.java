package org.ingcar_boot_war.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TransLogDTO {
    private int log_id;//시퀀스 : 생성될 때 마다 자동으로 증가
    private int car_registration_id;//차량 등록 아이디 car 테이블의 pk를 외래키로 지정
    private String user_id;//예약자의 아이디 users 테이블의 pk를 외래키로 지정
    private Integer current_translog;// 예약중 ( 0 ) / 거래완료 ( 1 ) 테이블에 항목이 생성될 때 기본값은 0
    private Date log_date;//거래날짜 기본값으로 sysdate
    private String translog_status;
    private String translog_price; // 구매자가 입력한 가격
}
