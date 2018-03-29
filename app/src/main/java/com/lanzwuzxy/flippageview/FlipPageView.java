package com.lanzwuzxy.flippageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FlipPageView extends View {

    private Paint mPaint;
    private Canvas canvas;
    private Bitmap page;
    private PointF finger, start_l, end_l, control_l, start_r, end_r, control_r;
    private PointF corner, pointG, pointE;
    private int screenWidth, screenHeight;

    FlipPageView(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);

        page = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(page);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        finger = new PointF(event.getX(), event.getY());
        if (finger.x > (screenWidth - 100) && finger.x < screenWidth
                && finger.y > (screenHeight - 100) && finger.y < screenHeight) {
            corner = new PointF(screenWidth, screenHeight);
        } else if (finger.x > (screenWidth - 100) && finger.x < screenWidth
                && finger.y > 0 && finger.y < 100) {
            corner = new PointF(screenWidth, 0);
        }
        pointG = new PointF((finger.x + corner.x) / 2, (finger.y + corner.y) / 2);
        pointE = new PointF(pointG.x - (screenWidth - pointG.y) *
                (screenWidth - pointG.y) / (screenWidth - pointG.x), screenHeight);


        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
