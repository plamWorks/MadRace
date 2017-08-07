import java.awt.Rectangle;
@SuppressWarnings("serial")
public class Car extends Rectangle{
	public static int carWidth = 40;
	public static int carHeight = 70;
	private static int carInitialY = (GameBoard.boardHeight-carHeight-50);
	private static int MAXIMUM_RIGHT = GameBoard.boardWidth-carWidth;
	private int xSpeed =0;
	public Car(int x)
	{
		super(x,carInitialY,carWidth,carHeight);
	}
	 public void move(int pressedCode,int keyHeld)
	{
		if(keyHeld==0)
		{
			xSpeed=0;
		}
		else
		{
			if(pressedCode==65 && keyHeld==-1)
			{
				
				xSpeed=-5;
			}
			else if(pressedCode==68 && keyHeld==1)
			{				
				xSpeed=5;
			}
			
			if(   (x+xSpeed)>0    &&   (x+xSpeed)  < MAXIMUM_RIGHT-15)
			{
			x+=xSpeed;
			}
		}
		}
	}

