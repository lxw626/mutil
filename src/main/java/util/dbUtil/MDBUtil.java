package util.dbUtil;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MDBUtil {
    private final static Logger LOGGER = Logger.getLogger(MDBUtil.class);
    private static Connection conn;
    private static  String dbType;
    /**
     * 用户名(mysql可以传入null,oracle必须传入用户名)
     */
    private static  String userName;
    private static  boolean isInit = false;

    /**
     * 初始化MDBUtil工具类
     * @param mdbConfig 数据源信息
     * @return 初始化结果
     */
    public static boolean initMDBUtil(MDBConfig mdbConfig){
        Connection conn = mdbConfig.getConn();
        if(conn == null){
            LOGGER.error("MDBUtil初始化失败,conn不能为空!");
        }
        MDBUtil.conn = conn;
        String dbType = mdbConfig.getDbType();
        if(dbType == null){
            LOGGER.error("MDBUtil初始化失败,dbType不能为空!");
        }
        MDBUtil.dbType = dbType;
        userName = mdbConfig.getUserName();
        isInit = true;
        LOGGER.info("MDBUtil初始化成功!");
        return true;
    }

    /**
     * 检查MDBUtil是否初始化,如果没有初始化则抛出异常
     */
    private static void chechInit(){
        if(!isInit){
           throw new RuntimeException("请先初始化MDBUtil");
        }
    }
    /**
     * 获取数据库表名
     * @return 表名集合
     */
    public static List<String> getTableNames() {
        chechInit();
        List<String> tableNames = new ArrayList<>();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            // 从元数据中获取到所有的表名
            dbType = dbType.toLowerCase();
            if("oracle".equals(dbType)){
                // Oracle数据库的前两个参数为用户名但是必须大写！必须大写！必须大写！
                rs = db.getTables(userName.toUpperCase(), userName.toUpperCase(), null, new String[]{"TABLE"});
            }else if("mysql".equals(dbType)){
                rs = db.getTables(null, null, null, new String[]{"TABLE"});
            }
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            LOGGER.error("获取数据库中表名列表失败", e);
        }
        Collections.sort(tableNames);
        return tableNames;
    }


    /**
     * 获取给定表的字段信息
     *
     * @param tableName 表名
     * @return 字段集合
     */
    public static List<ColumnInfo> getColumnInfos(String tableName) {
        chechInit();
        List<ColumnInfo> columnInfos = Collections.emptyList();
        if ("oracle".equals(dbType.toLowerCase())) {
            columnInfos = getColumnInfosForOracle(tableName);
        } else if("mysql".equals(dbType.toLowerCase())){
            columnInfos = getColumnInfosForMysql(tableName);
        }
        return columnInfos;
    }


    /**
     * 获取oracle给定表的表信息
     *
     * @param tableName 表名
     * @return 字段集合
     */
    private static List<ColumnInfo> getColumnInfosForOracle(String tableName) {
        chechInit();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        //与数据库的连接
        Statement statement;
        ResultSet rs;
        try {
            statement = conn.createStatement();
            String sql = "SELECT * FROM ( " +
                    "SELECT utc.column_name, utc.data_type, ucc.comments,utc.nullable,data_precision, data_scale " +
                    "FROM user_tab_columns utc, user_col_comments ucc " +
                    "WHERE utc.table_name = ucc.table_name AND utc.column_name = ucc.column_name AND utc.table_name =  '" + tableName + "' " +
                    "ORDER BY column_id " +
                    ") t1 LEFT JOIN (" +
                    "SELECT column_name AS pk " +
                    "FROM user_cons_columns " +
                    "WHERE constraint_name = ( " +
                    "SELECT constraint_name " +
                    "FROM user_constraints " +
                    "WHERE table_name =  '" + tableName + "' AND constraint_type = 'P' " +
                    ")" +
                    ") t2 ON t1.column_name = t2.pk ";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(rs.getString("COLUMN_NAME"));
                columnInfo.setColumnType(rs.getString("DATA_TYPE"));
                columnInfo.setColumnComment(rs.getString("COMMENTS"));
                String pk = rs.getString("PK");
                if (pk != null){
                    columnInfo.setPrimaryKey(true);
                }else {
                    columnInfo.setPrimaryKey(false);
                }
                String nullable = rs.getString("NULLABLE");
                if("Y".equals(nullable)){
                    columnInfo.setAllowNull(true);
                }else{
                    columnInfo.setAllowNull(false);
                }
                String scale = rs.getString("DATA_SCALE");
                String precision = rs.getString("DATA_PRECISION");
                if(precision!=null){
                    columnInfo.setPrecision(Integer.valueOf(precision));
                }
                if(scale!=null){
                    columnInfo.setScale(Integer.valueOf(scale));
                }
                columnInfos.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnInfos;
    }

    /**
     * 获取mysql给定表的表信息
     * @param tableName 表名
     * @return 列信息集合
     */
    private static List<ColumnInfo> getColumnInfosForMysql(String tableName) {
        chechInit();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        Statement  statement;
        ResultSet rs;
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                ColumnInfo columnInfo = new ColumnInfo();
                // 设置列的名字
                columnInfo.setColumnName(rs.getString("Field"));
                String typeAndLength = rs.getString("Type");
                String type = typeAndLength;
                if(type.contains("(")){
                    type = type.substring(0,type.indexOf("("));
                }
                // 设置列的类型
                columnInfo.setColumnType(type);
                // 如果是数字列设置总位数和小数位数
                if(isMysqlNumber(type)){
                    if(typeAndLength.contains("int")){
                        columnInfo.setPrecision(Integer.parseInt(typeAndLength.substring(typeAndLength.indexOf("(")+1, typeAndLength.length() - 2)));
                        columnInfo.setScale(0);
                    }else{
                        String[] split = typeAndLength.substring(typeAndLength.indexOf("(")+1, typeAndLength.length() - 1).split(",");
                        columnInfo.setPrecision(Integer.parseInt(split[0]));
                        columnInfo.setScale(Integer.parseInt(split[1]));
                    }
                }
                // 设置列注释
                columnInfo.setColumnComment(rs.getString("Comment"));
                // 判断是否为主键
                if("PRI".equals(rs.getString("Key"))){
                    columnInfo.setPrimaryKey(true);
                }else {
                    columnInfo.setPrimaryKey(false);
                }
                // 判断是否为null
                if("YES".equals(rs.getString("Null"))){
                    columnInfo.setAllowNull(true);
                }else{
                    columnInfo.setAllowNull(false);
                }
                columnInfos.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnInfos;
    }

    /**
     * 判断Mysql数据库中列的类型是否为数值类型
     * @param type 数据类型
     * @return 是否是数字类型
     */
    private static boolean isMysqlNumber(String type){
        List<String> list = new ArrayList<>(Arrays.asList(
            "tinyint","smallint","mediumint","int","integer",
            "bigint","float","double","decimal"
        ));
        for (String s : list) {
            if (s.equals(type)){
                return true;
            }
        }
        return false;
    }

    

    




}
