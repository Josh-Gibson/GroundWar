package jggames;

import javax.sound.sampled.*;
import java.io.*;

class AudioPlayer{
	public Clip clip;
	public String path;
	
	
	public void playSound(String pathname){
		
		File file = new File(pathname);
	
		
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch (UnsupportedAudioFileException ex) {
			System.out.println("You fucked up");
			ex.printStackTrace();
		}
		catch(LineUnavailableException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public void setPath(String pathname){
		path = pathname;
	}
	public void playGameOver(){
		/* 
		playSound("./audio/blockWin.wav");
		gameOverPlayed = true;
		
		clip.addLineListener(new LineListener(){
			public void update(LineEvent e){
				if(e.getType() == LineEvent.Type.STOP){
					clip.setFramePosition(0);
					clip.start();
				}
			}
		}); */
	}
}