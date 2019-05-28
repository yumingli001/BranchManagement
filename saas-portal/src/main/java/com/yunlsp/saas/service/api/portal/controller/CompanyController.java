package com.yunlsp.saas.service.api.portal.controller;

import com.yunlsp.common.domain.Result;
import com.yunlsp.common.exception.ParamException;
import com.yunlsp.saas.service.api.CompanyService;
import com.yunlsp.saas.service.api.model.CompanyInfoModel;
import com.yunlsp.saas.service.api.model.UserModel;
import com.yunlsp.saas.service.api.portal.annotation.UpdateUserInfo;
import com.yunlsp.saas.service.api.portal.constant.Conf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 许路路
 * @Date: 2019/3/7
 */
@Api(description = "往来户信息接口", tags = {"company info"})
@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @ApiOperation(value = "公司的详细信息", notes = "公司的详细信息")
    @GetMapping("detail")
    public Result<CompanyInfoModel> detail(HttpServletRequest request, String companyId) {
        UserModel user = (UserModel) request.getAttribute(Conf.SSO_USER);
        if (StringUtils.isEmpty(companyId)) {
            companyId = user.getCompanyId();
        }
        CompanyInfoModel model = companyService.companyInfo(companyId);
        return Result.builder(true).msg("success").T(model).build();
    }

    @ApiOperation(value = "公司信息修改", notes = "修改公司信息 管理员权限")
    @PostMapping("edit")
    @UpdateUserInfo
    public Result edit(HttpServletRequest request, @RequestBody CompanyInfoModel model) {
        UserModel user = (UserModel) request.getAttribute(Conf.SSO_USER);
        if (StringUtils.isBlank(model.getCompanyId())) {
            model.setCompanyId(user.getCompanyId());
        }
        try {
            companyService.modifyCompanyInfo(model);
        } catch (ParamException e) {
            return Result.builder(false).code(400).msg(e.getMessage()).build();
        }
        return Result.builder(true).msg("success").build();
    }

}
