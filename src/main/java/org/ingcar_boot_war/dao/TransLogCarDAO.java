package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.ingcar_boot_war.dto.TransLogCarDTO;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface TransLogCarDAO {

    // 전체 조회 (구매중)
    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE A.current_translog != 1 AND A.user_id = #{user_id} ORDER BY A.log_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> mypage_buying(String user_id, int offset, int pageSize);

    // 전체 DATA 개수 (구매중)
    @Select("SELECT COUNT(*) FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.current_translog=0 and A.user_id=#{user_id}")
    int mypage_buying_count(String user_id);

    // 전체 조회 (판매중)
//    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT B.car_registration_id, B.car_model, B.car_price, ROW_NUMBER() OVER (ORDER BY A.log_id DESC) AS RN FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE (A.current_translog = 0 OR A.current_translog=2) AND B.user_id = #{user_id}) A WHERE RN <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    @Select("WITH NumberedCars AS (\n" +
            "    SELECT B.car_registration_id, B.car_model, B.car_price, \n" +
            "           ROW_NUMBER() OVER (PARTITION BY B.car_registration_id ORDER BY A.log_id DESC) AS RN\n" +
            "    FROM TransLog A\n" +
            "    JOIN Car B ON A.car_registration_id = B.car_registration_id\n" +
            "    WHERE (A.current_translog = 0 OR A.current_translog = 2)\n" +
            "      AND B.user_id = #{user_id}\n" +
            "), FilteredCars AS (\n" +
            "    SELECT car_registration_id, car_model, car_price, ROW_NUMBER() OVER (ORDER BY RN) AS RNUM\n" +
            "    FROM NumberedCars\n" +
            "    WHERE RN = 1\n" +
            ")\n" +
            "SELECT *\n" +
            "FROM FilteredCars\n" +
            "WHERE RNUM > #{offset} AND RNUM <= #{offset} + #{pageSize}")
    List<TransLogCarDTO> mypage_selling(@Param("user_id") String user_id, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 전체 DATA 개수 (판매중)
    @Select("SELECT COUNT(DISTINCT B.car_registration_id) FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE (A.current_translog = 0 OR A.current_translog=2) AND B.user_id = #{user_id}")
    int mypage_selling_count(@Param("user_id") String user_id);

    // "구매자 list" (판매중)
//    @Select("SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.current_translog=0 and A.car_registration_id=#{car_registration_id}")


    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where (A.current_translog = 0 OR A.current_translog=2) and A.car_registration_id=#{car_registration_id} ORDER BY A.log_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> mypage_selling_list(int car_registration_id,  int offset, int pageSize);

    @Select("SELECT COUNT(*) FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE A.car_registration_id=#{car_registration_id} AND (A.current_translog = 0 OR A.current_translog=2)")
    int selling_list_totalPages(int car_registration_id);

    // 구매 "확정"
    @Select("SELECT A.car_registration_id FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id " +
            "where A.current_translog=2 and A.log_id=#{log_id}")
    int find_reg_id1(int log_id);

    // 전체 조회 (구매내역)
    @Select("SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.current_translog=1 and A.user_id=#{user_id}")
    List<TransLogCarDTO> mypage_buyed(String user_id);

    // 전체 조회 (판매내역)
    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.current_translog=1 and B.user_id=#{user_id} ORDER BY A.log_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> mypage_selled(String user_id, int offset, int pageSize);

    // 전체 DATA 개수 (구매중)
    @Select("SELECT COUNT(*) FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.current_translog=1 and B.user_id=#{user_id}")
    int mypage_selled_count(String user_id);

    // 차량상세페이지로 이동
    @Select("SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.log_id=1")
    List<TransLogCarDTO> selectOn(int log_id);

    // notification에서 translog조회
    @Select("SELECT B.car_model, B.user_id, A.log_id FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND A.translog_status='N' AND A.current_translog!=1 AND B.car_existed!='N'")
    List<TransLogCarDTO> find_translog_status(String user_id);

    @Select("SELECT B.car_model, B.user_id, A.log_id FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND B.car_existed='N' AND A.current_translog!=1")
    List<TransLogCarDTO> find_tcar_existed(String user_id);

    @Select("SELECT A.user_id FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where B.user_id=#{user_id} AND A.current_translog=2")
    List<String> find_user_id(String user_id);

    @Select("SELECT A.car_registration_id FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND B.car_existed='N'")
    List<Integer> dismiss_all_reg_id(String user_id);

    @Select("SELECT A.car_registration_id FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id where A.user_id=#{user_id} AND A.current_translog=2 AND B.user_id=#{seller}")
    int uddismiss(String user_id, String seller);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @Select("SELECT COUNT(DISTINCT B.car_registration_id) FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id")
//    int product_getAllCar_count();
    @Select("SELECT COUNT(DISTINCT B.car_registration_id) " +
            "FROM Car B " +
            "LEFT JOIN TransLog A ON A.car_registration_id = B.car_registration_id")
    int product_getAllCar_count();

    @Select("<script>" +
            "WITH LatestTransLog AS (" +
            "    SELECT t.*, ROW_NUMBER() OVER (PARTITION BY t.car_registration_id ORDER BY t.log_date DESC) AS rn " +
            "    FROM TransLog t" +
            "), FilteredTransLog AS (" +
            "    SELECT * FROM LatestTransLog WHERE rn = 1" +
            "), NumberedCars AS (" +
            "    SELECT B.car_registration_id, B.car_model, B.car_price2, B.car_existed, t.current_translog, B.car_brand, B.user_id, B.car_registration_date, " +
            "           ROW_NUMBER() OVER (ORDER BY B.car_registration_date DESC) AS RN " +
            "    FROM Car B " +
            "    LEFT JOIN FilteredTransLog t ON B.car_registration_id = t.car_registration_id " +
            "    WHERE B.car_existed = 'Y' " +
            "), FilteredCars AS (" +
            "    SELECT car_registration_id, car_model, car_price2, car_existed, current_translog, car_brand, user_id, car_registration_date " +
            "    FROM NumberedCars " +
            "    WHERE RN BETWEEN #{offset} + 1 AND #{offset} + #{pageSize} " +
            ") " +
            "SELECT * FROM FilteredCars " +
            "ORDER BY car_registration_date DESC" +
            "</script>")
    List<TransLogCarDTO> product_getAllCar(@Param("offset") int offset, @Param("pageSize") int pageSize);




    @Select("SELECT COUNT(*) FROM TransLog A JOIN Car B ON A.car_registration_id=B.car_registration_id WHERE A.current_translog=2")
    int producting_getAllCar_count();

    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE A.current_translog=2 AND B.car_existed!='N' ORDER BY B.car_registration_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> producting_getAllCar(int offset, int pageSize);

    //검색 추가
    @Select("<script>" +
            "WITH LatestTransLog AS (" +
            "    SELECT t.*, ROW_NUMBER() OVER (PARTITION BY t.car_registration_id ORDER BY t.log_date DESC) AS rn " +
            "    FROM TransLog t" +
            "), FilteredTransLog AS (" +
            "    SELECT * FROM LatestTransLog WHERE rn = 1" +
            "), NumberedCars AS (" +
            "    SELECT B.car_registration_id, B.car_model, B.car_price2, B.car_existed, t.current_translog, B.car_brand, B.user_id, B.car_registration_date, " +
            "           ROW_NUMBER() OVER (ORDER BY B.car_registration_date DESC) AS RN " +
            "    FROM Car B " +
            "    LEFT JOIN FilteredTransLog t ON B.car_registration_id = t.car_registration_id " +
            "    WHERE B.car_existed = 'Y' AND B.user_id LIKE '%' || #{keyword} || '%' " +
            "), FilteredCars AS (" +
            "    SELECT car_registration_id, car_model, car_price2, car_existed, current_translog, car_brand, user_id, car_registration_date " +
            "    FROM NumberedCars " +
            "    WHERE RN BETWEEN #{offset} + 1 AND #{offset} + #{pageSize} " +
            ") " +
            "SELECT * FROM FilteredCars " +
            "ORDER BY car_registration_date DESC" +
            "</script>")
    List<TransLogCarDTO> searchByUser_id(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("<script>" +
            "WITH LatestTransLog AS (" +
            "    SELECT t.*, ROW_NUMBER() OVER (PARTITION BY t.car_registration_id ORDER BY t.log_date DESC) AS rn " +
            "    FROM TransLog t" +
            "), FilteredTransLog AS (" +
            "    SELECT * FROM LatestTransLog WHERE rn = 1" +
            "), NumberedCars AS (" +
            "    SELECT B.car_registration_id, B.car_model, B.car_price2, B.car_existed, t.current_translog, B.car_brand, B.user_id, B.car_registration_date, " +
            "           ROW_NUMBER() OVER (ORDER BY B.car_registration_date DESC) AS RN " +
            "    FROM Car B " +
            "    LEFT JOIN FilteredTransLog t ON B.car_registration_id = t.car_registration_id " +
            "    WHERE B.car_existed = 'Y' AND B.car_brand LIKE '%' || #{keyword} || '%'" +
            "), FilteredCars AS (" +
            "    SELECT car_registration_id, car_model, car_price2, car_existed, current_translog, car_brand, user_id, car_registration_date " +
            "    FROM NumberedCars " +
            "    WHERE RN BETWEEN #{offset} + 1 AND #{offset} + #{pageSize} " +
            ") " +
            "SELECT * FROM FilteredCars " +
            "ORDER BY car_registration_date DESC" +
            "</script>")
    List<TransLogCarDTO> searchByCar_brand(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("<script>" +
            "WITH LatestTransLog AS (" +
            "    SELECT t.*, ROW_NUMBER() OVER (PARTITION BY t.car_registration_id ORDER BY t.log_date DESC) AS rn " +
            "    FROM TransLog t" +
            "), FilteredTransLog AS (" +
            "    SELECT * FROM LatestTransLog WHERE rn = 1" +
            "), NumberedCars AS (" +
            "    SELECT B.car_registration_id, B.car_model, B.car_price2, B.car_existed, t.current_translog, B.car_brand, B.user_id, B.car_registration_date, " +
            "           ROW_NUMBER() OVER (ORDER BY B.car_registration_date DESC) AS RN " +
            "    FROM Car B " +
            "    LEFT JOIN FilteredTransLog t ON B.car_registration_id = t.car_registration_id " +
            "    WHERE B.car_existed = 'Y' AND B.car_model LIKE '%' || #{keyword} || '%'" +
            "), FilteredCars AS (" +
            "    SELECT car_registration_id, car_model, car_price2, car_existed, current_translog, car_brand, user_id, car_registration_date " +
            "    FROM NumberedCars " +
            "    WHERE RN BETWEEN #{offset} + 1 AND #{offset} + #{pageSize} " +
            ") " +
            "SELECT * FROM FilteredCars " +
            "ORDER BY car_registration_date DESC" +
            "</script>")
    List<TransLogCarDTO> searchByCar_model(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);


    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE A.current_translog=2 AND B.car_existed!='N' AND B.user_id LIKE '%' || #{keyword} || '%' ORDER BY B.car_registration_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> productingSearchByUser_id(@Param("keyword") String keyword, int offset, int pageSize);

    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE A.current_translog=2 AND B.car_existed!='N' AND B.car_brand LIKE '%' || #{keyword} || '%' ORDER BY B.car_registration_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> productingSearchByCar_brand(@Param("keyword") String keyword, int offset, int pageSize);

    @Select("SELECT * FROM (SELECT A.*, ROWNUM AS RNUM FROM (SELECT * FROM TransLog A JOIN Car B ON A.car_registration_id = B.car_registration_id WHERE A.current_translog=2 AND B.car_existed!='N' AND B.car_model LIKE '%' || #{keyword} || '%' ORDER BY B.car_registration_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<TransLogCarDTO> productingSearchByCar_model(@Param("keyword") String keyword, int offset, int pageSize);


}



// SELECT A.log_id, B.reg_date, B.car_id, B.reg_price FROM TransLog A JOIN car_deatil B ON A.reg_id=B.reg_id