
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.tomcat.util.buf.StringUtils;
import org.json.JSONObject;


@ServerEndpoint("/game")
public class Socket {
	private static Set<Session> clients = 
			Collections.synchronizedSet(new HashSet<Session>());
	JSONObject json = new JSONObject();
	User user_one = new User();
	User user_two = new User();

	@OnMessage
	public void onMessage (String msg, Session session) throws IOException{
		JSONObject json = new JSONObject(msg);
		System.out.println(json.getInt("score_p2"));
		String id = session.getId();
		char charArray = id.charAt(0);
		if (id.length() == 2) 
		charArray = id.charAt(1);
		System.out.println();
			if ((charArray) % 2 != 0 || (charArray == 'a' || charArray == 'c' || charArray == 'e')) {
				user_one.setName("P1");
				user_one.setToken("X");
				json.put("token", user_one.getToken());
			} else {
				user_two.setName("P2");
				user_two.setToken("O");
				json.put("token", user_two.getToken());
			}
			
			if (json.getString("msg").equals("O has won!")) {
				int score = json.getInt("score_p2")+1;
				user_two.setScore(score);
				json.put("score_p2", score);
				synchronized (clients) {
					for (Session client : clients) {
						if(!session.getId().equals(client.getId())) {
							client.getBasicRemote().sendText(json.toString());
							}			
						}
				}
			} else if (json.getString("msg").equals("X has won!")){
				int score = json.getInt("score_p1")+1;
				user_one.setScore(score);
				json.put("score_p2", score);
				synchronized (clients) {
					for (Session client : clients) {
						if(!session.getId().equals(client.getId())) {
							client.getBasicRemote().sendText(json.toString());
							}			
						}
				}
			} else {
				user_two.setScore(json.getInt("score_p2"));
				user_one.setScore(json.getInt("score_p1"));
				json.put("score_two", user_two.getScore());
				json.put("score_one", user_one.getScore());


			}
	
			System.out.println(json.getInt("score_p1"));

		synchronized (clients) {
			for (Session client : clients) {

					
					client.getBasicRemote().sendText(json.toString());
				}
			}
		}
		

	
	@OnOpen
	public void onOpen(Session session) throws IOException{
			if (clients.size() < 2)
			user_one.setScore(0);
			user_two.setScore(0);
			json.put("score_p1", user_one.getScore());
			json.put("score_p2", user_two.getScore());
			clients.add(session);	
			synchronized (clients) {
				for (Session client : clients) {
					client.getBasicRemote().sendText(json.toString());
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