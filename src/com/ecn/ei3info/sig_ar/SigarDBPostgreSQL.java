package com.ecn.ei3info.sig_ar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hitlabnz.androidar.data.Coordinate;
import com.hitlabnz.androidar.data.ModelData;
import com.hitlabnz.androidar.data.SceneLocation;
import com.hitlabnz.androidar.data.representation.model.Transform;
/**
 * SigarDBPostgresSQL class allow to connect the Central SIGAR database (PostgreSQL) and dowload new scenes. 
 * @author bastienmarichalragot
 * @version 1
 */
public class SigarDBPostgreSQL extends Activity {

	protected Activity a;
	//pas uitl c'est champ?

	protected TextView tvStatus;
	protected ListView lvResult;
	protected Button searchButton;
	protected EditText inputQuery;

	protected ProgressBar progress;
	
	protected String status;
	protected String infoConnection;
	protected ArrayList<ModelInfo>result;

	// requete telporaire a modifier en parametre
	private static String sql="Select id_scene, nom_scene, description, nom_category, gps_longitude, gps_latitude, gps_altitude from scene, category where scene.id_category=category.id_category;";

	//TODO completer sql
	// TODO creat SQL query from a text file
	/// piste de code
	/*
	 * try {
BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
String str;
StringBuffer sb = new StringBuffer();
while ((str = in.readLine()) != null) {
sb.append(str + "\n ");
}
in.close();
stmt.executeUpdate(sb.toString());
isScriptExecuted = true;
} catch (Exception e) {
System.err.println("Failed to Execute" + aSQLScriptFilePath +". The error is"+ e.getMessage());
} 
	 */
	
	
	
	
	///
	private static String sqlgetAll="SELECT id_scene, " +
									"name_scene, " +
									"description, " +
									"scene.id_category, "+
									"name_category, " +
									"scene.id_icon, "+
									"file_icon, "+
									"id_author, "+
									"name_person, " +
									"firstname_person, "+
									"activation, "+
									"gps_longitude, " +
									"gps_latitude, " +
									"gps_altitude, " +
									"date_creation, "+
									"id_object3d, "+
									"translation_x, "+
									"translation_y, "+
									"translation_z, "+
									"rotation_x, "+
									"rotation_y, "+
									"rotation_z, "+
									"scale_x, "+
									"scale_y, "+
									"scale_z "+	
						" FROM scene, category, person, icon" +
						" WHERE scene.id_category=category.id_category AND " +
								"scene.id_author=person.id_person AND "+
								"scene.id_icon=icon.id_icon AND " +
								"scene.id_scene=";
	
	private static String sqlgetObject3d="SELECT id_object3d, " +
												"name_object, " +
												"file_obj, " +
												"name_mtl, " +
												"file_mtl, " +
												"date_creation, " +
												"id_author " +
										"FROM object3d " +
										"WHERE id_object3d=";
	
	private static String sqlgetAllTexture="SELECT id_texture, " +
													"name_texture, " +
													"file_texture, " +
													"id_object3d " +
											"FROM texture " +
											"WHERE id_object3d=";
	

	/** Called when the activity is first created. 
	 * This method create the layout and start connection to database if you are connected to network.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set UI layout
		setContentView(R.layout.sigardbpgsql);
		//automatic sleep mode deactivated
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//initialization of attributes
		a=this;

		tvStatus=(TextView) this.findViewById(R.id.tvStatus);
		inputQuery= (EditText) this.findViewById(R.id.inputQuery);
		searchButton= (Button) this.findViewById(R.id.searchButton);
		lvResult=(ListView) this.findViewById(R.id.listViewResult);
		progress= (ProgressBar) this.findViewById(R.id.progressBar);

		inputQuery.setEnabled(false);
		searchButton.setEnabled(false);

		//TODO tester la connectivity avant de lancr les requete


		
		new PGSQLConnection().execute();
		

	}
	/**
	 * Show alert message to manage connectivity. 
	 * Detected if you are connected to network and allow you to activated it if necessary.
	 */
	/*private void buildAlertMessageNoNetwork() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Network seems to be disabled, you must enable it if you wnat download new scenes")
		.setCancelable(false)
		.setPositiveButton("WIFI", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		})
		.setNeutralButton("DataRoaming", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				Toast.makeText(SigarDBPostgreSQL.this, "No data will be downloaded", Toast.LENGTH_SHORT).show();
				dialog.cancel();
				onBackPressed();
				/*Intent intent = new Intent(SigarDBPostgreSQL.this, ListActivity.class);
				intent.putExtra("GPSAlert",false);
				startActivity(intent);
				finish();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();	

	}*/




	/**
	 *	PGSQLConnection allow you to contact database and query new models. 
	 * @author bastienmarichalragot
	 *
	 */
	private class PGSQLConnection extends AsyncTask<String, Void, Void> {

		/**
		 * execute connection 
		 */
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO info de connection db

			try {
				Class.forName("org.postgresql.Driver");

				Connection c =  DriverManager.getConnection("jdbc:postgresql://54.246.97.87:5432/sigar_test","sigar", "rubyECN#2013");

				DatabaseMetaData dbmd;
				dbmd = c.getMetaData(); //get MetaData to confirm connection
				infoConnection= "Connection to"+dbmd.getDatabaseProductName()+" "+dbmd.getDatabaseProductVersion()+"successful.\n";

				if (c != null){
					Log.w("myApp","connection reussie");

					status="connected";

					c.close();

				}else{
					status="not connected";
				}
			} catch (SQLException se) {
				//tv.setText("Couldn't connect: print out a stack trace and exit.");
				se.printStackTrace();
				Log.w("myApp",se);
				// TODO retour d'erreur
				//System.exit(1);
			} catch (ClassNotFoundException cnfe) {
				//tv.setText("Couldn't find driver class:");
				//System.err.println("Couldn't find driver class:");
				cnfe.printStackTrace();
			}

			return null;
		}

		//implementer la fonctionnalité onprogressbar...



		

		/**
		 * Method called after at the end of AsyncTask PGSQLConnection.
		 * To display information of connection 
		 */
		protected void onPostExecute(Void result) {
			//tv.setText(result);
			tvStatus.setText(status);
			//lvResult.setAdapter(new PGSQLArrayAdapter(a,R.layout.list_pgsql,result));
			//TODO add toast with connecion information. 
			//TODO modify enable search button 

			Toast.makeText(SigarDBPostgreSQL.this, infoConnection, Toast.LENGTH_SHORT).show();
			
			
			if(status.equals("connected")){
				searchButton.setEnabled(true);
				inputQuery.setEnabled(true);
				searchButton.setFocusable(true);
				progress.setVisibility(a.getCurrentFocus().GONE);
			}


		}
	}//end of PGSQLConnection

	/**
	 * PGSQLQuery class in order to query the database
	 * @author bastienmarichalragot
	 *
	 */
	private class PGSQLQuery extends AsyncTask<String, Void, ArrayList<ModelInfo>> {

		
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setVisibility(a.getCurrentFocus().VISIBLE);

		}
		
		/**
		 * excute script sql o query database
		 */
		@Override
		protected ArrayList<ModelInfo> doInBackground(String... params) {

			//TODO modify code to connect db
			try {
				Class.forName("org.postgresql.Driver");

				Connection c = DriverManager.getConnection("jdbc:postgresql://54.246.97.87:5432/sigar_test","sigar", "rubyECN#2013");

				DatabaseMetaData dbmd= c.getMetaData(); //get MetaData to confirm connection

				if (c != null){
					Log.w("myApp","connection reussie");
					//	Log.w("myApp",result);
					status="connected";

					Statement st = c.createStatement();
					ResultSet rs =  st.executeQuery(sql);
					result= new ArrayList<ModelInfo>();
					// faire un test sur le nombre de ligne de resultat
					// uniquaement si taille=1
										
					if (rs!=null) {
						while(rs.next()){
							
							int id=rs.getInt(1);
							String name=rs.getString(2);
							String dsc=rs.getString(3);
							String cat=rs.getString(4);
							String aut=rs.getString(5);
							double gpslong= rs.getDouble(6);
							double gpslat= rs.getDouble(7);
							double gpsalt=rs.getDouble(8);
							result.add(new ModelInfo(id,name,dsc,cat,aut,gpslat,gpslong,gpsalt));
							
							
							
							
							
							
							
						}
					}
					rs.close();
					// deconnection database
					c.close();
				}
			} catch (SQLException se) {
				//tv.setText("Couldn't connect: print out a stack trace and exit.");
				se.printStackTrace();
				Log.w("myApp",se);
				// TODO retour d'erreur
				//System.exit(1);
			} catch (ClassNotFoundException cnfe) {
				//tv.setText("Couldn't find driver class:");
				//System.err.println("Couldn't find driver class:");
				cnfe.printStackTrace();
			}
			return result;
		}

		/**
		 * Method called after at the end of AsyncTask PGSQLQuery.
		 * To display the result of query
		 */
		protected void onPostExecute(final ArrayList<ModelInfo> result) {
			//tv.setText(result);
			//tvStatus.setText(status);

			//TODO modify toast with result query 

			Toast.makeText(SigarDBPostgreSQL.this, infoConnection, Toast.LENGTH_SHORT).show();

			lvResult.setAdapter(new PGSQLArrayAdapter(a,R.layout.list_pgsql,result));
			progress.setVisibility(a.getCurrentFocus().GONE);
			// user click on a result to choose if he want import it
			lvResult.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> parent, View view, final int position,long id) {
					// display a dialog to ask user

					if(result.get(position).isAlready()){
						Toast.makeText(a, "This model has been already imported.", Toast.LENGTH_SHORT).show();
						//TODO ici si un model est deaj importer alors aucune possibilite
						// prevoir peut etre la possibilite de le reimporter en ecrasant la version actuelle...
					}else{

						//TODO faire apparaitre les id deja compros et modifier la boite de dialog
						final AlertDialog.Builder builder = new AlertDialog.Builder(a);
						builder.setMessage("Do you want to import this scene on your device?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {

								//TODO add import file

								Toast.makeText(a, "Download the scene: "+result.get(position).getName(), Toast.LENGTH_SHORT).show();

								new PGSQLImport().execute(result.get(position).getId());

							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {
								dialog.cancel();
							}
						});
						final AlertDialog alert = builder.create();
						alert.show();
					}
				}
			});
		}		
	}
	/**
	 * Method call to import a scene on your device
	 * @author bastienmarichalragot
	 *
	 */
	private class PGSQLImport extends AsyncTask<Integer, Void, String> {
		//modifier peut etre le resulta pour afficher les resultat en toast?
		//reactuliser la list a la fin de l'import
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setVisibility(a.getCurrentFocus().VISIBLE);

		}
		
		
		
		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			String name_scene=null;
			
			try {
				Class.forName("org.postgresql.Driver");

				Connection c = DriverManager.getConnection("jdbc:postgresql://54.246.97.87:5432/sigar_test","sigar", "rubyECN#2013");

				DatabaseMetaData dbmd= c.getMetaData(); //get MetaData to confirm connection

				if (c != null){
					Log.w("myApp","connection reussie");
					//	Log.w("myApp",result);
					status="connected";
					
					
					Statement st = c.createStatement();
					ResultSet rs =  st.executeQuery(sqlgetAll+ Integer.toString(params[0])+" ;");
				
					//result= new ArrayList<ModelInfo>();
					
					// farie un test pour qu'un seul resultat sorte!
					if (rs!=null) {
						rs.next();

						int id_scene=rs.getInt(1);
						name_scene=rs.getString(2);
						String description=rs.getString(3);

						int id_category=rs.getInt(4);
						String name_category=rs.getString(5);

						int id_icon=rs.getInt(6);
						InputStream file_icon=rs.getBinaryStream(7);

						int id_author=rs.getInt(8);
						String name_person=rs.getString(9);
						String firstname_person=rs.getString(10);

						boolean activation=rs.getBoolean(11);

						double gpslong= rs.getDouble(12);
						double gpslat= rs.getDouble(13);
						double gpsalt=rs.getDouble(14);

						Timestamp date_creation = rs.getTimestamp(15);

						int id_object3d = rs.getInt(16);

						float translation_x=rs.getFloat(17);
						float translation_y=rs.getFloat(18);
						float translation_z=rs.getFloat(19);

						float rotation_x=rs.getFloat(20);
						float rotation_y=rs.getFloat(21);
						float rotation_z=rs.getFloat(22);

						float scale_x=rs.getFloat(23);
						float scale_y=rs.getFloat(24);
						float scale_z=rs.getFloat(25);
//						telchargement des donner
						//creation des dossiers
						File exst = Environment.getExternalStorageDirectory();
				    	String exstPath = exst.getPath();
				    	

				    	File inst = getFilesDir();
				    	String instPath = inst.getPath();
						
				    	File inicon = new File(exstPath+"/SIGAR/icons");
						
			
				    	File infileicon = new File(inicon, Integer.toString(id_icon)); //Getting a file within the dir.
				    	FileOutputStream inouticon = new FileOutputStream(infileicon); //Use the stream as usual to write into the file.
				  
				    	int inread1 = 0;
				    	byte[] inbytes1 = new byte[1024];
				     
				    	while ((inread1 = file_icon.read(inbytes1)) != -1) {
				    		inouticon.write(inbytes1, 0, inread1);
				    	}
				     
				    	file_icon.close();
				    	inouticon.flush();
				    	inouticon.close();
				    	
				    	
						
						Statement st1 = c.createStatement();
						ResultSet rs1 =  st1.executeQuery(sqlgetObject3d+ id_object3d+" ;");
						if (rs1!=null) {
							rs1.next();
							String name_object=rs1.getString(2);
							InputStream file_obj=rs1.getBinaryStream(3);
							String name_mtl=rs1.getString(4);
							
							InputStream file_mtl= rs1.getBinaryStream(5);
							Timestamp date_creation_object3d=rs1.getTimestamp(6);
							int id_author_object3d=rs1.getInt(7);

							//TODO insert object3D to sqlite

							//Add file to sdcard directory
					    	
					    	//create directory for model
					    	File objectfolder = new File(exstPath+"/SIGAR/3Dmodels/"+Integer.toString(id_object3d));
					    	objectfolder.mkdir();
							
					    	//TODO create a new methode and change the follow code
					    	//add file obj, mtl to folder
					    	File fileobj = new File(objectfolder, "model.obj"); //Getting a file within the dir.
					    	FileOutputStream outobj = new FileOutputStream(fileobj); //Use the stream as usual to write into the file.
					  
					    	int read = 0;
					    	byte[] bytes = new byte[1024];
					     
					    	while ((read = file_obj.read(bytes)) != -1) {
					    		outobj.write(bytes, 0, read);
					    	}
					     
					    	//file_obj.close();
					    	outobj.flush();
					    	outobj.close();
					    	
					    	
					    	//create directory for model
					    	File inobjectfolder = new File(instPath+"/SIGAR/3Dmodels/"+Integer.toString(id_object3d));
					    	inobjectfolder.mkdir();
							
					    	//TODO create a new methode and change the follow code
					    	//add file obj, mtl to folder
					    	File infileobj = new File(inobjectfolder, "model.obj"); //Getting a file within the dir.
					    	FileOutputStream inoutobj = new FileOutputStream(infileobj); //Use the stream as usual to write into the file.
					  
					    	int inread = 0;
					    	byte[] inbytes = new byte[1024];
					     
					    	while ((inread = file_obj.read(inbytes)) != -1) {
					    		inoutobj.write(inbytes, 0, inread);
					    	}
					     
					    	file_obj.close();
					    	inoutobj.flush();
					    	inoutobj.close();
					    	
					    	
					    	
					    	
					    	File filemtl = new File(objectfolder, "model.mtl"); //Getting a file within the dir.
					    	FileOutputStream outmtl = new FileOutputStream(filemtl); //Use the stream as usual to write into the file.
					    
					    	int readmtl = 0;
					    	byte[] bytesmtl = new byte[1024];
					    	Log.w("myapp",Integer.toString(file_mtl.read()));
					    	while ((readmtl = file_mtl.read(bytesmtl)) != -1) {
					    		outmtl.write(bytesmtl, 0, readmtl);
					    	}
					     
					    	file_mtl.close();
					    	outmtl.flush();
					    	outmtl.close();
					 	
					    	
							//Query to obtain all associated textures
							Statement st2 = c.createStatement();
							ResultSet rs2 =  st2.executeQuery(sqlgetAllTexture+ Integer.toString(id_object3d)+" ;");
							if (rs2!=null) {
								while(rs2.next()){
									
									int id_texture=rs2.getInt(1);
							    	Log.w("myapp",Integer.toString(id_texture));
							    	
									String name_texture=rs2.getString(2);
									InputStream file_texture=rs2.getBinaryStream(3);
									//TODO insert texture to table in sqlite
									
									File filetexture = new File(objectfolder, name_texture); //Getting a file within the dir.
							    	FileOutputStream outtexture = new FileOutputStream(filetexture); //Use the stream as usual to write into the file.
							    	
							    	int readtexture = 0;
							    	byte[] bytestexture = new byte[1024];
							    	//Log.w("myapp",Integer.toString(file_mtl.read()));
							    	while ((readtexture = file_texture.read(bytestexture)) != -1) {
							    		outtexture.write(bytestexture, 0, readtexture);
							    	}
							    	
							    	file_texture.close();
							    	outtexture.flush();
							    	outtexture.close();
									
									
								}
							}else{
								// alors la on est vraiment dans la merde!!!
							}
						}else{
							// c'est encore plus la merde
						}
						SceneLocation Location= new SceneLocation();
						Location.setAltitude(gpsalt);
						Location.setLongitude(gpslong);
						Location.setLatitude(gpslat);
						
						List<ModelData> models=new ArrayList<ModelData>();
						ModelData model= new ModelData();
						
						model.name=Integer.toString(id_object3d);
						model.id=Integer.toString(id_object3d);
						
						Transform transform=new Transform();

						transform.setTranslation(new Coordinate(translation_x,translation_y,translation_z));
						transform.setScale(new Coordinate(scale_x,scale_y,scale_z));
						transform.setRotation(new Coordinate(rotation_x,rotation_y,rotation_z));
						
						
						model.addTransform(transform);
						models.add(model);
						
						DataManager.getInstance(false).addScene(new Scene( id_scene, name_scene, description, name_category, name_person, activation, "inutil", Location, models));

						// insert into SQLite DB

						/*SigarDB database= new SigarDB(a);
						SQLiteDatabase sqlDB = database.getWritableDatabase();


						String sqlInsertScene="INSERT INTO scene (id_scene,name_scene,description,id_category,id_icon,activation,gps_longitude,gps_latitude,gps_altitude,id_author,date_creation,id_object3d," +
								"translation_x,translation_y,translation_z,rotation_x,rotation_y,rotation_z,scale_x,scale_y,scale_z ) " +
								"VALUES ("+Integer.toString(id_scene)+
								", '"+name_scene+"', '"+description+
								"', "+Integer.toString(id_category)+", "+Integer.toString(id_icon)+", "+Integer.toString((activation)?1:0)+
								", "+Double.toString(gpslong)+", "+Double.toString(gpslat)+", "+Double.toString(gpsalt)+
								", "+Integer.toString(id_author)+", '"+date_creation.toString()+"', "+Integer.toString(id_object3d)+
								", "+Float.toString(translation_x)+", "+Float.toString(translation_y)+", "+Float.toString(translation_z)+
								", "+Float.toString(rotation_x)+", "+Float.toString(rotation_y)+", "+Float.toString(rotation_z)+
						    							", "+Float.toString(scale_x)+", "+Float.toString(scale_y)+", "+Float.toString(scale_z)+"); ";
						    
						sqlDB.execSQL(sqlInsertScene);
						*/


						    
					}else{
						
						// c'est la grosse merde!!
					}
					
					//TODO ajouter prgress bar durant l'iport
					//plus un toast de resultat
					rs.close();
					// deconnection database
					c.close();
				}
			} catch (SQLException se) {
				//tv.setText("Couldn't connect: print out a stack trace and exit.");
				se.printStackTrace();
				Log.w("myApp",se);
				// TODO retour d'erreur
				//System.exit(1);
			} catch (ClassNotFoundException cnfe) {
				//tv.setText("Couldn't find driver class:");
				//System.err.println("Couldn't find driver class:");
				cnfe.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return name_scene;
		}
		
		
		/**
		 * Method called after at the end of AsyncTask PGSQLImport.
		 * To display the result of query
		 */
		protected void onPostExecute(String sceneName) {
			//tv.setText(result);
			//tvStatus.setText(status);

// TODO test null de string

			Toast.makeText(a, "The scene: "+sceneName+", has been correctly imported.", Toast.LENGTH_SHORT).show();
			lvResult.setAdapter(lvResult.getAdapter());// useless
			//lvResult.setAdapter(new PGSQLArrayAdapter(a,R.layout.list_pgsql,result));
			progress.setVisibility(a.getCurrentFocus().GONE);
			// user click on a result to choose if he want import it
			/*lvResult.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> parent, View view, final int position,long id) {
					// display a dialog to ask user

					if(result.get(position).isAlready()){
						Toast.makeText(a, "This model has been already imported.", Toast.LENGTH_SHORT).show();
						//TODO ici si un model est deaj importer alors aucune possibilite
						// prevoir peut etre la possibilite de le reimporter en ecrasant la version actuelle...
					}else{

						//TODO faire apparaitre les id deja compros et modifier la boite de dialog
						final AlertDialog.Builder builder = new AlertDialog.Builder(a);
						builder.setMessage("Do you want to import this scene on your device?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {

								//TODO add import file

								Toast.makeText(a, "Download the scene: "+result.get(position).getName(), Toast.LENGTH_SHORT).show();

								new PGSQLImport().execute(result.get(position).getId());

							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog, final int id) {
								dialog.cancel();
							}
						});
						final AlertDialog alert = builder.create();
						alert.show();
					}
				}
			});*/
		
		}
	}

	
	
	
	
	
	public void onGoSearch(View view){
		
		sql="SELECT id_scene, name_scene, description, name_category,name_person, gps_longitude, gps_latitude, gps_altitude" +
				" FROM scene, category, person" +
				" WHERE scene.id_category=category.id_category AND" +
				" scene.id_author=person.id_person AND"+
				" (scene.name_scene LIKE '%"+inputQuery.getText().toString()+"%' OR" +
				" scene.description LIKE '%"+inputQuery.getText().toString()+"%' OR" +
				" category.name_category LIKE '%"+inputQuery.getText().toString()+"%' OR" +
				" person.name_person LIKE '%"+inputQuery.getText().toString()+"%');";

		new PGSQLQuery().execute();

	}

	/**
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent intent = new Intent(SigarDBPostgreSQL.this, ListActivity.class);
		startActivity(intent);
		finish();
	}

	
	public void onGoBack(View view) {
		//super.onBackPressed();
		Intent intent = new Intent(SigarDBPostgreSQL.this, ListActivity.class);
		startActivity(intent);
		finish();
	}
}





