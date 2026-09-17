package com.example.hfh.mapper;

import com.example.hfh.entity.ColumnInfo;
import com.example.hfh.entity.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DatabaseMapper {

    /**
     * 查询所有表信息 - 简化版本
     */
    @Select("SELECT " +
            "table_name as tableName, " +
            "table_schema as tableSchema " +
            "FROM information_schema.tables " +
            "WHERE table_schema = 'public' " +
            "AND table_type = 'BASE TABLE' " +
            "ORDER BY table_name")
    List<TableInfo> selectAllTables();

    /**
     * 查询指定表的列信息 - 最简版本
     */
    @Select("SELECT " +
            "column_name as columnName, " +
            "data_type as dataType, " +
            "is_nullable = 'YES' as isNullable, " +
            "character_maximum_length as characterMaximumLength, " +
            "column_default as columnDefault " +
            "FROM information_schema.columns " +
            "WHERE table_name = #{tableName} " +
            "AND table_schema = 'public' " +
            "ORDER BY ordinal_position")
    List<ColumnInfo> selectColumnsByTableName(@Param("tableName") String tableName);

    /**
     * 查询主键信息 - 简化版本
     */
    @Select("SELECT kcu.column_name " +
            "FROM information_schema.table_constraints tc " +
            "JOIN information_schema.key_column_usage kcu " +
            "ON tc.constraint_name = kcu.constraint_name " +
            "WHERE tc.constraint_type = 'PRIMARY KEY' " +
            "AND tc.table_name = #{tableName} " +
            "AND tc.table_schema = 'public'")
    List<String> selectPrimaryKeys(@Param("tableName") String tableName);

    /**
     * 查询表注释信息 - 简化版本
     */
    @Select("SELECT description as tableComment " +
            "FROM pg_description " +
            "WHERE objoid = (SELECT oid FROM pg_class WHERE relname = #{tableName}) " +
            "AND objsubid = 0")
    String selectTableComment(@Param("tableName") String tableName);
}