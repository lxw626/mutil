package com.lxw.mutil.component.generator;

import com.lxw.mutil.config.DefaultConfig;
import lombok.Data;
import lombok.NonNull;
import util.dbUtil.MColumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-19 17:40
 */
@Data
public class MgConfig extends DefaultConfig {

    private Boolean Enabled;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类名
     */
    private String entityName;

    /**
     * 表中默认排序字段
     */
    private String defaultSort;

    private String entityName4Camel;

    private List<MColumn> columns;

    // 以下为全局配置

    private Integer gId;

    private String uid;


    /**
     * 数据源信息
     */
    private String dbUrl;

    private String dbUserName;
    private String dbPassword;
    private String dbType;

    /**
     * 作者
     */
    private String author;
    /**
     * 创建时间
     */
    private String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    /**
     * 核心包名
     */
    private String group;


    /**
     * 实体类包名
     */
    private String entityPackage;
    /**
     * controller包名
     */
    private String controllerPackage;
    /**
     * service包名
     */
    private String servicePackage;
    /**
     * mapper接口包名
     */
    private String mapperPackage;

    private String basicEntity;
    /**
     * (BasicEntity中的)排序字段
     */
    private String sortInfo = "sortInfo";
    /**
     * 生成xml时是否加入排序子句
     */
    private Boolean sortCode;
    /**
     * 生成entity时是否采用lombok
     */
    private Boolean lombok;
    /**
     * 是否生成实体类的注释
     */
    private Boolean fieldComment;
    /**
     * 是否生成toString方法
     */
    private Boolean toStringMethod;

    private String createTimeFormat;
    /**
     * MResponse全名
     */
    private String mResponseNameFull;

    /**
     * 生成的小数类型
     * 默认Double,可选BigDecimal
     */
    private String numberType;
    /**
     * add insert save 可选
     */
    private String addName;
    /**
     * get find select 可选
     */
    private String selectName;
    private String updateName;
    private String deleteName;


    /**
     * 附加的一些注释,比如作者,创建时间
     */
    private List<String> extraMethodNote;


    /**
     * 新增注释
     */
    private MList addNote;
    /**
     * 根据主键删除注释
     */
    private MList deleteByPrimaryKeyNote;
    /**
     * 根据主键更新注释
     */
    private MList updateByPrimaryKeyNote;
    /**
     * 根据主键查询注释
     */
    private MList findByPrimaryKeyNote;
    /**
     * 分页查询注释
     */
    private MList findByPageNote;

    /**
     * 条件查询
     */
    private MList findNote;

    // vue部分
    /**
     * 分页列表
     */
    private String pageSizes;
    /**
     * 每页显示的条数
     */
    private Integer pageSize;


}
