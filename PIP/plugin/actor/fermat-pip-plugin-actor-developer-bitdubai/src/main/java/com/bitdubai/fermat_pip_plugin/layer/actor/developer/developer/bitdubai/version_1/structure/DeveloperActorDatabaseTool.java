package com.bitdubai.fermat_pip_plugin.layer.actor.developer.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.pip_actor.developer.DatabaseTool;
import com.bitdubai.fermat_pip_plugin.layer.actor.developer.developer.bitdubai.version_1.ActorDeveloperPluginRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ciencias on 6/25/15.
 */
public class DeveloperActorDatabaseTool implements DatabaseTool {

    private Map<Plugins,Plugin> databaseLstPlugins;
    private Map<Addons,Addon> databaseLstAddonds;


    public DeveloperActorDatabaseTool( Map<Plugins,Plugin> databaseLstPlugins,Map<Addons,Addon> databaseLstAddonds
    ){

        this.databaseLstAddonds=databaseLstAddonds;
        this.databaseLstPlugins=databaseLstPlugins;
    }

    @Override
    public List<Plugins> getAvailablePluginList() {
        List<Plugins> lstPlugins=new ArrayList<Plugins>();
        for(Map.Entry<Plugins, Plugin> entry : databaseLstPlugins.entrySet()) {
            Plugins key = entry.getKey();
            lstPlugins.add(key);
        }
        return lstPlugins;
    }

    @Override
    public List<Addons> getAvailableAddonList() {
        List<Addons> lstAddons=new ArrayList<Addons>();
        for(Map.Entry<Addons, Addon> entry : databaseLstAddonds.entrySet()) {
            Addons key = entry.getKey();
            lstAddons.add(key);
        }
        return lstAddons;
    }

    @Override
    public List<DeveloperDatabase> getDatabaseListFromPlugin(Plugins plugin) {
        DatabaseManagerForDevelopers databaseManagerForDevelopers = (DatabaseManagerForDevelopers) databaseLstPlugins.get(plugin);
        return databaseManagerForDevelopers.getDatabaseList(new DeveloperActorDatabaseObjectFactory());

    }

    @Override
    public List<DeveloperDatabase> getDatabaseListFromAddon(Addons addon) {
        DatabaseManagerForDevelopers databaseManagerForDevelopers = (DatabaseManagerForDevelopers) databaseLstAddonds.get(addon);
        return databaseManagerForDevelopers.getDatabaseList(new DeveloperActorDatabaseObjectFactory());
    }

    @Override
    public List<DeveloperDatabaseTable> getPluginTableListFromDatabase(Plugins plugin, DeveloperDatabase developerDatabase) {
        DatabaseManagerForDevelopers databaseManagerForDevelopers = (DatabaseManagerForDevelopers) databaseLstPlugins.get(plugin);
        return databaseManagerForDevelopers.getDatabaseTableList(new DeveloperActorDatabaseObjectFactory(), developerDatabase);
    }

    @Override
    public List<DeveloperDatabaseTable> getAddonTableListFromDatabase(Addons addon, DeveloperDatabase developerDatabase) {
        DatabaseManagerForDevelopers databaseManagerForDevelopers = (DatabaseManagerForDevelopers) databaseLstAddonds.get(addon);
        return databaseManagerForDevelopers.getDatabaseTableList(new DeveloperActorDatabaseObjectFactory(), developerDatabase);
    }


    @Override
    public List<DeveloperDatabaseTableRecord> getPluginTableContent(Plugins plugin, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        DatabaseManagerForDevelopers databaseManagerForDevelopers = (DatabaseManagerForDevelopers) databaseLstPlugins.get(plugin);
        return databaseManagerForDevelopers.getDatabaseTableContent(new DeveloperActorDatabaseObjectFactory(), developerDatabase, developerDatabaseTable);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getAddonTableContent(Addons addon, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        DatabaseManagerForDevelopers databaseManagerForDevelopers = (DatabaseManagerForDevelopers) databaseLstAddonds.get(addon);
        return databaseManagerForDevelopers.getDatabaseTableContent(new DeveloperActorDatabaseObjectFactory(), developerDatabase, developerDatabaseTable);
    }
}
