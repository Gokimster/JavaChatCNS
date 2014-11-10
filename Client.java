import java.io.*;
import javax.net.ssl.*;

public class Client {
	public static void main(String[] args) {
	 try {
	   SSLSocketFactory f =
	          (SSLSocketFactory)SSLSocketFactory.getDefault();
	   int port = Integer.parseInt(args[1]);
	   SSLSocket s = (SSLSocket)f.createSocket(args[0], port);
	   s.setEnabledCipherSuites(s.getSupportedCipherSuites());
	   System.out.println("mode: " + s.getSession().getCipherSuite());
	   OutputStream output = s.getOutputStream();
	   output.write(args[2].getBytes());
	 } catch (Exception e) { e.printStackTrace(); }
	}
}