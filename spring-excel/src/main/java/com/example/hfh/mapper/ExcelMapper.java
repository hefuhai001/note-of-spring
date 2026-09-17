package com.example.hfh.mapper;

import com.example.hfh.entity.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExcelMapper {

    List<Emp> selectEmpAll();

    String selectById(Integer id);

    boolean insertById(Emp entity);

    boolean deleteById(Integer id);
}
