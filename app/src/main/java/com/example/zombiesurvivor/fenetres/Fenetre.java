package com.example.zombiesurvivor.fenetres;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Movable;

public abstract class Fenetre {
    protected Movable fond;
    protected final Page page;

    public Fenetre(Page p){
        this.page = p;
    }

    public abstract void update();
    public abstract void draw(Canvas canvas);
    public void goTo(Fenetre f){
        page.setFenetre(f);
    }
    public abstract boolean onClick(MotionEvent event);


}
