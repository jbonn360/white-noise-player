package com.whitenoiseplayer.app.volume;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class WeeklySchedule {
	private double[][] volumes;
	
	// 1 = MONDAY
	// 2 = TUESDAY
	//....
	// 7 = SUNDAY
	public WeeklySchedule(double[][] volumes) {
		this.volumes = volumes;
	}
	
	public double getCurrentVolume(ZonedDateTime currentTime, boolean fadeInOut) {
		int dayOfWeek = currentTime.getDayOfWeek().getValue() - 1;
		int hourOfDay = currentTime.getHour();
		
		int minutesSinceLastHour = currentTime.getMinute();
		
	}
}
