package jggames;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

public class Level{
	
	public ArrayList<Entity> Allies = new ArrayList<Entity>();
	public ArrayList<Entity> Enemies = new ArrayList<Entity>();
	
	public ArrayList<Entity> Entities = new ArrayList<Entity>();
	
	
	public ArrayList<Bullet> bulletAmount = new ArrayList<Bullet>();
	
	public Entity player;
	
	Collisions collisionManager = new Collisions();
	
	private ImageLoader il;
	private BufferedImage bgImage;
	private AudioPlayer p = new AudioPlayer();
	
	private Flag ff = new Flag(500, 1000, 400);
	private int flagNum;
	private Flag[] flags;
	private int allyPoints = 0;
	private int enemyPoints = 0;
	
	private boolean colliding;
	
	public boolean lose;
	public boolean win;
	
	public DeltaTimer timer = new DeltaTimer();
	
	
	public CaptureTheFlag ctf = new CaptureTheFlag();
	
	
	private Vector[] testPatrol = new Vector[3];
	public Level(){
		
		il = new ImageLoader();
		
		il.loadImg("res/images/field.png");
		bgImage = il.img;
		
		flagNum = 5;
		
		flags = new Flag[flagNum];
		
		for(int i = 0; i < flagNum; i++){
			flags[i] = new Flag(300 * i*20, 300 * i*2, 250);
		}
		
		//Set these as functions for create ally and enemy
		for(int i = 0; i < 15; i++){
			Entity e = new Entity(i*100, 0, 25);
			e.setColor(new Color(0, 0, 255));
			e.setType("ally");
			e.setWeapon(new Weapon(new Vector(0,0), 10));
			e.getWeapon().setRPM(1000);
			
			if(i == 0){
				e.setColor(new Color(255, 255, 0));
				e.getWeapon().setRPM(600);
				e.setPlayer(true);
				player = e;
			}
			
			//c.destination = new Vector(i*100, 1000);
			Allies.add(e);
		}
		for(int i = 0; i < 15; i++){
			Entity e = new Entity(i*100, 2500, 25);
			e.setColor(new Color(255, 0, 0));
			e.setType("enemy");
			e.setWeapon(new Weapon(new Vector(0,0), 10));
			e.getWeapon().setRPM(1000);
			
			//c.destination = new Vector(i*100, 1000);
			Enemies.add(e);
		}/* 
		Allies.get(2).player = player;
		Allies.get(2).setSpeed(4.5);
		Allies.get(2).shouldFollowPlayer = true; */
		Entities.addAll(Allies);
		Entities.addAll(Enemies);
		//makePatrol();
	}
	public void update(){
		
		//updatePoints();
		
		
		bulletAmount.clear();
		
		//updateFlags();
		
		for(int i = 0; i < Entities.size(); i++){
			
			bulletAmount.addAll(Entities.get(i).getWeapon().getBulletList());
			
			for(int j = 0; j < bulletAmount.size();j++){
				
				if(bulletAmount.size() > 0){
					collide2(bulletAmount.get(j));
					
				}
			}
			
			for(int j = 0; j < Entities.size(); j++){
			
				Entities.get(i).detectHostile(Entities.get(j));	
			}
			
			
			
			ctf.update(Entities.get(i));
			Entities.get(i).update();
		}
		//checkWin();
	}
	public void draw(Graphics2D g2){
		
		g2.drawImage(bgImage,0,0,null);
		
		//drawFlags(g2);
		ctf.drawUnder(g2);
		
		for(int i = 0; i < Entities.size(); i++){
			Entities.get(i).draw(g2);
		}
		ctf.drawOver(g2);
	}
	public void collide2(Bullet b){
		
		for(int i = 0; i < Entities.size(); i++){
		/* 	
			if(b.getPosition().x + 200 < Entities.get(i).getPosition().x || b.getPosition().x - 200 > Entities.get(i).getPosition().x){
				return;
			} */
			
			if(collisionManager.colliding(Entities.get(i), b) && !Entities.get(i).getDead() && !b.exhausted){
				b.exhausted = true;
				Entities.get(i).setHealth(Entities.get(i).getHealth() - 5);
				//p.playSound("././res/audio/death.wav");
				colliding = true;
				
			}
		}
	}
	public void makePatrol(){
		for(int i = 0; i < 3; i++){
			testPatrol[i] = new Vector(Math.random()*1920,Math.random()*1080);
		}
	}
	public void checkWin(){
		int dead = 0;
		
		for(int i = 0; i < Allies.size(); i++){
			if(Allies.get(i).getDead()){
				dead++;
			}
			if(dead > Allies.size() - 1){
				lose = true;
			}
		}
		dead = 0;
		for(int i = 0; i < Enemies.size(); i++){
			if(Enemies.get(i).getDead()){
				dead++;
			}
			if(dead > Enemies.size() - 1){
				win = true;
			}
		}
	}
	public void updateEntities(){}
	public void updateFlags(){

	
		int allyNum = 0;
		int enemyNum = 0;
	
		for(int i = 0; i < flagNum; i++){
			flags[i].update();
			
			
			if(flags[i].getDominance() == "ally"){
				allyNum++;
			}	
			if(flags[i].getDominance() == "enemy"){
				enemyNum++;
			}
			
			
			for(int j = 0; j < Entities.size(); j++){
					
				flags[i].entityInFlag(Entities.get(j));
				
			
				if(!Entities.get(j).getPlayer()){
					if(Entities.get(j).getType() == "ally"){
					
						Entities.get(j).moveTo(flags[allyNum].getPosition());		
					}
					
					if(Entities.get(j).getType() == "enemy"){
					
						Entities.get(j).moveTo(flags[enemyNum].getPosition());		
					}
				}
				
				
				/* if((GameMath.getDistanceFrom(flags[i].getPosition(), Entities.get(j))) > flags[i].getRadius()){
					
					if(flags[i].getDominance() == "enemy" && Entities.get(j).getType() == "enemy"){
						return;
					}
					
					
				
				} */
				
			}
			
		}
	}
	public void drawFlags(Graphics2D g2){
		for(int i = 0; i < flagNum; i++){
			flags[i].draw(g2);
		}
	}
	public void updatePoints(){
		
		if(ff.getDominance() == "ally"){
			allyPoints++;
		}
		if(ff.getDominance() == "enemy"){
			enemyPoints++;
		}
		
		
		if(allyPoints > 1000){
			win = true;
		}
		if(enemyPoints > 1000){
			lose = true;
		}
		
	}

}






	
	/* 
	Circle[] friendCircleList = new Circle[10];
	
	public ArrayList<Circle> Allies = new ArrayList<Circle>();
	public ArrayList<Circle> Enemies = new ArrayList<Circle>();
	
	
	public ArrayList<Bullet> bulletAmount = new ArrayList<Bullet>();
	
	ImageLoader il;
	BufferedImage bgImage;
	Collisions collisionManager = new Collisions();
	
	private AudioPlayer p = new AudioPlayer();
	
	boolean colliding = false;
	
	public Level(){
		
		il = new ImageLoader();
		
		il.loadImg("res/images/TEST_MAP.png");
		bgImage = il.img;
		
		
		for(int i = 0; i < 10; i++){
			Circle c = new Circle(i*100, 0, 50);
			c.color = new Color(0, 0, 255);
			
			c.destination = new Vector(i*100, 1000);
			Allies.add(c);
		}
		
		for(int i = 0; i < friendCircleList.length; i++){
			friendCircleList[i] = new Circle(i*100,i*100,50);
		}
		
			friendCircleList[8].destination = new Vector(3000, 1000);
	}
	
	public void draw(Graphics2D g2){
		
		g2.drawImage(bgImage,0,0,null);
		
		for(int i = 0; i < Allies.size(); i++){
			Allies.get(i).drawCircle(g2);
		}
		
		for(int i = 0; i < friendCircleList.length; i++){
			friendCircleList[i].drawCircle(g2);
		}
		
	}
	public void update(){
		
		for(int i = 0; i < Allies.size(); i++){
			if(Allies.get(i).reachedDestination){
				Allies.get(i).destination = new Vector(i*100, -500);
			}
			
			for(int j = 0; j < friendCircleList[i].bullets.size(); j++){
				collide2(friendCircleList[i].bullets.get(j));
			}
			
			Allies.get(i).update();
		}
		
		for(int i = 0; i < friendCircleList.length; i++){
			for(int j = 0; j < friendCircleList[i].bullets.size(); j++){
				
				testCollide(friendCircleList[i].bullets.get(j));
				
				if(colliding){
					colliding = false;
					friendCircleList[i].bullets.remove(j);
				}
			}
			friendCircleList[i].moveAtAngle(0);
			
			friendCircleList[i].update();
		}
			friendCircleList[8].goToDestination();
	}
	public void testCollide(Bullet b){
		
		for(int i = 0; i < friendCircleList.length; i++){
			if(collisionManager.colliding(friendCircleList[i], b) && !friendCircleList[i].isDead){
				friendCircleList[i].isDead = true;
				p.playSound("././res/audio/death.wav");
				colliding = true;
			}
		}
	}
	public void collide2(Bullet b){
		
		for(int i = 0; i < Allies.size(); i++){
			if(collisionManager.colliding(Allies.get(i), b) && !Allies.get(i).isDead){
				Allies.get(i).isDead = true;
				p.playSound("././res/audio/death.wav");
				colliding = true;
			}
		}
		
	} */





/* 

			for(int j = 0; j < friendCircleList.length;j++){
				if(friendCircleList[5].bullets.size() > 0 && collisionManager.colliding(friendCircleList[i],friendCircleList[5].bullets.get(0))){
					if(friendCircleList[i].isDead){
						break;
					}
					friendCircleList[i].isDead = true;
					friendCircleList[5].bullets.remove(0);
				}
			}

 */