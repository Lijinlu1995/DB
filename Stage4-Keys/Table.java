/* Store table in database. The table is contains a List of records,
and all records must have the same number of attributes.
Functions are supplied for adding, removing and selected records.
add functions: load and save table in files
Update keys part with auto ID*/
import java.io.*;
import java.util.*;
public class Table {
    private List<Record> records;
    private int key;
    private String tableName;

    // Initialise empty table with the input column headings
    Table(String name, String[] attributeNames)
    {
        key = 0;
        tableName = name;
        records = new ArrayList<>();
        records.add(0, new Record(autoKey(), attributeNames));
    }

    // Alternative constructor when loading a table from file.
    Table(String fileName, Boolean input) {
        tableName = fileName;
        records = new ArrayList<>();
        loadTable(fileName, input);
    }

    private int autoKey()
    {
        int newKey = key;
        key++;
        return newKey;
    }
    // Used to determine if two records are the same
    public boolean equals(Table other)
    {
        if (this.records.size() != other.records.size()) return false;
        if (this.key != other.key) return false;
        for (int i = 0; i < this.records.size(); i++) {
            if (!this.records.get(i).equals(other.records.get(i))) return false;
        }
        return true;
    }

    // Insert new record into the table if it has the correct number of attributes, returning
    // true, else return false. If this is the first record do not check for size.
    // If ID == -1, auto new ID, else use the given ID value.
    public Boolean insert(String[] inputStrings, int ID, Boolean noOut)
    {
        if (records.size() == 0 || inputStrings.length == records.get(0).getSize())
        {
            if (ID == -1) records.add(new Record(autoKey(), inputStrings));
            else          records.add(new Record(ID, inputStrings));

            if (noOut == false) System.out.println("Record added successfully.");
            return true;
        }
        else {
            if (noOut == false) System.out.println("Failed to add record, does not match table.");
            return false;
        }
    }

    public Boolean delete(int index)
    {
        if (index == 0) {
            System.out.println("Failed to delete record.");
            return false;
        }

        try {
            records.remove(index);
            System.out.println("Record deleted successfully.");
            return true;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Failed to delete record.");
            return false;
        }
    }

    // index 0 is the attribute row so counts as invalid.
    public Boolean update(int index, String[] inputStrings)
    {
        if (index == 0) {
            System.out.println("Failed to update record.");
            return false;
        }

        if (inputStrings.length == records.get(0).getSize()) {
            try {
                int recordID = records.get(index).getID();
                records.remove(index);
                records.add(index, new Record(recordID, inputStrings));
                System.out.println("Record updated successfully.");
                return true;
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("Failed to update record. Invalid table index.");
                return false;
            }
        }
        else {
            System.out.println("Failed to update record. New record does not match table.");
            return false;
        }
    }

    // Selects the given table index, and prints the record. Returns true if index is valid.
    // Index 0 is the attribute row, so counts as invalid.
    public int select(int[] indexes)
    {
        if (indexes.length == 1 && indexes[0] == 0) {
            indexes = new int[records.size() - 1];
            for (int i = 1; i < records.size(); i++) indexes[i - 1] = i;
        }

        int[] printWidth = setPrintWidth(indexes);
        int rowCount = 0;

        printDivider(printWidth, 0);
        printRow(0, printWidth);
        printDivider(printWidth, 1);
        for (int i = 0; i < indexes.length; i++) {
            printRow(indexes[i], printWidth);
            rowCount++;
        }
        printDivider(printWidth, 2);

        return rowCount;
    }

    private int[] setPrintWidth(int[] indexes)
    {
        int numCols = records.get(0).getSize() + 1;
        int[] printWidth = new int[numCols];
        printWidth[0] = 3;

        for (int attr = 1; attr < numCols; attr++) {
            printWidth[attr] = records.get(0).getElement(attr - 1).length() + 1;
        }

        for (int index : indexes) {
            int newIDLength = String.valueOf(records.get(index).getID()).length() + 1;
            if (newIDLength > printWidth[0]) printWidth[0] = newIDLength;

            for (int attr = 1; attr < numCols; attr++) {
                int newLength = records.get(index).getElement(attr - 1).length() + 1;
                if (newLength > printWidth[attr]) printWidth[attr] = newLength;
            }
        }

        return printWidth;
    }

    private void printRow(int index, int[] printWidth)
    {
        String recordID;

        if (index == 0) recordID = "ID";
        else recordID = String.valueOf(records.get(index).getID());

        System.out.print(" |" + recordID);
        int spaces = printWidth[0] - recordID.length();
        for (int j = 0; j < spaces; j++) System.out.print(" ");
        for(int attr = 0; attr < records.get(0).getSize(); attr++) {
            System.out.print(" | ");
            System.out.print("" + records.get(index).getElement(attr));

            spaces = printWidth[attr] - records.get(index).getElement(attr).length();
            for (int j = 0; j < spaces; j++) System.out.print(" ");

        }
        System.out.println(" ");
    }

    // Print the table divider, correctly formatted based on printWidth.
    private void printDivider(int[] printWidth, int pos)
    {
        //string to clearly show the sides of table
        String left, mid, right, line;

        if      (pos == 0) { left = "+"; mid = "="; right = "+"; line = "="; }
        else if (pos == 1) { left = "+"; mid = "="; right = "+"; line = "="; }
        else               { left = "+"; mid = "-"; right = "+"; line = "-"; }
        String divider = " " + left;

        for(int attr = 0; attr < printWidth.length; attr++) {
            for (int i = 0; i < printWidth[attr]; i++) divider += line;
            divider += line + mid;
        }

        System.out.println(divider.substring(0, divider.length() - 1) + right);
    }

    public Boolean saveTable()
    {
        String path = "tableFiles/" + tableName + ".txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            String line = "";

            for (int rec = 0; rec < records.size(); rec++) {
                for (int attr = 0; attr < records.get(0).getSize(); attr++) {
                    line += records.get(rec).getElement(attr) + ",";
                }
                line = line.substring(0, line.length() - 1);
                bw.write(line);
                bw.newLine();
            }
        }
        catch (IOException e) {
            System.out.println("Failed to save table!");
            return false;
        }
        return true;
    }

    // Load file insert each line of the record into the table.
    private Boolean loadTable(String fileName, Boolean importData)
    {
        String path = "tableFiles/" + fileName;
        if (importData == true) path += ".txt";
        else                    path += ".sql";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = "";
            int validLines = 0, invalidLines = 0;

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");

                // If importing data, autoID. Else the first value is the ID
                int ID;
                if (importData == true) ID = -1;
                else {
                    ID = Integer.parseInt(lineSplit[0]);
                    if (ID >= key) key= ID + 1;
                    lineSplit = Arrays.copyOfRange(lineSplit, 1, lineSplit.length);
                }

                if (insert(lineSplit, ID, true) == true ) validLines++;
                else invalidLines++;
            }

            System.out.println("Entries loaded: "   + validLines +
                    "\nFailed entries: " + invalidLines);
            return true;
        }
        catch (IOException e) {
            System.out.println("Failed to load table!");
            return false;
        }
    }

    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    // Tests for the Table class only called when run by itself
    private static void test()
    {
        // Two test records for testing the table
        String[] recordStr1 = {"Fido", "dog", "ab123"};
        String[] recordStr2 = {"Wanda", "hippopotamus", "ef789"};
        String[] recordStr3 = {"Jeremy", "Axolotl", "ef789"};
        String[] recordStr4 = {"ab123", "Jo"};
        String[] recordStr5 = {"ef789", "Jeff"};

        // Test table for testing the other functions
        Table testTable = new Table("animal", new String[]{"Name", "Kind", "Owner"});

        // Testing the insert function with valid and invalid input (wrong number of attributes)
        claim(testTable.insert(recordStr1, -1, false) == true);
        claim(testTable.insert(recordStr2, -1, false) == true);
        claim(testTable.insert(recordStr4, -1, false) == false);

        // Testing the select function with different index selections (int[]{0} is select all)
        claim(testTable.select(new int[]{1}) == 1);
        claim(testTable.select(new int[]{2}) == 1);
        claim(testTable.select(new int[]{1, 2}) == 2);
        claim(testTable.select(new int[]{2, 1}) == 2);
        claim(testTable.select(new int[]{0}) == 2);

        // Test for setPrintWidth
        claim(Arrays.equals(testTable.setPrintWidth(new int[]{1}), new int[]{3, 5, 5, 6}));
        claim(Arrays.equals(testTable.setPrintWidth(new int[]{2}), new int[]{3, 6, 13, 6}));
        claim(Arrays.equals(testTable.setPrintWidth(new int[]{1, 2}), new int[]{3, 6, 13, 6}));
        claim(Arrays.equals(testTable.setPrintWidth(new int[]{2, 1}), new int[]{3, 6, 13, 6}));

        // Testing the update function with valid and invalid array indexes, then test the update
        // has occured correctly (same ID and elements are equal to the new string values)
        int prevID = testTable.records.get(2).getID();
        claim(testTable.update(2, recordStr3) == true);
        claim(testTable.update(3, recordStr3) == false);
        claim(testTable.update(0, recordStr3) == false);
        claim(testTable.update(2, recordStr4) == false);

        int newID = testTable.records.get(2).getID();
        claim(prevID == newID);

        claim(testTable.records.get(2).getElement(0).equals(recordStr3[0]));
        claim(testTable.records.get(2).getElement(1).equals(recordStr3[1]));
        claim(testTable.records.get(2).getElement(2).equals(recordStr3[2]));

        // Testing the delete function with valid and invalid array indexes
        claim(testTable.delete(2) == true);
        claim(testTable.delete(1) == true);
        claim(testTable.delete(0) == false);
        // Testing that the records have been properly removed
        claim(testTable.records.size() == 1);

        // Testing the save and load functionality
        Table testTable2 = new Table("test", new String[]{"OwnerId", "Name"});
        Table testTable3 = new Table("test", new String[]{"OwnerId", "Name"});
        testTable2.insert(recordStr4, -1, false);
        testTable2.insert(recordStr5, -1, false);
        claim(testTable2.equals(testTable3) == false);
        claim(testTable2.saveTable() == true);
        testTable3 = new Table("test", false);
        claim(testTable2.equals(testTable3) == false);
        // Clean-up test file at the end
        try {
            File f1 = new File("tableFiles/test.txt");
            File f2 = new File("tableFiles/test.sql");
            f1.delete();
            f2.delete();

        }
        catch (SecurityException e) {
            System.out.println("Error deleting test file, SecurityException.");
        }
    }


    public static void main(String[] args) {
        Table t = new Table("animal", new String[]{"Id", "Name", "Kind", "Owner"});
        t.test();
        System.out.println("Tests pass.");
    }
}
