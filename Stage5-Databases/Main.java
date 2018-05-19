/* Main class which controls everything
   How to run:  javac Main.java
                java Main
*/
import java.io.File;
import java.util.Arrays;

public class Main {
    DB db;
    Boolean basicPrint = true;

    public static void main(String[] args)
    {
        Main program = new Main();

        program.run();
    }

    private void run()
    {
        getDatabaseList();
        // Load database tables from file and print out both tables
        db = new DB("Pets");
        listTables();
        showTable("animals");
        showTable("owners");
    }

    // Print out the specified table from the database
    private void showTable(String tName)
    {
        Print tp = new Print(basicPrint);
        tp.printTable(db.getTableRecords(tName));
    }

    // Print out a list of all the tables in the current database
    private void listTables()
    {
        System.out.println("Table List:");
        System.out.println(Arrays.toString(db.getTableList()));
    }

    // Create a string array of all the databases currently in the directory
    private void getDatabaseList()
    {
        File databases = new File("tableFiles");
        File[] dbFolders = databases.listFiles();
        String[] folderNames = new String[dbFolders.length];

        for (int i = 0; i < dbFolders.length; i++)
        {
            folderNames[i] = dbFolders[i].getName();
        }

        System.out.println("Welcome! Please select a database to start:");
        System.out.println(Arrays.toString(folderNames));
    }
}
