package com.bitdubai.fermat_api.layer.dmp_network_service.intra_user;

import java.util.UUID;

/**
 * Created by ciencias on 2/13/15.
 */
public interface IntraUserNetworkService {
    
    public IntraUser getSystemUser (UUID userId);
}
