package com.chakravarty.toneGenerator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;

public class toneGenerator {
	 private final int duration = 3; // seconds
	 private final int sampleRate = 8000;
	 private final int numSamples = duration * sampleRate;
	 private final double sample[] = new double[numSamples];
	 private final byte generatedSnd[] = new byte[2 * numSamples];
	 private double freqOfTone = 100; // hz
	 private int dir;
	 
	 Handler handler = new Handler();
	 toneGenerator( int freq, int d){
		 freqOfTone = freq;
		 dir = d;
	 }
	 void genTone(){
	  // fill out the array
	  for (int i = 0; i < numSamples; ++i) {
	   sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
	  }

	  // convert to 16 bit pcm sound array
	  // assumes the sample buffer is normalised.
	  int idx = 0;
	  for (double dVal : sample) {
	   short val = (short) (dVal * 32767);
	   generatedSnd[idx++] = (byte) (val & 0x00ff);
	   generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
	  }
	 }

	 void playSound(){
		 switch(dir){
		 	case 0:
		 		break;
		 	default:
		 		break;
		 }
	  AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
	    8000, AudioFormat.CHANNEL_IN_LEFT,
	    AudioFormat.ENCODING_PCM_16BIT, numSamples,
	    AudioTrack.MODE_STATIC);
	  audioTrack.write(generatedSnd, 0, numSamples);
	  audioTrack.play();
	 }

}
