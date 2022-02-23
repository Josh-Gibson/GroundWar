package jggames;

public class DeltaTimer{
	
	private boolean timerStarted;
	
	private long startTime;
	
	private boolean done;
	private boolean repeatable;
	
	public DeltaTimer(){
		repeatable = false;
	}
	public void waitTime(int seconds){
		//thank you to lolums
		//https://gamedev.stackexchange.com/questions/111741/calculating-delta-time
		if(!timerStarted){
			startTime = System.nanoTime();
			timerStarted = true;
			done = false;
		}
		if((System.nanoTime() - startTime) / 1000000L > seconds){
			done = true;
			
			if(repeatable){
				timerStarted = false;
			}
			if(done){
				timerStarted = false;
			}
		}
	}
	public void waitTime(long nanoseconds){
		
		if(!timerStarted){
			startTime = System.nanoTime();
			timerStarted = true;
			done = false;
		}
		if((System.nanoTime() - startTime) > nanoseconds){
			done = true;
			
			if(repeatable){
				timerStarted = false;
			}
			if(done){
				timerStarted = false;
			}
		}
	}
	
	public boolean isDone(){
		return done;
	}
	public void setRepeatable(boolean r){
		repeatable = r;
	}
}