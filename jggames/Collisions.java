package jggames;

import java.awt.Color;
 
public class Collisions{

	//courtesy of javidx9
	/* public boolean colliding(Circle c1, Circle c2){
		
		double x = (c1.position.x - c2.position.x) * (c1.position.x - c2.position.x);
		double y = (c1.position.y - c2.position.y) * (c1.position.y - c2.position.y);
		double r = (c1.radius + c2.radius) * (c1.radius + c2.radius);
		
		Vector nullVec = new Vector(0,0);
		
		if(Math.abs(x + y) <= r){
			if(c1 == c2 || c1.vel == nullVec && c2.vel == nullVec){
				return false;
			}
			return true;
		}
		else{
			return false;
		}
	} */
	
	public boolean colliding(Entity c1, Bullet c2){
		
		if(c2.getPosition().x + 110 < c1.getPosition().x || c2.getPosition().x - 110 > c1.getPosition().x){
			return false;
		}
		
		if(c2.getPosition().y + 110 < c1.getPosition().y || c2.getPosition().y - 110 > c1.getPosition().y){
			return false;
		}
		
		double x = (c1.getPosition().x - c2.getPosition().x) * (c1.getPosition().x - c2.getPosition().x);
		double y = (c1.getPosition().y - c2.getPosition().y) * (c1.getPosition().y - c2.getPosition().y);
		double r = (c1.getRadius() + c2.getSize()/2) * (c1.getRadius() + c2.getSize()/2);
		
		Vector nullVec = new Vector(0,0);
		
		if(Math.abs(x + y) <= r){
			
			return true;
			
		}
		
		return false;
		
	}
}