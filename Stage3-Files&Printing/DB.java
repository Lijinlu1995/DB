/* Main class which controls everything
   How to run:  javac DB.java
                java DB
*/
public class DB {
    public static void main(String[] args)
    {
        // Load table from file and print all the records
        Table t = new Table("animal");
        t.select(new int[]{0});
    }
}
