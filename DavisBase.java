import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DavisBase {

	static String prompt = "davisql> ";
	static String copyright = "Team Maroon";
	static String version = "v1.00";
	static boolean isExit = false;

	public static int pageSize = 512;

	static Scanner scanner = new Scanner(System.in).useDelimiter(";");


	public static void main(String[] args) {
		Init.init();

		splashScreen();

		String userCommand = ""; 

		while(!isExit) {
			userCommand = scanner.next().replace("\n", " ").replace("\r", "").trim().toLowerCase();
			parseUserCommand(userCommand);
		}
		System.out.println("Exiting...");

	}

	public static void splashScreen() {
		System.out.println(line("-",80));
		System.out.println("Welcome to DavisBase");
		System.out.println("DavisBase Version " + version);
		System.out.println(copyright);
		System.out.println("\nType \"help;\" to display supported commands.");
		System.out.println(line("-",80));
	}

	public static String line(String s,int num) {
		String a = "";
		for(int i=0;i<num;i++) {
			a += s;
		}
		return a;
	}

	public static void help() {
		System.out.println(line("*",80));
		System.out.println("SUPPORTED COMMANDS");
		System.out.println("All commands below are case insensitive");
		System.out.println();
		System.out.println("\tSHOW TABLES;                                                 Display all the tables in the database.");
		System.out.println("\tCREATE TABLE table_name (<column_name datatype>);            Create a new table in the database.");
		System.out.println("\tINSERT INTO table_name VALUES (value1,value2,..);            Insert a new record into the table.");
		System.out.println("\tDELETE FROM TABLE table_name WHERE row_id = key_value;       Delete a record from the table whose rowid is <key_value>.");
		System.out.println("\tUPDATE table_name SET column_name = value WHERE row_id = ..; Modifies the records in the table.");
		System.out.println("\tCREATE INDEX ON table_name (column_name);                    Create index for the specified column in the table");
		System.out.println("\tSELECT * FROM table_name;                                    Display all records in the table.");
		System.out.println("\tSELECT * FROM table_name WHERE column_name operator value;   Display records in the table where the given condition is satisfied.");
		System.out.println("\tDROP TABLE table_name;                                       Remove table data and its schema.");
		System.out.println("\tVERSION;                                                     Show the program version.");
		System.out.println("\tHELP;                                                        Show this help information.");
		System.out.println("\tEXIT;                                                        Exit DavisBase.");
		System.out.println();
		System.out.println();
		System.out.println(line("*",80));
	}

	public static void parseUserCommand (String userCommand) {

		ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));

		switch (commandTokens.get(0)) {

		case "show":
			System.out.println("STUB: Calling the method to process the command (SHOW)");
			Parser.parseShowTable();
			break;

		case "create":
			System.out.println("STUB: Calling the method to process the command (CREATE");
			Parser.parseCreateTable(userCommand);
			break;

		case "insert":
			System.out.println("STUB: Calling the method to process the command (INSERT)");
			Parser.parseInsert(userCommand);
			break;

		case "delete":
			System.out.println("STUB: Calling the method to process the command (DELETE)");
			Parser.parseDelete(userCommand);
			break;	

		case "update":
			System.out.println("STUB: Calling the method to process the command (UPDATE)");
			Parser.parseUpdateTable(userCommand);
			break;

		case "select":
			System.out.println("STUB: Calling the method to process the command (SELECT)");
			Parser.parseSelect(userCommand);
			break;

		case "drop":
			System.out.println("STUB: Calling the method to process the command (DROP)");
			Parser.parseDropTable(userCommand);
			break;	

		case "help":
			help();
			break;

		case "version":
			System.out.println("DavisBase Version " + version);
			System.out.println(copyright);
			break;

		case "exit":
			isExit=true;
			break;

		case "quit":
			isExit=true;
			break;

		default:
			System.out.println("I didn't understand the command: \"" + userCommand + "\"");
			System.out.println();
			break;
		}
	} 
}



