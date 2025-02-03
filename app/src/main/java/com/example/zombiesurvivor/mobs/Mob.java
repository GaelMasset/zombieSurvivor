package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
    protected static int NB_RAYON_NEARBY_OBS = 24;
    protected Ray[] nearbyObstacle = new Ray[NB_RAYON_NEARBY_OBS];
    int colorGreen = 0xFF00FF00;
    int colorRed = 0xFFFF0000;
    public Mob(Context context, double posX, double posY, int tailleX,
               int tailleY, String cheminImages,
               double enfoncementTop, double enfoncementBottom,
               double enfoncementLeft, double enfoncementRight,
               boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp,
               double speed) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiBetweenFrame, hp, maxHp);

        this.speed = speed;
// Instanciation des rays autour du Mob (dans les 8 directions cardinales et diagonales)
        float centerX = (float) (getPosX() + tailleX / 2);
        float centerY = (float) (getPosY() + tailleY / 2);
        int longueur = 250;

// Directions cardinales et diagonales avec leurs angles correspondants (en radians)
        for (int i = 0; i < NB_RAYON_NEARBY_OBS; i++) {
            float angle = (float) (i * (2 * Math.PI / NB_RAYON_NEARBY_OBS));  // Angle uniforme autour du cercle
            nearbyObstacle[i] = new Ray(centerX, centerY, angle, longueur);
        }

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

        // Met à jour chaque rayon
        for (Ray ray : nearbyObstacle) {
            ray.setStart((float) centerX, (float) centerY);  // Déplacer le point de départ de chaque rayon
        }

        // Vérifier les collisions
        ArrayList<Movable> mov = new ArrayList<>();
        mov.add(Game.getPartie().getJoueur());
        checkRayCollisions(Game.getPartie());    }

    // Vérifier les collisions avec les Movables (obstacles)
    void checkRayCollisions(Game partie) {
        for (Ray ray : nearbyObstacle) {
            float longueur = ray.getMaxLength();
            for (Movable movable : obstacles) {
                float longMov = ray.checkCollision(movable);
                if (longMov < longueur) {
                    longueur = longMov;
                }
            }
            ray.setLength(longueur);
            if(longueur < ray.getMaxLength()){
                ray.setColorPaint(colorFound);
            } else{
                ray.setColorPaint(colorRed);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Ray ray : nearbyObstacle) {
            ray.draw(canvas);
        }
    }
}
