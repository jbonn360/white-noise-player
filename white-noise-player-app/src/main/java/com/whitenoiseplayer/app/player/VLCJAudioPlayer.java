package com.whitenoiseplayer.app.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

@Component
public class VLCJAudioPlayer implements AudioPlayer {
	private final static Logger LOG = LoggerFactory.getLogger(VLCJAudioPlayer.class);

	private final HeadlessMediaPlayer mediaPlayer;
	
	private final String soundTrackPath;
	private double masterVolumeMultiplier;
	private double volume;

	public VLCJAudioPlayer(@Value("${app.player.track-path}") final String soundTrackPath,
			@Value("${app.player.start-master-volume-level}") final double masterVolumeLevel) {
		boolean vlcFound = new NativeDiscovery().discover();

		if (vlcFound)
			LOG.info(String.format("Vlc media player instance has been found. Version is: %s",
					LibVlc.INSTANCE.libvlc_get_version()));
		else {
			Error e = new Error("Vlc instance not found");
			LOG.error("Vlc media player instance could not be found. Please install it and try again.", e);
			throw e;
		}
		
		this.mediaPlayer = new MediaPlayerFactory().newHeadlessMediaPlayer();

		this.soundTrackPath = soundTrackPath;
		this.masterVolumeMultiplier = masterVolumeLevel;
		
		this.setVolume(0d);		
		this.mediaPlayer.setRepeat(true);
	}

	@Override
	public void playAudio() {
		mediaPlayer.playMedia(soundTrackPath);
	}

	@Override
	public void stopAudio() {
		mediaPlayer.stop();
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
		
		mediaPlayer.setVolume(volumeInt);
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
		return mediaPlayer.isPlaying();
	}

}
