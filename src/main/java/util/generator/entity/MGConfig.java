package util.generator.entity;

import lombok.Data;
import util.MStringUtil;

/**
 * @author lixiewen
 * @create 2020-01-19 17:40
 */
@Data
public class MGConfig extends MGGlobalConfig {
    /**
     * 必须传入tableName
     * 不提供无参构造器
     * @param tableName
     */
    public MGConfig(String tableName){
        this.tableName = tableName;
        this.entityName = MStringUtil.toUpperCaseFirstOne(tableName);
        this.entityName4Camel = MStringUtil.toLowerCaseFirstOne(tableName);
        if(entityName.contains("_")){
            this.entityName = MStringUtil._x2X(entityName);
            this.entityName4Camel = MStringUtil._x2X(entityName);
        }
    }
    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类名
     */
    private String entityName;

    /**
     * 表中默认排序字段
     */
    private String defaultSort;

    private String entityName4Camel;


}
