package jggames;

import java.awt.Color;
import java.util.ArrayList;

public class CaptureFlag extends Circle{
	
	private String ownership;
	
	public CaptureFlag(double x, double y, int radius){
		super(x, y, radius);
		setColor(new Color(255, 0, 255));
	}
	public String getOwnership(){
		return ownership;
	}
	public void setOwnership(String s){
		ownership = s;
	}
}