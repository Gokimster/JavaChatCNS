import java.nio.charset.Charset;
import java.util.*;

public class MessageManager
{
  
  static int MAX_MESSAGES = 10;
  List <Message> messages;


  MessageManager()
  {
  	messages = new ArrayList <Message> ();
  }

  //adds a new message with encrypted text
  //if the max number of messages is exceeded, remove the oldest message
  public void addMessage(String sender, String tite, String text)
  {
    Message message = new Message(sender, tite, encryptMessageText(text));
    //testing print- to be REMOVED
    System.out.println(encryptMessageText(text));
    if (messages.size() >= MAX_MESSAGES)
    {
      messages.remove(0);
    }
    messages.add(message);
  }

  //encrypts a String (should be done in encryption file, here for now)
  public String encryptMessageText(String text)
  {
    Base64 crypto = new Base64();
    String str = new String(crypto.encode(text.getBytes(Charset.forName("UTF-8"))));
    return str;
  }

  //decrypts a String (should be done in encryption file, here for now)
  public String decryptMessageText(String text)
  {
    Base64 crypto = new Base64();
    String str = new String(crypto.decode(text.getBytes(Charset.forName("UTF-8"))));
    return str;
  }

  //decrypts a message
  private Message decryptMessage(Message m)
  {
    Message decrypted = new Message(m.getSender(), m.getTitle(), decryptMessageText(m.getText()), m.getDate());
    return decrypted;
  }

  //returns an ArrayList of all messages with they texts decrypted
  public ArrayList getDecryptedMessages()
  {
    ArrayList <Message> l= new ArrayList <Message>();
    for (int i = 0; i < messages.size(); i++)
    {
      l.add(decryptMessage(messages.get(i)));
    }
    return l;
  }

}