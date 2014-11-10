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