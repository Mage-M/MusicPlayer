package com.example.mmplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

public class MusicService extends Service{
	
	public static  MediaPlayer mp = null;
	
	private Uri uri = null;	
	private int position;
	private int id;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (mp != null) {
			mp.reset();
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		
		position = intent.getIntExtra("position", -1);
		id = intent.getIntExtra("id", -1);
		uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + id);
		//DBOperate(id);
		try {
			mp.reset();
			mp.setDataSource(this, uri);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mp != null) {
			mp.stop();
			mp = null;
		}
		if (!mp.isPlaying()) {
			play();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void play() {
		mp.start();
	}
	
	//private void DB
	
}
