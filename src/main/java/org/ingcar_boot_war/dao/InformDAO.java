package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.InformDTO;
import org.ingcar_boot_war.dto.InquiryDTO;

import java.util.List;

@Mapper
public interface InformDAO {

    @Select("SELECT * FROM (" +
            "SELECT a.*, ROWNUM rnum FROM (" +
            "SELECT * FROM inform ORDER BY created_at DESC" +
            ") a WHERE ROWNUM <= #{page} * #{pageSize}" +
            ") WHERE rnum > (#{page} - 1) * #{pageSize}")
    List<InformDTO> findAllPaged(@Param("page") int page, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM inform")
    int getTotalCount();

    @Select("SELECT * FROM inform WHERE post_content LIKE '%' || #{keyword} || '%'")
    List<InformDTO> searchByPostContent(@Param("keyword") String keyword);

    @Select("SELECT * FROM inform WHERE post_title LIKE '%' || #{keyword} || '%'")
    List<InformDTO> searchByPostTitle(@Param("keyword") String keyword);



//    @Select("SELECT post_image FROM inform WHERE id = #{id}")
//    InformDTO getPostById(@Param("id") Long id);


    @Select("SELECT * FROM inform WHERE post_title LIKE '%' || #{titleKeyword} || '%' OR post_content LIKE '%' || #{contentKeyword} || '%'")
    List<InformDTO> searchByTitleOrContent(@Param("titleKeyword") String titleKeyword, @Param("contentKeyword") String contentKeyword);





    // ---------------------  관리자

    @Select("SELECT * FROM (" +
            "SELECT a.*, ROWNUM rnum FROM (" +
            "SELECT * FROM INFORM ORDER BY created_at DESC" +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize}" +
            ") WHERE rnum > #{offset}")
    List<InformDTO> findAllPagedInform(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM INFORM")
    int countAllInform();


    // 공지 새글 등록
    @Insert("INSERT INTO INFORM (post_id, post_writer, post_title, post_precontent, post_content, post_image, created_at) " +
            "VALUES (INFORM_SEQ.NEXTVAL, #{post_writer}, #{post_title}, #{post_precontent}, #{post_content}, '/images/etc/InformmainIMG.jpg', SYSDATE)")
    void createInform(InformDTO informDTO);


    // 공지 수정
    @Update("UPDATE INFORM SET post_writer = #{post_writer}, post_title = #{post_title}, post_precontent = #{post_precontent}, post_content = #{post_content} WHERE post_id = #{post_id}")
    void updateInform(InformDTO informDTO);

    // 공지 삭제
    @Delete("DELETE FROM INFORM WHERE post_id = #{post_id}")
    void deleteInform(Long post_id);


    // 공지 제목검색
    @Select("SELECT * FROM ( " +
            "SELECT ROWNUM AS rn, a.* FROM ( " +
            "SELECT * FROM INFORM WHERE post_title LIKE '%' || #{keyword} || '%' ORDER BY created_at DESC " +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize} " +
            ") WHERE rn > #{offset}")
    List<InformDTO> searchByInformTitle(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 공지 내용검색
    @Select("SELECT * FROM ( " +
            "SELECT ROWNUM AS rn, a.* FROM ( " +
            "SELECT * FROM INFORM WHERE post_content LIKE '%' || #{keyword} || '%' ORDER BY created_at DESC " +
            ") a WHERE ROWNUM <= #{offset} + #{pageSize} " +
            ") WHERE rn > #{offset}")
    List<InformDTO> searchByInformContent(@Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);



}




