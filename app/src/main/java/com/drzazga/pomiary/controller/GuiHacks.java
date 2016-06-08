package com.drzazga.pomiary.controller;

import android.support.design.widget.FloatingActionButton;
import android.widget.LinearLayout;

public interface GuiHacks {
    LinearLayout showBottomBar(int barRes);
    void hideBottomBar();
    FloatingActionButton getAddButton();
    FloatingActionButton getModifyButton();
    FloatingActionButton getDeleteButton();
    FloatingActionButton getSwitchButton();
    void hideAllButtons();
    void redraw();
}
