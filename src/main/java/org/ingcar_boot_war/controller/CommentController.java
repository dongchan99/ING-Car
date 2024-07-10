package org.ingcar_boot_war.controller;

//  REST API를 사용하기 때문에 model 객체가 필요하지 않음.

import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dao.CommentDAO;
import org.ingcar_boot_war.dto.CommentDTO;
import org.ingcar_boot_war.dto.UserDTO;
import org.ingcar_boot_war.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.sql.Date;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping
    public List<CommentDTO> getComments(@RequestParam("log_id") int log_id) {
        List<CommentDTO> comments = commentService.getCommentsByLogId(log_id);
        if (comments == null) {
            comments = new ArrayList<>(); // 댓글이 없으면 빈 리스트 반환
        }
        return comments;
    }

    @GetMapping("/replies")
    public List<CommentDTO> getReplies(@RequestParam("parent_id") int parent_id) {
        List<CommentDTO> replies = commentService.getRepliesByCommentId(parent_id);
        if (replies == null) {
            replies = new ArrayList<>(); // 대댓글이 없으면 빈 리스트 반환
        }
        return replies;
    }

    // 댓글 대댓글 등록시
    @PostMapping("/add")
    public CommentDTO addComment(@RequestParam("log_id") int log_id,
                                 @RequestParam("comment_content") String comment_content,
                                 @RequestParam(value = "parent_id", required = false, defaultValue = "0") int parent_id,
                                 @RequestParam(value = "child_id", required = false) Integer child_id,
                                 HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        String user_pw = user_session.getUser_password();
        System.out.println("user_pw = " + user_pw);

        if (user_session != null) {
            String user_id = user_session.getUser_id();
            commentService.insertComment(log_id, user_id, comment_content, parent_id, child_id);
            return new CommentDTO(0, log_id, user_id, comment_content, null, parent_id, child_id, 0);
        } else {
            throw new RuntimeException("User is not logged in.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateComment(@RequestParam("comment_id") int comment_id,
                                                @RequestParam("comment_content") String comment_content,
                                                HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        if (user_session != null) {
            CommentDTO existingComment = commentService.getCommentById(comment_id);
            if (existingComment != null && existingComment.getUser_id().equals(user_session.getUser_id())) {
                commentService.updateComment(comment_id, comment_content);
                return ResponseEntity.ok("Comment updated successfully.");
            } else {
                return ResponseEntity.status(403).body("Unauthorized action.");
            }
        } else {
            return ResponseEntity.status(403).body("User is not logged in.");
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("comment_id") int comment_id,
                                                HttpSession session) {
        UserDTO user_session = (UserDTO) session.getAttribute("user_session");
        System.out.println("deleteComment - user_session = " + user_session.getUser_id());
        if (user_session != null) {
            CommentDTO existingComment = commentService.getCommentById(comment_id);
            // System.out.println("existingComment = " + existingComment.toString());

            if (existingComment != null && existingComment.getUser_id().equals(user_session.getUser_id())) {
                commentService.deleteComment(comment_id);
                return ResponseEntity.ok("Comment deleted successfully.");
            } else {
                return ResponseEntity.status(403).body("Unauthorized action.");
            }
        } else {
            return ResponseEntity.status(403).body("User is not logged in.");
        }
    }

}