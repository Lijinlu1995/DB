/*class for print the table with the attribute name and divider
self test: javac Print.java
           java  Print
If it has no throw error the self test pass*/
import java.util.*;

public class Print {
    private int[] width;
    private List<String[]> data;
    private static String[][] cell = {{ "+","+","+","-" },
            {"+","+","+","-" },{"+","+","+","-" },};
    private static String vertical = "|";

    Print(Boolean basic)
    {
        if (basic == true) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) cell[i][j] = "+";
            }
            for (int i = 0; i < 3; i++) cell[i][3] = "-";
            vertical = "|";
        }
    }

    // Print out the records in a formatted table
    public int printTable(List<String[]> data)
    {
        this.data = data;
        setWidth();
        int lineCount = 0;
        printDivider(0);
        printRow(0);
        printDivider(1);
        for (int i = 1; i < data.size(); i++) {
            printRow(i);
            lineCount++;
        }
        printDivider(2);
        return lineCount;
    }

    // For the table being printed set width to equal the highest number of characters
    private void setWidth()
    {
        int numCols = data.get(0).length;
        width = new int[numCols];

        for (String[] line : data) {
            for (int col = 0; col < numCols; col++) {
                int newLength = line[col].length() + 1;
                if (newLength > width[col]) width[col] = newLength;
            }
        }
    }

    // Print the table divider,0 for top, 1 for middle, 2 for bottom
    private int printDivider(int pos)
    {
        String divider = " " + cell[pos][0];

        for(int col = 0; col < width.length; col++) {
            for (int i = 0; i < width[col]; i++) divider += cell[pos][3];
            divider += cell[pos][3] + cell[pos][1];
        }

        System.out.println(divider.substring(0, divider.length() - 1) + cell[pos][2]);

        return divider.length();
    }

    // Print the row with space padding based on width.
    private int printRow(int index)
    {
        String line = " ";

        for(int col = 0; col < data.get(index).length; col++) {
            line += vertical;
            line += " " + data.get(index)[col];

            int spaces = width[col] - data.get(index)[col].length();
            for (int i = 0; i < spaces; i++) line += " ";

        }
        line += vertical;
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

        Print testDisplay = new Print(true);

        // Test printTable prints the correct number of records
        claim(testDisplay.printTable(testInput) == 2);

        // Test width calculated values against hard-coded expected values
        claim(testDisplay.width[0] == 3);
        claim(testDisplay.width[1] == 6);
        claim(testDisplay.width[3] == 6);
    }

    public static void main(String[] args) {
        Print p = new Print(true);
        p.test();
        System.out.println("Tests pass.");
    }
}
