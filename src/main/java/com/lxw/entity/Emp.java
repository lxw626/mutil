package com.lxw.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Emp extends BasicEntity {
    /**
     * 员工编号
     */
    private Integer empno;

    /**
     * 姓名
     */
    private String ename;

    /**
     * 工作
     */
    private String job;

    /**
     * 领导编号
     */
    private Integer mgr;

    /**
     * 入职日期
     */
    private Date hiredate;

    /**
     * 工资
     */
    private BigDecimal sal;

    /**
     * 提成
     */
    private BigDecimal comm;

    /**
     * 部门编号
     */
    private Integer deptno;

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public BigDecimal getSal() {
        return sal;
    }

    public void setSal(BigDecimal sal) {
        this.sal = sal;
    }

    public BigDecimal getComm() {
        return comm;
    }

    public void setComm(BigDecimal comm) {
        this.comm = comm;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    @Override
    public String toString() {
        return "Emp{" +
        " empno=" + empno  +
        ", ename='" + ename + '\'' +
        ", job='" + job + '\'' +
        ", mgr='" + mgr + '\'' +
        ", hiredate='" + hiredate + '\'' +
        ", sal='" + sal + '\'' +
        ", comm='" + comm + '\'' +
        ", deptno='" + deptno + '\'' +
        '}';
    }
}
