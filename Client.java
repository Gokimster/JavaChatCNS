import java.io.*;

import javax.net.ssl.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Client
{

    //made them private, may bug, oups
    private String userID;
    private SSLSocket sock;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean gotMessage, gotMessageArray;
    private Message newMessage;
    public static ArrayList<Message> messages;
    
    public Client()
    {
        try
        {
            //init SSL socket
            SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
            sock = (SSLSocket)sf.createSocket("localhost", 999);
            sock.setEnabledCipherSuites(sock.getSupportedCipherSuites());

            //init object input and outputstreams for socket
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
        //new ServerListener().start();new ServerListener().start()
    }

    private void initKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
    {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        // get user password and file input stream
        char[] password = "a".toCharArray();

        java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream("clientKeyStore.txt");
            ks.load(fis, password);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
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
        //messages = new ArrayList<Message>();
        Message m = new Message("MESSAGE_LIST");
        oos.writeObject(m);
        while(!gotMessageArray)
        {
            System.out.print("");
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
            System.out.println("add message)");
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
        HashMap<Integer, Object> obj;
        ArrayList <Message> temp;
 
        public void run(){
                while(true){
                        try{
                                obj = new HashMap();
                                try{
                                        obj.putAll((HashMap<Integer, Object>) ois.readObject());
                                        if (obj.containsKey(0)) {
                                                newMessage = (Message) obj.get(0);
                                                gotMessage = true;
                                                System.out.println("YES SINGLE MESSAGE");
                                        }
                                        if (obj.containsKey(1)) {
                                                System.out.print("");
                                                messages=(ArrayList<Message>) obj.get(1);
                                                gotMessageArray = true;
                                                System.out.println(((ArrayList<Message>) obj.get(1)).size());
                                                System.out.println("YES MESSAGE");
                                                System.out.println(gotMessageArray);
                                                obj = null;

                                        }
                                }catch(IOException e){System.out.println("Error reading object from ois");}
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