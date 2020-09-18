package com.lxw.mutil.component.generator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-02 18:06
 */
@Data
public class MList {
    public static String tab = "    ";
    private List<String> arrayList;

    public MList() {
        this.arrayList = new ArrayList<>();
    }
    // 侧重点:传入tab=0 为了格式看起来整齐
    public boolean add(int tab, String s, String... placeholder) {
        if(tab>0){
            return arrayList.add(getTab(tab) + String.format(s, placeholder));
        }else{
            return arrayList.add(String.format(s, placeholder));
        }
    }
    // 侧重点:不需要传入tab在不需要格式整齐的情况下为了书写简洁
    public boolean add(String s, String... placeholder) {
        return arrayList.add(String.format(s, placeholder));
    }

    public boolean addAll(List<String> list){
        return arrayList.addAll(list);
    }

    public boolean addAll(MList list){
        return arrayList.addAll(list.arrayList);
    }

    /**
     * 获取指定数量的tab(1tab=4个空格)
     *
     * @param num tab的数量
     * @return 指定数量的tab
     */
    public static String getTab(int num) {
        String s = tab;
        if (num > 1) {
            for (int i = 0; i < num - 1; i++) {
                s += tab;
            }
        }
        return s;
    }
}
