package com.lxw.mutil.component;


import com.lxw.mutil.component.generator.MgConfig;
import util.MStringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoMgConfig {

    public static MgConfig config(MgConfig mgConfig){

        if (mgConfig.getEnabled() == null) {
            mgConfig.setEnabled(true);
        }
        String group = mgConfig.getGroup();
        // 自动配置包名
        packageNameAutoMgConfig(mgConfig);
        if (mgConfig.getDbType() == null) {
            mgConfig.setDbType("mysql");
        }
        if (mgConfig.getCreateTimeFormat() == null) {
            mgConfig.setCreateTimeFormat(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        if (mgConfig.getSortInfo() == null) {
            mgConfig.setSortInfo("sortInfo");
        }
        // todo 是否要排序 等待起个好名字

        if (mgConfig.getLombok() == null) {
            mgConfig.setLombok(true);
        }
        if (mgConfig.getFieldComment() == null) {
            mgConfig.setFieldComment(true);
        }
        if (mgConfig.getToStringMethod() == null) {
            if (mgConfig.getLombok() != null && mgConfig.getLombok()) {
                mgConfig.setToStringMethod(false);
            }
            if (mgConfig.getLombok() != null && mgConfig.getLombok() == false) {
                mgConfig.setToStringMethod(true);
            }
        }
        /**
         * 默认生成的小数类型
         * 默认Double,可选BigDecimal
         */
        if (mgConfig.getNumberType() == null) {
            mgConfig.setNumberType("Double");
        }
        // 自动配置默认方法名
        methodNameAutoMgConfig(mgConfig);

        if (mgConfig.getPageSize() == null) {
            mgConfig.setPageSize(10);
        }
        if (mgConfig.getPageSizes() == null) {
            mgConfig.setPageSizes("[3,5,10,15]");
        }
        String tableName = mgConfig.getTableName();
        String entityName = mgConfig.getEntityName();
        if(entityName == null){
            entityName = MStringUtil.toUpperCaseFirstOne(tableName);
            entityName = MStringUtil._x2X(entityName);
            mgConfig.setEntityName(entityName);
        }
        String entityName4Camel = mgConfig.getEntityName4Camel();
        if(entityName4Camel == null){
            entityName4Camel = MStringUtil.toLowerCaseFirstOne(entityName);
            mgConfig.setEntityName4Camel(entityName4Camel);
        }
        return mgConfig;
    }

    /**
     * 自动配置包名
     * @param mgConfig
     * @return
     */
    private static MgConfig packageNameAutoMgConfig(MgConfig mgConfig){
        String group = mgConfig.getGroup();
        String entityPackage = mgConfig.getEntityPackage();
        if (entityPackage == null) {
            mgConfig.setEntityPackage(group + ".entity");
        }
        String controllerPackage = mgConfig.getControllerPackage();
        if (controllerPackage == null) {
            mgConfig.setControllerPackage(group + ".controller");
        }
        String servicePackage = mgConfig.getServicePackage();
        if (servicePackage == null) {
            mgConfig.setServicePackage(group + ".service");
        }
        String mapperPackage = mgConfig.getMapperPackage();
        if (mapperPackage == null) {
            mgConfig.setMapperPackage(group + ".dao");
        }
        return mgConfig;
    }

    /**
     * 自动配置方法名
     * @param mgConfig
     * @return
     */
    private static MgConfig methodNameAutoMgConfig(MgConfig mgConfig){
        if (mgConfig.getAddName() == null) {
            mgConfig.setAddName("add");
        }
        if (mgConfig.getDeleteName() == null) {
            mgConfig.setDeleteName("delete");
        }
        if (mgConfig.getUpdateName() == null) {
            mgConfig.setUpdateName("update");
        }
        if (mgConfig.getSelectName() == null) {
            mgConfig.setSelectName("find");
        }
        return mgConfig;
    }

}
