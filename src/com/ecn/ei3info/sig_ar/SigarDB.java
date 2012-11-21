package com.ecn.ei3info.sig_ar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SigarDB extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "sigar.db";
	private static final int DATABASE_VERSION = 5;

	
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table category(" +
			"id_category integer primary key , " + 
			"nom_category text not null);"+
			
			"CREATE TABLE icon (" +
			"id_icone integer primary key  NOT NULL," +
			"file_icone blob);"+
			
			"create table  objet3d (" +
			"id_objet3d integer primary key NOT NULL," +
			"nom_objet text not null," +
			" file_obj blob, " +
			"nom_mtl text not null," +
			" file_mtl blob," +
			" date_creation timestamp without time zone," +
			"id_auteur integer NOT NULL" +
			"FOREIGN KEY(id_auteur) REFERENCES personne(id_personne)"+
			");"+
	
	"CREATE TABLE personne ("+
		    "id_personne integer primary key NOT NULL,"+
		    "nom_personne character varying(45),"+
		    "prenom_personne character varying(45)"+
		    ");"+
		    
	"CREATE TABLE scene ("+
		    "id_scene integer primary key NOT NULL,"+
		    "nom_scene character varying(45),"+
		    "description character varying(255),"+
		    "id_category integer NOT NULL,"+
		    "id_icone integer NOT NULL,"+
		    "activation boolean DEFAULT false,"+
		    "gps_longitude double precision,"+
		    "gps_latitude double precision,"+
		    "gps_altitude double precision,"+
		    "id_auteur integer NOT NULL,"+
		    "date_creation timestamp without time zone,"+
		    "id_objet3d integer NOT NULL,"+
		    "translation_x numeric,"+
		    "translation_y numeric,"+
		    "translation_z numeric,"+
		    "rotation_x numeric,"+
		    "rotation_y numeric,"+
		    "rotation_z numeric,"+
		    "echelle_x numeric,"+
		    "echelle_y numeric,"+
		    "echelle_z numeric"+
		");"+
		    
	"CREATE TABLE texture ("+
			"id_texture integer primary keyNOT NULL,"+
		    "nom_texture character varying(45),"+
		    "file_texture bytea,"+
		    "id_objet3d integer NOT NULL"+
		");"+
		    
"INSERT INTO category Values (1,'test');";

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
