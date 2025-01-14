package com.example.zombiesurvivor.Base;

import static com.example.zombiesurvivor.Base.MainActivity.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class PageLoop extends Thread{
    public static final double MAX_UPS = 100;
    private static final double UPS_PERIOD = 1000/MAX_UPS;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private Page page;
    private double averageUPS;
    private double averageFPS;

    public PageLoop(Page game, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.page = game;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run(){
        super.run();
        //Declarations
        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        //Jeu
        Canvas canvas;
        startTime = System.currentTimeMillis();
        while(isRunning){
            //Mets le jeu à jour
            try{
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    canvas.drawColor(Color.BLACK);
                    canvas.translate(offsetX, offsetY);
                    page.update();
                    updateCount++;
                    page.draw(canvas);
                    canvas.drawRect(-offsetX, -offsetY, 0, screenHeight, new Paint(Color.BLACK));
                    canvas.drawRect(-offsetX,-offsetY, screenWidth, 0, new Paint(Color.BLACK));
                    canvas.drawRect(canvasWidth, -offsetY, screenWidth, screenHeight, new Paint(Color.BLACK));
                    canvas.drawRect(-offsetX, canvasHeight, screenWidth, screenHeight, new Paint(Color.BLACK));
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
                frameCount++;
            } catch(IllegalArgumentException e){
                e.printStackTrace();
            }

            //Mets le jeu en pause pour pas dépasser un certain UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            //Skip des images pour garder le rythme je crois ?
            while(sleepTime < 0){
                page.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            }

            // Calcul FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime > 1000){
                averageUPS = updateCount / (elapsedTime/1000);
                averageFPS = frameCount / (elapsedTime/1000);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
}
