package com.hitlabnz.outdoorar.api;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.hitlabnz.androidar.viewcomponents.ViewComponent;

/**
 * MapBubble displays basic info about a scene with a callout to the scene's
 * position on the map
 * 
 * @author twh35
 * 
 */
public abstract class OANotificationBubbleView extends ViewComponent {

	private int color = Color.BLACK; // color
	private int left = 0; // bubble position
	private int top = 0; // bubble position
	
	private Point point = new Point(0, 0); // where the callout is pointing at
	// two points where the callout starts from on the buble
	private Point rootPoint1 = new Point(0, 0);
	private Point rootPoint2 = new Point(0, 0);
	
	private PopupWindow popupWindow; // main body of the bubble
	private Callout callout; // callout of the bubble
	
	private View notificationView; // view shown in the popupWindow
	
	
	public OANotificationBubbleView(Context context, int left, int top, int width, int height) {
		super(context);
		
		callout = new Callout(context);
		addView(callout);
		
		this.left = left;
		this.top = top;
		
		notificationView = setupNotificationView();
		notificationView.setBackgroundColor(color);
		
		popupWindow = new PopupWindow(notificationView, width, height, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setFocusable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		setVisibility(INVISIBLE); // invisible by default
	}
	
	
	/**
	 * Override this method to provide view to show in the notification bubble.
	 * This method is called back from the constructor of this class.
	 * @return	the view to show
	 */
	abstract public View setupNotificationView();
	
	/**
	 * Override this method to update the point on the screen the notification bubble is pointing at.
	 * Default implementation does nothing, maintaining the last set point.
	 * 
	 * @param point	the point to update
	 */
	public void updatePoint(Point point) {
	};
	
	public void setSize(int width, int height) {
		popupWindow.setWidth(width);
		popupWindow.setHeight(height);
	}
		
	public void setPosition(int left, int top) {
		this.left = left;
		this.top = top;
	}
	
	public void setPoint(int x, int y) {
		this.point.x = x;
		this.point.y = y;
	}
	
	public void setAnchorPoints(int x1, int y1, int x2, int y2) {
		rootPoint1.x = x1;
		rootPoint1.y = y1;
		
		rootPoint2.x = x2;
		rootPoint2.y = y2;
	}
	
	public void setColor(int color) {
		this.color = color;
		notificationView.setBackgroundColor(color);
	}

	/*
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	*/

	public void hide() {
		popupWindow.dismiss();
		setVisibility(INVISIBLE);
		invalidate();
	}
	
	public void show() {
		popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, left, top);
		setVisibility(VISIBLE);
		invalidate();
	}
	
	public boolean isShown() {
		return (this.getVisibility() == VISIBLE);
	}
	
	protected class Callout extends View {

		private Paint paint = new Paint();
		private Path path = new Path();
				
		public Callout(Context context) {
			super(context);
			paint.setStrokeWidth(10);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			updatePoint(point);
			
			// Draw the callout
			path.reset();
			path.moveTo(point.x, point.y);
			path.lineTo(rootPoint1.x, rootPoint1.y);
			path.lineTo(rootPoint2.x, rootPoint2.y);
			path.lineTo(point.x, point.y);
			paint.setColor(color);
			canvas.drawPath(path, paint);
		}
	}

}
