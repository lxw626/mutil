package util.generator.entity;

import com.lxw.mutil.config.DefaultConfig;
import lombok.Data;
import util.MStringUtil;
import util.generator.MList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-19 17:40
 */
@Data
public class MgConfig extends DefaultConfig {
    /**
     * 必须传入tableName
     * 不提供无参构造器
     * @param tableName
     */
    public MgConfig(String tableName){
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

    // 以下为全局配置

    private Integer gId = 1;

    private String uid = "lixiewen";


    /**
     * 数据源信息
     */
    private String url;

    private String dbUserName;
    private String dbPassword;
    private String dbType = "mysql";

    /**
     * 作者
     */
    private String author;
    /**
     * 创建时间
     */
    private String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    /**
     * 核心包名
     */
    private String group;

    public void setGroup(String group) {
        this.group = group;
        this.entityPackage = group + ".entity";
        this.controllerPackage = group + ".controller";
        this.servicePackage = group + ".service";
        this.mapperPackage = group + ".mapper";
    }

    /**
     * 实体类包名
     */
    private String entityPackage;
    /**
     * controller包名
     */
    private String controllerPackage;
    /**
     * service包名
     */
    private String servicePackage;
    /**
     * mapper接口包名
     */
    private String mapperPackage;

    private String basicEntity;
    /**
     * (BasicEntity中的)排序字段
     */
    private String sortInfo = "sortInfo";
    /**
     * 生成xml时是否加入排序子句
     */
    private boolean isSort = false;
    /**
     * 生成entity时是否采用lombok
     */
    private boolean isLombok = true;
    /**
     * 是否生成实体类的注释
     */
    private boolean isFieldComment = true;
    /**
     * 是否生成toString方法
     */
    private boolean isToString = true;
    /**
     * MResponse全名
     */
    private String mResponseName_full = "com.lxw.mutil.entity.MResponse";

    /**
     * 生成的小数类型
     * 默认Double,可选BigDecimal
     */
    private String numberType = "Double";
    /**
     * add insert save 可选
     */
    private String addName = "add";
    /**
     * get find select 可选
     */
    private String selectName = "find";
    private String updateName = "update";
    private String deleteName = "delete";


    /**
     * 附加的一些注释,比如作者,创建时间
     */
    private List<String> extraMethodNote;


    /**
     * 新增注释
     */
    private MList addNote;
    /**
     * 根据主键删除注释
     */
    private MList deleteByPrimaryKeyNote;
    /**
     * 根据主键更新注释
     */
    private MList updateByPrimaryKeyNote;
    /**
     * 根据主键查询注释
     */
    private MList findByPrimaryKeyNote;
    /**
     * 分页查询注释
     */
    private MList findByPageNote;

    /**
     * 条件查询
     */
    private MList findNote;

    // vue部分
    /**
     * 分页列表
     */
    private String pageSizes;
    /**
     * 每页显示的条数
     */
    private int pageSize;


}
