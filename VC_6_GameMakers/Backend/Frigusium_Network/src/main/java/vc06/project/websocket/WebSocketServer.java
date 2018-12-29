package vc06.project.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocketServer {

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new HashMap<>();
	private static Map<String, Session> usernameSessionMap = new HashMap<>();

	private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException {
		logger.info("Entered into Open");
		
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);
		
		String colorPart = username.substring(0, 6);
		String namePart = username.substring(6, username.length());
		
		if (!colorPart.equals("------"))
		{
			String message = " MT_SM-" + colorPart + " ------User " + namePart + " entered the Lobby------\n";
			broadcast(message);
		}
	}
	
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// Handle new messages
		logger.info("Entered into Message: Got Message:" + message);
		String username = sessionUsernameMap.get(session);

		String colorPart = username.substring(0, 6);
		String namePart = username.substring(6, username.length());

		if ( message.length() > 6 && message.substring(0, 6).equals("SYSTEM"))
		{
			Scanner s = new Scanner(message.substring(6,message.length()));
			String userTo = "------"+s.next().trim();
			String parsedMessage = "";
			while ( s.hasNext() ) { String toAdd = s.next().trim(); if ( toAdd != null ) { parsedMessage += toAdd+" "; }} s.close();
			sendMessageToParticularUser(userTo,parsedMessage);
		}
		else
		{
			broadcast(" MT_CM-" + colorPart + " " + namePart + " : " + message);
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

		String colorPart = username.substring(0, 6);
		String namePart = username.substring(6, username.length());

		if ( !colorPart.equals("------"))
		{
			String message = " MT_SM-" + colorPart + " ------User " + namePart + " left the Lobby------\n";
			broadcast(message);
		}
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
	}

	private void sendMessageToParticularUser(String username, String message) {
		try {
			usernameSessionMap.get(username).getBasicRemote().sendText(message);
		} catch (IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	private static void broadcast(String message) throws IOException {
		sessionUsernameMap.forEach((session, username) -> {
			synchronized (session) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
