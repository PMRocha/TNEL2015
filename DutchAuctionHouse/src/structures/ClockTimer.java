package structures;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ClockTimer {

	private int time=0;
	private boolean triggered;
	public boolean isTriggered() {
		return triggered;
	}

	public void setTriggered(boolean reduce) {
		this.triggered = reduce;
	}

	private int reduceTime;
	
	public ClockTimer (int rt)
	{
		triggered=false;
		reduceTime=rt;
	}
	
	public void runTime() {

		ScheduledExecutorService exec = Executors
				.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(!triggered)
				time++;
				
				//System.out.println(time);
				
				if(time>=reduceTime)
				{
					time=0;
					triggered=true;
				}
			}
		}, 0, 1, TimeUnit.SECONDS);

	}

}


	
