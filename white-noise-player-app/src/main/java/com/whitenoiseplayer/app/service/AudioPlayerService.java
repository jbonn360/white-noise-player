package com.whitenoiseplayer.app.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitenoiseplayer.app.player.AudioPlayer;
import com.whitenoiseplayer.app.volume.VolumeManagerExecutor;

@Service
public class AudioPlayerService {
	private final AudioPlayer audioPlayer;
	private final VolumeManagerExecutor volumeManagerExecutor;
	private boolean playbackOverride = false;	

	public AudioPlayerService(@Autowired AudioPlayer audioPlayer, @Autowired VolumeManagerExecutor volumeManagerExecutor) {
		this.audioPlayer = audioPlayer;		
		this.volumeManagerExecutor = volumeManagerExecutor;		
	}

	public void switchStateTo(boolean state) {
		if (state) {
			if(!playbackOverride)
				volumeManagerExecutor.executeAt(ZonedDateTime.now());
			else {
				volumeManagerExecutor.clearSchedule();
				audioPlayer.setVolume(1d);
				audioPlayer.playAudio();
			}				
		} else {			
			volumeManagerExecutor.clearSchedule();
			audioPlayer.stopAudio();
		}							
	}

	public double getPlayerVolume() {
		return audioPlayer.getVolume();
	}

	public void setPlayerMasterVolume(double volume) {
		audioPlayer.setMasterVolumeMultiplier(volume);
		audioPlayer.setVolume(audioPlayer.getVolume());
	}
	
	public double getPlayerMasterVolume() {
		return this.audioPlayer.getMasterVolumeMultiplier();
	}
	
	public boolean isPlayerActive() {
		return this.audioPlayer.isPlaying();
	}
	
	public boolean isPlaybackOverride() {
		return this.playbackOverride;
	}

	public boolean setPlaybackOverride(boolean value) {
		this.playbackOverride = value;
		
		if(!value) {
			audioPlayer.stopAudio();
			//volumeManagerExecutor.executeAt(ZonedDateTime.now());
			pause(100);
		}
		
		return (!value) ? audioPlayer.isPlaying() : true;
	}
	
	private void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
