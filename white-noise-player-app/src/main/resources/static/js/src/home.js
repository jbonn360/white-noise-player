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
	button.css('background-color', '#3e2828');
	button.text('STOP');
}

function setMasterButtonToStartMode(button) {
	button.removeAttr('style');
	button.text('START');
}

function postMasterSwitchState(state){
	$.ajax({
		url: 'audioPlayerState',
		type: 'PATCH',
		data: { 'state' : state }
	});
}

function postVolumeChange(value){
	var newVolume = value / 100;	
		
	$.ajax({
		url: 'masterVolume',
		type: 'PATCH',
		data: { 'volume' : newVolume }
	});
}

function disablePageDrag(){
	$('html').attr('ontouchstart', '');
}

function sendOverrideRequest(value){	
	$.ajax({
		url: 'playbackOverride',
		type: 'PATCH',
		data: { 'value' : value },
		success: function(result){
    		var button = $('#btnToggleAudioPlayer');   		
    		
    		if(button.text() === 'STOP' && result == false){	
				toggleMasterSwitch(button.attr('id'));			
			}
  		}
	});
}