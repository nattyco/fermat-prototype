package com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Action;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.ProtocolStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionProtocolManager;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantConfirmTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantStartAgentException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.DealsWithEvents;
import com.bitdubai.fermat_api.layer.pip_platform_service.event_manager.EventManager;
import com.bitdubai.fermat_cry_api.layer.crypto_network.bitcoin.BitcoinCryptoNetworkManager;
import com.bitdubai.fermat_cry_api.layer.crypto_network.bitcoin.BitcoinManager;
import com.bitdubai.fermat_cry_api.layer.crypto_network.bitcoin.DealsWithBitcoinCryptoNetwork;
import com.bitdubai.fermat_cry_api.layer.crypto_network.bitcoin.exceptions.CantConnectToBitcoinNetwork;
import com.bitdubai.fermat_cry_api.layer.crypto_network.bitcoin.exceptions.CantCreateCryptoWalletException;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVault;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.CouldNotSendMoneyException;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.InvalidSendToAddressException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.CantCalculateTransactionConfidenceException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_cry_plugin.layer.crypto_vault.developer.bitdubai.version_1.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.google.common.util.concurrent.ListenableFuture;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.store.UnreadableWalletException;
import org.bitcoinj.wallet.DeterministicSeed;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by rodrigo on 09/06/15.
 */
public class BitcoinCryptoVault implements BitcoinManager, CryptoVault, DealsWithBitcoinCryptoNetwork, DealsWithEvents,DealsWithErrors, DealsWithPluginIdentity, DealsWithPluginDatabaseSystem, DealsWithPluginFileSystem, TransactionProtocolManager{

    /**
     * BitcoinCryptoVault member variables
     */
    NetworkParameters networkParameters;
    File vaultFile;
    String vaultFileName;
    VaultEventListeners vaultEventListeners;


    /**
     * CryptoVault interface member variable
     */
    UUID userId;
    Wallet vault;

    /**
     * DealsWithCryptonetwork interface member variable
     */
    BitcoinCryptoNetworkManager bitcoinCryptoNetworkManager;


    /**
     * DealsWithEvents interface member variable
     */
    EventManager eventManager;

    /**
     * DealsWithErros interface member variable
     */
    ErrorManager errorManager;

    /**
     * DealsWithPluginIdentity interface member variable
     */
    UUID pluginId;


    /**
     * DealsWithPluginDatabaseSystem interface member variable
     */
    PluginDatabaseSystem pluginDatabaseSystem;
    Database database;

    /**
     * DealsWithPlugInFileSystem interface member variable
     */
    PluginFileSystem pluginFileSystem;


    /**
     * CryptoVault interface implementations
     * @param UserId
     */
    @Override
    public void setUserId(UUID UserId) {
        this.userId = UserId;
    }

    /**
     * CryptoVault interface implementations
     * @return
     */
    @Override
    public UUID getUserId() {
        return this.userId;
    }

    /**
     * CryptoVault interface implementations
     * @return
     */
    @Override
    public Object getWallet() {
        return vault;
    }

    /**
     * DealsWithBitcoinCryptoNetwork interface implementation
     * @param bitcoinCryptoNetworkManager
     */
    @Override
    public void setBitcoinCryptoNetworkManager(BitcoinCryptoNetworkManager bitcoinCryptoNetworkManager) {
        this.bitcoinCryptoNetworkManager = bitcoinCryptoNetworkManager;
    }

    /**
     * DealsWithEvents interface implementation
     * @param eventManager
     */
    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * DealsWithPluginFileSystem interface implementation
     * @param pluginFileSystem
     */
    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }

    /**
     * DealsWithPluginIdentity interface implementation
     * @param pluginId
     */
    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * DealsWithError interface implementation
     * @param errorManager
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithPluginDatabaseSystem interface implementation
     * @param pluginDatabaseSystem
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    public void setDatabase(Database database){
        this.database = database;
    }


    /**
     * Constructor
     * @param UserId the Id of the user of the platform.
     */
    public BitcoinCryptoVault (UUID UserId) throws CantCreateCryptoWalletException {
        this.userId = UserId;
        this.networkParameters = BitcoinNetworkConfiguration.getNetworkConfiguration();

        this.vaultFileName = userId.toString() + ".vault";
        //todo this needs to be fixed. I need to find a better way to get the file
        this.vaultFile = new File("/data/data/com.bitdubai.fermat/files", vaultFileName);
    }

    public  void loadOrCreateVault() throws CantCreateCryptoWalletException {
        if (vaultFile.exists())
            loadExistingVaultFromFile();
        else
            createNewVault();

        configureVault();
    }

    /**
     * creates a new vault.
     * @throws CantCreateCryptoWalletException
     */
    private void createNewVault() throws CantCreateCryptoWalletException {
        vault = new Wallet(networkParameters);
        try {
            PluginTextFile vaultFile = pluginFileSystem.createTextFile(pluginId, userId.toString(), vaultFileName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
            vaultFile.persistToMedia();
            System.out.println("Vault created into file " + vaultFileName);
            /**
             * If I couldn't create it I can't go on
             */
        } catch (CantCreateFileException cantCreateFileException) {
            throw new CantCreateCryptoWalletException("There was an error trying to create a new Vault." ,cantCreateFileException, "Vault filename: " + vaultFileName, "Not enought space on disk?");
        } catch (CantPersistFileException e) {
            throw new CantCreateCryptoWalletException("There was an error trying to save the Vault into a file." ,e, "Vault filename: " + vaultFileName, "Not enought space on disk?");
        }
    }

    /**
     * Loads an existing Vault from file
     * @throws CantCreateCryptoWalletException
     */
    private void loadExistingVaultFromFile() throws CantCreateCryptoWalletException {
        try {
            vault = Wallet.loadFromFile(vaultFile);
            System.out.println("Vault loaded from file " + vaultFile.getAbsoluteFile().toString());
            System.out.println("CryptoVault current balance: " + vault.getBalance().getValue());
            System.out.println("CryptoVault estimated current balance: " + vault.getBalance(Wallet.BalanceType.ESTIMATED).value);

            /**
             * If I couldn't load it I can't go on.
             */
        } catch (UnreadableWalletException unreadableWalletException) {
            throw new CantCreateCryptoWalletException("Vault file not accesible.", unreadableWalletException, "Vault filename: " + vaultFileName, "Corrupted file.");
        }

    }

    /**
     * Will load the Vault from a provided seed. The user will need to provide the mNemonic code,
     * which is a series of prefedined words and the creation time. The vault will need to provide the methods
     * to retrieve this information at soma point.
     * @param mNemonicCode
     * @param CreationTimeInSeconds
     */
    public void loadExistingVaultFromSeed(String mNemonicCode, long CreationTimeInSeconds) throws CantCreateCryptoWalletException {
        try {
            DeterministicSeed seed = new DeterministicSeed(mNemonicCode, null, null, CreationTimeInSeconds);
            vault = Wallet.fromSeed(networkParameters, seed);
            configureVault();
        } catch (UnreadableWalletException e) {
            /**
             * I cannot load the existing vault from the provide seed. I cannot handle this
             */
            throw new CantCreateCryptoWalletException("Error trying to load the Vault from a seed.", e, null, "Unreadeble seed.");
        }

    }

    /**
     * I'm connecting the vault to the bitcoin Agent.
     * @throws CantStartAgentException
     */
    public void connectVault() throws CantConnectToBitcoinNetwork {

        bitcoinCryptoNetworkManager.setVault(this);
        bitcoinCryptoNetworkManager.connectToBitcoinNetwork();
    }

    public void disconnectVault(){ //todo raise correct exception
        bitcoinCryptoNetworkManager.setVault(this);
        bitcoinCryptoNetworkManager.disconnectFromBitcoinNetwork();
    }

    /**
     * configures internal vault parameters and creates the database that will hold
     * the transactions status.
     * @throws CantCreateCryptoWalletException
     */
    private void configureVault() throws CantCreateCryptoWalletException {
        vault.autosaveToFile(vaultFile, 0, TimeUnit.NANOSECONDS, null);
        vaultEventListeners = new VaultEventListeners(database, errorManager, eventManager);
        vault.addEventListener(vaultEventListeners);

    }


    /**
     * returns a valid CryptoAddrres from this vault
     * @return
     */
    public CryptoAddress getAddress(){
        CryptoAddress address = new CryptoAddress();
        address.setCryptoCurrency(CryptoCurrency.BITCOIN);
        address.setAddress(vault.freshReceiveAddress().toString());
        return address;
    }


    /**
     * Sends bitcoins to the specified address
     * @param FermatTxId the internal txID set for the transfer protocol
     * @param addressTo the address to
     * @param amount the amount of satoshis
     * @return the transaction hash created to send bitcoins.
     * @throws com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.InsufficientMoneyException
     */

    public String sendBitcoins(UUID FermatTxId, CryptoAddress addressTo, long amount) throws com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.InsufficientMoneyException, InvalidSendToAddressException, CouldNotSendMoneyException {
        /**
         * if the transaction was requested before but resend my mistake, Im not going to send it again
         */
        CryptoVaultDatabaseActions db = new CryptoVaultDatabaseActions(database, errorManager, eventManager);
        db.setVault(vault);

        try {
            if (!db.isNewFermatTransaction(FermatTxId))
            /**
             * Already sent, this might be an error. I'm not going to send it again.
             */
                throw new CouldNotSendMoneyException("This transaction has already been sent before.", null, "Transaction ID: " + FermatTxId.toString(), "An error in a previous module.");


        } catch (CantExecuteQueryException e) {
            throw new CouldNotSendMoneyException("I coudln't persist the internal transaction Id.", e, "Transaction ID: " + FermatTxId.toString(), "An error in the Database plugin..");
        }


        Address address = null;
        /**
         * I generate the address in the BitcoinJ format
         */
        try {
            address = new Address(this.networkParameters, addressTo.getAddress());
        } catch (AddressFormatException e) {
            /**
             * If the address is incorrectly formated, then I will throw the exception so that other plug ins can handle it.
             */
            throw new InvalidSendToAddressException("The SendTo address is not correct." , e, "Address: " + addressTo.getAddress(), "Incorrect generation by scanner or user entry");
        }

        /**
         * If I dont have enought money, I will raise the exception
         */
        Wallet.SendRequest request = Wallet.SendRequest.to(address, Coin.valueOf(amount));

        try {
            vault.completeTx(request);
        } catch (InsufficientMoneyException e) {
            /**
             * this shouldn't happen because the money is checked by previois modules, but if it does I will throw it so that the user can handle this.
             */
            throw new com.bitdubai.fermat_cry_api.layer.crypto_vault.exceptions.InsufficientMoneyException("Not enought money in Vault to complete the transaction", e, "AddressTo:" + addressTo.getAddress() + ", Satoshis: " + amount, "Transaction confidence level too low to spend money. Wait for at least another block generation." );
        }


        /**
         * I commit the transaction locally and save the vault
         */
        String txHash = null;
        Transaction tx = request.tx;
        txHash = tx.getHashAsString();
        try {
            db.persistNewTransaction(FermatTxId.toString(), txHash);
            /**
             * new Transaction, I will persist it as a Fermat transaction.
             */
            db.persistnewFermatTransaction(FermatTxId.toString());

            vault.commitTx(request.tx);
        } catch (CantExecuteQueryException e) {
            e.printStackTrace();
        }

        PeerGroup peers = (PeerGroup) bitcoinCryptoNetworkManager.getBroadcasters();

        /**
         * I broadcast and wait for the confirmation of the network
         */
        ListenableFuture<Transaction> future = peers.broadcastTransaction(request.tx);

        try {

            future.get();
            /**
             * the transaction was broadcasted and accepted by the nwetwork
             * I will persist it to inform it when the confidence level changes
             */


            /**
             * at this point the transaction is already created and in the network, if there are erros in any other plug in, I can't roll back anything.
             * I will deal with any DB error later when I control the transactions.
             */

        } catch (InterruptedException e) {
            /**
             * If I have an error sending the money, I will raise it.
             */
            throw new CouldNotSendMoneyException("An error occured waiting for confirmation from the Bitcoin network.", e, null, "No peers connected at this time.");
        } catch (ExecutionException e) {
            throw new CouldNotSendMoneyException("Unknown error trying to send money", e, null, null);
        }


        /**
         * returns the created transaction id
         */
        System.out.println("CryptoVault information: bitcoin sent!!!");
        return txHash;
    }

    /**
     * Validates if the address sent is valid in the current network or not.
     * @param addressTo
     * @return
     */
    public boolean isValidAddress(CryptoAddress addressTo){
        try {
            Address address = new Address(networkParameters, addressTo.getAddress());
            return true;
        } catch (AddressFormatException e) {
            return false;
        }
    }


    /**
     * TransactionProtocolManager interface implementation
     */
    @Override
    public void confirmReception(UUID transactionID) throws CantConfirmTransactionException {
        /**
         * will marked the transaction as notified
         */
        try{
            CryptoVaultDatabaseActions db = new CryptoVaultDatabaseActions(database, errorManager, eventManager);
            db.setVault(vault);
            db.updateTransactionProtocolStatus(transactionID, ProtocolStatus.RECEPTION_NOTIFIED);
        } catch (Exception e){
            throw new CantConfirmTransactionException("There was an error trying to confirm reception of a transaction", e, "TransactionId: " + transactionID.toString(), "Database plugin error.");
        }
    }

    @Override
    public List<com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction> getPendingTransactions(Specialist specialist) throws CantDeliverPendingTransactionsException {
        /**
         * will return all the pending transactions
         */
        try{
            List<com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction> txs = new ArrayList<com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction>();
            CryptoVaultDatabaseActions db = new CryptoVaultDatabaseActions(database, errorManager, eventManager);
            db.setVault(vault);
            /**
             * Im getting the transaction headers which is a map with transactionID and Transaction Hash. I will use this information to access the vault.
             */
            HashMap<String, String> transactionHeaders = db.getPendingTransactionsHeaders();
            for (Map.Entry<String, String> entry : transactionHeaders.entrySet()){
                String txId = entry.getKey();
                String txHash = entry.getValue();

                /**
                 * I get the transaction from the vault
                 */
                String[] addresses = getAddressFromTransaction(txHash);
                CryptoAddress addressFrom = new CryptoAddress(addresses[0], CryptoCurrency.BITCOIN);
                CryptoAddress addressTo = new CryptoAddress(addresses[1], CryptoCurrency.BITCOIN);
                long amount = getAmountFromVault(txHash);


                /**
                 * Will calculate the correct Confidence of the transaction
                 */
                TransactionConfidenceCalculator transactionConfidenceCalculator = new TransactionConfidenceCalculator(txId, database, vault);
                CryptoStatus cryptoStatus;
                try{
                    cryptoStatus = transactionConfidenceCalculator.getCryptoStatus();
                } catch (CantCalculateTransactionConfidenceException cantCalculateTransactionConfidenceException){
                    cryptoStatus = CryptoStatus.ON_CRYPTO_NETWORK;
                }

                CryptoTransaction cryptoTransaction = new CryptoTransaction(txHash, addressFrom, addressTo,CryptoCurrency.BITCOIN, amount, cryptoStatus);

                com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction tx = new com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction(UUID.fromString(txId),cryptoTransaction, Action.APPLY, getTransactionTimestampFromVault(txHash));
                txs.add(tx);

                /**
                 * I update the status of the transaction protocol to SENDING_NOTIFIED
                 */
                db.updateTransactionProtocolStatus(UUID.fromString(txId), ProtocolStatus.SENDING_NOTIFIED);
            }


            /**
             * once the database is updated, I return the transaction
             */

            return txs;
        } catch (Exception e){
            throw new CantDeliverPendingTransactionsException("I couldn't deliver pending transactions",e,null,null);
        }
    }

    /**
     * gets the amount of satoshis of the transaction
     * @param txHash
     * @return satoshis
     */
    private long getAmountFromVault(String txHash) {
        /**
         * I calculate the ammount by SUM all the outputs amounts.
         */
        Sha256Hash hash = new Sha256Hash(txHash);
        Transaction tx = vault.getTransaction(hash);
        Coin values = tx.getValue(vault);
        return values.getValue();
    }

    /**
     * gets the address To of the transaction
     * @param txHash
     * @return the string of the address
     */
    private String[] getAddressFromTransaction(String txHash) {
        String[] addresses = new String[2];

        Sha256Hash hash = new Sha256Hash(txHash);
        Transaction tx = vault.getTransaction(hash);

        /**
         * I will search on all outputs for an address that is mine
         */
        for (TransactionOutput output : tx.getOutputs()) {
            if (output.isMine(vault)){
                /**
                 * this is address To
                 */
                addresses[1] = output.getScriptPubKey().getToAddress(this.networkParameters).toString();
            } else {
                /**
                 * This is address From
                 */
                addresses[0] = output.getScriptPubKey().getToAddress(networkParameters).toString();
            }

        }
        return addresses;
    }



    /**
     * Get the timestamp of the transaction
     * @param txHash
     * @return
     */
    private long getTransactionTimestampFromVault(String txHash){
        Sha256Hash hash = new Sha256Hash(txHash);
        Transaction tx = vault.getTransaction(hash);

        /**
         * I get the current timestamp
         */
        return tx.getLockTime();
    }

    public CryptoStatus getCryptoStatus(UUID transactionId) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        CryptoVaultDatabaseActions db = new CryptoVaultDatabaseActions(database, errorManager, eventManager);
        db.setVault(vault);
        return db.getCryptoStatus(transactionId.toString());
    }
}
