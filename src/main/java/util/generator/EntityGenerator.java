package util.generator;

import com.lxw.mutil.config.DefaultConfig;
import util.MStringUtil;
import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;
import util.generator.entity.MgConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lixiewen
 * @create 2020-01-08 10:17
 */
public class EntityGenerator extends BasicGenerator {

    public EntityGenerator(MgConfig mGConfig){
        this.mGConfig = mGConfig;
    }


    public List<String> content(List<MColumn> columns) {
        MList list = new MList();
        Set<String> packages = new HashSet<>();
        List<String> fields = new ArrayList<>();
        List<String> setAndGetMethods = new ArrayList<>();
        for (MColumn column : columns) {
            // 生成需要导入的包
            String fieldType = column.getFieldType();
            Set<String> keys = DefaultConfig.getRequisitePackages().keySet();
            if(keys.contains(fieldType)){
                String requisitePackage = DefaultConfig.getRequisitePackages().get(fieldType);
                packages.add(String.format("import %s;",requisitePackage));
            }
            // 生成成员变量及注释
            List<String> filed = filedGenerator(column);
            fields.addAll(filed);
            // 生成get方法
            List<String> getMethod = getMethodGenerator(column);
            setAndGetMethods.addAll(getMethod);
            // 生成set方法
            List<String> setMethod = setMethodGenerator(column);
            setAndGetMethods.addAll(setMethod);
        }
        // 生成包名
        list.add(String.format("package %s;",mGConfig.getEntityPackage()));
        list.add("");
        List importPackageList = new ArrayList(packages);
        if(mGConfig.isLombok()){
            importPackageList.add("import lombok.Data;");
            importPackageList.add("import lombok.experimental.Accessors;");
        }
        Collections.sort(importPackageList);
        list.addAll(new ArrayList(importPackageList));
        if (packages.size()>0){
            list.add("");
        }
        List<String> classStart = classStart();
        list.addAll(classStart);
        list.addAll(fields);
        if(!mGConfig.isLombok()){
            list.addAll(setAndGetMethods);
            if(mGConfig.isToString()){
                List<String> toStringMethod = toStringMethodGenerator(columns);
                list.addAll(toStringMethod);
            }
        }
        list.add("}");
        return list.getArrayList();
    }

    /**
     * 字段(注释)模型
     * @param column
     * @return
     */
    private List<String> filedGenerator(MColumn column) {
        MList list = new MList();
        String fieldType = column.getFieldType();
        String fieldName = column.getFieldName();
        String fieldComment = column.getColumnComment();
        if(mGConfig.isFieldComment() && fieldComment !=null && fieldComment.trim().length()>0){
            list.add(1,"/**");
            list.add(1," * %s",fieldComment);
            list.add(1," */");
        }
        list.add(1,"private %s %s;",fieldType,fieldName);
        list.add("");
        return list.getArrayList();
    }

    /**
     * getMethod
     * @param column
     * @return
     */
    private static List<String> getMethodGenerator(MColumn column) {
        String fieldType = column.getFieldType();
        String fieldName = column.getFieldName();
        MList list = new MList();
        String s = MStringUtil.toUpperCaseFirstOne(fieldName);
        list.add(1,"public %s get%s() {",fieldType,s);
        list.add(2,"return %s;",fieldName);
        list.add(1,"}");
        list.add("");
        return list.getArrayList();
    }

    /**
     * setMethod
     * @param column
     * @return
     */
    private static List<String> setMethodGenerator(MColumn column) {
        String fieldType = column.getFieldType();
        String fieldName = column.getFieldName();
        MList list = new MList();
        String s = MStringUtil.toUpperCaseFirstOne(fieldName);
        list.add(1,"public void set%s(%s %s) {",s,fieldType,fieldName);
        list.add(2,"this.%s = %s;",fieldName,fieldName);
        list.add(1,"}");
        list.add("");
        return list.getArrayList();
    }

    /**
     * toString方法
     * @param columns
     * @return
     */
    private List<String> toStringMethodGenerator(List<MColumn> columns) {
        MList list = new MList();
        list.add(1,"@Override");
        list.add(1,"public String toString() {");
        list.add(2,"return \"%s{\" +",mGConfig.getEntityName());
        for (int i = 0; i < columns.size(); i++) {
            String fieldName = columns.get(i).getFieldName();
            String fieldType = columns.get(i).getFieldType();
            if(i == 0){
                if("String".equals(fieldType)){
                    list.add(2,"\" %s='\" + %s + '\\\'' +",fieldName,fieldName);
                }else{
                    list.add(2,"\" %s=\" + %s  +",fieldName,fieldName);
                }
            }else {
                list.add(2,"\", %s='\" + %s + '\\\'' +",fieldName,fieldName);
            }
        }
        list.add(2,"'}';");
        list.add(1,"}");
        return list.getArrayList();
    }

    /**
     * 类开始部分
     * @return
     */
    private List<String> classStart() {
        MList list = new MList();
        if(mGConfig.isLombok()){
            list.add("@Data");
            list.add("@Accessors(chain = true)");
        }
        String classStart = String.format("public class %s {",mGConfig.getEntityName());
        if(mGConfig.getBasicEntity() != null && mGConfig.getBasicEntity().trim().length()>0){
            classStart = String.format("public class %s extends %s {",mGConfig.getEntityName(),mGConfig.getBasicEntity());
        }
        list.add(classStart);
        list.add("");

        return list.getArrayList();
    }

    public static void main(String[] args) throws SQLException {
        MgConfig mGConfig = new MgConfig("dept");
        Connection connection = MDBUtil.getConnection(mGConfig.getUrl(), mGConfig.getDbUserName(), mGConfig.getDbPassword());
        List<MColumn> columns = getColumns(connection,"mysql","dept");
        EntityGenerator entityGenerator = new EntityGenerator(mGConfig);
        List<String> contents = entityGenerator.content(columns);
        for (String content : contents) {
            System.out.println(content);
        }
        String fileName = String.format("src/main/java/com/lxw/entity/%s.java", mGConfig.getEntityName());
        generate(contents,fileName,true);
        MDBUtil.closeConnection(connection);
    }
}
