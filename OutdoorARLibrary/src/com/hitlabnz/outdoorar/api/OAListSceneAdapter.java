/*
 * Copyright 2011 the Human Interface Technology Laboratory New Zealand, University of Canterbury.
 * http://www.hitlabnz.org
 * 
 * This software is provided under the license terms described in LICENSE.TXT file distributed with this software package.
 */

package com.hitlabnz.outdoorar.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hitlabnz.androidar.data.SceneData;
import com.hitlabnz.outdoorar.R;

/**
 * Basic adapter class for making custom layout for list items in list components. 
 * @author Gun Lee
 */
public class OAListSceneAdapter extends ArrayAdapter<SceneData> {
	
	protected LayoutInflater layoutInflater;
	private int listItemLayoutId;
	
	/**
	 * Instantiate with the context to access the layout resource for list items.
	 * Subclasses should override this method and call super(context, layoutResId) with a customized layout resource.
	 * @param context	context for accessing resources
	 */
	public OAListSceneAdapter(Context context) {
		this(context, R.layout.oa_list_item);
	}
	
	/**
	 * Instantiate with the context to access the layout resource for list items.
	 * Subclasses should NOT override this method.
	 * @param context		context for accessing resources
	 * @param layoutResId	resource id of the list item layout
	 */
	protected OAListSceneAdapter(Context context, int layoutResId) {
		super(context, R.layout.list_item);
		
		this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listItemLayoutId = layoutResId;
	}
	
	/**
	 * Called back for setting up the list item view for a given scene.
	 * Subclasses should override this method for setting up the list item view for the scene.
	 * Subclasses need NOT to call the method in the super class.
	 * @param listItemView	view for the list item, create with the layout resource id given at instan
	 * @param scene
	 */
	protected void setupListItemView(View listItemView, OAScene scene) {
		TextView nameText = (TextView) listItemView.findViewById(R.id.listItemName);
		TextView categoryText = (TextView) listItemView.findViewById(R.id.listItemCategory);
		
		nameText.setText(scene.name);
		if(scene.category == null)
			categoryText.setText("");
		else
			categoryText.setText("- " + scene.category);
	}
	
	/**
	 * Called back for a view for the list item.
	 * Subclasses should NOT override this method, but override the setupListItemView() method for customization.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null)
			convertView = layoutInflater.inflate(listItemLayoutId, parent, false);
		
		OAScene scene = (OAScene)this.getItem(position);
		setupListItemView(convertView, scene);
		return convertView;
	}
	
}
