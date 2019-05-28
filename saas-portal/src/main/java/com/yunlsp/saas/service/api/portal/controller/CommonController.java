package com.yunlsp.saas.service.api.portal.controller;

import com.alibaba.fastjson.JSONObject;
import com.yunlsp.common.FastDFSClient;
import com.yunlsp.common.domain.Result;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import com.yunlsp.saas.service.api.portal.service.SendRemindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author 许路路
 */
@RestController
@Api(description = "公共接口", tags = {"common interface"})
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private SendRemindService messageService;

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @PostMapping("fileUpload")
    public Result fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileId = FastDFSClient.uploadFile(file.getInputStream(), file.getOriginalFilename());
        logger.info("Upload local file " + file.getOriginalFilename() + " ok, filed=" + fileId);
        if (!StringUtils.hasText(fileId)) {
            logger.error("文件上传失败," + file.getOriginalFilename());
            return Result.builder(false).msg("文件上传失败!").build();
        }
        return Result.builder(true).msg("success").T(fileId).build();
    }

    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码")
    @GetMapping("msgCode/get")
    public Result messageCode(HttpServletRequest request, HttpSession session, String phone, String imageCode) {
        //先验证图形验证码
        Result returnT = imageCodeValidate(request, imageCode);
        if (!returnT.getSuccess()) {
            return returnT;
        }
        session.removeAttribute(Conf.IMAGE_CODE_SESSION_KEY);
        //TODO 同一个手机号十分钟内最多允许发三条

        //产生短信验证码内容
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        String validateCode = sb.toString();
        //发送短信
        messageService.sendAuthCodeMessage(phone, validateCode);
        //设置session 10分钟有效
        session.setAttribute(Conf.MESSAGE_CODE_SESSION_KEY, msgCodeSessionValue(validateCode, phone));
        session.setMaxInactiveInterval(60 * 10);
        return returnT;
    }

    @ApiOperation(value = "验证短信验证码是否正确", notes = "验证短信验证码是否正确 正确后返回一个唯一key")
    @RequestMapping(value = "msgCode/validate", method = {RequestMethod.POST, RequestMethod.GET})
    public String validate(String value, String phone, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Result returnT = messageCodeValidate(request, value, phone);
        if (returnT.getSuccess()) {
            jsonObject.put("success", true);
            String unique = UUID.randomUUID().toString().replace("-", "");
            request.getSession().setAttribute(Conf.MESSAGE_CODE_SUCCCESS_IDENTIFY, unique);
            //10分钟有效
            request.getSession().setMaxInactiveInterval(60 * 10);
            jsonObject.put("key", unique);
            return jsonObject.toJSONString();
        }
        jsonObject.put("success", false);
        jsonObject.put("msg", returnT.getMsg());
        return jsonObject.toJSONString();
    }

    /**
     * 图形验证码验证
     *
     * @param request
     * @param value
     * @return
     */
    public static Result imageCodeValidate(HttpServletRequest request, String value) {
        Result returnT = sessionValidate(request, Conf.IMAGE_CODE_SESSION_KEY, value);
        if (!returnT.getSuccess()) {
            returnT.setMsg("图形" + returnT.getMsg());
        }
        return returnT;
    }

    /**
     * 短信验证码验证
     *
     * @param request
     * @param value
     * @param phone
     * @return
     */
    public static Result messageCodeValidate(HttpServletRequest request, String value, String phone) {
        Result returnT = sessionValidate(request, Conf.MESSAGE_CODE_SESSION_KEY, CommonController.msgCodeSessionValue(value, phone));
        if (!returnT.getSuccess()) {
            returnT.setMsg("短信" + returnT.getMsg());
        }
        return returnT;
    }

    /**
     * session 验证
     *
     * @param request
     * @param sessionKey
     * @param value
     * @return
     */
    public static Result sessionValidate(HttpServletRequest request, String sessionKey, String value) {
        String valueCode = (String) request.getSession().getAttribute(sessionKey);
        if (!StringUtils.hasText(valueCode)) {
            return Result.builder(false).code(400).msg("验证码过期,请重新获取").build();
        }
        if (!valueCode.equalsIgnoreCase(value)) {
            return Result.builder(false).code(400).msg("验证码输入错误").build();
        }
        return Result.builder(true).build();
    }

    public static String msgCodeSessionValue(String codeValue, String phone) {
        return codeValue + "," + phone;
    }
}
