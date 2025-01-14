package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Floor;

public class FenetreAccueil extends Fenetre {
    private Floor fond;

    public FenetreAccueil(Page p) {
        super(p);

        fond = new Floor(p.getContext(), 0,0,canvasWidth,canvasHeight,"background_accueil", true, 100, 0, 0, 0, 0);
    }

    @Override
    public void update() {
        fond.update();
    }

    @Override
    public void draw(Canvas canvas) {
        fond.draw(canvas);
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
                    System.out.println("Click en move");
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
