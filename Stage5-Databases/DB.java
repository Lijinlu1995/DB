/* The DB class is a generic class for storing a database.
The database holds a collection of tables. */
import java.io.File;
import java.util.*;
public class DB {
    private String name;
    private List<Table> tables;

    DB(String name)
    {
        this.name = name;
        tables = new ArrayList<>();
        loadDbTables();
    }

    // Set currentTable to the correct table from Tables using the given table name
    private Table findTable(String tableName)
    {
        for (Table table : tables)
        {
            if (table.getName().equals(tableName))
            {
                return table;
            }
        }
        return null;
    }

    // Load all database tables from this database directory
    public void loadDbTables()
    {
        File DbFolder = new File("tableFiles/" + name);
        File[] tableFiles = DbFolder.listFiles();

        for (File file : tableFiles)
        {
            String tableName = file.getName();
            Files fLoad = new Files(file, true);

            int pos = tableName.lastIndexOf(".");
            tableName = tableName.substring(0, pos);
            Table table = new Table(tableName, fLoad.getNext(), true);

            String[] next;
            int highestId, validLines, invalidLines;
            highestId = validLines = invalidLines = 0;

            while ((next = fLoad.getNext()) != null)
            {
                int thisId = Integer.parseInt(next[0]);
                if (thisId > highestId) highestId = thisId;

                if (table.insert(next, true) == true) validLines++;
                else invalidLines++;
            }

            table.setNextId(highestId + 1);
            tables.add(table);
            System.out.println("Table added: " + tableName);
            System.out.println("Entries loaded: " + validLines + "\nFailed entries: " + invalidLines);
        }
    }

    // Save all database tables from this database directory
    public void saveDbTables()
    {
        for (Table table : tables)
        {
            String filePath = "tableFiles/" + name + "/" + table.getName() + ".txt";
            File file = new File(filePath);
            Files fSave = new Files(file, false);

            for (int i = 0; i < table.getSize(); i++)
            {
                String[] record = table.getRecord(i);
                fSave.saveLineToFile(record);
            }
            tables.add(table);
            System.out.println("Table saved: " + table.getName());
        }
    }

    // For the given input table name print out a formatted table of all records
    public List<String[]> getTableRecords(String tableName)
    {
        Table table = findTable(tableName);
        if (table == null) return null;

        List<String[]> printData = new ArrayList<String[]>();
        for (int i = 0; i < table.getSize(); i++)
        {
            printData.add(table.select(i));
        }
        return printData;
    }

    public String[] getTableList()
    {
        String[] nameList = new String[tables.size()];

        for (int i = 0; i < tables.size(); i++)
        {
            nameList[i] = tables.get(i).getName();
        }
        return nameList;
    }
}
