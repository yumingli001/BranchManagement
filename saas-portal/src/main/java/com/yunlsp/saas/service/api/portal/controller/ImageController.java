package com.yunlsp.saas.service.api.portal.controller;

import com.alibaba.fastjson.JSONObject;
import com.yunlsp.common.RandomUtils;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author 许路路
 */
@Api(description = "图形验证码接口", tags = {"image validate code"})
@Controller
public class ImageController {

    private static final int[] random_length = {4, 5, 6};

    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @GetMapping({"code/authCode"})
    public void getAuthCode(HttpServletResponse response, HttpSession session)
            throws IOException {
        int width = 93;
        int height = 37;
        //设置response头信息  禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman", 0, 28));
        g.fillRect(0, 0, width, height);
        Random random = new Random();
        //绘制干扰线
        for (int i = 0; i < 40; i++) {
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(15);
            int y1 = random.nextInt(18);
            g.drawLine(x, y, x + x1, y + y1);
        }

        int length = random_length[random.nextInt(random_length.length)];
        //绘制字符
        String strCode = RandomUtils.randomString(length);
        for (int i = 0; i < strCode.length(); i++) {
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawChars(strCode.toCharArray(), i, 1, 13 * i + 6, 28);
        }
        //将字符保存到session中用于前端的验证 3分钟有效
        session.setAttribute(Conf.IMAGE_CODE_SESSION_KEY, strCode);
        session.setMaxInactiveInterval(60 * 3);
        g.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();
    }

    /**
     * 创建颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 进行验证用户的验证码是否正确
     *
     * @param value   用户输入的验证码
     * @param request HttpServletRequest对象
     * @return 一个String类型的字符串。格式为：<br/>
     * {"res",boolean},<br/>
     * 如果为{"res",true}，表示验证成功<br/>
     * 如果为{"res",false}，表示验证失败
     */
    @ApiOperation(value = "验证验证码是否正确", notes = "验证验证码是否正确")
    @RequestMapping(value = "code/validate", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String validate(String value, HttpSession session, HttpServletRequest request) {
        String valueCode = (String) request.getSession().getAttribute(Conf.IMAGE_CODE_SESSION_KEY);
        JSONObject jsonObject = new JSONObject();
        if (valueCode != null) {
            if (valueCode.equalsIgnoreCase(value)) {
                jsonObject.put("success", true);
                return jsonObject.toJSONString();
            }
        }
        jsonObject.put("success", false);
        jsonObject.put("msg", "图形验证码错误");
        return jsonObject.toJSONString();
    }
}
