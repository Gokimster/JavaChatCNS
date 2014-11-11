import java.util.*;
import java.io.Serializable;
import java.text.*;

public class Message implements Serializable
{
  
  String author, title, text;
  Date date;

  //instantiates a message with the current date and time
  Message(String author, String title, String text)
  {
  	this.author = author;
  	this.title = title;
  	this.text = text;
  	date = new Date();
  }

  //instantiates a message with a specified date and time (used when decrypting the messages)
  Message(String author, String title, String text, Date date)
  {
  	this.author = author;
  	this.title = title;
  	this.text = text;
  	this.date = date;
  }

  //returns the date and time in String format
  public String getTimestamp()
  {
  	SimpleDateFormat sdt = new SimpleDateFormat("[dd.MM. yy-HH:mm:ss]");
  	return sdt.format(date);
  }

  //getter functions
  public Date getDate()
  {
  	return date;
  }

  public String getAuthor()
  {
  	return author;
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