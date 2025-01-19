package com.example.zombiesurvivor.mobs;

import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.Destroyable;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Movable;
import com.example.zombiesurvivor.Zone;

public class Loup extends Mob{
    private int degatsAttaque;
    private double range;
    private double detectionRange = 400;
    private int attackCd = 500;
    private int currentAttackCd;
    private Canvas canvas;

    public Loup(Context context, double posX, double posY, int tailleX, int tailleY, String cheminImages, double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, boolean isAnimating, int timeCentiBetweenFrame, int hp, int maxHp, double speed, Game partie, int degatsAttaque, int range) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, isAnimating, timeCentiBetweenFrame, hp, maxHp, speed, partie);
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
            Zone hitbox = new Zone(partie.getContext(), posX + tailleX, posY, (int) range, tailleY);
            hitbox.draw(canvas);

            if (Movable.areTouching(hitbox, partie.getJoueur())) {
                System.out.println("aie..");
                partie.getJoueur().damage(degatsAttaque);
            }
        }
        // Hitbox à gauche
        else {
            Zone hitbox = new Zone(partie.getContext(), posX - range, posY, (int) range, tailleY);
            if (Movable.areTouching(hitbox, partie.getJoueur())) {
                System.out.println("aie..");
                partie.getJoueur().damage(degatsAttaque);
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
                    posX += 2;
                    timeCentiBetweenFrame = 20;
                    currentCentiFrame = currentCentiFrame % (animations.get(1).size() * timeCentiBetweenFrame);
                    break;

                case WALKING_LEFT:
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
                    double playerY = partie.getJoueur().getPosY();

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
        double distanceX = rangeXFrom(partie.getJoueur());
        double distanceY = rangeYFrom(partie.getJoueur());
        if(action == Action.ATTACKING) return;
        // Vérifier si le joueur est hors de la zone de détection
        if (distanceX > detectionRange-50 || distanceY > detectionRange-50) {
            action = Action.IDLE; // Rester en mode inactif
            return;
        }

        // Si le joueur est dans la zone de détection
        if (currentAttackCd > 0) { // Si le loup est en cooldown
            if (isAtTheRightOf(partie.getJoueur())) {
                action = Action.WALKING_RIGHT; // S'éloigner vers la droite
                lastDirection = Action.WALKING_RIGHT;
            } else if (!isAtTheRightOf(partie.getJoueur())) {
                action = Action.WALKING_LEFT; // S'éloigner vers la gauche
                lastDirection = Action.WALKING_LEFT;
            }
        } else { // Si le loup n'est pas en cooldown
            if (distanceX <= range) { // Si le joueur est dans la portée d'attaque
                if(action != Action.ATTACKING) currentCentiFrame = 0;
                action = Action.ATTACKING; // Attaquer
            } else { // Si le joueur est détecté mais pas encore dans la portée
                if (isAtTheRightOf(partie.getJoueur())) {
                    lastDirection = Action.WALKING_LEFT;
                    action = Action.WALKING_LEFT; // Se rapprocher vers la gauche
                } else if (!isAtTheRightOf(partie.getJoueur())) {
                    action = Action.WALKING_RIGHT; // Se rapprocher vers la droite
                    lastDirection = Action.WALKING_RIGHT;
                }
            }
        }
    }



    @Override
    public void draw(Canvas canvas) {
        this.canvas = canvas;
        // Dessiner le joueur en fonction de son action et sa direction
        switch (action) {
            case WALKING_RIGHT:
            case WALKING_LEFT:
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
        this.partie.getMobs().remove(this);
    }

}
