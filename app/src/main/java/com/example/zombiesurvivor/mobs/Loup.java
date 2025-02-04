package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.Destroyable;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Ray;
import com.example.zombiesurvivor.Tag;
import com.example.zombiesurvivor.Zone;

import java.util.ArrayList;

public class Loup extends Mob{
    private int currentAttackCd;
    private int range = 600;
    private Ray[] attraction = new Ray[NB_RAYON_NEARBY_OBS];

    public Loup(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp, double speed, int degatsAttaque, int range) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame, hp, maxHp, speed);
        for(int i = 0; i < NB_RAYON_NEARBY_OBS; i++){
            attraction[i] = new Ray((float) (getPosX()+getTailleX()/2), (float) (getPosY()+getTailleY()/2),
                    (float) (i * (2 * Math.PI / NB_RAYON_NEARBY_OBS)), 200);
        }
    }

    @Override
    public String[] getAnimations() {
        return new String[]{"_idle", "_walking", "_attacking"
        };
    }





    @Override
    public void update() {
        super.update();

        for(int i = 0; i < attraction.length; i++){
            float centerX = (float) (getPosX() + tailleX / 2);
            float centerY = (float) (getPosY() + tailleY / 2);
            attraction[i].setStart(centerX, centerY);
            attraction[i].setLength(attraction[i].getMaxLength());
        }

        for (Ray ray : nearbyObstacle) {
            ray.setColorPaint(colorRed);
            float longueur = ray.getMaxLength();
                float longMov = ray.checkCollision(Game.getPartie().getJoueur());
                if (longMov < longueur) {
                    longueur = longMov;
                    ray.addEncounteredMovable(Game.getPartie().getJoueur());
                    ray.setColorPaint(colorBlue);
            }
            ray.setLength(longueur);
        }

        for (int i = 0; i < attraction.length; i++) {
            if (nearbyObstacle[i].getEncounteredMovable() != null) {
                boolean containsDeco = false;
                for (Movable m : nearbyObstacle[i].getEncounteredMovable()) {
                    if (m.hasTag(Tag.DECORATION)) containsDeco = true;
                }

                if (containsDeco) {
                    // Définir les facteurs de réduction selon l’éloignement
                    float[] reductions = {0.1f, 0.2f,0.3f, 0.5f, 0.7f};

                    for (int offset = 1; offset <= reductions.length; offset++) {
                        int leftIndex = (i - offset + NB_RAYON_NEARBY_OBS) % NB_RAYON_NEARBY_OBS;
                        int rightIndex = (i + offset + NB_RAYON_NEARBY_OBS) % NB_RAYON_NEARBY_OBS;

                        attraction[leftIndex].setLength(attraction[leftIndex].getLength() * reductions[offset - 1]);
                        attraction[rightIndex].setLength(attraction[rightIndex].getLength() * reductions[offset - 1]);
                    }
                }
            }
        }



        // 1. Vérifier si le joueur est dans la portée du loup
        Movable joueur = Game.getPartie().getJoueur();
        float joueurX = (float) joueur.getPosX() + joueur.getTailleX() / 2;
        float joueurY = (float) joueur.getPosY() + joueur.getTailleY() / 2;

        float loupX = (float) getPosX() + getTailleX() / 2;
        float loupY = (float) getPosY() + getTailleY() / 2;

        //Ajout float sur la pos du joueur
        float distance = (float) Math.sqrt(Math.pow(joueurX - loupX, 2) + Math.pow(joueurY - loupY, 2));

        if (distance < range) {
            float angleJoueur = (float) Math.atan2(joueurY - loupY, joueurX - loupX);

            int joueurIndex = (int) Math.round((angleJoueur / (2 * Math.PI)) * NB_RAYON_NEARBY_OBS);
            joueurIndex = (joueurIndex + NB_RAYON_NEARBY_OBS) % NB_RAYON_NEARBY_OBS; // Éviter les valeurs négatives

            // Facteurs d'augmentation progressifs
            float[] increments = {1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f, 0.4f};

            for (int offset = 0; offset <= increments.length; offset++) {
                int leftIndex = (joueurIndex - offset + NB_RAYON_NEARBY_OBS) % NB_RAYON_NEARBY_OBS;
                int rightIndex = (joueurIndex + offset + NB_RAYON_NEARBY_OBS) % NB_RAYON_NEARBY_OBS;

                // Sélectionner un facteur d'augmentation progressif
                float factor = increments[Math.min(offset, increments.length - 1)];

                attraction[leftIndex].setLength(attraction[leftIndex].getLength() * factor);
                attraction[rightIndex].setLength(attraction[rightIndex].getLength() * factor);
            }
        }


        float maxLength = 0;
        int bestIndex = 0;
        for (int i = 0; i < attraction.length; i++) {
            if (attraction[i].getLength() > maxLength) {
                maxLength = attraction[i].getLength();
                bestIndex = i;
            }
        }

        float bestAngle = (float) (bestIndex * (2 * Math.PI / NB_RAYON_NEARBY_OBS));

        float dx = (float) ((float) Math.cos(bestAngle) * speed);
        float dy = (float) ((float) Math.sin(bestAngle) * speed);

        if(distance < range) {
            setPosX(getPosX() + dx * speed * 25);
            setPosY(getPosY() + dy * speed * 25);
        }

        if (isAnimating) {
            currentCentiFrame++;
            switch (action) {
                case IDLE:
                    timeCentiBetweenFrame = 50;
                    currentCentiFrame = currentCentiFrame % (animations.get(0).size() * timeCentiBetweenFrame);
                    break;
                case WALKING_RIGHT:
                case WALKING_RIGHT_BACKWARD:
                    posX += 2;
                    timeCentiBetweenFrame = 20;
                    currentCentiFrame = currentCentiFrame % (animations.get(1).size() * timeCentiBetweenFrame);
                    break;

                case WALKING_LEFT:
                case WALKING_LEFT_BACKWARD:
                    posX -= 2;
                    timeCentiBetweenFrame = 20;
                    currentCentiFrame = currentCentiFrame % (animations.get(1).size() * timeCentiBetweenFrame);
                    break;

            }


            if (currentAttackCd > 0) currentAttackCd--;
        }
    }




    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (Ray ray : nearbyObstacle) {
            ray.draw(canvas);
        }
/*        Paint rouge = new Paint();
        rouge.setColor(Color.RED);
        Paint blanc = new Paint();
        blanc.setColor(Color.WHITE);
        canvas.drawRect((float)posX, (float) ((float)posY-MainActivity.pourcentHauteur(1.5)), (float)posX+tailleX, (float)posY, blanc);
        canvas.drawRect((float)posX, (float) ((float)posY-MainActivity.pourcentHauteur(1.5)), (float) ((float)posX+(tailleX*((getHp()+0.0)/(getMaxHp()+0.0)))), (float)posY, rouge);
*/
        switch (lastDirection) {
            case WALKING_LEFT:
                canvas.drawBitmap(
                        MainActivity.createMirroredBitmap(
                                animations.get(0).get(currentCentiFrame / timeCentiBetweenFrame)
                        ),
                        (float) posX,
                        (float) posY,
                        null
                );
                break;
            case WALKING_RIGHT:
                canvas.drawBitmap(
                        animations.get(0).get(currentCentiFrame / timeCentiBetweenFrame),
                        (float) posX,
                        (float) posY,
                        null
                );
                break;
        }
        for(int i = 0; i < attraction.length; i++){
            attraction[i].draw(canvas);
        }
    }

    @Override
    public void destroy(){
        Game.getPartie().getMobs().remove(this);
    }

}
