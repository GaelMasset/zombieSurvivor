package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Bouton;
import com.example.zombiesurvivor.Bow;
import com.example.zombiesurvivor.Bullet;
import com.example.zombiesurvivor.Door;
import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.ImageHealthBar;
import com.example.zombiesurvivor.ImageHotbar;
import com.example.zombiesurvivor.ImageManaBar;
import com.example.zombiesurvivor.Joystick;
import com.example.zombiesurvivor.LecteurFichier;
import com.example.zombiesurvivor.Map;
import com.example.zombiesurvivor.Obstacle;
import com.example.zombiesurvivor.PixelText;
import com.example.zombiesurvivor.Player.Player;
import com.example.zombiesurvivor.PotionSoin;
import com.example.zombiesurvivor.R;
import com.example.zombiesurvivor.Weapon;
import com.example.zombiesurvivor.WeaponBaton;

import java.util.ArrayList;

public class FenetreTest extends Fenetre {
    Game partie;
    Floor sol;
    Obstacle o;
    Joystick joystickDeplacement;
    Bouton boutonAttaque;
    ImageHealthBar healthBar;
    ImageManaBar manaBar;
    ImageHotbar[] hotbar = new ImageHotbar[4];
    PixelText texteChalut;
    public FenetreTest(Page p) {
        super(p);
        ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        ArrayList<Door> doors = new ArrayList<Door>();
        Map c = new Map(obstacles, floors, doors);

        joystickDeplacement = new Joystick(p.getContext(), pourcentLongueur(15) ,canvasHeight-pourcentHauteur(26), pourcentLongueur(3), pourcentLongueur(8), "joystick_bottom_1", "joystick_top_1");
        boutonAttaque = new Bouton(p.getContext(), canvasWidth-pourcentLongueur(15) ,canvasHeight-pourcentHauteur(26), pourcentLongueur(8), pourcentLongueur(8), "joystick_bottom_", false, 100, 0, 0, 0, 0);

        Bow arme = new Bow(page.getContext(), 0, 0, 80, 96, "item_bow", false, 100,0, 0, 0, 0,95, 120,
                new Bullet(p.getContext(),0, 0, 36, 20, "pistol_bullet", false, 100,0,0,0,0, 5, 10, 0, 2000),1, 100);

        PotionSoin pot = new PotionSoin(page.getContext(), 0, 0, 80, 96, "item_potion", false, 100, 0, 0, 0, 0, 250, 10);

        WeaponBaton armeMelee = new WeaponBaton(page.getContext(), 0, 0, 80, 96, "item_sword1", false, 100, 0, 0, 0, 0, 100, 50, 150);
        partie = new Game(page.getContext(), new Player(page.getContext(), 3000, 3000, 80, 96,
                    "character",100, 79,0,0,0, true, 100, 50,100, 5,
                    joystickDeplacement, boutonAttaque, partie), c);
        partie.getJoueur().getHotbar().add(pot,1);
        partie.getJoueur().getHotbar().add(arme,1);
        partie.getJoueur().getHotbar().add(armeMelee, 1);


        LecteurFichier.chargerPartie(page.getContext(), partie, "niveau1.txt");
        arme.setGame(partie);
        pot.setGame(partie);
        armeMelee.setGame(partie);
        for(Obstacle o: obstacles){
            o.setPartie(partie);
        }

        healthBar = new ImageHealthBar(page.getContext(), pourcentLongueur(2.33), pourcentHauteur(2.66), (int) pourcentLongueur(22.33), (int) pourcentHauteur(17.33), "healthbar", 100, partie.getJoueur());
        manaBar = new ImageManaBar(page.getContext(), pourcentLongueur(1.33), (double) pourcentHauteur(15.66), (int) pourcentLongueur(22.33), (int) pourcentHauteur(17.33), "manabar", 100, partie.getJoueur());
        texteChalut = new PixelText(page.getContext(), "chalut a tous", MainActivity.pourcentLongueur(10), MainActivity.pourcentHauteur(20), 20, 2);

        hotbar[0] = new ImageHotbar(page.getContext(),pourcentLongueur(34.33) , pourcentHauteur(80.66), (int) pourcentLongueur(7.33),(int) pourcentHauteur(14.66),"hotbar", 100);
        hotbar[1] = new ImageHotbar(page.getContext(),pourcentLongueur(42.33) , pourcentHauteur(80.66), (int) pourcentLongueur(7.33),(int) pourcentHauteur(14.66),"hotbar", 100);
        hotbar[2] = new ImageHotbar(page.getContext(),pourcentLongueur(50.33) , pourcentHauteur(80.66), (int) pourcentLongueur(7.33),(int) pourcentHauteur(14.66),"hotbar", 100);
        hotbar[3] = new ImageHotbar(page.getContext(),pourcentLongueur(58.33) , pourcentHauteur(80.66), (int) pourcentLongueur(7.33),(int) pourcentHauteur(14.66),"hotbar", 100);

        for(ImageHotbar img: hotbar){
            img.setHotbar(hotbar);
        }
    }

    @Override
    public void update() {
        partie.update();
        joystickDeplacement.update();
        boutonAttaque.update();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(facteurZoom, facteurZoom);
        float translateX = (screenWidth * (1 - facteurZoom)) / (2 * facteurZoom);
        float translateY = (screenHeight * (1 - facteurZoom)) / (2 * facteurZoom);
        canvas.translate(translateX, translateY);
        partie.draw(canvas);
        canvas.restore();

        joystickDeplacement.draw(canvas);
        boutonAttaque.draw(canvas);

        //texteChalut.draw(canvas);
        healthBar.draw(canvas);
        manaBar.draw(canvas);
        for(int i = 0; i < hotbar.length; i++) {
            hotbar[i].draw(canvas);
            if (partie.getJoueur().getHotbar().get(i) != null) {
                partie.getJoueur().getHotbar().get(i).getItem().drawIcone(canvas, hotbar[i].getPosX(), hotbar[i].getPosY(), hotbar[i].getTailleX(), hotbar[i].getTailleY());
            }
        }
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
                    if(joystickDeplacement.isPressed(xClicked, yClicked)) {
                        joystickDeplacement.setIsPressed(true);
                        joystickDeplacement.setActuator(xClicked, yClicked);
                    }
                    if(boutonAttaque.isPressed(xClicked, yClicked)){
                        boutonAttaque.setIsPressed(true);
                        partie.getJoueur().useItem();
                    }
                    for(int barre = 0; barre < hotbar.length; barre++){
                        if(hotbar[barre].isTouched(xClicked, yClicked)){
                            hotbar[barre].select();
                            partie.getJoueur().getHotbar().setSelectedSlot(barre);
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if(xClicked/canvasWidth < 0.5 && joystickDeplacement.getIsPressed()) {
                        joystickDeplacement.setActuator(xClicked, yClicked);
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
                        boutonAttaque.setIsPressed(false);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
