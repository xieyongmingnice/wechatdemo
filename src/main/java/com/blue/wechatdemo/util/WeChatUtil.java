package com.blue.wechatdemo.util;

import com.blue.wechatdemo.util.model.WeChatSignature;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
public class WeChatUtil {

    private static final String APP_ID = "wxac13a04ce91a420a";

    private static final String APP_SECRET = "012d1f0f5175c2f2d7c125607138ec8e";

    private static final String WE_CHAT_GET_ACCESS_TOKEN_URL =
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    private static final String WE_CHAT_GET_JS_TICKET_URL =
            "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    private static String accessToken = null;
    private static String ticket = null;
    // accessToken 可用时长7200s 之后失效
    private Long expire = 60*60*2L;

    @Autowired
    private RedisUtil redisUtil;

    public String getAccessToken(){
        String accessToken = redisUtil.getStr("accessToken");
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = WE_CHAT_GET_ACCESS_TOKEN_URL.replace("APPID",APP_ID).replace("APPSECRET",APP_SECRET);
        String result = restTemplate.getForObject(url,String.class);
        Map<String,Object> accessTokenMap = com.phhc.util.JacksonUtil.getInstance().parseJsonToMap(result);

        if (null != accessTokenMap && ! accessTokenMap.isEmpty() ){
            if (null != accessTokenMap.get("access_token") && StringUtils.isNotBlank((String)accessTokenMap.get("access_token"))) {
                accessToken = (String)accessTokenMap.get("access_token");
                redisUtil.add("accessToken",accessToken,expire);
            }
        }
        return accessToken;
    }

    /**
     *
     * @param url 当前网页的URL，不包含#及其后面部分
     */
    public WeChatSignature getSignature(String url) {
        String ticket = redisUtil.getStr("ticket");
        if (StringUtils.isBlank(ticket)) {
            // 1 获取access_token （有效期7200秒，开发者必须在自己的服务全局缓存access_token）
            String accessToken = getAccessToken();
            // 2 用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）
            if (StringUtils.isNotBlank(accessToken)) {
                String ticketUrl = WE_CHAT_GET_JS_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(ticketUrl, String.class);
                Map<String, Object> ticketMap = com.phhc.util.JacksonUtil.getInstance().parseJsonToMap(result);
                if (null != ticketMap && ! ticketMap.isEmpty() ) {
                    if (null != ticketMap.get("ticket") && StringUtils.isNotBlank((String) ticketMap.get("ticket"))) {
                        ticket = (String)ticketMap.get("ticket");
                        redisUtil.add("ticket",ticket,expire);
                    }
                }
            }
        }
        /*
         * 3 生成签名
         * 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，
         * 不包含#及其后面部分） 。对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式
         * （即key1=value1&key2=value2…）拼接成字符串string1。这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，
         *  字段名和字段值都采用原始值，不进行URL 转义。
         */
        WeChatSignature signatureModel = new WeChatSignature();
        signatureModel.setAppId(APP_ID);
        String nonceStr = UUID.randomUUID().toString();
        signatureModel.setNonceStr(nonceStr);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        signatureModel.setTimestamp(timestamp);
        // 对字符串进行SHA1加密
        String shaRes = DigestUtils.sha1Hex("jsapi_ticket=" + ticket + "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp + "&url=" + url);
        signatureModel.setSignature(shaRes);
        return signatureModel;

    }






}
