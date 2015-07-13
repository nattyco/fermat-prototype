package com.bitdubai.fermat_dmp_plugin.layer.module.wallet_runtime.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.MenuItem;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.SideMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ciencias on 2/14/15.
 */
public class RuntimeSideMenu implements SideMenu {
    
    List<MenuItem> menuItems = new ArrayList<>();
    
    public void addMenuItem (MenuItem menuItem){
        menuItems.add(menuItem);
    }
    
    public List<MenuItem> getMenuItems () {
        return menuItems;
    }
    
}
