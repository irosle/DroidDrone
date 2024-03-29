package com.chakravarty.droiddrone;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.*;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.view.Menu;
import com.chakravarty.httpServer.*;
import com.chakravarty.duinControl.*;
import com.chakravarty.wsst.*;



public class Configuration_client extends Activity {
	//This acts as the server;
	httpServer fg;
	Camera mCamera;
	Bitmap bm;
	Duino arduino;
	videoStream vs = new videoStream();
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    	bm = Bitmap.createBitmap(80, 60, Bitmap.Config.RGB_565);
	    	bm.copyPixelsFromBuffer(ByteBuffer.wrap(data));
	    	vs.pushImage(bm);
	    }
	};
	public void turn(String Request){
		Request = "<htmL></html>";
    }
	public void left(String Request){
		Request = "<htmL></html>";
		arduino.sendChar('l');
    }
	public void right(String Request){
		Request = "<htmL></html>";
		arduino.sendChar('r');
    }
	public void stop(String Request){
		Request = "<htmL></html>";
		arduino.sendChar('s');
    }
	public void video(String Request){
		Request = "<htmL></html>";
		vs.render();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_client);
        @SuppressWarnings("rawtypes");
		Class[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;
		Method method1;
		try {
			method1 = Configuration_client.class.getMethod("turn", parameterTypes);
			fg.onRoute("/home*", this, method1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			method1 = Configuration_client.class.getMethod("left", parameterTypes);
			fg.onRoute("/left", this, method1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			method1 = Configuration_client.class.getMethod("right", parameterTypes);
			fg.onRoute("/right", this, method1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			method1 = Configuration_client.class.getMethod("stop", parameterTypes);
			fg.onRoute("/left", this, method1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			method1 = Configuration_client.class.getMethod("video", parameterTypes);
			fg.onRoute("/video.txt", this, method1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
        fg.start();
        
        mCamera = Camera.open();
        Parameters params = mCamera.getParameters();
        params.setPictureSize(80, 60);
        params.setPictureFormat(0x00000004);
        mCamera.setParameters(params);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuration_client, menu);
        return true;
    }
    

	@SuppressWarnings("unused")
	private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    class firstResponder extends Thread {
    	boolean running = true;
    	firstResponder() {
           
        }

        public void run() {
            // constantly takes pictures.
        	while(running){
        		mCamera.takePicture(null, null, mPicture);
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    } 
}
