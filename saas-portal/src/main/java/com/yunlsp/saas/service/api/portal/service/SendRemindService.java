package com.yunlsp.saas.service.api.portal.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.yunlsp.common.HttpUtils;
import com.yunlsp.common.encryption.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
@Component
public class SendRemindService {

    private static final Logger logger = LoggerFactory.getLogger(SendRemindService.class);

    @NacosValue(value = "${message.validate.templete:SaasVerificationCode}")
    private String validate_message_templete;
    @NacosValue(value = "${remind.url:http://118.25.147.52:800}")
    private String remind_url;
    @NacosValue(value = "${message.send.path:/sendMessageV3}")
    private String send_message_path;
    @NacosValue(value = "${email.send.path:/sendEmail}")
    private String send_email_path;
    @NacosValue(value = "${remind.appname:saas}")
    private String remind_appname;
    @NacosValue(value = "${remind.key:Da0u1JKYWM}")
    private String remind_key;


    public void sendAuthCodeMessage(String phones, String code) {
        Map<String, String> param = new HashMap<>(16);
        param.put("template", validate_message_templete);
        param.put("{{varificationcode}}", code);
        param.put("{{valid}}", "10分钟");
        sendSMS(phones, param);
    }

    protected boolean sendSMS(String phones, Map<String, String> param) {
        String apiUrl = remind_url + send_message_path;
        try {
            param.put("appname", remind_appname);
            param.put("key", Md5Utils.getMD5(remind_appname + remind_key));
            param.put("mobile", phones);
            String response = HttpUtils.doPost(apiUrl, param);
            // 判断返回状态是否为200
            if (StringUtils.isNotBlank(response)) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                if (jsonObject.getInteger("code") == 200) {
                    return true;
                }
            }
            logger.error("短信发送失败：" + response);
            return false;
        } catch (Exception ex) {
            logger.error("发送短信异常", ex);
        }
        return false;
    }

}
