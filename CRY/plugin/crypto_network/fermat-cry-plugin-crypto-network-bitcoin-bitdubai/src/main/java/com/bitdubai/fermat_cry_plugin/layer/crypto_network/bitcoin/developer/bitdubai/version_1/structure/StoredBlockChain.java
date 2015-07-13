package com.bitdubai.fermat_cry_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantInitializeMonitorAgentException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_cry_api.layer.crypto_network.bitcoin.BitcoinManager;
import com.bitdubai.fermat_cry_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.CantCreateBlockStoreFileException;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.SPVBlockStore;

import java.io.File;
import java.util.UUID;

import static com.bitdubai.fermat_cry_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.structure.BitcoinNetworkConfiguration.getNetworkConfiguration;

/**
 * Created by rodrigo on 25/05/15.
 * Holds the blockchain data.
 */

class StoredBlockChain implements BitcoinManager, DealsWithErrors, DealsWithPluginFileSystem, DealsWithPluginIdentity{
    /**
     * StoredBlockChain members variables
     */
    Wallet wallet;
    BlockChain chain;
    NetworkParameters networkParameters;
    SPVBlockStore spvStore;
    MemoryBlockStore memoryStore;
    UUID userId;


    /**
     * DealsWithErrors interface member variables
     */
    ErrorManager errorManager;


    /**
     * DealsWithPluginFileSystem interface member variables
     */
    PluginFileSystem pluginFileSystem;

    /**
     * DealsWithPluginIdentity interface member variable
     */
    UUID pluginId;

    /**
     * ErrorManager interface implementation
     * @param errorManager
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
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
     * constructor
     * @param wallet the BitcoinJ wallet with the addresses used to listen to the network
     * @param UserID The if of the user requesting the syncronization
     * @throws CantInitializeMonitorAgentException
     */
    public StoredBlockChain (Wallet wallet, UUID UserID) {
        this.networkParameters = getNetworkConfiguration();
        this.wallet = wallet;
        this.userId = UserID;
    }

    /**
     * creates the blockchain object and the repository
     */
    public void createBlockChain() throws CantCreateBlockStoreFileException {
        String blockChainFileName = userId.toString() + ".spv";
        try {
            /**
             * I will save the blockchain into disk.             */

            PluginTextFile blockchainFile = pluginFileSystem.createTextFile(pluginId, userId.toString(), blockChainFileName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
            blockchainFile.persistToMedia();

            //todo this needs to be fixed
            File spvFile = new File("/data/data/com.bitdubai.fermat/files", blockChainFileName);

            spvStore = new SPVBlockStore(this.networkParameters, spvFile);
            chain = new BlockChain(this.networkParameters, this.wallet, spvStore);
        } catch (CantPersistFileException e) {
            StringBuilder context = new StringBuilder();
            context.append("userId: " + userId);
            context.append(CantCreateBlockStoreFileException.CONTEXT_CONTENT_SEPARATOR);
            context.append("blockChainFileName: " + blockChainFileName.toString());
            throw new CantCreateBlockStoreFileException("Blockstore file could not be persisted into disk.", e, context.toString(), "Not enought space on disk.");
        } catch (CantCreateFileException e) {
            StringBuilder context = new StringBuilder();
            context.append("userId: " + userId);
            context.append(CantCreateBlockStoreFileException.CONTEXT_CONTENT_SEPARATOR);
            context.append("blockChainFileName: " + blockChainFileName.toString());
            throw new CantCreateBlockStoreFileException("Blockstore file could not be created.", e, context.toString(), "Not enought space on disk.");
        } catch (BlockStoreException e) {
            /**
             * in an error occurs, I will try to save it into memory
             */
            memoryStore = new MemoryBlockStore(this.networkParameters);
            try {
                chain = new BlockChain(this.networkParameters, this.wallet, memoryStore);
            } catch (BlockStoreException e1) {
                StringBuilder context = new StringBuilder();
                context.append("userId: " + userId);
                context.append(CantCreateBlockStoreFileException.CONTEXT_CONTENT_SEPARATOR);
                context.append("blockChainFileName: " + blockChainFileName.toString());
                throw new CantCreateBlockStoreFileException("Could not save blockchain in disk and in memory", e, context.toString(), "Not enought space on disk.");
            }
        }

    }



    /**
     * StoredBlockChain getter
     * @return the blockchain that is saved into disk
     */
    public BlockChain getBlockChain(){
        return chain;
    }
}
