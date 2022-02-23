package jggames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import java.awt.Toolkit;
import java.awt.Robot;
import java.awt.Desktop;
import java.net.URI;



public class Game extends JPanel{

static final int WIDTH = 900;
static final int HEIGHT = 900;
static final int FPS = 120;
static final long UPDATES_NSEC = 1000000000L / FPS;
long updateTest = 0;
static enum GameState{MENU, PLAYING, PAUSED, WIN, GAMEOVER}	//control gamestate 

static GameState state;
public GamePanel canvas;
public Color DefaultGameColor;

private Entity player;
private Circle testCircle = new Circle(0, 0, 5);
private Circle mouseCircle = new Circle(0,0,5);

private Level level;
private Dimension screenSize;
private Robot robot;
int num = 0;

Circle currentTarget;
private boolean timerStart = false;
private long startTime;

private boolean canShoot = false;
private boolean shooting = false;
private DeltaTimer timer = new DeltaTimer();

private double rpm = 300;

private String gameEndMsg = "";

public Camera camera;

	public Game(){
		init();
		new Flag(5,5,5);
	}
	
	public void init(){	//Game setup.
		System.out.println("Game Launchezd");
		
		canvas = new GamePanel();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
      	canvas.setPreferredSize(screenSize);
		add(canvas);
		camera = new Camera(-500,-500);
		
		state = GameState.PLAYING;
		DefaultGameColor = new Color(50, 50, 50);
		
		
		//currentTarget = level.friendCircleList[0];
	 	try{
			robot = new Robot();
		}catch(Exception e){}
		/*
		try{
			Desktop.getDesktop().browse(new URI("http://www.google.com"));	
		}
		catch(Exception e){}
		robotTech(); */
		
		level = new Level();
		
		player = level.player;
		
		gameStart();
	 
	}
	public void gameStart(){	//start gameloop
	
		Thread thread = new Thread(){
			@Override
			
			public void run(){
				gameLoop();
			} 
		};
		thread.start();
	}
	public void gameLoop(){	//60FPS loop 
	
		long beginTime, elapsedTime, timeDifference;
		
		while(state != GameState.GAMEOVER){
			beginTime = System.nanoTime();
			if(state == GameState.PLAYING){
				gameUpdate();
				repaint();
			}
			
			elapsedTime = System.nanoTime() - beginTime;
			timeDifference = (UPDATES_NSEC - elapsedTime) / 1000000;  
			updateTest = timeDifference;
			if(timeDifference < 10){
				timeDifference = 10;
			}
			try{
				Thread.sleep(timeDifference);
			}
			catch(InterruptedException e){}
		}
	}
	public void gameUpdate(){ //physics update
		switch(state){
			case PLAYING:
				break;
			case GAMEOVER:
				return;
		}
		
		//System.out.println(player.getAngle());
		if(player.getDead()){
			gameEndMsg = "YOU DIED";
			//gameEnd();
		}
		if(shooting){
			player.shoot();
		}
		level.update();
	}
	public void gameDraw(Graphics2D g2){	//draw everything within here. 
		
		Dimension lastSize = canvas.getPreferredSize();
		
		if(canvas.getPreferredSize() != lastSize){
			canvas.setPreferredSize(screenSize);
		}
		
		if(level.lose){
			gameEndMsg = "YOU LOSE";
			gameEnd();
		}
		else if(level.win){
			gameEndMsg = "YOU WIN";
			gameEnd();
		}
		
		switch(state){
			case MENU:
				return;
			case PAUSED:
				return;
			case WIN:
				return;
			case PLAYING:
				break;
			case GAMEOVER:
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("Arial", Font.PLAIN, 100));
				g2.drawString(gameEndMsg , (int)getWidth()/2, (int)getHeight()/2);
		
				return;
		}
		
		setBackground(DefaultGameColor);
		
		camera.offsetX = canvas.getWidth()/2;
		camera.offsetY = this.getParent().getHeight()/2; //BUT WHY WORK??
		
		camera.update(g2);
		
		level.draw(g2);
		
		if(!player.getDead()){
		
			camera.setPosition(player.getPosition());
			player.instantAimAt(mouseCircle);
		//	System.out.println(player.getAngle());
				
		}
		
		testCircle.getPosition().x = camera.getPosition().x; //position camera dot. make it a crosshair or something
		testCircle.getPosition().y =  camera.getPosition().y;
		testCircle.setColor(Color.RED);
		testCircle.draw(g2);
		
		try{	
			if(this.getMousePosition() != null){ //FIX: MAY CRASH mouse pointer thing. make it invisible
				
				mouseCircle.getPosition().x = camera.getPosition().x - this.getParent().getWidth()/2 + this.getMousePosition().x - 2.5;
				mouseCircle.getPosition().y = camera.getPosition().y - this.getParent().getHeight()/2 + this.getMousePosition().y - 2.5;
				
				mouseCircle.setColor(Color.GREEN);
				mouseCircle.draw(g2);
		
			}
		}
		catch(NullPointerException e){
			System.out.println(e);
		}
		
		
		//level.draw(g2);
		/*
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.drawString("LevelTest" , 800, 20);
		
		g2.drawString(updateTest + "", 50, 30);
		
		
		
		testCircle.position.x = camera.position.x; //position camera dot. make it a crosshair or something
		testCircle.position.y =  camera.position.y;
		
		//robot.mouseMove(canvas.getWidth()/2, canvas.getHeight()/2);
		
		mouseCircle.color = Color.GREEN;
		mouseCircle.drawCircle(g2);
		
		
		
		level.friendCircleList[8].aimAt(currentTarget);
		level.friendCircleList[5].aimAt(mouseCircle);
		level.friendCircleList[5].goToDestination();
		
		
		if(currentTarget.position.x > level.friendCircleList[8].position.x - 5 && !currentTarget.isDead){
			if(level.friendCircleList[8].bullets.size() < 4){
			
				level.friendCircleList[8].shoot();
			
			}
		}
		
		if(currentTarget.isDead){
			if(num < level.friendCircleList.length -2){
				num++;
				if(num == 8){
					num++;
				}
				currentTarget = level.friendCircleList[num];
			}
		}
		
		g2.setColor(Color.BLACK);
		g2.drawLine((int)camera.position.x - 30, (int)camera.position.y, (int)camera.position.x + 30, (int)camera.position.y);
		g2.drawLine((int)camera.position.x, (int)camera.position.y - 30, (int)camera.position.x, (int)camera.position.y + 30);

	
	public void timeTest(){
		
		if(!timerStart){
			startTime = System.nanoTime();
			timerStart = true;
		}
		
		if((System.nanoTime() - startTime) > 60000000000L / rpm){
			timerStart = false;
			canShoot = true;
		}
	}

		*/
	}
	 
	public void gameEnd(){
		
		state = GameState.GAMEOVER;
		
	}
	
	
	public void robotTech(){
		
		robot.delay(5000);
		robot.keyPress(KeyEvent.VK_H);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_E);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_L);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_L);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_O);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_ENTER);	
		robot.delay(1000);
	}
	
	class GamePanel extends JPanel implements KeyListener, MouseListener, MouseWheelListener{	//Key Listener
		public GamePanel(){
			setFocusable(true);
			requestFocus();
			addKeyListener(this);
			addMouseListener(this);
			addMouseWheelListener(this);
		}
		@Override
		public void paintComponent(Graphics g){

			Graphics2D g2 = (Graphics2D) g;
			gameDraw(g2);
		}
		@Override
		public void keyPressed(KeyEvent e){
			
			if(e.getKeyCode() == KeyEvent.VK_Q){
				gameEnd();
			}
			if(e.getKeyCode() == KeyEvent.VK_W){
				camera.up = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_A){
				camera.left = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				camera.down = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_D){
				camera.right = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				
				if(state == GameState.PAUSED){
					state = GameState.PLAYING;
					return;
				}
				state = GameState.PAUSED;
			}
		}
		@Override
		public void keyReleased(KeyEvent e){
			
			if(e.getKeyCode() == KeyEvent.VK_W){
				camera.up = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_A){
				camera.left = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				camera.down = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_D){
				camera.right = false;
			}
			
		}
		@Override
		public void keyTyped(KeyEvent e){}
		
		@Override
		public void mousePressed(MouseEvent e){
			if(e.getButton() == MouseEvent.BUTTON1){	
				shooting = true;
				//player.shoot();
			}
				/* 
				Circle c = new Circle(camera.position.x - camera.offsetX + this.getParent().getMousePosition().x - 50, camera.position.y - camera.offsetY + this.getParent().getMousePosition().y - 50, 50);
				c.vel.x = Math.random();
				c.vel.y = -Math.random();
				circleArray.add(c);
				num++;
				 
			 
			for(int i = 0; i < level.friendCircleList.length; i++){
				level.friendCircleList[5].destination = new Vector(mouseCircle.position.x, mouseCircle.position.y);
				
			}
			*/
		}
		@Override
		public void mouseReleased(MouseEvent e){
			shooting = false;
		}
		@Override
		public void mouseEntered(MouseEvent e){}
		@Override
		public void mouseExited(MouseEvent e){}
		@Override
		public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e){
			 if (e.getWheelRotation() < 0) {
				 camera.camSpeed ++;
			}
			else{
				camera.camSpeed--;
			}
		}
	}
}
