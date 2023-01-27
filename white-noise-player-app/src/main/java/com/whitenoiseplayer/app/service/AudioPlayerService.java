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

	public AudioPlayerService(@Autowired AudioPlayer audioPlayer, @Autowired VolumeManagerExecutor volumeManagerExecutor) {
		this.audioPlayer = audioPlayer;		
		this.volumeManagerExecutor = volumeManagerExecutor;		
	}

	public void switchStateTo(boolean state) {
		if (state) 
			volumeManagerExecutor.executeAt(ZonedDateTime.now());				
		else {			
			volumeManagerExecutor.clearSchedule();
			audioPlayer.stopAudio();
		}							
	}

	public double getPlayerVolume() {
		return audioPlayer.getVolume();
	}

	public void setPlayerVolume(double volume) {
		audioPlayer.setVolume(volume);
	}
}
