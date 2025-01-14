package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Bullet;
import com.example.zombiesurvivor.Door;
import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Joystick;
import com.example.zombiesurvivor.LecteurFichier;
import com.example.zombiesurvivor.Map;
import com.example.zombiesurvivor.Obstacle;
import com.example.zombiesurvivor.Player;
import com.example.zombiesurvivor.Weapon;

import java.util.ArrayList;

public class FenetreTest extends Fenetre {
    Game partie;
    Floor sol;
    Obstacle o;
    Joystick joystickDeplacement;
    Joystick joystickAttaque;

    public FenetreTest(Page p) {
        super(p);
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        ArrayList<Door> doors = new ArrayList<Door>();
        Map c = new Map(obstacles, floors, doors);

        joystickDeplacement = new Joystick(p.getContext(), pourcentLongueur(15) ,canvasHeight-pourcentHauteur(26), pourcentLongueur(3), pourcentLongueur(8), "joystick_bottom_1", "joystick_top_1");
        joystickAttaque = new Joystick(p.getContext(), canvasWidth-pourcentLongueur(15) ,canvasHeight-pourcentHauteur(26), pourcentLongueur(3), pourcentLongueur(8), "joystick_bottom_1", "joystick_top_1");

        Weapon arme = new Weapon(60, 1800,
                new Bullet(p.getContext(),0, 0, 20, 20, "floor", false, 100,0,0,0,0, 5, 10, 0, 2000),1);

        partie = new Game(page.getContext(), new Player(page.getContext(), 3000, 3000, 50, 100,
                    "character_pos_0_standby",79,0,0,0, false, 100, 100, 5,
                    joystickDeplacement, joystickAttaque, partie, arme), c);
        LecteurFichier.chargerPartie(page.getContext(), partie, "niveau1.txt");
        arme.setGame(partie);
        for(Obstacle o: obstacles){
            o.setPartie(partie);
        }


    }

    @Override
    public void update() {
        partie.update();
        joystickDeplacement.update();
        joystickAttaque.update();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        partie.draw(canvas);

        canvas.restore();

        joystickDeplacement.draw(canvas);
        joystickAttaque.draw(canvas);

    }


    @Override
    public boolean onClick(MotionEvent event) {
        int motionaction = event.getActionMasked();
        int pointerCount = event.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            // Get coordinates for each pointer
            double xClicked = event.getX(i) - offsetX;
            double yClicked = event.getY(i) - offsetY;

            switch (motionaction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    if(xClicked/canvasWidth < 0.5) {
                        joystickDeplacement.setIsPressed(true);
                        joystickDeplacement.setActuator(xClicked, yClicked);
                    }
                    if(xClicked/canvasWidth > 0.5){
                        joystickAttaque.setIsPressed(true);
                        joystickAttaque.setActuator(xClicked, yClicked);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if(xClicked/canvasWidth < 0.5 && joystickDeplacement.getIsPressed()) {
                        joystickDeplacement.setActuator(xClicked, yClicked);
                    }
                    if(xClicked/canvasWidth > 0.5 && joystickAttaque.getIsPressed()){
                        joystickAttaque.setActuator(xClicked, yClicked);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {
                    // Get the index of the pointer that left the screen
                    int pointerIndex = event.getActionIndex();
                    double xUp = event.getX(pointerIndex) - offsetX;

                    if(xUp/canvasWidth < 0.5) {
                        joystickDeplacement.setIsPressed(false);
                    }
                    if(xUp/canvasWidth > 0.5){
                        joystickAttaque.setIsPressed(false);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
