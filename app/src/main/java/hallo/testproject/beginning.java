package hallo.testproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class beginning extends AppCompatActivity implements OnClickListener { // This is the main game interface

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public Context c;
    TextView texter;
    Button left, up, down, right, takeItem, inventory;

    TextView[] items = new TextView[5];

    Button buttons[];

    public Stack<String> heldItems;
    public int count = -1;

    InputStream rooms; // for reading rooms

    public boolean instFlag = true;

    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(beginning.this);

    LayoutInflater inflater = this.getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.inventory, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        heldItems = new Stack();


        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        defaultPrefs(); // Set all shared preferences to default, including items

        // ----------------------------------------------------

        texter = (TextView)findViewById(R.id.textView4);
        texter.setMovementMethod(new ScrollingMovementMethod()); // scrolled txtview
        left = (Button)findViewById(R.id.left_button);
        up = (Button)findViewById(R.id.up_button);
        down = (Button)findViewById(R.id.down_button);
        right = (Button)findViewById(R.id.right_button);
        takeItem = (Button)findViewById(R.id.take_item_button);
        inventory = (Button)findViewById(R.id.inventory);


        left.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        right.setOnClickListener(this);
        takeItem.setOnClickListener(this);
        inventory.setOnClickListener(this);

        Button buttons2[] = {left, up, down, right};
        buttons = buttons2;

        // ----------------------------------------------------

        Gf.updateFlags(); // populates with txt file paths and sets flags all to 0

        InputStream testRoom = getResources().openRawResource(R.raw.start); // first room
        Gf.createRoom(testRoom, "start");

        initialItemCheck(); // gets called everytime there is a click

        updateButtons();

    }

    @Override
    public void onClick(View v) { // generic on click for all buttons

        int resId;

        String name = "";
        switch(v.getId()){


            case R.id.left_button:
                name = Gf.buttons[0][0];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name);
                updateButtons();
                //if (!Objects.equals(Gf.buttons[2][0], ""))
                //   checkHaveItem(Gf.buttons[2][0], 0);
                break;

            case R.id.up_button:
                name = Gf.buttons[0][1];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name );
                updateButtons();
                //if (!Objects.equals(Gf.buttons[2][1], ""))
                //  checkHaveItem(Gf.buttons[2][1], 1);
                break;


            case R.id.down_button:
                name = Gf.buttons[0][2];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name );
                updateButtons();
                //if (!Objects.equals(Gf.buttons[2][2], ""))
                //   checkHaveItem(Gf.buttons[2][2], 2);
                break;


            case R.id.right_button:
                name = Gf.buttons[0][3];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room", String.valueOf(resId));
                Gf.createRoom(rooms, name);
                updateButtons();
                //if (!Objects.equals(Gf.buttons[2][3], ""))
                //   checkHaveItem(Gf.buttons[2][3], 3);
                break;

            case R.id.take_item_button:
                takeItem();
                takeItem.setVisibility(View.GONE);

                break;

            case R.id.inventory:
                callInventory();

                break;

            default:
                break;


        }

        initialItemCheck();
        //callInventory();

    }

    public void callInventory(){

        /*LinearLayout ll = (LinearLayout) findViewById(R.id.inventory_layout);

        for (int i = 0; i < 5; i++)
        {
            TextView tv = new TextView(this);
            tv.setText("Dynamic TextView" + 1);
            tv.setId(1 + 7);
            ll.addView(tv);
        }*/

        //Iterator<String> itr = heldItems.iterator();

        /*final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(beginning.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.inventory, null);*/


        //String[] temp = new String[100];

        /*final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(beginning.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.inventory, null);
        dialogBuilder.setView(dialogView);*/


        dialogBuilder.setView(dialogView);


        if(instFlag) {
            items[0] = (TextView) dialogView.findViewById(R.id.item1);
            items[1] = (TextView) dialogView.findViewById(R.id.item2);
            items[2] = (TextView) dialogView.findViewById(R.id.item3);
            items[3] = (TextView) dialogView.findViewById(R.id.item4);
            items[4] = (TextView) dialogView.findViewById(R.id.item5);

            instFlag = false;
        }


        dialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();


    }


    public void initialItemCheck() { // checks if needs item

        buttons[0].setAlpha(1f);
        buttons[0].setClickable(true);
        buttons[1].setAlpha(1f);
        buttons[1].setClickable(true);
        buttons[2].setAlpha(1f);
        buttons[2].setClickable(true);
        buttons[3].setAlpha(1f);
        buttons[3].setClickable(true);
        

        for (int c = 0; c < 4; c++) {

            if (!Objects.equals(Gf.buttons[2][c], "")) {
                if (preferenceSettings.getBoolean(Gf.buttons[2][c], false) == false) {
                    Log.d("ItemFalse", Gf.buttons[2][0]);
                    buttons[c].setAlpha(.5f);
                    buttons[c].setClickable(false);
                } else {
                    Log.d("ItemTrue", Gf.buttons[2][0]);
                    buttons[c].setAlpha(1f);
                    buttons[c].setClickable(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() { // no bac
    }



    public void updateButtons() { // refreshes buttons after room is created

        takeItem.setVisibility(View.GONE);

        for (int c = 0; c < Gf.buttons[0].length; c++) // removes all
            buttons[c].setVisibility(View.GONE);


        for (int c = 0; c < Gf.buttons[0].length; c++) { // proper button txt
            try {
                //
                buttons[c].setText(Gf.buttons[1][c]);
            } catch (NullPointerException n) {
                Log.d("error", "You got a null point fool");

            }
        }

        for (int c = 0; c < Gf.buttons[0].length; c++) // adds buttons needed
            if (buttons[c].getText() != "")
                buttons[c].setVisibility(View.VISIBLE);


        if (Gf.item.length() > 0 && preferenceSettings.getBoolean(Gf.item, false) == false) {
            takeItem.setVisibility(View.VISIBLE);
            takeItem.setAlpha(1f);
            takeItem.setClickable(true);
            takeItem.setText("Take " + Gf.item);
        }

        Log.d("current", Gf.currentPath);

        setText();

    }

    public void setText() {

        texter.setText("");

        for (int c = 0; c < Gf.roomFlags[0].length; c++) {
            if (Gf.roomFlags[0][c] != null) {
                Log.d("path", Gf.roomFlags[0][c]);
                Log.d("current", Gf.currentPath);
            }


            if (Objects.equals(Gf.roomFlags[0][c], Gf.currentPath)) { // find current room in array
                // checks flags

                if (Integer.parseInt(Gf.roomFlags[2][c]) == 0) { // if first time
                    texter.append(Gf.thirdLine + "\n\n");
                    Gf.roomFlags[2][c] = "1";
                }
                if (Integer.parseInt(Gf.roomFlags[1][c]) == 0) { // if odd time
                    texter.append(Gf.firstLine);
                    Gf.roomFlags[1][c] = "1";
                } else { //(Integer.parseInt(Gf.roomFlags[1][c]) == 1) // if even time
                    texter.append(Gf.secondLine);
                    Gf.roomFlags[1][c] = "0";
                }
            }

        }
    }



    public void takeItem(){ // when you take an item
        preferenceEditor.putBoolean(Gf.item, true);
        preferenceEditor.commit(); //Always commit

        heldItems.push(Gf.item);
        count++;

    }

    public void defaultPrefs(){ // default items

        preferenceEditor.putBoolean("unlittorch", false);
        preferenceEditor.putBoolean("flint", false);
        preferenceEditor.putBoolean("littorch", false);
        preferenceEditor.putBoolean("book", false);
        preferenceEditor.commit();// Always commit

    }


}