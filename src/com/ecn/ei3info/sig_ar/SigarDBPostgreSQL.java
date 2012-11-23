package com.ecn.ei3info.sig_ar;



import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * SigarDBPostgresSQL class allow to connect the Central SIGAR database (PostgreSQL) and dowload new scenes. 
 * @author bastienmarichalragot
 * @version 1
 */
public class SigarDBPostgreSQL extends Activity {

	protected Activity a;

	protected TextView tvStatus;
	protected TextView tv;
	protected TextView tv2;
	protected ListView lvResult;

	/** Called when the activity is first created. 
	 * This method create the layout and start connection to database if you are connected to network.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sigardbpgsql);

		a=this;

		tvStatus=(TextView) this.findViewById(R.id.tvStatus);
		lvResult=(ListView) this.findViewById(R.id.listViewResult);
		//a modifier les fieds ci dessous
		tv = (TextView) this.findViewById(R.id.tv);

		tv2 = (TextView) this.findViewById(R.id.tv2);

		//TODO tester la connectivity avant de lancr les requete
		// ajouter requete query au lancement 
		
		
		
		if(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()==null){
			buildAlertMessageNoNetwork();
		}
			new PGSQLConnection().execute();
		

	}
	/**
	 * Show alert message to manage connectivity. 
	 * Detected if you are connected to network and allow you to activated it if necessary.
	 */
	private void buildAlertMessageNoNetwork() {
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
				finish();*/
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();	

	}
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		while( ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()==null){
			buildAlertMessageNoNetwork();
		}
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 *	PGSQLConnection allow you to contact database and query new models. 
	 * @author bastienmarichalragot
	 *
	 */
	private class PGSQLConnection extends AsyncTask<String, Void, ArrayList<ModelInfo>> {

		protected String status;
		protected ArrayList<ModelInfo>result;

		// requete telporaire a modifier en parametre
		private static final String sql="Select id_scene, nom_scene, description, nom_category, gps_longitude, gps_latitude, gps_altitude from scene, category where scene.id_category=category.id_category;";

		@Override
		protected ArrayList<ModelInfo> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {
				Class.forName("org.postgresql.Driver");

				//tv.setText("Registered the driver ok, so let's make a connection.");

				Connection c = null;

				c = DriverManager.getConnection("jdbc:postgresql://54.246.97.87:5432/sigar_test","sigar", "rubyECN#2013");

				//tv2.setText("jdbc:postgresql://192.168.0.2/postgres"+"postgres"+"alessio");
				DatabaseMetaData dbmd;
				dbmd = c.getMetaData(); //get MetaData to confirm connection
				//	result= "Connection to"+dbmd.getDatabaseProductName()+" "+dbmd.getDatabaseProductVersion()+"successful.\n";

				if (c != null){
					Log.w("myApp","connection reussie");
					//	Log.w("myApp",result);
					status="connected";

					// modifier test du result non null pus iteration....
					Statement st = c.createStatement();
					ResultSet rs =  st.executeQuery(sql);
					result= new ArrayList<ModelInfo>();
					if (rs!=null) {
						while(rs.next()){
							int id=rs.getInt(1);
							String name=rs.getString(2);
							String dsc=rs.getString(3);
							String cat=rs.getString(4);
							double gps= rs.getDouble(5);
							//rs.getDouble(7);
							//rs.getDouble(7)));
							result.add(new ModelInfo(id,name,dsc,cat,gps,gps,gps));
						}
					}
					rs.close();
					// deconnection database
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

			return result;
		}
		//implementer la fonctionnalité onprogressbar...

		protected void onPostExecute(ArrayList<ModelInfo> result) {
			//tv.setText(result);
			tvStatus.setText(status);
			lvResult.setAdapter(new PGSQLArrayAdapter(a,R.layout.list_pgsql,result));


		}




	}

	public void onGoBack(View view) {
		super.onBackPressed();
	}
}





