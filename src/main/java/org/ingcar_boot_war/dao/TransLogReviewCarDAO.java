package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.ingcar_boot_war.dto.TransLogReviewCarDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface TransLogReviewCarDAO {

    // 전체 조회 (구매내역)
    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT B.log_id, B.log_date, C.car_model, B.translog_price, A.log_id AS val FROM ReviewBoard A RIGHT JOIN TransLog B ON A.log_id = B.log_id LEFT JOIN Car C ON B.car_registration_id = C.car_registration_id WHERE B.user_id=#{user_id} and B.current_translog=1 ORDER BY B.log_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogReviewCarDTO> mypage_buyed(String user_id, int offset, int pageSize);

    // 전체 DATA 개수 (구매완료)
    @Select("SELECT COUNT(*) FROM ReviewBoard A RIGHT JOIN TransLog B ON A.log_id = B.log_id LEFT JOIN Car C ON B.car_registration_id = C.car_registration_id WHERE B.user_id=#{user_id} and B.current_translog=1")
    int mypage_buyed_count(@Param("user_id") String user_id);
}
