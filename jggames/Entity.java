package jggames;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;  
import java.util.ArrayList;

public class Entity extends Circle{ 
	
	private boolean isDead;
	private boolean isPlayer;
	private String type;
	private int health = 100;
	private Vector spawnpoint = new Vector(0,0);
	
	private double angle;
	
	private double speed = 1 + Math.random();
	private double turnSpeed = 2;
	
	public int behaviorID = (int)Math.abs(Math.random() * 3); //Random ID key to create unique entity behaviors
	
	private Weapon weapon;
	private Entity target;
	public boolean hasTarget;
	
	private Flag flagTarget;
	
	public boolean carryingFlag = false;
	
	private DeltaTimer timer = new DeltaTimer();
	private DeltaTimer gameDecisionTimer = new DeltaTimer();
	private boolean recoil = false;
	
	private Vector patrolPath[];
	private Vector patrolPoint;
	private int patrolNum = 0;
	
	private AudioPlayer audio = new AudioPlayer();
	
	public boolean shouldFollowPlayer = false;
	public Entity player;
	
	
	private Vector ran = new Vector(0,0);
	
	private boolean reachedDestination;
	
	public Entity(double x, double y, int radius){
		super(x, y, radius);
		
		ran = getPosition();
		
		weapon = new Weapon(new Vector(100,120), 20);
		
		startTimer();
	}
	
	@Override
	public void draw(Graphics2D g2){
		
		if(isDead){
			setColor(Color.BLACK);
		}
		
		weapon.draw(g2);
		drawAngleLine(g2);
		
		if(!isDead){
			
			g2.setColor(Color.BLACK);
			g2.drawRect((int)getPosition().x - 50, (int)getPosition().y -50, 100, 10);
			g2.setColor(Color.GREEN);
			g2.fillRect((int)getPosition().x - 50, (int)getPosition().y -50, health, 10);
			
		}
		
		super.draw(g2);
	}
	@Override
	public void update(){
		super.update();
		
		weapon.setAngle(getAngle());
		weapon.setPosition(getPosition());
		weapon.update();
		
		
		if(isDead){
			return;
		}
		if(health < 1){
			
			die();
		}
		if(shouldFollowPlayer){
			
			if(player == null){
				return;
			}
			follow(player);
		
		}
		else{
			
			//randomMovement();
			
		}
		
	}
	
	public void die(){
		if(isDead){
			return;
		}
		audio.playSound("././res/audio/death.wav");
		isDead = true;
	}
	public void respawn(Vector s){
		
		
		try{
			timer.waitTime(5000);
		}catch(Exception ee){
			System.out.println(ee);
		}
		
		if(!timer.isDone()){
			return;
		}
		health = 100;
		isDead = false;
		
		if(getType() == "ally"){
			setColor(Color.BLUE);
		}
		
		if(getType() == "enemy"){
			setColor(Color.RED);
		}
		
		setPosition(s);
	}
	
	public void shoot(){
		if(isDead){
			return;
		}
		weapon.shoot();
	}
	
	public void moveAtAngle(){
		getPosition().x +=  Math.cos(Math.toRadians(getAngle()));
		getPosition().y +=  Math.sin(Math.toRadians(getAngle()));
	}
	public void moveAtAngle(double angle){
		
		if(isDead){
			return;
		}
		
		//thank you Kevin
		//https://gamedev.stackexchange.com/questions/36046/how-do-i-make-an-entity-move-in-a-direction
		
		getPosition().x +=  Math.cos(Math.toRadians(angle));
		getPosition().y +=  Math.sin(Math.toRadians(angle));
		
	}
	public void drawAngleLine(Graphics2D g2){
		
		g2.setColor(Color.BLACK);
		
		g2.drawLine(
			(int)getPosition().x, 
			(int)getPosition().y, 
			(int)(getPosition().x + getRadius()*Math.cos(Math.toRadians(getAngle()))),  
			(int)(getPosition().y + getRadius()*Math.sin(Math.toRadians(getAngle()))));
	}
	
	
	public void aimAt(double a){
		if(isDead){
			return;
		}
		
		angle = a;
	}
	
	public double aimAt(Vector v){
		
		if(isDead){
			return angle;
		}
		
		double difAngle = 0; 
		double x = target.getPosition().x - getPosition().x;
		double y = target.getPosition().y - getPosition().y;
		
		difAngle = Math.toDegrees(Math.atan2(y,x));
		
		if(angle < difAngle){
			angle+=turnSpeed;
		}
		else if(angle > difAngle){
			angle-=turnSpeed;
		}
		
		if(angle < 0){
			angle +=360;
		}
		
		return angle;
		
	}
	public double aimAt(Circle target){
		
		if(isDead){
			return angle;
		}
		
		double difAngle = 0;
		double targetAngle = 0;
		double x = target.getPosition().x - getPosition().x;
		double y = target.getPosition().y - getPosition().y;
		
		difAngle = Math.toDegrees(Math.atan2(y,x));
		
		//angle = Math.toRadians(angle);
		
		//targetAngle =  (targetAngle < 0 ? -targetAngle : targetAngle);
		
		if(difAngle < 0){
			difAngle +=360;
		}
		if(angle < 0){
			angle +=360;
		}
		targetAngle = difAngle - angle;
		//System.out.println(targetAngle);
		
		if(Math.abs(targetAngle) > 270){
			angle = difAngle;
		}
		
		if(angle < difAngle){
			angle+=turnSpeed;
		}
		if(angle > difAngle){
			angle-=turnSpeed;
		}
		return angle;
		
	}
	public double instantAimAt(Circle target){
		if(isDead){
			return angle;
		}
		
		double x = target.getPosition().x - getPosition().x;
		double y = target.getPosition().y - getPosition().y;
		
		angle = Math.toDegrees(Math.atan2(y,x));
		
		if(angle < 0){
			angle +=360;
		}
		
		return angle;
	}
	public void moveTo(Vector v){
	
		double dx = v.x - getPosition().x;
		double dy = v.y - getPosition().y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		dx /= length;
		dy /= length;
	
		if(getDead()){
			return;
		}
		
		if(length > 5){
			getPosition().x += dx*speed;
			getPosition().y += dy*speed;
			reachedDestination = false;			
		}
		else{
			reachedDestination = true;
		}
	}
	public void moveTo(Entity e){
		
		if(e.getDead() || getDead()){
			return;
		}
		
		double dx = e.getPosition().x - getPosition().x;
		double dy = e.getPosition().y - getPosition().y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		dx /= length;
		dy /= length;
		
		if(length > 5){
			
			getPosition().x += dx * speed;
			getPosition().y += dy * speed;
		}
	}
	public void patrol(){
		
		if(patrolPath == null || patrolPath.length == 0 || isPlayer){
			return;
		}
		
		patrolPoint = patrolPath[patrolNum];
			
		if(reachedDestination){
			shoot();
			System.out.println("All clear here!" + patrolNum);
			patrolNum++;
				
			if(patrolNum > patrolPath.length -1){
				patrolNum = 0;
			}
			reachedDestination = false;
		}
		aimAt(patrolPoint);
		if(hasTarget == false){
			moveTo(patrolPoint);
		}
	}
	public void follow(Entity e){
		if(getDistanceFrom(player) > 110){
		
			moveTo(e);	
			
		}
		
	}
	public void randomMovement(){
		
		if(isPlayer)
			return;
		
		if(reachedDestination){
			ran =  new Vector(Math.random()*2000, Math.random()*2000);
			reachedDestination = false;
		}
		
		double d = getDistanceFrom(ran);
		if(d > 50 || target == null){
			reachedDestination = false;

			//aimAt(ran);
			moveTo(ran);	
			
		}
		else{
			reachedDestination = true;
		}
	}
	public void detectHostile(Entity e){
		
		if(isPlayer)
				return;
		
		if(isDead || e.getDead()){
			return;
		}
		
		//hasTarget = false;
		
		double distance = getDistanceFrom(e);
		
		if(e.getType() != getType()  && !e.getDead() && distance < 500){
			hasTarget = true;
			aimAt(e);
			shoot();
		}
		/* 
		if(e.getType() == "enemy" && getType() == "ally" && !e.getDead() && distance < 500){
			hasTarget = true;
			aimAt(e);
			shoot();
		}
		
		else if(e.getType() == "ally" && getType() == "enemy" && !e.getDead() && distance < 500){
			hasTarget = true;
			aimAt(e);
			shoot();
		} */
	}
	public double getDistanceFrom(Entity e){
		
		double dx = e.getPosition().x - getPosition().x;
		double dy = e.getPosition().y - getPosition().y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		return length;
		
	}
	public double getDistanceFrom(Vector v){
		
		double dx = v.x - getPosition().x;
		double dy = v.y - getPosition().y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		return length;
		
	}
	public void startTimer(){
		timer.waitTime(5000);
		if(target != null){
			aimAt(target);
		}
		
		while(timer.isDone()){
			shoot();
		}
	}
	public void newBehaviorID(){
		
		
		if(gameDecisionTimer.isDone()){
			behaviorID = (int)(Math.random() * 4);
		}
		
		try{
			gameDecisionTimer.waitTime(30000);
		}catch(Exception ee){
			System.out.println(ee);
		}
		
	}
	public double getAngle(){
		return angle;
	}
	public void setAngle(double a){
		angle = a;
	}
	public Weapon getWeapon(){
		return weapon;
	}
	public void setWeapon(Weapon w){
		weapon = w;
	}
	public boolean getDead(){
		return isDead;
	}
	public void setDead(boolean dead){
		isDead = dead;
	}
	public Entity getTarget(){
		return target;
	}
	public void setTarget(Entity t){
		target = t;
	}
	public String getType(){
		return type;
	}
	public void setType(String t){
		type = t;
	}
	public Vector[] getPatrol(){
		return patrolPath;
	}
	public void setPatrol(Vector[] p){
		patrolPath = p;
	}
	public boolean getPlayer(){
		return isPlayer;
	}
	public void setPlayer(boolean p){
		isPlayer = p;
	}
	public int getHealth(){
		return health;
	}
	public void setHealth(int h){
		health = h;
	}
	public double getSpeed(){
		return speed;
	}
	public void setSpeed(double s){
		speed = s;
	}
	public Flag getFlagTarget(){
		return flagTarget;
	}
	public void setFlagTarget(Flag f){
		flagTarget = f;
	}
	public Vector getSpawnPoint(){
		return spawnpoint;
	}
	public void setSpawnPoint(Vector s){
		spawnpoint = s;
	}
	
	/*
	
	private boolean followCursor = true;
	private Vector destination;
	private boolean reachedDestination = false;
	
	
	private Collisions cManager = new Collisions();
	
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>(); //switch to weapon
	
	
	public Entity(double x, double y, int radius){
		 
		this.position = new Vector(x, y);
		this.radius = radius;
		
		this.vel = new Vector(0, 0);
		this.color = new Color((int)(255 * Math.random()),(int)(255 * Math.random()),(int)(255 * Math.random()));
		this.isDead = false;
		
		this.sprite = new Circle(x, y, radius);
		sprite.color = color;
		
	}
	public void draw(Graphics2D g2){
		
		for(int i = 0; i < bullets.size();i++){
			if(bullets.size()> 0){
				bullets.get(i).draw(g2);	
			}
		}
		
		sprite.draw(g2);
		
		g2.setColor(Color.BLACK);
		drawAngleLine(g2);
		
	}
	public void update(){
		if(isDead){
			color = color.BLACK;
			return;
		}
		
		for(int i = 0; i < bullets.size();i++){
			if(bullets.size()> 0){
				bullets.get(i).update();
				
				if(isOutOfRange(i)){
					bullets.remove(i);
				}
			}
		}
		
		//moveAtAngle(angle);
		 goToDestination();
	//	checkBoundary();
	
	}
	
	public void goToDestination(){		
		
		if(destination == null || isDead){
			return;
		}
		
		double dx = destination.x - position.x;
		double dy = destination.y - position.y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		dx /= length;
		dy /= length;
		
		if(length > 5){
			position.x += dx* speedX;
			position.y += dy* speedY;
			reachedDestination = false;
		}
		else{
			reachedDestination = true;
		}
		
	}
	
	public double aimAt(Circle target){
		
		if(isDead){
			return angle;
		}
		
		double x = target.position.x - position.x;
		double y = target.position.y - position.y;
		
		angle = Math.atan2(y,x);
		
		return angle;
		
	}
	public void shoot(){
		if(isDead){
			return;
		}
		
		System.out.println(bullets.size());
		Bullet b = new Bullet(new Vector( (int)(position.x + 100*Math.cos(angle)),  (int)(position.y + 100*Math.sin(angle))));
		b.setSpeed(90);
		b.setAngle(angle);
		
		bullets.add(b);
	}
	public void checkBoundary(){
		if(position.x - radius <= 0){
			position.x = 0+radius;
			vel.x = vel.x * -1;
		}
		if(position.x + radius > 900){
			position.x = 900 - radius;
			vel.x = vel.x * -1;
		}
		if(position.y - radius <= 0){
			position.y = 0+radius;
			vel.y = vel.y * -1;
		}
		if(position.y + radius > 900){
			position.y = 900 - radius;
			vel.y = vel.y * -1;
		}
	}
	public void collisionUpdate(Bullet bullet){
		for(int i = 0; i < 10;i++){
			if(cManager.colliding(this, bullet)){
				isDead = true;
			}
		}
	}
	
	public boolean isOutOfRange(int counter){
		
		double x = position.x - bullets.get(counter).getPosition().x;
		double y = position.y - bullets.get(counter).getPosition().y;
		
		double distance = Math.sqrt((x * x) + (y * y));
		
		if(distance > 5000){
			System.out.println("Bullet Removed");
			return true;
		}
		return false;
	}
	public void die(){
		
	} */
}