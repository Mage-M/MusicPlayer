package com.example.mmplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private Button scan = null;
	private Button last = null;
	private Button next = null;
	private Button start = null;
	private Button stop = null;
	private TextView runing_name = null;
	private TextView duration_time = null;
	
	private ListView list = null;
	private int[] _id;				//内存中歌曲的id数组
	private String[] title ;
	private int old_id;			//按下停止时记录的歌曲id
	private int old_position;  	//按下记录当前按下的position
	
	private MediaPlayer mmp = new MediaPlayer();
	private Uri uri = null;		//内存中歌曲对应的Uri地址
	
	private boolean start_flog = false;   //音乐是否正在播放的标志位
	private update_runing_name_Receiver rnr;	//这个广播用来改变正在播放的歌曲名
	private IntentFilter intentFilter;
	
	private static List<MusicInfo> mList = new ArrayList<MusicInfo>();
	private static List<String> aList = new ArrayList<String>();
	private MusicLoader mMusicLoader = new MusicLoader();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		start = (Button)findViewById(R.id.start);
		stop = (Button)findViewById(R.id.stop);
		next = (Button)findViewById(R.id.next);
		last = (Button)findViewById(R.id.last);
		scan = (Button)findViewById(R.id.scan);
		runing_name = (TextView)findViewById(R.id.runing_name);
		duration_time = (TextView)findViewById(R.id.duration_time);
		
		scan.setOnClickListener(this);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		next.setOnClickListener(this);
		last.setOnClickListener(this);
		
		list = (ListView)findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(getApplicationContext(), "点击"+position, Toast.LENGTH_SHORT).show();
				//playMusic(position);
				uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + _id[position]);
				old_position = position;
				old_id = _id[position];
				mmp.reset();
				changeName(old_position);
				try {
					mmp.setDataSource(getApplicationContext(), uri);
					mmp.prepare();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				mmp.start();
				start_flog = true;
				System.out.println(start_flog);
				
				if(start_flog) {
					start.setText("暂停");
					System.out.println("############");
					start_flog = true;
				}
			}
		});
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("Song.Change");
		rnr = new update_runing_name_Receiver();
		registerReceiver(rnr, intentFilter);
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.scan:
				Cursor c = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						new String[]{
						MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.ALBUM_ID},
						null, null, null);
				if (c==null || c.getCount()==0){
					new AlertDialog.Builder(this).setTitle("抱歉").setMessage("未找到音乐！").setPositiveButton("确定", null).create();
					return;
			    }
				c.moveToFirst();
				title = new String[c.getCount()];
				_id = new int[c.getCount()];
				for(int i =0;i<c.getCount();i++) {
					title[i] = c.getString(0);
					_id[i]= c.getInt(3);
					System.out.println(title[i]);
					System.out.println("+++++++++"+_id[i]);
					c.moveToNext();
				}
				ArrayAdapter<String> a = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,title);
				list.setAdapter(a);
				break;
			case R.id.start:
				System.out.println("按了start");
				if(mmp.isPlaying() && start_flog){
					mmp.pause();
					start_flog = false;
					start.setText("播放");
				} else {
					mmp.start();
					start_flog = true;
					start.setText("暂停");
				}
				break;
			case R.id.stop:
				if(mmp.isPlaying()) {
					uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"" + old_id);
					mmp.reset();
					try {
						mmp.setDataSource(getApplicationContext(), uri);
						mmp.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				start_flog = false;
				start.setText("播放");
				break;
			case R.id.next:
				if(old_position + 1<list.getCount()) {
					uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"" + _id[old_position + 1]);
					old_position+=1;
					changeName(old_position);
					mmp.reset();
					try {
						mmp.setDataSource(getApplicationContext(), uri);
						mmp.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					mmp.start();
				} else {
					old_position = 0;
					changeName(old_position);
					uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"" + _id[old_position]);
					mmp.reset();
					try {
						mmp.setDataSource(getApplicationContext(), uri);
						mmp.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					mmp.start();
				}
				break;
			case R.id.last:
				if(old_position - 1 >= 0) {
					uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"" + _id[old_position - 1]);
					old_position-=1;
					changeName(old_position);
					mmp.reset();
					try {
						mmp.setDataSource(getApplicationContext(), uri);
						mmp.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					mmp.start();
				} else {
					old_position = list.getCount() - 1;
					changeName(old_position);
					uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"" + _id[old_position]);
					mmp.reset();
					try {
						mmp.setDataSource(getApplicationContext(), uri);
						mmp.prepare();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					mmp.start();
				}
				break;
			default:
				break;
		}
		
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(rnr);
		if(mmp != null) {
			mmp.stop();
			mmp.release();
		}
	}


	private class update_runing_name_Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int position = intent.getIntExtra("position", 0);
			runing_name.setText("正在播放："+title[position]);
			int duration = mmp.getDuration();
			String durationtime = duration/60000 + ":" + ((duration-(duration/60000)*60*1000)/1000)%100;
			duration_time.setText("/" + durationtime);
		}
		
	}
	
	//此方法用在在歌曲改变时发送改变正在播放歌曲名字的广播
	private void changeName(int position) {
		Intent intent = new Intent("Song.Change");
		intent.putExtra("position", position);
		sendBroadcast(intent);
	}
//	public void playMusic(int position) {
//		Intent intent = new Intent(MainActivity.this,MusicService.class);
//		intent.putExtra("position", position);
//		intent.putExtra("_id", _id);
//		startService(intent);
//	}
}
