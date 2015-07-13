package com.bitdubai.fermat_dmp_plugin.layer.middleware.app_runtime.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.*;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.enums.Activities;
import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.enums.Fragments;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ciencias on 2/14/15.
 */
public class RuntimeActivity implements Activity {

    Activities type;
    Map<Fragments, Fragment> fragments = new HashMap<Fragments, Fragment>();
    TitleBar titleBar;
    SideMenu sideMenu;
    MainMenu mainMenu;
    TabStrip tabStrip;
    String color;
    String statusBarColor;

    /**
     * RuntimeActivity interface implementation.
     */

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor()  {
        return this.color;
    }

    public void setType(Activities type) {
        this.type = type;
    }

    public void addFragment (Fragment fragment){
        fragments.put(fragment.getType(), fragment);
    }

    public void setTitleBar(TitleBar titleBar) {
        this.titleBar = titleBar;
    }

    public void setSideMenu(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void setTabStrip(TabStrip tabStrip) {
        this.tabStrip = tabStrip;
    }

    /**
     * SubApp interface implementation.
     */

    @Override
    public Activities getType() {
        return type;
    }

    @Override
    public Map<Fragments, Fragment> getFragments() {
        return fragments;
    }

    @Override
    public TitleBar getTitleBar() {
        return titleBar;
    }

    @Override
    public SideMenu getSideMenu() {
        return sideMenu;
    }

    @Override
    public MainMenu getMainMenu() {
        return mainMenu;
    }

    @Override
    public TabStrip getTabStrip() {
        return tabStrip;
    }

    @Override
    public String getStatusBarColor() {
        return statusBarColor;
    }
}
