package com.bitdubai.fermat_cry_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_cry_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.BitcoinCryptoNetworkPluginRoot;

import org.bitcoinj.core.AbstractBlockChain;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChainListener;
import org.bitcoinj.core.GetDataMessage;
import org.bitcoinj.core.Message;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerEventListener;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.VerificationException;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by rodrigo on 27/05/15.
 */
class BitcoinEventListeners implements BlockChainListener, DealsWithLogger,  PeerEventListener{

    LogManager logManager;
    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * BlockChainEventListener interface implementation
     * @param block
     * @throws VerificationException
     */
    @Override
    public void notifyNewBestBlock(StoredBlock block) throws VerificationException {

    }


    /**
     * BlockChainEventListener interface implementation
     * @param splitPoint
     * @param oldBlocks
     * @param newBlocks
     * @throws VerificationException
     */
    @Override
    public void reorganize(StoredBlock splitPoint, List<StoredBlock> oldBlocks, List<StoredBlock> newBlocks) throws VerificationException {

    }

    /**
     * BlockChainEventListener interface implementation     *
     * @param tx
     * @return
     * @throws ScriptException
     */
    @Override
    public boolean isTransactionRelevant(Transaction tx) throws ScriptException {
        return true;
    }

    /**
     * BlockChainEventListener interface implementation
     * @param tx
     * @param block
     * @param blockType
     * @param relativityOffset
     * @throws VerificationException
     */
    @Override
    public void receiveFromBlock(Transaction tx, StoredBlock block, AbstractBlockChain.NewBlockType blockType, int relativityOffset) throws VerificationException {
        StringBuilder logAggresive = new StringBuilder("BitcoinCryptoNetwork information message: Receive from block. Transaction: " + tx.toString());
        logAggresive.append(System.getProperty("line.separator"));
        logAggresive.append("block: " + block.toString());
        logManager.log(BitcoinCryptoNetworkPluginRoot.getLogLevelByClass(this.getClass().getName()), "New block Recieved", "BitcoinCryptoNetwork information message: Receive from block. Transaction: " + tx.toString(), logAggresive.toString());

    }

    /**
     * BlockChainEventListener interface implementation
     * @param txHash
     * @param block
     * @param blockType
     * @param relativityOffset
     * @return
     * @throws VerificationException
     */
    @Override
    public boolean notifyTransactionIsInBlock(Sha256Hash txHash, StoredBlock block, AbstractBlockChain.NewBlockType blockType, int relativityOffset) throws VerificationException {
        return true;
    }


    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param block
     * @param blocksLeft
     */
    @Override
    public void onBlocksDownloaded(Peer peer, Block block, int blocksLeft) {
        StringBuilder logAggresive = new StringBuilder("New Block downloaded from Peer  " + peer.toString());
        logAggresive.append(System.getProperty("line.separator"));
        logAggresive.append("Blocks Left: " + blocksLeft);
        logAggresive.append(System.getProperty("line.separator"));
        logAggresive.append("Blocks info: " + block.toString());
        LogLevel logLevel = BitcoinCryptoNetworkPluginRoot.getLogLevelByClass(this.getClass().getName());
        logManager.log(logLevel, "Block Downloaded", "Block downloaded. Blocks left " + blocksLeft, logAggresive.toString());
    }

    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param blocksLeft
     */
    @Override
    public void onChainDownloadStarted(Peer peer, int blocksLeft) {
        System.out.println("BitcoinCryptoNetwork information message: Blockchain Download started. Blocks left: " + blocksLeft);
    }

    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param peerCount
     */
    @Override
    public void onPeerConnected(Peer peer, int peerCount) {
        System.out.println("BitcoinCryptoNetwork information message: Peer connected (total: " + peerCount + "). Peer info: " + peer.toString());

        StringBuilder logAggresive = new StringBuilder("New connection to Peer " + peer.toString());
        logAggresive.append(System.getProperty("line.separator"));
        logAggresive.append("Total connected peers: " + peerCount);
        logManager.log(BitcoinCryptoNetworkPluginRoot.getLogLevelByClass(this.getClass().getName()), "Connected to Peer.", "Connected to peer. Total peers are " + peerCount, logAggresive.toString());
    }

    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param peerCount
     */
    @Override
    public void onPeerDisconnected(Peer peer, int peerCount) {
        System.out.println("BitcoinCryptoNetwork information message: Peer disconnected. Total Peers are " + peerCount);
    }

    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param m
     * @return
     */
    @Override
    public Message onPreMessageReceived(Peer peer, Message m) {
        return null;
    }

    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param t
     */
    @Override
    public void onTransaction(Peer peer, Transaction t) {

    }

    /**
     * PeerEventListener intercace implementation
     * @param peer
     * @param m
     * @return
     */
    @Nullable
    @Override
    public List<Message> getData(Peer peer, GetDataMessage m) {
        return null;
    }
}
