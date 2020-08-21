package com.blue.wechatdemo.controller;

import com.blue.wechatdemo.util.AESUtil;
import com.blue.wechatdemo.util.RSAEncryptionDemo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller("guidance")
public class GuidanceController {
    public static  final String ACCESS_KEY = "DMZ_PHHC_FRIENDSHIP";

    /**
     * 传输处方数据接口
     * @return
     */
    @RequestMapping("sendPresData")
    public String sendPresData() {
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
        // 接口地址
        String url = "http://192.168.1.66:50080/DrugSafeForWeChat/thirdHospital/saveData";

        // 请求添加header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // header AccessKey 的内容也为加密串 由普华和诚提供
        headers.set("AccessKey", "Enfp2rA7zLUk2v2vGEv9JA==");

        HttpEntity<String> request = new HttpEntity<>(encryptedData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        // 获取返回 code返回200 msg返回操作成功 表明数据存储成功
        System.out.println(response.getBody());

        return "true";
    }
}
