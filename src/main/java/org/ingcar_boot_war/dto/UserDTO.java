package org.ingcar_boot_war.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private String user_id; //
    private String user_password;
    private String user_name;
    private String user_birth;
    private String user_email;
    private String user_gender;
    private String user_phone;
    private String user_area;
    private String user_comment;
    private String user_role;
    private String use_YN;
}