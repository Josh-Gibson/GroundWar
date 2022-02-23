package jggames;

import java.awt.Color;

public class GameMath{
	
	public static double getDistanceFrom(Vector v, Entity e){
		
		double dx = v.x - e.getPosition().x;
		double dy = v.y - e.getPosition().y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		return length;
		
	}
	public static double getDistanceFrom(Vector v1, Vector v2){
		
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		return length;
		
	}
	public static double getDistanceFrom(Entity e1, Entity e2){
		
		double dx = e1.getPosition().x - e2.getPosition().x;
		double dy = e1.getPosition().y - e2.getPosition().y;
		double length = Math.sqrt((dx * dx) + (dy * dy));
		
		return length;
		
	}
}