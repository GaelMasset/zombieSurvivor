package com.example.zombiesurvivor;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Stack> stacks;
    private int selectedSlot;
    private int nbSlot;
    public Inventory(int nbSlot){
        this.nbSlot = nbSlot;
        stacks = new ArrayList<>();
        for(int i =0; i < nbSlot; i++) stacks.add(null);
    }


    public boolean add(Item item, int qtt) {
        for (int i = 0; i < this.nbSlot; i++) {
            if (stacks.get(i) != null && stacks.get(i).getItem().equals(item)) {
                if (stacks.get(i).getItem().getTailleStack() >= stacks.get(i).getQtt() + qtt) {
                    stacks.get(i).add(qtt);
                    return true;
                }
            }
        }
        for (int i = 0; i < this.nbSlot; i++) {
            if (stacks.get(i) == null) {
                stacks.set(i, new Stack(item, qtt));
                return true;
            }
        }
        return false;
    }


    public boolean remove(int slot) {
        if (slot >= 0 && slot < nbSlot && stacks.get(slot) != null) {
            stacks.set(slot, null);
            return true;
        }
        return false;
    }

    public Stack get(int slot) {
        if (slot >= 0 && slot < nbSlot && stacks.get(slot) != null) {
            return stacks.get(slot);
        }
        return null;
    }


    public int getSelectedSlot() {
        return selectedSlot;
    }
    public Stack getSelectedStack(){
        return stacks.get(selectedSlot);
    }
    public void setSelectedSlot(int slot){
        this.selectedSlot = slot;
    }
}
