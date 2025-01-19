package com.example.zombiesurvivor;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Map {
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Floor> sols;
    private ArrayList<Door> portes;

    public Map(ArrayList<Obstacle> obstacles, ArrayList<Floor> floors, ArrayList<Door> doors){
        this.obstacles = obstacles;
        this.sols = floors;
        this.portes = doors;
    }

    public void draw(Canvas canvas){
        for(Floor floor: sols){
            floor.draw(canvas);
        }
        for(Obstacle obstacle: obstacles){
            obstacle.draw(canvas);
        }
        for(Door door: portes){
            door.draw(canvas);
        }
    }
    public void update(){
        for(Floor floor: sols){
            floor.update();
        }
        for(Obstacle obstacle: obstacles){
            obstacle.update();
        }
        for(Door door: portes){
            door.update();
        }
    }

    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }
    public ArrayList<Floor> getFloors() {
        return this.sols;
    }
    public ArrayList<Door> getDoors(){
        return this.portes;
    }



}
