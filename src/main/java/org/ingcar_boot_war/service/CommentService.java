package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CommentDAO;
import org.ingcar_boot_war.dao.ReviewDAO;
import org.ingcar_boot_war.dao.UserDAO;
import org.ingcar_boot_war.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    public List<CommentDTO> getCommentsByLogId(int log_id) {
        return commentDAO.getCommentsByLogId(log_id);
    }

    public List<CommentDTO> getRepliesByCommentId(int parent_id) {
        return commentDAO.getRepliesByCommentId(parent_id);
    }

    public CommentDTO getCommentById(int comment_id) {
        return commentDAO.getCommentById(comment_id);
    }

    public int getCommentCountByLogId(int log_id) {
        return commentDAO.getCommentCountByLogId(log_id);
    }

    public void insertComment(int log_id, String user_id, String comment_content, int parent_id, Integer child_id) {
        // 댓글의 수를 확인하여 첫 댓글인지 판별
        int commentCount = getCommentCountByLogId(log_id);

        if (commentCount == 0) {
            // 첫 댓글일 경우
            parent_id = 0;
            child_id = 0;
        } else if (parent_id != 0) {
            // 부모 댓글이 존재하는지 확인
            CommentDTO parentComment = getCommentById(parent_id);
            if (parentComment == null) {
                throw new RuntimeException("Parent comment does not exist.");
            }
        }

        if (child_id == null) {
            child_id = 0;  // 기본값 설정
        }
        commentDAO.insertComment(log_id, user_id, comment_content, parent_id, child_id);
    }

    public void updateComment(int comment_id, String comment_content) {
        commentDAO.updateComment(comment_id, comment_content);
    }

    public void deleteComment(int comment_id) {
        commentDAO.deleteComment(comment_id);
    }

/*
    // 후기글이 삭제 될 시에는 log_id 가 일치하는 댓글도 db에서 삭제 되어야 함.
    public void deleteCommentsByLogId(int log_id) {
        commentDAO.deleteCommentsByLogId(log_id);
    }*/


    // 복호화
    public String getUserPassword(String userId) {
        return userDAO.findPasswordByUserId(userId);
    }

    public void deleteReview(int log_id) {
        reviewDAO.deleteReviewByLogId(log_id);
        commentDAO.deleteCommentsByLogId(log_id);
    }
}