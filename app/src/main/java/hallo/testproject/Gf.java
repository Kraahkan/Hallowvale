package hallo.testproject;

import android.util.Log;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;


public class Gf  { // game functions

    static String currentPath = "room1";

    static String firstLine = ""; // first direction
    static String secondLine = ""; // second direction
    static String thirdLine = ""; // flavor first time txt
    static String[] strings = {firstLine, secondLine, thirdLine};
    static String line_read = null;
    static beginning game = new beginning();

    static String[][] roomFlags = new String[3][200];  // first column is paths, second is direction, third is visited 1st time




    static String buttons[][] = { // button info for next rooms
            {"first.txt","second.txt","third.txt","fourth.txt"}, // file paths
            {"The first option","The second option","The third option","The fourth option"}, // button txt
            {"","","",""} // items needed
    };


    static String item = ""; // if an item is in the room, will be stored here


    /// END OF VARS


    public static void createRoom(InputStream file, String name) { // creates a room from a txt file

        currentPath = name;

        firstLine = ""; secondLine = ""; thirdLine = "";
        item = "";

        for (int c = 0; c < buttons.length; c++)
          for (int d = 0; d < buttons[c].length; d++)
              buttons[c][d] = "";

        int lineNum = 0; // index for moving through file

        Scanner s = new Scanner(file);

            while ((s.hasNextLine())) {

                String line = s.nextLine();

                if (lineNum == 0)
                    firstLine = line;

                if (lineNum == 1)
                    secondLine = line;

                if (lineNum == 2) // descriptions
                    thirdLine = line;

                if (lineNum == 3) // item
                    item = line;

                if (lineNum >= 4 && lineNum <= 7) { // rooms


                    boolean end = false;
                    boolean readPath = true; // path is read first, then description, then item
                    boolean readItem = false;
                    int count = 0;

                    String path = "", description = "", itemNeeded = "";


                    while ( end == false ) {

                        if (line.length() == 0)
                            break;

                        if (line.charAt(count) == '%') {
                            readPath = false;
                        }

                        if (line.charAt(count) == '&') {
                            readItem = true;

                        }

                        if (readPath)
                            path += line.charAt(count);

                        else

                        if (line.charAt(count) != '%' && !readItem )
                            description += line.charAt(count);

                        if (readItem)
                            itemNeeded += line.charAt(count);

                            if ( count == line.length() - 1 )
                            end = true;

                        if ( count < line.length() - 1 )
                            count++;

                        if (end) {


                            buttons[0][lineNum - 4] = path;
                            buttons[1][lineNum - 4] = description;
                            buttons[2][lineNum - 4] = itemNeeded;
                            Log.d("path & description - ", path + " " + "description");

                        }

                    }

                }




                lineNum++;

                // update flag

            }






    }

    public static void updateFlags() { // Updates array with txt paths and sets flags
        Log.d("DEBUG", "GF -> path");

        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            roomFlags[0][count] = fields[count].getName();
            roomFlags[1][count] = "0";
            roomFlags[2][count] = "0";
        }
    }



    public static void checkInfo() {

        Log.d("nogf", "cry" + firstLine);
        System.out.println(secondLine);
        System.out.println(thirdLine);
        System.out.println(item);
        for (int c = 0; c < buttons[0].length; c++) {
            System.out.println(buttons[0][c]);
            System.out.println(buttons[1][c]);
        }

        Log.d("yo",Arrays.deepToString(roomFlags[0]));

    }






}
