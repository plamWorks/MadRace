import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenForButton implements ActionListener
{
	public boolean flag;
	public void actionPerformed(ActionEvent act) {
		new GameBoard();
		flag=true;
	}
}