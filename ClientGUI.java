import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ClientGUI extends JFrame implements ActionListener 
{

  JTextField userID, pass, title;
  JTextArea message;
  JButton sendButton;
  MessageManager mm;
  JPanel messageCreationPanel, messageArchivePanel;
  JTabbedPane tabPane;

  ClientGUI()
  {
      init();
  }

  public void init()
  {
    mm= new MessageManager();
    initMessageCreationPanel();
    initMessageArchivePanel();
    tabPane = new JTabbedPane();
    tabPane.add("Create Message", messageCreationPanel);
    tabPane.add("Message Archive", messageArchivePanel);
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
    message = new JTextArea(200, 200);
    midPanel.add(message);
    messageCreationPanel.add(midPanel, BorderLayout.CENTER);

    JPanel botPanel = new JPanel (new GridLayout(1,2));
    sendButton = new JButton("Send Message");
    sendButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e)
            {
                sendButtonAction();
            }});
    botPanel.add(sendButton);
    messageCreationPanel.add(botPanel, BorderLayout.SOUTH);
  }

  //initiate the message archive panel
  private void initMessageArchivePanel()
  {
    messageArchivePanel = new JPanel(new GridLayout(0,1));
    ArrayList <Message> messages = mm.getDecryptedMessages();
    for (int i = 0; i < messages.size(); i++)
    {
      messageArchivePanel.add(createMessagePanel(messages.get(i)));
    }
  }

  //refreshes the archive panel with new messages
  private void refreshArchivePanel()
  {
    tabPane.remove(messageArchivePanel);
    initMessageArchivePanel();
    tabPane.add("Message Archive",messageArchivePanel);
  }

  //create a panel with the information of a single message
  private JPanel createMessagePanel(Message m)
  {
    JPanel main = new JPanel(new BorderLayout());
    JPanel northPanel = new JPanel(new GridLayout(3,1));
    northPanel.add(new JLabel(m.getSender()));
    northPanel.add(new JLabel(m.getTimestamp()));
    northPanel.add(new JLabel(m.getTitle()));
    main.add(northPanel, BorderLayout.NORTH);
    JPanel midPanel = new JPanel(new GridLayout(1,1));
    midPanel.add(new JLabel(m.getText()));
    main.add(midPanel);
    return main;
  }

  //sends a message and refreshes the archive panel to show the message
  private void sendButtonAction()
  {
    if (checkBoxesFilled() == true)
    {
      mm.addMessage(userID.getText(), title.getText(), message.getText());
    }
    refreshArchivePanel();
  }

  //checks if any of the boxes are empty, returns false if so
  public boolean checkBoxesFilled()
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