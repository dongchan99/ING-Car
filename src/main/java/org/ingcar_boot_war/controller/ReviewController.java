package org.ingcar_boot_war.controller;

import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dao.CommentDAO;
import org.ingcar_boot_war.dao.ReviewDAO;
import org.ingcar_boot_war.dao.ReviewLikeDAO;
import org.ingcar_boot_war.dto.ReviewDTO;
import org.ingcar_boot_war.dto.UserDTO;
import org.ingcar_boot_war.service.ReviewService;
import org.ingcar_boot_war.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewLikeDAO reviewLikeDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewDAO reviewDAO;

    @PostMapping("/submit_review")
    public String reviewInsert(@RequestParam("reviewSubject") String reviewSubject,
                               @RequestParam("reviewBody") String reviewBody,
                               @RequestParam("boardStar") String boardStar,
                               @RequestParam("log_id") int log_id) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setLog_id(log_id);
        reviewDTO.setBoard_subject(reviewSubject);
        reviewDTO.setBoard_content(reviewBody);
        reviewDTO.setBoard_star(boardStar);
        reviewService.insertReview(reviewDTO);
        return "redirect:/review_main";
    }

    @GetMapping("/review_main")
    public String reviewMain(Model model, @RequestParam(defaultValue = "1") int page, HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        String userId = userSession.getUser_id();

        model.addAttribute("user_Id", userId);

        int pageSize = 4;
        int offset = (page - 1) * pageSize;

        int totalReviews = reviewService.getTotalReviews();
        List<ReviewDTO> resultReviewList = reviewService.getPagedReviews(offset, pageSize);

        int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

        model.addAttribute("result_reviewlist", resultReviewList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        List<String> listenToMyHeartBeat = reviewService.getHeartBeatStatus(resultReviewList, userId, reviewLikeDAO);

        model.addAttribute("listen_to_my_heart_beat", listenToMyHeartBeat);

        return "review_main";
    }

    @GetMapping("/review_search")
    public String reviewSearch(Model model, @RequestParam(defaultValue = "1") int page,
                               @RequestParam("reviewsearchtext") String reviewSubject,
                               @RequestParam("review_search") String review_search, HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        String userId = userSession.getUser_id();

        model.addAttribute("user_Id", userId);

        List<ReviewDTO> resultReviewList;
        int totalReviews;
        int pageSize = 4;
        int offset = (page - 1) * pageSize;

        if ("car_model".equals(review_search)) {
            totalReviews = reviewService.countReviewsByCarModel(reviewSubject);
            resultReviewList = reviewService.searchReviewsByCarModel(reviewSubject, offset, pageSize);
            System.out.println("------------- resultReviewList = " + resultReviewList);
        } else {
            totalReviews = reviewService.countReviewsBySubject(reviewSubject);
            resultReviewList = reviewService.searchReviewsBySubject(reviewSubject, offset, pageSize);
            System.out.println("------------- resultReviewList = " + resultReviewList);
        }

        int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

        model.addAttribute("result_reviewlist", resultReviewList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchQuery", reviewSubject);
        model.addAttribute("searchType", review_search);

        List<String> listenToMyHeartBeat = reviewService.getHeartBeatStatus(resultReviewList, userId, reviewLikeDAO);

        model.addAttribute("listen_to_my_heart_beat", listenToMyHeartBeat);

        return "review_search";
    }

    @GetMapping("/review_detail")
    public String reviewDetail(Model model, @RequestParam("log_id") String log_id,
                               @RequestParam("board_subject") String board_subject, HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        String userId = userSession.getUser_id();

        model.addAttribute("user_session", userSession);

        reviewService.incrementViewCount(log_id, board_subject);

        List<ReviewDTO> resultReviewDetail = reviewService.getReviewDetails(log_id, board_subject);

        Boolean tfIsOwner = false;

        if (!resultReviewDetail.isEmpty()) {
            tfIsOwner = resultReviewDetail.get(0).getBuyer().equals(userId);
        }

        model.addAttribute("result_reviewdetail", resultReviewDetail);
        model.addAttribute("tf_IsOwner", tfIsOwner);
        model.addAttribute("userId", userId);

        return "review_detail";
    }

    @PostMapping("/reviews/update")
    @ResponseBody
    public ResponseEntity<String> updateReview(@RequestParam("log_id") int log_id,
                                               @RequestParam("reviewSubject") String reviewSubject,
                                               @RequestParam("reviewContent") String reviewContent,
                                               @RequestParam("reviewStars") int reviewStars, HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        if (userSession == null) {
            return ResponseEntity.status(403).body("User is not logged in.");
        }
        reviewService.updateReview(log_id, reviewSubject, reviewContent, reviewStars);
        return ResponseEntity.ok("Review updated successfully.");
    }

    // 20240626
    @PostMapping("/reviews/delete")
    @Transactional
    public ResponseEntity<String> deleteReview(@RequestParam("password") String password,
                                               @RequestParam("log_id") int log_id,
                                               HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        if (userSession != null) {
            String storedPassword = userService.getUserPassword(userSession.getUser_id());
            if (passwordEncoder.matches(password, storedPassword)) {
                System.out.println("비밀번호 일치. 삭제 진행");
                // 리뷰 및 관련 댓글, 좋아요 기록 삭제
                reviewService.deleteReview(log_id);
                return ResponseEntity.ok("success");

            } else {
                return ResponseEntity.status(403).body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.status(403).body("User is not logged in.");
        }
    }

    @GetMapping("/isLiked")
    public ResponseEntity<Map<String, Boolean>> isLiked(@RequestParam("log_id") String log_id,
                                                        @RequestParam("user_id") String user_id) {
        int logId = Integer.parseInt(log_id);
        boolean isLiked = reviewService.isLiked(user_id, logId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLiked", isLiked);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateLike")
    public ResponseEntity<Map<String, Integer>> updateLike(@RequestParam("log_id") String log_id,
                                                           @RequestParam("increment") int increment) {
        reviewService.updateLikeCount(log_id, increment);
        int newLikeCount = reviewService.getLikeCount(log_id);
        Map<String, Integer> response = new HashMap<>();
        response.put("newLikeCount", newLikeCount);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/heart_beat")
    @ResponseBody
    public ResponseEntity<String> heartBeat(@RequestParam(name = "log_id") String logId,
                                            @RequestParam(name = "color") String color, HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        HttpHeaders headers = new HttpHeaders();

        int log_id = Integer.parseInt(logId);
        int num = 0;

        if (Objects.equals(color, "red")) {
            reviewLikeDAO.deleteHeart(userSession.getUser_id(), log_id);
            int board_like = reviewDAO.find_board_like(log_id);
            reviewDAO.minus_board_like(log_id, board_like);
            num = board_like - 1;
            headers.add("board_like", String.valueOf(num));
            return ResponseEntity.ok().headers(headers).body("removed");
        }

        if (Objects.equals(color, "black")) {
            reviewLikeDAO.insertHeart(userSession.getUser_id(), log_id);
            int board_like = reviewDAO.find_board_like(log_id);
            reviewDAO.plus_board_like(log_id, board_like);
            num = board_like + 1;
            headers.add("board_like", String.valueOf(num));
            return ResponseEntity.ok().headers(headers).body("added");
        }

        return null;
    }

    @GetMapping("/mypage_review_detail")
    public String mypageReviewDetail(Model model, @RequestParam("log_id") int log_id, HttpSession session) {
        UserDTO userSession = (UserDTO) session.getAttribute("user_session");
        String userId = userSession.getUser_id();

        model.addAttribute("user_session", userSession);

        List<ReviewDTO> val = reviewDAO.mypage_review_find_log(log_id);

        if (val.isEmpty()) {
            return "mypage_empty";
        }

        reviewDAO.mypageincrementViewCount(log_id);
        List<ReviewDTO> resultReviewDetail = reviewDAO.mypageselectReviewWithDetails(log_id);

        Boolean tfIsOwner = false;

        if (!resultReviewDetail.isEmpty()) {
            tfIsOwner = resultReviewDetail.get(0).getBuyer().equals(userId);
        }

        model.addAttribute("result_reviewdetail", resultReviewDetail);
        model.addAttribute("tf_IsOwner", tfIsOwner);
        model.addAttribute("userId", userId);

        return "review_detail";
    }
}