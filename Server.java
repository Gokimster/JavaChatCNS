import java.io.*;
  import java.net.*;

  public class Server {
   public static void main(String args[]) {
    byte[] buf = new byte[1024];
    try {
      ServerSocket s = new ServerSocket(6001, 5); // create socket
      Socket sock = s.accept(); // accept connection
      InputStream input = sock.getInputStream();
      int n = input.read(buf); // read message
      System.out.println("Message: " + new String(buf, 0, n));
      sock.close(); // close connection
    } catch (IOException e) { System.err.println(e.getMessage()); }
   }
  }