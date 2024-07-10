package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CartDAO;
import org.ingcar_boot_war.dao.TransLogDAO;
import org.ingcar_boot_war.dto.TransLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransLogService {

    private final TransLogDAO transLogDAO;
    private final CartDAO cartDAO;

    @Autowired
    public TransLogService(TransLogDAO transLogDAO, CartDAO cartDAO) {

        this.transLogDAO = transLogDAO;
        this.cartDAO = cartDAO;
    }

    public String reserveCar(int car_registration_id, String user_id, String translog_price) {

        if (transLogDAO.countTransLog(car_registration_id, user_id) > 0) {

            return "exist"; //이미 예약 되어있음을 클라이언트에게 알려줍니다

        } else {
            transLogDAO.insertTransLog(car_registration_id, user_id, translog_price);
            return "added"; // 추가되었음을 클라이언트에게 알립니다.
        }
    }

    //거래 내역 여부 확인
    public boolean check_transLog(int car_registration_id){
        return transLogDAO.check_transLog(car_registration_id) > 0;
    }

    public List<TransLogDTO> check_current_translog(int car_registration_id){

        return transLogDAO.check_current_translog(car_registration_id);
    }
}

