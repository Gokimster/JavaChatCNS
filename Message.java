import java.util.*;
import java.text.*;

public class Message 
{
  
  String sender, title, text;
  Date date;

  Message(String sender, String title, String text)
  {
  	this.sender = sender;
  	this.title = title;
  	this.text = text;
  	date = new Date();
  }

  Message(String sender, String title, String text, Date date)
  {
  	this.sender = sender;
  	this.title = title;
  	this.text = text;
  	this.date = date;
  }

  public String getTimestamp()
  {
  	SimpleDateFormat sdt = new SimpleDateFormat("[dd.MM.'yy-HH:mm:ss]");
  	return sdt.format(date);
  }

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