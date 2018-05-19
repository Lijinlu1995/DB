/* Store table in database. The table is contains a List of records,
and all records must have the same number of attributes.
Functions are supplied for adding, removing and selected records.
add functions: can also load and save table in files */
import java.io.*;
import java.util.*;
public class Table {
    private List<Record> records;
    private String tableName;

    // Initialise empty table with the input column headings
    Table(String name, String[] attributeNames)
    {
        tableName = name;
        records = new ArrayList<>();
        records.add(new Record(attributeNames));
    }

    // Alternative constructor when loading a table from file.
    Table(String fileName) {
        tableName = fileName;
        records = new ArrayList<>();
        loadTable(fileName);
    }

    // Used to determine if two records are the same
    public boolean equals(Table other)
    {
        if (this.records.size() != other.records.size()) return false;

        for (int i = 0; i < this.records.size(); i++) {
            if (!this.records.get(i).equals(other.records.get(i))) return false;
        }

        return true;
    }

    public Boolean insert(Record newRecord, Boolean noOut)
    {
        if (records.size() == 0 || newRecord.getSize() == records.get(0).getSize()) {
            records.add(newRecord);
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
    public Boolean update(int index, Record newRecord)
    {
        if (index == 0) {
            System.out.println("Failed to update record.");
            return false;
        }

        if (newRecord.getSize() == records.get(0).getSize()) {
            try {
                records.remove(index);
                records.add(index, newRecord);
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
        int numCols = records.get(0).getSize();
        int[] printWidth = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            printWidth[i] = records.get(0).getElement(i).length() + 1;
        }

        for (int index : indexes) {
            for (int attr = 0; attr < numCols; attr++) {
                int newLength = records.get(index).getElement(attr).length() + 1;
                if (newLength > printWidth[attr]) printWidth[attr] = newLength;
            }
        }

        return printWidth;
    }

    private void printRow(int index, int[] printWidth)
    {
        System.out.print(" ");
        for(int attr = 0; attr < records.get(0).getSize(); attr++) {
            System.out.print("| ");
            System.out.print("" + records.get(index).getElement(attr));

            int spaces = printWidth[attr] - records.get(index).getElement(attr).length();
            for (int j = 0; j < spaces; j++) System.out.print(" ");

        }
        System.out.println("|");
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
                line = "";
            }
        }
        catch (IOException e) {
            System.out.println("Failed to save table!");
            return false;
        }

        return true;
    }

    // Load file insert each line of the record into the table.
    private Boolean loadTable(String fileName)
    {
        String path = "tableFiles/" + fileName + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line = "";
            int validLines = 0, invalidLines = 0;

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");
                if (insert(new Record(lineSplit), true) == true ) validLines++;
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
        Record r1 = new Record(new String[]{"1", "Fido", "dog", "ab123"});
        Record r2 = new Record(new String[]{"2", "Wanda", "hippopotamus", "ef789"});
        Record r3 = new Record(new String[]{"2", "Jeremy", "Axolotl", "ef789"});
        Record r4 = new Record(new String[]{"ab123", "Jo"});
        Record r5 = new Record(new String[]{"ef789", "Jeff"});

        // Test table for testing the other functions
        Table testTable = new Table("test", new String[]{"Id", "Name", "Kind", "Owner"});

        // Testing the insert function with valid and invalid input (wrong number of attributes)
        claim(testTable.insert(r1, false) == true);
        claim(testTable.insert(r2, false) == true);
        claim(testTable.insert(r4, false) == false);

        // Testing the select function with different index selections (int[]{0} is select all)
        claim(testTable.select(new int[]{1}) == 1);
        claim(testTable.select(new int[]{2}) == 1);
        claim(testTable.select(new int[]{1, 2}) == 2);
        claim(testTable.select(new int[]{2, 1}) == 2);
        claim(testTable.select(new int[]{0}) == 2);

        // Testing the update function with valid and invalid array indexes
        claim(testTable.update(2, r3) == true);
        claim(testTable.update(3, r3) == false);
        claim(testTable.update(0, r3) == false);
        claim(testTable.records.get(2).getElement(1).equals(r3.getElement(1)));
        claim(testTable.records.get(2).getElement(2).equals(r3.getElement(2)));

        // Testing the delete function with valid and invalid array indexes
        claim(testTable.delete(2) == true);
        claim(testTable.delete(1) == true);
        claim(testTable.delete(0) == false);
        // Testing that the records have been properly removed
        claim(testTable.records.size() == 1);

        // Testing the save and load functionality
        Table testTable2 = new Table("test", new String[]{"Id", "Name"});
        Table testTable3 = new Table("test", new String[]{"Id", "Name"});
        testTable2.insert(r4, false);
        testTable2.insert(r5, false);
        claim(testTable2.saveTable() ==true);
        claim(testTable2.equals(testTable3) == false);
        // Clean-up test file at the end
        try {
            File f = new File("tableFiles/test.txt");
            f.delete();
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
