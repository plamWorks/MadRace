
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import javax.swing.*;


@SuppressWarnings("serial")
public class GameBoard extends JFrame {
	public static int boardWidth = 1000;
	public static int boardHeight = 1000;
	public static int keyHeld=0;
	public static int  pressedkeyCode = 0;
	public static boolean running=true;
	ListenForButton lforbutton1;
	ListenForButton2 lforbutton2;
	GameDrawingPanel gamePanel;
	ScheduledThreadPoolExecutor exec;
	public static void main (String[] args)
	{
		new GameBoard();
	}
	public boolean check()
	{
		return(!gamePanel.intersect());
	}
	public long getTime()
	{
		return gamePanel.getCurrentTime();
	}
	GameBoard()
	{
		this.setSize(boardWidth,boardHeight);
		this.setTitle("Java mad racers");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyListener(){

			@Override
			public void keyReleased(KeyEvent arg0) {
				keyHeld=0;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 65)
				{
				//	System.out.println("LEFT");
					pressedkeyCode = 65;
					keyHeld=-1;
				}
				else if(e.getKeyCode() == 68)
				{
				//	System.out.println("RIGHT");
					pressedkeyCode = 68;
					keyHeld=1;
				}
				
				
			}

	

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
			gamePanel = new GameDrawingPanel();
			gamePanel.setLayout(null);
			this.add(gamePanel,BorderLayout.CENTER);
			exec = new ScheduledThreadPoolExecutor(8);
			exec.scheduleAtFixedRate(new RepaintTheBoard(this),0L,15L,TimeUnit.MILLISECONDS);	
			this.setVisible(true);
	}
	public void paintEndScreen()
	{
		gamePanel.setVisible(false);
		exec.shutdown();
		System.out.print("the end");
		try {
			Scanner s = new Scanner(new File("best.txt"));
			int bestResult = s.nextInt();
			s.close();
	
			if((int)this.getTime()> bestResult)
			{
				bestResult= (int)this.getTime();
			}
			
			JPanel jpj = new JPanel();
			jpj.setBackground(Color.PINK);
			jpj.setSize(GameBoard.boardWidth,GameBoard.boardHeight);
			jpj.setLayout(null);
			
			
			JLabel jlb = new JLabel("GAME OVER! TIME ENDURED: "+this.getTime() + " secs");
			jlb.setBackground(Color.PINK);
			jlb.setFont(new Font("TimesRoman", Font.PLAIN, 40));
			jlb.setBounds(100,50,800,100);
			
			JLabel jlb2 = new JLabel("ALL TIME BEST: "+bestResult +  " secs");
			jlb2.setBackground(Color.PINK);
			jlb2.setFont(new Font("TimesRoman", Font.PLAIN, 40));
			jlb2.setBounds(230,150,800,100);
			
			JLabel jlb3 = new JLabel("Would you play a new game?");
			jlb3.setBackground(Color.PINK);
			jlb3.setFont(new Font("TimesRoman", Font.PLAIN, 40));	
			jlb3.setBounds(200,200,500,100);
			
			jpj.add(jlb);
			jpj.add(jlb2);
			jpj.add(jlb3);
			
			
			JButton newGameButton = new JButton("New Game");
			newGameButton.setContentAreaFilled(false);
			newGameButton.setBounds(250,300,200,100);
			
			lforbutton1 = new ListenForButton();
			newGameButton.addActionListener(lforbutton1);
			
			JButton closeButton = new JButton("Close");
			closeButton.setContentAreaFilled(false);
			closeButton.setBounds(500,300,200,100);
			
			lforbutton2 = new ListenForButton2();
			closeButton.addActionListener(lforbutton2);
			
			jpj.add(newGameButton);
			jpj.add(closeButton);
			
		
			this.add(jpj);
			this.setSize(boardWidth, boardHeight);
			
			this.setVisible(true);
		
			Formatter f = new Formatter(new File("best.txt"));
			f.format("%d", bestResult);
			f.close();
			
			if(this.lforbutton1.flag==true)
			{
				this.setVisible(false);
			}
			
		} catch (FileNotFoundException e) {
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class RepaintTheBoard implements Runnable{

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
class GameDrawingPanel extends JComponent
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
		bi = ImageIO.read(new File("C:\\Users\\c5262015\\eclipse-workspace\\TheGame\\rsz_1pc.png"));
		} catch (IOException e) {
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
		biCar = ImageIO.read(new File("C:\\Users\\c5262015\\eclipse-workspace\\TheGame\\rsz_test.png"));
		} catch (IOException e) {
		}
	graphicSettings.drawImage(biCar,car.x, car.y, null);
	/*
		graphicSettings.setColor(Color.BLUE);
		graphicSettings.fill(car);
		graphicSettings.draw(car);
		*/
		}
	}
class ListenForButton implements ActionListener
{
	public boolean flag;
	public void actionPerformed(ActionEvent act) {
		new GameBoard();
		flag=true;
	}
}
class ListenForButton2 implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent act) {
		System.exit(0);
		
	}
	
}