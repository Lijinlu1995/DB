/* Store table in database. The table is contains a List of records,
and all records must have the same number of attributes.
Functions are supplied for adding, removing and selected records.*/
import java.util.*;
public class Table {
    // A List stores the table records, the first record in the list holds the attribute names.
    private List<Record> records;
    private String tableName;
    private int numColumns;
    private int nextId;

    // Initialise empty table with the input column headings
    Table(String name, String[] attributeNames, Boolean load)
    {
        tableName = name;
        if (load == true) numColumns = attributeNames.length;
        else              numColumns = attributeNames.length + 1;
        nextId = 0;
        records = new ArrayList<>();

        insert(attributeNames, load);
    }

    // Return the name of the table as a string.
    public String getName() { return tableName; }

    // Return the number of records in the table.
    public int getSize() { return records.size(); }

    // Return the record at the given index as an array of strings.
    public String[] getRecord(int index)
    {
        Record record = records.get(index);
        String[] output = new String[record.getSize()];
        for (int i = 0; i < record.getSize(); i++) output[i] = record.getElement(i);
        return output;
    }

    // Set the value of the next Id to be auto generated.
    public void setNextId(int nextId) { this.nextId = nextId; }

    // Used to determine if two records are the same
    public boolean equals(Table other)
    {
        if (this.records.size() != other.records.size()) return false;
        else if (this.nextId != other.nextId) return false;

        for (int i = 0; i < this.records.size(); i++)
        {
            Record thisRecord = this.records.get(i);
            Record otherRecord = other.records.get(i);
            if (thisRecord.equals(otherRecord) == false) return false;
        }
        return true;
    }

    // Returns the next record Id as a string and increment nextId
    private String autoId()
    {
        String newId = String.valueOf(nextId);
        nextId++;
        return newId;
    }

    // Insert the new record into the table if it has the correct number of attributes.
    // If loading the input should have an ID
    public Boolean insert(String[] inputStrings, Boolean load)
    {
        if ((load == true  && inputStrings.length == numColumns) ||
                (load == false && inputStrings.length + 1 == numColumns))
        {
            if (load == false)
            {
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

    // Remove the record from the table at the given index, return true if index is valid.
    // Index 0 is the attribute row so is invalid.
    public Boolean delete(int index)
    {
        if (index == 0) return false;
        try { records.remove(index); }
        catch (IndexOutOfBoundsException e) { fatalError(e); }
        return true;
    }

    // Update the record from the table at the given index with the new record, return true if
    // index is valid. Input array must be 1 smaller than records, since autoID cannot be changed.
    public Boolean update(int index, String[] inputStrings)
    {
        if (inputStrings.length == numColumns - 1)
        {
            try {
                Record thisRecord = records.get(index);

                for (int i = 1; i < inputStrings.length; i++)
                {
                    thisRecord.setElement(i, inputStrings[i - 1]);
                }
                return true;
            }
            catch (IndexOutOfBoundsException e) { fatalError(e); }
        }
        return false;
    }

    // Returns the record and the given index as a String array.
    public String[] select(int index)
    {
        Record thisRecord = records.get(index);
        String[] output = new String[thisRecord.getSize()];

        try {
            for (int i = 0; i < thisRecord.getSize(); i++)
            {
                output[i] = thisRecord.getElement(i);
            }
        }
        catch(IndexOutOfBoundsException e) { fatalError(e); }

        return output;
    }

    // Fatal error is called when an exception occurs in Table. These exceptions should be
    // avoided by the classes that use Table (i.e. select should only be called on valid indexes).
    private void fatalError(Exception e)
    {
        e.printStackTrace();
        System.exit(1);
    }
    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    // Tests for the Table class only called when run by itself
    private static void test()
    {

        // Two test records for testing the table
        String[] attributeNames = {"Name", "Kind", "Owner"};
        String[] recordStr1 = {"Fido", "dog", "ab123"};
        String[] recordStr2 = {"Wanda", "hippopotamus", "ef789"};
        String[] recordStr3 = {"Garfield", "Cat", "ab123"};
        String[] recordStr4 = {"ab123", "Jo"};
        String[] recordStr5 = {"cd456", "Sam"};

        // Test table for testing the other functions (with load = false, so auto IDs generated)
        Table testTable = new Table("Pets", attributeNames, false);

        // Testing the insert function with valid and invalid input (wrong number of attributes)
        claim(testTable.insert(recordStr1, false) == true);
        claim(testTable.insert(recordStr2, false) == true);
        claim(testTable.insert(recordStr4, false) == false);

        // Testing the select function with different index selections
        claim(Arrays.equals(testTable.select(0), new String[]{"ID","Name","Kind","Owner"}));
        claim(Arrays.equals(testTable.select(1), new String[]{"0","Fido", "dog", "ab123"}));
        claim(Arrays.equals(testTable.select(2), new String[]{"1","Wanda", "hippopotamus", "ef789"}));

        // Testing the update function with valid and invalid array indexes, then test the update
        // has occured correctly (same ID and elements are equal to the new string values)
        claim(testTable.update(2, recordStr3) == true);
        claim(testTable.update(2, recordStr4) == false);

        claim(testTable.records.get(2).getElement(0).equals("1"));
        claim(testTable.records.get(2).getElement(1).equals(recordStr3[0]));
        claim(testTable.records.get(2).getElement(2).equals(recordStr3[1]));

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
