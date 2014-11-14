import java.util.*;
import java.io.Serializable;
import java.text.*;

import com.sun.javafx.iio.ImageFormatDescription.Signature;

public class Message implements Serializable, Cloneable 
{
  
  String author, title, text;
  Date date;
  boolean serverMessage;

  //instantiates a message with the current date and time
  Message(String author, String title, String text)
  {
  	this.author = author;
  	this.title = title;
  	this.text = text;
  	date = new Date();
    serverMessage = false;
  }

  Message(String author, String title, String text, boolean serverMessage)
  {
    this.author = author;
    this.title = title;
    this.text = text;
    date = new Date();
    this.serverMessage = serverMessage;
  }

   Message(Message m)
  {
    author = m.getAuthor();
    title = m.getTitle();
    text = m.getText();
    date = m.getDate();
  }

  //instantiates a message with a specified date and time (used when decrypting the messages)
  Message(String author, String title, String text, Date date)
  {
  	this.author = author;
  	this.title = title;
  	this.text = text;
  	this.date = date;
  }

  Message(String request, boolean serverMessage)
  {
    this.text = request;
    this.serverMessage = serverMessage;
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