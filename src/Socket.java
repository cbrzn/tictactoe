
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;


@ServerEndpoint("/game")
public class Socket {
	private static Set<Session> clients = 
			Collections.synchronizedSet(new HashSet<Session>());
	
	
	@OnMessage
	public void onMessage (String msg, Session session) throws IOException{
		
		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText(msg);
			}
			System.out.println(clients.size());

		}
		
	}
	
	@OnOpen
	public void onOpen(Session session) throws IOException{
		clients.add(session);
		synchronized (clients) {
		for (Session client : clients) {
			client.getBasicRemote().sendText("Two users already connected");
		}
				}
	}
	
	@OnClose
	public void onClose (Session session) throws IOException{
		clients.remove(session);
		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText( "Alguien abandono la sala");
			}
		}
		
	}
	@OnError
	public void onError(Throwable e) throws IOException {
	    e.printStackTrace();
	    synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText( e.toString());
			}
		}
	}
	 
}