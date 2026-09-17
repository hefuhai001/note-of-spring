package com.example.hfh.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Employee", description = "$!{table.comment}")
public class EmpSaveParam {
    @Schema(description = "ID")
    private int id;
    @Schema(description = "用户名")
    private String names;
    @Schema(description = "工资")
    private double salary;
    @Schema(description = "生日")
    private Date birthday;
    @Schema(description = "头像")
    private String photo;
}
