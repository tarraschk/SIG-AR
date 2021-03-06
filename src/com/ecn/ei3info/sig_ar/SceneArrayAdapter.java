package com.ecn.ei3info.sig_ar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hitlabnz.androidar.data.Coordinate;
import com.hitlabnz.androidar.data.representation.model.Transform;
import com.hitlabnz.androidar.data.representation.vector.Vector3f;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SceneArrayAdapter extends BaseAdapter{
/*	private final Context context;
	private final DataManager listscene;
 
	public SceneArrayAdapter(Context context, DataManager dataManager) {
		super();
		this.context = context;
		this.listscene = dataManager;
	}*/
	
	private Activity parentActivity;
	private int itemLayoutId;
	private List<Scene> dataSource;
	private LayoutInflater inflater;

	// constructor for adapter
	public SceneArrayAdapter(Activity activity, int layoutId, List<Scene> ds){
		parentActivity = activity;
		itemLayoutId = layoutId;
		dataSource = ds;
		//sortData();
		//Collections.sort(dataSource);
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parentView){
		View view = null;
		if(convertView == null){
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			
			//TODO en fcontion de la taille de l'ecran ne pas afficher toutes les infos
			view = inflater.inflate(itemLayoutId, parentView, false);
			
			TextView textView = (TextView)view.findViewById(R.id.scene_name);
			String data = dataSource.get(pos).getName();
			textView.setText(data);
			
			// To display model details on long click
			/*textView.setOnLongClickListener(new OnLongClickListener() {
			    @Override
			    public boolean onLongClick(View v) {
			    	
			    	final Dialog dialog = new Dialog(v.getContext());
					dialog.setContentView(R.layout.modeldescription);
					dialog.setTitle("Details");
					
					TextView text = (TextView) dialog.findViewById(R.id.modelname);
					text.setText(dataSource.get(pos).getName());
					
					TextView textid = (TextView) dialog.findViewById(R.id.modelid);
					textid.setText(Integer.toString(dataSource.get(pos).getId()));
					
					TextView description = (TextView) dialog.findViewById(R.id.description);
					description.setText(dataSource.get(pos).getDescription());
					
					TextView category = (TextView) dialog.findViewById(R.id.category);
					category.setText(dataSource.get(pos).getCategory());
					
					TextView latitude = (TextView) dialog.findViewById(R.id.latitude);
					double lat=dataSource.get(pos).getLatitude();
					latitude.setText(lat+"");
					
					TextView longitude = (TextView) dialog.findViewById(R.id.longitude);
					double longi=dataSource.get(pos).getLongitude();
					longitude.setText(longi+"");

					TextView altitude = (TextView) dialog.findViewById(R.id.altitude);
					double alt=dataSource.get(pos).getAltitude();
					altitude.setText(alt+"");
					
					ImageButton okButton = (ImageButton) dialog.findViewById(R.id.ok);
					okButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					dialog.show();
					
			       return false;
			    }
			});*/

			ImageView imageView = (ImageView) view.findViewById(R.id.icons);
			Drawable icon= dataSource.get(pos).getIcon();
			if(icon==null){
				imageView.setImageResource(R.drawable.action_help);
			}else{
				imageView.setImageDrawable(icon);
			}
						
			TextView textView2 = (TextView)view.findViewById(R.id.scene_category);
			String data2=dataSource.get(pos).getCategory();
			textView2.setText(data2);
			
			TextView textView_latitude = (TextView)view.findViewById(R.id.scene_latitude);
			double data3=dataSource.get(pos).getLatitude();
			textView_latitude.setText(data3+"");
			
			TextView textView_longitude = (TextView)view.findViewById(R.id.scene_longitude);
			data3=dataSource.get(pos).getLongitude();
			textView_longitude.setText(data3+"");


			TextView textView_altitude = (TextView)view.findViewById(R.id.scene_altitude);
			data3=dataSource.get(pos).getAltitude();
			textView_altitude.setText(data3+"");

			final CheckBox cbox= (CheckBox) view.findViewById(R.id.checkBox1);
			cbox.setChecked( ((Scene) dataSource.get(pos)).isActivated());
			cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Scene sc = (Scene) cbox.getTag();
					sc.setActivated(buttonView.isChecked());

				}
			});
			cbox.setTag(dataSource.get(pos));
			
			ImageButton editButton= (ImageButton) view.findViewById(R.id.button_modify);
			editButton.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0){
					
					final Dialog dialog = new Dialog(arg0.getContext());
					dialog.setContentView(R.layout.custom_modifymodel);
					dialog.setTitle("Modify your model");

					// set the custom dialog components - text, image and button
					
					TextView text = (TextView) dialog.findViewById(R.id.modelname);
					text.setText(dataSource.get(pos).getName());
					
					TextView textid = (TextView) dialog.findViewById(R.id.modelid);
					textid.setText(Integer.toString(dataSource.get(pos).getId()));
					
					TextView description = (TextView) dialog.findViewById(R.id.description);
					description.setText(dataSource.get(pos).getDescription());
								
					final EditText latitude= (EditText) dialog.findViewById(R.id.latitude); 
					latitude.setText( String.valueOf(DataManager.singletonInstance.getSceneList().get(0).getLatitude()));
					
					final EditText longitude= (EditText) dialog.findViewById(R.id.longitude); 
					longitude.setText( String.valueOf(DataManager.singletonInstance.getSceneList().get(0).getLongitude()));
					
					final EditText altitude= (EditText) dialog.findViewById(R.id.altitude); 
					altitude.setText( String.valueOf(DataManager.singletonInstance.getSceneList().get(0).location.getAltitude()));
					
					final EditText scalex= (EditText) dialog.findViewById(R.id.scalex);
					//scalex.setText(String.valueOf(dataSource.get(pos).getTransforms().get(0).getScale().getX()));
					
					final EditText scaley= (EditText) dialog.findViewById(R.id.scaley);
					//scaley.setText(String.valueOf(dataSource.get(pos).getTransforms().get(0).getScale().getY()));
					
					final EditText scalez= (EditText) dialog.findViewById(R.id.scalez);
					//scalez.setText(String.valueOf(dataSource.get(pos).getTransforms().get(0).getScale().getZ()));
					
					final EditText rotx= (EditText) dialog.findViewById(R.id.rotx);
					//scalex.setText(String.valueOf(dataSource.get(pos).getTransforms().get(0).getRotation().getX()));
					
					final EditText roty= (EditText) dialog.findViewById(R.id.roty);
					//scalex.setText(String.valueOf(dataSource.get(pos).getTransforms().get(0).getRotation().getY()));
					
					final EditText rotz= (EditText) dialog.findViewById(R.id.rotz);
					//scalex.setText(String.valueOf(dataSource.get(pos).getTransforms().get(0).getRotation().getZ()));
					
					
					ImageButton okButton = (ImageButton) dialog.findViewById(R.id.ok);
					// if button is clicked, close the custom dialog
					okButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							//TODO completer les donn�e modif�es
							DataManager.singletonInstance.getSceneList2().get(pos).setLatitude(Double.parseDouble(latitude.getText().toString()));
							DataManager.singletonInstance.getSceneList2().get(pos).setLongitude(Double.parseDouble(longitude.getText().toString()));
							DataManager.singletonInstance.getSceneList2().get(pos).setAltitude(Double.parseDouble(altitude.getText().toString()));
							
							float x = Float.valueOf(scalex.getText().toString().trim()).floatValue();
							float y = Float.valueOf(scaley.getText().toString().trim()).floatValue();
							float z = Float.valueOf(scalez.getText().toString().trim()).floatValue();;
							
							Transform modelTransform = new Transform();
							modelTransform.setScale(new Coordinate(x,y,z));
							
							float rx = Float.valueOf(rotx.getText().toString().trim()).floatValue();
							float ry = Float.valueOf(roty.getText().toString().trim()).floatValue();
							float rz = Float.valueOf(rotz.getText().toString().trim()).floatValue();;
							
							modelTransform.setRotation(new Coordinate(rx,ry,rz));
							
							//dataSource.get(pos).models.get(0).addTransform(modelTransform);
							
							dataSource.get(pos).models.get(0).getTransforms().clear();
							dataSource.get(pos).addTransform(modelTransform);
							//dataSource.get(pos).models.get(0).getTransforms().add(modelTransform);

							//dataSource.get(pos).models.get(0).addTransform(modelTransform);
							
							//Vector3f scale = new Vector3f(x,y,z);
							//Vector3f rotation = new Vector3f(rx,ry,rz);
							//dataSource.get(pos).models.get(0).getTransforms().get(0).setScale(scale);

							
							//gere les exceptions
							dialog.dismiss();
						}
					});

					ImageButton cancelButton = (ImageButton) dialog.findViewById(R.id.cancel);
					// if button is clicked, close the custom dialog
					cancelButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.show();	
				}
			} );
			
			
			ImageButton infoButton= (ImageButton) view.findViewById(R.id.button_info);
			infoButton.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0){
					final Dialog dialog = new Dialog(arg0.getContext());
					dialog.setContentView(R.layout.modeldescription);
					dialog.setTitle("Details");
					
					TextView text = (TextView) dialog.findViewById(R.id.modelname);
					text.setText(dataSource.get(pos).getName());
					
					TextView textid = (TextView) dialog.findViewById(R.id.modelid);
					textid.setText(Integer.toString(dataSource.get(pos).getId()));
					
					TextView description = (TextView) dialog.findViewById(R.id.description);
					description.setText(dataSource.get(pos).getDescription());
					
					TextView category = (TextView) dialog.findViewById(R.id.category);
					category.setText(dataSource.get(pos).getCategory());
					
					TextView latitude = (TextView) dialog.findViewById(R.id.latitude);
					double lat=dataSource.get(pos).getLatitude();
					latitude.setText(lat+"");
					
					TextView longitude = (TextView) dialog.findViewById(R.id.longitude);
					double longi=dataSource.get(pos).getLongitude();
					longitude.setText(longi+"");

					TextView altitude = (TextView) dialog.findViewById(R.id.altitude);
					double alt=dataSource.get(pos).getAltitude();
					altitude.setText(alt+"");
					
					ImageButton okButton = (ImageButton) dialog.findViewById(R.id.ok);
					okButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					dialog.show();
				}
			});
			
		}else{
			view = convertView;
		}
		return view;
	}
	
	public void sortData(String string){
		if(string.equals("Name")){
			Collections.sort(dataSource, new Comparator<Scene>(){
				public int compare( Scene s1, Scene s2 ) {
					return s1.getName().compareTo( s2.getName() );
				}
			});						
		}else if(string.equals("Category")){
			Collections.sort(dataSource, new Comparator<Scene>(){
				public int compare( Scene s1, Scene s2 ) {
					return s1.getCategory().compareTo( s2.getCategory() );
				}
			});	
		}	
	}
	
	
	/*

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_scene, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icons);
		textView.setText(listscene.getSceneList().get(position).getName());
 
		// Change icon based on name
		/*String s = values[position];
 
		System.out.println(s);
 */
	/*	if (s.equals("WindowsMobile")) {
			imageView.setImageResource(R.drawable.windowsmobile_logo);
		} else if (s.equals("iOS")) {
			imageView.setImageResource(R.drawable.ios_logo);
		} else if (s.equals("Blackberry")) {
			imageView.setImageResource(R.drawable.blackberry_logo);
		} else {
			imageView.setImageResource(R.drawable.android_logo);
		}
 */
		/*return rowView;
	}*/

	/*public DataManager getListscene() {
		return listscene;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}*/
	@Override
	public int getCount() {
		return (dataSource != null) ? dataSource.size() : 0;
	}

	@Override
	public Object getItem(int idx) {
		return (dataSource != null) ? dataSource.get(idx) : null;
	}

	@Override
	public long getItemId(int position) {
		return  position;
	}

	@Override
	public boolean hasStableIds(){
		return true;
	}

	@Override
	public int getItemViewType(int pos){
		return IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount(){
		return 1;
	}

	
	
	
}
