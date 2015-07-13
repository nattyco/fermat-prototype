package com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.DealsWithBitcoinWallet;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.TransactionManager;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.CantSendFundsException;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.InsufficientFundsException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.exceptions.CantInitializeDaoException;

import java.util.UUID;

/**
 * Created by eze on 2015.06.23..
 */
public class OutgoingExtraUserTransactionManager implements DealsWithBitcoinWallet, DealsWithCryptoVault, DealsWithErrors, DealsWithPluginDatabaseSystem, DealsWithPluginIdentity, TransactionManager {

    /*
     * DealsWithBitcoinWallet Interface member Variables
     */
    private BitcoinWalletManager bitcoinWalletManager;

    /*
     * DealsWithCryptoVault Interface member Variables
     */
    private CryptoVaultManager cryptoVaultManager;

    /*
     * DealsWithErrors Interface member Variables
     */
    private ErrorManager errorManager;

    /*
     * DealsWithPluginDatabaseSystem Interface member Variables
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /*
     * DealsWithPluginIdentity Interface methods implementation
     */
     private  UUID pluginId;


    /*
     * DealsWithBitcoinWallet Interface methods implementation
     */
    @Override
    public void setBitcoinWalletManager(BitcoinWalletManager bitcoinWalletManager) {
        this.bitcoinWalletManager = bitcoinWalletManager;
    }

    /*
     * DealsWithCryptoVault Interface methods implementation
     */
    @Override
    public void setCryptoVaultManager(CryptoVaultManager cryptoVaultManager) {
        this.cryptoVaultManager = cryptoVaultManager;
    }

    /*
     * DealsWithErrors Interface methods implementation
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /*
    * DealsWithPluginDatabaseSystem Interface methods implementation
    */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /*
    * DealsWithPluginIdentity Interface methods implementation
    */
    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    /*
     * TransactionManager Interface methods implementation
     */

    @Override
    public void send(UUID walletID, CryptoAddress destinationAddress, long cryptoAmount) throws InsufficientFundsException, CantSendFundsException {
        OutgoingExtraUserDao dao = new OutgoingExtraUserDao();
        dao.setErrorManager(this.errorManager);
        dao.setPluginDatabaseSystem(this.pluginDatabaseSystem);
        try {
            dao.initialize(this.pluginId);
        } catch (CantInitializeDaoException e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
            throw new CantSendFundsException("I coundn't initialize dao",e,"Plug-in id: "+this.pluginId.toString(),"");
        }

        /* Wa
         * TODO: Create a class fir tge selection of the correct wallet
         *       We will have as parameter the walletId and walletType
         *       The class will have a reference to all the basicwallet managers
         *       implemented that could be a destination of the transactions managed
         *       by an extra user.
         */

        BitcoinWalletWallet bitcoinWalletWallet;
        try {
            bitcoinWalletWallet = this.bitcoinWalletManager.loadWallet(walletID);
        } catch (CantLoadWalletException e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
            throw new CantSendFundsException("I couldn't load the wallet",e,"WalletId: "+ walletID.toString(),"");
        }

        long funds;
        try {
            //TODO: revisar por el cambio en la interface

            funds = bitcoinWalletWallet.getAvailableBalance().getBalance();
        } catch (CantCalculateBalanceException e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantSendFundsException("I couldn't calculate balance",e,"","");
        }

        if(cryptoAmount > funds) {
            throw new InsufficientFundsException("", null, "CryptoAmount: " + cryptoAmount + "\nBalance: " + funds, "Many transactions were accepted before discounting from basic wallet balanace");
        }

        try {
            dao.registerNewTransaction(walletID, destinationAddress, cryptoAmount);
        } catch (CantInsertRecordException e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantSendFundsException("I couldn't insert new record",e,"","");
        }
    }
}
