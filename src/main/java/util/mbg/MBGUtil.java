package util.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MBGUtil {
    public static void generate(MBG mbg) throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        Configuration config = new Configuration();
        Context context = new Context(ModelType.CONDITIONAL);
        context.setTargetRuntime(mbg.getTargetRuntime());
        context.setId("defaultContext");

        /**
         * 自动识别数据库关键字，默认false，
         *如果设置为true，根据SqlReservedWords中定义的关键字列表；
         * 一般保留默认值，遇到数据库关键字（Java关键字），
         *使用columnOverride覆盖
         */
        context.addProperty("autoDelimitKeywords", "false");
        //生成的Java文件的编码
        context.addProperty("javaFileEncoding", "utf-8");

        /**
         * 指明数据库标记数据库对象名的符号，
         * 比如ORACLE就是双引号，MYSQL默认是`反引号；
         */
//            context.addProperty("beginningDelimiter", "`");
//            context.addProperty("endingDelimiter", "`");
        //格式化java代码
        context.addProperty("javaFormatter", "org.mybatis.generator.api.dom.DefaultJavaFormatter");
        //格式化xml代码
        context.addProperty("xmlFormatter", "org.mybatis.generator.api.dom.DefaultXmlFormatter");

        // 序列化 格式化 还能干吗???待开发
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        if (mbg.getSerializableSwitch()) {
            pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
        }
        if (mbg.getToStringSwitch()) {
            pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.ToStringPlugin");
        }
        context.addPluginConfiguration(pluginConfiguration);


        //设置是否生成注释
        if (mbg.getCommentSwitch()) {
            CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
            // 取消注释
            commentGeneratorConfiguration.addProperty("suppressAllComments", "true");
            // 取消注释上的日期信息(只有在生成日期的情况下才起作用)
            commentGeneratorConfiguration.addProperty("suppressDate", "true");
            context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
        }

        //设置连接数据库
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass(mbg.getDriverClass());
        jdbcConnectionConfiguration.setConnectionURL(mbg.getConnectionURL());
        jdbcConnectionConfiguration.setUserId(mbg.getUsername());
        jdbcConnectionConfiguration.setPassword(mbg.getPassword());
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);


        //是否使用bigDecimal， false可自动转化以下类型（Long, Integer, Short, etc.）
        /**
         * false：默认,
         *      scale>0;length>18：使用BigDecimal;
         *      scale=0;length[10,18]：使用Long；
         *      scale=0;length[5,9]：使用Integer；
         *      scale=0;length<5：使用Short；
         * true:把JDBC DECIMAL 和NUMERIC 类型解析为java.math.BigDecimal
         */
        if (mbg.getBigDecimalSwitch()) {
            JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
            javaTypeResolverConfiguration.addProperty("forceBigDecimals", "true");
            context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
        }

        // 配置实体类
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        String entityPath = mbg.getEntityPath();
        javaModelGeneratorConfiguration.setTargetPackage(mbg.getEntityPackage());
        if (entityPath != null && entityPath.trim().length() > 0) {
            javaModelGeneratorConfiguration.setTargetProject(entityPath);
        } else {
            javaModelGeneratorConfiguration.setTargetProject("src/main/java");
        }
        // 继承父类并导入父类的包
        String rootClass = mbg.getRootClass();
        if (rootClass != null && rootClass.trim().length() > 0) {
            javaModelGeneratorConfiguration.addProperty("rootClass", rootClass);
        }
        javaModelGeneratorConfiguration.addProperty("enableSubPackages", "false");
        /**
         * setter 赋值时是否进行清理前后空格和null判断,例如
         *    public void setModel(String model) {
         *      this.model = model == null ? null : model.trim();
         *    }
         */
        if (mbg.getTrimStringSwitch()) {
            javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        }
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        // 配置xml
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        String xmlPath = mbg.getXmlPath();
        sqlMapGeneratorConfiguration.setTargetPackage(mbg.getXmlPackage());
        if (xmlPath != null && xmlPath.trim().length() > 0) {
            sqlMapGeneratorConfiguration.setTargetProject(xmlPath);
        } else {
            sqlMapGeneratorConfiguration.setTargetProject("src/main/resources");
        }
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages", "false");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        // 配置mapper接口
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(mbg.getMapperPackage());
        String mapperPath = mbg.getMapperPath();
        if (mapperPath != null && mapperPath.trim().length() > 0) {
            javaClientGeneratorConfiguration.setTargetProject(mapperPath);
        } else {
            javaClientGeneratorConfiguration.setTargetProject("src/main/java");
        }
        String mapperType = mbg.getMapperType();
        if (mapperType != null && mapperType.trim().length() > 0) {
            javaClientGeneratorConfiguration.setConfigurationType(mapperType);
        } else {
            javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        }
        String rootInterface = mbg.getRootInterface();
        if (rootInterface != null && rootInterface.trim().length() > 0) {
            javaClientGeneratorConfiguration.addProperty("rootInterface", rootInterface);
        }
        javaClientGeneratorConfiguration.addProperty("enableSubPackages", "false");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        List<MBG.TableConfig> tableNames = mbg.getTableConfigs();
        for (MBG.TableConfig tableConfig : tableNames) {
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(tableConfig.getTableName());
            tableConfiguration.setCountByExampleStatementEnabled(true);
            tableConfiguration.setUpdateByExampleStatementEnabled(true);
            tableConfiguration.setDeleteByExampleStatementEnabled(true);
            tableConfiguration.setInsertStatementEnabled(true);
            tableConfiguration.setDeleteByPrimaryKeyStatementEnabled(true);
            GeneratedKey generatedKey = new GeneratedKey("id", "MySql", true, null);
            tableConfiguration.setGeneratedKey(generatedKey);

            context.addTableConfiguration(tableConfiguration);
        }
        config.addContext(context);

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
    public static void generateWithXml() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File(".\\src\\main\\resources\\mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
