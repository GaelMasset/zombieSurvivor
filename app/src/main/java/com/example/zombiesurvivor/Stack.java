package com.example.zombiesurvivor;

public class Stack{
    private Item item;
    private int qtt;

    public Stack(Item i, int qtt){
        this.qtt = qtt;
        this.item = i;
    }

    public Item getItem() {
        return item;
    }
    public int getQtt(){
        return this.qtt;
    }
    public boolean add(int qtt){
        if(qtt + this.qtt > item.getTailleStack()) return false;
        this.qtt+=qtt;
        return true;
    }
}
