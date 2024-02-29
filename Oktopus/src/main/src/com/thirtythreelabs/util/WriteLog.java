package com.thirtythreelabs.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLog {
	
	private final static String LOG_TAG = "WriteLog";

	public void appendLog(String text){

//		File logFile = new File("/sdcard/download/Oktopus.log");
//		File logFile = new File(Environment.getExternalStorageDirectory().getPath() + "Oktopus.log");

		Log.v("################", text);

//		if (!logFile.exists()){
//			try {
//				logFile.createNewFile();
//			} catch (IOException e){
//
//				e.printStackTrace();
//			}
//		}
//		try {
//			String mDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date());
//
//			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//			buf.append(mDate + ": " + text);
//			buf.newLine();
//			buf.close();
//		}catch (IOException e){
//
//			e.printStackTrace();
//		}
	}
}
