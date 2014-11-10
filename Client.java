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