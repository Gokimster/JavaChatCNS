import java.util.ArrayList;

//test class for Messages

public class Test
{
	public static void main (String [] argv)
	{
		MessageManager mm = new MessageManager();
		mm.addMessage("tami", "test", "HEYOOOOO");
		mm.addMessage("inna", "test2", "SHUDUP TAMI");
		ArrayList <Message> messages = mm.getMessages();
		System.out.println(messages.get(0).getText());
		System.out.println(messages.get(1).getText());
	}
}