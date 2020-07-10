package com.lxw.mutil.controller;

import com.lxw.mutil.service.MgGlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.generator.entity.MgGlobalConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MGeneratorController {

    @Autowired
    private MgGlobalConfigService mgGlobalConfigService;

    @GetMapping("/getMgGlobalConfig")
    public MgGlobalConfig getMgGlobalConfig(HttpServletRequest request){
        // 登录
        request.getSession().setAttribute("user","lxw");
        // todo 查询参数1
        String user = request.getSession().getAttribute("user").toString();
        // 查询参数2 "enabled"
        MgGlobalConfig mgGlobalConfig = new MgGlobalConfig();
        mgGlobalConfig.setId(1);
        mgGlobalConfig.setGroup("com.lxw.mutil");
        mgGlobalConfig.setDbUserName("root");
        mgGlobalConfig.setDbPassword("i&Z*M$lxw@9yb2#E33%Tsz");
        mgGlobalConfig.setUrl("jdbc:mysql://123.57.128.136/mgenerator?useUnicode=true&characterEncoding=UTF-8&useSSL=true");
        return mgGlobalConfig;
    }
    @GetMapping("/getMgGlobalConfigList")
    public List<Map<String,String>> getMgGlobalConfigList(HttpServletRequest request){
        // 登录
        request.getSession().setAttribute("user","lxw");
        String user = request.getSession().getAttribute("user").toString();
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
    public void saveMgGlobalConfig(@RequestBody MgGlobalConfig mgGlobalConfig){
        mgGlobalConfigService.save(mgGlobalConfig);
    }
}
