package com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.fiat_over_crypto_wallet.developer.bitdubai;

import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.PluginDeveloper;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.TimeFrequency;
import com.bitdubai.fermat_api.layer.pip_license.PluginLicensor;
import com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.fiat_over_crypto_wallet.developer.bitdubai.version_1.FiatOverCryptoWalletNicheWalletTypePluginRoot;

/**
 * Created by loui on 27/05/15.
 */
public class DeveloperBitDubai  implements PluginDeveloper, PluginLicensor {

    Plugin plugin;

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    public DeveloperBitDubai () {

        /**
         * I will choose from the different versions of my implementations which one to start. Now there is only one, so
         * it is easy to choose.
         */

        plugin = new FiatOverCryptoWalletNicheWalletTypePluginRoot();

    }

    @Override
    public int getAmountToPay() {
        return 100;
    }

    @Override
    public CryptoCurrency getCryptoCurrency() {
        return CryptoCurrency.BITCOIN;
    }

    @Override
    public String getAddress() {
        return "13gpMizSNvQCbJzAPyGCUnfUGqFD8ryzcv";
    }

    @Override
    public TimeFrequency getTimePeriod() {
        return TimeFrequency.MONTHLY;
    }
}
