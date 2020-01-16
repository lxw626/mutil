package util.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-03 16:23
 */
public class MapperGenerator extends BasicGenerator {

    public static List<String> add() {
        MList list = new MList();
        list.add(1,"int %s(%s %s);",cName,entityName,entityName_camel);
        return list.getArrayList();
    }
    public static List<String> deleteByPrimaryKey() {
        MList list = new MList();
        // todo 主键的类型不一定为int ==> 配置主键的类型
        list.add(1,"int %sByPrimaryKey(Integer id);",dName);
        return list.getArrayList();
    }
    public static List<String> findByPrimaryKey() {
        MList list = new MList();
        // todo 主键的类型不一定为int ==> 配置主键的类型
        list.add(1,"%s %sByPrimaryKey(Integer id);",entityName,rName);
        return list.getArrayList();
    }
    public static List<String> updateByPrimaryKey() {
        MList list = new MList();
        list.add(1,"int %sByPrimaryKey(%s %s);",uName,entityName,entityName_camel);
        return list.getArrayList();
    }
    public static List<String> find() {
        MList list = new MList();
        // todo 此处入参可以写成Object 兼容Map 与java类??? 待测试
//        list.add(1,"List<%s> find(Object param);",entityName);
        list.add(1,"List<%s> %s(%s %s);",entityName,rName,entityName,entityName_camel);
        return list.getArrayList();
    }

    public static List<String> packages() {
        MList list = new MList();
        list.add("import %s;",entityName_full);
        list.add("import org.apache.ibatis.annotations.Mapper;");
        list.add("import org.springframework.stereotype.Repository;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }

    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart() {
        MList list = new MList();
        list.add("@Repository");
        list.add("@Mapper");
        list.add("public interface %sMapper {",entityName);
        return list.getArrayList();
    }

    public static List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",mapperPackageName));
        list.add("");
        list.addAll(packages());
        list.add("");
        list.addAll(classStart());
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
        String fileName = String.format("src/main/java/com/lxw/mapper/%sMapper.java", entityName);
        generate(content,fileName);

    }


}
