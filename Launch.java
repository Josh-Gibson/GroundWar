package jggames;

import javax.swing.JFrame; 
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

public class Launch{
	
	public static void main(String args[]){
		
		System.out.println("Launch started");
		Game game = new Game();
		  
		JFrame frame = new JFrame("GROUND WAR");
		frame.setContentPane(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		
		//BIG thanks to coobird on Stack Overflow for this!
		//https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application
		
		BufferedImage tst = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
			tst, new Point(0,0), "blank curdsor"
		);
		
		frame.getContentPane().setCursor(cursor);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}