package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.entity.Menu;
import com.fatigue.monitoring.mapper.MenuMapper;
import com.fatigue.monitoring.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> selectMenusByUserId(Long userId) {
        return baseMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<String> selectPermissionsByUserId(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public List<Menu> selectAllMenus() {
        return baseMapper.selectAllMenus();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMenu(Menu menu) {
        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(Menu menu) {
        Menu existMenu = this.getById(menu.getId());
        if (existMenu == null) {
            throw new BusinessException("菜单不存在");
        }

        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Long menuId) {
        Menu menu = this.getById(menuId);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 检查是否有子菜单
        long childCount = this.lambdaQuery()
                .eq(Menu::getParentId, menuId)
                .count();
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }

        return this.removeById(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long menuId, Integer status) {
        Menu menu = this.getById(menuId);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        menu.setStatus(status);
        return this.updateById(menu);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> tree = new ArrayList<>();
        for (Menu menu : menus) {
            // 查找顶级菜单（父ID为0）
            if (menu.getParentId() == 0) {
                tree.add(findChildren(menu, menus));
            }
        }
        return tree;
    }

    /**
     * 递归查找子菜单
     *
     * @param menu 当前菜单
     * @param menus 所有菜单
     * @return 包含子菜单的菜单
     */
    private Menu findChildren(Menu menu, List<Menu> menus) {
        List<Menu> children = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> findChildren(m, menus))
                .collect(Collectors.toList());
        menu.setChildren(children);
        return menu;
    }
}
