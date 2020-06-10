package util.generator.entity;

import com.lxw.mutil.config.DefaultConfig;
import lombok.Data;
import util.generator.MList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-19 17:53
 */
@Data
public class MGGlobalConfig extends DefaultConfig {
    /**
     * 数据源信息
     */
    private String url = "jdbc:mysql://123.57.128.136/scott?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
    private String userName = "root";
    private String password = "i&Z*M$lxw@9yb2#E33%Tsz";
    private String dbType = "mysql";

    /**
     * 作者
     */
    private String author = "荔谢文";
    /**
     * 创建时间
     */
    private String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    /**
     * 核心包名
     */
    private String group = "com.lxw";

    public void setGroup(String group) {
        this.group = group;
        this.entityPackage = group + ".entity";
        this.controllerPackageName = group + ".controller";
        this.servicePackageName = group + ".service";
        this.mapperPackageName = group + ".mapper";
    }

    /**
     * 实体类包名
     */
    private String entityPackage;
    /**
     * controller包名
     */
    private String controllerPackageName;
    /**
     * service包名
     */
    private String servicePackageName;
    /**
     * mapper接口包名
     */
    private String mapperPackageName;

    private String basicEntity = "BasicEntity";
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
    private boolean isLombok = false;
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
    private String decimal = "Double";
    /**
     * add insert save 可选
     */
    private String cName = "add";
    /**
     * get find select 可选
     */
    private String rName = "find";
    private String uName = "update";
    private String dName = "delete";


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
    private MList findXxxsByPageNote;

    /**
     * 条件查询
     */
    private MList findNote;

    // vue部分
    /**
     * 分页列表
     */
    private String pageSizes = "[3,5,10,15]";
    /**
     * 每页显示的条数
     */
    private int pageSize = 3;




}
