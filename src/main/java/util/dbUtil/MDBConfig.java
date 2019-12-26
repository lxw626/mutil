package util.dbUtil;

import lombok.Data;

import java.sql.Connection;

/**
 * @author lixiewen
 * @create 2019-12-26 11:29
 */
@Data
public class MDBConfig {
    private Connection conn;
    private  String dbType;
    private  String userName;
}
