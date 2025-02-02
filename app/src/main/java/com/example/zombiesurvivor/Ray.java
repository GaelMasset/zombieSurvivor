package com.example.zombiesurvivor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Ray {
    private PointF start;    // Point de départ du rayon
    private PointF direction; // Direction du rayon
    private float length;     // Longueur (portée) du rayon
    private Paint paint;

    // Constructeur qui prend startX, startY, un angle en radians et la longueur
    public Ray(float startX, float startY, float angle, float length) {
        this.start = new PointF(startX, startY); // Point de départ
        this.direction = new PointF((float) Math.cos(angle), (float) Math.sin(angle)); // Direction basée sur l'angle
        this.length = length;
        this.paint = new Paint();
        paint.setColor(0xFFFFFFFF); // Rayon rouge
        paint.setStrokeWidth(5);
    }

    // Méthode pour définir les coordonnées de départ
    public void setStart(float startX, float startY) {
        start.set(startX, startY);
    }

    // Méthode pour dessiner le rayon sur le Canvas
    public void draw(Canvas canvas) {
        // Calculer la fin du rayon en utilisant la direction et la longueur
        float endX = start.x + direction.x * length;
        float endY = start.y + direction.y * length;
        // Dessiner une ligne entre le point de départ et le point de fin
        canvas.drawLine(start.x, start.y, endX, endY, paint);
    }

    // Vérifie si le rayon touche un objet Movable
    public boolean checkCollision(Movable movable) {
        // Définir les bords du rectangle de l'objet Movable
        float left = (float) movable.posX;
        float top = (float) movable.posY;
        float right = (float) (movable.posX + movable.tailleX);
        float bottom = (float) (movable.posY + movable.tailleY);

        // Vérifier les intersections avec chaque côté du rectangle

        // Côté gauche du rectangle (vertical)
        if (intersectsLine(left, top, left, bottom)) {
            return true;
        }

        // Côté droit du rectangle (vertical)
        if (intersectsLine(right, top, right, bottom)) {
            return true;
        }

        // Côté supérieur du rectangle (horizontal)
        if (intersectsLine(left, top, right, top)) {
            return true;
        }

        // Côté inférieur du rectangle (horizontal)
        if (intersectsLine(left, bottom, right, bottom)) {
            return true;
        }

        return false;
    }


    private boolean intersectsLine(float x1, float y1, float x2, float y2) {
        // Calculer la direction du segment
        float dx = x2 - x1;
        float dy = y2 - y1;

        // Calculer les vecteurs entre le point de départ du rayon et le point d'intersection
        float denom = direction.x * dy - direction.y * dx;

        float t = ((start.x - x1) * dy - (start.y - y1) * dx) / denom;
        float u = ((start.x - x1) * direction.y - (start.y - y1) * direction.x) / denom;

        // Vérifier si l'intersection est sur le segment et à une distance <= length
        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            float intersectionX = start.x + direction.x * t * length;
            float intersectionY = start.y + direction.y * t * length;
            float distance = (float) Math.sqrt((intersectionX - start.x) * (intersectionX - start.x) +
                    (intersectionY - start.y) * (intersectionY - start.y));
            return distance <= length;
        }

        return false;
    }


    // Modifier la couleur du rayon
    public void setColorPaint(int color) {
        this.paint.setColor(color);
    }
}
