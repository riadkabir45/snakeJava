import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

class Snake implements ActionListener
{
	//Window Properties
	JFrame window;
	JPanel panl,gameOver;
	JLabel gameinfo;
	Color purple;
	Color lYellow;
	Font fnt;
	final static int WIDTH = 30;
	final static int HEIGHT = 20;
	final static int BLOCK_SIZE = 16;
	final static int TICK_TIME = 200;
	
	//Snake Properties
	LinkedList<Integer> snakeX;
	LinkedList<Integer> snakeY;
	int foodX,foodY;
	char angle = 'R';
	boolean justAte = false;
	
	//Mechanical items
	KeyAdapter key;//Handle imputs
	Random random;
	Timer tmr;//The tick generator(It generates event after specific time to create a tick)
	
	Snake()
	{
		//Initializing the variables
		window = new JFrame();
		gameinfo = new JLabel();
		gameOver = new JPanel();
		purple = new Color(123,50,250);
		lYellow = new Color(255,220,0);
		panl = new JPanel()
		{
			private static final long serialVersionUID = 7947846712684328646L;//Currently I don't know what the hack this is(Its skip-able,Just gives warning)

			public void paintComponent(Graphics drawr)//The drawing tool(Called every time the panel has to refresh)
			{
				drawr.setColor(purple);
				drawr.fillRect(0, 0, Snake.WIDTH*BLOCK_SIZE, Snake.HEIGHT*BLOCK_SIZE);
				drawr.setColor(Color.red);
				drawr.fillRect(foodX*BLOCK_SIZE, foodY*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				drawr.setColor(Color.YELLOW);
				for(int i=0;i<snakeX.size();i++)
				{
					drawr.fillRect(snakeX.get(i)*BLOCK_SIZE, snakeY.get(i)*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
					if(i == 0)
						drawr.setColor(lYellow);
				}
			}
		};
		key = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(angle != 'R')
						angle = 'L';
					break;
				case KeyEvent.VK_RIGHT:
					if(angle != 'L')
						angle = 'R';
					break;
				case KeyEvent.VK_UP:
					if(angle != 'D')
						angle = 'U';
					break;
				case KeyEvent.VK_DOWN:
					if(angle != 'U')
						angle = 'D';
					break;
				}
			}
		};
		snakeX = new LinkedList<Integer>();
		snakeY = new LinkedList<Integer>();
		random = new Random();
		tmr = new Timer(TICK_TIME,this);
		
		//Setting components properties
		fnt = gameinfo.getFont();
		fnt = fnt.deriveFont(Font.BOLD,BLOCK_SIZE);
		panl.setPreferredSize(new Dimension(WIDTH*BLOCK_SIZE,HEIGHT*BLOCK_SIZE));
		gameinfo.setPreferredSize(new Dimension(WIDTH*BLOCK_SIZE,HEIGHT*BLOCK_SIZE));
		gameinfo.setHorizontalAlignment(JLabel.CENTER);
		gameinfo.setFont(fnt);
		gameOver.setPreferredSize(new Dimension(WIDTH*BLOCK_SIZE,HEIGHT*BLOCK_SIZE));
		gameOver.setBackground(purple);
		gameOver.add(gameinfo);
		
		//Setting window properties
		window.setTitle("Snake");
		window.add(panl);
		window.addKeyListener(key);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		//Initializing the snake body and food
		snakeAdd(3,5);
		lengthen(2);
		createFood();
		
		tmr.start();//Starting the tick
	}
	
	public static void main(String[] args)
	{
		new Snake();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)//This emulates the loop using the timer
	{
		for(int i=1;i<snakeX.size();i++)
			if(snakeX.get(0) == snakeX.get(i) && snakeY.get(0) == snakeY.get(i))
				endGame();
		if(snakeX.get(0) == foodX && snakeY.get(0) == foodY)
			justAte = true;
		switch(angle)
		{
			case 'U':
				if(snakeY.get(0)-1 >= 0)
					snakeAdd(snakeX.get(0),snakeY.get(0)-1);
				else
					snakeAdd(snakeX.get(0),snakeY.get(0)-1+HEIGHT);
				break;
			case 'D':
				if(snakeY.get(0)+1 < HEIGHT)
					snakeAdd(snakeX.get(0),snakeY.get(0)+1);
				else
					snakeAdd(snakeX.get(0),snakeY.get(0)+1-HEIGHT);
				break;
			case 'L':
				if(snakeX.get(0)-1 >= 0)
					snakeAdd(snakeX.get(0)-1,snakeY.get(0));
				else
					snakeAdd(snakeX.get(0)-1+WIDTH,snakeY.get(0));
				break;
			case 'R':
				if(snakeX.get(0)+1 < WIDTH)
					snakeAdd(snakeX.get(0)+1,snakeY.get(0));
				else
					snakeAdd(snakeX.get(0)+1-WIDTH,snakeY.get(0));
				break;
		}
		if(!justAte)
			snakeRem();
		else
			createFood();
		panl.repaint();
	}
	
	 public void createFood()
	{
		 boolean ok;
		 while(true) 
		{
				ok = true;
			 	foodX = random.nextInt(WIDTH-1);
				foodY = random.nextInt(HEIGHT-1);
				for(int i=0;i<snakeX.size();i++)
					if(snakeX.get(i) == foodX && snakeY.get(i) == foodY)
						ok = false;
				if(ok)
					break;
		}
		justAte = false;
	}
	 
	 public void lengthen(int n)//Makes the snake n block longer
	 {
		 for(int i=0;i<n;i++)
		 {
			 justAte = true;
			 actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		 }
	 }
	 
	 public void endGame()//END_GAME!!!!!!!!!
	 {
		 tmr.stop();
		 window.remove(panl);
		 gameinfo.setText("Game Over. Score:"+(snakeX.size()-1));
		 gameOver.repaint();
		 window.add(gameOver);
		 window.pack();
		 window.repaint();
		 
	 }
	
	//Simulates the snake movement by extending the head and cutting the tail
	public void snakeAdd(int a,int b)
	{
		snakeX.addFirst(a);
		snakeY.addFirst(b);
	}
	
	public void snakeRem()
	{
		snakeX.removeLast();
		snakeY.removeLast();
	}
}