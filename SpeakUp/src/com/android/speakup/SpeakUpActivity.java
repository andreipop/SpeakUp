package com.android.speakup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class SpeakUpActivity extends Activity implements OnInitListener {
	
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech tts;
	private EditText inputText;
	private Button speakUp;
	private Button clearText;
	private AudioManager volumeManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        volumeManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = volumeManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = volumeManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar volumeControl = (SeekBar)findViewById(R.id.VolumeBar);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curVolume);
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				volumeManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
			}
		});
        
        
        Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	
        initControls();
    }

	private void initControls() {
		inputText = (EditText) findViewById(R.id.InputField);
		speakUp = (Button) findViewById(R.id.SpeakButton);
		clearText = (Button) findViewById(R.id.ClearField);
		
		
		speakUp.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				SpeakText(v);
			}
		});
		
		clearText.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Clear(v);
			}
		});
		
		
	}
	
	
	private void SpeakText(View v) {
		String text = inputText.getText().toString();
		if(text!=null && text.length()>0) {
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	private void Clear(View v) {
		inputText.setText("");
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			} 
			else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}

	}

	@Override
	public void onInit(int status) {		
		if (status == TextToSpeech.SUCCESS) {
			Toast.makeText(SpeakUpActivity.this, 
					"Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
		}
		else if (status == TextToSpeech.ERROR) {
			Toast.makeText(SpeakUpActivity.this, 
					"Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
		}
	}
	
}
