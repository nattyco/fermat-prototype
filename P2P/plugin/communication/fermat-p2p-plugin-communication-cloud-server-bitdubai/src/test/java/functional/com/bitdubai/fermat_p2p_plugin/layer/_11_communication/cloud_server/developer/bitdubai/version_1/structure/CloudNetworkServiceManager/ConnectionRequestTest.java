package functional.com.bitdubai.fermat_p2p_plugin.layer._11_communication.cloud_server.developer.bitdubai.version_1.structure.CloudNetworkServiceManager;

import static org.fest.assertions.api.Assertions.*;
import functional.com.bitdubai.fermat_p2p_plugin.layer._11_communication.cloud_server.developer.bitdubai.version_1.structure.mocks.MockFMPPacketsFactory;

import org.junit.Before;
import org.junit.Test;

import com.bitdubai.fermat_p2p_api.layer.p2p_communication.fmp.FMPPacket;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.fmp.FMPPacket.FMPPacketType;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmectricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.enums.NetworkServices;

/**
 * Created by jorgeejgonzalez on 27/04/15.
 */
public class ConnectionRequestTest extends CloudNetworkServiceManagerIntegrationTest {
	
	
	@Before
	public void setUpParameters() throws Exception{
		setUpAddressInfo(TCP_BASE_TEST_PORT+200);
		setUpKeyPair();
		setUpExecutor(2);		
	}
	
	@Test
	public void ConnectionRequest_SendValidRequest_ClientGetsResponse() throws Exception{
		setUpConnections(0);
		FMPPacket response = requestConnection();
		assertThat(response).isNotNull();
	}
	
	@Test
	public void ConnectionRequest_SendValidRequest_ResponseTypeConnectionAccept() throws Exception{
		setUpConnections(2);
		FMPPacket response = requestConnection();
		assertThat(response.getType()).isEqualTo(FMPPacketType.CONNECTION_ACCEPT);
	}
	
	@Test
	public void ConnectionRequest_SendValidRequest_ResponseDestinationEqualsRequestSender() throws Exception{
		setUpConnections(4);
		FMPPacket response = requestConnection();
		assertThat(response.getDestination()).isEqualTo(MockFMPPacketsFactory.MOCK_PUBLIC_KEY);
	}
	
	@Test
	public void ConnectionRequest_SendValidRequest_ResponseSignatureVerified() throws Exception{
		setUpConnections(6);
		FMPPacket response = requestConnection();	
		boolean signatureVerification = AsymmectricCryptography.verifyMessageSignature(response.getSignature(), response.getMessage(), response.getSender());
		assertThat(signatureVerification).isTrue();	
	}
	
	@Test
	public void ConnectionRequest_SendRequestForDifferentNetworkService_ResponseTypeConnectionDeny() throws Exception{
		setUpConnections(8);
		FMPPacket request = MockFMPPacketsFactory.mockRequestConnectionNetworkServicePacket(NetworkServices.MONEY, testManager.getPublicKey());
		testClient.sendMessage(request);		
		FMPPacket response = getResponse();		
		assertThat(response.getType()).isEqualTo(FMPPacketType.CONNECTION_DENY);
	}
	
	@Test
	public void ConnectionRequest_RequestDestinationInvalid_NoResponse() throws Exception{
		setUpConnections(10);
		FMPPacket request = MockFMPPacketsFactory.mockRequestConnectionNetworkServicePacket(testNetworkService);
		testClient.sendMessage(request);
		FMPPacket response = getResponse();
		assertThat(response).isNull();
	}
	
	@Test
	public void ConnectionRequest_RequestMessageIsNotNetworkService_ResponseTypeConnectionDeny() throws Exception{
		setUpConnections(12);
		FMPPacket request = MockFMPPacketsFactory.mockRequestConnectionPacket(testManager.getPublicKey());
		testClient.sendMessage(request);
		FMPPacket response = getResponse();
		assertThat(response.getType()).isEqualTo(FMPPacketType.CONNECTION_DENY);
	}

}