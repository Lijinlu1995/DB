/* Store table in database. The table is contains a List of records,
and all records must have the same number of attributes.
Functions are supplied for adding, removing and selected records.
self test: javac Table.java
           java  Table
If it has no throw error the self test pass*/
import java.util.*;
public class Table {
    // A List stores the table records.
    private List<Record> records;
    private String tableName;
    private int column;
    private int nextId;

    // Initialise empty table with the input column headings
    Table(String name, String[] attributeNames, Boolean load)
    {
        nextId = 0;
        records = new ArrayList<>();
        tableName = name;
        if (load == true) column = attributeNames.length;
        else              column = attributeNames.length + 1;
        insert(attributeNames, load);
    }

    // Return the name of the table as a string.
    public String getName() { return tableName; }

    // Return the count of records in the table.
    public int getSize() { return records.size(); }

    // Return the record at the given index as an array of strings.
    public String[] getRecord(int index)
    {
        Record record = records.get(index);
        String[] output = new String[record.getSize()];
        for (int i = 0; i < record.getSize(); i++) output[i] = record.getRecord(i);
        return output;
    }

    // Set the value of the next Id to be auto generated.
    public void setNextId(int nextId) { this.nextId = nextId; }

    // Returns the next record Id as a string and increment nextId
    private String autoId()
    {
        String newId = String.valueOf(nextId);
        nextId++;
        return newId;
    }

    // Insert the new record into the table if it has the correct number of attributes.
    public Boolean insert(String[] inputStrings, Boolean load)
    {
        if ((load == true  && inputStrings.length == column) || 
                (load == false && inputStrings.length + 1 == column)) {
            if (load == false) {   
                // If loading the input should have an ID
                List<String> addId = new ArrayList<>(Arrays.asList(inputStrings));
                if (records.size() == 0) addId.add(0, "ID");
                else                     addId.add(0, autoId());
                inputStrings = addId.toArray(new String[addId.size()]);
            }
            records.add(new Record(inputStrings));
            return true;
        }
        else return false;
    }

    // Remove record from the table index 0 is the attribute which is invalid.
    public Boolean delete(int index)
    {
        if (index == 0) return false;
        try { records.remove(index); }
        catch (IndexOutOfBoundsException e) { error(e); }
        return true;
    }

    // Update the record and input array must be smaller than records.
    public Boolean update(int index, String[] inputStrings)
    {
        if (inputStrings.length == column - 1) {
            try {
                Record thisRecord = records.get(index);
                for (int i = 1; i < inputStrings.length; i++) {
                    thisRecord.setRecord(i, inputStrings[i - 1]);
                }
                return true;
            }
            catch (IndexOutOfBoundsException e) { error(e); }
        }
        return false;
    }

    // Returns the record for output.
    public String[] select(int index)
    {
        Record thisRecord = records.get(index);
        String[] output = new String[thisRecord.getSize()];

        try {
            for (int i = 0; i < thisRecord.getSize(); i++) {
                output[i] = thisRecord.getRecord(i);
            }
        }
        catch(IndexOutOfBoundsException e) { error(e); }

        return output;
    }

    // Checks whether the table has an ID column,if not add one.
    public void checkId()
    {
        if (records.get(0).getRecord(0).equals("ID") != true) {
            setNextId(0);
            records.get(0).addRecord(0, "ID");
            for (int i = 1; i < records.size(); i++) {
                records.get(i).addRecord(0, autoId());
            }
        }
    }

    // Returns column index, returns -1 if it does not exist
    public int columnExists(String colName)
    {
        for (int i = 0; i < records.get(0).getSize(); i++) {
            if (colName.equals(records.get(0).getRecord(i))) return i;
        }
        return -1;
    }

    private void error(Exception e)
    {
        e.printStackTrace();
        System.exit(1);
    }
    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    // Tests for the Table class only called when run by itself
    private static void test()
    {

        //test records for testing the table
        String[] attributeNames = {"Name", "Kind", "Owner"};
        String[] r1 = {"Fido", "dog", "ab123"};
        String[] r2 = {"Wanda", "hippopotamus", "ef789"};
        String[] r3 = {"Garfield", "Cat", "ab123"};
        String[] r4 = {"ab123", "Jo"};

        Table testTable = new Table("Pets", attributeNames, false);

        // Testing the insert function with valid and invalid input
        claim(testTable.insert(r1, false) == true);
        claim(testTable.insert(r2, false) == true);
        claim(testTable.insert(r4, false) == false);

        // Testing the select function with different index selections
        claim(Arrays.equals(testTable.select(0), new String[]{"ID","Name","Kind","Owner"}));
        claim(Arrays.equals(testTable.select(1), new String[]{"0","Fido", "dog", "ab123"}));
        claim(Arrays.equals(testTable.select(2), new String[]{"1","Wanda", "hippopotamus", "ef789"}));

        // Testing the update function with valid and invalid array indexes and the records
        claim(testTable.update(2, r3) == true);
        claim(testTable.update(2, r4) == false);

        claim(testTable.records.get(2).getRecord(0).equals("1"));
        claim(testTable.records.get(2).getRecord(1).equals(r3[0]));
        claim(testTable.records.get(2).getRecord(2).equals(r3[1]));

        // Testing the delete function with valid and invalid array indexes
        claim(testTable.delete(2) == true);
        claim(testTable.delete(1) == true);
        claim(testTable.delete(0) == false);

        // Testing that the records have been properly removed
        claim(testTable.records.size() == 1);
    }


    public static void main(String[] args) {
        Table t = new Table("animal", new String[]{"Id", "Name", "Kind", "Owner"},false);
        t.test();
        System.out.println("Tests pass.");
    }
}
