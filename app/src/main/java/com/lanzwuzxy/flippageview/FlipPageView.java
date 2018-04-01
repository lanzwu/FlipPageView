package com.lanzwuzxy.flippageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FlipPageView extends View {

    private Paint mPaint;
    private Canvas canvas;
    private Bitmap page;
    private PointF finger, start_l, end_l, control_l, start_r, end_r, control_r;
    private PointF corner, pointG, pointE, pointH, pointC, pointJ, pointB, pointK, pointD, pointI;
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
        mPaint.setTextSize(20);
        mPaint.setAntiAlias(true);

        page = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(page);
        init();
    }

    private void init() {
        finger = new PointF(0, 0);
        corner = new PointF(0, 0);
        pointG = new PointF(0, 0);
        pointE = new PointF(0, 0);
        pointH = new PointF(0, 0);
        pointC = new PointF(0, 0);
        pointJ = new PointF(0, 0);
        pointB = new PointF(0, 0);
        pointK = new PointF(0, 0);
        pointD = new PointF(0, 0);
        pointI = new PointF(0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(finger.x, finger.y, 10, mPaint);
        canvas.drawCircle(corner.x, corner.y, 10, mPaint);
        canvas.drawText("E", pointE.x, pointE.y, mPaint);
        canvas.drawText("G", pointG.x, pointG.y, mPaint);
        canvas.drawText("H", pointH.x, pointH.y, mPaint);

        canvas.drawText("J", pointJ.x, pointJ.y, mPaint);
        canvas.drawText("C", pointC.x, pointC.y, mPaint);

        canvas.drawText("B", pointB.x, pointB.y, mPaint);
        canvas.drawText("K", pointK.x, pointK.y, mPaint);

        canvas.drawText("D", pointD.x, pointD.y, mPaint);
        canvas.drawText("I", pointI.x, pointI.y, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        finger = new PointF(event.getX(), event.getY());
        if (finger.x > screenWidth / 2 && finger.x < screenWidth
                && finger.y > screenHeight / 2 && finger.y < screenHeight) {
            corner = new PointF(screenWidth, screenHeight);
        } else if (finger.x > screenWidth / 2 && finger.x < screenWidth
                && finger.y > 0 && finger.y < screenHeight / 2) {
            corner = new PointF(screenWidth, 0);
        }
        pointG = new PointF((finger.x + corner.x) / 2, (finger.y + corner.y) / 2);

        pointE = new PointF(pointG.x -
                (corner.y - pointG.y) * (corner.y - pointG.y) / (corner.x - pointG.x), corner.y);
        pointH = new PointF(corner.x, pointG.y -
                (corner.x - pointG.x) * (corner.x - pointG.x) / (corner.y - pointG.y));

        pointJ = new PointF(corner.x, pointH.y - (corner.y - pointH.y) / 2);
        pointC = new PointF(pointE.x - (corner.x - pointE.x) / 2, corner.y);

        pointB = cross(finger, pointE, pointC, pointJ);
        pointK = cross(finger, pointH, pointC, pointJ);

        pointD = new PointF((pointC.x + 2 * pointE.x + pointB.x) / 4, (pointC.y + 2 * pointE.y + pointB.y) / 4);
        pointI = new PointF((pointJ.x + 2 * pointH.x + pointK.x) / 4, (pointJ.y + 2 * pointH.y + pointK.y) / 4);


        postInvalidate();
        performClick();
        return true;
    }

    private PointF cross(PointF p1, PointF p2, PointF p3, PointF p4) {
        float crossX = ((p1.x - p2.x) * (p3.x * p4.y - p4.x * p3.y) - (p3.x - p4.x) * (p1.x * p2.y - p2.x * p1.y))
                / ((p3.x - p4.x) * (p1.y - p2.y) - (p1.x - p2.x) * (p3.y - p4.y));
        float crossY = ((p1.y - p2.y) * (p3.x * p4.y - p4.x * p3.y) - (p1.x * p2.y - p2.x * p1.y) * (p3.y - p4.y))
                / ((p1.y - p2.y) * (p3.x - p4.x) - (p1.x - p2.x) * (p3.y - p4.y));
        return new PointF(crossX, crossY);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
