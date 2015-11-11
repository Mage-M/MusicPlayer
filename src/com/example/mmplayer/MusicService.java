package com.example.mmplayer;

import java.io.IOException;

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
	private int[] _id;
	
	
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
		System.out.println(">>>>>>>>"+position);
		_id = intent.getIntArrayExtra("_id");
		System.out.println(">>>>>>>>"+_id[position]);
		uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id[position]);
		System.out.println(">>>>>>>>"+uri);
		//DBOperate(id);
		if(mp!= null) {
			mp.start();
		} else {
			mp = new MediaPlayer();
			try {
				mp.setDataSource(this,uri);
				mp.prepare();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mp.prepare();
			mp.start();
		}
//		try {
//			mp.reset();
//			mp.setDataSource(this, uri);
//			mp.prepare();
//			mp.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
		super.onDestroy();
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
