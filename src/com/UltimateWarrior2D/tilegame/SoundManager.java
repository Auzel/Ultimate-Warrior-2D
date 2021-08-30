package com.UltimateWarrior2D.tilegame;

import javax.sound.sampled.AudioInputStream;		// for playing sound clips
import javax.sound.sampled.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import java.util.HashMap;				// for storing sound clips

public class SoundManager {				// a Singleton class
	private HashMap<String, Clip> effectsClips;
   	private ArrayList<Clip> backgroundClips = null;			// played continuously after ball is created
    
    private long mutePos;
    private int currBackgroundIndex;

	private static SoundManager instance = null;	// keeps track of Singleton instance

    
	private SoundManager () {
		effectsClips = new HashMap<String, Clip>();
        backgroundClips = new ArrayList<>();

        Clip clip;

        //backgroundClips
        clip = loadClip("sounds/background.wav");
        backgroundClips.add(clip);
        clip = loadClip("sounds/background2.wav");
        backgroundClips.add(clip);
        clip = loadClip("sounds/background3.wav");
        backgroundClips.add(clip);
		

        //effectsClips
		clip = loadClip("sounds/XP.wav");
		effectsClips.put("XP", clip);

		clip = loadClip("sounds/life.wav");
		effectsClips.put("life", clip);

        clip = loadClip("sounds/health.wav");
		effectsClips.put("health", clip);

        clip = loadClip("sounds/guardHurt.wav");
		effectsClips.put("policeHurt", clip);

        clip = loadClip("sounds/warriorHurt.wav");
		effectsClips.put("warriorHurt", clip);

        clip = loadClip("sounds/dogHurt.wav");
		effectsClips.put("dogHurt", clip);

        mutePos=0;
        currBackgroundIndex =0;
	}

	public static SoundManager getInstance() {	// class method
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		

	private Clip getBackgroundClip (int index) {
        if(index>=backgroundClips.size())
            index=index%backgroundClips.size(); //assuming bgrndSize notEmpty
		return backgroundClips.get(index);
	}

    private Clip getEffectsClip (String title) {
		return effectsClips.get(title);
	}

    public Clip loadClip (String fileName) {	// gets clip from the specified file
    AudioInputStream audioIn;
    Clip clip = null;

    try {
            File file = new File(fileName);
            audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
            clip = AudioSystem.getClip();
            clip.open(audioIn);
    }
    catch (Exception e) {
        System.out.println ("Error opening sound files: " + e);
    }
        return clip;
    }


    private void playTrack(int index){ //tracks deal with background msuic
        Clip clip;
        clip = getBackgroundClip(index);
        if(clip!=null){
            clip.setMicrosecondPosition(mutePos);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
        
    }
    public void playNextTrack(){
        stopTrack();
        playTrack(++currBackgroundIndex);
    }
    public void playTrack(){
        stopTrack();
        playTrack(currBackgroundIndex);
    }

    public void stopTrack(){
        Clip clip;
        clip = getBackgroundClip(currBackgroundIndex);
        if(clip!=null){
            clip.stop();
        }
    }

    public void muteTrack(){
        Clip clip;
        clip = getBackgroundClip(currBackgroundIndex);
        if(clip!=null){
            mutePos = clip.getLongFramePosition();
            clip.stop();
        }
    }

    public void playEffects(String title) {
        Clip clip = getEffectsClip(title);
        if (clip != null) {
            clip.start();
        }
    }

    public void stopEffects(String title) {
        Clip clip = getEffectsClip(title);
        if (clip != null) {
            clip.stop();
        }
    }

}