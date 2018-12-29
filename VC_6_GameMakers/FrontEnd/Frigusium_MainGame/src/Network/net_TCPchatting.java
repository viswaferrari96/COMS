package Network;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import MainGame.o_stuff;

public class net_TCPchatting extends WebSocketClient
{
	private o_stuff textStorage;
	
	public net_TCPchatting(URI serverUri, o_stuff textStorage)
	{
		super(serverUri);
		this.textStorage = textStorage;
	}
	
	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {}

	@Override
	public void onError(Exception arg0) {}

	@Override
	public void onMessage(String inputMessage)
	{
		textStorage.setString(textStorage.getString()+inputMessage);
	}

	@Override
	public void onOpen(ServerHandshake arg0) {}
}
