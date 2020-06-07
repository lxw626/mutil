package util.generator;

import util.MStringUtil;
import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;
import util.generator.entity.MGConfig;
import util.generator.entity.MGGlobalConfig;
import util.vue.MVueUtil;

import java.sql.Connection;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-19 10:54
 */
public class MGenerator extends BasicGenerator {

    /**
     * xml文件全名
     */
    public String xmlFullName = String.format("src/main/resources/com/lxw/mapper/%sMapper.xml", mGConfig.getEntityName());
    /**
     * entity文件全名
     */
    public String entityFullName = String.format("src/main/java/com/lxw/entity/%s.java", mGConfig.getEntityName());
    /**
     * mapper文件生成
     */
    public String mapperFullName = String.format("src/main/java/com/lxw/mapper/%sMapper.java", mGConfig.getEntityName());
    /**
     * serviceImpl文件生成
     */
    public String serviceImplFullName = String.format("src/main/java/com/lxw/service/impl/%sServiceImpl.java", mGConfig.getEntityName());
    /**
     * serviceFullName文件生成
     */
    public String serviceFullName = String.format("src/main/java/com/lxw/service/%sService.java", mGConfig.getEntityName());
    /**
     * controllerFullName文件生成
     */
    public String controllerFullName = String.format("src/main/java/com/lxw/controller/%sController.java", mGConfig.getEntityName());
    /**
     * vue文件生成
     */
    public String vueFullName = String.format("src/main/resources/vue/pages/%s.vue", mGConfig.getEntityName());


    public void generator(){
        MGConfig mgConfig = new MGConfig("dept");
        MGGlobalConfig mgGlobalConfig = new MGGlobalConfig();
        mgConfig.setTableName("dept");
        Connection connection = MDBUtil.getConnection(mgGlobalConfig.getUrl(), mgGlobalConfig.getUserName(), mgGlobalConfig.getPassword());
        List<MColumn> columns = getColumns(connection,mgGlobalConfig.getDbType(),mgConfig.getTableName());
        // 生成实体类

        List<String> entity = new EntityGenerator(mgConfig).content(columns);
        generate(entity,entityFullName);
        // 生成Xml
        List<String> content = new XmlGenerator().content(columns);
        generate(content,xmlFullName);
        // 生成mapper
        List<String> mapperContent = new MapperGenerator().content();
        generate(mapperContent,mapperFullName);
        // 生成serviceImpl
        List<String> serviceImplContent = new ServiceImplGenerator().content();
        generate(serviceImplContent,serviceImplFullName);
        // 生成service
        List<String> serviceContent = new ServiceGenerator(mgConfig).content();
        generate(serviceContent,serviceFullName);
        // 生成controller
        List<String> controllerContent = new ControllerGenerator(mgConfig).content();
        generate(controllerContent,controllerFullName);
        // 生成vue
        List<String> vueContent = new VueGenerator().content(columns);
        generate(vueContent,vueFullName);
        // 解析vue文件到index.html里面
         MVueUtil.indexGenerator();
    }
    public void initBasicGenerator(){
        /*MGConfig mgConfig = new MGConfig();
        mgConfig.setTableName("dept");
        mgConfig.setgroup = "com.lxw"+".";

        String corePathName = mgConfig.group.replace(".","/");
        tableName = mgConfig.tableName;
        // 实体类名
        if(MGConfig.entityName !=null && entityName.trim().length()>0){
            entityName = MGConfig.entityName;
        }else{

            entityName = MStringUtil.toUpperCaseFirstOne(MStringUtil._x2X(mgConfig.tableName));
        }
        // 包名
        entityPackage = mgConfig.group +"entity";
        controllerPackageName = mgConfig.group +"controller";
        servicePackageName = mgConfig.group + "service";
        mapperPackageName = mgConfig.group + "mapper";
        // 全类名
        entityFullName = entityPackage+"."+entityName;
        controllerFullName = controllerPackageName + "." + entityName + "Controller";
        serviceFullName = servicePackageName + "."  + entityName +  "Service";
        // 文件生成路径
        entityFullName = String.format("src/main/java/%sentity/%s.java",corePathName,entityName);
        xmlFullName = String.format("src/main/resources/%smapper/%sMapper"+".xml",corePathName,entityName);
        mapperFullName = String.format("src/main/java/%smapper/%sMapper.java",corePathName,entityName);
        serviceImplFullName = String.format("src/main/java/%sservice/impl/%sServiceImpl.java",corePathName, entityName);
        serviceFullName = String.format("src/main/java/%sservice/%sService.java",corePathName,entityName);
        controllerFullName = String.format("src/main/java/%scontroller/%sController.java",corePathName,entityName);
        vueFullName = String.format("src/main/resources/vue/pages/%s.vue", entityName);*/





    }


    public static void main(String[] args) {
//        generator();
    }
}
