import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class GameDrawingPanel extends JComponent
{
	public ArrayList<Cop> cops= new ArrayList<Cop>();
	public Car car = new Car((GameBoard.boardWidth-Car.carWidth)/2);
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	Clock cl=new Clock();
	
	public GameDrawingPanel()
	{
		Random rand = new Random();
		for(int i=0;i<GameBoard.boardWidth/100;i++)
		{
			int startingX = (rand.nextInt(10) + 1)*GameBoard.boardWidth/10;
			if(startingX<200)
			{
				cops.add(new Cop(startingX,100));
			}
			else
			{
				cops.add(new Cop(startingX,5));
			}
			
		}
		
		cl.start();
	}
	public long getCurrentTime()
	{
		return cl.getElapsedTimeSecs();
	}
	public boolean intersect()
	{
		for(Cop c : cops)
		{
			if(c.intersects(car))
			{
				return true;
			}
		}
		return false;
	}
	synchronized public void paint(Graphics g) 
	{
		Graphics2D graphicSettings =(Graphics2D)g;
		graphicSettings.setColor(Color.PINK);
		graphicSettings.fillRect(0,0,getWidth(),getHeight());
		graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		graphicSettings.setPaint(Color.BLACK);
		String time = String.valueOf(cl.getElapsedTimeSecs());
		g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
		graphicSettings.drawString(time, 100, 100);
		int decide=0;
		Random rand = new Random();
		decide = rand.nextInt(100);
	
		if(decide<10)
		{
			int startingX = (rand.nextInt(10) + 1)*GameBoard.boardWidth/10;
			if(startingX<200)
			{
				cops.add(new Cop(startingX,100));
			}
			else
			{
				cops.add(new Cop(startingX,5));
			}
			
		}
		BufferedImage bi=null;
		try {
		bi = ImageIO.read(new File(".\\rsz_1pc.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Cop c : cops)
		{
			c.move();
			graphicSettings.drawImage(bi,c.x, c.y, null);
		}
		for(int i=0; i<cops.size(); i++)
		{	
			Cop temp = cops.get(i);
			if(temp.y>=GameBoard.boardHeight)
				cops.remove(temp);
		}
		car.move(GameBoard.pressedkeyCode,GameBoard.keyHeld);
		BufferedImage biCar=null;
		try {
		biCar = ImageIO.read(new File(".\\rsz_test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	graphicSettings.drawImage(biCar,car.x, car.y, null);
		}
	}