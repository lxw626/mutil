package util.generator;

import com.lxw.mutil.config.DefaultConfig;
import util.generator.entity.MgConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:45
 */
public class ServiceGenerator extends BasicGenerator {

    public ServiceGenerator(MgConfig mGConfig){
        this.mGConfig = mGConfig;
    }

    public List<String> add() {
        MList list = new MList();
        if(mGConfig.getAddNote() != null){
            list.addAll(mGConfig.getAddNote());
        }else{
            list.addAll(DefaultConfig.getDefaultAddNote(mGConfig.getEntityName4Camel()));
        }
        list.add(1,"int add(%s %s);",mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public List<String> deleteByPrimaryKey() {
        MList list = new MList();
        if(mGConfig.getDeleteByPrimaryKeyNote() != null){
            list.addAll(mGConfig.getDeleteByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultDeleteByPrimaryKeyNote());
        }
        list.add(1,"int deleteByPrimaryKey(Integer id);");
        return list.getArrayList();
    }

    public List<String> updateByPrimaryKey() {
        MList list = new MList();
        if(mGConfig.getUpdateByPrimaryKeyNote() != null){
            list.addAll(mGConfig.getUpdateByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultUpdateByPrimaryKeyNote(mGConfig.getEntityName4Camel()));
        }
        list.add(1,"int updateByPrimaryKey(%s %s);",mGConfig.getEntityName(),mGConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public List<String> findByPrimaryKey() {
        MList list = new MList();
        if(mGConfig.getFindByPrimaryKeyNote() != null){
            list.addAll(mGConfig.getFindByPrimaryKeyNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindByPrimaryKeyNote());
        }
        list.add(1,"%s findByPrimaryKey(Integer id);",mGConfig.getEntityName());
        return list.getArrayList();
    }
    public List<String> find() {
        MList list = new MList();
        if(mGConfig.getFindNote() != null){
            list.addAll(mGConfig.getFindNote());
        }else{
            list.addAll(DefaultConfig.getDefaultFindNote());
        }
        list.add(1,"List<%s> find(Object param);",mGConfig.getEntityName());
        return list.getArrayList();
    }
    public List<String> packages() {
        MList list = new MList();
        list.add("import %s.%s;",mGConfig.getEntityPackage(),mGConfig.getEntityName());
        list.add("import java.util.List;");
        return list.getArrayList();
    }
    /**
     * 类的开始部分
     * @return
     */
    public List<String> classStart() {
        MList list = new MList();
        list.add("public interface %sService {",mGConfig.getEntityName());
        return list.getArrayList();
    }
    public List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",mGConfig.getServicePackage()));
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
        List<String> contents = new ServiceGenerator(mgConfig).content();
        for (String content : contents) {
            System.out.println(content);
        }
        String fileName = String.format("src/main/java/com/lxw/service/%sService.java", mgConfig.getEntityName());
        generate(contents,fileName,false);
    }

}
