
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Font;
import javax.imageio.ImageIO;


public class Board extends Canvas implements KeyListener, Runnable
{

	private Catcher catches;
	private ArrayList <Bomb> ast;
	private int chance = 970000;

	private ArrayList<Fruit> frt;
	private ArrayList<Heart> hrt;
	
	private Random rand;
	private int lives;
	private int score;
	
	private Background b;
	private int game;
	
	private long start;

	private boolean[] keys;
	private BufferedImage back;
	
	public Board()
	{
		setBackground(Color.BLACK);

		keys = new boolean[6];
		
		rand = new Random();
		catches = new Catcher(500,400,3);
		ast = new ArrayList<Bomb>();
		
		frt = new ArrayList<Fruit>();
		hrt = new ArrayList<Heart>();
		
		lives = 3;
		
		score = 0;
		b = new Background(0);
		game = 0;
		start = System.currentTimeMillis();

		this.addKeyListener(this);
		new Thread(this).start();

		setVisible(true);
	}

 public void update(Graphics window)
 {
	   paint(window);
 }

	public void paint( Graphics window )
	{
		Graphics2D twoDGraph = (Graphics2D)window;
		if(back==null)
		   back = (BufferedImage)(createImage(getWidth(),getHeight()));
		Graphics graphToBack = back.createGraphics();
		
		
		if(game == 0){
			b = new Background(0);
			b.draw(graphToBack);
			System.out.println("test");
			if (keys[5] == true){
				game = 1;
			}
		}

		else if (game == 1){
			b = new Background(1);
			b.draw(graphToBack);
			graphToBack.setColor(Color.BLACK);
			graphToBack.drawString("FRUIT CATCHER ", 25, 50 );
			graphToBack.drawString("LIVES: " + lives,25,70);
			graphToBack.drawString("SCORE: " + score,25,110);

	
			catches.draw(graphToBack);
			
			//BOMB GENERATION
			long current = System.currentTimeMillis();
			
			if(((current - start) % 1000 == 1) && chance > 500000){
				chance = chance - 500;
			}
			
			
			int r = (int)(Math.random() * 800);
			int go = rand.nextInt(1000000);
			
			int aspd = 2 + rand.nextInt(2);
			if (go > chance){
				ast.add(new Bomb(r,0,aspd));
			}		

			for(int i = 0; i < ast.size(); i++){
				if (lives == 0){
					break;
				}
				ast.get(i).draw(graphToBack);
				if (catches.cTop(ast.get(i)) || catches.cBottom(ast.get(i)) || catches.cRight(ast.get(i)) || catches.cLeft(ast.get(i))){
					ast.get(i).setPos(1000, 1000);
					lives--;
				}
			}
			
			
			
			//HEART GENERATION
			int t = (int)(Math.random() * 800);
			int gohrt = rand.nextInt(1000000);
			if (gohrt > 999000){
				hrt.add(new Heart(t,0,5));
			}
			for(int i = 0; i < hrt.size(); i++){
				if (lives == 0){
					break;
				}
				hrt.get(i).draw(graphToBack);
				if (catches.chTop(hrt.get(i)) || catches.chBottom(hrt.get(i))){
					hrt.get(i).setPos(1000, 1000);
					lives++;
				}
			}
		
//			//FRUIT GENERATION
			int o = (int)(Math.random() * 800);
			int gofrt = rand.nextInt(1000000);
			if (gofrt > 985000){
				frt.add(new Fruit(o,0,5));
			}
			for(int i = 0; i < frt.size(); i++){
				if (lives == 0){
					break;
				}
				frt.get(i).draw(graphToBack);
				if (catches.cfTop(frt.get(i)) || catches.cfBottom(frt.get(i))){
					frt.get(i).setPos(1000, 1000);
					score++;
				}
			}
	
		//KEEPING catches IN BOUNDS
		if (catches.getX() < 0){
			catches.setX(0);
		}
		if (catches.getX() > 760){
			catches.setX(760);
		}
		if (catches.getY() < 0){
			catches.setY(0);
		}
		if (catches.getY() > 545){
			catches.setY(545);
		}
		
		//CATCHER EXPLODED
		if (lives == 0){
			catches.explode();
		}
		
		//CATCHER MOVEMENT
		if(keys[1] == true)
		{
			catches.move("A");
			catches.move("A");
//			catches.move("A");
//			catches.move("A");
		}
		if(keys[3] == true)
		{
			catches.move("D");
			catches.move("D");
//			catches.move("D");
//			catches.move("D");
		}
		
		
		
		
		}
		twoDGraph.drawImage(back, null, 0, 0);
	}
	


	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			keys[0] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_A)
		{
			keys[1] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			keys[2] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D)
		{
			keys[3] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			keys[4] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keys[5] = true;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_W)
		{
			keys[0] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_A)
		{
			keys[1] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_S)
		{
			keys[2] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_D)
		{
			keys[3] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keys[5] = false;
		}
		repaint();
	}

	public void keyTyped(KeyEvent e) { }
	

 public void run()
 {
 	try
 	{
 		while(true)
 		{
 		   Thread.currentThread().sleep(5);
          repaint();
       }
    }catch(Exception e)
    {
    }
	}
}
