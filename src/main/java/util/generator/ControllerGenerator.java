package util.generator;

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
    public static List<String> insert() {
        MList list = new MList();
        // 添加方法注释
        list.addAll(addNote);
        // 请求名
        list.add(1,"@PostMapping(\"/%s\")", entityName_camel);
        // 方法的开始
        list.add(1,"public MResponse %s(@RequestBody %s %s) {", cName, entityName, entityName_camel);
        list.add(2,"%sService.%s(%s);", entityName_camel,cName, entityName_camel);
        list.add(2,"return new MResponse().success(\"新增成功\");");
        list.add(1,"}");
        return list.getArrayList();
    }

    /**
     * * 删除
     * @return
     */
    public static List<String> deleteByPrimaryKey() {
        MList list = new MList();
        list.addAll(deleteByPrimaryKeyNote);
        list.add(1,"@DeleteMapping(\"/emp/{id}\")");
        list.add(1,"public MResponse %sByPrimaryKey(@PathVariable(\"id\") Integer id) {", dName);
        list.add(2,"%sService.%sByPrimaryKey(id);", entityName_camel, dName);
        list.add(2,"return new MResponse().success(\"删除成功\");");
        list.add(1,"}");
        return list.getArrayList();
    }


    /**
     * 根据主键修改
     * @return
     */
    public static List<String> updateByPrimaryKey() {
        MList list = new MList();
        list.addAll(updateByPrimaryKeyNote);
        list.add(1,"@PutMapping(\"/%s\")", entityName_camel);
        list.add(1,"public MResponse %sByPrimaryKey(@RequestBody %s %s) {", uName, entityName, entityName_camel);
        list.add(2,"%sService.%sByPrimaryKey(%s);", entityName_camel, uName, entityName_camel);
        list.add(2,"return new MResponse().success(\"修改成功\");");
        list.add(1,"}");
        return list.getArrayList();
    }

    public static List<String> findByPrimaryKey() {
        MList list = new MList();
        list.addAll(findByPrimaryKeyNote);
        list.add(1,"@GetMapping(\"/emp/{id}\")");
        list.add(1,"public MResponse %sByPrimaryKey(@PathVariable(\"id\") Integer id) {", rName);
        list.add(2,"%s %s = %sService.%sByPrimaryKey(id);",entityName,entityName_camel,entityName_camel,rName);
        list.add(2,"return new MResponse().success(%s);",entityName_camel);
        list.add(1,"}");
        return list.getArrayList();
    }

    public static List<String> getXxxsByPage() {
        MList list = new MList();
        list.addAll(getXxxsByPageNote);
        list.add(1,"@PostMapping(\"/get%ssByPage\")", entityName);
        list.add(1,"public PageInfo<%s> %s(@RequestBody %s param) {", entityName, rName, entityName);
        list.add(2,"Integer currentPage = param.getCurrentPage();");
        list.add(2,"Integer pageSize = param.getPageSize();");
        list.add(2,"PageHelper.startPage(currentPage, pageSize);");
        list.add(2,"List<%s> %ss = %sService.%s(param);", entityName, entityName_camel, entityName_camel, rName);
        list.add(2,"return new PageInfo<>(%ss);", entityName_camel);
        list.add(1,"}");
        return list.getArrayList();
    }
    public static List<String> packages() {
        MList list = new MList();
        list.add("import com.github.pagehelper.PageHelper;");
        list.add("import com.github.pagehelper.PageInfo;");
        list.add("import %s;", entityName_full);
        list.add("import %s;",mResponseName_full);
        list.add("import %s;",serviceName_full);
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
    public static List<String> classStart() {
        MList list = new MList();
        list.add("@RestController");
        list.add("public class %sController {", entityName);
        list.add("");
        list.add(1,"@Autowired");
        list.add(1,"%sService %sService;", entityName, entityName_camel);
        return list.getArrayList();
    }
    public static List<String> content() {
        List<String> list = new ArrayList();
        list.add(String.format("package %s;",controllerPackageName));
        list.add("");
        list.addAll(packages());
        list.add("");
        list.addAll(classStart());
        list.add("");
        list.addAll(insert());
        list.add("");
        list.addAll(deleteByPrimaryKey());
        list.add("");
        list.addAll(updateByPrimaryKey());
        list.add("");
        list.addAll(findByPrimaryKey());
        list.add("");
        list.addAll(getXxxsByPage());
        list.add("");
        list.add("}");
        List<String> content = extraOperation(list);

        return content;
    }




    public static void main(String[] args) throws IOException {
        List<String> content = content();
        String fileName = String.format("src/main/java/com/lxw/controller/%sController.java", entityName);
        generate(content,fileName);
        LOGGER.info(entityName+"Controller生成完毕");
    }

}

