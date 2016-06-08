package com.drzazga.pomiary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.controller.MeasureController;
import com.drzazga.pomiary.model.MeasureAngleData;
import com.drzazga.pomiary.model.MeasureData;
import com.drzazga.pomiary.model.MeasureDataElement;
import com.drzazga.pomiary.model.MeasureLineData;
import com.drzazga.pomiary.model.MeasurePointData;

public class MeasureSurface extends View {

    public static final float POINT_CIRCLE_R = (float) 18.0;
    public static final float STROKE_WIDTH = (float) 8.0;
    public static final float ANGLE_CIRCLE_R = (float) 100.0;
    public static final float TEXT_SIZE = (float) 24.0;

    private final Paint paintStartPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintAngle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNotFocused = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNotFocusedOutline;
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

    private PointF translation = new PointF(200, 200);
    private float scale = 1;
    private int measureId;
    private GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(), new MeasureSurfaceGestureListener());
    private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new MeasureSurfaceScaleGestureListener());
    private MeasureController controller;

    public MeasureSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        measureId = ((Activity) context).getIntent().getExtras().getInt("id");
        paintStartPoint.setColor(ContextCompat.getColor(context, R.color.colorStartPoint));
        paintPoint.setColor(ContextCompat.getColor(context, R.color.colorPoint));
        paintLine.setColor(ContextCompat.getColor(context, R.color.colorLine));
        paintLine.setStrokeWidth(STROKE_WIDTH);
        paintAngle.setStyle(Paint.Style.STROKE);
        paintAngle.setColor(ContextCompat.getColor(context, R.color.colorAngle));
        paintAngle.setStrokeWidth(STROKE_WIDTH);
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(TEXT_SIZE);
        paintNotFocused.setColor(ContextCompat.getColor(context, R.color.colorNotFocused));
        paintNotFocusedOutline = new Paint(paintNotFocused);
        paintNotFocusedOutline.setStyle(Paint.Style.STROKE);
        paintNotFocusedOutline.setStrokeWidth(STROKE_WIDTH);
        controller = new MeasureController(measureId, getContext());
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }

    private Paint decideOnPaint(MeasureDataElement item, Paint focused, Paint notFocused) {
        if (controller.isSelectedItem() && !item.isFocused())
            return notFocused;
        return focused;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(translation.x, translation.y);
        canvas.scale(scale, scale);
        Point pos, pos2;
        PointF strPos;
        for (MeasureAngleData angle : controller.model.angles.values()) {
            Log.i("testing", String.valueOf(angle));
            canvas.drawArc(angle.getOval(), angle.getStartAngleDeg(), angle.getSweepAngleDeg(), false, decideOnPaint(angle, paintAngle, paintNotFocusedOutline));
        }
        for (MeasureLineData line : controller.model.lines.values()) {
            pos = line.p1.getRelativePos();
            pos2 = line.p2.getRelativePos();
            canvas.drawLine(pos.x, pos.y, pos2.x, pos2.y, decideOnPaint(line, paintLine, paintNotFocusedOutline));
        }
        canvas.drawCircle(MeasureData.startPoint.get(0).x, MeasureData.startPoint.get(0).y, POINT_CIRCLE_R, decideOnPaint(MeasureData.startPoint.get(0), paintStartPoint, paintNotFocused));
        for (MeasurePointData point : controller.model.points.values()) {
            pos = point.getRelativePos();
            canvas.drawCircle(pos.x, pos.y, POINT_CIRCLE_R, decideOnPaint(point, paintPoint, paintNotFocused));
        }
        for (MeasureDataElement item : controller.model) {
            strPos = item.getStringValuePosition();
            canvas.drawText(item.getStringValue(), strPos.x, strPos.y, paintText);
        }
        for (MeasureDataElement item : controller.model) {
            if (item.name != null) {
                strPos = item.getNamePosition();
                canvas.drawText(item.name, strPos.x, strPos.y, paintText);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || scaleGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public OnClickListener getButtonAddListener() {
        return controller.buttonAddListener;
    }

    public OnClickListener getButtonDeleteListener() {
        return controller.buttonDeleteListener;
    }

    public OnClickListener getButtonModifyListener() {
        return controller.buttonModifyListener;
    }

    public OnClickListener getButtonSwitchListener() {
        return controller.buttonSwitchListener;
    }

    private class MeasureSurfaceScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float prevSpan;
        private float startSpan;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            prevSpan = scale;
            startSpan = detector.getCurrentSpan();
            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = prevSpan * detector.getCurrentSpan() / startSpan;
            invalidate();
            return super.onScale(detector);
        }

    }

    private class MeasureSurfaceGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            invalidate();
            Matrix matrix = new Matrix();
            matrix.preScale((float) (1.0 / scale), (float) (1.0 / scale));
            matrix.preTranslate(-translation.x, -translation.y);
            float[] result = new float[] {
                e.getX(),
                e.getY()
            };
            Log.i("touching", "before: (" + result[0] + ", " + result[1] + ")");
            matrix.mapPoints(result);
            Log.i("touching", "after: (" + result[0] + ", " + result[1] + ")");
            return controller.resolveSelected(new PointF(result[0], result[1]));
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("touching", "onLongPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            translation.x -= distanceX;
            translation.y -= distanceY;
            invalidate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    }
}
