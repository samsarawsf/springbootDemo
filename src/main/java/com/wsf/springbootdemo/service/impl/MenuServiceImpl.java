package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.Menu;
import com.wsf.springbootdemo.service.MenuService;
import com.wsf.springbootdemo.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author weijin
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2022-05-20 22:18:35
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Autowired
    private MenuMapper menuMapper;


    /**
     * 循环连查  从一级列表往里查  时间复杂度为n^2
     * 优化了一下
     * @return
     */
    public List<Menu> getMenuTree(){
//        List<Menu> menus = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", 0));
//        for (Menu menu:menus
//             ) {
//            menu.setMenus(menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id",menu.getId())));
//            List<Menu> menus1 = menu.getMenus();
//            for (Menu menu1:menus1
//                 ) {
//                menu1.setMenus(menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id",menu1.getId())));
//            }
//        }
        List<Menu> finalMenus = new ArrayList<>();
        List<Menu> menus = menuMapper.selectList(null);
        for (Menu menu: menus
             ) {
            //先寻找各自的孩子
            for (Menu e:menus
                 ) {
                if(e.getParentId().equals(menu.getId())){
                    menu.getMenus().add(e);
                }
            }

            //提取出父节点
            if(menu.getParentId().equals(0L)){
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }
}




