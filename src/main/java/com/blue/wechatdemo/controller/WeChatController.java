package com.blue.wechatdemo.controller;

import com.blue.wechatdemo.util.WeChatUtil;
import com.blue.wechatdemo.util.model.WeChatSignature;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * rest
 */
@RestController
@RequestMapping("wechat")
public class WeChatController {

    @RequestMapping("signature")
    public WeChatSignature getSignature(@RequestParam("url") String url){
        WeChatUtil weChatUtil = new WeChatUtil();
        return weChatUtil.getSignature(url);
    }
}
