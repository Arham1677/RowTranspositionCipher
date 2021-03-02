package com.example.rowtranspositioncipher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputLayout enterText, enterKey;
    TextInputEditText resultText;
    Button cipherBtn;
    Switch cipherSwitch;

    String encryptedText, decryptedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterText = findViewById(R.id.enterText);
        enterKey = findViewById(R.id.enterKey);
        resultText = findViewById(R.id.resultView);
        cipherBtn = findViewById(R.id.cipherBtn);
        cipherSwitch = findViewById(R.id.cipherSwitch);

        cipherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cipherSwitch.isChecked()){
                    cipherSwitch.setText("Encrypt ");
                    cipherBtn.setText("Encrypt");
                }
                else{
                    cipherSwitch.setText("Decrypt ");
                    cipherBtn.setText("Decrypt");
                }
            }
        });

        cipherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = enterText.getEditText().getText().toString().trim();
                String key = enterKey.getEditText().getText().toString().trim();
                if(cipherSwitch.isChecked()) {
//                    encryption(text, key);
                    resultText.setText(encryption(text, key));
                } else {
                    resultText.setText(decryption(text, key));
                }
            }
        });
    }

    public static String encryption(String text, String key){
        String plainText = text.toUpperCase().replace(" ", "");
        StringBuilder msg = new StringBuilder(plainText);
        String keyword = key.toUpperCase();
        int[] kywrdNumList = keywordNumAssign(keyword);
        int extraLetters = msg.length() % keyword.length();
        int dummyCharacters = keyword.length() - extraLetters;
        if (extraLetters != 0){
            for (int i = 0; i < dummyCharacters; i++) {
                msg.append("*");
            }
        }
        int numOfRows = msg.length() / keyword.length();
        char[][] arr = new char[numOfRows][keyword.length()];
        int z = 0;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < keyword.length(); j++) {
                arr[i][j] = msg.charAt(z);
                z++;
            }
        }
        // cipher text generation
        StringBuilder cipherText = new StringBuilder();
        String numLoc = getNumberLocation(keyword, kywrdNumList);
        for (int i = 0, k = 0; i < numOfRows; i++, k++) {
            int d;
            if (k == keyword.length()){
                break;
            } else {
                d = Character.getNumericValue(numLoc.charAt(k));
            }
            for (int j = 0; j < numOfRows; j++) {
                cipherText.append(arr[j][d]);
            }
        }
        return cipherText.toString();
    }

    public static String decryption(String text, String key){
        String msg = text.toUpperCase().replace(" ", "");
        String keyword = key.toUpperCase();
        int numOfRows = msg.length() / keyword.length();
        char[][] arr = new char[numOfRows][keyword.length()];
        int[] kywrdNumList = keywordNumAssign(keyword);
        String numLoc = getNumberLocation(keyword, kywrdNumList);
        for (int i = 0, k = 0; i < msg.length(); i++, k++) {
            int d = 0;
            if (k == keyword.length()){
                k = 0;
            } else {
                d = Character.getNumericValue(numLoc.charAt(k));
            }
            for (int j = 0; j < numOfRows; j++, i++) {
                arr[j][d] = msg.charAt(i);
            } // for loop
            --i;
        }
        StringBuilder plainText = new StringBuilder();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < keyword.length(); j++) {
                plainText.append(arr[i][j]);
            } // inner for loop
        } // for loop
        return plainText.toString();
    }


    private static String getNumberLocation(String keyword, int[] kywrdNumList) {
        String numLoc = "";
        for (int i = 1; i < keyword.length() + 1; i++) {
            for (int j = 0; j < keyword.length(); j++) {
                if (kywrdNumList[j] == i){
                    numLoc += j;
                }
            }
        }
        return numLoc;
    }

    private static int[] keywordNumAssign(String keyword) {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int[] kywrdNumList = new int[keyword.length()];

        int init = 0;
        for (int i = 0; i < alpha.length(); i ++){
            for (int j = 0; j < keyword.length(); j++) {
                if (alpha.charAt(i) == keyword.charAt(j)){
                    init++;
                    kywrdNumList[j] = init;
                }
            } // inner for
        } // for
        return kywrdNumList;
    }
}