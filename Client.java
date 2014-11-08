import java.io.*;
import java.net.*;

public class Client {
  public static void main(String argv[]) {
    try {
      Socket socket = new Socket(argv[0], 6001); // connect
      OutputStream output = socket.getOutputStream();
      output.write(argv[1].getBytes()); // send message
      output.flush(); // flush connection
    } catch (IOException e) { System.err.println(e.getMessage()); }
  }
}