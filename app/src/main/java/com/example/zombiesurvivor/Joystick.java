package com.example.zombiesurvivor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick extends BoutonDeplacement{
        private Paint outerCirclePaint;
        private Paint innerCirclePaint;
        private int outerCircleCenterPositionX;
        private int outerCircleCenterPositionY;
        private int innerCircleCenterPositionX;
        private int innerCircleCenterPositionY;
        private int innerCircleRadius;
        private int outerCircleRadius;
        private double joystickCenterToTouchDistance;
        Bitmap imgBottom;
        Bitmap imgTop;
        private boolean isPressed;

        public Joystick(Context context, int centerPositionX, int centerPositionY, int innerCircleRadius, int outerCircleRadius, String cheminImgBottom, String cheminImgTop){
            outerCircleCenterPositionX = centerPositionX;
            outerCircleCenterPositionY = centerPositionY;
            innerCircleCenterPositionX = centerPositionX;
            innerCircleCenterPositionY = centerPositionY;
            //Radius
            this.innerCircleRadius = innerCircleRadius;
            this.outerCircleRadius = outerCircleRadius;
            //Paint
            outerCirclePaint = new Paint();
            outerCirclePaint.setColor(Color.BLUE);
            outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            innerCirclePaint = new Paint();
            innerCirclePaint.setColor(Color.GREEN);
            innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

            int resId = context.getResources().getIdentifier(cheminImgBottom, "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            imgBottom = Bitmap.createScaledBitmap(bitmap, outerCircleRadius * 2, outerCircleRadius * 2, false);
            resId = context.getResources().getIdentifier(cheminImgTop, "drawable", context.getPackageName());
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            imgTop = Bitmap.createScaledBitmap(bitmap, innerCircleRadius * 2, innerCircleRadius * 2, false);


        }

    @Override
    public void draw(Canvas canvas) {
        // Dessine le cercle extérieur (imgBottom)
        canvas.drawBitmap(imgBottom,
                outerCircleCenterPositionX - imgBottom.getWidth() / 2,
                outerCircleCenterPositionY - imgBottom.getHeight() / 2,
                null);

        // Dessine le cercle intérieur (imgTop)
        canvas.drawBitmap(imgTop,
                innerCircleCenterPositionX - imgTop.getWidth() / 2,
                innerCircleCenterPositionY - imgTop.getHeight() / 2,
                null);
    }



    public void update(){
            updateInnerCirclePosition();
            if(!isPressed) resetActuator();
        }
        private void updateInnerCirclePosition() {
            innerCircleCenterPositionX = (int) (outerCircleCenterPositionX + actuatorX*outerCircleRadius);
            innerCircleCenterPositionY = (int) (outerCircleCenterPositionY + actuatorY*outerCircleRadius);
        }
        public boolean isPressed(double x, double y) {
            joystickCenterToTouchDistance = Math.sqrt(
                    Math.pow(outerCircleCenterPositionX - x, 2) +
                            Math.pow(outerCircleCenterPositionY - y, 2)
            );
            return joystickCenterToTouchDistance < outerCircleRadius;
        }
        public void setIsPressed(boolean b) {
            this.isPressed = b;
        }
        public boolean getIsPressed() {
            return this.isPressed;
        }
        @Override
        public void setActuator(double x, double y) {
            double deltaX = x - outerCircleCenterPositionX;
            double deltaY = y - outerCircleCenterPositionY;
            double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY,2));
            if(deltaDistance < outerCircleRadius){
                actuatorX = deltaX/outerCircleRadius;
                actuatorY = deltaY/outerCircleRadius;
            } else{
                actuatorX = deltaX/deltaDistance;
                actuatorY = deltaY/deltaDistance;
            }
        }
        public void resetActuator(){
            actuatorX = 0.0;
            actuatorY = 0.0;
        }
        public double getActuatorX() {
            return this.actuatorX;
        }
        public double getActuatorY(){
            return this.actuatorY;
        }
    }
