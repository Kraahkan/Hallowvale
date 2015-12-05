package hallo.testproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;


public class theforge extends AppCompatActivity {

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    ArrayList<String> items;
    ArrayList<String> items2;

    public static String[] itemNames = {"torch", "flint", "book", "rope", "watch", "pickaxe", "grapple", "butterfly"};

    TextView text1, text2, forgedItem;

    String selectedItem, selectedItem2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theforge);

        selectedItem = "";
        selectedItem2 = "";

        items = new ArrayList<String>();
        items2 = new ArrayList<String>();
        ListView itemsList = (ListView)findViewById(R.id.listView);
        ListView itemsList2 = (ListView)findViewById(R.id.listView2);

        text1 = (TextView)findViewById(R.id.itemForge1);
        text2 = (TextView)findViewById(R.id.itemForge2);
        forgedItem = (TextView)findViewById(R.id.forgedItem);

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        getItems();

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);

        itemsList.setAdapter(arrayAdapter);


        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                selectedItem = items.get(position);
                //Toast.makeText(getApplicationContext(), "Item selected " + selectedAnimal, Toast.LENGTH_LONG).show();

                text1.setText(selectedItem);
            }
        });

        ArrayAdapter<String> arrayAdapter2 =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items2);

        itemsList2.setAdapter(arrayAdapter2);

        itemsList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                selectedItem2 = items.get(position);
                //Toast.makeText(getApplicationContext(), "Item selected " + selectedAnimal, Toast.LENGTH_LONG).show();

                text2.setText(selectedItem2);
            }
        });


    }

    void getItems() {

        for (int i = 0; i < itemNames.length; i++) {

            if (Objects.equals(preferenceSettings.getBoolean(itemNames[i], false), true) && !checkValidity(theforge.itemNames[i])) {

                items.add(itemNames[i].toUpperCase());
                items2.add(itemNames[i].toUpperCase());

            }

        }

    }

    public void forgeItems(View v){

        forgeCheck("ROPE", "PICKAXE", "grapple"); //First and second params for items to be forged. Third for the item name that can be created.

    }

    public void forgeCheck(String item1, String item2, String item3){


        if((Objects.equals(selectedItem, item1) && Objects.equals(selectedItem2, item2)) || (Objects.equals(selectedItem2, item1) && Objects.equals(selectedItem, item2))){


            if(Objects.equals(preferenceSettings.getBoolean(item3, false), false)) {
                preferenceEditor.putBoolean(item3, true);
                preferenceEditor.commit();
                Toast.makeText(getApplicationContext(), "Forged a " + item3.substring(0, 1).toUpperCase() + item3.substring(1), Toast.LENGTH_LONG).show();
                forgedItem.setText(item3.substring(0, 1).toUpperCase() + item3.substring(1));

            }else {

                forgedItem.setText("Already forged");
                Toast.makeText(getApplicationContext(), "Already forged", Toast.LENGTH_LONG).show();

            }

        }else
        {

            Toast.makeText(getApplicationContext(), "Nothing happens", Toast.LENGTH_LONG).show();
            forgedItem.setText("Nothing happens");

        }

    }

    public boolean checkValidity(String s){

       return checkIt("rope", "pickaxe", "grapple", s);

    }

    public boolean checkIt(String item1, String item2, String item3, String current){

        return ((Objects.equals(current, item1) || Objects.equals(current, item2)) && preferenceSettings.getBoolean(item3, false));

    }



    @Override
    public void onPause(){

        super.onPause();

        finish();

    }

    public void onBackPress(View v){

        Intent i = new Intent(theforge.this, beginning.class);
        startActivity(i);

        finish();

    }
}
