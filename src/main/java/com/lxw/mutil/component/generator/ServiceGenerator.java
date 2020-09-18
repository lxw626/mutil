package com.lxw.mutil.component.generator;

import com.lxw.mutil.component.AutoMgConfig;
import com.lxw.mutil.config.DefaultConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:45
 */
public class ServiceGenerator extends BasicGenerator {


    public static List<String> add(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getAddNote() != null){
            list.addAll(mgConfig.getAddNote());
        }else{
            list.addAll(DefaultConfig.getDefaultAddNote(mgConfig.getEntityName4Camel()));
        }
        list.add(1,"int add(%s %s);",mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public static List<String> deleteByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getDeleteByPrimaryKeyNote() != null){
            list.addAll(mgConfig.getDeleteByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultDeleteByPrimaryKeyNote());
        }
        list.add(1,"int deleteByPrimaryKey(Integer id);");
        return list.getArrayList();
    }

    public static List<String> updateByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getUpdateByPrimaryKeyNote() != null){
            list.addAll(mgConfig.getUpdateByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultUpdateByPrimaryKeyNote(mgConfig.getEntityName4Camel()));
        }
        list.add(1,"int updateByPrimaryKey(%s %s);",mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public static List<String> findByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getFindByPrimaryKeyNote() != null){
            list.addAll(mgConfig.getFindByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindByPrimaryKeyNote());
        }
        list.add(1,"%s findByPrimaryKey(Integer id);",mgConfig.getEntityName());
        return list.getArrayList();
    }
    public static List<String> find(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getFindNote() != null){
            list.addAll(mgConfig.getFindNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindNote());
        }
        list.add(1,"List<%s> find(Object param);",mgConfig.getEntityName());
        return list.getArrayList();
    }
    public static List<String> packages(MgConfig mgConfig) {
        MList list = new MList();
        list.add("import %s.%s;",mgConfig.getEntityPackage(),mgConfig.getEntityName());
        list.add("import java.util.List;");
        return list.getArrayList();
    }
    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart(MgConfig mgConfig) {
        MList list = new MList();
        list.add("public interface %sService {",mgConfig.getEntityName());
        return list.getArrayList();
    }
    public static List<String> content(MgConfig mgConfig) {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",mgConfig.getServicePackage()));
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
        String fileName = String.format("src/main/java/com/lxw/service/%sService.java", mgConfig.getEntityName());
        generate(contents,fileName,false);
    }

}
