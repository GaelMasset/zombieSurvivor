package com.example.zombiesurvivor;

import android.content.Context;

import com.example.zombiesurvivor.mobs.Action;

public class Weapon extends Item{
    private int firerate; // Tirs par minute
    private double precision;
    private Bullet munitionType;
    private double heatPerFire;
    private double cooldownRemaining = 0.0;

    public Weapon(double posX, double posY, int tailleX, int tailleY, String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,int postUseAnimTime ,double enfoncementTop, double enfoncementBottom, double enfoncementLeft, double enfoncementRight, double precision, int firerate, Bullet munitionType, double heatPerFire, int timeCentiToUse) {
        super(posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame,postUseAnimTime, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight, timeCentiToUse, false, 1);
        this.firerate = firerate;
        this.precision = precision;
        this.munitionType = munitionType;
        this.heatPerFire = heatPerFire;
    }

    private double applyPrecision(double angle) {
        if (precision < 100) {
            double maxDeviation = Math.toRadians((100 - precision) / 2.0); // Plus la précision est basse, plus la déviation est grande
            double randomDeviation = (Math.random() * 2 - 1) * maxDeviation; // Génère une déviation entre -maxDeviation et +maxDeviation
            angle += randomDeviation;
        }
        return angle;
    }


    private long lastFireTime = 0;

    private long getFireCooldown() {
        return 60000 / firerate;
    }

    public boolean fire() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFireTime >= getFireCooldown()) {

            Bullet newBullet;
            double angle;

            if (Game.getPartie().getJoueur().getLastDirection() == Action.WALKING_RIGHT) {
                angle = 0; // Angle pour aller à droite
                newBullet = new Bullet(munitionType, angle, this.munitionType.getRange());
                newBullet.setWeapon(this);
            } else {
                angle = Math.PI; // Angle pour aller à gauche (180° en radians)
                newBullet = new Bullet(munitionType, angle, this.munitionType.getRange());
                newBullet.setWeapon(this);
            }



            newBullet.setPosX(Game.getPartie().getJoueur().posX + (double) Game.getPartie().getJoueur().tailleX / 2);
            newBullet.setPosY(Game.getPartie().getJoueur().posY + (double) Game.getPartie().getJoueur().tailleY / 2);

            Game.getPartie().getBalles().add(newBullet);

            lastFireTime = currentTime;

            return true;
        }

        return false;
    }


    public void update(double deltaTime) {
        super.update();
        if (cooldownRemaining > 0.0) {
            cooldownRemaining -= deltaTime;
        }
    }



    @Override
    public boolean use() {
        super.use();
        return fire();
    }
}
