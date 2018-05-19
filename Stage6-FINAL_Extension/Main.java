/* Main class which controls everything
   How to run:  javac Main.java
                java Main
*/
import java.io.File;
import java.util.Arrays;

public class Main {
    String[] dbList;
    DB db;
    Boolean initial = false;

    public static void main(String[] args)
    {
        Main program = new Main();
        program.run();
    }

    private void run()
    {
        Catalogs parse = new Catalogs();
        String dbName;
        String[] query;
        getDatabaseList();
        while ((dbName = parse.parseName()).equals("quit") != true)
        {
            if (inList(dbName) != true) {
                System.out.println("Database does not exist!");
                Boolean yes;
                do { System.out.println("Do you want to create a new database with this name (Y/N)?"); }
                while ((yes = parse.yesNo()) == null);
                if (yes == true) createDatabase(dbName);
            }
            else {
                db = new DB(dbName);
                Boolean quit = false;
                do {
                    query = parse.nextQuery();
                    if (query == null) System.out.println("Invalid input, please try again.");
                    else {
                        // Check if the user has attempted to quit
                        if (query[0].equals("quit")) quit = true;
                        else quit = false;
                        if (quit == false) performQuery(query);
                    }
                }
                while (quit == false);
                db.saveDbTables();
            }
            getDatabaseList();
        }
        System.out.println("Bye!");
    }

    // Create a new database folder
    private void createDatabase(String newDbName)
    {
        Boolean success = new File("tableFiles/" + newDbName).mkdir();

        if (success == true) {
            System.out.println("New database " + newDbName + " created.");
            System.out.println("You can add the record by command: [add + attribute value] eg.add Tom 70");
            System.out.println("You can also save this table by command:[save + table name] eg.save Grade");
        }
        else System.out.println("Database folder cannot be created.");
    }

    // Perform a query inputted by the user.
    private void performQuery(String[] query)
    {
        String input = query[0];
        // "list" query
        if      (input.equals("list")) listTables();
            // "show" query
        else if (input.equals("show")) {
            if (db.tableExists(query[1])) showTable(query[1]);
        }
        else {
            Boolean print = db.query(query);

            // For these queries reprint the updated table after completing the query
            if (print == true && (input.equals("load") || input.equals("create")
                    || input.equals("add") || input.equals("rename"))) {
                Print tp = new Print(initial);
                tp.printTable(db.queryTable());
            }
        }
    }

    // Checks if the database name specified currently exists.
    private Boolean inList(String dbName)
    {
        for (int i = 0; i < dbList.length; i++) {
            if (dbName.equals(dbList[i])) return true;
        }
        return false;
    }

    // Print out the specified table from the database
    private void showTable(String tName)
    {
        Print tp = new Print(initial);
        tp.printTable(db.getTableRecords(tName));
    }

    // Print out a list of all the tables in the current database
    private void listTables()
    {
        System.out.println(Arrays.toString(db.getTableList()));
    }

    // Create a string array of all the databases currently in the directory
    private void getDatabaseList()
    {
        File databases = new File("tableFiles");
        File[] dbFolders = databases.listFiles();
        dbList = new String[dbFolders.length];
        for (int i = 0; i < dbFolders.length; i++) {
            dbList[i] = dbFolders[i].getName();
        }
        System.out.println("\nWelcome to databases:");
        System.out.println(Arrays.toString(dbList));
        System.out.println("\nPlease enter the database name you want to load:");
        System.out.println("or you can enter a new database name you want to create");
    }
}
