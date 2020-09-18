package com.lxw.mutil.component.generator;

import com.lxw.mutil.component.AutoMgConfig;
import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;

import java.sql.Connection;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-19 10:54
 */
public class MGenerator extends BasicGenerator {

    public static void generator(MgConfig mgConfig){

        String basicPath = mgConfig.getGroup().replaceAll("\\.","/");
        /**
         * xml文件全名
         */
        String xmlFullName = String.format("src/main/resources/%s/mapper/%sMapper.xml",basicPath, mgConfig.getEntityName());
        /**
         * entity文件全名
         */
        String entityFullName = String.format("src/main/java/%s/entity/%s.java",basicPath, mgConfig.getEntityName());
        /**
         * dao文件生成
         */
        String mapperFullName = String.format("src/main/java/%s/dao/%sMapper.java", basicPath,mgConfig.getEntityName());
        /**
         * serviceImpl文件生成
         */
        String serviceImplFullName = String.format("src/main/java/%s/service/impl/%sServiceImpl.java", basicPath,mgConfig.getEntityName());
        /**
         * serviceFullName文件生成
         */
        String serviceFullName = String.format("src/main/java/%s/service/%sService.java", basicPath,mgConfig.getEntityName());
        /**
         * controllerFullName文件生成
         */
        String controllerFullName = String.format("src/main/java/%s/controller/%sController.java", basicPath,mgConfig.getEntityName());
        /**
         * vue文件生成
         */
        String vueFullName = String.format("src/main/resources/vue/pages/%s.vue", mgConfig.getEntityName());

        Connection connection = MDBUtil.getConnection(mgConfig.getDbUrl(), mgConfig.getDbUserName(), mgConfig.getDbPassword());
        List<MColumn> columns = getColumns(connection,mgConfig.getDbType(),mgConfig.getTableName());
        MDBUtil.closeConnection(connection);
        mgConfig.setColumns(columns);
        // 生成实体类
        List<String> entity = EntityGenerator.content(mgConfig);
        generate(entity,entityFullName,true);
        // 生成Xml
        List<String> content = XmlGenerator.content(mgConfig,columns);
        generate(content,xmlFullName,true);
        // 生成mapper
        List<String> mapperContent = MapperGenerator.content(mgConfig);
        generate(mapperContent,mapperFullName,false);
        // 生成serviceImpl
        List<String> serviceImplContent = ServiceImplGenerator.content(mgConfig);
        generate(serviceImplContent,serviceImplFullName,false);
        // 生成service
        List<String> serviceContent = ServiceGenerator.content(mgConfig);
        generate(serviceContent,serviceFullName,false);
        // 生成controller
//        List<String> controllerContent = new ControllerGenerator(mgConfig).content();
//        generate(controllerContent,controllerFullName,false);
        // 生成vue todo
//        List<String> vueContent = new VueGenerator().content(columns);
//        generate(vueContent,vueFullName);
        // 解析vue文件到index.html里面 todo
//         MVueUtil.indexGenerator();
    }
//    public void initBasicGenerator(){
//        MGConfig mgConfig = new MGConfig("dept");
//
//        String corePathName = mgConfig.getGroup()+".".replace(".","/");
//        // 实体类名
//        if(MGConfig.entityName !=null && entityName.trim().length()>0){
//            entityName = MGConfig.entityName;
//        }else{
//
//            entityName = MStringUtil.toUpperCaseFirstOne(MStringUtil._x2X(mgConfig.tableName));
//        }
//        // 包名
//        entityPackage = mgConfig.group +"entity";
//        controllerPackageName = mgConfig.group +"controller";
//        servicePackageName = mgConfig.group + "service";
//        mapperPackageName = mgConfig.group + "mapper";
//        // 全类名
//        entityFullName = entityPackage+"."+entityName;
//        controllerFullName = controllerPackageName + "." + entityName + "Controller";
//        serviceFullName = servicePackageName + "."  + entityName +  "Service";
//        // 文件生成路径
//        entityFullName = String.format("src/main/java/%sentity/%s.java",corePathName,entityName);
//        xmlFullName = String.format("src/main/resources/%smapper/%sMapper"+".xml",corePathName,entityName);
//        mapperFullName = String.format("src/main/java/%smapper/%sMapper.java",corePathName,entityName);
//        serviceImplFullName = String.format("src/main/java/%sservice/impl/%sServiceImpl.java",corePathName, entityName);
//        serviceFullName = String.format("src/main/java/%sservice/%sService.java",corePathName,entityName);
//        controllerFullName = String.format("src/main/java/%scontroller/%sController.java",corePathName,entityName);
//        vueFullName = String.format("src/main/resources/vue/pages/%s.vue", entityName);
//
//
//
//
//
//    }


    public static void main(String[] args) {
        MgConfig mgConfig = new MgConfig();
        // 全局配置
        mgConfig.setDbUrl("jdbc:mysql://123.57.128.136/mgenerator?useUnicode=true&characterEncoding=utf8&ssl=true&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull");
        mgConfig.setDbUserName("root");
        mgConfig.setDbPassword("i&Z*M$lxw@9yb2#E33%Tsz");
        mgConfig.setGroup("com.lxw.mutil");
        mgConfig.setAuthor("荔谢文");
        mgConfig.setBasicEntity("BasicEntity");
        mgConfig.setTableName("emp");
        mgConfig = AutoMgConfig.config(mgConfig);
        generator(mgConfig);
    }
}
