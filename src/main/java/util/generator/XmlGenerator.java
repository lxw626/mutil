package util.generator;

import util.dbUtil.MColumn;

import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-14 17:13
 */
public class XmlGenerator extends BasicGenerator {
    public static List<String> baseResultMap(List<MColumn> columns) {
        MList list = new MList();
        list.add(1,"<resultMap id=\"BaseResultMap\" type=\"%s\" >",entityName_full);
        for (MColumn column : columns) {
            Boolean primaryKey = column.getPrimaryKey();
            String columnName = column.getColumnName();
            String fieldName = column.getFieldName();
            if(primaryKey){
                list.add(2,"<id id=\"%s\" property=\"%s\" />",columnName,fieldName);
            }else{
                list.add(2,"<result column=\"%s\" property=\"%s\" />",columnName,fieldName);
            }
        }
        list.add(1,"</resultMap>");
        return list.getArrayList();
    }
    public static List<String> baseColumnList(List<MColumn> columns) {
        MList list = new MList();
        StringBuilder sb = new StringBuilder();
        for (MColumn column : columns) {
            sb.append(column.getColumnName()).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        list.add(1,"<sql id=\"BaseColumnList\">");
        // todo 控制每行显示多少个字
        list.add(2,sb.toString());
        list.add(1," </sql>");
        return list.getArrayList();
    }
    public static List<String> whereClause(List<MColumn> columns) {
        MList list = new MList();
        list.add(1,"<sql id=\"WhereClause\">");
        list.add(2,"<where>");
        for (MColumn column : columns) {
            String columnName = column.getColumnName();
            String fieldName = column.getFieldName();
            String jdbcType = column.getJdbcType();
            String fieldType = column.getFieldType();
            String test = String.format("<if test=\"%s != null\">and %s = #{%s,jdbcType=%s}</if>",
                fieldName,columnName,fieldName,jdbcType);
            if("String".equals(fieldType)){
                test = String.format("<if test=\"%s != null and %s != ''\">and %s = #{%s,jdbcType=%s}</if>",
                    fieldName,fieldName,columnName,fieldName,jdbcType);
            }
            list.add(3,test);
        }
        list.add(2,"</where>");
        list.add(1,"</sql>");
        return list.getArrayList();
    }
    public static List<String> insert(List<MColumn> columns,String tableName) {
        MList list = new MList();
        list.add(1," <insert id=\"%s\" parameterType=\"%s\">",cName,entityName_full);
        list.add(2,"INSERT INTO %s",tableName);
        MList insertColumns = new MList();
        insertColumns.add(2,"<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        MList insertValues = new MList();
        insertValues.add(2,"<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        for (MColumn column : columns) {
            String fieldName = column.getFieldName();
            String columnName = column.getColumnName();
            insertColumns.add(3,"<if test ='%s != null'>%s,</if>",fieldName,columnName);
            insertValues.add(3,"<if test ='%s != null'>#{%s},</if>",fieldName,columnName);
        }
        insertColumns.add(2,"</trim>");
        insertValues.add(2,"</trim>");
        list.addAll(insertColumns);
        list.addAll(insertValues);
        list.add(1,"</insert>");
        return list.getArrayList();
    }

    /**
     * 根据第一个主键删除,如果没有主键则不会生成这部分代码
     * todo 记得在controller service ... 部分也加条件生成
     * @param primaryKeyColumn 主键列
     * @param tableName 表名
     * @return
     */
    public static List<String> deleteByPrimaryKey(MColumn primaryKeyColumn,String tableName) {
        MList list = new MList();
        String columnName = primaryKeyColumn.getColumnName();
        String fieldName = primaryKeyColumn.getFieldName();
        list.add(1,"<delete id=\"%s\" >",dName);
        list.add(2,"DELETE FROM %s WHERE %s = #{%s}",tableName,columnName,fieldName);
        list.add(1,"</delete>");
        return list.getArrayList();
    }

    /**
     * 根据第一个主键更新,如果没有主键则不会生成这部分代码
     * todo 记得在controller service ... 部分也加条件生成
     * @param columns
     * @param tableName
     * @return
     */
    public static List<String> updateByPrimaryKey(List<MColumn> columns,MColumn primaryKeyColumn,String tableName) {
        MList list = new MList();
        list.add(1, "<update id=\"%s\" >", dName);
        list.add(2, "UPDATE %s ", tableName);
        list.add(2, "<set>");
        String primaryKeyColumnName = primaryKeyColumn.getColumnName();
        String primaryKeyFieldName = primaryKeyColumn.getFieldName();
        for (MColumn column : columns) {
            String columnName = column.getColumnName();
            String fieldName = column.getFieldName();
            if(!primaryKeyColumnName.equals(columnName)){
                list.add(3, "<if test ='%s != null'>%s = #{%s},</if>",fieldName,columnName,fieldName);
            }
        }
        list.add(2, "</set>");
        list.add(2, "WHERE %s = #{%s}",primaryKeyColumnName,primaryKeyFieldName);
        list.add(1, "</update>");
        return list.getArrayList();
    }



    public static List<String> findByPrimaryKey(MColumn primaryKeyColumn,String tableName) {
        MList list = new MList();
        String columnName = primaryKeyColumn.getColumnName();
        String fieldName = primaryKeyColumn.getFieldName();
        list.add(1,"<select id=\"%sByPrimaryKey\" resultMap=\"BaseResultMap\">",rName);
        list.add(2,"SELECT <include refid=\"BaseColumnList\"/> FROM %s WHERE %s = #{%s}",tableName,columnName,fieldName);
        list.add(1,"</select>");
        return list.getArrayList();
    }

    public static List<String> find(String tableName) {
        MList list = new MList();
        // parameterType 可以不写让mybatis自动识别
        list.add(1,"<select id=\"%s\" resultMap=\"BaseResultMap\">",rName);
        list.add(2,"SELECT <include refid=\"BaseColumnList\"/> FROM %s <include refid=\"WhereClause\"/>",tableName);
        list.add(2,"ORDER BY <if test=\"order != null\">${order}</if>");
        list.add(1,"</select>");
        return list.getArrayList();
    }
    public static List<String> xmlStart() {
        MList list = new MList();
        list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        list.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        list.add("<mapper namespace=\"%s\">",mapperName_full);
        return list.getArrayList();
    }
    public static List<String> content(List<MColumn> columns){
        MList list = new MList();
        // xml开始部分
        List<String> xmlStart = xmlStart();
        list.addAll(xmlStart);
        // 结果集
        List<String> baseResultMap = baseResultMap(columns);
        list.addAll(baseResultMap);
        // 列
        List<String> baseColumnList = baseColumnList(columns);
        list.addAll(baseColumnList);
        // where条件
        List<String> whereClause = whereClause(columns);
        list.addAll(whereClause);
        List<String> insert = insert(columns,"emp");
        list.addAll(insert);
        // 如果有主键则生成deleteByPrimaryKey updateByPrimaryKey findByPrimaryKey
        for (MColumn column : columns) {
            Boolean primaryKey = column.getPrimaryKey();
            if(primaryKey){
                List<String> deleteByPrimaryKey = deleteByPrimaryKey(column,"emp");
                list.addAll(deleteByPrimaryKey);
                List<String> updateByPrimaryKey = updateByPrimaryKey(columns,column,"emp");
                list.addAll(updateByPrimaryKey);
                List<String> findByPrimaryKey = findByPrimaryKey(column,"emp");
                list.addAll(findByPrimaryKey);
                break;
            }
        }
        List<String> find = find("emp");
        list.addAll(find);
        // xml结束
        list.add("</mapper>");
        return list.getArrayList();
    }

    public static void main(String[] args) throws Exception {
        List<MColumn> columns = getColumns();
        List<String> content = content(columns);
        String fileName = String.format("src/main/resources/mapper/%s.xml",mapperName);
        generate(content,fileName);
    }
}
