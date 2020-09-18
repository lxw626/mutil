package com.lxw.mutil.component.generator;

import com.lxw.mutil.component.AutoMgConfig;
import com.lxw.mutil.config.DefaultConfig;
import com.lxw.mutil.entity.MgGlobalConfig;
import com.lxw.mutil.entity.MgTableConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import util.MStringUtil;
import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author lixiewen
 * @create 2020-01-08 10:17
 */
public class EntityGenerator extends BasicGenerator {


    public static List<String> content(MgConfig mgConfig) {
        MList list = new MList();
        Set<String> packages = new HashSet<>();
        List<String> fields = new ArrayList<>();
        List<String> setAndGetMethods = new ArrayList<>();
        for (MColumn column : mgConfig.getColumns()) {
            // 生成需要导入的包
            String fieldType = column.getFieldType();
            Set<String> keys = DefaultConfig.getRequisitePackages().keySet();
            if(keys.contains(fieldType)){
                String requisitePackage = DefaultConfig.getRequisitePackages().get(fieldType);
                packages.add(String.format("import %s;",requisitePackage));
            }
            // 生成成员变量及注释
            List<String> filed = filedGenerator(mgConfig,column);
            fields.addAll(filed);
            // 生成get方法
            List<String> getMethod = getMethodGenerator(column);
            setAndGetMethods.addAll(getMethod);
            // 生成set方法
            List<String> setMethod = setMethodGenerator(column);
            setAndGetMethods.addAll(setMethod);
        }
        // 生成包名
        list.add(String.format("package %s;",mgConfig.getEntityPackage()));
        list.add("");
        List importPackageList = new ArrayList(packages);
        if(mgConfig.getLombok()){
            importPackageList.add("import lombok.Data;");
            importPackageList.add("import lombok.experimental.Accessors;");
        }
        Collections.sort(importPackageList);
        list.addAll(new ArrayList(importPackageList));
        if (packages.size()>0){
            list.add("");
        }
        List<String> classStart = classStart(mgConfig);
        list.addAll(classStart);
        list.addAll(fields);
        if(!mgConfig.getLombok()){
            list.addAll(setAndGetMethods);
            if(mgConfig.getToStringMethod()){
                List<String> toStringMethod = toStringMethodGenerator(mgConfig);
                list.addAll(toStringMethod);
            }
        }
        list.add("}");
        return list.getArrayList();
    }

    /**
     * 字段(注释)模型
     * @param column
     * @return
     */
    private static List<String> filedGenerator(MgConfig mgConfig,MColumn column) {
        MList list = new MList();
        String fieldType = column.getFieldType();
        String fieldName = column.getFieldName();
        String fieldComment = column.getColumnComment();
        if(mgConfig.getFieldComment() && fieldComment !=null && fieldComment.trim().length()>0){
            list.add(1,"/**");
            list.add(1," * %s",fieldComment);
            list.add(1," */");
        }
        list.add(1,"private %s %s;",fieldType,fieldName);
        list.add("");
        return list.getArrayList();
    }

    /**
     * getMethod
     * @param column
     * @return
     */
    private static List<String> getMethodGenerator(MColumn column) {
        String fieldType = column.getFieldType();
        String fieldName = column.getFieldName();
        MList list = new MList();
        String s = MStringUtil.toUpperCaseFirstOne(fieldName);
        list.add(1,"public %s get%s() {",fieldType,s);
        list.add(2,"return %s;",fieldName);
        list.add(1,"}");
        list.add("");
        return list.getArrayList();
    }

    /**
     * setMethod
     * @param column
     * @return
     */
    private static List<String> setMethodGenerator(MColumn column) {
        String fieldType = column.getFieldType();
        String fieldName = column.getFieldName();
        MList list = new MList();
        String s = MStringUtil.toUpperCaseFirstOne(fieldName);
        list.add(1,"public void set%s(%s %s) {",s,fieldType,fieldName);
        list.add(2,"this.%s = %s;",fieldName,fieldName);
        list.add(1,"}");
        list.add("");
        return list.getArrayList();
    }

    /**
     * toString方法
     * @param columns
     * @return
     */
    private static List<String> toStringMethodGenerator(MgConfig mgConfig) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public String toString() {");
        list.add(2,"return \"%s{\" +",mgConfig.getEntityName());
        for (int i = 0; i < mgConfig.getColumns().size(); i++) {
            String fieldName = mgConfig.getColumns().get(i).getFieldName();
            String fieldType = mgConfig.getColumns().get(i).getFieldType();
            if(i == 0){
                if("String".equals(fieldType)){
                    list.add(2,"\" %s='\" + %s + '\\\'' +",fieldName,fieldName);
                }else{
                    list.add(2,"\" %s=\" + %s  +",fieldName,fieldName);
                }
            }else {
                list.add(2,"\", %s='\" + %s + '\\\'' +",fieldName,fieldName);
            }
        }
        list.add(2,"'}';");
        list.add(1,"}");
        return list.getArrayList();
    }

    /**
     * 类开始部分
     * @return
     */
    private static List<String> classStart(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getLombok()){
            list.add("@Data");
            list.add("@Accessors(chain = true)");
        }
        String classStart = String.format("public class %s {",mgConfig.getEntityName());
        if(mgConfig.getBasicEntity() != null && mgConfig.getBasicEntity().trim().length()>0){
            classStart = String.format("public class %s extends %s {",mgConfig.getEntityName(),mgConfig.getBasicEntity());
        }
        list.add(classStart);
        list.add("");

        return list.getArrayList();
    }

    public static void main(String[] args) throws SQLException {
        MgGlobalConfig mgGlobalConfig = new MgGlobalConfig();
        mgGlobalConfig.setDbUserName("rj_user");
        mgGlobalConfig.setDbPassword("rjuser2016");
        mgGlobalConfig.setDbUrl("jdbc:mysql://172.16.3.64:3306/RJPP?useUnicode=true&characterEncoding=utf8&ssl=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull");
        mgGlobalConfig.setGroup("com.lxw.mutil");
        MgTableConfig mgTableConfig = new MgTableConfig();
        mgTableConfig.setTableName("rule_precise_correlation");
        mgTableConfig.setLombok(false);
        MgConfig mgConfig = new MgConfig();
        BeanUtils.copyProperties(mgGlobalConfig,mgConfig);
        System.out.println(mgConfig);
        BeanUtils.copyProperties(mgTableConfig,mgConfig,getNullPropertyNames(mgTableConfig));
        System.out.println(mgConfig);
        AutoMgConfig.config(mgConfig);

        // 准备 columns
        Connection connection = MDBUtil.getConnection(mgConfig.getDbUrl(), mgConfig.getDbUserName(), mgConfig.getDbPassword());
        List<MColumn> columns = getColumns(connection,mgConfig.getDbType(),mgConfig.getTableName());
        mgConfig.setColumns(columns);
        // 以上为数据准备

        List<String> contents = content(mgConfig);
        for (String content : contents) {
            System.out.println(content);
        }
        String basicPath = mgConfig.getGroup().replaceAll("\\.","/");
        String fileName = String.format("src/main/java/%s/entity/%s.java",basicPath,mgConfig.getEntityName());
        generate(contents,fileName,true);
        MDBUtil.closeConnection(connection);
    }
    // todo 这个方法放到哪里好呢
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
