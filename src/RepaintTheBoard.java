public class RepaintTheBoard implements Runnable{

	GameBoard brd;
	RepaintTheBoard(GameBoard brdparam)
	{
		brd=brdparam;
	}
	public void run() {
			if(brd.check())
			{
				brd.repaint();	
			}
			else
			{
				brd.paintEndScreen();	
				return;
			}		
	}
	
}