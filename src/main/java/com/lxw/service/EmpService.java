package com.lxw.service;

import com.lxw.entity.Emp;
import com.lxw.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmpService {
    @Autowired
    EmpMapper empMapper;

    /**
     * 新增
     * @param emp
     * @return
     */
    public int add(Emp emp) {
        return empMapper.add(emp);
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        return empMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据主键修改
     * @param emp
     * @return
     */
    public int updateByPrimaryKey(Emp emp) {
        return empMapper.updateByPrimaryKey(emp);
    }

    /**
     * 根据主键查找
     * @param id 主键
     * @return
     */
    public Emp findByPrimaryKey(Integer id) {
        return empMapper.findByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param param
     * @return
     */
    public List<Emp> find(Emp param) {
        return empMapper.find(param);
    }

}
