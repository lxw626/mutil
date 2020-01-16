package com.lxw.mapper;

import com.lxw.entity.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface EmpMapper {
    int add(Emp emp);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKey(Emp emp);

    Emp findByPrimaryKey(Integer id);

    List<Emp> find(Emp emp);

}
