package org.ingcar_boot_war.service;

import org.ingcar_boot_war.dao.*;
import org.ingcar_boot_war.dto.CommentDTO;
import org.ingcar_boot_war.dto.InformDTO;
import org.ingcar_boot_war.dto.InquiryDTO;
import org.ingcar_boot_war.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

////////////////// 관리자 화면에서 데이터 가져올 떄 필요한 sql 문 메소드 호출을 위함

@Service
public class AdminBoardService {


    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private InquiryDAO inquiryDAO;

    @Autowired
    private InformDAO informDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private ReviewLikeDAO reviewLikeDAO;


    // 공용
    private final int pageSize = 15;  // 페이지당 게시물 수

    // 공용
    public int offset(int page){

        return (page - 1) * pageSize;
    }



    // 문의 전체 리스트 개수
    public int board_totalInquiryPages(){

        int totalInquiry = inquiryDAO.countAllInquiry(); // 총 레코드 개수
        return (int) Math.ceil((double) totalInquiry / pageSize);
    }

    // 문의 전체 리스트
    public List<InquiryDTO> getInquiry(int offset) {

        return inquiryDAO.findAllPagedInquiry(offset, pageSize);
    }


    // 공지 전체 카운트
    public int board_totalInformPages(){

        int totalInforms= informDAO.countAllInform(); // 총 레코드 개수
        System.out.println("totalInforms 총 레코드 개수 = " + totalInforms);
        System.out.println("------ "+(int) Math.ceil((double) totalInforms / pageSize));
        return (int) Math.ceil((double) totalInforms / pageSize)-1;
    }

    // 공지 전체 리스트
    public List<InformDTO> getInform(int offset) {

        return informDAO.findAllPagedInform(offset, pageSize);
    }

    // 새로운 공지 등록
    public void createInform(String post_writer, String post_title, String post_content) {
        post_title = "[공지] " + post_title;
        String post_precontent = "잉카를 이용해주시는 회원 여러분께 감사드립니다.";


        InformDTO newInform = new InformDTO();
        newInform.setPost_writer(post_writer);
        newInform.setPost_title(post_title);
        newInform.setPost_precontent(post_precontent);
        newInform.setPost_content(post_content);
        newInform.setPost_image("/images/etc/InformmainIMG.jpg"); // Fixed image path
        newInform.setCreated_at(new Date(System.currentTimeMillis()));

        informDAO.createInform(newInform);

    }

    // 공지 수정
    public void updateInform(String post_writer, String post_title, String post_content) {
        post_title = "[공지] " + post_title;
        String post_precontent = "잉카를 이용해주시는 회원 여러분께 감사드립니다.";

        InformDTO newInform = new InformDTO();
        newInform.setPost_writer(post_writer);
        newInform.setPost_title(post_title);
        newInform.setPost_precontent(post_precontent);
        newInform.setPost_content(post_content);

        informDAO.updateInform(newInform);
    }

    // 공지 제목 검색
    public List<InformDTO> searchByPostTitle(String keyword, int offset) {
        System.out.println("keyword = " + keyword);
        System.out.println("offset = " + offset);

        return informDAO.searchByInformTitle(keyword, offset, pageSize);
    }

    // 공지 내용 검색
    public List<InformDTO> searchInformsByContent(String keyword, int offset) {
        return informDAO.searchByInformContent(keyword, offset, pageSize);
    }

    // 공지 삭제
    public void deleteInform(Long post_id) {
        informDAO.deleteInform(post_id);
    }

    // 리뷰 전체 리스트
    public List<ReviewDTO> getReview(int offset) {
        return reviewDAO.findAllPagedReview(offset, pageSize);
    }

    // 리뷰 전체 카운트
    public int board_totalReviewPages() {

        int totalReviews = reviewDAO.countAllReview();
        return (int) Math.ceil((double) totalReviews / pageSize);

    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(int log_id) {
        // reviewlike 테이블의 데이터 삭제
        reviewLikeDAO.deleteLikesByLogId(log_id);
        // comments 테이블의 데이터 삭제
        commentDAO.deleteCommentsByLogId(log_id);
        // reviewboard 테이블의 데이터 삭제
        reviewDAO.deleteReviewByLogId(log_id);
    }


    // 리뷰 제목 검색
    public List<ReviewDTO> searchReviewByTitle(String keyword, int offset) {
        return reviewDAO.searchByReviewTitle(keyword, offset, pageSize);
    }

    // 리뷰 내용 검색
    public List<ReviewDTO> searchReviewByContent(String keyword, int offset) {
        return reviewDAO.searchByReviewContent(keyword, offset, pageSize);
    }


    // 특정 문의 가져오기
    public InquiryDTO findByIdInquiry(int inquiry_id) {
        return inquiryDAO.findByIdInquiry(inquiry_id);
    }

    // 특정 문의 답변 달기

    public void updateInquiryComment(int inquiry_id, String response_id,  String response_title, String response_content) {
        inquiryDAO.updateInquiryResponse(inquiry_id, response_id, response_title, response_content);
    }

    // 특정 문의 글 삭제
    public void deleteInquiry(int inquiry_id) {
        inquiryDAO.deleteInquiryById(inquiry_id);
    }


    // 문의 제목 검색
    public List<InquiryDTO> searchInquiryByTitle(String keyword, int offset) {

        return inquiryDAO.searchByReviewTitle(keyword, offset, pageSize);
    }

    // 문의 내용 검색
    public List<InquiryDTO> searchInquiryByContent(String keyword, int offset) {
        return inquiryDAO.searchByReviewContent(keyword, offset, pageSize);
    }




}
