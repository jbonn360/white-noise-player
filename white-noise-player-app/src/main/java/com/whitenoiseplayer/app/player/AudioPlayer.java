package com.whitenoiseplayer.app.player;

public interface AudioPlayer {
	public void playAudio();
	
	public void stopAudio();
	
	public double getVolume();
	
	public void setVolume(double volume);
	
	public double getMasterVolumeMultiplier();
	
	public void setMasterVolumeMultiplier(double volume);
	
	public boolean isPlaying();
}
