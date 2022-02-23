package jggames;

import java.awt.Graphics2D;
import java.awt.Color;

public class Camera{
	
	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;
	
	private Vector position;
	
	public int xSpeed = 0;
	public int ySpeed = 0;
	public int camSpeed = 5;
	public int offsetX = 0;
	public int offsetY = 400;
	
	
	public Camera(){
		
		position = new Vector(0,0);
		
	}
	public Camera(double x, double y){
		
		position = new Vector(x, y);
		
	}
	public Camera(Vector v){
		
			position = v;
			
	}
	public void update(Graphics2D g2){
		
		checkCamSpeed();
		
		position.x += xSpeed;
		position.y += ySpeed;
		
		//scope sway 0w0
		// + Math.cos(num)*2;
		// + Math.sin(5*num)*2;
		
		g2.translate(-(int)position.x+offsetX, -(int)position.y+offsetY); //inverted to make sense
		
	}
	
	public void checkCamSpeed(){
		
		camSpeed = Math.abs(camSpeed);
		
		if(up){
			ySpeed = -camSpeed;
		}
		if(down){
			ySpeed = camSpeed;
		}
		if(left){
			xSpeed = -camSpeed;
		}
		if(right){
			xSpeed = camSpeed;
		}
		
		if(!up && !down){
			ySpeed = 0;
		}
		if(!left && !right){
			xSpeed = 0;
		}
		
	}
	public Vector getPosition(){
		return position;
	}
	public void setPosition(Vector p){
		position = p;
	}
}