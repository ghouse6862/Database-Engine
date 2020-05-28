public class Insert {
	public static void parseinsertCommand(String insertCommand) {

		
		// insertString changed to insertCommand
		System.out.println("Command:\"" + insertCommand + "\"");

		// tokens changed to parsed command
		String[] parsed_cmd = insertCommand.split(" ");
		// table to table_name
		String table_name = parsed_cmd[2];
		
		String[] temp = insertCommand.split("values");
		// temporary changed to temp_arr
		String temp_arr = temp[1].trim();
		
		// values_to_insert to values_to_insert 
		String[] values_to_insert = temp_arr.substring(1, temp_arr.length() - 1).split(",");
		
		for (int j = 0; j < values_to_insert.length; j++)
			values_to_insert[j] = values_to_insert[j].trim();
		
		if (!DavisbaseUtility.tablePresent(table_name)) {
			System.out.println("Enter valid table name");
		} else {
			CreateTable.tableInsert(table_name, values_to_insert);
		}

	}

}
