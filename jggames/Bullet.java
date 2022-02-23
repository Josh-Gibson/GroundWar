package jggames;

import java.awt.Graphics2D;
import java.awt.Color;
import javax.sound.sampled.*;

public class Bullet{
	
	private Vector position;
	private double sizeX;
	private double sizeY;
	
	private double speed;
	private double angle;
	
	public boolean exhausted = false;
	
	private AudioPlayer player = new AudioPlayer();
	
	
	
	public Bullet(Vector p){
		position = p;
		sizeX = 3;
		sizeY = 3;
		speed = 40;
		//player.playSound("././res/audio/gunshot.wav");
		
	}
	
	public void update(){
		moveAtAngle(angle);
	}
	public void draw(Graphics2D g2){
		g2.setColor(Color.WHITE);
		g2.fillRect((int)position.x, (int)position.y, (int)sizeX, (int)sizeY);
		g2.setColor(Color.BLACK);
		g2.drawRect((int)position.x, (int)position.y, (int)sizeX, (int)sizeY);
	}
	public void moveAtAngle(double angle){
		position.x +=  speed * Math.cos(angle);
		position.y +=  speed * Math.sin(angle);
	}
	public double getSpeed(){
		return speed;
	}
	public void setSpeed(double s){
		speed = s;
	}
	public double getAngle(){
		return angle;
	}
	public void setAngle(double a){
		angle = a;
	}
	public Vector getPosition(){
		return position;
	}
	public void setPosition(Vector p){
		position = p;
	}
	public double getSize(){
		return sizeX;
	}
	public void SetSize(double s){
		sizeX = s;
	}
}