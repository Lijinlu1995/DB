/*Store the record string in an ArrayList,which can be
wrapped up as a table in next stag*/
import java.util.ArrayList;
import java.util.List;

public class Record {
    //a list to collect the string of records
    private List<String> row;
    Record(String[] inputs) {
        row = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            row.add(inputs[i]);
        }
    }

    public String getElement(int i) {
        try {
            return row.get(i);
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid column index specified.");
            return "ERROR";
        }
    }

    public int getSize() {
        return row.size();
    }

    // check whether two records are same
    public boolean equals(Record other)
    {
        if (this.getSize() != other.getSize()) return false;
        for (int i = 0; i < this.getSize(); i++) {
            if (!this.getElement(i).equals(other.getElement(i))) return false;
        }

        return true;
    }

    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }

    // Tests for the Record class only called when Record is run by itself
    private static void test()
    {
        // Two test records for testing the other functions
        Record r1 = new Record(new String[]{"1", "Fido", "dog", "ab123"});
        Record r2 = new Record(new String[]{"ab123", "Jo"});
        //Test the getElement() with the correct string
        claim(r1.getElement(0) == "1");
        claim(r1.getElement(1) == "Fido");
        claim(r1.getElement(2) == "dog");
        claim(r1.getElement(3) == "ab123");
        claim(r2.getElement(0) == "ab123");
        claim(r2.getElement(1) == "Jo");
        // Test invalid inputs for getElement() whether can catch IndexOutOfBoundsException
        claim(r1.getElement(-1) == "ERROR");
        claim(r1.getElement(4) == "ERROR");
        claim(r2.getElement(-1) == "ERROR");
        claim(r2.getElement(2) == "ERROR");
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
