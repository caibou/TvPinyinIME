package me.caibou.ime;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_select_input_method).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
            }
        });

        findViewById(R.id.btn_setting_input_method).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(intent);*/

                KeyboardLoader keyboardLoader = new KeyboardLoader(getApplicationContext());
                keyboardLoader.load(R.xml.skb_qwerty_en);
            }
        });
    }
}
