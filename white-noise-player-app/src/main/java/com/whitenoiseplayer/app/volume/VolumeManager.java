package com.whitenoiseplayer.app.volume;

import static com.whitenoiseplayer.app.util.Util.toSysDefault;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.whitenoiseplayer.app.player.AudioPlayer;
import com.whitenoiseplayer.app.schedule.SleepTime;
import com.whitenoiseplayer.app.schedule.WeeklySchedule;

@Component
public class VolumeManager implements Runnable {	
	private final static Logger LOG = LoggerFactory.getLogger(VolumeManager.class);
	
	private final AudioPlayer player;
	private final VolumeManagerExecutor volumeManagerExecutor;

	private final WeeklySchedule schedule;
	private final SleepTime sleepTime;

	public VolumeManager(@Autowired AudioPlayer player, @Lazy VolumeManagerExecutor volumeManagerExecutor,
			@Autowired WeeklySchedule schedule, @Autowired SleepTime sleepTime) {
		this.player = player;
		this.volumeManagerExecutor = volumeManagerExecutor;
		this.schedule = schedule;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run() {
		final ZonedDateTime now = toSysDefault(Instant.now());		
		
		// set volume to new level according to current time
		final double newVolume = schedule.getScheduledVolume(now, isSleepTime(now));		
		LOG.info(String.format("Setting volume to %.6f", newVolume));		
		player.setVolume(newVolume);
		
		// ensure that player is in the correct state
		if(player.getVolume() <= 0d && player.isPlaying()) 
			player.stopAudio();
		else if(player.getVolume() > 0d && !player.isPlaying())
			player.playAudio();
		
		// schedule the next run
		volumeManagerExecutor.executeAt(getNextRequiredRunTime(now));
	}

	private ZonedDateTime getNextRequiredRunTime(ZonedDateTime now) {		
		ZonedDateTime nextTopOfHour = getNextTopOfHour(now);

		if (isInFadeInOutZone(now)) {
			ZonedDateTime fiveMinutesFromNow = now.plus(1, ChronoUnit.MINUTES);
			return fiveMinutesFromNow.isBefore(nextTopOfHour) ? fiveMinutesFromNow : nextTopOfHour;
		} else {
			if (schedule.getLastScheduledVolume(now) > 0)
				return nextTopOfHour;
			else
				return getNextNonZeroVolumeTime(now);
		}
	}
	
	private ZonedDateTime getNextTopOfHour(ZonedDateTime time) {
		return time.plusMinutes(60 - time.getMinute());
	}

	private ZonedDateTime getNextNonZeroVolumeTime(ZonedDateTime now) {
		double lastVolume = schedule.getLastScheduledVolume(now);
		double nextVolume = schedule.getNextScheduledVolume(now);

		ZonedDateTime result = now;

		if (lastVolume != 0)
			throw new Error("Something went wrong...");

		while (nextVolume == 0) {
			result = result.plus(1, ChronoUnit.HOURS);
			nextVolume = schedule.getNextScheduledVolume(result);
		}

		return getNextTopOfHour(result);
	}

	private boolean isInFadeInOutZone(ZonedDateTime now) {
		if (!isSleepTime(now))
			return false;

		double lastVolume = schedule.getLastScheduledVolume(now);
		double nextVolume = schedule.getNextScheduledVolume(now);

		return (lastVolume != nextVolume);
	}

	private boolean isSleepTime(ZonedDateTime now) {
		int sleepStart = -1;
		int sleepStop = -1;

		if (!isWeekend(now)) {
			sleepStart = sleepTime.getSleepTimeStart();
			sleepStop = sleepTime.getSleepTimeStop();
		} else {
			sleepStart = sleepTime.getSleepTimeStartWeekend();
			sleepStop = sleepTime.getSleepTimeStopWeekend();
		}

		return !(now.getHour() > sleepStop && now.getHour() < sleepStart);
	}

	private boolean isWeekend(ZonedDateTime now) {
		return (now.getDayOfWeek().equals(DayOfWeek.SATURDAY) || now.getDayOfWeek().equals(DayOfWeek.SUNDAY));
	}
}
