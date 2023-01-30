package com.whitenoiseplayer.app.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

@Component
public class VLCJAudioPlayer implements AudioPlayer {
	private final static Logger LOG = LoggerFactory.getLogger(VLCJAudioPlayer.class);

	private final MediaPlayer mediaPlayer;
	
	private final String soundTrackPath;
	private double masterVolumeMultiplier;
	private double volume;

	public VLCJAudioPlayer(@Value("${app.player.track-path}") final String soundTrackPath,
			@Value("${app.player.start-master-volume-level}") final double masterVolumeLevel) {	
		//boolean vlcFound = new NativeDiscovery().discover();
		this.mediaPlayer = new AudioPlayerComponent().mediaPlayer();

		this.soundTrackPath = soundTrackPath;
		this.masterVolumeMultiplier = masterVolumeLevel;
		
		this.setVolume(0d);		
		this.mediaPlayer.controls().setRepeat(true);
		this.mediaPlayer.media().prepare(soundTrackPath);
	}

	@Override
	public void playAudio() {
		mediaPlayer.controls().start();
	}

	@Override
	public void stopAudio() {
		mediaPlayer.controls().stop();
	}

	@Override
	public double getVolume() {
		return volume;
	}

	@Override
	public void setVolume(double volume) {
		this.volume = volume;
		
		int volumeInt = (int)((this.volume * masterVolumeMultiplier) * 150);
		
		LOG.info("Setting volume to: " + volumeInt);
		
		mediaPlayer.audio().setVolume(volumeInt);
	}

	@Override
	public double getMasterVolumeMultiplier() {
		return masterVolumeMultiplier;
	}

	@Override
	public void setMasterVolumeMultiplier(double masterVolumeMultiplier) {
		this.masterVolumeMultiplier = masterVolumeMultiplier;
	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.status().isPlaying();
	}

}
