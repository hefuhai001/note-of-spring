package com.example.springboot.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Employee", description = "$!{table.comment}")
public class Emp {
    @ExcelProperty("ID")
    @Schema(description = "ID")
    private int id;
    @ExcelProperty("用户名")
    @Schema(description = "用户名")
    private String names;
    @ExcelProperty("工资")
    @Schema(description = "工资")
    private double salary;
    @ExcelProperty("生日")
    @Schema(description = "生日")
    private Date birthday;
    @ColumnWidth(20)
    @ExcelProperty("头像")
    @Schema(description = "头像")
    private String photo;

//    @ColumnWidth(20)
//    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ExcelProperty("创建日期")
//    private Date u_create_time;
}
