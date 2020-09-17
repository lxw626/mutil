package com.lxw.mutil.config;

import com.lxw.mutil.component.generator.MList;

import java.util.HashMap;
import java.util.Map;

public class DefaultConfig {



    /**
     * 生成的小数类型
     * 默认Double,可选BigDecimal
     */
    public static String decimal = "Double";
    /**
     * add insert save 可选
     */
    public static String cName = "add";
    /**
     * get find select 可选
     */
    public static String rName = "find";
    public static String uName = "update";
    public static String dName = "delete";

    /**
     * 实体类中有可能要导入的包
     * todo 后期可以维护到数据库
     */
    public static Map<String, String> getRequisitePackages (){
        Map<String, String> requisitePackages = new HashMap<>();
        requisitePackages.put("Date","java.util.Date");
        requisitePackages.put("BigDecimal","java.math.BigDecimal");
        return requisitePackages;
    };


    /**
     * 新增注释
     */
    public static MList getDefaultAddNote(String entityName4Camel) {
        MList addNote = new MList();
        // 新增部分注释
        addNote.add(1, "/**");
        addNote.add(1, " * 新增");
        addNote.add(1, " * @param " + entityName4Camel);
        addNote.add(1, " * @return");
        addNote.add(1, " */");
        return addNote;
    }

    /**
     * 根据主键删除注释
     */
    public static MList getDefaultDeleteByPrimaryKeyNote() {
        MList deleteByPrimaryKeyNote = new MList();
        // 删除部分注释
        deleteByPrimaryKeyNote.add(1, "/**");
        deleteByPrimaryKeyNote.add(1, " * 根据主键删除");
        deleteByPrimaryKeyNote.add(1, " * @param id");
        deleteByPrimaryKeyNote.add(1, " * @return");
        deleteByPrimaryKeyNote.add(1, " */");
        return deleteByPrimaryKeyNote;
    }
    /**
     * 根据主键更新注释
     */
    public static MList getDefaultUpdateByPrimaryKeyNote(String entityName4Camel) {
        MList updateByPrimaryKeyNote = new MList();
        updateByPrimaryKeyNote.add(1, "/**");
        updateByPrimaryKeyNote.add(1, " * 根据主键修改");
        updateByPrimaryKeyNote.add(1, " * @param %s", entityName4Camel);
        updateByPrimaryKeyNote.add(1, " * @return");
        updateByPrimaryKeyNote.add(1, " */");
        return updateByPrimaryKeyNote;
    }

    /**
     * 根据主键查询注释
     */
    public static MList getDefaultFindByPrimaryKeyNote(){
        MList findByPrimaryKeyNote = new MList();
        findByPrimaryKeyNote.add(1, "/**");
        findByPrimaryKeyNote.add(1, " * 根据主键查找");
        findByPrimaryKeyNote.add(1, " * @param id 主键");
        findByPrimaryKeyNote.add(1, " * @return");
        findByPrimaryKeyNote.add(1, " */");
        return findByPrimaryKeyNote;
    }
    /**
     * 分页查询注释
     */
    public static MList getDefaultFindXxxsByPageNote() {
        MList findXxxsByPageNote = new MList();
        // 分页查询注释
        findXxxsByPageNote.add(1, "/**");
        findXxxsByPageNote.add(1, " * 分页查询");
        findXxxsByPageNote.add(1, " * @param param");
        findXxxsByPageNote.add(1, " * @return");
        findXxxsByPageNote.add(1, " */");
        return findXxxsByPageNote;
    }
    /**
     * 条件查询注释
     */
    public static MList getDefaultFindNote() {
        MList findNote = new MList();
        findNote.add(1, "/**");
        findNote.add(1, " * 条件查询");
        findNote.add(1, " * @param param 可以是实体类,也可以是Map");
        findNote.add(1, " * @return");
        findNote.add(1, " */");
        return findNote;
    }

    // vue部分
    /**
     * 分页列表
     */
    public static String pageSizes = "[3,5,10,15]";
    /**
     * 每页显示的条数
     */
    public static int pageSize = 3;

}
