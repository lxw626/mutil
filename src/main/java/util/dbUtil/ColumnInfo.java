package util.dbUtil;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ColumnInfo {
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
    private String fieldName;
    private String fieldType;
    private String jdbcType;
}
