$(function() {
	initMasterSwitchButton('#btnToggleAudioPlayer', audioPlayerState);
});

function initMasterSwitchButton(buttonId, state){
	var button = $(buttonId);

	if(state)		
		setMasterButtonToStopMode(button);
}

function toggleMasterSwitch(buttonId) {
	var button = $('#' + buttonId);
	
	if(button.text() === 'START'){
		postMasterSwitchState(true);
		setMasterButtonToStopMode(button);
	} else {
		postMasterSwitchState(false);
		setMasterButtonToStartMode(button);
	}
}

function setMasterButtonToStopMode(button) {
	button.css('background-color', 'red');
	button.text('STOP');
}

function setMasterButtonToStartMode(button) {
	button.removeAttr('style');
	button.text('START');
}

function postMasterSwitchState(state){
	$.post('setAudioPlayerState', { state });
}

function postVolumeChange(value){
	var newVolume = value / 100;	
	
	$.post('setVolume', { 'volume' : newVolume });
}

function disablePageDrag(){
	$('html').attr('ontouchstart', '');
}