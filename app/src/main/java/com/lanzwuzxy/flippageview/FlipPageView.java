package com.lanzwuzxy.flippageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private Path cornerPath;
    private PointF finger, corner, pointG, pointE, pointH, pointC, pointJ, pointB, pointK, pointD, pointI;
    private int screenWidth, screenHeight;

    FlipPageView(Context context) {
        super(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
        }
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;


        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(20);
        mPaint.setAntiAlias(true);

        page = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(page);

        cornerPath = new Path();
        init();
    }

    private void init() {
        finger = new PointF(0, 0);
        corner = new PointF(screenWidth, screenHeight);
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

//        canvas.drawText("E", pointE.x, pointE.y, mPaint);
//        canvas.drawText("G", pointG.x, pointG.y, mPaint);
//        canvas.drawText("H", pointH.x - 10, pointH.y, mPaint);
//
//        canvas.drawText("J", pointJ.x - 10, pointJ.y, mPaint);
//        canvas.drawText("C", pointC.x, pointC.y, mPaint);
//
//        canvas.drawText("B", pointB.x, pointB.y, mPaint);
//        canvas.drawText("K", pointK.x, pointK.y, mPaint);
//
//        canvas.drawText("D", pointD.x, pointD.y, mPaint);
//        canvas.drawText("I", pointI.x, pointI.y, mPaint);

        canvas.drawPath(cornerPath, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finger.set(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (finger.x > screenWidth / 2 && finger.x < screenWidth
                        && finger.y > screenHeight / 4 * 3 && finger.y < screenHeight) {
                    corner.set(screenWidth, screenHeight);
                } else if (finger.x > screenWidth / 2 && finger.x < screenWidth
                        && finger.y > 0 && finger.y < screenHeight / 4) {
                    corner.set(screenWidth, 0);
                }

                if (finger.x > screenWidth / 5 * 2 && finger.x < screenWidth
                        && finger.y > screenHeight / 4 && finger.y < screenHeight / 4 * 3) {
                    finger.set(event.getX(), event.getY() - 10);
                }
            case MotionEvent.ACTION_MOVE:

                calculatePoint(finger.x, finger.y);

                if (pointC.x <= 0) {
                    reCalculateFinger();
                }
                drawPath(false);
                break;
            case MotionEvent.ACTION_UP:
                drawPath(true);
                break;
        }

        performClick();
        return true;
    }

    private void calculatePoint(float x, float y) {
        pointG.set((x + corner.x) / 2f, (y + corner.y) / 2f);

        pointE.set(pointG.x -
                (corner.y - pointG.y) * (corner.y - pointG.y) / (corner.x - pointG.x), corner.y);
        pointH.set(corner.x, pointG.y -
                (corner.x - pointG.x) * (corner.x - pointG.x) / (corner.y - pointG.y));

        pointC.set(pointE.x - (corner.x - pointE.x) / 2f, corner.y);
        pointJ.set(corner.x, pointH.y - (corner.y - pointH.y) / 2f);

        pointB.set((x + pointE.x) / 2, (y + pointE.y) / 2);
        pointK.set((x + pointH.x) / 2, (y + pointH.y) / 2);

        pointD.set((pointC.x + 2f * pointE.x + pointB.x) / 4f, (pointC.y + 2f * pointE.y + pointB.y) / 4f);
        pointI.set((pointJ.x + 2f * pointH.x + pointK.x) / 4f, (pointJ.y + 2f * pointH.y + pointK.y) / 4f);
    }

    private void reCalculateFinger() {
        PointF fingerCal = new PointF();
        float w0 = screenWidth - pointC.x;

        float w1 = Math.abs(corner.x - finger.x);
        float w2 = screenWidth * w1 / w0;
        fingerCal.x = Math.abs(corner.x - w2);

        float h1 = Math.abs(corner.y - finger.y);
        float h2 = w2 * h1 / w1;
        fingerCal.y = Math.abs(corner.y - h2);
        finger.set(fingerCal.x, fingerCal.y);

        calculatePoint(finger.x, finger.y);
    }

    private void drawPath(boolean clean) {
        cornerPath.reset();
        if (!clean) {
            cornerPath.moveTo(corner.x, corner.y);
            cornerPath.lineTo(pointC.x, pointC.y);
            cornerPath.quadTo(pointE.x, pointE.y, pointB.x, pointB.y);
            cornerPath.lineTo(finger.x, finger.y);
            cornerPath.lineTo(pointK.x, pointK.y);
            cornerPath.quadTo(pointH.x, pointH.y, pointJ.x, pointJ.y);
            cornerPath.close();
        }
        postInvalidate();
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
