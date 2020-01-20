package com.lxw.service;

import com.lxw.entity.Emp;
import java.util.List;

public interface EmpService {

    /**
     * 新增
     * @param emp
     * @return
     */
    int add(Emp emp);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 根据主键修改
     * @param emp
     * @return
     */
    int updateByPrimaryKey(Emp emp);

    /**
     * 根据主键查找
     * @param id 主键
     * @return
     */
    Emp findByPrimaryKey(Integer id);

    /**
     * 分页查询
     * @param param
     * @return
     */
    List<Emp> find(Object param);

}
