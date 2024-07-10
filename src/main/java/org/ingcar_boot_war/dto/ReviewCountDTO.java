package org.ingcar_boot_war.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReviewCountDTO {
    private int board_star;
    private int review_count;

    public ReviewCountDTO(int board_star, int review_count) {
        this.board_star = board_star;
        this.review_count = review_count;
    }

}
