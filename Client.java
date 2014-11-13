import java.io.*;

import javax.net.ssl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Client
{

    String userID;
    Socket sock;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public boolean gotMessage, gotMessageArray;
    Message newMessage;
    public static ArrayList<Message> messages;
    ServerListener sl;
    
    public Client()
    {
        try
        {
            sock = new Socket("localhost",9999);
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong when creating Client");
        }
        gotMessage = false;
        gotMessageArray = false;
        newMessage = null;
        messages = new ArrayList<Message>();
        //new ServerListener().start();
        sl = new ServerListener();
        sl.start();
    }

    public boolean authenticate(String userID, String pass)
    {   
        //true for now, will have to check userId and pass true
        System.out.println("AUTHENTICATING");
        this.userID = userID;
        return true;
    }
    

    public void sendMessage(String title, String text) throws IOException
    {
        Message m = new Message(userID, title, text);
        oos.writeObject(m);
    }

    public Message getMessage() throws ClassNotFoundException, IOException
    {
        return newMessage;
    }

    public void resetRecievedMessages()
    {
        gotMessage = false;
        gotMessageArray = false;
    }

    public ArrayList <Message> getMessages() throws ClassNotFoundException, IOException
    {
        messages = new ArrayList<Message>();
        Message m = new Message("MESSAGE_LIST");
        oos.writeObject(m);
        while(!gotMessageArray)
        {
            System.out.println(gotMessageArray);
        }
        //copyList(messages, sl.getList());
        System.out.println(gotMessageArray +"     " +messages.size());

        return messages;
    }

    public void copyList(ArrayList<Message> to,ArrayList<Message> from )
    {
        to = new ArrayList<Message>();
        for(Message c : from)
        {
            to.add(c);
        }
    }


    public boolean hasNewMessage()
    {
        return gotMessage;
    }

    public boolean hasNewMessageArray()
    {
        return gotMessageArray;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException 
    {
    	
        Client c = new Client();
        
        while(true){
        System.out.print("Enter Something : ");
        InputStreamReader rd = new InputStreamReader(System.in);
        BufferedReader ed = new BufferedReader(rd);
        String temp = ed.readLine();
        c.sendMessage(temp, temp);
        ArrayList <Message> tm = c.getMessages();
        System.out.print(tm.get(0).getText());
        }
        
    }
    
    
    
    public class ServerListener extends Thread{
        HashMap obj;
        ArrayList <Message> temp;
    	public void run(){
    		while(true){
    			try{
                    obj = new HashMap();
                    //System.out.println(temp.size());
    				try{
    					obj.putAll((HashMap<Integer, Object>) ois.readObject());
    				}catch(IOException e){System.out.println("Error reading object from ois");}
                    if (obj.containsKey((Integer)0))
                    {
                        newMessage = (Message) obj.get((Integer)0);
                        gotMessage = true;
                        System.out.println("YES SINGLE MESSAGE");
                    }
                     if (obj.containsKey((Integer)1))
                    {
                        System.out.print("");
                        copyList(messages, (ArrayList<Message>) obj.get((Integer)1));
                        //temp = (ArrayList<Message>) obj.get((Integer)1);
                        //messages = (ArrayList<Message>) obj.get((Integer)1);//addAll(obj.values());

                        gotMessageArray = true;
                        //System.out.print("messages size"+messages.size());
                        System.out.println("YES MESSAGE");
                        System.out.println(gotMessageArray);
                    }
                    
    			}catch(ClassNotFoundException e2){}
    		}
    		
    	}

        public ArrayList<Message> getList()
        {
            return temp;
        }
    }	
    }


/*try {
SSLSocketFactory f =
(SSLSocketFactory)SSLSocketFactory.getDefault();
int port = Integer.parseInt(args[1]);
SSLSocket s = (SSLSocket)f.createSocket(args[0], port);
s.setEnabledCipherSuites(s.getSupportedCipherSuites());
System.out.println("mode: " + s.getSession().getCipherSuite());
OutputStream output = s.getOutputStream();
output.write(args[2].getBytes());
} catch (Exception e) { e.printStackTrace(); }*/

/* SSL example is changed to authenticate server by using
truststore "trust" for SSL client with store password "giants"
keystore "key" for SSL server with store password "broncos"
password "rams" for private key in keystore "key"
KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("trust"), "giants".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ks);
SSLContext sc = SSLContext.getInstance("SSLv3");
sc.init(null, tmf.getTrustManagers(), null);
SSLSocketFactory f = sc.getSocketFactory();
*/