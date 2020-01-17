package util.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:45
 */
public class ServiceImplGenerator extends BasicGenerator {

    public static List<String> add() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %s(%s %s) {",cName,entityName,entityName_camel);
        list.add(2,"return %sMapper.%s(%s);",entityName_camel,cName,entityName_camel);
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> deleteByPrimaryKey() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %sByPrimaryKey(Integer id) {",dName);
        list.add(2,"return %sMapper.%sByPrimaryKey(id);",entityName_camel,dName);
        list.add(1,"}");
        return list.getArrayList();
    }

    public static List<String> updateByPrimaryKey() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public int %sByPrimaryKey(%s %s) {",uName,entityName,entityName_camel);
        list.add(2,"return %sMapper.%sByPrimaryKey(%s);",entityName_camel,uName,entityName_camel);
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> findByPrimaryKey() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public %s %sByPrimaryKey(Integer id) {",entityName,rName);
        list.add(2,"return %sMapper.%sByPrimaryKey(id);",entityName_camel,rName);
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> find() {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public List<%s> %s(%s param) {",entityName,rName,entityName);
        list.add(2,"return %sMapper.%s(param);",entityName_camel,rName);
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> packages() {
        MList list = new MList();
        list.add("import %s;",entityName_full);
        list.add("import %s;",mapperName_full);
        list.add("import org.springframework.beans.factory.annotation.Autowired;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }
    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart() {
        MList list = new MList();
        list.add("public class %sServiceImpl implements %sService{",entityName,entityName);
        list.add("");
        list.add(1,"@Autowired");
        list.add(1,"%sMapper %sMapper;",entityName,entityName_camel);
        return list.getArrayList();
    }
    public static List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s.impl;",servicePackageName));
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
        List<String> content = content();
        String fileName = String.format("src/main/java/com/lxw/service/impl/%sServiceImpl.java", entityName);
        generate(content,fileName);

    }

}
