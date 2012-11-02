package com.ecn.ei3info.sig_ar;

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

import com.hitlabnz.outdoorar.api.OAScene;
//ArrayAdapter<Scene> 

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
	private List<OAScene> dataSource;
	private LayoutInflater inflater;

	// constructor for adapter
	public SceneArrayAdapter(Activity activity, int layoutId, List<OAScene> ds){
		parentActivity = activity;
		itemLayoutId = layoutId;
		dataSource = ds;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parentView){
		View view = null;
		if(convertView == null){
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(itemLayoutId, parentView, false);
			
			TextView textView = (TextView)view.findViewById(R.id.scene_name);
			String data = dataSource.get(pos).getName();
			textView.setText(data);

			ImageView imageView = (ImageView) view.findViewById(R.id.icons);
			//imageView.setImageResource(dataSource);
			
			TextView textView2 = (TextView)view.findViewById(R.id.scene_category);
			String data2=dataSource.get(pos).getCategory();
			textView2.setText(data2);
			
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
		}else{
			view = convertView;
		}
		return view;
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
