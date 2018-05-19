/*Store the record string in an ArrayList,which can be
wrapped up as a table in database
self test: javac Record.java
           java  Record
If it has no throw error the self test pass*/
import java.util.*;

public class Record {
    //a list to collect the string of records
    private List<String> record;
    Record(String[] inputs)
    {
        record = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            record.add(inputs[i]);
        }
    }

    public String getRecord(int i) {
        try {
            return record.get(i);
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid column index specified.");
            return "ERROR";
        }
    }

    // Update the records at the given index with the input string.
    public Boolean setRecord(int index, String update)
    {
        try { record.set(index, update); }
        catch (IndexOutOfBoundsException e) { error(e); }
        return true;
    }

    // Update the records at the given index with the input string.
    public Boolean addRecord(int index, String Record)
    {
        try { record.add(index, Record); }
        catch (IndexOutOfBoundsException e) { error(e); }
        return true;
    }

    public void removeRecord(int index)
    {
        record.remove(index);
    }

    public int getSize() {
        return record.size();
    }

    private void error(Exception e) { e.printStackTrace();System.exit(1); }

    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    // Tests for the Record class only called when Record is run by itself
    private static void test()
    {
        // Two test records for testing the other functions
        Record r1 = new Record(new String[]{"1", "Fido", "dog", "ab123"});
        Record r2 = new Record(new String[]{"ab123", "Jo"});
        //Test the getRecord() with the correct string
        claim(r1.getRecord(0) == "1");
        claim(r1.getRecord(1) == "Fido");
        claim(r1.getRecord(2) == "dog");
        claim(r1.getRecord(3) == "ab123");
        claim(r2.getRecord(0) == "ab123");
        claim(r2.getRecord(1) == "Jo");
        // Test invalid inputs for getRecord() whether can catch IndexOutOfBoundsException
        claim(r1.getRecord(-1) == "ERROR");
        claim(r1.getRecord(4) == "ERROR");
        claim(r2.getRecord(-1) == "ERROR");
        claim(r2.getRecord(2) == "ERROR");
        // Test the getSize() function
        claim(r1.getSize() == 4);
        claim(r2.getSize() == 2);
    }

    public static void main(String[] args) {
        Record r = new Record(new String[]{"Id", "Name", "Kind", "Owner"});
        r.test();
        System.out.println("Tests pass.");
    }
}
