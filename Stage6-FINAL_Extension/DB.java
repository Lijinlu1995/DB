/* The DB class is a generic class for storing a database.
The database holds a collection of tables.
And also add part of textual interface function*/
import java.io.File;
import java.util.*;
public class DB {
    private String dbName;
    private List<Table> tables;
    Catalogs catalogs;

    DB(String name)
    {
        dbName = name;
        catalogs = new Catalogs(dbName);
        tables = new ArrayList<>();
        loadTable();
    }

    private Table findTable(String tableName)
    {
        for (Table table : tables) {
            if (table.getName().equals(tableName)){
                return table;
            }
        }
        return null;
    }

    // Load all database tables from this database path
    private void loadTable()
    {
        File DbFolder = new File("tableFiles/" + dbName);
        File[] tableFiles = DbFolder.listFiles();
        for (File file : tableFiles) { tables.add(loadTable(file, false)); }
        if (tableFiles.length == 0) {
            System.out.println("Database is empty, no tables to load!");
            System.out.println("You can create a new table [create + attributes],eg.create name grade");
        }
    }

    // Load one table from file
    private Table loadTable(File file, Boolean suppress)
    {
        String[] next;
        int highestId, recordLines;
        highestId = recordLines = 0;
        String tableName = file.getName();
        
        Files f = new Files(file, true);
        int pos = tableName.lastIndexOf(".");
        tableName = tableName.substring(0, pos);
        Table table = new Table(tableName, f.getNext(), true);
        
        while ((next = f.getNext()) != null)
        {
            int thisId = Integer.parseInt(next[0]);
            if (thisId > highestId) highestId = thisId;
            if (table.insert(next, true) == true) recordLines++;
        }

        table.setNextId(highestId + 1);
        if (suppress == false){
            System.out.println("Table [" + tableName + "] with "+ recordLines+ " records");
            System.out.println("You can also create a new table [create + attributes],eg.create name grade");
        }
        return table;
    }

    // Save all database tables from this database directory
    public void saveDbTables()
    {
        for (Table table : tables) { saveTable(table.getName(), table); }
    }

    // Save one table to the filename specified
    public void saveTable(String tableName, Table table)
    {
        String filePath = "tableFiles/" + dbName + "/" + tableName + ".txt";
        File file = new File(filePath);
        Files fSave = new Files(file, false);
        table.checkId();

        for (int i = 0; i < table.getSize(); i++) {
            String[] record = table.getRecord(i);
            fSave.saveFiles(record);
        }
        System.out.println("Table saved: " + tableName);
    }


    // For the given input table name print out a formatted table of all records
    public List<String[]> getTableRecords(String tableName)
    {
        Table table = findTable(tableName);
        if (table == null) return null;

        List<String[]> printData = new ArrayList<>();
        for (int i = 0; i < table.getSize(); i++) {
            printData.add(table.select(i));
        }
        return printData;
    }

    // Returns a string array containing all the names of tables within this database
    public String[] getTableList()
    {
        String[] nameList = new String[tables.size()];

        for (int i = 0; i < tables.size(); i++) {
            nameList[i] = tables.get(i).getName();
        }
        return nameList;
    }

    // return true if the table is in the current database
    public Boolean tableExists(String name)
    {
        String[] tableList = getTableList();

        for (String table : tableList) {
            if (table.equals(name)) return true;
        }

        System.out.println("Table \"" + name + "\" does not exist.");
        return false;
    }

    // Remove the table from the tables list and delete the file from the database
    private void deleteTable(String name)
    {
        Table table = findTable(name);
        tables.remove(table);

        try {
            File file = new File("tableFiles/" + dbName + "/" + name + ".txt");
            file.delete();
            System.out.println(name + ".txt has been deleted.");
        }
        catch (SecurityException e) {
            System.out.println("File cannot be deleted.");
        }

    }

    // Retrieve the print data from the table at the top of the query stack
    public List<String[]> queryTable() { return catalogs.getTable(); }

    // users through query key words to call the certain function
    public Boolean query(String[] query)
    {
        String inputs = query[0];
        String[] otherWords = Arrays.copyOfRange(query, 1, query.length);

        if (inputs.equals("load"))
        {
            if (tableExists(query[1])) {
                File file = new File("tableFiles/" + dbName + "/" + query[1] + ".txt");
                catalogs.load(loadTable(file, true));
            }
        }
        else if (query[0].equals("save")) {
            saveTable(query[1], catalogs.save());
            File file = new File("tableFiles/" + dbName + "/" + query[1] + ".txt");
            tables.add(loadTable(file, true));
            return false;
        }
        else if (inputs.equals("delete")) {
            if (tableExists(query[1])) deleteTable(query[1]);
            return false;
        }
        else if (inputs.equals("clear")) { catalogs.clear(); return false; }
        else if (inputs.equals("create")) catalogs.create(otherWords, false);
        else if (inputs.equals("add")) catalogs.add(otherWords);
        else if (inputs.equals("rename")) catalogs.rename(otherWords);

        return true;
    }
}
