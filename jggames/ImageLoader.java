package jggames;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

class ImageLoader{
	
BufferedImage img;

	public void loadImg(String filename){
		try{
			
			this.img = ImageIO.read(new File(filename));
			
		}
		
			catch(IOException e){
				
				System.out.println("Couldn't load image!");
		}
	}
}