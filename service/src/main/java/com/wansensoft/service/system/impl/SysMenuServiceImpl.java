package com.wansensoft.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wansensoft.entities.role.SysRoleMenuRel;
import com.wansensoft.entities.user.SysUserRoleRel;
import com.wansensoft.mappers.role.SysRoleMenuRelMapper;
import com.wansensoft.service.role.ISysRoleMenuRelService;
import com.wansensoft.service.system.ISysMenuService;
import com.wansensoft.entities.system.SysMenu;
import com.wansensoft.mappers.system.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.service.user.ISysUserRoleRelService;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.MenuVo;
import nonapi.io.github.classgraph.utils.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能模块表 服务实现类
 * </p>
 *
 * @author James Zow
 * @since 2023-09-05
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ISysUserRoleRelService userRoleRelService;

    private final SysRoleMenuRelMapper roleMenuRelMapper;

    private final SysMenuMapper menuMapper;

    private final ISysUserService userService;

    public SysMenuServiceImpl(ISysUserRoleRelService userRoleRelService, SysRoleMenuRelMapper roleMenuRelMapper, SysMenuMapper menuMapper, ISysUserService userService) {
        this.userRoleRelService = userRoleRelService;
        this.roleMenuRelMapper = roleMenuRelMapper;
        this.menuMapper = menuMapper;
        this.userService = userService;
    }

    public String test(){
        try {
            File file = ResourceUtils.getFile("classpath:testMenu.json");
            String json = FileUtil.readAsString(file);
            return json;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Response<List<MenuVo>> menuList() {
        var menuVos = new ArrayList<MenuVo>();

        var userId = userService.getCurrentUserId();
        if (!StringUtils.hasText(userId)) {
            Response.fail();
        }
        var roleIds = userRoleRelService.queryByUserId(Long.parseLong(userId))
                .stream().map(SysUserRoleRel::getRoleId).toList();

        if (!roleIds.isEmpty()) {
            var menusReals = roleMenuRelMapper.listByRoleId(roleIds);
            if(!menusReals.isEmpty()) {
                var numberList = menusReals.stream().map(SysRoleMenuRel::getMenuId).toList();
                // menuIds 去重和分割成数字
                System.err.println(numberList);
                Pattern pattern = Pattern.compile("\\d+");
                List<String> distinctNumbers = numberList.stream()
                        .flatMap(item -> {
                            Matcher matcher = pattern.matcher(item);
                            List<String> numbers = new ArrayList<>();
                            while (matcher.find()) {
                                numbers.add(matcher.group());
                            }
                            return numbers.stream();
                        })
                        .distinct()
                        .collect(Collectors.toList());

                var menus = menuMapper.selectBatchIds(distinctNumbers);
                System.out.println(menus);
                if(!menus.isEmpty()) {
                    menus.forEach(menu -> {
                        MenuVo menuVo = MenuVo.builder()
                                .id(menu.getId())
                                .parentId(Integer.valueOf(menu.getParentNumber()))
                                .name(menu.getName())
                            //    .menuType(Integer.valueOf(menu.getType()))
                                .path(menu.getUrl())
                                .component(menu.getComponent())
                                .icon(menu.getIcon())
                                .sort(Integer.valueOf(menu.getSort()))
                                .build();

                        menuVos.add(menuVo);
                    });
                }
            }
        }

        return Response.responseData(menuVos);
    }
}
