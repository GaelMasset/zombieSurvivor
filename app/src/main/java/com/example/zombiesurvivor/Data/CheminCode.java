package com.example.zombiesurvivor.Data;

public class CheminCode{
    private final String cheminImage;
    private final String code;
    private final String type;
    private final double enfoncementTop;
    private final double enfoncementBottom;
    private final double enfoncementLeft;
    private final double enfoncementRight;


    public CheminCode(String cheminImage, String code, String type,
                      double enfoncementTop, double enfoncementBottom,
                      double enfoncementLeft, double enfoncementRight){
        this.cheminImage = cheminImage;
        this.enfoncementTop = enfoncementTop;
        this.enfoncementBottom = enfoncementBottom;
        this.enfoncementLeft = enfoncementLeft;
        this.enfoncementRight = enfoncementRight;
        this.code = code;
        this.type = type;
    }
    public String getCheminImage() {
        return cheminImage;
    }
    public String getType(){
        return this.type;
    }

    public String getCode() {
        return code;
    }

    public double getEnfoncementTop() {
        return enfoncementTop;
    }

    public double getEnfoncementBottom() {
        return enfoncementBottom;
    }

    public double getEnfoncementLeft() {
        return enfoncementLeft;
    }

    public double getEnfoncementRight() {
        return enfoncementRight;
    }
}