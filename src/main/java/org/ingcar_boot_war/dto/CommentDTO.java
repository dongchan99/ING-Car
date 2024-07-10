package org.ingcar_boot_war.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private int comment_id;
    private int log_id;
    private String user_id;
    private String comment_content;
    private Date comment_date; // 유지하되, 설정을 DB에서 자동으로 하도록 함
    private int parent_id; // 대댓글을 위한 부모 댓글 ID
    private Integer child_id;  // 대댓글의 ID, 부모 댓글의 comment_id를 저장
    private int is_deleted; // 삭제 여부 판단
}
