/*class for print the table with the attribute name and divider*/
import java.util.ArrayList;
import java.util.List;

public class Print {
    private int[] printWidth;
    private List<String[]> printData;
    private static String[][] boxChars = {{ "+","+","+","-" },{"+","+","+","-" },{"+","+","+","-" },};
    private static String vertChar = "|";

    Print(Boolean basic)
    {
        if (basic == true)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++) boxChars[i][j] = "+";
            }
            for (int i = 0; i < 3; i++) boxChars[i][3] = "-";
            vertChar = "|";
        }
    }

    // Print out the records in a formatted table
    public int printTable(List<String[]> printData)
    {
        this.printData = printData;
        setPrintWidth();

        int lineCount = 0;
        printDivider(0);
        printRow(0);
        printDivider(1);
        for (int i = 1; i < printData.size(); i++)
        {
            printRow(i);
            lineCount++;
        }
        printDivider(2);
        return lineCount;
    }

    // For the table being printed set printWidth to equal the highest number of characters for
    // each column (including the attribute names). This is required for correct table formatting.
    private void setPrintWidth()
    {
        int numCols = printData.get(0).length;
        printWidth = new int[numCols];

        for (String[] line : printData)
        {
            for (int col = 0; col < numCols; col++) {
                int newLength = line[col].length() + 1;
                if (newLength > printWidth[col]) printWidth[col] = newLength;
            }
        }
    }

    // Print the table divider, correctly formatted based on printWidth with pos indicating where
    // the divider lies on the table. Return the length of the printed string (for testing).
    // pos = 0 (top table divider), pos = 1 (middle table divider), pos = 2 (bottom table divider)
    private int printDivider(int pos)
    {
        String divider = " " + boxChars[pos][0];

        for(int col = 0; col < printWidth.length; col++)
        {
            for (int i = 0; i < printWidth[col]; i++) divider += boxChars[pos][3];
            divider += boxChars[pos][3] + boxChars[pos][1];
        }

        System.out.println(divider.substring(0, divider.length() - 1) + boxChars[pos][2]);

        return divider.length();
    }

    // Print the row at the given index, with space padding based on printWidth.
    // Return the length of the printed string (for testing).
    private int printRow(int index)
    {
        String line = " ";

        for(int col = 0; col < printData.get(index).length; col++) {
            line += vertChar;
            line += " " + printData.get(index)[col];

            int spaces = printWidth[col] - printData.get(index)[col].length();
            for (int i = 0; i < spaces; i++) line += " ";

        }
        line += vertChar;
        System.out.println(line);

        return line.length();
    }

    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    private static void test()
    {
        List<String[]> testInput = new ArrayList<>();
        testInput.add(new String[]{"ID", "Name", "Kind", "Owner"});
        testInput.add(new String[]{"1", "Fido", "dog", "ab123"});
        testInput.add(new String[]{"2", "Wanda", "Fish", "ef789"});
        int lineCharLength = 38;

        Print testDisplay = new Print(true);

        // Test printTable prints the correct number of records
        claim(testDisplay.printTable(testInput) == 2);

        // Test printWidth calculated values against hard-coded expected values
        claim(testDisplay.printWidth[0] == 3);
        claim(testDisplay.printWidth[1] == 6);
        claim(testDisplay.printWidth[2] == 13);
        claim(testDisplay.printWidth[3] == 6);

        // Test printDivider prints the expected number of characters
        claim(testDisplay.printDivider(0) == lineCharLength);
        claim(testDisplay.printDivider(1) == lineCharLength);
        claim(testDisplay.printDivider(2) == lineCharLength);

        // Test printRow prints the expected number of characters
        claim(testDisplay.printRow(0) == lineCharLength);
        claim(testDisplay.printRow(1) == lineCharLength);
        claim(testDisplay.printRow(2) == lineCharLength);
    }

    public static void main(String[] args) {
        Print p = new Print(true);
        p.test();
        System.out.println("Tests pass.");
    }
}
