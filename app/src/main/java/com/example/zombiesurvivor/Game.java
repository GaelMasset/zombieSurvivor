package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.carte.Map;
import com.example.zombiesurvivor.mobs.Loup;
import com.example.zombiesurvivor.mobs.Mob;
import com.example.zombiesurvivor.mobs.Player;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Player joueur;
    private Image fond;
    private Map carte;
    private Camera camera;
    private ArrayList<Mob> zombies = new ArrayList<Mob>();
    private ArrayList<Bullet> balles = new ArrayList<Bullet>();
    private Context context;
    private double xPlayerSpawn;
    private double yPlayerSpawn;

    public Game(Context context, Player joueur, Map carte) {
        this.joueur = joueur;
        this.carte = carte;
        this.carte.setContext(context);
        this.camera = new CameraPlayer(this);
        this.context = context;
        joueur.setGame(this);

        fond = new Image(context, 0, 0, MainActivity.canvasWidth, MainActivity.canvasHeight,"example_background" , 80);
        Random r = new Random();
        int solPif = r.nextInt(carte.getFloors().size());
        xPlayerSpawn = carte.getFloors().get(solPif).posX+ (double) carte.getFloors().get(solPif).tailleX /2;
        yPlayerSpawn = carte.getFloors().get(solPif).posY+ (double) carte.getFloors().get(solPif).tailleY /2;
    }

    public void draw(Canvas canvas){
        canvas.translate(-camera.getPosX(), camera.getPosY());
        this.carte.draw(canvas);
        this.joueur.draw(canvas);

        for(Bullet balle: balles){
            balle.draw(canvas);
        }
        for(Mob mobs: zombies){
            mobs.draw(canvas);
        }
    }

    public void update(){
        this.joueur.update();
        this.carte.update();
        this.camera.update();
        for(int i = 0; i < balles.size(); i++){
            balles.get(i).update();
        }
        for(Mob mobs: zombies){
            mobs.update();
        }
    }

    public void addMonster(){
        this.zombies.add(new Loup(context, 2500, 3000, 63, 66, "monster_wolf", 0, 0, 0, 0, true, 50, 50, 50, 0.2, this, 30, 100));
    }

    public Player getJoueur(){
        return this.joueur;
    }

    public Map getCarte() {
        return this.carte;
    }
    public ArrayList<Bullet> getBalles() {
        return this.balles;
    }
    public Context getContext(){
        return this.context;
    }
    public ArrayList<? extends Movable> getMobs() {
        return zombies;
    }

    public double getxPlayerSpawn() {
        return xPlayerSpawn;
    }

    public double getyPlayerSpawn() {
        return yPlayerSpawn;
    }

    public Image getFond() {
        return fond;
    }
}
