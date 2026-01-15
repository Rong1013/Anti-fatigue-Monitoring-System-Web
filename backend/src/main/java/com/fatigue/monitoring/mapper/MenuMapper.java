package com.fatigue.monitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fatigue.monitoring.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper接口
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    List<Menu> selectAllMenus();
}
