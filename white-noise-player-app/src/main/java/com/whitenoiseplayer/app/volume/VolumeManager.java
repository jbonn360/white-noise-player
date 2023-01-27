package com.whitenoiseplayer.app.volume;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.whitenoiseplayer.app.player.AudioPlayer;
import com.whitenoiseplayer.app.schedule.SleepTime;
import com.whitenoiseplayer.app.schedule.WeeklySchedule;

import static com.whitenoiseplayer.app.util.Util.toSysDefault;

@Component
public class VolumeManager implements Runnable {
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
		
		player.setVolume(schedule.getScheduledVolume(now, isSleepTime(now)));
		
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
			ZonedDateTime fiveMinutesFromNow = now.plus(5, ChronoUnit.MINUTES);
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
