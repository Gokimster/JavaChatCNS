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

				// format message saying we are waiting, just for debugging
				// to be removed in later versions
				System.out.println("Server waiting for Clients on port " + port
						+ ".");

				Socket socket = ss.accept(); // accept connection
				// if I was asked to stop

				ClientThread t = new ClientThread(socket); // make a thread for
															// the client
				ct.add(t); // save it in the ArrayList of the client threads
				t.start(); // initialize the thread connectin
			}
			// If the server has to stop, all the threads and in/out streams
			// have to be closed.
			try {
				ss.close(); // close the server socket
				for (int i = 0; i < ct.size(); ++i) { // go thtough the whole
														// list of clients
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

}

class ClientThread extends Thread {

	//private String author;
	public Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	boolean isActive = true;

	public ClientThread(Socket sock) {
		socket = sock;
		System.out.println("New thread connected");
		// try to create output/input streams
		try {
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			//author = ((Message) in.readObject()).getAuthor();
		} catch (IOException e) {
			System.out
					.println("There was an error creating the input/output stream"
							+ e.toString());
		} //catch (ClassNotFoundException e1) {
		//	System.out.println("Problem with the sender");
		//}

	}

	public void active() {
		while (isActive) {
			try {
				Message message = (Message) in.readObject();
			} catch (IOException e) {
				System.out
						.println("There was a problem reading the messaage object "
								+ e);
			} catch (ClassNotFoundException e1) {
				// I don't know what to do in this case tbf
			}
			//String message_text = message.getText();
			//System.out.println(message.getAuthor()+ ":" + message_text);
		}
		closeConnections();
	}

	private void closeConnections() {
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
		if (!socket.isConnected()) {
			return 0;
		}
		try {
			out.writeObject(msg.getText());
		} catch (IOException e) {
			System.out.println("error writing message to server");
		}

		return 1;
	}

}

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