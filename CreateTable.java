import java.io.RandomAccessFile;

public class CreateTable {
	public static void parseCreateString(String createString) {

		System.out.println("CREATE METHOD");
		System.out.println("Parsing the string:\"" + createString + "\"");
		String[] parsed_command = createString.split(" ");

		// CHECKKKKKK - executes create index func. 
		if (parsed_command[1].compareTo("index") == 0) {
			String col = parsed_command[4];
			String colName = col.substring(1,col.length()-1);
			Index.createIndex(parsed_command[3],colName, "String");
			
		} else {

			// ----- parsed_command[1].compareTo("table")>0 ------
			if (parsed_command[1].compareTo("table") != 0){
				System.out.println("Wrong syntax");
			} else{

				String tableName = parsed_command[2];
				String[] temp = createString.split(tableName);
				String cols = temp[1].trim();
				String[] col_names = cols.substring(1, cols.length()-1).split(",");


				// Taking care of the spaces. Column names with spaces not allowed. 
				for(int i = 0; i < col_names.length; i++)
					col_names[i] = col_names[i].trim();

				if(DavisbaseUtility.tablePresent(tableName)) {
					System.out.println("Table " + tableName + " already exists.");
				} else {
					tableCreate(tableName, col_names);		
				}
			}
		}
	}
	
	public static void tableCreate(String table, String[] col){                                     //Create from DavisBase.java
		try{	

			RandomAccessFile file = new RandomAccessFile("data/" + table + ".tbl", "rw");				//creates .tbl file (table)
			file.setLength(Table.pageSize);	//512bytes										
			file.seek(0);				//seek first position
			file.writeByte(0x0D);		//Write
			file.close();

			file = new RandomAccessFile(DavisbaseUtility.db_tables, "rw");

			// ********* NOT SURE
			int numPages = Table.pages(file);
			int page = 1;
			for(int p = 1; p <= numPages; p++){
				int right = Page.getRightMost(file, p);
				if(right == 0)
					page = p;
			}


			int[] keys = Page.getKeyArray(file, page);
			int l = keys[0];
			for(int i = 0; i < keys.length; i++) {
				if(keys[i]>l)
					l = keys[i];
			}
			file.close();

			String[] values = {Integer.toString(l+1), table};
			tableInsert("davisbase_tables", values);

			file = new RandomAccessFile(DavisbaseUtility.db_columns, "rw");

			numPages = Table.pages(file);
			page=1;
			for(int p = 1; p <= numPages; p++){
				int right = Page.getRightMost(file, p);
				if(right == 0)
					page = p;
			}

			keys = Page.getKeyArray(file, page);
			l = keys[0];
			for(int i = 0; i < keys.length; i++) {
				if(keys[i]>l)
					l = keys[i];
			}
			file.close();

			for(int i = 0; i < col.length; i++) {
				l = l + 1;
				String[] tkn = col[i].split(" ");
				String col_name = tkn[0];
				String dt = tkn[1].toUpperCase();
				String position = Integer.toString(i+1);
				String nullable;
				if(tkn.length > 2)
					nullable = "NO";
				else
					nullable = "YES";
				String[] value = {Integer.toString(l), table, col_name, dt, position, nullable};
				tableInsert("davisbase_columns", value);
			}

		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void tableInsert(String table, String[] values){
		try{
			RandomAccessFile file = new RandomAccessFile("data/" + table + ".tbl", "rw");
			tableInsert(file, table, values);
			file.close();

		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void tableInsert(RandomAccessFile file, String table, String[] values){
		String[] dataType = Table.getDataType(table);
		String[] nullable = Table.getNullable(table);

		for(int i = 0; i < nullable.length; i++)
			if(values[i].equals("null") && nullable[i].equals("NO")){
				System.out.println("NULL-value constraint violation");
				System.out.println();
				return;
			}

		int key = new Integer(values[0]);
		int page = Table.searchKeyPage(file, key);
		if(page != 0) {
			if(Page.hasKey(file, page, key)){
				System.out.println("Uniqueness constraint violation");
				return;
			}
		}
		if(page == 0)
			page = 1;


		byte[] stc = new byte[dataType.length-1];
		short plSize = (short) Table.getPayloadSize(table, values, stc);
		int cellSize = plSize + 6;
		int offset = Page.checkLeafSpace(file, page, cellSize);


		if(offset != -1){
			Page.insertLeafCell(file, page, offset, plSize, key, stc, values);
		} else{
			Page.splitLeaf(file, page);
			tableInsert(file, table, values);
		}
	}

}
