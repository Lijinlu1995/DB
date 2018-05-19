/*This class is to manage of the user query command,
and achieve a simple textual interface like the catalogs function*/
import java.util.*;
public class Catalogs {
    private Stack<Table> catalogs;
    private String dbName;

    Catalogs(String dbName)
    {
        catalogs = new Stack<>();
        this.dbName = dbName;
    }
    Catalogs(){ }

    // Return a list of string arrays of the table from the top of the stack
    public List<String[]> getTable()
    {
        Table table = catalogs.peek();
        if (table == null) return null;
        List<String[]> printData = new ArrayList<>();
        for (int i = 0; i < table.getSize(); i++) {
            printData.add(table.select(i));
        }
        return printData;
    }

    // Commands that are valid by themselves
    private static final String[] singleCommands ={"list","quit","clear"};
    // Commands that must be followed by one column or table names
    private static final String[] doubleCommands ={
            "load", "show", "save", "delete"
    };
    // Commands that must be followed by multiple column or table names
    private static final String[] multiCommands ={"add","create","rename"};

    // Scan in the name of a table or database from the user.
    public String parseName()
    {
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine().trim();
        if (name.split(" ").length == 1) return name;
        return null;
    }

    // Scan in a table query from the user.
    public String[] nextQuery()
    {
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();
        String[] words = line.split(" ");

        for (String keyword : singleCommands){
            if (words[0].equals(keyword)){
                if (words.length == 1) return words;
                else return null;
            }
        }
        for (String keyword : doubleCommands){
            if (words[0].equals(keyword)) {
                if (words.length > 1) return words;
                else return null;
            }
        }
        for (String keyword : multiCommands) {
            if (words[0].equals(keyword)) {
                if (words.length > 2) return words;
                else return null;
            }
        }
        return null;
    }

    // If the user inputs Y/y then return true, if N/n return false, else return null.
    public Boolean yesNo()
    {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim().toLowerCase();

        if (line.equals("y")) return true;
        else if (line.equals("n")) return false;
        return null;
    }

    public void load(Table table) { catalogs.push(table); }

    public Table save() { return catalogs.pop(); }

    // Empty the Stack
    public void clear()
    {
        catalogs.removeAllElements();
        System.out.println("Stack has been cleared.");
    }

    public void create(String[] attributeNames, Boolean suppress)
    {
        catalogs.push(new Table("temp", attributeNames, false));
        if (suppress == false) System.out.println("New table has been created.");
    }

    public void add(String[] attributes)
    {
        Table table = catalogs.peek();
        if (table.insert(attributes, false) == true) {
            System.out.println("Row added successfully.");
            System.out.println("You can also save this table by command:[save + table name] eg.save Grade");
        }
        else System.out.println("Error adding row, incorrect number of attributes");
    }

    public void rename(String[] rename)
    {
        Table table = catalogs.peek();
        int colIndex;

        if ((colIndex = table.columnExists(rename[0])) != -1) {
            String[] newAttributes = table.getRecord(0);
            newAttributes[colIndex] = rename[1];
            table.update(0, Arrays.copyOfRange(newAttributes, 1, newAttributes.length));
            System.out.println("Column renamed successfully.");
        }
        else {
            System.out.println("Column rename failed. Specified column not found.");
        }
    }
}
