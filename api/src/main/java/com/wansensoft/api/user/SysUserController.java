package com.wansensoft.api.user;


import com.wansensoft.dto.login.AccountLoginDto;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.utils.constants.ApiVersionConstants;
import com.wansensoft.vo.UserInfoVo;
import com.wansensoft.vo.UserRoleVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    private final ISysUserService userService;

    public SysUserController(ISysUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = ApiVersionConstants.API_VERSION_V2 + "/login")
    public Response<UserInfoVo> accountLogin(@RequestBody AccountLoginDto accountLoginDto) {
        return userService.accountLogin(accountLoginDto);
    }

    @GetMapping(value = "info")
    public Response<UserInfoVo> info() {
        return userService.userInfo();
    }

    @GetMapping(value = "perm")
    public Response<List<UserRoleVo>> permission() {
        return userService.userRole();
    }
}
