package com.drzazga.pomiary.controller;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.drzazga.pomiary.R;
import com.drzazga.pomiary.model.MeasureAngleData;
import com.drzazga.pomiary.model.MeasureData;
import com.drzazga.pomiary.model.MeasureDataElement;
import com.drzazga.pomiary.model.MeasureLineData;
import com.drzazga.pomiary.model.MeasurePointData;
import com.drzazga.pomiary.model.MeasureStartPointData;
import com.drzazga.pomiary.view.MeasureElementBarHolder;
import com.drzazga.pomiary.view.MeasurePointBarHolder;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MeasureController implements DeltaCompassController.CompassListener {

    private final Context context;
    public MeasureData model;
    private GuiHacks guiHacks;
    private MeasureDataElement selectedItem = null;
    private MeasureElementBarHolder bar = null;
    private DeltaCompassController compass;
    private boolean compassWorking = false;
    private Float compassValue;
    private ActionMode mode;

    public MeasureController(int measureId, Context context) {
        this.context = context;
        this.model = new MeasureData(context, measureId);
        this.guiHacks = (GuiHacks) context;
        this.compass = new DeltaCompassController(context, this);
        loseFocus();
        model.load();
    }

    public View.OnClickListener buttonAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedItem instanceof MeasurePointData) {
                newConnection((MeasurePointData) selectedItem);
            }
        }
    };

    public View.OnClickListener buttonDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            model.delete(selectedItem);
            loseFocus();
            model.load();
            guiHacks.redraw();
        }
    };

    public View.OnClickListener buttonModifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openContextMode();
        }
    };

    public View.OnClickListener buttonSwitchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedItem instanceof MeasureAngleData) {
                switchAngles((MeasureAngleData) selectedItem);
            }
        }
    };

    private void loseFocus() {
        for (MeasureDataElement item : model) {
            if (item.isFocused()) {
                item.loseFocus();
                selectedItem = null;
                return;
            }
        }
    }

    public void setSelectedItem(MeasureDataElement item) {
        loseFocus();
        item.performSelectedActon();
        if (item instanceof MeasureStartPointData) {
            guiHacks.getModifyButton().hide();
            guiHacks.getDeleteButton().hide();
        }
        else {
            guiHacks.getModifyButton().show();
            guiHacks.getDeleteButton().show();
        }
        if (item instanceof MeasurePointData) {
            guiHacks.getAddButton().show();
        } else {
            guiHacks.getAddButton().hide();
        }
        if (item instanceof MeasureAngleData) {
            guiHacks.getSwitchButton().show();
        } else {
            guiHacks.getSwitchButton().hide();
        }
        selectedItem = item;
    }

    public boolean resolveSelected(PointF p) {
        if (mode == null) {
            if (bar == null) {
                for (MeasureDataElement item : model) {
                    if (item.isSelected(p)) {
                        setSelectedItem(item);
                        return true;
                    }
                }
                loseFocus();
                this.guiHacks.hideAllButtons();
            } else {
                setBarIndicators(p);
                guiHacks.redraw();
                return true;
            }
        }
        return false;
    }

    private void setBarIndicators(PointF p) {
        bar.name.setText(selectedItem.name);
        if (selectedItem instanceof MeasurePointData) {
            MeasurePointData casted = (MeasurePointData) selectedItem;
            MeasurePointBarHolder castedBar = (MeasurePointBarHolder) bar;
            Point relPos = casted.relativeTo.getRelativePos();
            casted.x = (int) p.x - relPos.x;
            casted.y = (int) p.y - relPos.y;
            castedBar.x.setText(String.valueOf(casted.x));
            castedBar.y.setText(String.valueOf(casted.y));
        }
    }

    public boolean isSelectedItem() {
        return selectedItem != null;
    }

    private Set<MeasurePointData> getPointsToMakeAngle(MeasurePointData relativeTo) {
        Set<MeasurePointData> set = new HashSet<>();
        for (MeasureLineData line : model.lines.values()) {
            if (line.p1 == relativeTo)
                set.add(line.p2);
            else if (line.p2 == relativeTo)
                set.add(line.p1);
        }
        return set;
    }

    private void openContextMode() {
        closeContextMode();
        guiHacks.hideAllButtons();
        if (selectedItem instanceof MeasurePointData) {
            final MeasurePointBarHolder specificBar = new MeasurePointBarHolder(guiHacks.showBottomBar(selectedItem.getBarRes()));
            final MeasurePointData casted = (MeasurePointData) selectedItem;
            specificBar.x.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        casted.x = Integer.valueOf(specificBar.x.getText().toString());
                        guiHacks.redraw();
                        return true;
                    }
                    return false;
                }
            });
            specificBar.y.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        casted.y = Integer.valueOf(specificBar.y.getText().toString());
                        guiHacks.redraw();
                        return true;
                    }
                    return false;
                }
            });
            bar = specificBar;
        } else {
            bar = new MeasureElementBarHolder(guiHacks.showBottomBar(selectedItem.getBarRes()));
        }
        bar.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.update(selectedItem);
                closeContextMode();
            }
        });
        bar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeContextMode();
            }
        });
        bar.name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    selectedItem.name = bar.name.getText().toString();
                    guiHacks.redraw();
                    return true;
                }
                return false;
            }
        });
        setBarIndicators(new PointF());
    }

    private void closeContextMode() {
        if (bar != null) {
            loseFocus();
            model.load();
            guiHacks.redraw();
            guiHacks.hideBottomBar();
            bar = null;
        }
    }

    private void newConnection(MeasurePointData relativeTo) {
        int newPointId = model.addPoint(relativeTo.id);
        model.addLine(relativeTo.id, newPointId);
        Set<MeasurePointData> pointsToMakeAngle = getPointsToMakeAngle(relativeTo);
        for (MeasurePointData pointToMakeAngle : pointsToMakeAngle) {
            model.addAngle(relativeTo.id, pointToMakeAngle.id, newPointId);
        }
        model.load();
        MeasurePointData newPoint = model.points.get(newPointId);
        setSelectedItem(newPoint);
        guiHacks.redraw();
        openContextMode();
    }

    private void switchAngles(MeasureAngleData angle) {
        MeasurePointData temp = angle.p1;
        angle.p1 = angle.p2;
        angle.p2 = temp;
        model.update(angle);
        guiHacks.redraw();
    }

    @Override
    public void compassChanged(float degree) {
        compassValue = degree;
        guiHacks.redraw();
        mode.setTitle(context.getString(R.string.compassWorking) + String.format(
                Locale.getDefault(),
                " %.2f" + (char) 0x00B0,
                degree
        ));
    }

    public Float getCompassValue() {
        return compassValue;
    }

    public void activityPaused() {
        compass.stop();
    }

    public void switchCompass() {
        if (compassWorking) {
            compass.stop();
            compassValue = null;
            mode = null;
        }
        else {
            compass.start();
            loseFocus();
            guiHacks.hideAllButtons();
        }
        compassWorking = !compassWorking;
        guiHacks.redraw();
    }

    public void compassContextInit(ActionMode mode) {
        this.mode = mode;
        mode.setTitle(R.string.compassEstablishing);
    }
}
