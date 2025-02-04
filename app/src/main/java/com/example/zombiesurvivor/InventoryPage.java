package com.example.zombiesurvivor;

import static com.example.zombiesurvivor.Base.MainActivity.canvasWidth;
import static com.example.zombiesurvivor.Base.MainActivity.offsetX;
import static com.example.zombiesurvivor.Base.MainActivity.offsetY;
import static com.example.zombiesurvivor.Base.MainActivity.pourcentHauteur;
import static com.example.zombiesurvivor.Base.MainActivity.pourcentLongueur;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.zombiesurvivor.Base.MainActivity;


public class InventoryPage {
    private Image fond;
    private Bouton icone;
    private Bouton closeButton;
    private Image hudMap;
    private Bouton[] slotInventory = new Bouton[12];
    private Bouton[] slotHotbar = new Bouton[4];
    private Bouton trashIcon;
    private boolean isOpen;
    private boolean isDraggingInventory = false;
    private boolean isDraggingHotbar = false;
    private double posRelativeImgX;
    private double posRelativeImgY;
    private double xImageDrag;
    private double yImageDrag;

    public InventoryPage(){
        this.fond = new Image(Game.getPartie().getContext(), 0, 0, MainActivity.canvasWidth, MainActivity.canvasHeight,
                "inventory_hud", 80);
        this.icone = new Bouton(Game.getPartie().getContext(), pourcentLongueur(265.0/3), pourcentHauteur(5.0/1.5),
                (int) pourcentLongueur(17.0/3), (int) pourcentHauteur(17.0/1.5), "inventory_icon_button",
                "inventory_icon_button", true, 80);
        this.closeButton = new Bouton(Game.getPartie().getContext(), pourcentLongueur(278.0/3), pourcentHauteur(5.0/1.5),
                (int) pourcentLongueur(17.0/3), (int) pourcentHauteur(17.0/1.5), "inventory_close_button",
                "inventory_close_button", true, 80);
        this.hudMap = new Image(Game.getPartie().getContext(), pourcentLongueur(8.0/3), pourcentHauteur(8.0/1.5),
                (int) pourcentLongueur(81.0/3), (int) pourcentHauteur(81.0/1.5), "inventory_hud_map", 80);
        for(int i = 0; i < 12; i++) {
            this.slotInventory[i] = new Bouton(Game.getPartie().getContext(), pourcentLongueur(100.0 / 3)+(i%4)*pourcentLongueur(26.0 / 3),
                    pourcentHauteur(35.0 / 1.5) + (i/4) * pourcentHauteur(26.0 / 1.5),
                    (int) pourcentLongueur(22.0 / 3), (int) pourcentHauteur(22.0 / 1.5), "inventory_inventory_slot",
                    "inventory_inventory_slot_clicked", true, 80);
        }
        //The 4 slots of the hotbar in the hud
        double[] coX = {pourcentLongueur(241.0 / 3), pourcentLongueur(217.0 / 3), pourcentLongueur(266.0 / 3), pourcentLongueur(241.0 / 3)};
        double[] coY = {pourcentHauteur(73.0 / 1.5), pourcentHauteur(97.0 / 1.5), pourcentHauteur(97.0 / 1.5), pourcentHauteur(120.0 / 1.5)};
        for(int i = 0; i < 4; i++) {
            slotHotbar[i] = new Bouton(Game.getPartie().getContext(),
                    coX[i],coY[i],
                    (int) pourcentLongueur(22.0 / 3), (int) pourcentHauteur(22.0 / 1.5), "inventory_inventory_slot",
                    "inventory_inventory_slot_clicked", true, 80);
        }
        trashIcon = new Bouton(Game.getPartie().getContext(), pourcentLongueur(142.0/3), pourcentHauteur(115.0/1.5),
                (int)pourcentLongueur(16.0/3), (int)pourcentHauteur(17.0/1.5),
                "inventory_trash_icon", "inventory_trash_icon",
                true, 100);
    }

    public void draw(Canvas canvas){
        if(isOpen){
            fond.draw(canvas);
            closeButton.draw(canvas);

            Game.getPartie().getCarte().drawMinMap(canvas, (int) pourcentLongueur(11.0/3), (int) pourcentHauteur(11.0/1.5),
                    (int) pourcentLongueur(86.0/3), (int) pourcentHauteur(86.0/1.5));
            hudMap.draw(canvas);

            //Dessines les slots d'inventaire
            for(int i = 0; i < 12; i++){
                slotInventory[i].draw(canvas);
                if(Game.getPartie().getJoueur().getInventory().get(i) != null &&
                        !(Game.getPartie().getJoueur().getInventory().getSelectedSlot() == i && isDraggingInventory)){
                    Game.getPartie().getJoueur().getInventory().get(i).getItem().drawIcone(canvas, slotInventory[i].posX,
                            slotInventory[i].posY, slotInventory[i].tailleX, slotInventory[i].tailleY);
                } else if(Game.getPartie().getJoueur().getInventory().get(i) != null &&
                    isDraggingInventory){
                    Game.getPartie().getJoueur().getInventory().get(i).getItem().drawIcone(canvas, xImageDrag,
                            yImageDrag, slotInventory[i].tailleX, slotInventory[i].tailleY);
                }
            }
            for(int i = 0; i < 4; i++){
                slotHotbar[i].draw(canvas);
                if(Game.getPartie().getJoueur().getHotbar().get(i) != null &&
                        !(Game.getPartie().getJoueur().getHotbar().getSelectedSlot() == i && isDraggingHotbar)){
                    Game.getPartie().getJoueur().getHotbar().get(i).getItem().drawIcone(canvas, slotHotbar[i].posX,
                            slotHotbar[i].posY, slotHotbar[i].tailleX, slotHotbar[i].tailleY);
                } else if(Game.getPartie().getJoueur().getHotbar().get(i) != null &&
                        isDraggingHotbar){
                    Game.getPartie().getJoueur().getHotbar().get(i).getItem().drawIcone(canvas, xImageDrag,
                            yImageDrag, slotInventory[i].tailleX, slotHotbar[i].tailleY);
                }
            }
            trashIcon.draw(canvas);

        } else{
            icone.draw(canvas);
        }
    }

    public boolean onClick(MotionEvent event){

        double xClicked = event.getX() - offsetX;
        double yClicked = event.getY() - offsetY;
        int motionaction = event.getActionMasked();
        if (!isOpen) if (icone.isPressed(xClicked, yClicked)) this.isOpen = true;

        if(isOpen) {
            switch (motionaction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    if (isOpen) {
                        if (closeButton.isPressed(xClicked, yClicked)) this.isOpen = false;
                        //Check si on a cliqué sur une des cases de l'inv
                        for (int i = 0; i < 12; i++) {
                            if (slotInventory[i].isPressed(xClicked, yClicked)) {
                                //Si on a cliqué sur un ca desactive les autres
                                for (int j = 0; j < 12; j++) {
                                    slotInventory[j].setIsPressed(false);
                                }
                                for (int j = 0; j < 4; j++) {
                                    slotHotbar[j].setIsPressed(false);
                                }
                                slotInventory[i].setIsPressed(true);
                                Game.getPartie().getJoueur().getInventory().setSelectedSlot(i);
                                isDraggingInventory = true;
                                posRelativeImgX = xClicked - slotInventory[i].getPosX();
                                posRelativeImgY = yClicked - slotInventory[i].getPosY();
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            if (slotHotbar[i].isPressed(xClicked, yClicked)) {
                                //Si on a cliqué sur un ca desactive les autres
                                for (int j = 0; j < 12; j++) {
                                    slotInventory[j].setIsPressed(false);
                                }
                                for (int j = 0; j < 4; j++) {
                                    slotHotbar[j].setIsPressed(false);
                                }
                                slotHotbar[i].setIsPressed(true);
                                Game.getPartie().getJoueur().getHotbar().setSelectedSlot(i);
                                isDraggingHotbar = true;
                                posRelativeImgX = xClicked - slotHotbar[i].getPosX();
                                posRelativeImgY = yClicked - slotHotbar[i].getPosY();
                            }
                        }
                        if (isDraggingInventory || isDraggingHotbar) {
                            xImageDrag = xClicked-posRelativeImgX;
                            yImageDrag = yClicked-posRelativeImgY;
                        }
                    }
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (isOpen) {
                        if (isDraggingInventory || isDraggingHotbar) {
                            xImageDrag = xClicked-posRelativeImgX;
                            yImageDrag = yClicked-posRelativeImgY;
                        }
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP: {

                    for (int i = 0; i < 12; i++) {
                        if (slotInventory[i].isPressed(xClicked, yClicked)) {
                            if (isDraggingHotbar) {
                                Inventory temp = Game.getPartie().getJoueur().getInventory();
                                Inventory temp2 = Game.getPartie().getJoueur().getHotbar();
                                Stack tempo = temp2.get(temp2.getSelectedSlot());
                                Stack s = temp.get(i);
                                Stack s1 = temp2.get(temp2.getSelectedSlot());
                                temp.set(i, tempo);
                                temp2.set(temp2.getSelectedSlot(), s);
                            }
                            else if (isDraggingInventory) {
                                // Echange dans l'inventaire
                                Inventory temp = Game.getPartie().getJoueur().getInventory();
                                Stack tempo = temp.get(temp.getSelectedSlot());
                                Stack s = temp.get(i);
                                Stack s1 = temp.get(temp.getSelectedSlot());
                                temp.set(i, tempo);
                                temp.set(temp.getSelectedSlot(), s);
                            }
                        }
                    }

                    for (int i = 0; i < 4; i++) {
                        if (slotHotbar[i].isPressed(xClicked, yClicked)) {
                            if (isDraggingHotbar) {
                                Inventory temp2 = Game.getPartie().getJoueur().getHotbar();
                                int selectedSlot = temp2.getSelectedSlot();
                                Stack tempo = temp2.get(selectedSlot);
                                Stack s = temp2.get(i);
                                temp2.set(selectedSlot, s);
                                temp2.set(i, tempo);
                            }
                            else if (isDraggingInventory) {
                                Inventory temp = Game.getPartie().getJoueur().getInventory();
                                Inventory temp2 = Game.getPartie().getJoueur().getHotbar();
                                int selectedSlot = temp.getSelectedSlot();
                                Stack tempo = temp.get(selectedSlot);
                                Stack s = temp2.get(i);
                                temp.set(selectedSlot, s);
                                temp2.set(i, tempo);
                            }
                        }
                    }
                    if(trashIcon.isPressed(xClicked, yClicked)){
                        Inventory s;
                        if(isDraggingInventory) s = Game.getPartie().getJoueur().getInventory();
                        else s = Game.getPartie().getJoueur().getHotbar();

                        s.getSelectedStack().getItem().drop();
                        s.remove(s.getSelectedSlot());
                    }


                    isDraggingHotbar = false;
                    isDraggingInventory = false;
                    return true;
                }
            }
        }
        if(!isOpen) return false;
        return true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}

