package com.whitenoiseplayer.app.volume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whitenoiseplayer.app.player.AudioPlayer;

@Component
public class VolumeManager implements Runnable {
	private final AudioPlayer player;
	private final WeeklySchedule schedule;
	
	public VolumeManager(@Autowired AudioPlayer player, @Autowired WeeklySchedule schedule) {
		this.player = player;
		this.schedule = schedule;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
