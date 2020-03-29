package com.rrpserivce.demo.service;


import com.rrpserivce.demo.entity.DynamicMenu;
import com.rrpserivce.demo.entity.Menu;
import com.rrpserivce.demo.repository.DynamicMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class DynamicMenuService {
    @Autowired
    private DynamicMenuRepository dynamicMenuRepository;
    @Autowired
    private MenuService menuService;

    /**
     * 你品，细品
     * @param user_id
     * @return
     */
    public Set<DynamicMenu> getDynamicMenu(Integer user_id){
        Set<Menu> initialList =  dynamicMenuRepository.getDynamicMenu(user_id);
        Set<DynamicMenu> finalList = new TreeSet<>();
        for (Menu initialListMenu: initialList){
            String parentMenu = initialListMenu.getMenu();
            if (parentMenu != null)
                finalList.add(new DynamicMenu(
                        menuService.findById(Integer.parseInt(parentMenu)),
                        dynamicMenuRepository.getDynamicMenuByMenu_id(user_id, Integer.parseInt(parentMenu))));
        }
        // 该加不进去的就加不进去，
        for (Menu initialListMenu: initialList) {
            String parentMenu = initialListMenu.getMenu();
            // 避免无谓的硬添
            if (parentMenu == null)
                finalList.add(new DynamicMenu(menuService.findById(initialListMenu.getId()), null));
        }
        return finalList;
    }

    public Set<DynamicMenu> getDynamicMenuByUsername(String username){
        Set<Menu> initialList =  dynamicMenuRepository.getDynamicMenuByUsername(username);
        Set<DynamicMenu> finalList = new TreeSet<>();
        for (Menu initialListMenu: initialList){
            String parentMenu = initialListMenu.getMenu();
            if (parentMenu != null)
                finalList.add(new DynamicMenu(
                        menuService.findById(Integer.parseInt(parentMenu)),
                        dynamicMenuRepository.getDynamicMenuByMenu_id(dynamicMenuRepository.getIdByUsername(username), Integer.parseInt(parentMenu))));
        }
        // 该加不进去的就加不进去，
        for (Menu initialListMenu: initialList) {
            String parentMenu = initialListMenu.getMenu();
            // 避免无谓的硬添
            if (parentMenu == null)
                finalList.add(new DynamicMenu(menuService.findById(initialListMenu.getId()), null));
        }
        return finalList;
    }
    public Set<Menu> getDynamicMenuByMenu_id(Integer user_id, Integer menu_id) {
        return dynamicMenuRepository.getDynamicMenuByMenu_id(user_id, menu_id);
    }

    /*[
    {
      "menu": {
        "id": 1,
        "title": "互联网",
        "url": "/internet",
        "menu": null
      },
      "menus": null
    },
    {
      "menu": {
        "id": 2,
        "title": "生产数据",
        "url": "/production_data",
        "menu": null
      },
      "menus": [
        {
          "id": 9,
          "title": "模台个数",
          "url": "/desk_number",
          "menu": {
            "id": 2,
            "title": "生产数据",
            "url": "/production_data",
            "menu": null
          }
        },
        {
          "id": 10,
          "title": "模台利用率",
          "url": "/desk_usage",
          "menu": {
            "id": 2,
            "title": "生产数据",
            "url": "/production_data",
            "menu": null
          }
        }
      ]
    },
    {
      "menu": {
        "id": 3,
        "title": "生产管理",
        "url": "/production_management",
        "menu": null
      },
      "menus": null
    },
    {
      "menu": {
        "id": 4,
        "title": "运行情况",
        "url": "/running_conditions",
        "menu": null
      },
      "menus": [
        {
          "id": 15,
          "title": "设备运行状态",
          "url": "/1",
          "menu": {
            "id": 4,
            "title": "运行情况",
            "url": "/running_conditions",
            "menu": null
          }
        },
        {
          "id": 16,
          "title": "运行数据统计",
          "url": "/2",
          "menu": {
            "id": 4,
            "title": "运行情况",
            "url": "/running_conditions",
            "menu": null
          }
        }
      ]
    },
    {
      "menu": {
        "id": 5,
        "title": "维修维护",
        "url": "/maintenance",
        "menu": null
      },
      "menus": null
    }
  ]
  */
}