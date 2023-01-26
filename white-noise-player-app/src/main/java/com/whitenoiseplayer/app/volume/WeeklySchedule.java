package com.whitenoiseplayer.app.volume;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.whitenoiseplayer.app.util.Util;

@Component
public class WeeklySchedule {
	private double[][] volumes;
	
	// 0 = MONDAY
	// 1 = TUESDAY
	// 6 = SUNDAY
	public WeeklySchedule(double[][] volumes, @Value("classpath:static/json/schedule.json") final Resource scheduleFile) {
		this.volumes = volumes;
		
		String scheduleString = null;
		try {
			scheduleString = Util.readFileContents(scheduleFile.getURI().toString());
		} catch (IOException e) {
			throw new Error("Failed to read schedule file", e);
		}
		
		JSONArray array = new JSONArray(scheduleString);
		
		for(int i = 0; i < array.length(); i++) {
			JSONArray subArray = new JSONArray(array.get(i));
			
			for(int j = 0; j < subArray.length(); j++) 
				volumes[i][j] = subArray.getInt(j);			
		}
	}
	
	public double getCurrentVolume(ZonedDateTime currentTime, boolean fadeInOut) {
		int dayOfWeek = currentTime.getDayOfWeek().getValue() - 1;
		int hourOfDay = currentTime.getHour();
		
		double lastVolume = volumes[dayOfWeek][hourOfDay];		
		double nextVolume = 0d;
		
		if(hourOfDay == 23)
			nextVolume = volumes[dayOfWeek + 1 % 7][0];
		else
			nextVolume = volumes[dayOfWeek][hourOfDay + 1];
		
		// if next volume is same, just return same volume
		if(lastVolume == nextVolume)
			return lastVolume;
		
		// if next volume is different, we need to calculate the correct level
		// at this point in time
		int minutesSinceLastHour = currentTime.getMinute();
		
		double curVolumeChange = (minutesSinceLastHour / 60) * ( nextVolume - lastVolume );
		
		return lastVolume + curVolumeChange;
	}
}
