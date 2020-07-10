package util.generator;

import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;
import util.generator.entity.MgConfig;

import java.sql.Connection;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-19 10:54
 */
public class MGenerator extends BasicGenerator {
    public MGenerator(MgConfig mGConfig){
        this.mGConfig =mGConfig;
    }




    public void generator(){

        String basicPath = this.mGConfig.getGroup().replaceAll("\\.","/");
        /**
         * xml文件全名
         */
        String xmlFullName = String.format("src/main/resources/%s/mapper/%sMapper.xml",basicPath, mGConfig.getEntityName());
        /**
         * entity文件全名
         */
        String entityFullName = String.format("src/main/java/%s/entity/%s.java",basicPath, mGConfig.getEntityName());
        /**
         * mapper文件生成
         */
        String mapperFullName = String.format("src/main/java/%s/mapper/%sMapper.java", basicPath,mGConfig.getEntityName());
        /**
         * serviceImpl文件生成
         */
        String serviceImplFullName = String.format("src/main/java/%s/service/impl/%sServiceImpl.java", basicPath,mGConfig.getEntityName());
        /**
         * serviceFullName文件生成
         */
        String serviceFullName = String.format("src/main/java/%s/service/%sService.java", basicPath,mGConfig.getEntityName());
        /**
         * controllerFullName文件生成
         */
        String controllerFullName = String.format("src/main/java/%s/controller/%sController.java", basicPath,mGConfig.getEntityName());
        /**
         * vue文件生成
         */
        String vueFullName = String.format("src/main/resources/vue/pages/%s.vue", mGConfig.getEntityName());

        Connection connection = MDBUtil.getConnection(mGConfig.getUrl(), mGConfig.getDbUserName(), mGConfig.getDbPassword());
        List<MColumn> columns = getColumns(connection,mGConfig.getDbType(),mGConfig.getTableName());
        MDBUtil.closeConnection(connection);
        // 生成实体类
        List<String> entity = new EntityGenerator(mGConfig).content(columns);
        generate(entity,entityFullName,true);
        // 生成Xml
        List<String> content = new XmlGenerator(mGConfig).content(columns);
        generate(content,xmlFullName,true);
        // 生成mapper
        List<String> mapperContent = new MapperGenerator(mGConfig).content();
        generate(mapperContent,mapperFullName,false);
        // 生成serviceImpl
        List<String> serviceImplContent = new ServiceImplGenerator(mGConfig).content();
        generate(serviceImplContent,serviceImplFullName,false);
        // 生成service
        List<String> serviceContent = new ServiceGenerator(mGConfig).content();
        generate(serviceContent,serviceFullName,false);
        // 生成controller
        List<String> controllerContent = new ControllerGenerator(mGConfig).content();
        generate(controllerContent,controllerFullName,false);
        // 生成vue todo
//        List<String> vueContent = new VueGenerator().content(columns);
//        generate(vueContent,vueFullName);
        // 解析vue文件到index.html里面 todo
//         MVueUtil.indexGenerator();
    }
//    public void initBasicGenerator(){
//        MGConfig mGConfig = new MGConfig("dept");
//
//        String corePathName = mGConfig.getGroup()+".".replace(".","/");
//        // 实体类名
//        if(MGConfig.entityName !=null && entityName.trim().length()>0){
//            entityName = MGConfig.entityName;
//        }else{
//
//            entityName = MStringUtil.toUpperCaseFirstOne(MStringUtil._x2X(mGConfig.tableName));
//        }
//        // 包名
//        entityPackage = mGConfig.group +"entity";
//        controllerPackageName = mGConfig.group +"controller";
//        servicePackageName = mGConfig.group + "service";
//        mapperPackageName = mGConfig.group + "mapper";
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
        MgConfig mGConfig = new MgConfig("mg_global_config");
        // 全局配置
        mGConfig.setUrl("jdbc:mysql://123.57.128.136/mgenerator?useUnicode=true&characterEncoding=UTF-8&useSSL=true");
        mGConfig.setDbUserName("root");
        mGConfig.setDbPassword("i&Z*M$lxw@9yb2#E33%Tsz");
        mGConfig.setGroup("com.lxw.mutil");
        mGConfig.setAuthor("荔谢文");
        mGConfig.setBasicEntity("BasicEntity");
        String controllerPackageName = mGConfig.getControllerPackage();
        new MGenerator(mGConfig).generator();
    }
}
