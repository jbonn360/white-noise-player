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
	private boolean executionScheduled;
	
	public VolumeManagerExecutor(@Autowired VolumeManager volumeManager) {
		executor = initExecutor();
		this.volumeManager = volumeManager;
		this.executionScheduled = false;
	}
	
	public void executeAt(ZonedDateTime target) {
		if(executor.isShutdown())
			executor = initExecutor();
		
		long millisToTarget = ZonedDateTime.now().until(target, ChronoUnit.MILLIS);		
		executor.schedule(volumeManager, millisToTarget, TimeUnit.MILLISECONDS);
		executionScheduled = true;
	}
	
	public boolean isExecutionScheduled() {
		return executionScheduled;
	}
	
	public void clearSchedule() {
		if(!executor.isShutdown()) {
			executor.shutdownNow();
			executionScheduled = false;
		}
			
	}
	
	private ScheduledExecutorService initExecutor() {
		return Executors.newScheduledThreadPool(1);
	}
}
