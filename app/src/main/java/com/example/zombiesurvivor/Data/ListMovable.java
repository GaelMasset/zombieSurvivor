package com.example.zombiesurvivor.Data;

import com.example.zombiesurvivor.Floor;
import com.example.zombiesurvivor.Movable;

import java.util.ArrayList;

public class ListMovable {
    public static ArrayList<CheminCode> chemins = new ArrayList<CheminCode>(){{
        add(new CheminCode("floor_grass_base",   "F000001", "floor", 0,0,0,0));
        add(new CheminCode("floor_grass_base_top",   "F000002", "floor", 0,0,0,0));
        add(new CheminCode("floor_grass_stone_1", "F000011", "floor", 0,0,0,0));
        add(new CheminCode("floor_grass_stone_2", "F000012", "floor", 0,0,0,0));
        add(new CheminCode("floor_grass_stone_3", "F000013", "floor", 20,60,75,5));

        add(new CheminCode("path_grass_bottom", "F000030", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_bottomleft", "F000031", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_left", "F000032", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_topleft", "F000033", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_top", "F000034", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_topright", "F000035", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_right", "F000036", "floor", 0,0,0,0));
        add(new CheminCode("path_grass_bottomright", "F000037", "floor", 0,0,0,0));


        add(new CheminCode("border_grass_inner_topleft", "F100001", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_inner_topright", "F100002", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_inner_bottomright", "F100003", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_inner_bottomleft", "F100004", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_bottom", "F100005", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_left", "F100006", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_right", "F100007", "wall", 0,0,0,0));
        add(new CheminCode("border_grass_top", "F100008", "wall", 0,30,0,0));
    }};
}

