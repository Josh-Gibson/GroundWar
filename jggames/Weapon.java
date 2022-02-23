package jggames;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;


public class Weapon{
	
	private String[] WEAPON_TYPE = {"AUTO", "SEMI_AUTO"};
	
	private Vector position;
	private double size;
	private double angle;
	private String type;
	
	private AudioPlayer player = new AudioPlayer();
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>(); //switch to weapon
	
	private ImageLoader il;
	private BufferedImage weaponImg;
	
	private AffineTransform transform = new AffineTransform();
	
	private boolean canShoot = true;
	private DeltaTimer timer;
	private int rpm;
	private long rateOfFireWait;
	
	public Weapon(Vector p, double s){
		position = p;
		size = s*2;
		
		il = new ImageLoader();
		
		il.loadImg("res/images/testweapon.png");
		weaponImg = il.img;
		
		timer = new DeltaTimer();
		rpm = 600;
		rateOfFireWait = 60000000000L / rpm;
	}
	public Weapon(){
		type = null;
	}
	public void update(){
		
		
		if(timer.isDone()){
			canShoot = true;
		}
		
		try{
			timer.waitTime(rateOfFireWait);
		}catch(Exception e){
			System.out.println(e);
		}
		
		for(int i = 0; i < bullets.size(); i++){
			
			if(bullets.get(i).exhausted){
				bullets.remove(bullets.get(i));
			}
			else{
				bullets.get(i).update();
			}
		}
		isOutOfRange();
	}
	public void draw(Graphics2D g2){
		
		//Thank you to tckmn for this wonderful trick!
		//https://stackoverflow.com/questions/14124593/how-to-rotate-graphics-in-java
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(g2);
		}
		
		AffineTransform old = g2.getTransform();
		
		g2.rotate(Math.toRadians(angle), position.x, position.y);
		g2.drawImage(weaponImg,(int)position.x, (int)position.y - weaponImg.getHeight()/2, null);
		g2.setTransform(old);
	}
	
	public void shoot(){
		
		if(!canShoot){
			return;
		}
		
		canShoot = false;
		Bullet b = new Bullet(new Vector( 
		(int)(position.x + weaponImg.getWidth()*Math.cos(Math.toRadians(angle))),
		(int)(position.y + weaponImg.getWidth()*Math.sin(Math.toRadians(angle)
		))));
		
		b.setAngle(Math.toRadians(angle));
		b.setSpeed(10);
		
		bullets.add(b);
	}
	
	public void isOutOfRange(){
		double x = 0;
		double y = 0;
		double distance = 0;
		
		for(int i = 0; i < bullets.size(); i++){
			
			 x = getPosition().x - bullets.get(i).getPosition().x;
			 y = getPosition().y - bullets.get(i).getPosition().y;
			 
			 
			 if(Math.abs(x) > 1500){
				 
			//	System.out.println("Bullet Removed");
				bullets.remove(bullets.get(i));
				return;
			 
			 }
			
			 else if(Math.abs(y) > 1500){
				 
			//	System.out.println("Bullet Removed");
				bullets.remove(bullets.get(i)); 
				
			 }
			/*  distance = Math.sqrt((x * x) + (y * y));
			
			if(distance > 5000){
				System.out.println("Bullet Removed");
				bullets.remove(bullets.get(i));
			} */
			
		}
		
	}
	
	public Bullet getBullets(){
		if(bullets.size() > 0){
			for(int i = 0; i < bullets.size(); i++){
				if(bullets.get(i).exhausted){
					return null;
				}
				return bullets.get(i);	
			}
			return null;
		}
		else{
			return null;
		}
	}
	public ArrayList getBulletList(){
		return bullets;
	}
	public Vector getPosition(){
		return position;
	}
	public void setPosition(Vector p){
		position = p;
	}
	public double getAngle(){
		return angle;
	}
	public void setAngle(double a){
		angle = a;
	}
	public int getRPM(){
		return rpm;
	}
	public void setRPM(int rate){
		rpm = rate;
		
		rateOfFireWait = 60000000000L / rpm;
	}
}