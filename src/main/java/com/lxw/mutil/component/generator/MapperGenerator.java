package com.lxw.mutil.component.generator;

import com.lxw.mutil.component.AutoMgConfig;
import com.lxw.mutil.config.DefaultConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-03 16:23
 */
public class MapperGenerator extends BasicGenerator {

    public static List<String> add(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getAddNote() != null){
            list.addAll(mgConfig.getAddNote());
        }else{
            list.addAll(DefaultConfig.getDefaultAddNote(mgConfig.getEntityName4Camel()));
        }
        list.add(1,"int %s(%s %s);",mgConfig.getAddName(),mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public  static List<String> deleteByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getDeleteByPrimaryKeyNote() != null){
            list.addAll(mgConfig.getDeleteByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultDeleteByPrimaryKeyNote());
        }
        // todo 主键的类型不一定为int ==> 配置主键的类型
        list.add(1,"int %s(Integer id);",mgConfig.getDeleteName());
        return list.getArrayList();
    }
    public static List<String> updateByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getUpdateByPrimaryKeyNote() != null){
            list.addAll(mgConfig.getUpdateByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultUpdateByPrimaryKeyNote(mgConfig.getEntityName4Camel()));
        }
        list.add(1,"int %sByPrimaryKey(%s %s);",mgConfig.getUpdateName(),mgConfig.getEntityName(),mgConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public static List<String> findByPrimaryKey(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getFindByPrimaryKeyNote() != null){
            list.addAll(mgConfig.getFindByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindByPrimaryKeyNote());
        }
        // todo 主键的类型不一定为int ==> 配置主键的类型
        list.add(1,"%s %sByPrimaryKey(Integer id);",mgConfig.getEntityName(),mgConfig.getSelectName());
        return list.getArrayList();
    }
    public static List<String> find(MgConfig mgConfig) {
        MList list = new MList();
        if(mgConfig.getFindNote() != null){
            list.addAll(mgConfig.getFindNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindNote());
        }
        // todo 此处入参可以写成Object 兼容Map 与java类??? 待测试
        list.add(1,"List<%s> find(Object param);",mgConfig.getEntityName());
//        list.add(1,"List<%s> %s(%s %s);",mgConfig.getEntityName(),rName,mgConfig.getEntityName(),mgConfig.getEntityName_camel());
        return list.getArrayList();
    }

    public static List<String> packages(MgConfig mgConfig) {
        MList list = new MList();
        list.add("import %s.%s;",mgConfig.getEntityPackage(),mgConfig.getEntityName());
        list.add("import org.apache.ibatis.annotations.Mapper;");
        list.add("import org.springframework.stereotype.Repository;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }

    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart(MgConfig mgConfig) {
        MList list = new MList();
        list.add("@Repository");
        list.add("@Mapper");
        list.add("public interface %sMapper {",mgConfig.getEntityName());
        return list.getArrayList();
    }

    public static List<String> content(MgConfig mgConfig) {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",mgConfig.getMapperPackage()));
        list.add("");
        list.addAll(packages(mgConfig));
        list.add("");
        list.addAll(classStart(mgConfig));
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
        // todo 没有无惨构造为什么不报错,调用父类的???
//        MGConfig mgConfig = new MGConfig();
        List<String> content = MapperGenerator.content(mgConfig);
        String fileName = String.format("src/main/java/com/lxw/mapper/%sMapper.java", mgConfig.getEntityName());
        generate(content,fileName,false);

    }


}
