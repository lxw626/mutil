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
    static String author = "荔谢文";
    static String tableName = "emp";
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static String createDate = simpleDateFormat.format(new Date());

    static String entityName_full = "com.lxw.entity.Emp";
    static String entityPackage = "com.lxw.entity";
    static String entityName = "Emp";
    static String entityName_camel = "emp";
//    static String basicEntity = "";
    static String basicEntity = "BasicEntity";
    static boolean isLombok = false;
    static boolean isFieldComment = true;
    static boolean isToString = true;
    static boolean isServiceImpl = false;

    static String controllerPackageName = "com.lxw.controller";
    static List<String> requirePckage = new ArrayList<>();
    static String mResponseName_full = "com.lxw.entity.MResponse";
    static String serviceName_full = "com.lxw.service.EmpService";

    static Map<String, String> requisitePackages = new HashMap<>();
    static List<String> extraMethodNote = new ArrayList();
    static MList addNote = new MList();
    static MList deleteByPrimaryKeyNote = new MList();
    static MList updateByPrimaryKeyNote = new MList();
    static MList findByPrimaryKeyNote = new MList();
    static MList getXxxsBypageNote = new MList();

    static {
        requisitePackages.put("Date", "java.util.Date");
        requisitePackages.put("BigDecimal", "java.math.BigDecimal");
        MList list = new MList();
        list.add(1," * @Author: %s",author);
        list.add(1," * @CreateDate: %s",createDate);
        extraMethodNote.addAll(list.getArrayList());
        // 新增部分注释
        addNote.add(1,"/**");
        addNote.add(1," * 新增");
        addNote.add(1," * @param " + entityName_camel);
        addNote.add(1," * @return");
        addNote.add(1," */");
        // 删除部分注释
        deleteByPrimaryKeyNote.add(1,"/**");
        deleteByPrimaryKeyNote.add(1," * 根据主键删除");
        deleteByPrimaryKeyNote.add(1," * @param id");
        deleteByPrimaryKeyNote.add(1," * @return");
        deleteByPrimaryKeyNote.add(1," */");
        updateByPrimaryKeyNote.add(1,"/**");
        // 修改部分注释
        updateByPrimaryKeyNote.add(1," * 根据主键修改");
        updateByPrimaryKeyNote.add(1," * @param %s", entityName_camel);
        updateByPrimaryKeyNote.add(1," * @return");
        updateByPrimaryKeyNote.add(1," */");
        // 根据主键查询注释
        findByPrimaryKeyNote.add(1 ,"/**");
        findByPrimaryKeyNote.add(1," * 根据主键查找");
        findByPrimaryKeyNote.add(1," * @param id 主键");
        findByPrimaryKeyNote.add(1," * @return");
        findByPrimaryKeyNote.add(1," */");
        // 分页查询注释
        getXxxsBypageNote.add(1,"/**");
        getXxxsBypageNote.add(1," * 分页查询");
        getXxxsBypageNote.add(1," * @param param");
        getXxxsBypageNote.add(1," * @return");
        getXxxsBypageNote.add(1," */");
    }

    static String servicePackageName = "com.lxw.service";
    static String mapperPackageName = "com.lxw.mapper";
    static String mapperName = "EmpMapper";
    static String mapperName_full = "com.lxw.mapper.EmpMapper";
    /**
     * 生成的小数类型
     * 默认Double,可选BigDecimal
     */
    public static String decimal = "Double";
    static String cName = "add";
    static String rName = "find";
    static String uName = "update";
    static String dName = "delete";

    public static void generate(List<String> list, String fillFullName) {
        File file = new File(fillFullName);
        PrintWriter pw = MIOUtil.getPrintWriter(file);
        for (String text : list) {
            pw.write(text + "\n");
        }
        pw.flush();
        pw.close();
    }

    public static List<MColumn> getColumns() {

        //本地mysql数据库
        String url = "jdbc:mysql://localhost:3306/scott?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
        String userName = "root";
        String password = "i&Z*M$lxw@9yb2#E33%Tsz";
        Connection conn = MDBUtil.getConnection(url, userName, password);
        MDBConfig mdbConfig = new MDBConfig();
        mdbConfig.setConn(conn);
        mdbConfig.setDbType("mysql");
        MDBUtil.initMDBUtil(mdbConfig);
        List<MColumn> columns = MDBUtil.getColumnInfos(tableName);
        MDBUtil.closeConnection(conn);
        return columns;
    }

    /**
     * 添加额外的操作:生成内容的最后一道流水线
     * @param list
     * @return
     */
    public static List<String> extraOperation(List<String> list){
        List<String> content = new ArrayList();
        for (String s : list) {
            content.add(s);
            // 如果有额外的注释,则加在* @return后面
            if(extraMethodNote.size()>0){
                if(s != null && s.trim().startsWith("* @return")){
                    content.addAll(extraMethodNote);
                }
            }
        }
        return content;
    }


}
