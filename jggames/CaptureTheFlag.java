package jggames;


import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;


public class CaptureTheFlag{

private CaptureFlag flag;

	
	//If flag dropped, go to flag
	//if team has flag, go to return it
	//Keep reserves to defend
	
	private Vector allySpawn = new Vector(0, 500);
	private Vector enemySpawn = new Vector(3000, 500);
	
	private Vector flagSpawn = new Vector(1500, 500);
	
	private AudioPlayer audio = new AudioPlayer();
	
	private int allyScore = 0;
	private int enemyScore = 0;
	
	private DeltaTimer timer = new DeltaTimer();
	
	private Circle allyBase = new Circle(0, 500, 50);
	private Circle enemyBase = new Circle(3000, 500, 50);
	
	public CaptureTheFlag(){
		
		allyBase.setColor(new Color(0, 0, 50));
		enemyBase.setColor(new Color(50, 0, 0));
		
		spawnFlag();
	}
	
	
	public void drawUnder(Graphics2D g2){
		
		allyBase.draw(g2);
		enemyBase.draw(g2);
		
		
	}
	public void drawOver(Graphics2D g2){
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.PLAIN, 50));
		g2.drawString("ALLIES: " + allyScore + "\n ENEMIES: " + enemyScore,0, -20);
		flag.draw(g2);
	}
	public void update(Entity e){
		
	/* 	if(timer.isDone()){
			e.newBehaviorID();
			if(e.getPlayer()){
				e.behaviorID = 2;	
			}
			System.out.println(e.behaviorID);
		}
		
		try{
			timer.waitTime(500);
		}catch(Exception ee){
			System.out.println(ee);
		} */
		
		e.newBehaviorID();
		
		returnFlag();
		
		switch(e.behaviorID){
			case 0:
				defendBase(e);
				break;
			case 1:
				attackBase(e);
				break;
			case 2:
				if(flag.getOwnership() != e.getType()){
		
					chaseFlag(e);	
				}
				else{
					returnToBase(e);
				}
				break;
			case 3:
				attackBase(e);
				break;
				
		}
		

		pickupFlag(e);
		
		if(e.getDead()){

			if(e.getType() == "ally"){
				e.respawn(new Vector(-200,500));
			}
			else if(e.getType() == "enemy"){
				e.respawn(new Vector(3200, 500));
			}


			//e.respawn();
		}

		
	}
	
	
	
	public void spawnFlag(){
		flag = new CaptureFlag(flagSpawn.x, flagSpawn.y, 20);
	}
	public void chaseFlag(Entity e){
		if(!e.getPlayer() && !e.getDead()){
			e.moveTo(flag.getPosition());
		}
		
	}
	public void pickupFlag(Entity e){
		
		if(flag.getOwnership() == e.getType()){
		//	return;
		}
	
		if(e.getDead())
			return;
		if(GameMath.getDistanceFrom(flag.getPosition(), e.getPosition()) < flag.getRadius()){
			
		
		
			if(flag.getOwnership() == null && !e.getDead()){
			
				audio.playSound("././res/audio/ctfFlagPickUp.wav");	
			}
			
			e.behaviorID = 2;
			flag.setOwnership(e.getType());
			flag.setPosition(e.getPosition());
			
		}
		
	}
	public void returnToBase(Entity e){
		
		if(e.getPlayer()){
			return;
		}
		
		if(flag.getOwnership() == "ally"){
			e.moveTo(allySpawn);
		}
		else if(flag.getOwnership() == "enemy"){
			e.moveTo(enemySpawn);
		}
		
	}
	public void returnFlag(){
		if(GameMath.getDistanceFrom(allySpawn, flag.getPosition()) < flag.getRadius() * 2){
			
			audio.playSound("././res/audio/ctfFlagCapture.wav");
			flagSpawn = new Vector(1500, (int)(Math.random()*3000));
			flag.setPosition(flagSpawn);
			flag.setOwnership(null);
			allyScore++;
		}
		if(GameMath.getDistanceFrom(enemySpawn, flag.getPosition()) < flag.getRadius() * 2){
				
			audio.playSound("././res/audio/ctfFlagCapture.wav");
			flagSpawn = new Vector(1500, (int)(Math.random()*3000));
			flag.setPosition(flagSpawn);
			flag.setOwnership(null);
			enemyScore++;
		}
	}
	
	public void defendBase(Entity e){
		
		if(e.getPlayer()){
			return;
		}
		
		if(e.getType() == "ally"){
			e.moveTo(allySpawn);
		}
		else if(e.getType() == "enemy"){
			e.moveTo(enemySpawn);
		}
		
	}
	public void attackBase(Entity e){

		if(e.getPlayer()){
			return;
		}

		if(e.getType() == "ally"){
			e.moveTo(enemySpawn);
		}
		else if(e.getType() == "enemy"){
			e.moveTo(allySpawn);
		}
		
	}
	public void campFlagPosition(Entity e){
		if(e.getPlayer()){
			return;
		}
		e.moveTo(flagSpawn);
	}
}