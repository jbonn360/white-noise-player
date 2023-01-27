package com.whitenoiseplayer.app.volume;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VolumeManagerExecutor {
	private ScheduledExecutorService executor;
	private final VolumeManager volumeManager;
	
	public VolumeManagerExecutor(@Autowired VolumeManager volumeManager) {
		executor = initExecutor();
		this.volumeManager = volumeManager;
	}
	
	public void executeAt(ZonedDateTime target) {
		if(executor.isShutdown())
			executor = initExecutor();
		
		long millisToTarget = ZonedDateTime.now().until(target, ChronoUnit.MILLIS);		
		executor.schedule(volumeManager, millisToTarget, TimeUnit.MILLISECONDS);
	}
	
	public void clearSchedule() {
		executor.shutdownNow();
	}
	
	private ScheduledExecutorService initExecutor() {
		return Executors.newScheduledThreadPool(1);
	}
}
