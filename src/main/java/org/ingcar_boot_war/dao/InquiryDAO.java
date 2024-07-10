package org.ingcar_boot_war.dao;


import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.InquiryDTO;

import java.util.List;

@Mapper
public interface InquiryDAO {

    @Insert("INSERT INTO Inquiry (inquiry_id, inquiry_title, inquiry_content, inquiry_status, user_id, inquiry_date) " +
            "VALUES (inquiry_SEQ.NEXTVAL, #{inquiry_title}, #{inquiry_content}, #{inquiry_status}, #{user_id}, sysdate)")
    void insertInquiry(InquiryDTO inquiryDTO);



    @Select("SELECT COUNT(*) FROM Inquiry where user_id = #{user_id}")
    int countAll(String user_id);

    // 사용자가 작성한 문의글만 가져오기
    @Select("SELECT * FROM Inquiry WHERE user_id = #{user_id} ORDER BY inquiry_id DESC")
    List<InquiryDTO> selectByUserId(String user_id);


    @Select("SELECT * FROM (SELECT ROWNUM AS RNUM, A.* FROM (SELECT i.* FROM INQUIRY i " +
            "WHERE i.user_id = #{user_id} " +
            "ORDER BY i.inquiry_id DESC) A WHERE ROWNUM <= #{offset} + #{pageSize}) WHERE RNUM > #{offset}")
    List<InquiryDTO> selectPaged(@Param("user_id") String user_id, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 문의글 상세화면
    @Select("SELECT * FROM Inquiry WHERE inquiry_id = #{inquiry_id}")
    InquiryDTO findById(@Param("inquiry_id") int inquiry_id);

    // 문의글 수정
    @Update("UPDATE Inquiry SET inquiry_title = #{inquiry_title}, inquiry_content = #{inquiry_content}, inquiry_date = SYSDATE WHERE inquiry_id = #{inquiry_id}")
    void updateInquiry(@Param("inquiry_id") int inquiry_id,
                       @Param("inquiry_title") String inquiry_title,
                       @Param("inquiry_content") String inquiry_content);


    // 문의글 삭제
    @Delete("DELETE FROM Inquiry WHERE inquiry_id = #{inquiry_id}")
    void deleteInquiryById(@Param("inquiry_id") int inquiry_id);


    // ---------------------------------------   관리자

    @Select("SELECT * FROM (" +
            "SELECT a.*, ROWNUM rnum FROM (" +
            "SELECT * FROM INQUIRY ORDER BY inquiry_date DESC" +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize}" +
            ") WHERE rnum > #{offset}")
    List<InquiryDTO> findAllPagedInquiry(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM INQUIRY")
    int countAllInquiry();

    // 특정 문의글 리스트 뽑아오기
    @Select("SELECT * FROM Inquiry WHERE inquiry_id = #{inquiry_id}")
    InquiryDTO findByIdInquiry(@Param("inquiry_id") int inquiry_id);


    @Update("UPDATE Inquiry SET response_id = #{response_id}, response_title = #{response_title}, response_content = #{response_content}, response_date = SYSDATE, inquiry_status='Y' WHERE inquiry_id = #{inquiry_id}")
    void updateInquiryResponse(@Param("inquiry_id") int inquiry_id,
                               @Param("response_id") String response_id,
                               @Param("response_title") String response_title,
                               @Param("response_content") String response_content);


    // 문의 제목 검색
    @Select("SELECT * FROM ( " +
            "SELECT ROWNUM AS rn, a.* FROM ( " +
            "SELECT * FROM Inquiry WHERE inquiry_title LIKE '%' || #{keyword} || '%' ORDER BY inquiry_date DESC " +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize} " +
            ") WHERE rn > #{offset}")
    List<InquiryDTO> searchByReviewTitle(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);


    // 문의 내용 검색
    @Select("SELECT * FROM ( " +
            "SELECT ROWNUM AS rn, a.* FROM ( " +
            "SELECT * FROM Inquiry WHERE inquiry_content LIKE '%' || #{keyword} || '%' ORDER BY inquiry_date DESC " +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize} " +
            ") WHERE rn > #{offset}")
    List<InquiryDTO> searchByReviewContent(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

}

