package com.kgrieger.yogaclass;

import java.util.ArrayList;
import java.util.List;

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
		db.execSQL("CREATE TABLE session ( "+
				"session_id     INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"name        TEXT, " +
				"description TEXT " +
				")");
		db.execSQL("CREATE TABLE session_pose ( "
				+ "session_pose_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "session_id    INTEGER, "
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
		try{
			db.execSQL("drop table if exists pose");
			db.execSQL("drop table if exists class");
			db.execSQL("drop table if exists class_pose");
			onCreate(db);	
		}catch(Exception e){
			Log.d("YogaClass", e.getMessage());
		}
		
	}
	
	public void log(String message){
		Log.d("YogaClass",message);
	}
	
	public void log(String message,Object args){
		Log.d("YogaClass",String.format(message, args));
	}
	
	public void log(String message, Object arg1, Object arg2){
		Log.d("YogaClass",String.format(message, arg1, arg2));
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
	    cursor.close();
	    return posesList;
	}
	
	public Pose poseRead(int pose_id){
		SQLiteDatabase db=this.getWritableDatabase();
		Pose pose = null;
		Cursor cursor = db.rawQuery("select * from pose "
				+ String.format("where pose_id = %d ", pose_id)
				+ "order by name", null);
	    if (cursor.moveToFirst()) {
        	pose = new Pose(
        			cursor.getInt(0),
        			cursor.getString(1),
        			cursor.getString(2));
	    }
	    cursor.close();
	    return pose;
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
	
	public List<Session> sessionList(){
		SQLiteDatabase db=this.getWritableDatabase();
		List<Session> sessionsList = new ArrayList<Session>();
		Session session;
		Cursor cursor = db.rawQuery("select * from session order by name", null);
	    if (cursor.moveToFirst()) {
	        do {
	        	session = new Session(
	        			cursor.getInt(0),
	        			cursor.getString(1),
	        			cursor.getString(2));
	        	sessionsList.add(session);
	        } while (cursor.moveToNext());
	    }
	    return sessionsList;
	}
	
	public void sessionCreate(Session session){
		 SQLiteDatabase db=this.getWritableDatabase();
		 SQLiteStatement stmt = db.compileStatement(
				 "insert into session(name,description) values "
		 		+ "(?,?)");
		 stmt.bindString(1, session.getName());
		 stmt.bindString(2, session.getDescription());
		 stmt.executeInsert();
		}
		
		public void sessionUpdate(Session session){
			 SQLiteDatabase db=this.getWritableDatabase();
			 SQLiteStatement stmt = db.compileStatement(
					 "update session "
					 + "set name=?, "
					 + "description=? "
					 + "where session_id=?");
			 stmt.bindString(1, session.getName());
			 stmt.bindString(2, session.getDescription());
			 stmt.bindLong(3, session.getSessionId());
			 stmt.executeUpdateDelete();
		}
		
		public void sessionDelete(Session session){
			 SQLiteDatabase db=this.getWritableDatabase();
			 SQLiteStatement stmt = db.compileStatement(
					 "delete from session "
					 + "where session_id = ? ");
			 stmt.bindString(1, Integer.toString(session.getSessionId()));
			 stmt.executeUpdateDelete();
		}
		

		public List<SessionPose> sessionPoseList(int session_id){
			SQLiteDatabase db=this.getWritableDatabase();
			List<SessionPose> sessionPoseList = new ArrayList<SessionPose>();
			String query = "select * from session_pose sp "
					+ "inner join pose p on sp.pose_id = p.pose_id "
					+ "where sp.session_id = ? "
					+ "order by sp.position desc";
			Cursor cursor = db.rawQuery(query,new String[]{Integer.toString(session_id)});
	    	if (cursor.moveToFirst()) {
		        do {
		        	Pose pose = new Pose(
			        			cursor.getInt(5),
			        			cursor.getString(6),
			        			cursor.getString(7)
		        			);
		        	SessionPose sp = new SessionPose(
			        			pose,
			        			cursor.getInt(0),
			        			cursor.getInt(1),
			        			cursor.getInt(3),
			        			cursor.getInt(4)
		        			);
		        	sessionPoseList.add(sp);
		        } while (cursor.moveToNext());
		    }
	    	cursor.close();
			return sessionPoseList;
		}
		
		public void sessionPoseDeleteAll(int session_id){
			 SQLiteDatabase db=this.getWritableDatabase();
			 SQLiteStatement stmt = db.compileStatement(
					 "delete from session_pose "
					 + "where session_id = ? ");
			 stmt.bindString(1, Integer.toString(session_id));
			 stmt.executeUpdateDelete();
		}
		
		public void sessionPoseUpdateAll(List<SessionPose> sessionPoseList){
			//delete all previous entries first
			 int session_id = sessionPoseList.get(0).getSessionId();
			 sessionPoseDeleteAll(session_id);
			 SQLiteDatabase db=this.getWritableDatabase();
			 for(SessionPose sp : sessionPoseList){
				 SQLiteStatement stmt = db.compileStatement(
						 "insert into session_pose(session_id, pose_id, position, duration) values "
				 		+ "(?,?,?,?)");
				 stmt.bindString(1, Integer.toString(sp.getSessionId()));
				 stmt.bindString(2, Integer.toString(sp.getPose().getPoseId()));
				 stmt.bindString(3, Integer.toString(sp.getPosition()));
				 stmt.bindString(4, Integer.toString(sp.getDuration()));
				 stmt.executeInsert();
			 }
		}

}
