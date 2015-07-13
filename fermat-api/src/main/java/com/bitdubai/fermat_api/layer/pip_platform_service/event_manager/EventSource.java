package com.bitdubai.fermat_api.layer.pip_platform_service.event_manager;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by ciencias on 24.01.15.
 */
public enum EventSource {
    USER_DEVICE_USER_PLUGIN ("UDU"),
    CRYPTO_NETWORK_BITCOIN_PLUGIN ("CNB"),
    COMMUNICATION_CLOUD_PLUGIN ("CCL"),
    NETWORK_SERVICE_INTRA_USER_PLUGIN ("NIU"),
    NETWORK_SERVICE_WALLET_RESOURCES_PLUGIN ("NWR"),
    NETWORK_SERVICE_WALLET_COMMUNITY_PLUGIN ("NWC"),
    MIDDLEWARE_APP_RUNTIME_PLUGIN ("MAR"),
    MIDDLEWARE_WALLET_CONTACTS_PLUGIN ("MWC"),
    MIDDLEWARE_WALLET_PLUGIN ("MW0"),
    MODULE_WALLET_MANAGER_PLUGIN ("MWM"),
    USER_INTRA_USER_PLUGIN ("UIU"),
    CRYPTO_ADDRESS_BOOK ("CAB"),
    NETWORK_SERVICE_MONEY_REQUEST_PLUGIN ("NMR"),
    MIDDLEWARE_MONEY_REQUEST_PLUGIN ("MMR"),
    NETWORK_SERVICE_MONEY_PLUGIN ("NSM"),
    WORLD_BLOCKCHAIN_INFO_PLUGIN ("WBI"),
    DISCOUNT_WALLET_BASIC_WALLET_PLUGIN ("DWB"),
    DEVICE_CONNECTIVITY ("DCO"),
    CRYPTO_ROUTER ("CCR"),
    CRYPTO_VAULT ("CCV");


    private final String code;

    EventSource(String Code) {
        this.code = Code;
    }

    public String getCode()   { return this.code ; }

    public static EventSource getByCode(String code) throws InvalidParameterException {

        switch (code) {
            case "UDU":
                return EventSource.USER_DEVICE_USER_PLUGIN;
            case "CNB":
                return EventSource.CRYPTO_NETWORK_BITCOIN_PLUGIN;
            case "CCL":
                return EventSource.COMMUNICATION_CLOUD_PLUGIN;
            case "NIU":
                return EventSource.NETWORK_SERVICE_INTRA_USER_PLUGIN;
            case "NWR":
                return EventSource.NETWORK_SERVICE_WALLET_RESOURCES_PLUGIN;
            case "NWC":
                return EventSource.NETWORK_SERVICE_WALLET_COMMUNITY_PLUGIN;
            case "MAR":
                return EventSource.MIDDLEWARE_APP_RUNTIME_PLUGIN;
            case "MWC":
                return EventSource.MIDDLEWARE_WALLET_CONTACTS_PLUGIN;
            case "MW0":
                return EventSource.MIDDLEWARE_WALLET_PLUGIN;
            case "MWM":
                return EventSource.MODULE_WALLET_MANAGER_PLUGIN;
            case "UIU":
                return EventSource.USER_INTRA_USER_PLUGIN;
            case "CAB":
                return EventSource.CRYPTO_ADDRESS_BOOK;
            case "NMR":
                return EventSource.NETWORK_SERVICE_MONEY_REQUEST_PLUGIN;
            case "MMR":
                return EventSource.MIDDLEWARE_MONEY_REQUEST_PLUGIN;
            case "NSM":
                return EventSource.NETWORK_SERVICE_MONEY_PLUGIN;
            case "WBI":
                return EventSource.WORLD_BLOCKCHAIN_INFO_PLUGIN;
            case "DWB":
                return EventSource.DISCOUNT_WALLET_BASIC_WALLET_PLUGIN;
            case "DCO":
                return EventSource.DEVICE_CONNECTIVITY;
            case "CCR":
                return EventSource.CRYPTO_ROUTER;
            case "CCV":
                return EventSource.CRYPTO_VAULT;
        }

        /**
         * If we try to cpmvert am invalid string.
         */
        throw new InvalidParameterException(code);
    }
}
