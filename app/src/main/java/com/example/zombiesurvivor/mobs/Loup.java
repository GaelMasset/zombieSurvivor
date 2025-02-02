package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.Destroyable;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Zone;

public class Loup extends Mob{
    private int degatsAttaque;
    private double range;
    private double detectionRange = 400;
    private double preferedRange = 300;
    private int attackCd = 500;
    private int currentAttackCd;

    public Loup(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp, double speed, int degatsAttaque, int range) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame, hp, maxHp, speed);
        this.degatsAttaque = degatsAttaque;
        this.range = range;
    }

    @Override
    public String[] getAnimations() {
        return new String[]{"_idle", "_walking", "_attacking"
        };
    }





    @Override
    public void update() {
        super.update();
        updateAction();

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

                case ATTACKING:
                    timeCentiBetweenFrame = 50;
                    currentCentiFrame = currentCentiFrame % (animations.get(2).size() * timeCentiBetweenFrame);
                    break;
            }
        }


        if (currentAttackCd > 0) currentAttackCd--;
    }



    /*
    Methodes disponibles:
    posX, posY, tailleX, tailleY (tailles et coordonnées de Loup)
    partie.getJoueur().posX, ect ... (pareil pour le joueur)
    rangeXFrom(partie.getJoueur() distance du joueur (tjr positif)
    rangeYFrom
    isAtTheRightOf(partie.getJoueur())
    isAtTheLeftOf(partie.getJoueur())
    int currentAttackCd; Le cd avant de pouvoir attaquer
    action = Action.IDLE ou Action.ATTACKING ou Action.WALKING_RIGHT ou Action.WALKING_LEFT
     */
    private void updateAction() {
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
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
    }

    @Override
    public void destroy(){
        Game.getPartie().getMobs().remove(this);
    }

}
