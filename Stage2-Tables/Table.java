/* Store table in database. The table is contains a List of records,
and all records must have the same number of attributes.
Functions are supplied for adding, removing and selected records. */
import java.util.*;
public class Table {
    private List<Record> records;
    Table(String[] attributeNames)
    {
        records = new ArrayList<>();
        records.add(new Record(attributeNames));
    }

    public Boolean insert(Record newRecord)
    {
        if (newRecord.getSize() == records.get(0).getSize()) {
            records.add(newRecord);
            System.out.println("Record added successfully.");
            return true;
        }
        else {
            System.out.println("Failed to add record. Record does not match table.");
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

    // index is valid. Index 0 is the attribute row so counts as invalid.
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
    public Boolean select(int index)
    {
        if (index == 0) {
            System.out.println("Select failed. Invalid table index specified.");
            return false;
        }

        try {
            int size = records.get(index).getSize();
            printSelected(index, size);
            return true;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Select failed. Invalid table index specified.");
            return false;
        }
    }

    // Print out the record at the given index
    void printSelected(int index, int size)
    {
        Record record = records.get(index);
        int[] printWidth = setPrintWidth(index);

        printDivider(printWidth);
        printRow(0, printWidth);
        printDivider(printWidth);
        printRow(index, printWidth);
        printDivider(printWidth);
    }

    private int[] setPrintWidth(int selected)
    {
        int[] printWidth = new int[records.get(0).getSize()];
        int[] indexes = {0, selected};

        for (int attr = 0; attr < records.get(0).getSize(); attr++) {
            for (int index : indexes) {
                int newLength = records.get(index).getElement(attr).length() + 1;
                if (newLength > printWidth[attr]) printWidth[attr] = newLength;
            }
        }

        return printWidth;
    }

    private void printRow(int index, int[] printWidth)
    {
        for(int attr = 0; attr < records.get(0).getSize(); attr++) {
            System.out.print("| ");
            System.out.print("" + records.get(index).getElement(attr));

            int spaces = printWidth[attr] - records.get(index).getElement(attr).length();
            for (int j = 0; j < spaces; j++) System.out.print(" ");

        }
        System.out.println("|");
    }

    // Print the table divider, correctly formatted based on printWidth.
    private void printDivider(int[] printWidth)
    {
        String divider = "+";

        for(int attr = 0; attr < printWidth.length; attr++) {
            for (int i = 0; i < printWidth[attr]; i++) divider += "-";
            divider += "-+";
        }

        System.out.println("" + divider);
    }

    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    // Tests for the Record class only called when Record is run by itself
    private static void test()
    {
        // Two test records for testing the table
        Record r1 = new Record(new String[]{"1", "Fido", "dog", "ab123"});
        Record r2 = new Record(new String[]{"2", "Wanda", "hippopotamus", "ef789"});
        Record r3 = new Record(new String[]{"ab123", "Jo"});
        Record r4 = new Record(new String[]{"2", "Jeremy", "Axolotl", "ef789"});

        // Test table for testing the other functions
        Table testTable = new Table(new String[]{"Id", "Name", "Kind", "Owner"});

        // Testing the insert function with valid and invalid input (wrong number of attributes)
        claim(testTable.insert(r1) == true);
        claim(testTable.insert(r2) == true);
        claim(testTable.insert(r3) == false);

        // Testing the select function with valid and invalid array indexes
        claim(testTable.select(1) == true);
        claim(testTable.select(2) == true);
        claim(testTable.select(0) == false);
        claim(testTable.select(3) == false);

        // Test for setPrintWidth
        claim(Arrays.equals(testTable.setPrintWidth(1), new int[]{3, 5, 5, 6}));
        claim(Arrays.equals(testTable.setPrintWidth(2), new int[]{3, 6, 13, 6}));

        // Testing the update function with valid and invalid array indexes, then test the update
        // has occured correctly
        claim(testTable.update(2, r4) == true);
        claim(testTable.update(3, r4) == false);
        claim(testTable.update(0, r4) == false);
        claim(testTable.records.get(2).getElement(1).equals(r4.getElement(1)));
        claim(testTable.records.get(2).getElement(2).equals(r4.getElement(2)));

        // Testing the delete function with valid and invalid array indexes
        claim(testTable.delete(2) == true);
        claim(testTable.delete(1) == true);
        claim(testTable.delete(0) == false);
        // Testing that records have been properly removed
        claim(testTable.select(1) == false);
        claim(testTable.select(2) == false);
    }

    public static void main(String[] args) {
        Table t = new Table(new String[]{"Id", "Name", "Kind", "Owner"});
        t.test();
        System.out.println("Tests pass.");
    }
}
