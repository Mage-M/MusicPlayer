package com.example.mmplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener{

	private Button scan = null;
	private Button last = null;
	private Button next = null;
	private Button start = null;
	private Button stop = null;
	
	private ListView list = null;
	
	private static List<MusicInfo> mList = new ArrayList<MusicInfo>();
	private static List<String> aList = new ArrayList<String>();
	private MusicLoader mMusicLoader = new MusicLoader();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		scan = (Button)findViewById(R.id.scan);
		scan.setOnClickListener(this);
		
		list = (ListView)findViewById(R.id.list);
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
					new AlertDialog.Builder(this).setTitle("±ß«∏").setMessage("Œ¥’“µΩ“Ù¿÷£°").setPositiveButton("»∑∂®", null).create();
					return;
			    }
				c.moveToFirst();
				String[] title = new String[c.getCount()];
				for(int i =0;i<c.getCount();i++) {
					title[i] = c.getString(0);
					System.out.println(title[i]);
					c.moveToNext();
				}
				ArrayAdapter<String> a = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,title);
				list.setAdapter(a);
				break;
			default:
				break;
		}
		
	}

	
}
