/* Main class which controls everything
   How to run:  javac DB.java
                java DB
*/
public class DB {
    public static void main(String[] args)
    {
        DB db = new DB();
        Record record1 = new Record(new String[]{"1", "Fido", "dog", "ab123"});
        Record record2 = new Record(new String[]{"2", "Wanda", "fish", "ef789"});
        Table table = new Table(new String[]{"Id", "Name", "Kind", "Owner"});
        table.insert(record1);
        table.insert(record2);
        table.select(1);
        table.select(2);
        table.delete(1);
        table.select(1);
    }
}
