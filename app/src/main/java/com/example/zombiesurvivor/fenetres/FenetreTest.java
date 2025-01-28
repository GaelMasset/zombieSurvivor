package com.example.zombiesurvivor.fenetres;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Base.MainActivity;
import com.example.zombiesurvivor.Base.Page;
import com.example.zombiesurvivor.Bouton;
import com.example.zombiesurvivor.Bow;
import com.example.zombiesurvivor.Bullet;
import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Game;
import com.example.zombiesurvivor.Image;
import com.example.zombiesurvivor.ImageHealthBar;
import com.example.zombiesurvivor.ImageHotbar;
import com.example.zombiesurvivor.ImageManaBar;
import com.example.zombiesurvivor.Joystick;
import com.example.zombiesurvivor.Obstacle;
import com.example.zombiesurvivor.PixelText;
import com.example.zombiesurvivor.carte.GenerateurNiveau;
import com.example.zombiesurvivor.carte.Map;
import com.example.zombiesurvivor.mobs.Player;
import com.example.zombiesurvivor.PotionSoin;
import com.example.zombiesurvivor.WeaponBaton;

public class FenetreTest extends Fenetre {
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

        joystickDeplacement = new Joystick(p.getContext(), pourcentLongueur(15) ,canvasHeight-pourcentHauteur(26), pourcentLongueur(6), pourcentLongueur(4), "joystick_bottom_1", "joystick_top_1");
        boutonAttaque = new Bouton(p.getContext(), canvasWidth-pourcentLongueur(15) ,canvasHeight-pourcentHauteur(26), pourcentLongueur(8), pourcentLongueur(8), "joystick_bottom_","joystick_bottom_", false, 100);


        //Initailise game
        Game.getPartie();

        Map c = GenerateurNiveau.genererCarte(page.getContext());
        Game.getPartie().setCarte(c);
        Game.getPartie().setJoueur(new Player(page.getContext(), 3000, 3000, 80, 96,
                "character",100, 79,0,0,0, true, 100, 50,100, 5,
                joystickDeplacement, boutonAttaque));
        Game.setContext(p.getContext());
        Game.getPartie().setFond(new Image(Game.getPartie().getContext(), 0, 0, MainActivity.canvasWidth, MainActivity.canvasHeight,"example_background" , 80));



        Bow arme = new Bow(page.getContext(), 0, 0, 80, 96, "item_bow", false, 100,0,0, 0, 0, 0,95, 120,
                new Bullet(p.getContext(),0, 0, 36, 20, "pistol_bullet", false, 100,0,0,0,0, 5, 10, 0, 2000),1, 100);

        PotionSoin pot = new PotionSoin(page.getContext(), 0, 0, 80, 96, "item_potion", false, 100,0 , 0, 0, 0, 0, 250, 100);

        WeaponBaton armeMelee = new WeaponBaton(page.getContext(), 0, 0, 80, 96, "item_sword1", false, 20,60, 0, 0, 0, 0, 40, 10, 150);


        Game.getPartie().getJoueur().getHotbar().add(pot,1);
        Game.getPartie().getJoueur().getHotbar().add(arme,1);
        Game.getPartie().getJoueur().getHotbar().add(armeMelee, 1);

        Game.getPartie().getJoueur().setSpawnPoint(Game.getPartie().getxPlayerSpawn(), Game.getPartie().getyPlayerSpawn());
        //partie.getJoueur().setSpawnPoint(2800, 2800);
        Game.getPartie().addMonster();


        healthBar = new ImageHealthBar(page.getContext(), pourcentLongueur(2.33), pourcentHauteur(2.66), (int) pourcentLongueur(22.33), (int) pourcentHauteur(17.33), "healthbar", 100);
        manaBar = new ImageManaBar(page.getContext(), pourcentLongueur(1.33), (double) pourcentHauteur(15.66), (int) pourcentLongueur(22.33), (int) pourcentHauteur(17.33), "manabar", 100);

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
        Game.getPartie().update();
        joystickDeplacement.update();
        boutonAttaque.update();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Game.getPartie().getFond().draw(canvas);

        canvas.scale(facteurZoom, facteurZoom);
        float translateX = (screenWidth * (1 - facteurZoom)) / (2 * facteurZoom);
        float translateY = (screenHeight * (1 - facteurZoom)) / (2 * facteurZoom);
        canvas.translate(translateX, translateY);
        Game.getPartie().draw(canvas);
        canvas.restore();
        Game.getPartie().getCarte().drawMinMap(canvas, (int) pourcentLongueur(259.0/3), (int) pourcentHauteur(8.0/1.5), (int) pourcentLongueur(289.0/3), (int) pourcentHauteur(38.0/1.5));

        joystickDeplacement.draw(canvas);
        boutonAttaque.draw(canvas);

        //texteChalut.draw(canvas);
        healthBar.draw(canvas);
        manaBar.draw(canvas);
        for(int i = 0; i < hotbar.length; i++) {
            hotbar[i].draw(canvas);
            if (Game.getPartie().getJoueur().getHotbar().get(i) != null) {
                Game.getPartie().getJoueur().getHotbar().get(i).getItem().drawIcone(canvas, hotbar[i].getPosX(), hotbar[i].getPosY(), hotbar[i].getTailleX(), hotbar[i].getTailleY());
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
                        Game.getPartie().getJoueur().useItem();
                    }
                    for(int barre = 0; barre < hotbar.length; barre++){
                        if(hotbar[barre].isTouched(xClicked, yClicked)){
                            hotbar[barre].select();
                            Game.getPartie().getJoueur().getHotbar().setSelectedSlot(barre);
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
