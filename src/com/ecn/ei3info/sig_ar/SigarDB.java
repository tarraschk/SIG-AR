package com.ecn.ei3info.sig_ar;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SigarDB extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "sigar.db";
	private static final int DATABASE_VERSION = 53;

	private static final String DATABASE_INSERT=" INSERT INTO category(name_category) VALUES ('test');" ;
													
// supprimer les insert pour la mise en prod

	/**
	 * Constructor
	 * @param context
	 */
	public SigarDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.w("myApp","creation DB OBJET");
		
	}
	/**
	 * Method called to create database with different tables
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		try{
		database.execSQL("CREATE TABLE category(" 
							+"id_category integer primary key NOT NULL, " 
							+"name_category TEXT NOT NULL" 
							+"); ");
		database.execSQL("CREATE TABLE icon ("
							+"id_icon integer primary key  NOT NULL, " 
							+"file_icon blob" 
							+"); ");
		database.execSQL("CREATE TABLE person ( " 
							+"id_person integer " 
							+"primary key NOT NULL, " 
							+"name_person text, " 
							+"firstname_person text)" 
							+";");
		database.execSQL("CREATE TABLE object3d ( " 
							+"id_object3d integer primary key NOT NULL, " 
							+"name_object text NOT NULL, " 
							+"file_obj blob, " 
							+"name_mtl text NOT NULL, " 
							+"file_mtl blob, " 
							+"date_creation timestamp without time zone, " 
							+"id_author integer NOT NULL, " 
							+"FOREIGN KEY(id_author) REFERENCES personne(id_person) " 
							+");"); 
		database.execSQL("CREATE TABLE texture ("
							+"id_texture integer primary key NOT NULL, "
							+"name_texture text, "
							+"file_texture blob, "
							+"id_object3d integer NOT NULL, "
							+"FOREIGN KEY(id_object3D) REFERENCES object3D(id_object3D)" 
							+"); ");
		database.execSQL("CREATE TABLE scene ( "
							+"id_scene integer primary key, "
							+"name_scene text, "
							+"description text, "
							+"id_category integer NOT NULL, "
							+"id_icon integer NOT NULL, "
							+"activation integer,  "
							+"gps_longitude real, " 
							+"gps_latitude real, "
							+"gps_altitude real, "
							+"id_author integer NOT NULL, "
							+"date_creation text, "
							+"id_object3d integer NOT NULL, "
							+"translation_x real, "
							+"translation_y real, "
							+"translation_z real, "
							+"rotation_x real, "
							+"rotation_y real, "
							+"rotation_z real, "
							+"echelle_x real, "
							+"echelle_y real, "
							+"echelle_z real, "
							+"FOREIGN KEY (id_category) REFERENCES category(id_category), "
							+"FOREIGN KEY (id_icon) REFERENCES icon(id_icon), "
							+"FOREIGN KEY (id_author) REFERENCES person(id_person), "
							+"FOREIGN KEY (id_object3D) REFERENCES object3D(id_object3D)" 
							+");");
		
		
		database.execSQL("PRAGMA foreign_keys = ON;");

		database.execSQL(DATABASE_INSERT);
		database.execSQL("INSERT INTO icon(id_icon) VALUES (1);");
//		database.execSQL("INSERT INTO scene VALUES (1,'scene1','truc de test',1,1,false,'0','0','0',1,'0',1,'0','0','0','0','0','0','0','0','0');");


		Log.w("myApp","Creation DB");
		}catch(SQLException e){
			e.printStackTrace();
			Log.w("dtdhd","dafuk");
			
		}
		
	}
	/**
	 * Methode called to updated database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SigarDB.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS category");
		db.execSQL("DROP TABLE IF EXISTS icon");
		db.execSQL("DROP TABLE IF EXISTS person");
		db.execSQL("DROP TABLE IF EXISTS object3d");
		db.execSQL("DROP TABLE IF EXISTS texture");
		db.execSQL("DROP TABLE IF EXISTS scene");
		db.execSQL("DROP TABLE IF EXISTS android_metadata");
		
		onCreate(db);
	}
}
