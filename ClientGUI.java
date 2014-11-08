import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame implements ActionListener 
{

  JTextField userID, pass, title;
  JTextArea message;
  JButton send;

  ClientGUI()
  {
      init();
  }

  public void init()
  {
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
    add(topPanel, BorderLayout.NORTH);

    JPanel midPanel = new JPanel (new GridLayout(1,1));
    message = new JTextArea();
    midPanel.add(message);
    add(midPanel, BorderLayout.CENTER);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(600, 600);
    setVisible(true);
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