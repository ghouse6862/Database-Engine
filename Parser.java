
public class Parser {
	
	public static void parseShowTable() {
		ShowTables.showTables();
	}
	
	public static void parseCreateTable(String userCommand) {
		CreateTable.parseCreateString(userCommand);
	}
	
	public static void parseInsert(String userCommand) {
		Insert.parseinsertCommand(userCommand);
	}
	
	public static void parseDelete(String userCommand) {
		DeleteTable.parseDeleteString(userCommand);
	}
	
	public static void parseUpdateTable(String userCommand) {
		UpdateTable.parseUpdateString(userCommand);
	}
	
	public static void parseSelect(String userCommand) {
		parseQueryString(userCommand);
	}
	
	public static void parseDropTable(String userCommand) {
		DropTable.dropTable(userCommand);
	}
	
	public static void parseQueryString(String queryString) {
		
		System.out.println("STUB: Calling the method to process the command");
		System.out.println("Parsing the string:\"" + queryString + "\"");

		String[] cmp;
		String[] column;
		String[] selectQuery = queryString.split("where");

		if(selectQuery.length > 1){
			String whereClause = selectQuery[1].trim();
			cmp = parserEquation(whereClause);
		} else {
			cmp = new String[0];
		}

		String[] select = selectQuery[0].split("from");
		String tableName = select[1].trim();
		String cols = select[0].replace("select", "").trim();

		if(cols.contains("*")){
			column = new String[1];
			column[0] = "*";
		} else {
			column = cols.split(",");
			for(int i = 0; i < column.length; i++)
				column[i] = column[i].trim();
		}

		if(!DavisbaseUtility.tablePresent(tableName)){
			System.out.println("Table "+tableName+" does not exist.");
		} else {
			ShowTables.select(tableName, column, cmp);
		}
	}
	
	public static String[] parserEquation(String equation){
		String comparator[] = new String[3];
		String temp[] = new String[2];

		if(equation.contains("=")) {
			temp = equation.split("=");
			comparator[0] = temp[0].trim();
			comparator[1] = "=";
			comparator[2] = temp[1].trim();

		} else if(equation.contains("<")) {
			temp = equation.split("<");
			comparator[0] = temp[0].trim();
			comparator[1] = "<";
			comparator[2] = temp[1].trim();

		} else if(equation.contains(">")) {
			temp = equation.split(">");
			comparator[0] = temp[0].trim();
			comparator[1] = ">";
			comparator[2] = temp[1].trim();

		} else if(equation.contains("<=")) {
			temp = equation.split("<=");
			comparator[0] = temp[0].trim();
			comparator[1] = "<=";
			comparator[2] = temp[1].trim();

		} else if(equation.contains(">=")) {
			temp = equation.split(">=");
			comparator[0] = temp[0].trim();
			comparator[1] = ">=";
			comparator[2] = temp[1].trim();
		}
		return comparator;
	}
}
