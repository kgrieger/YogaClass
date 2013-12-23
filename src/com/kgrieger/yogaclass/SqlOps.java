package com.kgrieger.yogaclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SqlOps extends SQLiteOpenHelper {

	public SqlOps(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public SqlOps(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE pose ( "+
				"pose_id     INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"name        TEXT, " +
				"description TEXT " +
				")");
		db.execSQL("CREATE TABLE class ( "+
				"class_id     INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"name        TEXT, " +
				"description TEXT " +
				")");
		db.execSQL("CREATE TABLE class_pose ( "
				+ "class_pose_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "class_id      INTEGER, "
				+ "pose_id       INTEGER, "
				+ "position      INTEGER, "
				+ "duration      INTEGER "
				+ ")");
		Log.d("YogaClass","Database successfully created.");
		}catch(Exception e)
		{
			Log.d("YogaClass", e.getMessage());
			
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public List<Pose> poseList(){
		SQLiteDatabase db=this.getWritableDatabase();
		List<Pose> posesList = new ArrayList<Pose>();
		Pose pose;
		Cursor cursor = db.rawQuery("select * from pose order by name", null);
	    if (cursor.moveToFirst()) {
	        do {
	        	pose = new Pose(
	        			cursor.getInt(0),
	        			cursor.getString(1),
	        			cursor.getString(2));
	        	posesList.add(pose);
	        } while (cursor.moveToNext());
	    }
	    return posesList;
	}
	
	public void poseCreate(Pose pose){
	 SQLiteDatabase db=this.getWritableDatabase();
	 SQLiteStatement stmt = db.compileStatement(
			 "insert into pose(name,description) values "
	 		+ "(?,?)");
	 stmt.bindString(1, pose.getName());
	 stmt.bindString(2, pose.getDescription());
	 stmt.executeInsert();
	}
	
	public void poseUpdate(Pose pose){
		 SQLiteDatabase db=this.getWritableDatabase();
		 SQLiteStatement stmt = db.compileStatement(
				 "update pose "
				 + "set name=?, "
				 + "description=? "
				 + "where pose_id=?");
		 stmt.bindString(1, pose.getName());
		 stmt.bindString(2, pose.getDescription());
		 stmt.bindLong(3, pose.getPoseId());
		 stmt.executeUpdateDelete();
	}
	
	public void poseDelete(Pose pose){
		 SQLiteDatabase db=this.getWritableDatabase();
		 SQLiteStatement stmt = db.compileStatement(
				 "delete from pose "
				 + "where pose_id = ? ");
		 stmt.bindString(1, Integer.toString(pose.getPoseId()));
		 stmt.executeUpdateDelete();
	}

}
