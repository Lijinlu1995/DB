/* Provides functionality for saving and loading tables to/from file. A new FileIO object is
required for each save/load.
self test: javac Files.java
           java  Files
If it has no throw error the self test pass*/
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
        if (loading == true) {
            fileLines = new ArrayList<>();
            lineCount = 0;
            loadFiles();
        }
        // Empty the table file if saving a table
        else {
            try { new PrintWriter(file).close(); }
            catch (IOException e) { System.out.println("File not found!"); }
        }
    }

    // Load all the lines from the file into the fileLines list.
    public void loadFiles()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");
                fileLines.add(lineSplit);
            }
        }
        catch (IOException e) {
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
    public Boolean saveFiles(String[] line)
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
    
    private static void test()
    {
        // Test file and test String arrays for saving and loading
        File file = new File("test.txt");
        String[] testString1 = new String[]{"Id","Name","Kind","Owner"};
        String[] testString2 = new String[]{"1","Fido","dog","ab123"};
        String[] testString3 = new String[]{"2","Wanda","Hippopotamus","ef789"};

        // Testing save in the files
        Files file1 = new Files(file, false);
        claim(file1.saveFiles(testString1) == true);
        claim(file1.saveFiles(testString2) == true);
        claim(file1.saveFiles(testString3) == true);

        // Testing with loading
        Files file2 = new Files(file, true);
        claim(file2.fileLines.size() == 3);
        claim(file2.lineCount == 0);
        claim(Arrays.equals(file2.getNext(), testString1) == true);
        claim(Arrays.equals(file2.getNext(), testString2) == true);
        claim(Arrays.equals(file2.getNext(), testString3) == true);
        claim(file2.lineCount == 3);
        claim(file2.getNext() == null);
        claim(file2.getNext() == null);
        claim(file2.lineCount == 3);

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
