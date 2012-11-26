package com.ecn.ei3info.sig_ar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SigarDB extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "sigar.db";
	private static final int DATABASE_VERSION = 10;

	
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE category(" 
														+"id_category integer primary key NOT NULL , " 
														+"name_category text not null" 
												+"); "
												+"CREATE TABLE icon (" 
														+"id_icon integer primary key  NOT NULL, " 
														+"file_icon blob" 
												+"); "
												+"CREATE TABLE person ("
														+"id_person integer primary key NOT NULL, "
														+"name_person text, "
														+"firstname_person text "
												+");"
												+"CREATE TABLE  object3d (" 
														+"id_object3d integer primary key NOT NULL, " 
														+"name_object text NOT NULL," 
														+"file_obj blob, " 
														+"name_mtl text NOT NULL, " 
														+"file_mtl blob, " 
														+"date_creation timestamp without time zone, "
														+"id_author integer NOT NULL, "
														+"FOREIGN KEY(id_author) REFERENCES personne(id_person)" 
												+");"
												+"CREATE TABLE texture ("
														+"id_texture integer primary key NOT NULL, "
													    +"name_texture text, "
													    +"file_texture blob, "
													    +"id_object3d integer NOT NULL, "
														+"FOREIGN KEY(id_object3D) REFERENCES object3D(id_object3D)" 
												+");"
												+"CREATE TABLE scene ( "
													    +"id_scene integer primary key NOT NULL, "
													    +"name_scene text, "
													    +"description text, "
													    +"id_category integer NOT NULL, "
													    +"id_icon integer NOT NULL, "
													    +"activation boolean DEFAULT false, "
													    +"gps_longitude double precision, " 
													    +"gps_latitude double precision, "
													    +"gps_altitude double precision, "
													    +"id_author integer NOT NULL, "
													    +"date_creation timestamp without time zone, "
													    +"id_object3d integer NOT NULL, "
													    +"translation_x numeric, "
													    +"translation_y numeric, "
													    +"translation_z numeric, "
													    +"rotation_x numeric, "
													    +"rotation_y numeric, "
													    +"rotation_z numeric, "
													    +"echelle_x numeric, "
													    +"echelle_y numeric, "
													    +"echelle_z numeric, "
													    +"FOREIGN KEY(id_category) REFERENCES category(id_category), "
													    +"FOREIGN KEY(id_icon) REFERENCES icon(id_icon), "
													    +"FOREIGN KEY(id_author) REFERENCES person(id_person), "
													    +"FOREIGN KEY(id_object3D) REFERENCES object3D(id_object3D)" 
												+");";
						// check numeric format to sqlite??
	
	private static final String DATABASE_INSERT=" INSERT INTO category(name_category) VALUES ('test');" +
													"INSERT INTO person(name_person, firstname_person) VALUES ('MARICHAL', 'BASTIEN');" +
													"INSERT INTO icon(id_icon) VALUES (1);" +
													"INSERT INTO object3d VALUES (1,'monpremierobjet3D','','fichiermtl','','',1);" +
													"INSERT INTO texture(name_texture,id_object3d) VALUES ('texturetest',1);" +
													"INSERT INTO scene VALUES (1,'scene1','truc de test',1,1,false,,'','','',1,'',1,'','','','','','','','','');";
													
			
												
			
			
	
		    
	
		    
	
		    


// supprimer les insert pour la mise en prod

	/**
	 * Constructor
	 * @param context
	 */
	public SigarDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.w("myApp","creation DB OBJET");
		
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_INSERT);
		Log.w("myApp",DATABASE_CREATE);
		Log.w("myApp","Creation DB");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SigarDB.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS category");
		onCreate(db);
		Log.w("myApp","Update");

	}
}
