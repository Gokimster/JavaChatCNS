import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {

	// a list keeping the client list
	private ArrayList<ClientThread> ct;
	// which port to be listening at
	private int port;
	// should the server be listening or not
	private boolean active;

	public Server(int portNO) {
		port = portNO;
		ct = new ArrayList<ClientThread>();
	}

	public void initialize() {
		active = true;
		try {
			// the socket used by the server
			ServerSocket ss = new ServerSocket(port);
			// infinite loop to wait for connections
			while (active) {
<<<<<<< HEAD
				System.out.println("server active loop");
=======

>>>>>>> origin/master
				// format message saying we are waiting, just for debugging
				// to be removed in later versions
				System.out.println("Server waiting for Clients on port " + port
						+ ".");
<<<<<<< HEAD
				if(active==false)
					break;
				Socket socket = ss.accept(); // accept connection
				// if I was asked to stop
				//
				ClientThread t = new ClientThread(socket); // make a thread for
				// the client
=======

				Socket socket = ss.accept(); // accept connection
				// if I was asked to stop

				ClientThread t = new ClientThread(socket); // make a thread for
															// the client
>>>>>>> origin/master
				ct.add(t); // save it in the ArrayList of the client threads
				t.start(); // initialize the thread connectin
			}
			// If the server has to stop, all the threads and in/out streams
			// have to be closed.
			try {
<<<<<<< HEAD
				System.out.println("attempt to close everything");
				ss.close(); // close the server socket
				for (int i = 0; i < ct.size(); ++i) { // go thtough the whole
					// list of clients
=======
				ss.close(); // close the server socket
				for (int i = 0; i < ct.size(); ++i) { // go thtough the whole
														// list of clients
>>>>>>> origin/master
					ClientThread tc = ct.get(i);
					// try to close each of them
					try {
						tc.in.close();
						tc.out.close();
						tc.socket.close();
					} catch (IOException ioE) {
						// not much I can do
					}
				}
				// in case something doesnt want to close
			} catch (Exception e) {
				System.out
						.println("1 Problem with the closing of the threads and the in/out streams "
								+ e);
			}
		}
		// something went wrong
		catch (IOException e) {
			String msg = "Exception on new ServerSocket: " + e + "\n";
			System.out.println(msg);
		}
<<<<<<< HEAD
		System.out.println("reset");

	}

	public synchronized void distributeMessage(Message msg) {
		String message_text = msg.getText();
		String author = msg.getAuthor();
		System.out.println(author + ">>>"+message_text);
		for (int i = ct.size()-1; i >= 0; --i) {
			ClientThread current = ct.get(i);
			//try to send them a meessage
			if ((current.sendMessage(msg)) == 0) {
				System.out.println("The user has disconnected");
			}
		}

	}

	/*for(int i = 0; i < al.size(); ++i) {
            ClientThread ct = al.get(i);
		           // found it
		         if(ct.id == id) {
	               al.remove(i);
	                return;*/
	
	public static void main(String[] args) {

		int portnumber = 9999;

		Server server = new Server(portnumber);
=======

	}
	
	private synchronized void distributeMessage(Message msg){
		for(int i=ct.size(); i>0; i--){
			ClientThread current = ct.get(i);
			if((current.sendMessage(msg))==0){
				System.out.println("The user has disconnected");
			}
		}
		
	}

	public static void main(String[] args) {
		
		int portnumber = 9999;
		
		Server server = new Server (portnumber);
>>>>>>> origin/master
		server.initialize();
		/*
		 * byte[] buf = new byte[1024]; try { SSLServerSocketFactory f =
		 * (SSLServerSocketFactory)SSLServerSocketFactory.getDefault(); int port
		 * = Integer.parseInt(args[0]); SSLServerSocket s =
		 * (SSLServerSocket)f.createServerSocket(port);
		 * s.setEnabledCipherSuites(s.getSupportedCipherSuites()); SSLSocket
		 * sock = (SSLSocket)s.accept(); InputStream input =
		 * sock.getInputStream(); int n = input.read(buf);
		 * System.out.println("Message: " + new String(buf, 0, n)); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}

<<<<<<< HEAD


class ClientThread extends Thread {

	
	// private String author;
	public Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	//this is a problem cause message is saved and afterwards when it keeps looping it 
	//wont terminate cause it has something to loop over; 
	//dunno how to do it tho cause kinda lost and tired.
	Message message;
	boolean isActive;

	public ClientThread(Socket sock) {
		isActive = true;
=======
}

class ClientThread extends Thread {

	//private String author;
	public Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	boolean isActive = true;

	public ClientThread(Socket sock) {
>>>>>>> origin/master
		socket = sock;
		System.out.println("New thread connected");
		// try to create output/input streams
		try {
<<<<<<< HEAD
			System.out.println("Thread trying to create Object Input/Output Streams");
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			// author = ((Message) in.readObject()).getAuthor();
			System.out.println("Thread IO win!!");
=======
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			//author = ((Message) in.readObject()).getAuthor();
>>>>>>> origin/master
		} catch (IOException e) {
			System.out
					.println("There was an error creating the input/output stream"
							+ e.toString());
<<<<<<< HEAD
		} // catch (ClassNotFoundException e1) {
			// System.out.println("Problem with the sender");
			// }
		active();
=======
		} //catch (ClassNotFoundException e1) {
		//	System.out.println("Problem with the sender");
		//}
>>>>>>> origin/master

	}

	public void active() {
		while (isActive) {
<<<<<<< HEAD
			System.out.println("In Thread's active while loop");
			try {
				System.out.println("reading message from thread");
				message = (Message) in.readObject();
=======
			try {
				Message message = (Message) in.readObject();
>>>>>>> origin/master
			} catch (IOException e) {
				System.out
						.println("There was a problem reading the messaage object "
								+ e);
			} catch (ClassNotFoundException e1) {
				// I don't know what to do in this case tbf
			}
<<<<<<< HEAD
			System.out.println("propagading message");	
			distributeMessage(message);	
			// String message_text = message.getText();
			// System.out.println(message.getAuthor()+ ":" + message_text)
			isActive =false;
		}
		
	//closeConnections();	
	}

	private void closeConnections() {
		System.out.println("CLOSING CONNECTIONS IN THREAD closeconnections()");
=======
			//String message_text = message.getText();
			//System.out.println(message.getAuthor()+ ":" + message_text);
		}
		closeConnections();
	}

	private void closeConnections() {
>>>>>>> origin/master
		try {
			if (in != null)
				in.close();
		} catch (IOException e) {
			System.out.println("Error closing input stream" + e);
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e1) {
			System.out.println("Error closing output stream" + e1);
		}
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e2) {
			System.out.println("Error closing the socket" + e2);
		}
	}

	public int sendMessage(Message msg) {
<<<<<<< HEAD
		
		if (socket.isConnected()==false) {
			
			System.out.println("IT IS CLOSED FIX IT!!!");
			return 0;
		}else{
		try {
			System.out.println("trying to send a message");
			out.writeObject(msg);
		} catch (IOException e) {
			System.out.println("error sending message"+e);
=======
		if (!socket.isConnected()) {
			return 0;
		}
		try {
			out.writeObject(msg.getText());
		} catch (IOException e) {
			System.out.println("error writing message to server");
>>>>>>> origin/master
		}

		return 1;
	}
<<<<<<< HEAD
	}

}
}
=======

}

>>>>>>> origin/master
/*
 * FOR SOCKET FACTORY
 * 
 * SSL example is changed to authenticate server by using
 * 
 * truststore "trust" for SSL client with store password "giants" keystore "key"
 * for SSL server with store password "broncos" password "rams" for private key
 * in keystore "key"
 * 
 * 
 * KeyStore ks = KeyStore.getInstance("JKS"); ks.load(new
 * FileInputStream("key"), "broncos".toCharArray()); KeyManagerFactory kmf =
 * KeyManagerFactory.getInstance("SunX509"); kmf.init(ks, "rams".toCharArray());
 * SSLContext sc = SSLContext.getInstance("SSLv3");
 * sc.init(kmf.getKeyManagers(), null, null); SSLServerSocketFactory f =
 * sc.getServerSocketFactory();
 */