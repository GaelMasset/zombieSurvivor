package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.zombiesurvivor.Destroyable;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Obstacle;
import com.example.zombiesurvivor.Ray;
import com.example.zombiesurvivor.Movable;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Mob extends Destroyable {
    protected Action action = Action.IDLE;
    protected Action lastDirection = Action.WALKING_RIGHT;
    protected ArrayList<ArrayList<Bitmap>> animations = new ArrayList<ArrayList<Bitmap>>();
    protected double speed;
    protected Ray[] surroundingZones = new Ray[8];  // 8 rayons autour du Mob
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
        int longueur = 200;

// Directions cardinales et diagonales avec leurs angles correspondants (en radians)
        surroundingZones[0] = new Ray(centerX, centerY, 0, longueur);    // Droite -> angle 0 (0 radians)
        surroundingZones[1] = new Ray(centerX, centerY, (float) Math.PI, longueur);  // Gauche -> angle π
        surroundingZones[2] = new Ray(centerX, centerY, (float) (Math.PI / 2), longueur);   // Bas -> angle π/2
        surroundingZones[3] = new Ray(centerX, centerY, (float) (-Math.PI / 2), longueur);  // Haut -> angle -π/2
        surroundingZones[4] = new Ray(centerX, centerY, (float) (Math.PI / 4), longueur);   // Bas-droit (diagonale) -> angle π/4
        surroundingZones[5] = new Ray(centerX, centerY, (float) (3 * Math.PI / 4), longueur);  // Bas-gauche (diagonale) -> angle 3π/4
        surroundingZones[6] = new Ray(centerX, centerY, (float) (-Math.PI / 4), longueur);  // Haut-droit (diagonale) -> angle -π/4
        surroundingZones[7] = new Ray(centerX, centerY, (float) (-3 * Math.PI / 4), longueur); // Haut-gauche (diagonale) -> angle -3π/4

        // Initialisation des animations (inchangée)
        int slot = 0;
        for (String animation : getAnimations()) {
            animations.add(new ArrayList<Bitmap>());
            int i = 1;
            boolean moreImg = true;
            while (moreImg) {
                int resId = context.getResources().getIdentifier(cheminImages + animation + i, "drawable", context.getPackageName());
                if (resId != 0) {
                    System.out.println("Trouvé pour le " + this.getClass().toString() + cheminImages + animation + i);
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
        for (Ray ray : surroundingZones) {
            ray.setStart((float) centerX, (float) centerY);  // Déplacer le point de départ de chaque rayon
        }

        // Vérifier les collisions
        checkRayCollisions();
    }

    // Vérifier les collisions avec les Movables (obstacles)
    private void checkRayCollisions() {
        ArrayList<Obstacle> obstacles = Game.getPartie().getCarte().getObstacles();

        for (Ray ray : surroundingZones) {
            for (Movable movable : obstacles) {
                if (ray.checkCollision(movable)) {  // Si le rayon touche un obstacle
                    ray.setColorPaint(colorGreen);
                    return;
                } else{
                    ray.setColorPaint(colorRed);
                }
            }
        }
    }

    // Méthode draw : dessine le Mob et ses zones autour de lui
    @Override
    public void draw(Canvas canvas) {
        for (Ray ray : surroundingZones) {
            ray.draw(canvas); // Dessiner chaque rayon
        }
    }
}
