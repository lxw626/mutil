package util.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:45
 */
public class ServiceGenerator extends BasicGenerator {

    public static List<String> add() {
        MList list = new MList();
        list.addAll(addNote);
        list.add(1,"int add(%s %s);",entityName,entityName_camel);
        return list.getArrayList();
    }
    public static List<String> deleteByPrimaryKey() {
        MList list = new MList();
        list.addAll(deleteByPrimaryKeyNote);
        list.add(1,"int deleteByPrimaryKey(Integer id);");
        return list.getArrayList();
    }

    public static List<String> updateByPrimaryKey() {
        MList list = new MList();
        list.addAll(updateByPrimaryKeyNote);
        list.add(1,"int updateByPrimaryKey(%s %s);",entityName,entityName_camel);
        return list.getArrayList();
    }
    public static List<String> findByPrimaryKey() {
        MList list = new MList();
        list.addAll(findByPrimaryKeyNote);
        list.add(1,"%s findByPrimaryKey(Integer id);",entityName);
        return list.getArrayList();
    }
    public static List<String> find() {
        MList list = new MList();
        list.addAll(getXxxsByPageNote);
        list.add(1,"List<%s> find(Object param);",entityName);
        return list.getArrayList();
    }
    public static List<String> packages() {
        MList list = new MList();
        list.add("import %s;",entityName_full);
        list.add("import java.util.List;");
        return list.getArrayList();
    }
    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart() {
        MList list = new MList();
        list.add("public interface %sService {",entityName);
        return list.getArrayList();
    }
    public static List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",servicePackageName));
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
        String fileName = String.format("src/main/java/com/lxw/service/%sService.java", entityName);
        generate(content,fileName);
    }

}
