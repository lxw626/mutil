package util.generator;

import util.generator.entity.MgConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:45
 */
public class ServiceImplGenerator extends BasicGenerator {

    public ServiceImplGenerator(MgConfig mGConfig){
        this.mGConfig = mGConfig;
    }

    public List<String> add() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %s(%s %s) {",mGConfig.getAddName(),mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        list.add(2,"return %sMapper.%s(%s);",mGConfig.getEntityName4Camel(),mGConfig.getAddName(),mGConfig.getEntityName4Camel());
        list.add(1,"}");
        return list.getArrayList();
    }
    public List<String> deleteByPrimaryKey() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %sByPrimaryKey(Integer id) {",mGConfig.getDeleteName());
        list.add(2,"return %sMapper.%sByPrimaryKey(id);",mGConfig.getEntityName4Camel(),mGConfig.getDeleteName());
        list.add(1,"}");
        return list.getArrayList();
    }

    public List<String> updateByPrimaryKey() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %sByPrimaryKey(%s %s) {",mGConfig.getUpdateName(),mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        list.add(2,"return %sMapper.%sByPrimaryKey(%s);",mGConfig.getEntityName4Camel(),mGConfig.getUpdateName(),mGConfig.getEntityName4Camel());
        list.add(1,"}");
        return list.getArrayList();
    }
    public List<String> findByPrimaryKey() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public %s %sByPrimaryKey(Integer id) {",mGConfig.getEntityName(),mGConfig.getSelectName());
        list.add(2,"return %sMapper.%sByPrimaryKey(id);",mGConfig.getEntityName4Camel(),mGConfig.getSelectName());
        list.add(1,"}");
        return list.getArrayList();
    }
    public List<String> find() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public List<%s> %s(Object param) {",mGConfig.getEntityName(),mGConfig.getSelectName());
        list.add(2,"return %sMapper.%s(param);",mGConfig.getEntityName4Camel(),mGConfig.getSelectName());
        list.add(1,"}");
        return list.getArrayList();
    }
    public List<String> packages() {
        MList list = new MList();
        list.add("import %s.%s;",mGConfig.getEntityPackage(),mGConfig.getEntityName());
        list.add("import %s.%sMapper;",mGConfig.getMapperPackage(),mGConfig.getEntityName());
        list.add("import %s.%sService;",mGConfig.getServicePackage(),mGConfig.getEntityName());
        list.add("import org.springframework.beans.factory.annotation.Autowired;");
        list.add("import org.springframework.stereotype.Service;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }
    /**
     * 类的开始部分
     * @return
     */
    public List<String> classStart() {
        MList list = new MList();
        list.add("@Service");
        list.add("public class %sServiceImpl implements %sService{",mGConfig.getEntityName(),mGConfig.getEntityName());
        list.add("");
        list.add(1,"@Autowired");
        list.add(1,"%sMapper %sMapper;",mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s.impl;",mGConfig.getServicePackage()));
        list.add("");
        list.addAll(packages());
        list.add("");
        list.addAll(classStart());
        list.add("");
        list.addAll(add());
        list.add("");
        list.addAll(deleteByPrimaryKey());
        list.add("");
        list.addAll(updateByPrimaryKey());
        list.add("");
        list.addAll(findByPrimaryKey());
        list.add("");
        list.addAll(find());
        list.add("");
        list.add("}");
        return list;
    }
    public static void main(String[] args) {
        MgConfig mgConfig = new MgConfig("dept");
        List<String> contents = new ServiceImplGenerator(mgConfig).content();
        for (String content : contents) {
            System.out.println(content);
        }
        String fileName = String.format("src/main/java/com/lxw/service/impl/%sServiceImpl.java", mgConfig.getEntityName());
        generate(contents,fileName,false);

    }

}
