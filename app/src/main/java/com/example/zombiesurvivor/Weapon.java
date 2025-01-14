package com.example.zombiesurvivor;

public class Weapon {
    private int firerate; // Tirs par minute
    private double precision;
    private Bullet munitionType;
    private double heatPerFire;
    private Game partie;

    // Temps restant avant le prochain tir (en secondes)
    private double cooldownRemaining = 0.0;

    public Weapon(double precision, int firerate, Bullet munitionType, double heatPerFire) {
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

    public boolean fire(Joystick joystick) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFireTime >= getFireCooldown()) {

            double angle = Math.atan2(joystick.getActuatorY(), joystick.getActuatorX());
            System.out.println(angle);
            angle = applyPrecision(angle);

            Bullet newBullet = new Bullet(munitionType, angle, this.munitionType.getRange());
            newBullet.setWeapon(this);

            newBullet.setPosX(partie.getJoueur().posX + (double) partie.getJoueur().tailleX / 2);
            newBullet.setPosY(partie.getJoueur().posY + (double) partie.getJoueur().tailleY / 2);

            partie.getBalles().add(newBullet);

            lastFireTime = currentTime;

            return true;
        }

        return false;
    }

    public void update(double deltaTime) {
        if (cooldownRemaining > 0.0) {
            cooldownRemaining -= deltaTime;
        }
    }

    public Game getGame(){
        return partie;
    }
    public void setGame(Game partie) {
        this.partie = partie;
    }
}
