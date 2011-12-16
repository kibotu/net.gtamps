package net.gtamps.android.menu;

import net.gtamps.android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MenuButton extends Button {
	public MenuButton(Context context, AttributeSet attrs){
		super(context, attrs,0);
		init(attrs);
	}
	public MenuButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
		
	}
	
	private void init(AttributeSet attrs) {
		this.pos = new float[2];
		this.pos[0] = 15;
		this.pos[1] = 25;
		String currentText = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
		setWidth(currentText.length()*9+30);	
		setHeight(40);
	}

	private float[] pos; 
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = this.getPaint();
		//paint.setARGB(0, 0, 0, 0);
		//canvas.drawRect(new Rect(0, 0, this.getWidth(), this.getHeight()), paint);
		paint.setTypeface(Typeface.MONOSPACE);
		paint.setColor(0xffFFB400);
		canvas.drawText((String) this.getText(), pos[0], pos[1], paint);
		
	}	

}
