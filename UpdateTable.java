import java.io.RandomAccessFile;

public class UpdateTable {
	public static void parseUpdateString(String updateCommand) {
		
		System.out.println("Update Command :\"" + updateCommand + "\"");
		// updateString changed to updateCommand 
		// token changed to parsed_cmd
		String[] parsed_cmd = updateCommand.split(" ");
		String table = parsed_cmd[1];
		String[] temp1 = updateCommand.split("set");
		String[] temp2 = temp1[1].split("where");
		String cmpTemp = temp2[1];
		String setTemp = temp2[0];
		String[] cmp = Parser.parserEquation(cmpTemp);
		String[] set = Parser.parserEquation(setTemp);
		if (!DavisbaseUtility.tablePresent(table)) {
			System.out.println(table + " table not present.");
		} else {
			update(table, cmp, set);
		}

	}

	public static void update(String table, String[] cmp, String[] set) {
		try {

			int key = new Integer(cmp[2]);

			RandomAccessFile file = new RandomAccessFile("data/" + table + ".tbl", "rw");
			// changed numPages to numOfPg.
			int numOfPg = Table.pages(file);
			//changed page to pg.
			int pg = 0;
			for (int p = 1; p <= numOfPg; p++)
				if (Page.hasKey(file, p, key) & Page.getPageType(file, p) == 0x0D) {
					pg = p;
				}

			if (pg == 0) {
				System.out.println("The given key value does not exist");
				return;
			}

			int[] keys = Page.getKeyArray(file, pg);
			int x = 0;
			for (int i = 0; i < keys.length; i++)
				if (keys[i] == key)
					x = i;
			int offset = Page.getCellOffset(file, pg, x);
			//changed loc to location.
			long location = Page.getCellLoc(file, pg, x);

			String[] cols = Table.getColumnNames(table);
			String[] values = Table.retrieveValues(file, location);

			String[] type = Table.getDataType(table);
			for (int i = 0; i < type.length; i++)
				if (type[i].equals("DATE") || type[i].equals("DATETIME"))
					values[i] = "'" + values[i] + "'";

			for (int i = 0; i < cols.length; i++)
				if (cols[i].equals(set[0]))
					x = i;
			values[x] = set[2];

			String[] nullable = Table.getNullable(table);
			for (int i = 0; i < nullable.length; i++) {
				if (values[i].equals("null") && nullable[i].equals("NO")) {
					System.out.println("NULL-value constraint violation");
					return;
				}
			}

			byte[] stc = new byte[cols.length - 1];
			int plsize = Table.getPayloadSize(table, values, stc);
			Page.updateLeafCell(file, pg, offset, plsize, key, stc, values);

			file.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
