package com.chakravarty.wsst;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.*;
import android.graphics.Bitmap;

//TODO - Implement

public class videoStream  {
	Bitmap previous;
	boolean initialValue = true;
	videoStream(){
		
	}
	public void pushImage(Bitmap bm){
		previous = bm;
	}
	public void render(){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		previous.compress(Bitmap.CompressFormat.JPEG, 100, os);
		initialValue = false;
		try {
			ByteArrayOutputStream gzos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(gzos);
			gzip.write(os.toString().getBytes());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

