package com.whitenoiseplayer.app.player;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

@Component
public class BPAudioPlayer implements AudioPlayer {
	private final static Logger LOG = LoggerFactory.getLogger(JavaAudioPlayer.class);

	private final BasicPlayer player;
	private final String soundTrackPath;
	
	private boolean isPlaying;
	private double masterVolumeMultiplier;
	private double volume;

	public BPAudioPlayer(@Value("${app.player.track-path}") final String soundTrackPath,
			@Value("${app.player.start-master-volume-level}") final double masterVolumeLevel) {

		this.soundTrackPath = soundTrackPath;
		this.masterVolumeMultiplier = masterVolumeLevel;
		this.isPlaying = false;
		this.player = new BasicPlayer();
		initPlayer(soundTrackPath);
	}
	
	private void initPlayer(String soundTrackPath) {		
		try {			
			this.player.open(new URL(soundTrackPath));			
			this.player.play();
			this.setVolume(0d);
			this.player.stop();
		} catch (BasicPlayerException | MalformedURLException e) {
			LOG.error("Error occurred while initialising basic audio player", e);
		}
	}

	@Override
	public void playAudio() {
		try {
			this.player.play();
			setVolume(this.volume);
		} catch (BasicPlayerException e) {
			LOG.error("Error occurred while playing audio", e);
		}
	}

	@Override
	public void stopAudio() {
		try {
			player.stop();			
		} catch (BasicPlayerException e) {
			LOG.error("Error occurred while stopping audio", e);
		}
	}

	@Override
	public double getVolume() {	
		return this.volume;
	}

	@Override
	public void setVolume(double volume) {
		this.volume = volume;
		try {
			this.player.setGain(this.volume * masterVolumeMultiplier); 
		} catch (BasicPlayerException e) {
			LOG.error("Error occurred while setting volume", e);
		}
	}

	@Override
	public double getMasterVolumeMultiplier() {
		return masterVolumeMultiplier;
	}

	@Override
	public void setMasterVolumeMultiplier(double volume) {
		this.masterVolumeMultiplier = volume;
	}

	@Override
	public boolean isPlaying() {
		boolean result = player.getStatus() == BasicPlayer.PLAYING;
		
		return result;
	}

}
