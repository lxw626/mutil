package util.generator;

/**
 * @author lixiewen
 * @create 2020-01-15 15:27
 */

import util.dbUtil.MColumn;
import util.dbUtil.MDBConfig;
import util.dbUtil.MDBUtil;
import util.ioUtil.MIOUtil;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixiewen
 * @create 2020-01-02 17:52
 */
public class BasicGenerator {
    /**
     * 表名
     */
    public static String tableName = "emp";
    /**
     * 作者
     */
    public static String author = "荔谢文";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 创建时间
     */
    public static String createDate = simpleDateFormat.format(new Date());
    /**
     * 实体类包名
     */
    public static String entityPackage = "com.lxw.entity";
    /**
     * controller包名
     */
    public static String controllerPackageName = "com.lxw.controller";
    /**
     * service包名
     */
    public static String servicePackageName = "com.lxw.service";
    /**
     * mapper接口包名
     */
    public static String mapperPackageName = "com.lxw.mapper";
    /**
     * 实体类名
     */
    public static String entityName = "Emp";
    /**
     * entity类全名
     */
    public static String entityName_full = "com.lxw.entity.Emp";

    public static String entityName_camel = "emp";
    //    static String basicEntity = "";
    public static String basicEntity = "BasicEntity";
    /**
     * service全名
     */
    public static String serviceName_full = "com.lxw.service.EmpService";
    /**
     * Mapper接口全名
     */
    public static String mapperName_full = "com.lxw.mapper.EmpMapper";
    /**
     * 表中默认排序字段
     */
    public static String defaultSort = "empno asc";
    /**
     * (BasicEntity中的)排序字段
     */
    public static String sortInfo = "sortInfo";
    /**
     * 生成xml时是否加入排序子句
     */
    public static boolean isSort = false;
    /**
     * 生成entity时是否采用lombok
     */
    public static boolean isLombok = false;
    /**
     * 是否生成实体类的注释
     */
    public static boolean isFieldComment = true;
    /**
     * 是否生成toString方法
     */
    public static boolean isToString = true;

    /**
     * MResponse全名
     */
    public static String mResponseName_full = "com.lxw.entity.MResponse";

    /**
     * 实体类中有可能要导入的包
     * todo 后期可以维护到数据库
     */
    public static Map<String, String> requisitePackages = new HashMap<>();
    /**
     * 附加的一些注释,比如作者,创建时间
     */
    public static List<String> extraMethodNote = new ArrayList();
    /**
     * 新增注释
     */
    public static MList addNote = new MList();
    /**
     * 根据主键删除注释
     */
    public static MList deleteByPrimaryKeyNote = new MList();
    /**
     * 根据主键更新注释
     */
    public static MList updateByPrimaryKeyNote = new MList();
    /**
     * 根据主键查询注释
     */
    public static MList findByPrimaryKeyNote = new MList();
    /**
     * 分页查询注释
     */
    public static MList getXxxsByPageNote = new MList();

    // vue部分
    /**
     * 分页列表
     */
    public static String pageSizes = "[3,5,10,15]";
    /**
     * 每页显示的条数
     */
    public static int pageSize = 3;


    static {
        requisitePackages.put("Date", "java.util.Date");
        requisitePackages.put("BigDecimal", "java.math.BigDecimal");
        MList list = new MList();
        list.add(1, " * @Author: %s", author);
        list.add(1, " * @CreateDate: %s", createDate);
        extraMethodNote.addAll(list.getArrayList());
        // 新增部分注释
        addNote.add(1, "/**");
        addNote.add(1, " * 新增");
        addNote.add(1, " * @param " + entityName_camel);
        addNote.add(1, " * @return");
        addNote.add(1, " */");
        // 删除部分注释
        deleteByPrimaryKeyNote.add(1, "/**");
        deleteByPrimaryKeyNote.add(1, " * 根据主键删除");
        deleteByPrimaryKeyNote.add(1, " * @param id");
        deleteByPrimaryKeyNote.add(1, " * @return");
        deleteByPrimaryKeyNote.add(1, " */");
        updateByPrimaryKeyNote.add(1, "/**");
        // 修改部分注释
        updateByPrimaryKeyNote.add(1, " * 根据主键修改");
        updateByPrimaryKeyNote.add(1, " * @param %s", entityName_camel);
        updateByPrimaryKeyNote.add(1, " * @return");
        updateByPrimaryKeyNote.add(1, " */");
        // 根据主键查询注释
        findByPrimaryKeyNote.add(1, "/**");
        findByPrimaryKeyNote.add(1, " * 根据主键查找");
        findByPrimaryKeyNote.add(1, " * @param id 主键");
        findByPrimaryKeyNote.add(1, " * @return");
        findByPrimaryKeyNote.add(1, " */");
        // 分页查询注释
        getXxxsByPageNote.add(1, "/**");
        getXxxsByPageNote.add(1, " * 分页查询");
        getXxxsByPageNote.add(1, " * @param param");
        getXxxsByPageNote.add(1, " * @return");
        getXxxsByPageNote.add(1, " */");
    }



    /**
     * 生成的小数类型
     * 默认Double,可选BigDecimal
     */
    public static String decimal = "Double";
    /**
     * add insert save 可选
     */
    public static String cName = "add";
    /**
     * get find select 可选
     */
    public static String rName = "find";
    public static String uName = "update";
    public static String dName = "delete";

    public static void generate(List<String> list, String fillFullName) {
        File file = new File(fillFullName);
        PrintWriter pw = MIOUtil.getPrintWriter(file);
        for (String text : list) {
            pw.write(text + "\n");
        }
        pw.flush();
        pw.close();
    }

    /**
     * 数据源信息
     */
    public static String url = "jdbc:mysql://localhost:3306/scott?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
    public static String userName = "root";
    public static String password = "i&Z*M$lxw@9yb2#E33%Tsz";
    public static String dbType = "mysql";

    public static List<MColumn> getColumns() {
        Connection conn = MDBUtil.getConnection(url, userName, password);
        MDBConfig mdbConfig = new MDBConfig();
        mdbConfig.setConn(conn);
        mdbConfig.setDbType(dbType);
        MDBUtil.initMDBUtil(mdbConfig);
        List<MColumn> columns = MDBUtil.getColumnInfos(tableName);
        MDBUtil.closeConnection(conn);
        return columns;
    }


    /**
     * 添加额外的操作:生成内容的最后一道流水线
     *
     * @param list
     * @return
     */
    public static List<String> extraOperation(List<String> list) {
        List<String> content = new ArrayList();
        for (String s : list) {
            content.add(s);
            // 如果有额外的注释,则加在* @return后面
            if (extraMethodNote.size() > 0) {
                if (s != null && s.trim().startsWith("* @return")) {
                    content.addAll(extraMethodNote);
                }
            }
        }
        return content;
    }

    /**
     * xml文件全名
     */
    public static String xmlFullName = String.format("src/main/resources/com/lxw/mapper/%sMapper.xml", entityName);
    /**
     * entity文件全名
     */
    public static String entityFullName = String.format("src/main/java/com/lxw/entity/%s.java", entityName);
    /**
     * mapper文件生成
     */
    public static String mapperFullName = String.format("src/main/java/com/lxw/mapper/%sMapper.java", entityName);
    /**
     * serviceImpl文件生成
     */
    public static String serviceImplFullName = String.format("src/main/java/com/lxw/service/impl/%sServiceImpl.java", entityName);
    /**
     * serviceFullName文件生成
     */
    public static String serviceFullName = String.format("src/main/java/com/lxw/service/%sService.java", entityName);
    /**
     * controllerFullName文件生成
     */
    public static String controllerFullName = String.format("src/main/java/com/lxw/controller/%sController.java", entityName);
    /**
     * vue文件生成
     */
    public static String vueFullName = String.format("src/main/resources/vue/pages/%s.vue", entityName);


}
