package com.whitenoiseplayer.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.http.HttpStatus;

import com.whitenoiseplayer.app.service.AudioPlayerService;

@Controller
public class HomeController {
	private final AudioPlayerService audioPlayerService;
	
	public HomeController(@Autowired AudioPlayerService audioPlayerService) {
		this.audioPlayerService = audioPlayerService;
	}
	
	@GetMapping("/")
	public String Index() {
		final ModelAndView modelAndView = new ModelAndView("home");
		
		modelAndView.addObject("audioPlayerVolume", audioPlayerService.getPlayerVolume());	
		
		return "home";
	}
	
	@PostMapping("/setAudioPlayerState")
	@ResponseStatus(HttpStatus.OK)
	public void setAudioPlayerState(boolean state) {
		audioPlayerService.switchStateTo(state);
	}
	
	@PostMapping("/setVolume")
	@ResponseStatus(HttpStatus.OK)
	public void setVolume(double volume) {
		audioPlayerService.setPlayerVolume(volume);
	}
}
