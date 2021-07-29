// This file is auto-generated, don't edit it. Thanks.
package com.itheima.test;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;

public class sample {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static void main(String[] args_) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = sample.createClient("LTAI5tSc4SxGdecXkyp83Xdy", "I8SYcHpOjlM96Pxi9o9yDtQRy4UTIn");
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        // 复制代码运行请自行打印 API 的返回值
        sendSmsRequest.setPhoneNumbers("13316915805");
        sendSmsRequest.setSignName("黑马程序员");
        sendSmsRequest.setTemplateCode("SMS_189616640");
        sendSmsRequest.setTemplateParam("String.format(\"{\\\"%s\\\":\\\"%s\\\"}\",smsProperties.getParameterName(),param)");
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        System.out.println(sendSmsResponse);
    }
}