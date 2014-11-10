import java.io.*;
import javax.net.ssl.*;

public class Server {
 public static void main(String[] args) {
   byte[] buf = new byte[1024];
   try {
     SSLServerSocketFactory f =
         (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
     int port = Integer.parseInt(args[0]);
     SSLServerSocket s = (SSLServerSocket)f.createServerSocket(port);
     s.setEnabledCipherSuites(s.getSupportedCipherSuites());
     SSLSocket sock = (SSLSocket)s.accept();
     InputStream input = sock.getInputStream();
     int n = input.read(buf);
     System.out.println("Message: " + new String(buf, 0, n));
   } catch (Exception e) { e.printStackTrace(); }
 }
}
/* FOR SOCKET FACTORY

 SSL example is changed to authenticate server by using

    truststore "trust" for SSL client with store password "giants"
    keystore "key" for SSL server with store password "broncos"
    password "rams" for private key in keystore "key"


KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("key"), "broncos".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "rams".toCharArray());
SSLContext sc = SSLContext.getInstance("SSLv3");
sc.init(kmf.getKeyManagers(), null, null);
SSLServerSocketFactory f = sc.getServerSocketFactory(); */ 