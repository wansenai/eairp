package com.wansensoft.service.system.impl;

import com.alibaba.fastjson.JSONObject;
import com.wansensoft.entities.role.SysRoleMenuRel;
import com.wansensoft.entities.system.SysMenu;
import com.wansensoft.entities.user.SysUserRoleRel;
import com.wansensoft.mappers.role.SysRoleMenuRelMapper;
import com.wansensoft.service.system.ISysMenuService;
import com.wansensoft.mappers.system.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansensoft.service.user.ISysUserRoleRelService;
import com.wansensoft.service.user.ISysUserService;
import com.wansensoft.utils.response.Response;
import com.wansensoft.vo.MenuVo;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
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
    public Response<JSONObject> menuList() {
        var menuData = new JSONObject();
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
                // menuIds  deduplication and segmentation into numbers
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
                if(!menus.isEmpty()) {
                    menus.forEach(menu -> {
                        // JSON Assembly
                        var meta = getMetaJsonObject(menu);
                        var menuVo = MenuVo.builder()
                                .id(menu.getId())
                                .name(menu.getName())
                                .menuType(menu.getMenuType())
                                .path(menu.getPath())
                                .component(menu.getComponent())
                                .icon(menu.getIcon())
                                .sort(menu.getSort())
                                .redirect(menu.getRedirect())
                                .meta(meta)
                                .build();
                        if(menu.getParentId() != null) {
                            menuVo.setParentId(menu.getParentId());
                        }

                        menuVos.add(menuVo);
                    });
                }
                menuData.put("total", menuVos.size());
                menuData.put("data", menuVos);
            }
        }
        return Response.responseData(menuData);
    }

    /**
     *  menu Json Assembly
     * @param menu menu data object
     * @return meta data
     */
    private static JSONObject getMetaJsonObject(SysMenu menu) {
        var meta = new JSONObject();
        meta.put("title", menu.getTitle());
        meta.put("icon", menu.getIcon());
        meta.put("hideMenu", menu.getHideMenu());
        meta.put("hideBreadcrumb", menu.getHideBreadcrumb());
        meta.put("ignoreKeepAlive", menu.getIgnoreKeepAlive());
        meta.put("hideTab", menu.getHideTab());
        meta.put("carryParam", menu.getCarryParam());
        meta.put("hideChildrenInMenu", menu.getHideChildrenInMenu());
        meta.put("affix", menu.getAffix());
        meta.put("frameSrc", menu.getFrameSrc());
        meta.put("realPath", menu.getRealPath());
        // default value 20
        meta.put("dynamicLevel", 20);

        return meta;
    }
}
