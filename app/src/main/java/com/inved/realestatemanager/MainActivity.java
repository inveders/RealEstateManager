package com.inved.realestatemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);


        this.configureTextViewMain();
        this.configureTextViewQuantity();
    }


    /**I add a unit value to the setTextSize. Before setTextSize(15). After setTextSize(15,TypedValue.COMPLEX_UNIT_PX)*/
    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15, TypedValue.COMPLEX_UNIT_PX);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20, TypedValue.COMPLEX_UNIT_PX    );
        this.textViewQuantity.setText(quantity);
    }
}
