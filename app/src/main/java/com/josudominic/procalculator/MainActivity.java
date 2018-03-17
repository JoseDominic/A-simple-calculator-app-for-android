package com.josudominic.procalculator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    /***Variable Declaration*/
    private TextView _screen1;
    private TextView _screen2;
    private String display="";
    private String display1="";
    private String currentoperator="";
    private Boolean hasresult= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /***assigning values to screen 1 and screen 2 when app launches*/
        _screen1 = (TextView)findViewById(R.id.screen1);
        _screen1.setText(display);
        _screen2 = (TextView)findViewById(R.id.screen2);

    }
    /**Method to update screen 1*/
    private  void updatescreen1()
    {/****Code to change color of operators using spannable string****/
     /****************************************************************/

        SpannableStringBuilder sb = new SpannableStringBuilder(display);
        Pattern p = Pattern.compile("x", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(display);
        while (m.find()){
            //String word = m.group();
            //String word1 = notes.substring(m.start(), m.end());

            sb.setSpan(new ForegroundColorSpan(Color.rgb(8, 153, 210)), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }


        /*************updating screen 1*******************/
        _screen1.setText(sb);
    }

    /**Method to update result screen(screen 2)*/
    private void updatescreen2(){
        _screen2.setText(display1);
    }


    /**On click number button*/

    protected void onclicknumber(View v)
    {   if (hasresult){
        clear();
        updatescreen1();
        updatescreen2();}

        Button b = (Button) v;
        display+=b.getText();
        updatescreen1();
    }
    /**On click operator button**/
    protected void  onclickoperator(View v){
        if (hasresult) {

            hasresult=false;
        }
        Button b = (Button) v;
        display+=b.getText();
        currentoperator = b.getText().toString();
        updatescreen1();
    }
    /**function to clear strings in both screens */
    private void clear(){
        display="";
        display1="";
        currentoperator = "";
        hasresult=false;

    }

    /**on click C button**/
    protected void  onclickclear(View v){
        clear();
        updatescreen1();
        updatescreen2();
    }


    /********************On click backspace button**********/
    protected void onclickbackspace(View v) {
        if(display.length()>0) {
            display = display.substring(0, display.length() - 1);
            updatescreen1();
            /**To clear result screen if backspace is pressed**/
            display1 = "";
            hasresult = false;
            updatescreen2();
        }
    }


    /**On click equal button**/
    protected void onclickequal(View v){
        /*****Evaluation of expression using rhino libray***/
        /**************************************************/

        /**Replacing x with * for evaluation by library*/
        String temp;
        temp=display.replaceAll("x","*");
        /**Evaluation of expression with rhino**/
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        try{
            Scriptable scope =rhino.initStandardObjects();
            display1=rhino.evaluateString(scope,temp,"JavaScript",1,null).toString();
            hasresult=true;
        }
        catch (Exception e){
            display1="Error";
        }

        updatescreen2();

    }
}

