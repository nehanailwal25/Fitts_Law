package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;

import java.util.Random;

public class RandomActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRandom;
    private int currentTrial=1;
    // possition of last drawn button
    private int lastX=-1;
    private int lastY=-1;
    // available screen size
    private int screenWidth;
    private int screenHeight;
    // button dimensions
    private int buttonWidth;
    private int buttonHeight;
    // counter
    private int partCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        // get button's refarence
        buttonRandom = findViewById(R.id.my_button);
        // button actions
        setlistener();
        // show buttons randomaly
        showRandomButtons();
    }

    /// Button action
    public void setlistener(){
        buttonRandom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showRandomButtons();
    }


    public void showRandomButtons(){
        AbsoluteLayout.LayoutParams absParams =
                (AbsoluteLayout.LayoutParams)buttonRandom.getLayoutParams();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        buttonRandom.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        // button dimensions
        buttonWidth=buttonRandom.getMeasuredWidth();
        buttonHeight=buttonRandom.getMeasuredHeight();
        // screen's drawable width and height
        screenWidth = displaymetrics.widthPixels-buttonWidth-20;
        screenHeight = displaymetrics.heightPixels-buttonHeight;

        // get random position for button
        Random r = new Random();
        int x = getRandomX(r,screenWidth);
        int y=  getRandomY(r, screenHeight);

        // for 1st and 4th button, update stored possion of last drawn button
        if(partCount==0 || partCount==4){
            lastX = getRandomX(r,screenWidth);
            lastY=  getRandomY(r, screenHeight);
        }
        // update counter
        partCount++;
        // assign position to layout
        absParams.x=x;
        absParams.y=y;
        // absParams.width=500;
        buttonRandom.setLayoutParams(absParams);
    }

    /// Get random value of x where next button will be drawn
    public int getRandomX(Random random,int width){
        // get new x
        int x=random.nextInt(width);
        // check if it is first button
        if(lastX==-1)
            return x;

        // space available in right side of current button
        int rightSpace=width-(lastX+buttonWidth);
        // space available in left side of current button
        int leftSpace=lastX;

        // range with minimum and maximum points where next button can draw
        int randomMin=rightSpace;
        int randomMax=rightSpace;
        // if there is more space in left
        if(leftSpace > rightSpace){
            randomMin=leftSpace;
            randomMax=leftSpace;
        }

        Random r = new Random();
        int range = r.nextInt(randomMax - randomMin + 1);
        x = range + randomMin;

        if (x < 0) {
            x = 10;
            System.out.println("X value");
            System.out.println(x);
            System.out.println(randomMin);
        }


        return x;
    }

    public int getRandomY(Random random,int height){
        int y=random.nextInt(height);
        if(lastY==-1)
            return y;

        int bottomSpace=height-(lastY+buttonHeight);
        int topSpace=lastY;

        int randomMin=bottomSpace;
        int randomMax=bottomSpace-1;

        if(topSpace>bottomSpace){
            randomMin=topSpace;
            randomMax=topSpace;
        }
        Random r = new Random();
        y = r.nextInt((randomMax+1) - randomMin) + randomMin;

        if (y < 0) {
            y = 10;

            System.out.println("Y value");
            System.out.println(y);
            System.out.println(randomMin);
        }


        return y;
    }

    public void showRandomButtonsV2(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Random R = new Random();
        final float dx = R.nextFloat() * displaymetrics.widthPixels;
        final float dy = R.nextFloat() * displaymetrics.heightPixels;
        buttonRandom.animate()
                .x(dx)
                .y(dy)
                .setDuration(0)
                .start();

    }

}