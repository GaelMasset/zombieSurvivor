package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.canvasHeight;
import static com.example.zombiesurvivor.Base.MainActivity.canvasWidth;
import static com.example.zombiesurvivor.Base.MainActivity.offsetX;
import static com.example.zombiesurvivor.Base.MainActivity.offsetY;
import static com.example.zombiesurvivor.Base.MainActivity.pourcentHauteur;
import static com.example.zombiesurvivor.Base.MainActivity.pourcentLongueur;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Bouton;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.PixelText;
import com.example.zombiesurvivor.map.IleDescriptor;

import java.util.ArrayList;

public class FenetreChoixNiveau extends Fenetre{
    private int selectedIle;
    private ArrayList<IleDescriptor> iles = new ArrayList<IleDescriptor>();


    public FenetreChoixNiveau(Page p) {
        super(p);
        fond = new Movable(0,0,canvasWidth,canvasHeight,"mapchoose_background", true, 100, 0, 0, 0, 0);
        iles.add(new IleDescriptor(
                new PixelText(p.getContext(), "accueil", pourcentLongueur(123.0/3), pourcentHauteur(39.0/1.5), pourcentHauteur(10.0/1.5),pourcentLongueur(0.18) ,true),
                new Bouton(pourcentLongueur(32.0/3), pourcentHauteur(51.0/1.5), (int) pourcentLongueur(35.0/3), (int) pourcentHauteur(51.0/1.5), "mapchoose_ile1_","mapchoose_ile", true, 80),
                new Bouton(pourcentLongueur(131.0/3), pourcentHauteur(88.0/1.5), (int) pourcentLongueur(19.0/3), (int) pourcentHauteur(19.0/1.5), "mapchoose_playbutton","mapchoose_playbutton_clicked", true, 80),
                new Bouton(pourcentLongueur(96.0/3), pourcentHauteur(88.0/1.5), (int) pourcentLongueur(19.0/3), (int) pourcentHauteur(19.0/1.5), "mapchoose_infobutton","mapchoose_infobutton_clicked", true, 80),
                new Image(p.getContext(), pourcentLongueur(27.0/3), pourcentHauteur(34.0/1.5), (int) pourcentLongueur(128.0/3), (int) pourcentHauteur(78.0/1.5), "mapchoose_ile1_hud", 80)
                ));
    }

    @Override
    public void update() {
        iles.get(0).update();
    }

    @Override
    public void draw(Canvas canvas) {
        fond.draw(canvas);
        iles.get(0).draw(canvas);
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
                    for(IleDescriptor ile: iles) {
                        if (ile.getIle().isPressed(xClicked, yClicked) && !ile.getIsSelected()) {
                            ile.setIsSelected(true);
                        } else if (ile.getIle().isPressed(xClicked, yClicked) && ile.getIsSelected()){
                            ile.setIsSelected(false);
                        }
                        if (ile.getIsSelected()) {
                            if (ile.getPlayButton().isPressed(xClicked, yClicked)) {
                                ile.getPlayButton().setIsPressed(true);
                                goTo(new FenetreTest(this.page));
                            }
                            if (ile.getInfoButton().isPressed(xClicked, yClicked))
                                ile.getInfoButton().setIsPressed(true);
                        }
                    }
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    System.out.println("X : " + xClicked/canvasWidth + " %");
                    return true;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {
                    for(IleDescriptor ile: iles){
                        ile.unselectAll();
                    }
                    return true;
                }
            }
        }
        return true;
    }
}
