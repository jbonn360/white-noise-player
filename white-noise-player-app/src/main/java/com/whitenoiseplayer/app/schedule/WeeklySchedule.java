package com.whitenoiseplayer.app.schedule;

import java.time.ZonedDateTime;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.whitenoiseplayer.app.util.Util;

@Component
public class WeeklySchedule {
	// 0 = MONDAY, 6 = SUNDAY
	// 0 = 12am, 23 = 11pm
	private final double[][] volumes;
	
	public WeeklySchedule(@Value("classpath:static/json/schedule.json") final Resource scheduleFile) {		
		final String scheduleJson = Util.resourceToString(scheduleFile);		
		this.volumes = parseScheduleString(scheduleJson);
	}
	
	// 0 = MONDAY, 6 = SUNDAY
	// 0 = 12am, 23 = 11pm
	public double getScheduledVolume(ZonedDateTime currentTime, boolean fadeInOut) {	
		double lastVolume = getLastScheduledVolume(currentTime);
		double nextVolume = getNextScheduledVolume(currentTime);
		
		// if no fade in/out needed, then just switch to the next volume
		if(!fadeInOut)
			return nextVolume;
		
		// if next volume is same, just return same volume
		if(lastVolume == nextVolume)
			return lastVolume;
		
		// if next volume is different, we need to calculate the correct level
		// at this point in time
		int minutesSinceLastHour = currentTime.getMinute();		
		double curVolumeChange = (minutesSinceLastHour / 60) * ( nextVolume - lastVolume );
		
		return lastVolume + curVolumeChange;
	}
	
	public double getLastScheduledVolume(ZonedDateTime currentTime) {
		int dayOfWeek = getDayOfWeek(currentTime);
		int hourOfDay = getHourOfDay(currentTime);
		
		return volumes[dayOfWeek][hourOfDay];
	}
	
	public double getNextScheduledVolume(ZonedDateTime currentTime) {
		int dayOfWeek = getDayOfWeek(currentTime);
		int hourOfDay = getHourOfDay(currentTime);
		
		double nextVolume = 0d;
		
		if(hourOfDay == 23)
			nextVolume = volumes[dayOfWeek + 1 % 7][0];
		else
			nextVolume = volumes[dayOfWeek][hourOfDay + 1];
		
		 return nextVolume;
	}
	
	private int getDayOfWeek(ZonedDateTime currentTime) {
		return currentTime.getDayOfWeek().getValue() - 1;
	}
	
	private int getHourOfDay(ZonedDateTime currentTime) {
		return currentTime.getHour();
	}
	
	private double[][] parseScheduleString(String contents){
		double[][] result = new double[7][24];
		JSONArray array = new JSONArray(contents);
		
		for(int i = 0; i < array.length(); i++) {
			JSONArray subArray = (JSONArray)array.get(i);
			
			for(int j = 0; j < subArray.length(); j++) 
				result[i][j] = subArray.getInt(j);			
		}
		
		return result;
	}
}
