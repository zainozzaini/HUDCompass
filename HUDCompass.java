
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;

public class HUDCompass extends View
{
	private Context context;
	
	private int centerX;
	private int centerY;
	private int height;
	
	protected float azimuth;
	private float azimuthPixel;
	protected float roll;
	
	protected float pitch;
	private float pitchPixel;
	
	private final static int greenColor = Color.parseColor("#33FF00");

	private float density;
	private Paint pLine;
	private Paint pTxtCenter;
	private Paint pTxtLeft;
	private Paint pTxtRight;
	
	private float line_4;
	private float line_5;
	private float line_10;
	private float line_12;
	private float line_24;
	private float line_30;
	private float line_40;
	private float line_50;

	private int width;

	public HUDCompass(Context context)
	{
		super(context);
		init(context);
	}

	public HUDCompass(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public HUDCompass(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
	}



	private void drawAzimuth(Canvas canvas, int paramInt)
	{
		int i = paramInt % 5;


		for(int j=0;j<8;j++){
			int k = i + j * 5;
			int m = paramInt - k;
			float f2;
			int i1;

			int n = 5 - i + j * 5;
			f2 = centerX + n * azimuthPixel;

			float f1 = centerX - k * azimuthPixel;
			if (m < 0)
				m = (m + 360) % 360;

			canvas.drawLine(f1, 0.0F, f1, line_4, pLine);//small line



			if (m % 10 == 0) {
				canvas.drawLine(f1, 0.0F, f1, line_12, pLine);
				if (m % 30 == 0){
					if(m==0 || m==360)
						canvas.drawText(String.valueOf("N"), f1, line_24, pTxtCenter);
					else if (m == 90)
						canvas.drawText(String.valueOf("E"), f1, line_24, pTxtCenter);
					else if (m == 180)
						canvas.drawText(String.valueOf("S"), f1, line_24, pTxtCenter);
					else if (m == 270)
						canvas.drawText(String.valueOf("W"), f1, line_24, pTxtCenter);
					else
						canvas.drawText(String.valueOf(m / 10), f1, line_24, pTxtCenter);
				}



			}else{
				canvas.drawLine(f2, 0.0F, f2, line_12, pLine);
			}




			i1 = paramInt + n;
			if (i1 < 0)
				i1 = (i1 + 360) % 360;

			if (i1 % 10 != 0)
				canvas.drawLine(f2, 0.0F, f2, line_4, pLine);

			if (i1 % 30 == 0)
			{
				if(i1==0 || i1==360)
					canvas.drawText(String.valueOf("N"), f2, line_24, pTxtCenter);
				else if (i1 == 90)
					canvas.drawText(String.valueOf("E"), f2, line_24, pTxtCenter);
				else if (i1 == 180)
					canvas.drawText(String.valueOf("S"), f2, line_24, pTxtCenter);
				else if (i1 == 270)
					canvas.drawText(String.valueOf("W"), f2, line_24, pTxtCenter);
				else
					canvas.drawText(String.valueOf(i1 / 10), f2, line_24, pTxtCenter);

			}

		}
	}



	private void drawCenterArrow(Canvas canvas)
	{

		canvas.drawLine(centerX, centerY+ line_10, centerX, centerY - line_10, pLine);
		canvas.drawLine(centerX + line_10, centerY, centerX  - line_10, centerY, pLine);
		
		
		canvas.drawLine(centerX, line_40, centerX - line_5, line_50, pLine);
		canvas.drawLine(centerX, line_40, centerX + line_5, line_50, pLine);
		canvas.drawLine(centerX - line_5, line_50, centerX + line_5, line_50, pLine);
	}

	private void drawRollAndPitch(Canvas canvas, float paramFloat, int paramInt)
	{
		int i = paramInt % 50;
		canvas.rotate(paramFloat, centerX, centerY);
		
		canvas.drawArc(new RectF(centerX - (centerY - line_40), line_40, centerX + (centerY - line_40), height - line_40), 210.0F, 120.0F, false, pLine);
		canvas.drawLine(centerX - line_5, line_30, centerX, line_40, pLine);
		canvas.drawLine(centerX + line_5, line_30, centerX, line_40, pLine);
		canvas.drawLine(centerX - line_5, line_30, centerX + line_5, line_30, pLine);


		for(int j=0;j<5;j++){
			int k = i + j * 50;
			float f1 = k + centerY;
			int m = paramInt - k;
			float f2;

			
			
			if (f1 <= 200 + centerY){
				canvas.drawLine(centerX - 50, f1, 50 + centerX, f1, pLine);
				if (m % 100 == 0){
					if (m > 900) m = 900 - (m - 900);
					if (m < -900)m = -900 - (900 + m);
					
					canvas.drawLine(centerX - 80, f1, 80 + centerX, f1, pLine);
					
					canvas.drawText(String.valueOf(m / 10), centerX - 90, f1 + line_5, pTxtRight);
					canvas.drawText(String.valueOf(m / 10), 90 + centerX, f1 + line_5, pTxtLeft);
				}
					
			}
			
			int n = 50 - i + j * 50;
			f2 = centerY - n;
			int i1 = paramInt + n;
			
			
			if (f2 >= centerY - 150){
				canvas.drawLine(centerX - 50, f2, 50 + centerX, f2, pLine);
				
				if (i1 % 100 == 0){
					if (i1 > 900) i1 = 900 - (i1 - 900);
					if (i1 < -900) i1 = -900 - (900 + i1);
					canvas.drawLine(centerX - 80, f2, 80 + centerX, f2, pLine);
					canvas.drawText(String.valueOf(i1 / 10), centerX - 90, f2 + line_5, pTxtRight);
					canvas.drawText(String.valueOf(i1 / 10), 90 + centerX, f2 + line_5, pTxtLeft);
				}
					
				
			}
			
		
		}



	}



	private void init(Context context)
	{
		this.context = context;
		WindowManager localWindowManager = (WindowManager)context.getSystemService("window");
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
		density = localDisplayMetrics.density;
		
		//creating line
		line_4 = (4.0F * density);
		line_5 = (5.0F * density);
		line_10 = (10.0F * density);
		line_12 = (12.0F * density);
		line_24 = (24.0F * density);
		line_30 = (30.0F * density);
		line_40 = (40.0F * density);
		line_50 = (50.0F * density);

		//define pixel
		azimuthPixel = (2.6F * density);
		pitchPixel = 10.0F;
		
		//line and text color
		pLine = new Paint(1);
		pLine.setStyle(Paint.Style.STROKE);
		pLine.setColor(-13369549);
		pLine.setStrokeWidth(2.0F * density);
		pTxtCenter = new Paint(1);
		pTxtCenter.setColor(-13369549);
		pTxtCenter.setTextAlign(Paint.Align.CENTER);
		pTxtCenter.setTextSize(14.0F * density);
		pTxtRight = new Paint(1);
		pTxtRight.setColor(-13369549);
		pTxtRight.setTextAlign(Paint.Align.RIGHT);
		pTxtRight.setTextSize(14.0F * density);
		pTxtLeft = new Paint(1);
		pTxtLeft.setColor(-13369549);
		pTxtLeft.setTextAlign(Paint.Align.LEFT);

	}

	protected void onDraw(Canvas canvas)
	{
		float f1 = 3;
		pLine.setStrokeWidth(f1 * density);
		float f2 = 14;
		pTxtCenter.setTextSize(f2 * density);
		pTxtRight.setTextSize(f2 * density);
		pTxtLeft.setTextSize(f2 * density);
		
		drawCenterArrow(canvas);
		drawAzimuth(canvas,(int)azimuth);
		drawRollAndPitch(canvas, roll, (int)(pitch * pitchPixel));
	}

	protected void onMeasure(int paramInt1, int paramInt2)
	{
		setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), View.MeasureSpec.getSize(paramInt2));
		height = getMeasuredHeight();
		width = getMeasuredWidth();
		centerX = (width / 2);
		centerY = (height / 2);
	}
}
