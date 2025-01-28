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
    private Canvas canvas;
    Zone hitbox;

    public Loup(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp, double speed, int degatsAttaque, int range) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame, hp, maxHp, speed);
        this.degatsAttaque = degatsAttaque;
        this.range = range;
    }

    @Override
    public String[] getAnimations() {
        return new String[]{"_idle","_walking","_attacking"};
    }

    @Override
    public boolean move() {
        return false;
    }

    public boolean canAttack(){
        if(currentCentiFrame >= animations.get(2).size()*timeCentiBetweenFrame-1 && currentAttackCd <= 0 && action == Action.ATTACKING){
            return true;
        }
        return false;
    }
    public void attack() {
        System.out.println("attaque du renard");

        // Hitbox à droite
        if (lastDirection == Action.WALKING_RIGHT) {
            hitbox = new Zone(Game.getPartie().getContext(), posX + tailleX, posY, (int) range, tailleY);
            hitbox.draw(canvas);

            if (Movable.areTouching(hitbox, Game.getPartie().getJoueur(), false)) {
                System.out.println("aie..");
                Game.getPartie().getJoueur().damage(degatsAttaque);
            }
        }
        // Hitbox à gauche
        else {
            hitbox = new Zone(Game.getPartie().getContext(), posX - range, posY, (int) range, tailleY);
            if (Movable.areTouching(hitbox, Game.getPartie().getJoueur(), false)) {
                System.out.println("aie..");
                Game.getPartie().getJoueur().damage(degatsAttaque);
            }
        }

        // Remettre en mode IDLE et reset cooldown
        action = Action.IDLE;
        currentAttackCd = attackCd;
    }


    @Override
    public void update() {
        updateAction();

        if (canAttack()) attack();

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
            switch (action){
                case WALKING_LEFT:
                case WALKING_RIGHT:
                    double playerY = Game.getPartie().getJoueur().getPosY();

                    if (posY < playerY - 10) {
                        posY += 1;
                    } else if (posY > playerY + 10) {
                        posY -= 1;
                    }
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
        // Calculer la distance entre le loup et le joueur
        double distanceX = rangeXFrom(Game.getPartie().getJoueur());
        double distanceY = rangeYFrom(Game.getPartie().getJoueur());
        if(action == Action.ATTACKING) return;
        // Vérifier si le joueur est hors de la zone de détection
        if (distanceX > detectionRange-50 || distanceY > detectionRange-50) {
            action = Action.IDLE; // Rester en mode inactif
            return;
        }

        // Si le joueur est dans la zone de détection
        if (currentAttackCd > 0) { // Si le loup est en cooldown
            if (isAtTheRightOf(Game.getPartie().getJoueur())) {
                if(distanceX < preferedRange) {
                    action = Action.WALKING_RIGHT_BACKWARD; // S'éloigner vers la droite
                    lastDirection = Action.WALKING_LEFT;
                }
                else{
                    action = Action.WALKING_LEFT; // S'éloigner vers la droite
                    lastDirection = Action.WALKING_RIGHT;
                }
            } else if (!isAtTheRightOf(Game.getPartie().getJoueur())) {
                if(distanceX < preferedRange) {
                    action = Action.WALKING_LEFT_BACKWARD; // S'éloigner vers la gauche
                    lastDirection = Action.WALKING_RIGHT;
                } else if(distanceX > preferedRange + 10) {
                    action = Action.WALKING_RIGHT; // S'éloigner vers la gauche
                    lastDirection = Action.WALKING_RIGHT;
                } else{
                    action = Action.IDLE;
                }
            }
        } else { // Si le loup n'est pas en cooldown
            if (distanceX <= range-Game.getPartie().getJoueur().getTailleX()/2) { // Si le joueur est dans la portée d'attaque
                if(action != Action.ATTACKING) currentCentiFrame = 0;
                action = Action.ATTACKING;
            } else { // Si le joueur est détecté mais pas encore dans la portée
                if (isAtTheRightOf(Game.getPartie().getJoueur())) {
                    lastDirection = Action.WALKING_LEFT;
                    action = Action.WALKING_LEFT; // Se rapprocher vers la gauche
                } else if (!isAtTheRightOf(Game.getPartie().getJoueur())) {
                    action = Action.WALKING_RIGHT; // Se rapprocher vers la droite
                    lastDirection = Action.WALKING_RIGHT;
                }
            }
        }
    }



    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        //if(hitbox!=null) hitbox.draw(canvas);
        Paint rouge = new Paint();
        rouge.setColor(Color.RED);
        Paint blanc = new Paint();
        blanc.setColor(Color.WHITE);
        canvas.drawRect((float)posX, (float) ((float)posY-MainActivity.pourcentHauteur(1.5)), (float)posX+tailleX, (float)posY, blanc);
        canvas.drawRect((float)posX, (float) ((float)posY-MainActivity.pourcentHauteur(1.5)), (float) ((float)posX+(tailleX*((getHp()+0.0)/(getMaxHp()+0.0)))), (float)posY, rouge);

        // Dessiner le joueur en fonction de son action et sa direction
        switch (action) {
            case WALKING_RIGHT:
            case WALKING_LEFT:
            case WALKING_LEFT_BACKWARD:
            case WALKING_RIGHT_BACKWARD:
                switch (lastDirection) {
                    case WALKING_RIGHT:
                        canvas.drawBitmap(
                                animations.get(1).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                    case WALKING_LEFT:
                        canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        animations.get(1).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                }
                break;
            case IDLE:
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
                break;
            case ATTACKING:
                switch (lastDirection) {
                    case WALKING_LEFT:
                        canvas.drawBitmap(
                                MainActivity.createMirroredBitmap(
                                        animations.get(2).get(currentCentiFrame / timeCentiBetweenFrame)
                                ),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                    case WALKING_RIGHT:
                        canvas.drawBitmap(
                                animations.get(2).get(currentCentiFrame / timeCentiBetweenFrame),
                                (float) posX,
                                (float) posY,
                                null
                        );
                        break;
                }
                break;

        }
    }

    @Override
    public void destroy(){
        Game.getPartie().getMobs().remove(this);
    }

}
