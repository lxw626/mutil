package com.lxw.mutil.component.generator;

import com.lxw.mutil.component.AutoMgConfig;
import com.lxw.mutil.config.DefaultConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2019-12-30 14:36
 */
public class ControllerGenerator extends BasicGenerator {
    private final static Logger LOGGER = Logger.getLogger(ControllerGenerator.class);


    /**
     * 新增
     * @return
     */
    public static List<String> insert(MgConfig mGConfig) {
        MList list = new MList();
        // 添加方法注释
        list.addAll(DefaultConfig.getDefaultAddNote(mGConfig.getEntityName4Camel()));
        // 请求名
        list.add(1,"@PostMapping(\"/%s\")", mGConfig.getEntityName4Camel());
        // 方法的开始
        list.add(1,"public MResponse %s(@RequestBody %s %s) {", mGConfig.getAddName(), mGConfig.getEntityName(),  mGConfig.getEntityName4Camel());
        list.add(2,"%sService.%s(%s);", mGConfig.getEntityName4Camel(), mGConfig.getAddName(),  mGConfig.getEntityName4Camel());
        list.add(2,"return new MResponse().success(\"新增成功\");");
        list.add(1,"}");
        return list.getArrayList();
    }

    /**
     * * 删除
     * @return
     */
    public static List<String> deleteByPrimaryKey(MgConfig mGConfig) {
        MList list = new MList();
        list.addAll(DefaultConfig.getDefaultDeleteByPrimaryKeyNote());
        list.add(1,"@DeleteMapping(\"/emp/{id}\")");
        list.add(1,"public MResponse %sByPrimaryKey(@PathVariable(\"id\") Integer id) {", mGConfig.getDeleteName());
        list.add(2,"%sService.%sByPrimaryKey(id);", mGConfig.getEntityName4Camel(), mGConfig.getDeleteName());
        list.add(2,"return new MResponse().success(\"删除成功\");");
        list.add(1,"}");
        return list.getArrayList();
    }


    /**
     * 根据主键修改
     * @return
     */
    public static List<String> updateByPrimaryKey(MgConfig mGConfig) {
        MList list = new MList();
        list.addAll(DefaultConfig.getDefaultUpdateByPrimaryKeyNote(mGConfig.getEntityName4Camel()));
        list.add(1,"@PutMapping(\"/%s\")", mGConfig.getEntityName4Camel());
        list.add(1,"public MResponse %sByPrimaryKey(@RequestBody %s %s) {", mGConfig.getUpdateName(), mGConfig.getEntityName(), mGConfig.getEntityName4Camel());
        list.add(2,"%sService.%sByPrimaryKey(%s);", mGConfig.getEntityName4Camel(), mGConfig.getUpdateName(), mGConfig.getEntityName4Camel());
        list.add(2,"return new MResponse().success(\"修改成功\");");
        list.add(1,"}");
        return list.getArrayList();
    }

    public static List<String> findByPrimaryKey(MgConfig mGConfig) {
        MList list = new MList();
        list.addAll(DefaultConfig.getDefaultFindByPrimaryKeyNote());
        list.add(1,"@GetMapping(\"/emp/{id}\")");
        list.add(1,"public MResponse %sByPrimaryKey(@PathVariable(\"id\") Integer id) {", mGConfig.getSelectName());
        list.add(2,"%s %s = %sService.%sByPrimaryKey(id);",mGConfig.getEntityName(),mGConfig.getEntityName4Camel(),mGConfig.getEntityName4Camel(),mGConfig.getSelectName());
        list.add(2,"return new MResponse().success(%s);",mGConfig.getEntityName4Camel());
        list.add(1,"}");
        return list.getArrayList();
    }

    public static List<String> getXxxsByPage(MgConfig mGConfig) {
        MList list = new MList();
        list.addAll(DefaultConfig.getDefaultFindXxxsByPageNote());
        list.add(1,"@PostMapping(\"/get%ssByPage\")", mGConfig.getEntityName());
        list.add(1,"public PageInfo<%s> %s(@RequestBody %s param) {", mGConfig.getEntityName(), mGConfig.getSelectName(), mGConfig.getEntityName());
        list.add(2,"Integer currentPage = param.getCurrentPage();");
        list.add(2,"Integer pageSize = param.getPageSize();");
        list.add(2,"PageHelper.startPage(currentPage, pageSize);");
        list.add(2,"List<%s> %ss = %sService.%s(param);", mGConfig.getEntityName(), mGConfig.getEntityName4Camel(), mGConfig.getEntityName4Camel(), mGConfig.getSelectName());
        list.add(2,"return new PageInfo<>(%ss);", mGConfig.getEntityName4Camel());
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> packages(MgConfig mGConfig) {
        MList list = new MList();
        list.add("import com.github.pagehelper.PageHelper;");
        list.add("import com.github.pagehelper.PageInfo;");
        list.add("import %s.%s;", mGConfig.getEntityPackage(),mGConfig.getEntityName());
        list.add("import %s;",mGConfig.getMResponseNameFull());
        list.add("import %s.%sService;",mGConfig.getServicePackage(),mGConfig.getEntityName());
        list.add("import org.springframework.beans.factory.annotation.Autowired;");
        list.add("import org.springframework.web.bind.annotation.DeleteMapping;");
        list.add("import org.springframework.web.bind.annotation.GetMapping;");
        list.add("import org.springframework.web.bind.annotation.PathVariable;");
        list.add("import org.springframework.web.bind.annotation.PostMapping;");
        list.add("import org.springframework.web.bind.annotation.PutMapping;");
        list.add("import org.springframework.web.bind.annotation.RequestBody;");
        list.add("import org.springframework.web.bind.annotation.RestController;");
        list.add("import java.util.List;");
        return list.getArrayList();
    }

    /**
     * 类的开始部分
     * @return
     */
    public static List<String> classStart(MgConfig mGConfig) {
        MList list = new MList();
        list.add("@RestController");
        list.add("public class %sController {", mGConfig.getEntityName());
        list.add("");
        list.add(1,"@Autowired");
        list.add(1,"%sService %sService;", mGConfig.getEntityName(), mGConfig.getEntityName4Camel());
        return list.getArrayList();
    }
    public static List<String> content(MgConfig mGConfig) {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",mGConfig.getControllerPackage()));
        list.add("");
        list.addAll(packages(mGConfig));
        list.add("");
        list.addAll(classStart(mGConfig));
        list.add("");
        list.addAll(insert(mGConfig));
        list.add("");
        list.addAll(deleteByPrimaryKey(mGConfig));
        list.add("");
        list.addAll(updateByPrimaryKey(mGConfig));
        list.add("");
        list.addAll(findByPrimaryKey(mGConfig));
        list.add("");
        list.addAll(getXxxsByPage(mGConfig));
        list.add("");
        list.add("}");
        List<String> content = extraOperation(list,mGConfig);

        return content;
    }




    public static void main(String[] args) throws IOException {
        MgConfig mgConfig = new MgConfig();
        AutoMgConfig.config(mgConfig);
        List<String> content = content(mgConfig);
        System.out.println(content);
        String fileName = String.format("src/main/java/com/lxw/controller/%sController.java", mgConfig.getEntityName());
        generate(content,fileName,false);
        LOGGER.info(mgConfig.getEntityName()+"Controller生成完毕");
    }

}

