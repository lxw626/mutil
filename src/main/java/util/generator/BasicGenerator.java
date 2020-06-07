package util.generator;

/**
 * @author lixiewen
 * @create 2020-01-15 15:27
 */

import util.dbUtil.MColumn;
import util.dbUtil.MDBUtil;
import util.generator.entity.MGConfig;
import util.ioUtil.MIOUtil;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 17:52
 */
public class BasicGenerator {

    MGConfig mGConfig;










//    /**
//     * 生成的小数类型
//     * 默认Double,可选BigDecimal
//     */
//    public static String decimal = "Double";
//    /**
//     * add insert save 可选
//     */
//    public static String cName = "add";
//    /**
//     * get find select 可选
//     */
//    public static String rName = "find";
//    public static String uName = "update";
//    public static String dName = "delete";

    public static void generate(List<String> list, String fillFullName) {
        File file = new File(fillFullName);
        PrintWriter pw = MIOUtil.getPrintWriter(file);
        for (String text : list) {
            pw.write(text + "\n");
        }
        pw.flush();
        pw.close();
    }



    public static List<MColumn> getColumns(Connection conn,String dbType,String tableName) {
        List<MColumn> columns = MDBUtil.getColumnInfos(conn,dbType,tableName);
        MDBUtil.closeConnection(conn);
        return columns;
    }


    /**
     * 添加额外的操作:生成内容的最后一道流水线
     *
     * @param list
     * @return
     */
    public  List<String> extraOperation(List<String> list) {
        List<String> content = new ArrayList();
        for (String s : list) {
            content.add(s);
            // 如果有额外的注释,则加在* @return后面
            if (mGConfig.getExtraMethodNote() != null && mGConfig.getExtraMethodNote().size() > 0) {
                if (s != null && s.trim().startsWith("* @return")) {
                    content.addAll(mGConfig.getExtraMethodNote());
                }
            }
        }
        return content;
    }




}
