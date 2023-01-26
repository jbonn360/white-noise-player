package com.whitenoiseplayer.app.volume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whitenoiseplayer.app.player.AudioPlayer;

@Component
public class VolumeManager implements Runnable {
	private final AudioPlayer player;
	private final 
	
	public VolumeManager(@Autowired AudioPlayer player) {
		this.player = player;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
