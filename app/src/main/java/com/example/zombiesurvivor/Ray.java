package com.example.zombiesurvivor;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collections;

public class Ray {
    private PointF start;    // Point de départ du rayon
    private PointF direction; // Direction du rayon
    private float length;     // Longueur (portée) du rayon
    private float maxlength;
    private Paint paint;
    private ArrayList<Movable> encounteredMovable = new ArrayList<>();

    // Constructeur
    public Ray(float startX, float startY, float angle, float length) {
        this.start = new PointF(startX, startY);
        this.direction = new PointF((float) Math.cos(angle), (float) Math.sin(angle));
        this.length = length;
        this.maxlength = length;
        this.paint = new Paint();
        paint.setColor(0xFFFFFFFF); // Rayon blanc par défaut
        paint.setStrokeWidth(2);
    }

    // Définir les coordonnées de départ
    public void setStart(float startX, float startY) {
        start.set(startX, startY);
    }

    // Dessiner le rayon
    public void draw(Canvas canvas) {
        float endX = start.x + direction.x * length;
        float endY = start.y + direction.y * length;
        canvas.drawLine(start.x, start.y, endX, endY, paint);
    }

    // Vérifie si le rayon touche un objet Movable et met à jour la longueur du rayon
    public float checkCollision(Movable movable) {
        float left = (float) movable.posX;
        float top = (float) movable.posY;
        float right = (float) (movable.posX + movable.tailleX);
        float bottom = (float) (movable.posY + movable.tailleY);

        // Calculer les intersections avec les quatre côtés du rectangle de l'objet
        ArrayList<Float> distances = new ArrayList<>();
        float intersection1 = intersectsLine(left, top, left, bottom);
        float intersection2 = intersectsLine(right, top, right, bottom);
        float intersection3 = intersectsLine(left, top, right, top);
        float intersection4 = intersectsLine(left, bottom, right, bottom);

        if (intersection1 != -1) distances.add(intersection1);
        if (intersection2 != -1) distances.add(intersection2);
        if (intersection3 != -1) distances.add(intersection3);
        if (intersection4 != -1) distances.add(intersection4);

        // Si le rayon touche un des bords, ajuster la longueur du rayon
        if (!distances.isEmpty()) {
            this.length = Collections.min(distances);
            return this.length; // Retourner la distance de l'intersection
        } else {
            this.length = maxlength; // Pas d'intersection, rayon reste à sa longueur maximale
            return maxlength;
        }
    }

    // Méthode pour calculer l'intersection du rayon avec un segment de ligne
    private float intersectsLine(float x1, float y1, float x2, float y2) {
        float denom = direction.x * (y2 - y1) - direction.y * (x2 - x1);
        if (denom == 0) {
            return -1; // Les lignes sont parallèles, pas d'intersection
        }

        float t1 = ((x1 - start.x) * (y2 - y1) - (y1 - start.y) * (x2 - x1)) / denom;
        float t2 = ((x1 - start.x) * direction.y - (y1 - start.y) * direction.x) / denom;

        // Vérifier si l'intersection se trouve sur le segment
        if (t1 >= 0 && t1 <= length && t2 >= 0 && t2 <= 1) {
            return t1; // Retourner la distance de l'intersection sur le rayon
        }

        return -1; // Pas d'intersection
    }

    // Modifier la couleur du rayon
    public void setColorPaint(int color) {
        this.paint.setColor(color);
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getMaxLength() {
        return maxlength;
    }

    public void addEncounteredMovable(Movable movable) {
        this.encounteredMovable.add(movable);
    }
    public void emptyEncounteredMovable(){
        encounteredMovable.clear();
    }
    public ArrayList<Movable> getEncounteredMovable(){
        return encounteredMovable;
    }
}
