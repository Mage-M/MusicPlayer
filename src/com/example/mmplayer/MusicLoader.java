package com.example.mmplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

public class MusicLoader {

	private static final String TAG = "loader";
	
	
	private static List<MusicInfo> musicList = new ArrayList<MusicInfo>();  
	
	private static MainActivity mMainActivity;
	
	
	public static List loader() {
		ContentResolver cr = mMainActivity.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		
		if(cursor == null) {
			Log.e(TAG, "…®√Ë ß∞‹");
		} else if(!cursor.moveToFirst()) {
			Log.e(TAG, "…®√Ë¥ÌŒÛ");
		} else {
			int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);  
            int albumCol = cursor.getColumnIndex(Media.ALBUM);  
            int idCol = cursor.getColumnIndex(Media._ID);  
            int durationCol = cursor.getColumnIndex(Media.DURATION);  
            int sizeCol = cursor.getColumnIndex(Media.SIZE);  
            int artistCol = cursor.getColumnIndex(Media.ARTIST);  
            int urlCol = cursor.getColumnIndex(Media.DATA); 
            do{  
                String title = cursor.getString(displayNameCol);  
                String album = cursor.getString(albumCol);  
                long id = cursor.getLong(idCol);                  
                int duration = cursor.getInt(durationCol);  
                long size = cursor.getLong(sizeCol);  
                String artist = cursor.getString(artistCol);  
                String url = cursor.getString(urlCol);  
                  
                MusicInfo musicInfo = new MusicInfo(id, title);  
                musicInfo.setAlbum(album);  
                musicInfo.setDuration(duration);  
                musicInfo.setSize(size);  
                musicInfo.setArtist(artist);  
                musicInfo.setUrl(url);  
                musicList.add(musicInfo);  
                  
            }while(cursor.moveToNext()); 
		}
		
		return musicList;
	}

	
}