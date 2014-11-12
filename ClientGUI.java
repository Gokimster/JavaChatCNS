import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ClientGUI extends JFrame implements ActionListener 
{

  JTextField userID, pass, title, authorSearch, titleSearch;
  JTextArea message;
  JButton sendButton, searchButton;
  //to be removed after linking with client server
  MessageManager mm;

  Client client;
  JPanel messageCreationPanel, messageArchivePanel, messageSearchPanel;
  JTabbedPane tabPane;
  JScrollPane panelScroll, searchScroll;

  ClientGUI()
  {
      init();
  }

  public void init()
  {
    //message manager to be removed
    mm= new MessageManager();

    client = new Client();
    initMessageCreationPanel();
    initMessageSearchPanel();
    initMessageArchivePanel();
    tabPane = new JTabbedPane();
    tabPane.add("Create Message", messageCreationPanel);
    tabPane.add("Search for Messages", messageSearchPanel);
    tabPane.add("Message Archive", panelScroll);
    add(tabPane);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setBounds(500, 200, 500, 500);
    setVisible(true);
  }

  //initiate the message creation panel
  private void initMessageCreationPanel()
  {
    messageCreationPanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel(new GridLayout(3, 2));
    userID = new JTextField();
    pass = new JTextField();
    title = new JTextField();
    topPanel.add(new JLabel("UserID: "));
    topPanel.add(userID);
    topPanel.add(new JLabel("Password: "));
    topPanel.add(pass);
    topPanel.add(new JLabel("Message Title: "));
    topPanel.add(title);
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
            		System.out.println("Message coud not be sent");
            	}
            }});
    botPanel.add(sendButton);
    messageCreationPanel.add(botPanel, BorderLayout.SOUTH);
  }

  //initiate the message archive panel
  private void initMessageArchivePanel()
  {
    messageArchivePanel = new JPanel(new GridLayout(0,1));
    ArrayList <Message> messages = mm.getMessages();
    for (int i = messages.size() -1 ; i >= 0; i--)
    {
      JPanel oneMessage;
      if (i % 2 == 0)
      {
        oneMessage = createMessagePanel(messages.get(i), true);
      }
      else 
      {
        oneMessage = createMessagePanel(messages.get(i), false);
      }
      messageArchivePanel.add(oneMessage);
    }
    panelScroll = new JScrollPane(messageArchivePanel);
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
                searchButtonAction();
            }});
    botPanel.add(searchButton);
    messageSearchPanel.add(botPanel, BorderLayout.SOUTH);
  }

  //refreshes the archive panel with new messages
  private void refreshArchivePanel()
  {
    tabPane.remove(panelScroll);
    initMessageArchivePanel();
    tabPane.add("Message Archive",panelScroll);
  }

  private int refreshSearchPanel()
  {
    messageSearchPanel.remove(searchScroll);
    JPanel midPanel = new JPanel(new GridLayout(0,1));
    ArrayList <Message> messages = mm.getMessages();
    int counter = 0;
    for (int i = messages.size() -1 ; i >= 0; i--)
    {
      Message m = messages.get(i);
      if (m.getAuthor().equals(authorSearch.getText()))
      {
        if ((titleSearch.getText().equals("")) || (m.getTitle().equals(titleSearch.getText())))
        {
          if (++counter % 2 != 0)
            midPanel.add(createMessagePanel(m, false));
          else 
            midPanel.add(createMessagePanel(m, true));
        }
      }
      else
        if (authorSearch.getText().equals(""))
        {
          if (m.getTitle().equals(titleSearch.getText()))
          {
            if (++counter % 2 != 0)
              midPanel.add(createMessagePanel(m, false));
            else 
              midPanel.add(createMessagePanel(m, true));
          }
        }
    }
    searchScroll = new JScrollPane(midPanel);
    messageSearchPanel.add(searchScroll, BorderLayout.CENTER);
    messageSearchPanel.revalidate();
    messageSearchPanel.repaint();
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

  //sends a message and refreshes the archive panel to show the message
  private void sendButtonAction() throws IOException
  {
    if (checkMessageBoxesFilled() == true)
    {
      client.sendMessage(new Message(userID.getText(), title.getText(), message.getText()));
    }
    refreshArchivePanel();
  }

  private void searchButtonAction()
  {
    if (checkSearchBoxesFilled())
    {
      if (refreshSearchPanel() == 0)
      {
        showErrorMessage("No messages found", "We could not find a message with which fits the author and title provided!");
      }
    }
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
      else
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

  public static void main(String[] argv)
  {
    new ClientGUI();
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
		
  }

}