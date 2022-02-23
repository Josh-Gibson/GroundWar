package jggames;

import java.awt.Color;
import java.awt.Graphics2D;	
import java.awt.Font;  
import java.util.ArrayList;

public class Circle{
	
	private int radius;
	
	private Vector position;
	
	private Color color;
	
	public Circle(double x, double y, int radius){
		 
		this.position = new Vector(x, y);
		this.radius = radius;
		
		this.color = new Color((int)(255 * Math.random()),(int)(255 * Math.random()),(int)(255 * Math.random()));
		
		}
	public void draw(Graphics2D g2){
		g2.setColor(color);
		g2.fillOval((int)position.x - radius, (int)position.y - radius, radius * 2, radius * 2);
		
	}
	public void update(){
		
	}
	public Vector getPosition(){
		return position;
	}
	public void setPosition(Vector p){
		position = p;
	}
	public Color getColor(){
		return color;
	}
	public void setColor(Color c){
		color = c;
	}
	public int getRadius(){
		return radius;
	}
	public void setRadius(int r){
		radius = r;
	}
}