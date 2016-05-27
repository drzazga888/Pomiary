package com.drzazga.pomiary.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.DatabaseModel;
import com.drzazga.pomiary.model.MeasureAngleData;
import com.drzazga.pomiary.model.MeasureData;
import com.drzazga.pomiary.model.MeasureDataElement;
import com.drzazga.pomiary.model.MeasureLineData;
import com.drzazga.pomiary.model.MeasurePointData;
import com.drzazga.pomiary.utils.MathExtra;

public class MeasureSurface extends View {

    public static final float POINT_CIRCLE_R = (float) 18.0;
    public static final PointF POINT_START = new PointF((float) 200.0, (float) 200.0);
    public static final float STROKE_WIDTH = (float) 8.0;
    public static final float ANGLE_CIRCLE_R = (float) 100.0;
    public static final float TEXT_SIZE = (float) 24.0;

    private final DatabaseModel databaseModel = new DatabaseModel(getContext());
    private int measureId;
    private final Paint paintStartPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintAngle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNotFocused = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNotFocusedOutline;
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

    private GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(), new MeasureSurfaceGestureListener());
    private MeasureData data;

    public MeasureSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        measureId = ((Activity) context).getIntent().getExtras().getInt("id");
        data = databaseModel.getMeasureData(measureId);
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
    }

    private Paint decideOnPaint(MeasureDataElement item, Paint focused, Paint notFocused) {
        if (data.isSthSelected() && !item.isFocused())
            return notFocused;
        return focused;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PointF pos, pos2;
        for (MeasureAngleData angle : data.angles.values())
            canvas.drawArc(angle.getOval(), angle.getStartAngleDeg(), angle.getSweepAngleDeg(), false, decideOnPaint(angle, paintAngle, paintNotFocusedOutline));
        for (MeasureLineData line : data.lines.values()) {
            pos = line.p1.getRelativePos();
            pos2 = line.p2.getRelativePos();
            canvas.drawLine(pos.x, pos.y, pos2.x, pos2.y, decideOnPaint(line, paintLine, paintNotFocusedOutline));
        }
        canvas.drawCircle(POINT_START.x, POINT_START.y, POINT_CIRCLE_R, decideOnPaint(MeasureData.startPoint.get(0), paintStartPoint, paintNotFocused));
        for (MeasurePointData point : data.points.values()) {
            pos = point.getRelativePos();
            canvas.drawCircle(pos.x, pos.y, POINT_CIRCLE_R, decideOnPaint(point, paintPoint, paintNotFocused));
        }
        for (MeasureDataElement item : data) {
            pos = item.getStringValuePosition();
            canvas.drawText(item.getStringValue(), pos.x, pos.y, paintText);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private class MeasureSurfaceGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            invalidate();
            return data.resolveSelected(new PointF(e.getX(), e.getY()));
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("touching", "onLongPress");
        }
    }
}
