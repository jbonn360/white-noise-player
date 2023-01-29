package com.whitenoiseplayer.app.player;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

//@Component
public class JavaAudioPlayer implements AudioPlayer {
	private final static Logger LOG = LoggerFactory.getLogger(JavaAudioPlayer.class);

	private final MediaPlayer mediaPlayer;
	private boolean isPlaying;

	private double masterVolumeMultiplier;

	public JavaAudioPlayer(@Value("classpath:static/audio/track.mp3") final Resource audioFileResource,
			@Value("${app.player.start-master-volume-level}") final double masterVolumeLevel) {
		Optional<URI> audioFileURI = Optional.empty();
		try {
			audioFileURI = Optional.ofNullable(audioFileResource.getURI());
		} catch (IOException e) {
			LOG.error(String.format("Audio file not found '%s'", audioFileURI), e);
		}

		// needed for the media player to be able to play audio
		new JFXPanel();

		this.masterVolumeMultiplier = masterVolumeLevel;
		this.mediaPlayer = audioFileURI.map(URI::toString).map(Media::new).map(MediaPlayer::new).orElseThrow();
		this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		this.isPlaying = false;

		// this.mediaPlayer.setVolume(1d);
	}

	@Override
	public void playAudio() {
		this.mediaPlayer.stop();
		this.mediaPlayer.play();
		this.isPlaying = true;
	}

	@Override
	public void stopAudio() {
		this.mediaPlayer.stop();
		this.isPlaying = false;
	}

	@Override
	public double getVolume() {
		return this.mediaPlayer.getVolume();
	}

	@Override
	public void setVolume(double volume) {
		this.mediaPlayer.setVolume(volume * masterVolumeMultiplier);
	}

	@Override
	public boolean isPlaying() {
		return this.isPlaying;
	}

	@Override
	public void setMasterVolumeMultiplier(double volume) {
		this.masterVolumeMultiplier = volume;
	}

	@Override
	public double getMasterVolumeMultiplier() {
		return this.masterVolumeMultiplier;
	}
}
