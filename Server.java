import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
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
	private ArrayList<Message> messageList;
	
	String keystore;
	String password;

	public Server(int portNO) {
		port = portNO;
		ct = new ArrayList<ClientThread>();
		messageList=new ArrayList<Message>();
	}

	public void start(SSLServerSocket s) {
		active = true;
		try {
			//creating keystore factory
			
			// infinite loop to wait for connections
			while (active) {
				System.out.println("Server waiting for Clients on port " + port
						+ ".");
				if(active==false)
					break;
				SSLSocket sock = (SSLSocket)s.accept(); // accept connection
				ClientThread t = new ClientThread(sock); // make a thread for
				
				// the client
				ct.add(t); // save it in the ArrayList of the client threads
				t.start(); // initialize the thread connectin
				//sendToClient(messageList, t);
			}
			// If the server has to stop, all the threads and in/out streams
			// have to be closed.
			try {
				System.out.println("attempt to close everything");
				s.close(); // close the server socket
				for (int i = 0; i < ct.size(); ++i) { // go thtough the whole
					// list of clients
					ClientThread tc = ct.get(i);
					// try to close each of them
					try {
						tc.in.close();
						tc.out.close();
						tc.socket.close();
					} catch (IOException ioE) {
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
		System.out.println("reset");

	}

		public void initialize() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException{
			keystore="cert.store";
			password = "unicorn";
			KeyStore ks = KeyStore.getInstance("JKS");
			 ks.load(new FileInputStream(keystore), password.toCharArray());
			 KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			 kmf.init(ks, password.toCharArray());
			 SSLContext sc = SSLContext.getInstance("SSLv3");
			 sc.init(kmf.getKeyManagers(), null, null);
			 SSLServerSocketFactory f = sc.getServerSocketFactory();
			SSLServerSocket s = (SSLServerSocket)f.createServerSocket(port);
			s.setEnabledCipherSuites(s.getSupportedCipherSuites());
			start(s);
		}
	
			
		public void removeClient(ClientThread toClose){
			if(ct.contains(toClose)){
				System.out.println("In remove client");
				ct.remove(toClose);
			}
		}
		
	public synchronized void distributeMessage(Message msg) {
		messageList.add(msg);
		String message_text = msg.getText();
		String author = msg.getAuthor();
		if(ct.size()>0){
		for (ClientThread c : ct){
			System.out.println("sending message to peers");
			//try to send them a meessage
			if(c==null) continue;
			if ((c.sendMessage(msg,null)) == 0) {
				System.out.println("The user has disconnected");
			}
		}
		}
	}

	public static void main(String[] args) {
		
		int portnumber = 9999;
		
		Server server = new Server (portnumber);
		try {
			server.initialize();
		} catch (UnrecoverableKeyException | KeyManagementException
				| NoSuchAlgorithmException | CertificateException
				| KeyStoreException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



class ClientThread extends Thread {

	
	// private String author;
	public Socket socket;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	boolean isActive;

	public ClientThread(SSLSocket sock) {
		isActive = true;
		socket = sock;
		System.out.println("New thread connected");
		// try to create output/input streams
		try {
			System.out.println("Thread trying to create Object Input/Output Streams");
			in = new ObjectInputStream(sock.getInputStream());
			out = new ObjectOutputStream(sock.getOutputStream());
			System.out.println("Thread IO win successful");
		} catch (IOException e) {
			System.out.println("There was an error creating the input/output stream"
							+ e.toString());
		} 
		
	}

	public void run() {
		while (isActive) {
			Message message;
			System.out.println("In Thread's active while loop");
			try {
				
				 message = (Message) in.readObject();
				 ArrayList<Message> searchResult = new ArrayList<Message>();
				 if (message.isServerMessage()==true){
					 if(message.getText().equals("MESSAGE_LIST")){
						 sendMessage(null,messageList);
					 }else if(message.getText().equals("SEARCH")){
						 
						 for (int i = messageList.size() -1 ; i >= 0; i--)
					    {
							 Message m = messageList.get(i);
					      if (m.getAuthor().equals(message.getAuthor()))
					      {
					        if ((message.getTitle().equals("")) || (m.getTitle().equals(message.getTitle ())))
					        {
					          searchResult.add(m);
					        }
					      }
					      else
					      {
					        if (message.getAuthor().equals(""))
					        {
					          if (m.getTitle().equals(message.getTitle()))
					          {
					            searchResult.add(m);
					          }
					        }
					      }
					    }
						 sendMessage(null,searchResult);
				 }
					 
				 }
				 else{
				 	distributeMessage(message);
				 }
			} catch (IOException e) {
				System.out
						.println("There was a problem reading the messaage object "
								+ e);
				destructor();
			} catch (ClassNotFoundException e1) {}
			System.out.println("propagading message");
				
		}
		
	closeConnections();	
	}

	private void closeConnections() {
		System.out.println("CLOSING CONNECTIONS IN THREAD closeconnections()");
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
	
	public void destructor(){
		
			isActive = false;
			System.out.println("removing clinet");
			removeClient(this);
		
	}

	public int sendMessage(Message msg,ArrayList<Message> messages) {
		HashMap<Integer ,Object> hm = new HashMap<Integer, Object>();
		if(msg!=null){
		if (socket.isClosed()==true) {
			destructor();
			System.out.println("IT IS CLOSED FIX IT!!!");
			return 0;
		}else{
		try {
			System.out.println("trying to send a message");
			hm.put((Integer)0, msg);
			out.writeObject(hm);
		} catch (IOException e) {
			System.out.println("error sending message"+e);
		}

		return 1;
	}
	}else if(messages!=null) {
		try{
	    ArrayList<Message> clonedList = new ArrayList<Message>(messages.size());
	    System.out.println(clonedList.size());
	    for (Message temp : messages) {
        clonedList.add(new Message(temp));
    	}
		hm.put((Integer)1,clonedList);
		out.writeObject(hm);
		return 2;
	}catch(IOException e){System.out.println("problem sending arraylist to client");}
	}  return 0;
	}

}
}

