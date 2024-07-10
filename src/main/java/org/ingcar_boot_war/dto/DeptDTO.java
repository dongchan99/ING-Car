package org.ingcar_boot_war.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeptDTO {
    int deptno;
    String dname;
    String loc;
}
