import java.io.File;
import java.io.RandomAccessFile;

public class DropTable {
	
	public static void dropTable(String dropTableString) {
		System.out.println("DROP METHOD");
		System.out.println("Parsing the string:\"" + dropTableString + "\"");

		String[] tks = dropTableString.split(" ");
		String tblName = tks[2];
		
		if(!DavisbaseUtility.tablePresent(tblName)){
			System.out.println("Table " + tblName + " does not exist.");
		} else {
			drop(tblName);
		}		

	}
	public static void drop(String table){
		
		try{

			RandomAccessFile file = new RandomAccessFile(DavisbaseUtility.db_tables, "rw");
			
			int noOfPg = Table.pages(file);
			
			for(int pg = 1; pg <= noOfPg; pg ++){
				file.seek((pg-1) * Table.pageSize);
				byte ftype = file.readByte();
				if(ftype == 0x0D) {
					short[] cellsAddr = Page.getCellArray(file, pg);
					int k = 0;
					for(int i = 0; i < cellsAddr.length; i++) {
						long location = Page.getCellLoc(file, pg, i);
						String[] values = Table.retrieveValues(file, location);
						String tb = values[1];
						if(!tb.equals(table)) {
							Page.setCellOffset(file, pg, k, cellsAddr[i]);
							k++;
						}
					}
					Page.setCellNumber(file, pg, (byte)k);
				} else
					continue;
			}

			file = new RandomAccessFile(DavisbaseUtility.db_columns, "rw");
			noOfPg = Table.pages(file);
			for(int pg = 1; pg <= noOfPg; pg ++){
				file.seek((pg-1) * Table.pageSize);
				byte fType = file.readByte();
				if(fType == 0x0D) {
					short[] cellsAddr = Page.getCellArray(file, pg);
					int k = 0;
					for(int i = 0; i < cellsAddr.length; i++) {
						long location = Page.getCellLoc(file, pg, i);
						String[] values = Table.retrieveValues(file, location);
						String tb = values[1];
						if(!tb.equals(table)) {
							Page.setCellOffset(file, pg, k, cellsAddr[i]);
							k++;
						}
					}
					Page.setCellNumber(file, pg, (byte)k);
				} else
					continue;
			}

			File anOldFile = new File("data", table + ".tbl"); 
			anOldFile.delete();
			
		} catch(Exception e){
			System.out.println(e);
		}

	}

}
