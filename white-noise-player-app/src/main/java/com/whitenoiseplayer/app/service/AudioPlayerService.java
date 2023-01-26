package com.whitenoiseplayer.app.service;

import java.time.Instant;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitenoiseplayer.app.player.AudioPlayer;
import com.whitenoiseplayer.app.volume.WeeklySchedule;

@Service
public class AudioPlayerService {
	private final AudioPlayer audioPlayer;

	public AudioPlayerService(@Autowired AudioPlayer audioPlayer, @Autowired WeeklySchedule schedule) {
		this.audioPlayer = audioPlayer;
		
		System.out.println(schedule.getCurrentVolume(Instant.now().atZone(ZoneId.systemDefault()), true));
	}

	public void switchStateTo(boolean state) {
		if (state)
			audioPlayer.playAudio();
		else 
			audioPlayer.stopAudio();				
	}

	public double getPlayerVolume() {
		return audioPlayer.getVolume();
	}

	public void setPlayerVolume(double volume) {
		audioPlayer.setVolume(volume);
	}
}
