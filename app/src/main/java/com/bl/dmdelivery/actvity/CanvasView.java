package com.bl.dmdelivery.actvity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.bl.dmdelivery.R;
import com.bl.dmdelivery.model.Order;
import com.bl.dmdelivery.model.Unpack;

import java.util.ArrayList;

public class CanvasView extends View {

	public static int totalDx = 0;
	public static int totalDy = 0;

	public String invoice;
	public String rep_code;
	public String rep_name;
	public String rep_addr12;
	public String rep_addr34;
	public String repdist_tel = "";
	public String order_type = "";
	public String mailgroup = "";
	public String carton = "";
	public String bag = "";
	public String printmark = "";
	public String latlng;
	public String reason="";
	public String gps="";
	public String gpstext="";
	public String note="";


	public int width;
	public int height;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	Context context;
	private Paint mPaint;
	private float mX, mY;
	private static final float TOLERANCE = 5;
	public int xUnpack,yUnpack;
	public int xReason,yReason;
	public int xGps,yGps;
	public int xGpstext,yGpstext;
	public int xOrder,yOrder;
	public int xNote,yNote;
	private String defaultFonts = "fonts/PSL162pro-webfont.ttf";
	private Typeface tf;


	public ArrayList<Unpack> mListUnpackData = new ArrayList<Unpack>();
	public ArrayList<Order> mListOrderData = new ArrayList<Order>();

	//GlobalObject ogject = GlobalObject.getInstance();

	public CanvasView(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;

		// we set a new Path
		mPath = new Path();

		// and we set a new Paint with the desired attributes
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		//mPaint.setStrokeWidth(4f);
		mPaint.setStrokeWidth(5f);



		tf = Typeface.createFromAsset(getContext().getAssets(), defaultFonts);

	}

	// override onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// your Canvas will draw onto the defined Bitmap
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	// override onDraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
	/*	paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPaint(paint);*/

		paint.setColor(context.getResources().getColor(R.color.colorStroke));
		paint.setTextSize(50);
		paint.setTypeface(tf);

		//canvas.drawText("Some Text", 10, 25, paint);

//		mListUnpackData = ogject.getListUnpackData();
//
		xUnpack = 20;
		yUnpack = 50;

		if(mListUnpackData != null)
		{
			if(mListUnpackData.size()>0)
			{
				for(int i=0; i<mListUnpackData.size();i++){

					String unpacktext = "";

					if(mListUnpackData.get(i).getUnpack_desc().toString().length() < 50)
					{
						unpacktext = mListUnpackData.get(i).getUnpack_desc().toString();
					}
					else
					{
						unpacktext = mListUnpackData.get(i).getUnpack_desc().toString().substring(0,49)+"...";
					}

					canvas.drawText(unpacktext+" x "+mListUnpackData.get(i).getUnpack_qty().toString(), xUnpack, yUnpack, paint);
					//canvas.drawText(mListUnpackData.get(i).getFsname().toString(), 10, 25, paint);
					//canvas.drawText("ของนอกกล่อง"+yUnpack, xUnpack, yUnpack, paint);
					yUnpack=yUnpack+45;
				}
			}
		}



		xOrder = 780;
		yOrder = 1115;


		if(mListOrderData != null)
		{
			if(mListOrderData.size()>0)
			{
				for(int i=0; i<mListOrderData.size();i++){

					String inv = "";
					String carton = "";

					inv = mListOrderData.get(i).getTransNo().toString();
					carton = mListOrderData.get(i).getCont_desc().toString();

					canvas.drawText(inv+" "+carton, xOrder, yOrder, paint);
					//canvas.drawText(mListUnpackData.get(i).getFsname().toString(), 10, 25, paint);
					//canvas.drawText("ของนอกกล่อง"+yUnpack, xUnpack, yUnpack, paint);
					yOrder=yOrder-50;
				}
			}
		}




		xGps = 20;
		yGps = 970;

		if(!gps.equals(""))
		{
			canvas.drawText(gps, xGps, yGps, paint);
		}




		xGpstext = 20;
		//yGpstext = 890;
		yGpstext = 1020;

		if(!gpstext.equals(""))
		{
			canvas.drawText(gpstext, xGpstext, yGpstext, paint);
		}




		xReason = 20;
		//yReason = 940;
		yReason = 1070;

		if(!reason.equals(""))
		{
			canvas.drawText("เหตุผล : "+reason, xReason, yReason, paint);
		}




		xNote = 20;
		//yReason = 940;
		yNote = 1115;

		if(!note.equals(""))
		{
			canvas.drawText("หมายเหตุ : "+note, xNote, yNote, paint);
		}else
		{
			canvas.drawText("หมายเหตุ : ", xNote, yNote, paint);
		}






		// draw the mPath with the mPaint on the canvas when onDraw
		canvas.drawPath(mPath, mPaint);
	}

	// when ACTION_DOWN start touch according to the x,y values
	private void startTouch(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	// when ACTION_MOVE move touch according to the x,y values
	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOLERANCE || dy >= TOLERANCE) {

			totalDx += dx;
			totalDy += dy;

			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	public void clearCanvas() {
		mPath.reset();
		invalidate();
	}

	// when ACTION_UP stop touch
	private void upTouch() {
		mPath.lineTo(mX, mY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startTouch(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			moveTouch(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			upTouch();
			invalidate();
			break;
		}
		return true;
	}
}