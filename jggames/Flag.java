package jggames;

import java.awt.Color;
import java.util.ArrayList;

public class Flag extends Circle{
	
	
	public ArrayList<Entity> EntitiesInFlag = new ArrayList<Entity>();
	
	private String dominance = "";
	
	public Flag(double x, double y, int radius){
		super(x, y, radius);
		
		setColor(Color.GRAY);
	}
	public void entityInFlag(Entity e){
		
		if(e.getDead()){
			if(EntitiesInFlag.contains(e)){
				EntitiesInFlag.remove(e);
			}
			return;
		}
		
		if(GameMath.getDistanceFrom(getPosition(),e) < getRadius()){
			if(EntitiesInFlag.contains(e)){
				return;
			}
			
			EntitiesInFlag.add(e);
		}
		else{
			EntitiesInFlag.remove(e);
		}
	}
 	public void checkFlagColor(){
		
		
		double allySize = 0;
		double enemySize = 0;
		
		for(int i = 0; i < EntitiesInFlag.size(); i++){
			
			Entity e = EntitiesInFlag.get(i);
			
			if(e.getType() == "ally" ){
			
				allySize++;
				
			}
			
			if(e.getType() == "enemy"){
			
				enemySize++;
			
			}
		}
		//System.out.println(allySize);
		if(allySize > enemySize){
			
			dominance = "ally";
			setColor(new Color(0, 0, 100));
		}
		if(allySize < enemySize){
			dominance = "enemy";
			setColor(new Color(100, 0, 0));
		}
		if(allySize == enemySize){
			if(EntitiesInFlag.size() == 0){
			/* 	dominance = "NONE";
				setColor(Color.GRAY); */
				return;
			}
			dominance = "NONE";
			setColor(new Color(255, 0, 255));
		}
	} 
	@Override
	public void update(){
		super.update();
		checkFlagColor();
	}
	public String getDominance(){
		return dominance;
	}
	
}