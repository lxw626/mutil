package com.lxw.mutil.component.generator;

import com.lxw.mutil.component.AutoMgConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:45
 */
public class ServiceImplGenerator extends BasicGenerator {

    public static List<String> add(MgConfig mgConfig) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %s(%s %s) {",mgConfig.getAddName(),mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        list.add(2,"return %sMapper.%s(%s);",mgConfig.getEntityName4Camel(),mgConfig.getAddName(),mgConfig.getEntityName4Camel());
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> deleteByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %sByPrimaryKey(Integer id) {",mgConfig.getDeleteName());
        list.add(2,"return %sMapper.%sByPrimaryKey(id);",mgConfig.getEntityName4Camel(),mgConfig.getDeleteName());
        list.add(1,"}");
        return list.getArrayList();
    }

    public static List<String> updateByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %sByPrimaryKey(%s %s) {",mgConfig.getUpdateName(),mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        list.add(2,"return %sMapper.%sByPrimaryKey(%s);",mgConfig.getEntityName4Camel(),mgConfig.getUpdateName(),mgConfig.getEntityName4Camel());
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> findByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public %s %sByPrimaryKey(Integer id) {",mgConfig.getEntityName(),mgConfig.getSelectName());
        list.add(2,"return %sMapper.%sByPrimaryKey(id);",mgConfig.getEntityName4Camel(),mgConfig.getSelectName());
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> find(MgConfig mgConfig) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public List<%s> %s(Object param) {",mgConfig.getEntityName(),mgConfig.getSelectName());
        list.add(2,"return %sMapper.%s(param);",mgConfig.getEntityName4Camel(),mgConfig.getSelectName());
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> packages(MgConfig mgConfig) {
        MList list = new MList();
        list.add("import %s.%s;",mgConfig.getEntityPackage(),mgConfig.getEntityName());
        list.add("import %s.%sMapper;",mgConfig.getMapperPackage(),mgConfig.getEntityName());
        list.add("import %s.%sService;",mgConfig.getServicePackage(),mgConfig.getEntityName());
        list.add("import org.springframework.beans.factory.annotation.Autowired;");
        list.add("import org.springframework.stereotype.Service;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }
    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart(MgConfig mgConfig) {
        MList list = new MList();
        list.add("@Service");
        list.add("public class %sServiceImpl implements %sService{",mgConfig.getEntityName(),mgConfig.getEntityName());
        list.add("");
        list.add(1,"@Autowired");
        list.add(1,"%sMapper %sMapper;",mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public static List<String> content(MgConfig mgConfig) {
        List<String> list = new ArrayList();
        list.add(String.format("package %s.impl;",mgConfig.getServicePackage()));
        list.add("");
        list.addAll(packages(mgConfig));
        list.add("");
        list.addAll(classStart(mgConfig));
        list.add("");
        list.addAll(add(mgConfig));
        list.add("");
        list.addAll(deleteByPrimaryKey(mgConfig));
        list.add("");
        list.addAll(updateByPrimaryKey(mgConfig));
        list.add("");
        list.addAll(findByPrimaryKey(mgConfig));
        list.add("");
        list.addAll(find(mgConfig));
        list.add("");
        list.add("}");
        return list;
    }
    public static void main(String[] args) {
        MgConfig mgConfig = new MgConfig();
        AutoMgConfig.config(mgConfig);
        List<String> contents = content(mgConfig);
        for (String content : contents) {
            System.out.println(content);
        }
        String fileName = String.format("src/main/java/com/lxw/service/impl/%sServiceImpl.java", mgConfig.getEntityName());
        generate(contents,fileName,false);

    }

}
