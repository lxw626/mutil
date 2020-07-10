package util.generator;

import com.lxw.mutil.config.DefaultConfig;
import util.generator.entity.MgConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-03 16:23
 */
public class MapperGenerator extends BasicGenerator {
    public MapperGenerator(MgConfig mGConfig){
        this.mGConfig = mGConfig;
    }

    public List<String> add() {
        MList list = new MList();
        if(mGConfig.getAddNote() != null){
            list.addAll(mGConfig.getAddNote());
        }else{
            list.addAll(DefaultConfig.getDefaultAddNote(mGConfig.getEntityName4Camel()));
        }
        list.add(1,"int %s(%s %s);",mGConfig.getAddName(),mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public  List<String> deleteByPrimaryKey() {
        MList list = new MList();
        if(mGConfig.getDeleteByPrimaryKeyNote() != null){
            list.addAll(mGConfig.getDeleteByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultDeleteByPrimaryKeyNote());
        }
        // todo 主键的类型不一定为int ==> 配置主键的类型
        list.add(1,"int %sByPrimaryKey(Integer id);",mGConfig.getDeleteName());
        return list.getArrayList();
    }
    public List<String> updateByPrimaryKey() {
        MList list = new MList();
        if(mGConfig.getUpdateByPrimaryKeyNote() != null){
            list.addAll(mGConfig.getUpdateByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultUpdateByPrimaryKeyNote(mGConfig.getEntityName4Camel()));
        }
        list.add(1,"int %sByPrimaryKey(%s %s);",mGConfig.getUpdateName(),mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public List<String> findByPrimaryKey() {
        MList list = new MList();
        if(mGConfig.getFindByPrimaryKeyNote() != null){
            list.addAll(mGConfig.getFindByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindByPrimaryKeyNote());
        }
        // todo 主键的类型不一定为int ==> 配置主键的类型
        list.add(1,"%s %sByPrimaryKey(Integer id);",mGConfig.getEntityName(),mGConfig.getSelectName());
        return list.getArrayList();
    }
    public List<String> find() {
        MList list = new MList();
        if(mGConfig.getFindNote() != null){
            list.addAll(mGConfig.getFindNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindNote());
        }
        // todo 此处入参可以写成Object 兼容Map 与java类??? 待测试
        list.add(1,"List<%s> find(Object param);",mGConfig.getEntityName());
//        list.add(1,"List<%s> %s(%s %s);",mGConfig.getEntityName(),rName,mGConfig.getEntityName(),mGConfig.getEntityName_camel());
        return list.getArrayList();
    }

    public List<String> packages() {
        MList list = new MList();
        list.add("import %s.%s;",mGConfig.getEntityPackage(),mGConfig.getEntityName());
        list.add("import org.apache.ibatis.annotations.Mapper;");
        list.add("import org.springframework.stereotype.Repository;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }

    /**
     * 类的开始部分
     * @return
     */
    public List<String> classStart() {
        MList list = new MList();
        list.add("@Repository");
        list.add("@Mapper");
        list.add("public interface %sMapper {",mGConfig.getEntityName());
        return list.getArrayList();
    }

    public List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",mGConfig.getMapperPackage()));
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
        MgConfig mgConfig = new MgConfig("dept");
        // todo 没有无惨构造为什么不报错,调用父类的???
//        MGConfig mgConfig = new MGConfig();
        List<String> content = new MapperGenerator(mgConfig).content();
        String fileName = String.format("src/main/java/com/lxw/mapper/%sMapper.java", mgConfig.getEntityName());
        generate(content,fileName,false);

    }


}
