/*
    공지사항 InformDTO.java
*/

package org.ingcar_boot_war.dto;

import java.sql.Date;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data /* Lombok annotation */
@NoArgsConstructor /* 매개변수가 없는 기본 생성자를 생성 */
public class InformDTO {
    private Long post_id;
    private String post_writer;
    private String post_title;
    private String post_precontent;
    private String post_content;
    private String post_image;
    private Date created_at; // write date


}