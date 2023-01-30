package com.whitenoiseplayer.app.player;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class BPAudioPlayerLooper implements BasicPlayerListener {
	private final static Logger LOG = LoggerFactory.getLogger(BasicPlayerListener.class);
	private final BPAudioPlayer player;

	public BPAudioPlayerLooper(BPAudioPlayer player) {
		this.player = player;
	}

	@Override
	public void opened(Object stream, Map properties) {
		// TODO Auto-generated method stub

	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		// EOM code signifies the end of a track (end of media?)
		if (event.getCode() == BasicPlayerEvent.EOM) {
			player.setVolume(player.getVolume());
			player.playAudio();
		}
	}

	@Override
	public void setController(BasicController controller) {
		// TODO Auto-generated method stub

	}

}
