package com.bitdubai.fermat_core.layer.dmp_world.coinapult;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.layer.dmp_module.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer.dmp_world.WorldSubsystem;
//import com.bitdubai.fermat_dmp_plugin.layer.world.coinapult.developer.bitdubai.DeveloperBitDubai;

/**
 * Created by loui on 12/03/15.
 */
public class CoinapultWorldSubsystem implements WorldSubsystem {

    Plugin plugin;

    @Override
    public Plugin getPlugin(){
        return plugin;
    }

    @Override
    public void start() throws CantStartSubsystemException {
        /**
         * I will choose from the different Developers available which implementation to use. Right now there is only
         * one, so it is not difficult to choose.
         */
/*
        try {
            DeveloperBitDubai developerBitDubai = new DeveloperBitDubai();
            plugin = developerBitDubai.getPlugin();
        }
        catch (Exception e)
        {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException();
        }
*/
    }
}
