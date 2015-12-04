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
    static String roomName = "";
    static String[] strings = {firstLine, secondLine, thirdLine};
    static String line_read = null;
    static beginning game = new beginning();

    static String[][] roomFlags = new String[3][200];  // first column is paths, second is direction, third is visited 1st time

    static String buttons[][] = { // button info for next rooms
            {"first.txt","second.txt","third.txt","fourth.txt"}, // file paths
            {"The first option","The second option","The third option","The fourth option"}, // button txt
            {"","","",""} // items needed
    };

    static String item = ""; // current item

    // -------------------------------------------------------------------------------------------

    public static void createRoom(InputStream file, String name) { // creates a room from a txt file

        currentPath = name;

        firstLine = ""; secondLine = ""; thirdLine = "";
        item = ""; roomName = "";

        for (int c = 0; c < buttons.length; c++)
            for (int d = 0; d < buttons[c].length; d++)
                buttons[c][d] = "";

        int lineNum = 0; // index for moving through file

        Scanner s = new Scanner(file);

        while ((s.hasNextLine())) {

            String line = s.nextLine();

            switch (lineNum) {

                case 0:
                    firstLine = line;
                    break;

                case 1:
                    secondLine = line;
                    break;

                case 2:
                    thirdLine = line;
                    break;

                case 3:
                    item = line;
                    break;

                case 8:
                    roomName = line;
                    break;

                default:
                    break;

            }


            if (lineNum >= 4 && lineNum <= 7) { // rooms


                boolean readPath, readDescription, readItemNeeded, readDirection, end;
                readPath = true; readDescription = false; readItemNeeded = false; readDirection = false; end = false;
                int count = 0;

                String path = "", description = "", itemNeeded = ""; String direction = "";

                while (!end) {

                    if (line.length() == 0) // break if line is empty
                        break;

                    if (line.charAt(count) == '%') { //check if done reading path
                        readPath = false;
                        readDescription = true;
                    }

                    if (line.charAt(count) == '&') { // check if done reading description
                        readDescription = false;
                        readItemNeeded = true;
                    }

                    if (readPath)
                        path += line.charAt(count);

                    if (readDescription && line.charAt(count) != '%')
                        description += line.charAt(count);

                    if (readItemNeeded && line.charAt(count) != '&')
                        itemNeeded += line.charAt(count);

                    if (readItemNeeded && Character.isDigit(line.charAt(count))) // reads direction we are entering the next room
                        direction = String.valueOf(line.charAt(count));

                    if ( count == line.length() - 1 ) // checks if end of line
                        end = true;

                    if ( count < line.length() - 1 )
                        count++;

                    if (end) {

                        buttons[0][lineNum - 4] = path;
                        buttons[1][lineNum - 4] = description;
                        buttons[2][lineNum - 4] = itemNeeded;
                        for (int c = 0; c < roomFlags[0][c].length(); c++) // changes direction
                            if (roomFlags[0][c].equals(currentPath))
                                roomFlags[1][c] = direction;


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
