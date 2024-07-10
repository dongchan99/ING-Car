package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.CommentDAO;
import org.ingcar_boot_war.dao.ReviewDAO;
import org.ingcar_boot_war.dao.ReviewLikeDAO;
import org.ingcar_boot_war.dto.ReviewDTO;
import org.ingcar_boot_war.dto.ReviewLikeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private ReviewLikeDAO reviewLikeDAO;



    public void insertReview(ReviewDTO reviewDTO) {
        reviewDAO.insertReview(reviewDTO.getLog_id(), reviewDTO.getBoard_star(), reviewDTO.getBoard_content(), reviewDTO.getBoard_subject());
    }

    public List<ReviewDTO> getPagedReviews(int offset, int pageSize) {
        return reviewDAO.findAllPagedReview(offset, pageSize);
    }

    public int getTotalReviews() {
        return reviewDAO.countAllReview();
    }

    public List<ReviewDTO> searchReviewsBySubject(String reviewSubject, int offset, int pageSize) {
        return reviewDAO.findByBoardSubjectWithPaging(reviewSubject, offset, pageSize);
    }

    public int countReviewsBySubject(String reviewSubject) {
        return reviewDAO.countByBoardSubject(reviewSubject);
    }

    public void incrementViewCount(String log_id, String board_subject) {
        reviewDAO.incrementViewCount(log_id, board_subject);
    }

    public List<ReviewDTO> getReviewDetails(String log_id, String board_subject) {
        return reviewDAO.selectReviewWithDetails(log_id, board_subject);
    }

    public void updateReview(int log_id, String reviewSubject, String reviewContent, int reviewStars) {
        reviewDAO.updateReviewByLogId(log_id, reviewSubject, reviewContent, reviewStars);
    }

    @Transactional
    public void deleteReview(int log_id) {
        // reviewlike 테이블의 데이터 삭제
        reviewLikeDAO.deleteLikesByLogId(log_id);
        // comments 테이블의 데이터 삭제
        commentDAO.deleteCommentsByLogId(log_id);
        // reviewboard 테이블의 데이터 삭제
        reviewDAO.deleteReviewByLogId(log_id);
    }

    public int getLikeCount(String logId) {
        return reviewDAO.getLikeCount(logId);
    }

    public void updateLikeCount(String logId, int increment) {
        reviewDAO.updateLikeCount(logId, increment);
    }

    public List<ReviewDTO> searchReviewsByCarModel(String carModel, int offset, int pageSize) {
        return reviewDAO.findByCarModelWithPaging(carModel, offset, pageSize);
    }

    public int countReviewsByCarModel(String carModel) {
        return reviewDAO.countByCarModel(carModel);
    }

    public boolean isLiked(String userId, int logId) {
        return reviewDAO.isLiked(userId, logId) > 0;
    }

    public List<String> getHeartBeatStatus(List<ReviewDTO> reviews, String userId, ReviewLikeDAO reviewLikeDAO) {
        List<String> listen_to_my_heart_beat = new ArrayList<>();
        for (ReviewDTO review : reviews) {
            List<ReviewLikeDTO> likeList = reviewLikeDAO.checkHeart(userId, review.getLog_id());
            if (likeList.isEmpty()) {
                listen_to_my_heart_beat.add("true");
            } else {
                listen_to_my_heart_beat.add("false");
            }
        }
        return listen_to_my_heart_beat;
    }



}