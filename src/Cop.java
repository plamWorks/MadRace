import java.awt.*;
import java.util.Random;
@SuppressWarnings("serial")
public class Cop extends Rectangle{
	public static int copWidth = 40;
	public static int copHeight = 90;
	public static Random rand = new Random();
	public Cop(int a,int b)
	{
		super(a,b,copWidth,copHeight);
	}
	public void move() 
	{
		y+=5;
	}
}

