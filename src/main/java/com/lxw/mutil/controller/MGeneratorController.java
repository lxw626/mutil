package com.lxw.mutil.controller;

import com.lxw.mutil.component.generator.MgConfig;
import com.lxw.mutil.entity.MResponse;
import com.lxw.mutil.entity.MgGlobalConfig;
import com.lxw.mutil.entity.MgTableConfig;
import com.lxw.mutil.service.MgGlobalConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class MGeneratorController {

    @Autowired
    private MgGlobalConfigService mgGlobalConfigService;

    @GetMapping("/getMgGlobalConfig")
    public MgGlobalConfig getMgGlobalConfig(Integer gId,HttpServletRequest request){
        MgGlobalConfig mgGlobalConfig;
        String userName = request.getSession().getAttribute("userName").toString();
        if(gId == null){
            Object userNameGid = request.getSession().getAttribute(userName+"Gid");
            if(userNameGid != null){
                gId = (Integer)userNameGid;
            }
        }
        if(gId != null){
            mgGlobalConfig = mgGlobalConfigService.findByPrimaryKey(gId);
            MgGlobalConfig mgGlobalConfig4Update = new MgGlobalConfig();
            mgGlobalConfig4Update.setLastVisitTime(new Date());
            mgGlobalConfig4Update.setGId(gId);
            mgGlobalConfigService.updateByPrimaryKey(mgGlobalConfig4Update);
            request.getSession().setAttribute(userName+"Gid",gId);
        }else{
            // 查询最后一次访问的
            mgGlobalConfig = mgGlobalConfigService.lastVisitMgGlobalConfig();
        }
        return mgGlobalConfig;
    }

    @GetMapping("/getMgGlobalConfigList")
    public List<Map<String,String>> getMgGlobalConfigList(HttpServletRequest request){
        String userName = request.getSession().getAttribute("userName").toString();
        List<Map<String,String>> result = new ArrayList();
        Map<String,String> map1 = new HashMap();
        map1.put("gId","1");
        map1.put("gName","mgGlobalConfig1");
        map1.put("enabled","1");
        result.add(map1);
        Map<String,String> map2 = new HashMap();
        map2.put("gId","2");
        map2.put("gName","mgGlobalConfig2");
        map2.put("gName","mgGlobalConfig2");
        map1.put("enabled","0");
        result.add(map2);
        return result;
    }

    @PostMapping("/saveMgGlobalConfig")
    public MResponse saveMgGlobalConfig(@RequestBody MgGlobalConfig mgGlobalConfig){
//        mgGlobalConfigService.save(mgGlobalConfig);
        return new MResponse().success().setMsg("保存成功");
    }

    @PostMapping("/addMgGlobalConfig")
    public MResponse addMgGlobalConfig(@RequestBody MgGlobalConfig mgGlobalConfig,HttpServletRequest request){
        String userName = request.getSession().getAttribute("userName").toString();
        mgGlobalConfig.setUserName(userName);
        System.out.println(mgGlobalConfig);
        return new MResponse().success().setMsg("新增成功");
    }


    @GetMapping("/login")
    public String login (HttpServletRequest request){
        request.getSession().setAttribute("userName","lxw");
        String id = request.getSession().getId();
        return id;
    }

    @PostMapping("/generate")
    public  void  generate(@RequestBody MgTableConfig mgTableConfig,HttpServletRequest request){
        MgConfig mgConfig = new MgConfig();
        // 将mgTableConfig拷贝到mgConfig
        BeanUtils.copyProperties(mgTableConfig,mgConfig);
        // 将全局变量拷贝到mgConfig
        String userName = request.getSession().getAttribute("userName").toString();
        MgGlobalConfig mgGlobalConfig = new MgGlobalConfig();
        mgGlobalConfig.setUserName(userName);
        mgGlobalConfig.setEnabled(true);
        List<MgGlobalConfig> mgGlobalConfigs = mgGlobalConfigService.find(mgGlobalConfig);
        if(mgGlobalConfigs.size()<1){
            throw new RuntimeException("没有查到全局配置");
        }
        mgGlobalConfig = mgGlobalConfigs.get(0);
        BeanUtils.copyProperties(mgGlobalConfig,mgConfig);
        BeanUtils.copyProperties(mgTableConfig,mgConfig);

    }
}
