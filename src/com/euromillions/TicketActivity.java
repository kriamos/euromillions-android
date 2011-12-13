package com.euromillions;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.euromillions.actions.DataGenerate;
import com.euromillions.beans.Number;
import com.euromillions.beans.Ticket;

public class TicketActivity extends Activity implements SensorEventListener{
	
	private static final String TAG = "TicketActivity:";
	
	// For shake motion detection.
	private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int FORCE_THRESHOLD = 350;
    private static final int SHAKE_COUNT = 3;
    private static final int SHAKE_DURATION = 1000;
	
	private SensorManager sensorMgr;
    private long mLastForce;
    private int mShakeCount = 0;
    private long mLastTime;
    private long mLastShake;
    
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    
    
	
    
    private int selectedAction = 0; 
	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ticket);
		
		Bundle extras = getIntent().getExtras();
		
		Ticket ticket = (Ticket)extras.get("ticket");
		selectedAction = extras.getInt("selectedAction");
		
		fillBalls(ticket.getNumbers());
		fillStars(ticket.getStars());
		
		// start motion detection
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		boolean accelSupported = sensorMgr.registerListener(this,
			sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
			SensorManager.SENSOR_DELAY_GAME);
	 
		if (!accelSupported) {
		    // on accelerometer on this device
		    sensorMgr.unregisterListener(this,
		    		sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		}
		
	}
	
	
	public void onAccuracyChanged (Sensor sensor, int accuracy){
		//
	}
	public void onSensorChanged (SensorEvent event){
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
		    long now = System.currentTimeMillis();
		    // only allow one update every 100ms.
		    if ((now - mLastForce) > SHAKE_TIMEOUT) {
		    	mShakeCount = 0;
		    }
		    
			if((now-mLastTime) > TIME_THRESHOLD){
				
				long diff = (now - mLastTime);
				x = values[SensorManager.DATA_X];
				y = values[SensorManager.DATA_Y];
				z = values[SensorManager.DATA_Z];
				float speed = Math.abs(x+y+z - last_x - last_y - last_z)
                	/ diff * 10000;
				
				if(speed > FORCE_THRESHOLD){
					if(++mShakeCount >= SHAKE_COUNT && 
							(now - mLastShake > SHAKE_DURATION)){
						mLastShake = now;
						mShakeCount = 0;
						//Toast.makeText(getApplicationContext(), "Shake!!!", Toast.LENGTH_SHORT).show();
						try {
							Ticket ticket = new DataGenerate(getApplicationContext()).execute(new Integer[]{this.selectedAction}).get();
							repaintTicket(ticket);
						} catch (InterruptedException e) {
							Log.e(TAG, e.getMessage(), e);
							//ALERT
						} catch (ExecutionException e) {
							Log.e(TAG, e.getMessage(), e);
							//ALERT
						}
						
						
					}
					mLastForce = now;
				}
				mLastTime = now;
				
				last_x = x;
				last_y = y;
				last_z = z;
		    }
		}
	}
	
	
	private void repaintTicket(Ticket ticket){
		fillBalls(ticket.getNumbers());
		fillStars(ticket.getStars());
		View view = getWindow().getDecorView();
		view.invalidate();
		view.refreshDrawableState();
	}
	
	private void fillBalls(Number[] balls){
		
		int[] ballIds = new int[]{
				R.id.ball1, R.id.ball2,
				R.id.ball3, R.id.ball4,
				R.id.ball5
		};
		
		int ballPosition = 0;
		for(int ballId: ballIds){
			TextView ball = (TextView) findViewById(ballId); 
			ball.setText(String.valueOf(balls[ballPosition++].getNumber()));
		}
		
	}
	
	private void fillStars(Number[] stars){
		int[] starsIds = new int[]{
			R.id.star1, R.id.star2	
		};
		int starPosition = 0;
		for(int starId:starsIds){
			TextView star = (TextView) findViewById(starId);
			star.setText(String.valueOf(stars[starPosition++].getNumber()));
		}
	}
	
}
