/* Provides functionality for saving and loading tables to/from file. A new FileIO object is
 * required for each save/load. */
import java.io.*;
import java.util.*;
public class Files {
    private File file;
    private List<String[]> fileLines;
    private int lineCount;

    Files(File file, Boolean loading)
    {
        this.file = file;

        // Load the file data into fileLines if  loading a table
        if (loading == true)
        {
            fileLines = new ArrayList<>();
            lineCount = 0;
            loadFromFile();
        }
        // Empty the table file if saving a table
        else {
            try {
                new PrintWriter(file).close();
            }
            catch (IOException e) { System.out.println("File not found!"); }
        }
    }

    // Load all the lines from the file into the fileLines list.
    public void loadFromFile()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line = "";

            while ((line = br.readLine()) != null)
            {
                String[] lineSplit = line.split(",");
                fileLines.add(lineSplit);
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to load file: " + file.getName());
        }
    }

    // If there are still lines left, return the next line. Otherwise return null
    public String[] getNext()
    {
        if (lineCount > fileLines.size() - 1) return null;
        else if (fileLines.get(lineCount)[0].equals("")) return null;
        else return fileLines.get(lineCount++);
    }

    // Append the line to the file being saved to
    public Boolean saveLineToFile(String[] line)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true)))
        {
            String txtLine = "";
            for (int i = 0; i < line.length; i++) { txtLine += line[i] + ","; }
            txtLine = txtLine.substring(0, txtLine.length() - 1);

            bw.append(txtLine);
            bw.newLine();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("Failed to save file: " + file.getName());
            return false;
        }
    }

    static void claim(boolean b) { if (!b) throw new Error("Test failure"); }
    
    // Testing function for FileIO, run when claimions are enabled
    private static void test()
    {
        // Test file and test String arrays for saving and loading
        File file = new File("test.txt");
        String[] testString1 = new String[]{"Id","Name","Kind","Owner"};
        String[] testString2 = new String[]{"1","Fido","dog","ab123"};
        String[] testString3 = new String[]{"2","Wanda","Hippopotamus","ef789"};

        // Testing FileIO with saving
        Files sFile = new Files(file, false);
        claim(sFile.saveLineToFile(testString1) == true);
        claim(sFile.saveLineToFile(testString2) == true);
        claim(sFile.saveLineToFile(testString3) == true);

        // Testing FileIO with loading
        Files lFile = new Files(file, true);
        claim(lFile.fileLines.size() == 3);
        claim(lFile.lineCount == 0);
        claim(Arrays.equals(lFile.getNext(), testString1) == true);
        claim(Arrays.equals(lFile.getNext(), testString2) == true);
        claim(Arrays.equals(lFile.getNext(), testString3) == true);
        claim(lFile.lineCount == 3);
        claim(lFile.getNext() == null);
        claim(lFile.getNext() == null);
        claim(lFile.lineCount == 3);

        // Clean-up test file at the end
        try { file.delete(); }
        catch (SecurityException e) {
            System.out.println("Error deleting test file, SecurityException.");
        }
    }

    public static void main(String[] args) {
        File file = new File("test.txt");
        Files f = new Files(file,false);
        f.test();
        System.out.println("Tests pass.");
    }
}
