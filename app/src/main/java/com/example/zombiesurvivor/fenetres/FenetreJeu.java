package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Base.Page;

public class FenetreJeu extends Fenetre {


    public FenetreJeu(Page p) {
        super(p);

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public boolean onClick(MotionEvent event) {
        int motionaction = event.getActionMasked();

        int pointerCount = event.getPointerCount();
        double xClicked = event.getX() - offsetX;
        double yClicked = event.getY() - offsetY;

        for (int i = 0; i < pointerCount; i++) {
            switch (motionaction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    System.out.println("Click en bas");
                    goTo(new FenetreTest(page));
                    System.out.println("Jeu en lol");
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    System.out.println("Click en mouve");
                    return true;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {
                    System.out.println("Click en haut");
                    return true;
                }
            }
        }
        return true;
    }
}
