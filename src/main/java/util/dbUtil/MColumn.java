package util.dbUtil;


import lombok.Data;
import util.MStringUtil;
import util.generator.constant.MDBColumnType;

@Data
public class MColumn {
    private String columnName;
    private String columnType;
    private String columnComment;
    // 是否是主键
    private Boolean primaryKey;
    // 是否允许null
    private Boolean allowNull;
    // 总位数
    private Integer precision;
    // 小数位数
    private Integer scale;
    private String jdbcType;
    private String fieldName;
    private String fieldType;
    // todo 配置文件里面 开关
    private Boolean tinyint2Boolean = true;


    /**
     * 根据列的类型给Jdbc的类型赋值
     *
     * @param columnType
     */
    public void columnType2JdbcType(String columnType) {
        columnType = columnType.toUpperCase();
        jdbcType = columnType;
        if (columnType.startsWith("VARCHAR2") || columnType.startsWith("VARCHAR")) {
            jdbcType = "VARCHAR";
        } else if (columnType.startsWith("NUMBER")) {
            jdbcType = "DECIMAL";
        } else if (columnType.startsWith("TIMESTAMP")) {
            jdbcType = "TIMESTAMP";
        } else if (columnType.startsWith("INT")) {
            jdbcType = "INTEGER";
        }
    }

    /**
     * 根据列的类型给成员变量的类型赋值
     *
     * @param columnType
     */
    public void columnType2FieldType(String columnType) {
        columnType = columnType.toUpperCase();
        fieldType = columnType;
        if (columnType.startsWith(MDBColumnType.CHAR)) {
            fieldType = "String";
        } else if (columnType.startsWith(MDBColumnType.VARCHAR)) {
            fieldType = "String";
        } else if (columnType.startsWith(MDBColumnType.VARCHAR2)) {
            fieldType = "String";
        } else if (columnType.startsWith(MDBColumnType.NVARCHAR2)) {
            fieldType = "String";
        } else if (columnType.startsWith(MDBColumnType.DATE)) {
            fieldType = "Date";
        } else if (columnType.startsWith(MDBColumnType.TIMESTAMP)) {
            fieldType = "Date";
        } else if (columnType.startsWith(MDBColumnType._INT)) {
            fieldType = "Integer";
        } else if (columnType.startsWith(MDBColumnType.DECIMAL)) {
            fieldType = "BigDecimal";
        } else if (columnType.startsWith(MDBColumnType.NUMBER)) {
            // todo 处理与BasicGenerator的关系
            // 如果数据库列是NUMBER,默认做小数处理,在setScale里面重新判断
            fieldType = "BigDecimal";
        }
    }


    public void columnName2FieldName(String columnName) {
        // 解决数据库字段大驼峰命名问题
        fieldName = MStringUtil.toLowerCaseFirstOne(columnName);
        // 如果数据库是下划线命名风格
        if(fieldName.contains("_")){
            fieldName = MStringUtil._x2X(columnName.toLowerCase()
            );
        }
    }

    // TODO 数据库关键字
    public String getColumnName() {
        if("group".equals(columnName.toLowerCase())){
            columnName = "`"+columnName+"`";
        }
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
        // 给fieldName赋值
        columnName2FieldName(columnName);
    }


    public void setColumnType(String columnType) {
        this.columnType = columnType;
        // 给jdbcType赋值
        columnType2JdbcType(columnType);
        // 给FieldType赋值
        columnType2FieldType(columnType);
    }



    public void setScale(Integer scale) {
        this.scale = scale;
        // 如果数字总长度有值,小数位位0,则作为整数处理
        if (precision != null && scale == 0) {
            fieldType = "Integer";
        }
    }


    public String getFieldType() {
        if(tinyint2Boolean){
            if("tinyint".equals(columnType)){
                return "Boolean";
            }
        }
        return fieldType;
    }

    public String getFieldName() {
        if(tinyint2Boolean){
            if("tinyint".equals(columnType)){
                if(fieldName.startsWith("is")){
                    fieldName = fieldName.replaceAll("is","");
                    fieldName = MStringUtil.toLowerCaseFirstOne(fieldName);
                }
            }
        }
        return fieldName;
    }
}
