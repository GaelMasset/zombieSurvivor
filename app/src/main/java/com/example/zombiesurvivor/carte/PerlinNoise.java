package com.example.zombiesurvivor.carte;

import java.util.Random;

public class PerlinNoise {

    private int[] permutationTable;
    private int repeat;

    public PerlinNoise(Random random) {
        repeat = 4096;
        permutationTable = new int[repeat * 2];
        for (int i = 0; i < repeat; i++) {
            permutationTable[i] = i;
        }

        // Mélange la table de permutation
        for (int i = 0; i < repeat; i++) {
            int r = i + random.nextInt(repeat - i);
            int temp = permutationTable[i];
            permutationTable[i] = permutationTable[r];
            permutationTable[r] = temp;
        }
    }

    // Fonction de bruit de Perlin
    public float getNoise(float x, float y) {
        int X = (int) Math.floor(x) & (repeat - 1);
        int Y = (int) Math.floor(y) & (repeat - 1);

        x -= Math.floor(x);
        y -= Math.floor(y);

        float u = fade(x);
        float v = fade(y);

        int a = permutationTable[X] + Y;
        int aa = permutationTable[a];
        int ab = permutationTable[a + 1];
        int b = permutationTable[X + 1] + Y;
        int ba = permutationTable[b];
        int bb = permutationTable[b + 1];

        return lerp(v, lerp(u, grad(permutationTable[aa], x, y), grad(permutationTable[ba], x - 1, y)),
                lerp(u, grad(permutationTable[ab], x, y - 1), grad(permutationTable[bb], x - 1, y - 1)));
    }

    private float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private float grad(int hash, float x, float y) {
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : (h == 12 || h == 14 ? x : 0);
        return (h & 1) == 0 ? u + v : u - v;
    }
}
