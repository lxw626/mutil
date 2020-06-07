package util.generator;

import util.dbUtil.MColumn;

import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-17 17:55
 */
public class VueGenerator extends BasicGenerator{
    public List<String> vueTemplate(List<MColumn> columns) {
        MList list = new MList();
        list.add(0,"<template>");
        list.add(1,"<div>");
        list.add(2,"<el-table :data=\"%ss\">",mGConfig.getEntityName4Camel());
        for (MColumn column : columns) {
            String fieldName = column.getFieldName();
            String columnComment = column.getColumnComment();
            list.add(3,"<el-table-column prop=\"%s\" label=\"%s\"></el-table-column>",fieldName,columnComment);
        }
        list.add(2,"</el-table>");
        list.add(2,"<el-pagination");
        list.add(3,"@size-change=\"sizeChangeHandle\" @current-change=\"currentChangeHandle\"");
        list.add(3,"layout=\"total, sizes, prev, pager, next, jumper\" :current-page=\"currentPage\"");
        list.add(3,":page-sizes=\"%s\" :page-size=\"pageSize\" :total=\"total\">",mGConfig.getPageSizes());
        list.add(2,"</el-pagination>");
        list.add(1,"</div>");
        list.add(0,"</template>");
        return list.getArrayList();
    }
    public List<String> vueScript(List<MColumn> columns) {
        MList list = new MList();
        list.add(0,"<script>");
        list.add(1,"export default {");
        list.add(2,"data: function () {");
        list.add(3,"return {");
        list.add(4,"%ss: [],",mGConfig.getEntityName4Camel());
        list.add(4,"// 新增(新增前需要清空emp),编辑(需要将当前行赋值给emp),查询(与查询条件双向绑定)共用");
        list.add(4,"%s: { ",mGConfig.getEntityName4Camel());
        for (MColumn column : columns) {
            String fieldName = column.getFieldName();
            list.add(5,"%s: undefined,",fieldName);
        }
        list.add(4,"},");
        list.add(4,"total: 0,");
        list.add(4,"currentPage: 1,");
        list.add(4,"pageSize: %s,",mGConfig.getPageSize()+"");
        list.add(4,"sortInfo: '%s'",mGConfig.getDefaultSort());
        list.add(3,"}");
        list.add(2,"},");
        list.add(2,"methods: {");
        list.add(3,"// pageSize变动,触发重新从后台加载数据");
        list.add(3,"sizeChangeHandle(pageSize) {");
        list.add(4,"this.pageSize = pageSize");
        list.add(4,"this.get%ssByPage()",mGConfig.getEntityName());
        list.add(3,"},");
        list.add(3,"// currentPage变动,触发从后台加载数据");
        list.add(3,"currentChangeHandle(currentPage) {");
        list.add(4,"this.currentPage = currentPage");
        list.add(4,"this.get%ss()",mGConfig.getEntityName());
        list.add(3,"},");
        list.add(3,"async get%ss() {",mGConfig.getEntityName());
        list.add(4,"let url = 'get%ssByPage'",mGConfig.getEntityName());
        list.add(4,"let {%s, currentPage, pageSize, sortInfo} = this",mGConfig.getEntityName4Camel());
        list.add(4,"// 组装分页与排序");
        list.add(4,"%s.currentPage = currentPage",mGConfig.getEntityName4Camel());
        list.add(4,"%s.pageSize = pageSize",mGConfig.getEntityName4Camel());
        list.add(4,"%s.sortInfo = sortInfo",mGConfig.getEntityName4Camel());
        list.add(4,"let pageInfo = await ajax(url, %s, 'POST')",mGConfig.getEntityName4Camel());
        list.add(4,"this.%ss = pageInfo.list",mGConfig.getEntityName4Camel());
        list.add(4,"this.total = pageInfo.total");
        list.add(3,"},");
        list.add(2,"},");
        list.add(2,"mounted() {");
        list.add(3,"this.get%ss()",mGConfig.getEntityName());
        list.add(2,"}");
        list.add(1,"}");
        list.add("</script>");
        return list.getArrayList();
    }
    public List<String> vueStyle() {
        MList list = new MList();
        list.add("<style>");
        list.add("");
        list.add("</style>");
        return list.getArrayList();
    }
    public List<String> content(List<MColumn> columns) {
        MList list = new MList();
        list.addAll(vueTemplate(columns));
        list.addAll(vueScript(columns));
        list.addAll(vueStyle());
        return list.getArrayList();
    }

    public static void main(String[] args) {
//        List<MColumn> columns = getColumns();
//        List<String> content = content(columns);
//        generate(content,vueFullName);
    }
}
