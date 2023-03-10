package com.whitenoiseplayer.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.whitenoiseplayer.app.service.AudioPlayerService;

@Controller
public class HomeController {
	private final AudioPlayerService audioPlayerService;

	public HomeController(@Autowired AudioPlayerService audioPlayerService) {
		this.audioPlayerService = audioPlayerService;
	}

	@GetMapping("/")
	public ModelAndView Index() {
		final ModelAndView modelAndView = new ModelAndView("home");

		modelAndView.addObject("audioPlayerState", audioPlayerService.isActive());
		modelAndView.addObject("masterVolumeLevel", audioPlayerService.getPlayerMasterVolume());
		modelAndView.addObject("playbackOverride", audioPlayerService.isPlaybackOverride());

		return modelAndView;
	}

	@PatchMapping("/audioPlayerState")
	@ResponseStatus(HttpStatus.OK)
	public void setAudioPlayerState(boolean state) {
		audioPlayerService.switchStateTo(state);
	}

	@PatchMapping("/masterVolume")
	@ResponseStatus(HttpStatus.OK)
	public void setMasterVolume(double volume) {
		audioPlayerService.setPlayerMasterVolume(volume);
	}
	
	@PatchMapping("/playbackOverride")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean overridePlayback(boolean value) {
		boolean buttonStatus = audioPlayerService.setPlaybackOverride(value);
		
		return buttonStatus;
	}
}
