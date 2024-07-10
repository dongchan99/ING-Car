package org.ingcar_boot_war.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    @Getter
    @Setter
    @ToString
    public class CartDTO {
        private int cart_id; //자동으로 증가
        private int car_registration_id; //등록 번호
        private String user_id;//내 정보 세션에 있는(user_id)
        private String cart_status;
    }
