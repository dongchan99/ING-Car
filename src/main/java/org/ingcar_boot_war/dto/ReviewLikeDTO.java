package org.ingcar_boot_war.dto;

import lombok.Data;

@Data
public class ReviewLikeDTO {
    private int like_id;
    private int log_id;
    private String user_id;
}
