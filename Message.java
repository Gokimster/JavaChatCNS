import java.util.*;
import java.text.*;

public class Message 
{
  
  String sender, title, text;
  Date date;

  //instantiates a message with the current date and time
  Message(String sender, String title, String text)
  {
  	this.sender = sender;
  	this.title = title;
  	this.text = text;
  	date = new Date();
  }

  //instantiates a message with a specified date and time (used when decrypting the messages)
  Message(String sender, String title, String text, Date date)
  {
  	this.sender = sender;
  	this.title = title;
  	this.text = text;
  	this.date = date;
  }

  //returns the date and time in String format
  public String getTimestamp()
  {
  	SimpleDateFormat sdt = new SimpleDateFormat("[dd.MM.'yy-HH:mm:ss]");
  	return sdt.format(date);
  }

  //getter functions
  public Date getDate()
  {
  	return date;
  }

  public String getSender()
  {
  	return sender;
  }

  public String getTitle()
  {
  	return title;
  }

  public String getText()
  {
  	return text;
  }
}