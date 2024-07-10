package org.ingcar_boot_war.dao;

import org.apache.ibatis.annotations.*;
import org.ingcar_boot_war.dto.CommentDTO;

import java.util.List;

@Mapper
public interface CommentDAO {

        @Select("SELECT * FROM comments " +
                "WHERE log_id = #{log_id} AND parent_id = 0 and is_deleted != 1 " +
                "ORDER BY comment_id ASC")
        List<CommentDTO> getCommentsByLogId(@Param("log_id") int log_id);

        @Select("SELECT * FROM comments WHERE parent_id = #{parent_id} and is_deleted != 1 ORDER BY comment_id ASC")
        List<CommentDTO> getRepliesByCommentId(@Param("parent_id") int parent_id);

        @Select("SELECT * FROM comments WHERE comment_id = #{comment_id} " )
        CommentDTO getCommentById(@Param("comment_id") int comment_id);

        @Select("SELECT COUNT(*) FROM comments WHERE log_id = #{log_id}")
        int getCommentCountByLogId(@Param("log_id") int log_id);



        // 댓글 등록
        @Insert("INSERT INTO comments (comment_id, log_id, user_id, comment_content, parent_id, child_id) " +
                "VALUES (comment_seq.NEXTVAL, #{log_id}, #{user_id}, #{comment_content}, #{parent_id}, NVL(#{child_id}, 0))")
        void insertComment(@Param("log_id") int log_id,
                           @Param("user_id") String user_id,
                           @Param("comment_content") String comment_content,
                           @Param("parent_id") int parent_id,
                           @Param("child_id") Integer child_id);

        // 댓글 수정 후 등록
        @Update("UPDATE comments SET comment_content = #{comment_content}, comment_date = SYSDATE WHERE comment_id = #{comment_id}")
        void updateComment(@Param("comment_id") int comment_id, @Param("comment_content") String comment_content);

        // 댓글 삭제( is_deleted 값만 1로 바꾸어서 화면에 보이지만 않게한다)
        @Update("UPDATE comments SET is_deleted = 1 WHERE comment_id = #{comment_id}")
        void deleteComment(@Param("comment_id") int comment_id);

        // 트랜잭션으로 댓글, 후기 동시 삭제
        @Delete("DELETE FROM comments WHERE log_id = #{log_id}")
        void deleteCommentsByLogId(@Param("log_id") int log_id);


}
