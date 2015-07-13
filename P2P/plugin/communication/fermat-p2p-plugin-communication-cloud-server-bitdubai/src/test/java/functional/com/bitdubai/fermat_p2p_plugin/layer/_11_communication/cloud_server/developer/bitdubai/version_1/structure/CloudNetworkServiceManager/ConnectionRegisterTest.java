package functional.com.bitdubai.fermat_p2p_plugin.layer._11_communication.cloud_server.developer.bitdubai.version_1.structure.CloudNetworkServiceManager;

import static org.fest.assertions.api.Assertions.*;
import functional.com.bitdubai.fermat_p2p_plugin.layer._11_communication.cloud_server.developer.bitdubai.version_1.structure.mocks.MockFMPPacketsFactory;

import org.junit.Before;
import org.junit.Test;

import com.bitdubai.fermat_p2p_api.layer.p2p_communication.fmp.FMPPacket;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.fmp.FMPPacket.FMPPacketType;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmectricCryptography;

/**
 * Created by jorgeejgonzalez on 27/04/15.
 */
public class ConnectionRegisterTest extends CloudNetworkServiceManagerIntegrationTest{
	
	@Before
	public void setUpParameters() throws Exception{
		setUpAddressInfo(TCP_BASE_TEST_PORT+100);
		setUpKeyPair();
		setUpExecutor(2);		
	}
	
	@Test
	public void ConnectionRegister_SendValidRequest_ClientGetsResponse() throws Exception{
		setUpConnections(0);
		requestConnection();
		FMPPacket response = registerConnection();
		assertThat(response).isNotNull();
	}
	
	@Test
	public void ConnectionRegister_SendValidRequest_ResponsePacketTypeDataTransmit() throws Exception{
		setUpConnections(2);
		requestConnection();
		FMPPacket response = registerConnection();
		assertThat(response.getType()).isEqualTo(FMPPacketType.DATA_TRANSMIT);
	}
	
	@Test
	public void ConnectionRegister_SendValidRequest_ResponseMessageDecrypted() throws Exception{
		setUpConnections(4);
		requestConnection();
		FMPPacket response = registerConnection();
		String decryptedMessage = AsymmectricCryptography.decryptMessagePrivateKey(response.getMessage(), MockFMPPacketsFactory.MOCK_PRIVATE_KEY);		
		assertThat(decryptedMessage).isEqualTo("REGISTERED");
	}
	
	@Test
	public void ConnectionRegister_SendValidRequest_ResponseSignatureIsValid() throws Exception{
		setUpConnections(6);
		requestConnection();
		FMPPacket response = registerConnection();
		boolean signatureVerification = AsymmectricCryptography.verifyMessageSignature(response.getSignature(), response.getMessage(), response.getSender());
		assertThat(signatureVerification).isTrue();
	}
	
	@Test
	public void ConnectionRegister_NoRequest_ResponseIsNull() throws Exception{
		setUpConnections(8);
		FMPPacket response = registerConnection();
		assertThat(response).isNull();
	}
}