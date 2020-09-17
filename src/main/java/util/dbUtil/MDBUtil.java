package util.dbUtil;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MDBUtil {
    private final static Logger LOGGER = Logger.getLogger(MDBUtil.class);



    /**
     * 获取数据库表名
     * 用户名(mysql可以传入null,oracle必须传入用户名)
     * @return 表名集合
     */
    public static List<String> getTableNames(Connection conn,String dbType,String userName) {
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
    public static List<MColumn> getColumnInfos(Connection conn,String dbType,String tableName) {
        List<MColumn> MColumns = Collections.emptyList();
        if ("oracle".equals(dbType.toLowerCase())) {
            MColumns = getColumnInfosForOracle(conn,tableName);
        } else if("mysql".equals(dbType.toLowerCase())){
            MColumns = getColumnInfosForMysql(conn,tableName);
        }
        return MColumns;
    }


    /**
     * 获取oracle给定表的表信息
     *
     * @param tableName 表名
     * @return 字段集合
     */
    private static List<MColumn> getColumnInfosForOracle(Connection conn,String tableName) {
        List<MColumn> MColumns = new ArrayList<>();
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
                MColumn MColumn = new MColumn();
                MColumn.setColumnName(rs.getString("COLUMN_NAME"));
                MColumn.setColumnType(rs.getString("DATA_TYPE"));
                MColumn.setColumnComment(rs.getString("COMMENTS"));
                String pk = rs.getString("PK");
                if (pk != null){
                    MColumn.setPrimaryKey(true);
                }else {
                    MColumn.setPrimaryKey(false);
                }
                String nullable = rs.getString("NULLABLE");
                if("Y".equals(nullable)){
                    MColumn.setAllowNull(true);
                }else{
                    MColumn.setAllowNull(false);
                }
                String scale = rs.getString("DATA_SCALE");
                String precision = rs.getString("DATA_PRECISION");
                if(precision!=null){
                    MColumn.setPrecision(Integer.valueOf(precision));
                }
                if(scale!=null){
                    MColumn.setScale(Integer.valueOf(scale));
                }
                MColumns.add(MColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return MColumns;
    }

    /**
     * 获取mysql给定表的表信息
     * @param tableName 表名
     * @return 列信息集合
     */
    private static List<MColumn> getColumnInfosForMysql(Connection conn,String tableName) {
        List<MColumn> MColumns = new ArrayList<>();
        Statement  statement;
        ResultSet rs;
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                MColumn mColumn = new MColumn();
                // 设置列的名字
                mColumn.setColumnName(rs.getString("Field"));
                // 带长度的类型 例如 int(10) varchar(50) decimal(10,2) datetime
                String typeAndLength = rs.getString("Type");
                String type = typeAndLength;
                if(type.contains("(")){
                    type = type.substring(0,type.indexOf("("));
                }
                // 设置列的类型
                mColumn.setColumnType(type);
                // 如果是数字列设置总位数和小数位数
                if(isMysqlNumber(type)){
                    if(typeAndLength.contains("int")){
                        // 类型有可能是这样的 int(11) unsigned zerofill
                        mColumn.setPrecision(Integer.parseInt(typeAndLength.substring(typeAndLength.indexOf("(")+1, typeAndLength.indexOf(")"))));
                        mColumn.setScale(0);
                    }else{
                        String[] split = typeAndLength.substring(typeAndLength.indexOf("(")+1, typeAndLength.length() - 1).split(",");
                        mColumn.setPrecision(Integer.parseInt(split[0]));
                        mColumn.setScale(Integer.parseInt(split[1]));
                    }
                }
                // 设置列注释
                mColumn.setColumnComment(rs.getString("Comment"));
                // 判断是否为主键
                if("PRI".equals(rs.getString("Key"))){
                    mColumn.setPrimaryKey(true);
                }else {
                    mColumn.setPrimaryKey(false);
                }
                // 判断是否为null
                if("YES".equals(rs.getString("Null"))){
                    mColumn.setAllowNull(true);
                }else{
                    mColumn.setAllowNull(false);
                }
                MColumns.add(mColumn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return MColumns;
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
    /**
    public static DruidDataSource getDruidDataSource(String driverClassName,String username,String password,String url){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setUrl(url);
        return druidDataSource;
    }
    public static DruidDataSource getDruidDataSource4Mysql(String url,String username,String password){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }
     */

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection(String url,String userName,String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            LOGGER.error("获取数据库连接失败",e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("关闭数据库失败",e);
            }
        }
    }

    

    




}
