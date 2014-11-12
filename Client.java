import java.io.*;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client
{

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
        new ServerListener().start();
    }
    

    public void sendMessage(Message m) throws IOException
    {
        oos.writeObject(m);
    }

    public Message getMessage() throws ClassNotFoundException, IOException
    {
        Message m = (Message) ois.readObject();
        return m;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException 
    {
    	
        Client c = new Client();
        
        while(true){
        System.out.print("Enter Something : ");
        InputStreamReader rd = new InputStreamReader(System.in);
        BufferedReader ed = new BufferedReader(rd);
        String temp = ed.readLine();
        c.sendMessage(new Message(temp, temp, temp));
        //System.out.println(">>>>>>>>>>>"+tm.getText());
        //System.out.print(tm.getText());
        }
        
    }
    
    
    
    public class ServerListener extends Thread{
    	Message msg;
    	public void run(){
    		while(true){
    			try{
    				try{
    					msg = (Message)ois.readObject();
    				}catch(IOException e){System.out.println("Error reading object from ois");}
    				System.out.println(">>>>>>>>>>>>>>>>"+msg.getText());
    			}catch(ClassNotFoundException e2){}
    		}
    		
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