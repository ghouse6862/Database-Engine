import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DavisbaseUtility {
	
	
	// changed davisbase_tables to db_tables
	public static final String db_tables = "data/davisbase_tables.tbl";
	public static final String db_columns = "data/davisbase_columns.tbl";
	
	public static void printFile(String filename) {
		 BufferedReader buffer;
		try {
			buffer = new BufferedReader(new FileReader(filename));
			String line = null;
			 while ((line = buffer.readLine()) != null) {
			   System.out.println(line);
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}
	
	public static void print(String msg) {
		System.out.println(msg);
	}
	
	// changed checkTableExistence to tablePresent
	public static boolean tablePresent(String name_of_table){
		
		// tablename changed to name_of_table 
		name_of_table = name_of_table + ".tbl";

		try {
			File dataDir = new File("data");
			String[] tableNameList;
			tableNameList = dataDir.list();
			
			// oldTableName changed to tableNameList
			for (int i = 0; i < tableNameList.length; i++) {
				if(tableNameList[i].equals(name_of_table))
					return true;
			}
		} catch (Exception se) {
			System.out.println("Directory creation failed");
			System.out.println(se);
		}

		return false;
	}

}
