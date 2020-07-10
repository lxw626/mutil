package util.generator;

import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;
import util.generator.entity.MgConfig;

import java.sql.Connection;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-14 17:13
 */
public class XmlGenerator extends BasicGenerator {
    public XmlGenerator(MgConfig mGConfig){
        this.mGConfig = mGConfig;
    }
    public List<String> baseResultMap(List<MColumn> columns) {
        MList list = new MList();
        list.add(1,"<resultMap id=\"BaseResultMap\" type=\"%s.%s\" >",mGConfig.getEntityPackage(),mGConfig.getEntityName());
        for (MColumn column : columns) {
            Boolean primaryKey = column.getPrimaryKey();
            String columnName = column.getColumnName();
            String fieldName = column.getFieldName();
            if(primaryKey){
                list.add(2,"<id column=\"%s\" property=\"%s\" />",columnName,fieldName);
            }else{
                list.add(2,"<result column=\"%s\" property=\"%s\" />",columnName,fieldName);
            }
        }
        list.add(1,"</resultMap>");
        return list.getArrayList();
    }
    public List<String> baseColumnList(List<MColumn> columns) {
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
    public List<String> whereClause(List<MColumn> columns) {
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
    public List<String> insert(List<MColumn> columns,String tableName) {
        MList list = new MList();
        list.add(1," <insert id=\"%s\" parameterType=\"%s.%s\">",mGConfig.getAddName(),mGConfig.getEntityPackage(),mGConfig.getEntityName());
        list.add(2,"INSERT INTO %s",tableName);
        MList insertColumns = new MList();
        insertColumns.add(2,"<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        MList insertValues = new MList();
        insertValues.add(2,"<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        for (MColumn column : columns) {
            String fieldName = column.getFieldName();
            String columnName = column.getColumnName();
            insertColumns.add(3,"<if test ='%s != null'>%s,</if>",fieldName,columnName);
            insertValues.add(3,"<if test ='%s != null'>#{%s},</if>",fieldName,fieldName);
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
    public List<String> deleteByPrimaryKey(MColumn primaryKeyColumn,String tableName) {
        MList list = new MList();
        String columnName = primaryKeyColumn.getColumnName();
        String fieldName = primaryKeyColumn.getFieldName();
        list.add(1,"<delete id=\"%s\" >",mGConfig.getDeleteName());
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
    public List<String> updateByPrimaryKey(List<MColumn> columns,MColumn primaryKeyColumn,String tableName) {
        MList list = new MList();
        list.add(1, "<update id=\"%sByPrimaryKey\" >", mGConfig.getUpdateName());
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



    public List<String> findByPrimaryKey(MColumn primaryKeyColumn,String tableName) {
        MList list = new MList();
        String columnName = primaryKeyColumn.getColumnName();
        String fieldName = primaryKeyColumn.getFieldName();
        list.add(1,"<select id=\"%sByPrimaryKey\" resultMap=\"BaseResultMap\">",mGConfig.getSelectName());
        list.add(2,"SELECT <include refid=\"BaseColumnList\"/> FROM %s WHERE %s = #{%s}",tableName,columnName,fieldName);
        list.add(1,"</select>");
        return list.getArrayList();
    }

    public List<String> find(String tableName) {
        MList list = new MList();
        // parameterType 可以不写让mybatis自动识别
        list.add(1,"<select id=\"%s\" resultMap=\"BaseResultMap\">",mGConfig.getSelectName());
        list.add(2,"SELECT <include refid=\"BaseColumnList\"/> FROM %s <include refid=\"WhereClause\"/>",tableName);
        if(mGConfig.isSort()){
            list.add(2,"<if test=\"%s != null\">ORDER BY ${%s}</if>",mGConfig.getSortInfo(),mGConfig.getSortInfo());
        }
        list.add(1,"</select>");
        return list.getArrayList();
    }
    public List<String> xmlStart() {
        MList list = new MList();
        list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        list.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        list.add("<mapper namespace=\"%s.%sMapper\">",mGConfig.getMapperPackage(),mGConfig.getEntityName());
        return list.getArrayList();
    }
    public List<String> content(List<MColumn> columns){
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
        List<String> insert = insert(columns,mGConfig.getTableName());
        list.addAll(insert);
        // 如果有主键则生成deleteByPrimaryKey updateByPrimaryKey findByPrimaryKey
        for (MColumn column : columns) {
            Boolean primaryKey = column.getPrimaryKey();
            if(primaryKey){
                List<String> deleteByPrimaryKey = deleteByPrimaryKey(column,mGConfig.getTableName());
                list.addAll(deleteByPrimaryKey);
                List<String> updateByPrimaryKey = updateByPrimaryKey(columns,column,mGConfig.getTableName());
                list.addAll(updateByPrimaryKey);
                List<String> findByPrimaryKey = findByPrimaryKey(column,mGConfig.getTableName());
                list.addAll(findByPrimaryKey);
                break;
            }
        }
        List<String> find = find(mGConfig.getTableName());
        list.addAll(find);
        // xml结束
        list.add("</mapper>");
        return list.getArrayList();
    }

    public static void main(String[] args) {
        MgConfig mGConfig = new MgConfig("dept");
        Connection connection = MDBUtil.getConnection(mGConfig.getUrl(), mGConfig.getDbUserName(), mGConfig.getDbPassword());
        List<MColumn> columns = getColumns(connection,mGConfig.getDbType(),mGConfig.getTableName());
        List<String> content = new XmlGenerator(mGConfig).content(columns);
        String fileName = String.format("src/main/resources/com/lxw/mapper/%sMapper.xml",mGConfig.getEntityName());
        generate(content,fileName,true);
        MDBUtil.closeConnection(connection);
    }
}
