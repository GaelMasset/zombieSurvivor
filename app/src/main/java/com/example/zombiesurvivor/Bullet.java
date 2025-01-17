package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;

public class Bullet extends Movable {
    private double vitesse;  // La vitesse de la balle
    private int damage;      // Les dégâts de la balle
    private Weapon weapon; // Le joueur qui a tiré la balle
    private double angle;    // L'angle de tir en radians
    private double range;

    // Constructeur
    public Bullet(Context context, double posX, double posY, int tailleX, int tailleY,
                  String cheminImages, boolean isAnimating, int timeCentiBetweenFrame,
                  double enfoncementTop, double enfoncementBottom,
                  double enfoncementLeft, double enfoncementRight,
                  double vitesse, int damage, double angle, double range) {
        super(context, posX, posY, tailleX, tailleY, cheminImages, isAnimating, timeCentiBetweenFrame, enfoncementTop, enfoncementBottom, enfoncementLeft, enfoncementRight);
        this.vitesse = vitesse;
        this.damage = damage;
        this.angle = angle; // Angle de tir en radians
        this.range = range;
    }
    public Bullet(Bullet b, double angle, double range) {
        super(b.context, b.posX, b.posY, b.tailleX, b.tailleY, b.cheminImages, b.isAnimating, b.timeCentiBetweenFrame, b.enfoncementTop, b.enfoncementBottom, b.enfoncementLeft, b.enfoncementRight);

        this.vitesse = b.vitesse;
        this.damage = b.damage;
        this.angle = angle;
        this.range = range;
    }

    // Mettre à jour la position de la balle
    @Override
    public void update() {
        double dx = vitesse * Math.cos(angle);  // Composant horizontal de la vitesse
        double dy = vitesse * Math.sin(angle);  // Composant vertical de la vitesse

        setPosX(getPosX() + dx);
        setPosY(getPosY() + dy);
        Movable m = Movable.isOneTouching(this, weapon.getGame().getCarte().getObstacles());

        if(m!=null){
            hit(m);
        }

        // Fix: Use absolute values when calculating range reduction to handle negative velocities
        range -= Math.sqrt(dx*dx + dy*dy);
        if(range<=0) weapon.getGame().getBalles().remove(this);
    }

    // Dessiner la balle sur le Canvas (si nécessaire)
    @Override
    public void draw(Canvas canvas) {
        // Save the current canvas state
        canvas.save();

        // Translate to the bullet's position
        canvas.translate((float) posX, (float) posY);

        // Rotate the canvas by the bullet's angle + 90 degrees (convert to degrees)
        canvas.rotate((float) Math.toDegrees(angle) + 0);

        // Translate back so rotation happens around bullet center
        canvas.translate((float) -posX, (float) -posY);

        // Draw the bullet using parent draw method
        super.draw(canvas);

        // Restore the canvas to its original state
        canvas.restore();
    }


    // Gestion des collisions (si la balle touche un objet Destroyable)
    public void hit(Movable m) {
        if (m instanceof Destroyable) {
            System.out.println(((Destroyable) m).getHp());
            ((Destroyable) m).damage(this.damage);
            System.out.println(((Destroyable) m).getHp());
            weapon.getGame().getBalles().remove(this);
        }
    }

    // Getters et setters pour l'angle et les autres propriétés
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setWeapon(Weapon w){
        this.weapon = w;
    }

    public double getRange() {
        return this.range;
    }
}
