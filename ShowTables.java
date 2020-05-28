import java.io.RandomAccessFile;

public class ShowTables {

	public static void showTables() {
		System.out.println("Executing SHOW TABLES command");

		// changed tableName to tblName.
		String tblName = "davisbase_tables";
		// changed columns to cols.
		String[] cols = { "table_name" };
		// changed comparator to cmp.
		String[] cmp = new String[0];
		select(tblName, cols, cmp);
	}

	public static void select(String tblName, String[] cols, String[] cmp) {
		try {

			RandomAccessFile file = new RandomAccessFile("data/" + tblName + ".tbl", "rw");
			// changed columnNames to colNames.
			String[] colNames = Table.getColumnNames(tblName);
			// changed dataTypes to dTypes.
			String[] dTypes = Table.getDataType(tblName);

			Payload payload = new Payload();

			Table.filter(file, cmp, colNames, dTypes, payload);
			payload.display(cols);
			file.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
