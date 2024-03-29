/*
 * @#IntraUserNetworkServiceMessage.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dmp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure;



import com.bitdubai.fermat_p2p_api.layer.p2p_communication.Message;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.MessagesStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.MessagesTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import sun.misc.resources.Messages;

/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer._11_network_service.intra_user.developer.bitdubai.version_1.structure.IntraUserNetworkServiceMessage</code>
 * is the implementation of the message<p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 09/06/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class IntraUserNetworkServiceMessage implements Message, Serializable {

    /**
     * Represent the serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represent the id of the message
     */
    private Long id;

    /**
     * Represent the sender of the message
     */
    private UUID sender;

    /**
     * Represent the receiver of the message
     */
    private UUID receiver;

    /**
     * Represent the textContent
     */
    private String textContent;

    /**
     * Represent the type of the message
     */
    private MessagesTypes messageType;

    /**
     * Represent the shipping timestamp of the message
     */
    private Timestamp shippingTimestamp;

    /**
     * Represent the delivery timestamp of the message
     */
    private Timestamp deliveryTimestamp;

    /**
     * Represent the status
     */
    private MessagesStatus status;


    /**
     * Represent the signature
     */
    private String signature;


    /**
     * Constructor
     */
    public IntraUserNetworkServiceMessage(){
        super();
        this.deliveryTimestamp = null;
        this.id           = null;
        this.receiver     = null;
        this.sender       = null;
        this.shippingTimestamp = new Timestamp(System.currentTimeMillis())  ;
        this.status       = null;
        this.textContent  = null;
        this.messageType = null;
    }

    /**
     * Constructor whit parameters
     */
    public IntraUserNetworkServiceMessage(Timestamp deliveryTimestamp, Long id, UUID receiver, UUID sender, Timestamp shippingTimestamp, MessagesStatus status, String textContent, MessagesTypes messageType) {
        super();
        this.deliveryTimestamp = deliveryTimestamp;
        this.id           = id;
        this.receiver     = receiver;
        this.sender       = sender;
        this.shippingTimestamp = shippingTimestamp;
        this.status       = status;
        this.textContent  = textContent;
        this.messageType = messageType;
    }

    /**
     * Get the deliveryTimestamp
     *
     * @return Long the delivery time
     */
    public Timestamp getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    /**
     * Set the deliveryTimestamp
     *
     * @param deliveryTimestamp the delivery time
     */
    public void setDeliveryTimestamp(Timestamp deliveryTimestamp) {
        this.deliveryTimestamp = deliveryTimestamp;
    }

    /**
     * Get the id
     *
     * @return Long the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the id of the receiver
     *
     * @return UUID the id of the receiver
     */
    public UUID getReceiver() {
        return receiver;
    }

    /**
     * Set the id of the receiver
     *
     * @param receiver the id of the receiver
     */
    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    /**
     * Get the id of the sender
     *
     * @return the id of the sender
     */
    public UUID getSender() {
        return sender;
    }

    /**
     * Set the id of the sender
     *
     * @param sender the id of the sender
     */
    public void setSender(UUID sender) {
        this.sender = sender;
    }

    /**
     * Get the shipping time
     *
     * @return the shipping time
     */
    public Timestamp getShippingTimestamp() {
        return shippingTimestamp;
    }

    /**
     * Set the shipping time
     *
     * @param shippingTimestamp the shipping time
     */
    public void setShippingTimestamp(Timestamp shippingTimestamp) {
        this.shippingTimestamp = shippingTimestamp;
    }

    /**
     * Get the status
     *
     * @return the status
     */
    @Override
    public MessagesStatus getStatus() {
        return status;
    }

    /**
     * Set the status
     *
     * @param status the status
     */
    public void setStatus(MessagesStatus status) {
        this.status = status;
    }

    /**
     * Get the textContent
     *
     * @return  the textContent
     */
    @Override
    public String getTextContent() {
        return textContent;
    }

    /**
     * Set the textContent
     *
     * @param textContent the textContent
     */
    @Override
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    /**
     * Get the textContent
     *
     * @return the textContent
     */
    public MessagesTypes getMessageType() {
        return messageType;
    }

    /**
     * Set the type message
     * @param messageType the type message
     */
    public void setMessageType(MessagesTypes messageType) {
        this.messageType = messageType;
    }


    /**
     * Get the signature
     *
     * @return String the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Set the signature message
     * @param signature of the message
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
