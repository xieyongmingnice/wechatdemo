package com.blue.wechatdemo.controller;

import com.blue.wechatdemo.util.WeChatUtil;
import com.blue.wechatdemo.util.model.WeChatSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生成微信jssdk签名的demo
 */
@RestController
@RequestMapping("wechat")
public class WeChatController {

    @Autowired
    WeChatUtil weChatUtil;

    @RequestMapping("signature")
    public WeChatSignature getSignature(@RequestParam("url") String url){
        return weChatUtil.getSignature(url);
    }
}
