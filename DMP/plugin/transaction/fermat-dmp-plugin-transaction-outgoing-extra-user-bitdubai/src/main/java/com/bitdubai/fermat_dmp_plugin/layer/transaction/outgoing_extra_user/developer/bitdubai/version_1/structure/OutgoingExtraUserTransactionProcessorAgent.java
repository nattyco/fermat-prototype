package com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantRegisterDebitDebitException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.DealsWithBitcoinWallet;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.InconsistentFundsException;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.InsufficientFundsException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.CouldNotSendMoneyException;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.InsufficientMoneyException;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.InvalidSendToAddressException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.exceptions.InconsistentTableStateException;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.interfaces.TransactionAgent;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.outgoing_extra_user.developer.bitdubai.version_1.util.TransactionWrapper;

import java.util.List;
import java.util.UUID;

/**
 * Created by eze on 2015.06.25..
 */
public class OutgoingExtraUserTransactionProcessorAgent implements DealsWithBitcoinWallet, DealsWithCryptoVault, DealsWithErrors, TransactionAgent {


    /**
     * DealsWithBitcoinWallet Interface member variables.
     */
    private BitcoinWalletManager bitcoinWalletManager;

    /**
     * DealsWithCryptoVault Interface member variables.
     */
    private CryptoVaultManager cryptoVaultManager;

    /**
     * DealsWithEvents Interface member variables.
     */
    private ErrorManager errorManager;


    /**
     * TransactionAgent Member Variables.
     */
    private Thread agentThread;
    private TransactionProcessorAgent transactionProcessorAgent;
    private OutgoingExtraUserDao dao;



    /**
     *DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     *  DealsWithBitcoinWallet Interface implementation.
     */
    @Override
    public void setBitcoinWalletManager(BitcoinWalletManager bitcoinWalletManager) {
        this.bitcoinWalletManager = bitcoinWalletManager;
    }

    /**
     *  DealsWithCryptoVault Interface implementation.
     */
    @Override
    public void setCryptoVaultManager(CryptoVaultManager cryptoVaultManager) {
        this.cryptoVaultManager = cryptoVaultManager;
    }

    /**
     * TransactionAgent Interface implementation.
     */
    @Override
    public void start() {

        this.transactionProcessorAgent = new TransactionProcessorAgent();
        this.transactionProcessorAgent.setErrorManager(this.errorManager);
        this.transactionProcessorAgent.initialize(this.dao,this.bitcoinWalletManager,this.cryptoVaultManager);
        this.agentThread = new Thread(this.transactionProcessorAgent);
        this.agentThread.start();

    }

    @Override
    public void stop() {

        this.agentThread.interrupt();

    }


    public void setOutgoingExtraUserDao(OutgoingExtraUserDao dao){
        this.dao = dao;
    }













    private static class TransactionProcessorAgent implements DealsWithErrors, Runnable  {

        private ErrorManager errorManager;
        private BitcoinWalletManager bitcoinWalletManager;
        private CryptoVaultManager cryptoVaultManager;


        private static final int SLEEP_TIME = 5000;
        private OutgoingExtraUserDao dao;

        /**
         * MonitorAgent interface implementation.
         */
        private void initialize (OutgoingExtraUserDao dao, BitcoinWalletManager bitcoinWalletManager, CryptoVaultManager cryptoVaultManager) {
            this.dao = dao;
            this.bitcoinWalletManager = bitcoinWalletManager;
            this.cryptoVaultManager = cryptoVaultManager;
        }

        /**

         /**
         * Runnable Interface implementation.
         */
        @Override
        public void run() {

            /**
             * Infinite loop.
             */
            while (true) {

                /**
                 * Sleep for a while.
                 */
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    cleanResources();
                    return;
                }

                /**
                 * Now I do the main task.
                 */
                doTheMainTask();

                /**
                 * Check if I have been Interrupted.
                 */
                if (Thread.currentThread().isInterrupted()) {
                    cleanResources();
                    return;
                }
            }
        }

        private void doTheMainTask() {


            BitcoinWalletWallet bitcoinWalletWallet = null;

            UUID temporalId = UUID.fromString("25428311-deb3-4064-93b2-69093e859871");


            try {
                bitcoinWalletWallet = this.bitcoinWalletManager.loadWallet(temporalId);
            } catch (CantLoadWalletException e) {
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                return;
            }

            List<TransactionWrapper> transactionList;

            // We first check for the new transactions registered
            try {
                transactionList = dao.getNewTransactions();
            } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,cantLoadTableToMemory);
                return;
            } catch (InvalidParameterException e) {
                this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                return;
            }

            /* For each transaction:
             1. We check that we can apply it
             2. We apply it in the bitcoin network
            */

            long funds;
            for(TransactionWrapper transaction : transactionList) {
                try {
                    funds = bitcoinWalletWallet.getAvailableBalance().getBalance();
                    if (funds < transaction.getAmount()) {
                        FermatException insufficientFundsException = new InsufficientFundsException("I don't have funds for this transaction",null,"Balance: "+ funds + "\ncryptoAmount: "+transaction.getAmount(),"");
                        this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,insufficientFundsException );
                        return;
                    }
                } catch (CantCalculateBalanceException e) {
                    this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                }

                // Now we apply it in the vault
                try {
                    this.cryptoVaultManager.sendBitcoins(transaction.getWalletId(), transaction.getTransactionId(), transaction.getAddressTo(), transaction.getAmount());
                    dao.setToSTCV(transaction);
                } catch (InsufficientMoneyException e) {
                    /*
                     * TODO: THEN RAISE EVENT TO INFORM THE SITUATION
                     */
                    try {
                        dao.cancelTransaction(transaction);
                    } catch (CantUpdateRecordException cantUpdateRecord) {
                        errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantUpdateRecord);
                        return;
                    } catch (InconsistentTableStateException e1) {
                        errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                        return;
                    } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
                        errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
                        return;
                    }
                    // And we filally report the error
                    Exception inconsistentFundsException = new InconsistentFundsException("Basic wallet balance and crypto vault funds are inconsistent",e,"","");
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, inconsistentFundsException);
                    return;

                } catch (InvalidSendToAddressException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                } catch (CouldNotSendMoneyException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
                    return;
                } catch (CantUpdateRecordException cantUpdateRecord) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantUpdateRecord);
                    return;
                } catch (InconsistentTableStateException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                }
            }

            /*
             * Now we proceed to apply the transactions sent to the bitcoin network to the wallet balance
             */

            try {
                transactionList = dao.getSentToCryptoVaultTransactions();
            } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantLoadTableToMemory);
                return;
            } catch (InvalidParameterException e) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                return;
            }

            for(TransactionWrapper transaction : transactionList){
                try {
                    //TODO: revisar por el cambio en la interface
                        bitcoinWalletWallet.getAvailableBalance().debit(transaction);

                    dao.setToPIW(transaction);
                } catch (CantCalculateBalanceException |CantRegisterDebitDebitException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
                    return;
                } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
                    cantLoadTableToMemory.printStackTrace();
                } catch (CantUpdateRecordException cantUpdateRecord) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantUpdateRecord);
                    return;
                } catch (InconsistentTableStateException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_OUTGOING_EXTRA_USER_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                    return;
                }
            }
        }


        private void cleanResources() {

            /**
             * Disconnect from database and explicitly set all references to null.
             */

        }

        /*
         * DealsWithErrors Interface method implementation
         */
        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }
    }

}
