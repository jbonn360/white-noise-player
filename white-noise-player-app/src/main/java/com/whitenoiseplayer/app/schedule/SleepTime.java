package com.whitenoiseplayer.app.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SleepTime {	
	private final int sleepTimeStart;
	private final int sleepTimeStop;
	private final int sleepTimeStartWeekend;
	private final int sleepTimeStopWeekend;
	
	public SleepTime(@Value("${app.schedule.sleep-time-start}") int sleepTimeStart,
			@Value("${app.schedule.sleep-time-stop}") int sleepTimeEnd,
			@Value("${app.schedule.sleep-time-start.weekend}") int sleepTimeStartWeekend,
			@Value("${app.schedule.sleep-time-stop.weekend}") int sleepTimeEndWeekend) {
		this.sleepTimeStart = sleepTimeStart;
		this.sleepTimeStop = sleepTimeEnd;
		this.sleepTimeStartWeekend = sleepTimeStartWeekend;
		this.sleepTimeStopWeekend = sleepTimeEndWeekend;
	}

	public int getSleepTimeStart() {
		return sleepTimeStart;
	}

	public int getSleepTimeStop() {
		return sleepTimeStop;
	}

	public int getSleepTimeStartWeekend() {
		return sleepTimeStartWeekend;
	}

	public int getSleepTimeStopWeekend() {
		return sleepTimeStopWeekend;
	}
}
