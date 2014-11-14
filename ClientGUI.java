import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class ClientGUI extends JFrame implements ActionListener 
{

  JTextField userID, pass, title, authorSearch, titleSearch;
  JTextArea message;
  JButton sendButton, searchButton, loginButton;
  int messageNoInArchive;
  Client client;
  JPanel messageCreationPanel, messageArchivePanel, messageSearchPanel, loginPanel;
  JTabbedPane tabPane;
  JScrollPane panelScroll, searchScroll;

  ClientGUI() throws CertificateException, IOException
  {
      init();
  }

  public void init() throws CertificateException, IOException
  {
    client = new Client();
    initLoginPanel();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setBounds(500, 200, 500, 100);
    setVisible(true);
  }

  //init the tab panels- after login is authenticated
  public void initTabs() throws ClassNotFoundException, IOException
  {
    new ClientListener().start();
    remove(loginPanel);
    initMessageCreationPanel();
    initMessageSearchPanel();
    initMessageArchivePanel();
    tabPane = new JTabbedPane();
    tabPane.add("Create Message", messageCreationPanel);
    tabPane.add("Search for Messages", messageSearchPanel);
    tabPane.add("Message Archive", panelScroll);
    add(tabPane);
    setBounds(500, 200, 500, 500);
    revalidate();
    repaint();
  }

  private void initLoginPanel()
  {
    loginPanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel(new GridLayout(2,2));
    userID = new JTextField();
    pass = new JTextField();
    topPanel.add(new JLabel("UserID: "));
    topPanel.add(userID);
    topPanel.add(new JLabel("Password: "));
    topPanel.add(pass);
    loginPanel.add(topPanel, BorderLayout.CENTER);
    loginButton = new JButton("Login");
    loginButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
                try {
					loginButtonAction();
				} catch (Exception e1) {
          showErrorMessage("Not logged in", "Could not log in, please check your details");
				}             
            }});
    JPanel botPanel = new JPanel(new GridLayout(1,1));
    botPanel.add(loginButton);
    loginPanel.add(botPanel, BorderLayout.SOUTH);
    add(loginPanel);
  }


  //initiate the message creation panel
  private void initMessageCreationPanel()
  {
    messageCreationPanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel(new GridLayout(3, 1));
    title = new JTextField();
    topPanel.add(new JLabel("Message Title: "));
    topPanel.add(title);
    topPanel.add(new JLabel("Message: "));
    messageCreationPanel.add(topPanel, BorderLayout.NORTH);

    JPanel midPanel = new JPanel (new GridLayout(1,1));
    message = new JTextArea();
    midPanel.add(message);
    JScrollPane midScroll = new JScrollPane(midPanel);
    messageCreationPanel.add(midScroll, BorderLayout.CENTER);

    JPanel botPanel = new JPanel (new GridLayout(1,2));
    sendButton = new JButton("Send Message");
    sendButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
            	try{
            		sendButtonAction();
            	}catch(Exception ex)
            	{
            		showErrorMessage("Message not sent", "Message could not be sent");
            	}
            }});
    botPanel.add(sendButton);
    messageCreationPanel.add(botPanel, BorderLayout.SOUTH);
  }

  //initiate the message archive panel
  private void initMessageArchivePanel() throws ClassNotFoundException, IOException
  {
    messageNoInArchive = 0;
    messageArchivePanel = new JPanel(new GridLayout(0,1));
    ArrayList <Message> messages = (ArrayList <Message>) client.getMessages();
    if (!messages.isEmpty())
    {
	    for (int i = 0 ; i < messages.size(); i++)
	    {
	      messageNoInArchive++;
	      JPanel oneMessage;
	      if (i % 2 == 0)
	      {
	        oneMessage = createMessagePanel(messages.get(i), true);
	      }
	      else 
	      {
	        oneMessage = createMessagePanel(messages.get(i), false);
	      }
	      messageArchivePanel.add(oneMessage, 0);
	    }
    }
    panelScroll = new JScrollPane(messageArchivePanel);
    client.resetRecievedMessages();
  }


  private void addMessageToArchive() throws ClassNotFoundException, IOException
  {
   // JPanel oneMessage = createMessagePanel(client.getMessage(), true)
    JPanel oneMessage;
     if (messageNoInArchive % 2 == 0)
      {
        oneMessage = createMessagePanel(client.getMessage(), true);
      }
      else 
      {
        oneMessage = createMessagePanel(client.getMessage(), false);
      }
    messageNoInArchive++;
    messageArchivePanel.add(oneMessage, 0);
  }

  private void refreshArchivePanel() throws ClassNotFoundException, IOException
  {    
    addMessageToArchive();
    messageArchivePanel.revalidate();
    messageArchivePanel.repaint();
    panelScroll.setViewportView(messageArchivePanel);
    panelScroll.revalidate();
    panelScroll.repaint();
    client.resetRecievedMessages();
  }


  private void initMessageSearchPanel()
  {
    messageSearchPanel = new JPanel(new BorderLayout());
    JPanel northPanel = new JPanel(new GridLayout(2,2));
    northPanel.add(new JLabel("Author: "));
    authorSearch = new JTextField();
    northPanel.add(authorSearch);
    northPanel.add(new JLabel("Title: "));
    titleSearch = new JTextField();
    northPanel.add(titleSearch);
    messageSearchPanel.add(northPanel, BorderLayout.NORTH);
    JPanel midPanel = new JPanel(new GridLayout(0,1));
    searchScroll = new JScrollPane(midPanel);
    messageSearchPanel.add(searchScroll, BorderLayout.CENTER);
    JPanel botPanel = new JPanel(new GridLayout(1,1));
    searchButton = new JButton("Search");
    searchButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
                try {
        					searchButtonAction();
        				} catch (ClassNotFoundException | IOException e1) {
                  showErrorMessage("Could not search", "There was a problem when searching for messages");
        				}
            }});
    botPanel.add(searchButton);
    messageSearchPanel.add(botPanel, BorderLayout.SOUTH);
  }

  //refreshes the archive panel with new messages
  private int refreshSearchPanel() throws ClassNotFoundException, IOException
  {
    messageSearchPanel.remove(searchScroll);
    JPanel midPanel = new JPanel(new GridLayout(0,1));
    ArrayList <Message> messages = client.getSearchMessages(authorSearch.getText(), titleSearch.getText());
    int counter = 0;
    for (int i = 0 ; i <messages.size(); i++)
    {
      Message m = messages.get(i);
          if (++counter % 2 != 0)
            midPanel.add(createMessagePanel(m, false));
          else 
            midPanel.add(createMessagePanel(m, true));
    }
    searchScroll = new JScrollPane(midPanel);
    messageSearchPanel.add(searchScroll, BorderLayout.CENTER);
    messageSearchPanel.revalidate();
    messageSearchPanel.repaint();
    client.resetRecievedMessages();
    return counter;
  }

  //create a panel with the information of a single message
  private JPanel createMessagePanel(Message m, boolean changeColor)
  {
    JPanel main = new JPanel(new BorderLayout());
    JPanel northPanel = new JPanel(new GridLayout(3,1));
    northPanel.add(new JLabel("By: " + m.getAuthor()));
    northPanel.add(new JLabel("At: " + m.getTimestamp()));
    northPanel.add(new JLabel(m.getTitle()+":"));
    if (changeColor)
      northPanel.setBackground(Color.WHITE);
    else
      northPanel.setBackground(new Color(134,187,255, 255));
    main.add(northPanel, BorderLayout.NORTH);
    JTextArea midPanel = new JTextArea(m.getText());
    midPanel.setEditable(false);
    midPanel.setBorder(null);
    midPanel.setLineWrap(true);
    midPanel.setWrapStyleWord(true);
    midPanel.setFocusable(false);
    if (changeColor)
     midPanel.setBackground(Color.WHITE);
    else 
      midPanel.setBackground(new Color(134,187,255, 255));
    main.add(midPanel);
    return main;
  }

  private void loginButtonAction() throws ClassNotFoundException, IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException
  {
    if (checkLoginBoxesFilled())
    {
      if (client.authenticate(userID.getText(), pass.getText()))
      {
        initTabs();
      }
      else
        showErrorMessage("Authentication Failed", "Your user id and password don't match!");
    }
  }

  //sends a message to the server
  private void sendButtonAction() throws IOException, ClassNotFoundException
  {
    if (checkMessageBoxesFilled() == true)
    {
      client.sendMessage(title.getText(), message.getText());
    }
  }

  private void searchButtonAction() throws ClassNotFoundException, IOException
  {
    if (checkSearchBoxesFilled())
    {
      if (refreshSearchPanel() == 0)
      {
        showErrorMessage("No messages found", "We could not find a message with which fits the author and title provided!");
      }
    }
  }

  public boolean checkLoginBoxesFilled()
  {
    if (userID.getText().equals(""))
    {
      showErrorMessage("User ID Missing", "Please enter your UserID!");
      return false;
    }
    else 
      if (pass.getText().equals(""))
      {
       showErrorMessage("Password Missing", "Please enter your Password!");
       return false;
      }
    return true;
  }

  //checks if both of the boxes in the search tab are empty
  public boolean checkSearchBoxesFilled()
  {
    if ((authorSearch.getText().equals("")) && (titleSearch.getText().equals("")))
    {
      showErrorMessage("No search parameters", "Please fill in at least one search criteria!");
      return false;
    }
    return true;
  }

  //checks if any of the boxes in the message creation tab are empty, returns false if so
  public boolean checkMessageBoxesFilled()
  {
    if (title.getText().equals(""))
    {
      showErrorMessage("Message Title Missing", "Please enter a title for your message!");
      return false;
    }
    else
      if (message.getText().equals(""))
      {
        showErrorMessage("Message Empty", "Your message is empty, you need to write a message to be able to send it!");
        return false; 
      }
    return true; 
  }

  //displays an error popup
  private void showErrorMessage(String title, String message)
  {
    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
  }

  public static void main(String[] argv) throws CertificateException, IOException
  {
    new ClientGUI();
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
		
  }

  public class ClientListener extends Thread
  {
    public void run()
    {
      while(true)
      {
        System.out.print("");
        if(client.hasNewMessage())
        {
          try {
      			refreshArchivePanel();
		    } catch (ClassNotFoundException | IOException e) {
			showErrorMessage("Problem refreshing archive", "Could not refresh the archive with the new message");
		  }
        }
      }
      
    }
  } 

}