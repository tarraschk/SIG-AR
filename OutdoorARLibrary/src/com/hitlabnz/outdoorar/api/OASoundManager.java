package com.hitlabnz.outdoorar.api;

import com.hitlabnz.androidar.data.representation.sound.SoundManager;
import com.hitlabnz.androidar.data.representation.sound.SoundManagerFMOD;

public class OASoundManager{

	private static SoundManagerFMOD soundManager = null;
	private static boolean started = false;
	
	public OASoundManager(){
		super();
	}
	
	public static SoundManager getSoundManager(){
		if(OASoundManager.soundManager == null){
			OASoundManager.soundManager = new SoundManagerFMOD();
		}
		if(!started){
			OASoundManager.soundManager.start();
			OASoundManager.started = true;
		}
		return OASoundManager.soundManager;
	}
	
	public static void finish(){
		if(OASoundManager.soundManager != null){
			OASoundManager.soundManager.stop();
			OASoundManager.started = false;
		}
	}
	
	public static void start(){
		OASoundManager.soundManager.start();
		OASoundManager.started = true;
	}
	
	public static boolean isRunning(){
		return OASoundManager.started;
	}
}
