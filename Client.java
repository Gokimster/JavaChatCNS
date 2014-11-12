import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client
{
    String userID;
    Socket sock;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    
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
    }

    public boolean authenticate(String userID, String pass)
    {   
        //true for now, will have to check userId and pass true
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
        Message m = (Message) ois.readObject();
        return m;
    }

    public ArrayList <Message> getMessages() throws ClassNotFoundException, IOException
    {
        //to be changed to return all messages from server
        //Message m = (Message) ois.readObject();
        Message m = new Message(userID, "text","text");
        ArrayList<Message> messages= new ArrayList<Message>();
        messages.add(m);
        m = (Message) ois.readObject();
        if (m != null)
        {
            System.out.println("NOT NULL< ADD ");
            messages.add(m);
        }
        return messages;
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