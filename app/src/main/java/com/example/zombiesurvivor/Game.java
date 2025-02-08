package com.example.zombiesurvivor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;

import com.example.zombiesurvivor.carte.Map;
import com.example.zombiesurvivor.mobs.Loup;
import com.example.zombiesurvivor.mobs.Mob;
import com.example.zombiesurvivor.mobs.Player;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    @SuppressLint("StaticFieldLeak")
    private static Game partie;
    public static void setContext(Context c){
        partie.context = c;
    }

    public static Game getPartie(){
        if(partie == null){
            partie = new Game(null, null, null);
        }
        return partie;
    }

    private Player joueur;
    private Image fond;
    private Map carte;
    private Camera camera;
    private ArrayList<Mob> mobs = new ArrayList<Mob>();
    private ArrayList<Bullet> balles = new ArrayList<Bullet>();
    private Context context;

    private int randomSpawnPoint;
    private double xPlayerSpawn;
    private double yPlayerSpawn;

    public Game(Context context, Player joueur, Map carte) {
        this.camera = new CameraPlayer();
        this.context = context;

        Random r = new Random();
        randomSpawnPoint = r.nextInt(1000);
    }

    public void draw(Canvas canvas){
        canvas.translate(-camera.getPosX(), camera.getPosY());
        this.carte.draw(canvas);

        for(Bullet balle: balles){
            balle.draw(canvas);
        }
        for(Mob mobs: mobs){
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
        for(Mob mobs: mobs){
            mobs.update();
        }
    }

    public void addMonster(){
        this.mobs.add(new Loup(context, 2500, 3000, 63, 66, "monster_wolf", 0, 0, 0, 0, true, 50, 50, 50, 0.2, 30, 100));
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
        return mobs;
    }

    public double getxPlayerSpawn() {
        return carte.getFloors().get(randomSpawnPoint%carte.getFloors().size()).posX;
    }

    public double getyPlayerSpawn() {
        return carte.getFloors().get(randomSpawnPoint%carte.getFloors().size()).posY;
    }

    public Image getFond() {
        return fond;
    }

    public void setFond(Image fond) {
        this.fond = fond;
    }

    public void setJoueur(Player joueur) {
        this.joueur = joueur;
    }

    public void setCarte(Map carte) {
        this.carte = carte;
        this.carte.setContext(context);
    }
}
