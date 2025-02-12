package com.example.zombiesurvivor.mobs;

import static com.example.zombiesurvivor.carte.GenerateurNiveau.TAILLE_CASE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.zombiesurvivor.Destroyable;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Ray;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Tag;

import java.util.ArrayList;

public abstract class Mob extends Destroyable {
    protected Action action = Action.IDLE;
    protected Action lastDirection = Action.WALKING_RIGHT;
    protected ArrayList<ArrayList<Bitmap>> animations = new ArrayList<ArrayList<Bitmap>>();
    protected double speed;
    int colorGreen = 0xFF00FF00;
    int colorRed = 0xFFFF0000;
    int colorBlue = 0xFF0000FF;
    int colorPurple = 0xFFFFAAAA;
    public Mob(double posX, double posY, int tailleX,
               int tailleY, String cheminImages,
               double enfoncementTop, double enfoncementBottom,
               double enfoncementLeft, double enfoncementRight,
               boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp,
               double speed) {
        super(posX, posY, tailleX, tailleY, cheminImages, isAnimating, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiBetweenFrame, hp, maxHp);

        this.speed = speed;



        // Initialisation des animations (inchangée)
        int slot = 0;
        for (String animation : getAnimations()) {
            animations.add(new ArrayList<Bitmap>());
            int i = 1;
            boolean moreImg = true;
            while (moreImg) {
                int resId = context.getResources().getIdentifier(cheminImages + animation + i, "drawable", context.getPackageName());
                if (resId != 0) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                    animations.get(slot).add(Bitmap.createScaledBitmap(bitmap, tailleX, tailleY, false));
                    i++;
                } else {
                    moreImg = false;
                }
            }
            slot++;
        }
    }

    public abstract String[] getAnimations();

    @Override
    public void destroy() {
        // Gestion de la destruction (vide ici)
    }

    // Méthode update : met à jour la position des zones autour du Mob
    public void update() {
        // Calcul des positions des zones autour du Mob
        double centerX = getPosX() + tailleX / 2;
        double centerY = getPosY() + tailleY / 2;


        // Vérifier les collisions
        ArrayList<Movable> mov = new ArrayList<>();
        mov.add(Game.getPartie().getJoueur());
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
