import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Snake implements ActionListener {
	
	class Coordinate{
		public int x,y;
		Coordinate(int a, int b){
			this.x = a;
			this.y = b;
		}
	}
	
	final static int WIDTH = 30;
	final static int HEIGHT = 20;
	final static int BLOCK_SIZE = 16;
	char direction = 'L';
	Coordinate snakeHead = new Coordinate(WIDTH/2,HEIGHT/2);
	Coordinate map[] = new Coordinate[WIDTH*HEIGHT];
	Timer tmr = new Timer(50,this);
	
	JFrame win = new JFrame();
	
	JPanel panl = new JPanel(){
		@Override
		public void paintComponent(Graphics drawr){
			int x,y;
			for(int i=0;i<Snake.WIDTH*Snake.HEIGHT;i++)
			{
				x = (i%Snake.WIDTH)*BLOCK_SIZE;
				y = (i/Snake.WIDTH)*BLOCK_SIZE;
				//System.out.println(Snake.WIDTH+","+x+","+y);
				if(map[i].x == -1 && map[i].x == -1)
					drawr.setColor(Color.black);
				else
					drawr.setColor(Color.yellow);
				drawr.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
			}
			//System.out.println("//");
		}
	};
	
	KeyAdapter key = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("OK");
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				if(direction != 'R')
					direction = 'L';
				break;
			case KeyEvent.VK_D:
				if(direction != 'L')
					direction = 'R';
				break;
			case KeyEvent.VK_W:
				if(direction != 'D')
					direction = 'U';
				break;
			case KeyEvent.VK_S:
				if(direction != 'U')
					direction = 'D';
				break;
			}
		}
	};
	
	Snake()
	{
		for(int i=0;i<WIDTH*HEIGHT;i++)
		{
			map[i] = new Coordinate(-1,-1);
		}
		
		System.out.println(WIDTH*HEIGHT);
		
		panl.setPreferredSize(new Dimension(WIDTH*BLOCK_SIZE,HEIGHT*BLOCK_SIZE));
		panl.addKeyListener(key);
		
		win.add(panl);
		
		win.pack();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
		tmr.start();
	}
	
	public static void main(String[] args) {
		new Snake();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		panl.repaint();
		map[snakeHead.x+snakeHead.y*HEIGHT] = new Coordinate(-1, -1);
		switch (direction) {
		case 'L':
			snakeHead.x++;
			break;
		case 'R':
			snakeHead.x--;
			break;
		case 'D':
			snakeHead.y++;
			break;
		case 'U':
			snakeHead.y--;
			break;
		}
		map[snakeHead.x+snakeHead.y*HEIGHT] = new Coordinate(0, 0);
		
	}
}