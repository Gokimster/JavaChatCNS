import java.nio.charset.Charset;
import java.util.*;

public class MessageManager
{
  
  static int MAX_MESSAGES = 10;
  ArrayList <Message> messages;


  MessageManager()
  {
  	messages = new ArrayList <Message> ();
  }

  //adds a new message with encrypted text
  //if the max number of messages is exceeded, remove the oldest message
  public void addMessage(String author, String tite, String text)
  {
    Message message = new Message(author, tite, text);
    if (messages.size() >= MAX_MESSAGES)
    {
      messages.remove(0);
    }
    messages.add(message);
  }

  //returns an ArrayList of all messages with they texts decrypted
  public ArrayList <Message> getMessages()
  {
    return messages;
  }

}