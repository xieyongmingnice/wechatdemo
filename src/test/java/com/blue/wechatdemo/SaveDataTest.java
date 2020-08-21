package com.blue.wechatdemo;

import com.blue.wechatdemo.model.resp.SingleDataResult;
import com.blue.wechatdemo.util.AESUtil;
import com.blue.wechatdemo.util.RSAEncryptionDemo;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class SaveDataTest {

    public static  final String DMZ_PHHC_FRIENDSHIP_KEY="DMZ_PHHC_FRIENDSHIP";

    // 调用接口demo
    @Test
    public void saveData(){
        // 处方数据
        String dataStr = "{" +
                "\"hospitalId\":\"37818012-5c55-4aa1-99a8-fa5da2fa8358\"," +
                "    \"prescriptionCode\":\"M081201\"," +
                "    \"prescriptionDate\":\"2020-08-12 14:47:36\"," +
                "\"dispenseDoctorId\":\"\"," +
                "\"patientBirthday\":\"1989-08-12\"," +
                "    \"patientName\":\"M081201\"," +
                "    \"patientNumber\":\"\"," +
                "    \"patientRegisterCode\":\"M081201\"," +
                "    \"patientSex\":\"女\"," +
                "    \"patientWeight\":\"56.0\"," +
                "    \"specialCrowd\":\"\"," +
                "    \"diagnoseList\":[" +
                "        \"急性肾衰恢复期\"," +
                "\"肾衰竭\"" +
                "    ]," +
                "    \"drugList\":[" +
                "        {" +
                "            \"drugFrequency\":\"q12h1\"," +
                "            \"drugHisId\":\"5349\"," +
                "            \"drugRoute\":\"口服\"," +
                "            \"quantity\":\"1.0\"," +
                "            \"quantityUnit\":\"kg\"," +
                "            \"singleDose\":\"6.0\"," +
                "            \"singleDoseUnit\":\"g\"" +
                "        }," +
                "        {" +
                "            \"drugFrequency\":\"q12h1\"," +
                "            \"drugHisId\":\"864\"," +
                "            \"drugRoute\":\"口服\"," +
                "            \"quantity\":\"1.0\"," +
                "            \"quantityUnit\":\"kg\"," +
                "            \"singleDose\":\"5.0\"," +
                "            \"singleDoseUnit\":\"ml\"" +
                "        }" +
                "]" +
                "}";
        // 加密处方数据
        String encryptedData = RSAEncryptionDemo.encipher(dataStr);
        // 组织http请求
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.1.66:50080/DrugSafeForWeChat/thirdHospital/saveData";

        // 请求添加header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // header AccessKey 的内容也为加密串
        headers.set("AccessKey", AESUtil.encrypt("phhcMpDmz",DMZ_PHHC_FRIENDSHIP_KEY));

        HttpEntity<String> request = new HttpEntity<>(encryptedData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        // 获取返回 code返回200 msg返回操作成功 表明数据存储成功
        System.out.println(response.getBody());
        System.out.println(AESUtil.encrypt("phhcMpDmz", DMZ_PHHC_FRIENDSHIP_KEY));
    }


    // 调用生成指导单接口
    // 从H5跳转到小程序 如果跳转链接中带有patientId及hospitalCode参数，则会自动请求该接口为用户生成指导单。
    @Test
    public void genGuidance(){
        // 组织http请求
        RestTemplate restTemplate = new RestTemplate();
        // 接口地址
        String url = "http://192.168.1.66:8080/DrugSafeForWeChat/mp/prescriptionForward/generateGuidance?" +
                "userBaseId={userBaseId}&patientRegisterCode={patientRegisterCode}&hospitalCode={hospitalCode}";
        // header AccessKey 的内容也为加密串
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("userBaseId","fc4f88bd-3d99-4dd1-9f0b-2836732824a2");
        map.add("patientRegisterCode","M081201");
        map.add("hospitalCode","FnAg/4kkGataidJySwSRLg==");

        ResponseEntity<String> response = restTemplate.postForEntity(url, null,String.class,
                "fc4f88bd-3d99-4dd1-9f0b-2836732824a2","M081201","FnAg/4kkGataidJySwSRLg==");

        // 获取返回 code返回200 msg返回操作成功 表明数据存储成功
        System.out.println(response.getBody());
    }
}
