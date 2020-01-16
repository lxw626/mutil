package com.lxw.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lxw.entity.Emp;
import com.lxw.entity.MResponse;
import com.lxw.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class EmpController {
    @Autowired
    EmpService empService;

    /**
     * 新增
     * @param emp
     * @return
     * @Author: 荔谢文
     * @CreateDate: 2020-01-15
     */
    @PostMapping("/emp")
    public MResponse add(@RequestBody Emp emp) {
        empService.add(emp);
        return new MResponse().success("新增成功");
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     * @Author: 荔谢文
     * @CreateDate: 2020-01-15
     */
    @DeleteMapping("/emp/{id}")
    public MResponse deleteByPrimaryKey(@PathVariable("id") Integer id) {
        empService.deleteByPrimaryKey(id);
        return new MResponse().success("删除成功");
    }

    /**
     * 根据主键修改
     * @param emp
     * @return
     * @Author: 荔谢文
     * @CreateDate: 2020-01-15
     */
    @PutMapping("/emp")
    public MResponse updateByPrimaryKey(@RequestBody Emp emp) {
        empService.updateByPrimaryKey(emp);
        return new MResponse().success("修改成功");
    }

    /**
     * 根据主键查找
     * @param id 主键
     * @return
     * @Author: 荔谢文
     * @CreateDate: 2020-01-15
     */
    @GetMapping("/emp/{id}")
    public MResponse findByPrimaryKey(@PathVariable("id") Integer id) {
        Emp emp = empService.findByPrimaryKey(id);
        return new MResponse().success(emp);
    }

    /**
     * 分页查询
     * @param param
     * @return
     * @Author: 荔谢文
     * @CreateDate: 2020-01-15
     */
    @PostMapping("/getEmpsBypage")
    public PageInfo<Emp> find(@RequestBody Emp param) {
        Integer currentPage = param.getCurrentPage();
        Integer pageSize = param.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
        List<Emp> emps = empService.find(param);
        return new PageInfo<>(emps);
    }

}
