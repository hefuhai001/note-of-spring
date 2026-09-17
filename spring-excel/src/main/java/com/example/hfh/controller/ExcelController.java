package com.example.hfh.controller;

import com.alibaba.excel.EasyExcel;
import com.example.hfh.req.IdParam;
import com.example.hfh.resp.Result;
import com.example.hfh.dto.EmpSaveParam;
import com.example.hfh.entity.Emp;
import com.example.hfh.mapper.ExcelMapper;
import com.example.hfh.utils.VerifyCodeUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RestController
public class ExcelController {

    @Autowired
    private ExcelMapper excelMapper;


    @Operation(summary = "用户列表")
    @GetMapping("/selectAll")
    public Result<List<Emp>> selectAll() {
        return Result.success(excelMapper.selectEmpAll());
    }

    @Operation(summary = "根据id查找用户")
    @PostMapping("/detail")
    public Result<Emp> detail(@RequestBody @Validated IdParam param) {
        return Result.success(excelMapper.selectById(param.getId()));
    }

    @Operation(summary = "增加修改用户信息")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated EmpSaveParam param) {
        Emp entity = new Emp();
        BeanUtils.copyProperties(param, entity);
        if (excelMapper.insertById(entity)) {
            return Result.success();
        } else {
            return Result.failure("保存失败，请稍后重试");
        }
    }

    @Operation(summary = "根据id删除用户")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated IdParam param) {
        if (excelMapper.deleteById(param.getId())) {
            return Result.success();
        } else {
            return Result.failure("删除失败，请稍后重试");
        }
    }

    /**
     * 导出数据
     */
    @Operation(summary = "导出Excel")
    @GetMapping("exportExcel")
    public void exportData(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        List<Emp> users = excelMapper.selectEmpAll();
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Emp.class).sheet("用户表").doWrite(users);
    }

    /**
     * 导入数据
     */
    @Operation(summary = "导入Excel")
    @PostMapping("/importExcel")
    public Integer importData(MultipartFile file) {
        try {
            //获取文件的输入流
            InputStream inputStream = file.getInputStream();
            List<Emp> lst = EasyExcel.read(inputStream) //调用read方法
                    //注册自定义监听器，字段校验可以在监听器内实现
//                    .registerReadListener(new UserListener())
                    .head(Emp.class) //对应导入的实体类
                    .sheet(0) //导入数据的sheet页编号，0代表第一个sheet页，如果不填，则会导入所有sheet页的数据
                    .headRowNumber(1) //列表头行数，1代表列表头有1行，第二行开始为数据行
                    .doReadSync(); //开始读Excel，返回一个List<T>集合，继续后续入库操作
            //模拟导入数据库操作
            for (Emp user : lst) {
                Date date = user.getBirthday();
                String form = String.format("%tF", date);
                System.out.println(form);
                System.out.println(user.getNames());
                EmpSaveParam empSaveParam = new EmpSaveParam();
                BeanUtils.copyProperties(user,empSaveParam);
                save(empSaveParam);
            }
            return 1;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Operation(summary = "生成验证码")
    @GetMapping("/generateImageCode")
    public void generateImageCode(HttpSession session, HttpServletResponse response) throws IOException {
        //随机生成四位随机数
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //保存到session域中
        session.setAttribute("code", code);
        //根据随机数生成图片，reqponse响应图片
        response.setContentType("image/png");
        ServletOutputStream os = response.getOutputStream();
        VerifyCodeUtils.outputImage(130, 60, os, code);
    }


}
