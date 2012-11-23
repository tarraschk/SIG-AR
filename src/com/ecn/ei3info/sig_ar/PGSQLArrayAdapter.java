package com.ecn.ei3info.sig_ar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PGSQLArrayAdapter extends BaseAdapter{
	
	private Activity parentActivity;
	private int itemLayoutId;
	private ArrayList<ModelInfo> dataSource;
	private LayoutInflater inflater;

	// constructor for adapter
	public PGSQLArrayAdapter(Activity activity, int layoutId, ArrayList<ModelInfo> ds){
		parentActivity = activity;
		itemLayoutId = layoutId;
		dataSource = ds;
		//sortData();
		//Collections.sort(dataSource);
		inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parentView){
		View view = null;
		if(convertView == null){
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(itemLayoutId, parentView, false);
			
			TextView textView = (TextView)view.findViewById(R.id.textViewName);
			String data = dataSource.get(pos).getName();
			textView.setText(data);

			TextView textView2 = (TextView)view.findViewById(R.id.textViewCategory);
			String data2=dataSource.get(pos).getCategory();
			textView2.setText(data2);
			
			TextView textView_latitude = (TextView)view.findViewById(R.id.textViewLatitude);
			double data3=dataSource.get(pos).getLatitude();
			textView_latitude.setText(data3+"");
			
			TextView textView_longitude = (TextView)view.findViewById(R.id.textViewLongitude);
			data3=dataSource.get(pos).getLongitude();
			textView_longitude.setText(data3+"");


			/*final CheckBox cbox= (CheckBox) view.findViewById(R.id.checkBox1);
			cbox.setChecked( ((Scene) dataSource.get(pos)).isActivated());
			cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Scene sc = (Scene) cbox.getTag();
					sc.setActivated(buttonView.isChecked());

				}
			});
			cbox.setTag(dataSource.get(pos));*/
		}else{
			view = convertView;
		}
		return view;
	}
	
	/*public void sortData(String string){
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
