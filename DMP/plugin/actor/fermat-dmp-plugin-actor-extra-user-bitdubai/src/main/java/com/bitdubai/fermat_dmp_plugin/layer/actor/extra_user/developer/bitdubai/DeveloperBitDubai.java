package com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.AddonDeveloper;
import com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.ExtraUserUserAddonRoot;

/**
 * Created by loui on 22/02/15.
 */
public class DeveloperBitDubai implements AddonDeveloper {

    Addon addon;





    @Override
    public Addon getAddon() {
        return addon;
    }





    public DeveloperBitDubai () {

        /**
         * I will choose from the different versions of my implementations which one to start. Now there is only one, so
         * it is easy to choose.
         */

        addon = new ExtraUserUserAddonRoot();

    }


}
