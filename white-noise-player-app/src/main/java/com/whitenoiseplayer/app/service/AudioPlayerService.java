package com.whitenoiseplayer.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whitenoiseplayer.app.player.AudioPlayer;

@Service
public class AudioPlayerService {
	private final AudioPlayer audioPlayer;

	public AudioPlayerService(@Autowired AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
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
