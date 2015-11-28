package hallo.testproject;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;


public class Gf  { // game functions

    static String currentPath = "room1";

    static String firstLine = ""; // first description, first and third are only displayed on visiting the room the first time
    static String secondLine = ""; // description, displayed everytime
    static String thirdLine = "";
    static String[] strings = {firstLine, secondLine, thirdLine};
    static String line_read = null;
    static beginning game = new beginning();

    static String[][] roomFlags;  // dynamically generated 2d array for flagging if rooms have been visited00


    static String buttons[][] = { // button info for next rooms
            {"first.txt","second.txt","third.txt","fourth.txt"}, // file paths
            {"The first option","The second option","The third option","The fourth option"} // button txt
    };


    static String item = ""; // if an item is in the room, will be stored here


    /// END OF VARS


    public static void createRoom(InputStream file) { // creates a room from a txt file

        firstLine = ""; secondLine = ""; thirdLine = "";

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
                    boolean readPath = true;
                    int count = 0;

                    String path = "", description = "";


                    while ( end == false ) {

                        // System.out.println(count);
                        // System.out.println(line.length());

                        if (line.charAt(count) == '%') {
                            readPath = false;
                        }

                        if (readPath)
                            path += line.charAt(count);

                        else
                        if (line.charAt(count) != '%')
                            description += line.charAt(count);

                        if ( count == line.length() - 1 )
                            end = true;

                        if ( count < line.length() - 1 )
                            count++;



                        if (end) {


                            buttons[0][lineNum - 4] = path;
                            buttons[1][lineNum - 4] = description;

                        }

                    }



                }




                lineNum++;

                // update flag

            }






    }

    public static void updateFlags() { // looks at all txt files and generates a 2d array
        Log.d("DEBUG", "GF -> path");

        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            Log.i("Raw Asset: ", fields[count].getName());
        }
        //try {
        //     final File folder = new File("C:/Users/helmiligi/Desktop/gameFunctions");
     //   MainMenu myActivity = new MainMenu();
      //  Context context = myActivity.context;
      //  int path = context.getResources().getIdentifier("raw/blastoise","raw", context.getPackageName());




        //    Log.d("DEBUG", "folder -> path" + folder.toString());
         //   listFilesForFolder(folder);
        //} catch (Exception e) {
  //          e.printStackTrace();

//        }


    }




    public static void listFilesForFolder(File folder) {
        Log.d("DEBUG", "GF -> listFilesForFolder");

        for (File fileEntry : folder.listFiles()) {
            Log.d("DEBUG", "fileEntry");
            if (fileEntry.isDirectory()) {
                Log.d("DEBUG", "isDirectory");
                listFilesForFolder(fileEntry);
            } else {
                Log.d("DEBUG", "else");
                System.out.println(fileEntry.getName());
            }
        }
    }

    //roomFlags = new String[2][n];
    //roomFlags[0] = paths;





    public static void checkInfo() {

        Log.d("nogf", "cry" + firstLine);
        System.out.println(secondLine);
        System.out.println(thirdLine);
        System.out.println(item);
        //for (int c = 0; c < buttons[0].length; c++) {
        //    System.out.println(buttons[0][c]);
         //   System.out.println(buttons[1][c]);
        //}

        // System.out.println(Arrays.deepToString(roomFlags[0]));

    }






}
