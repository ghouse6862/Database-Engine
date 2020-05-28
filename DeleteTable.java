import java.io.RandomAccessFile;

public class DeleteTable {
	
	public static void parseDeleteString(String deleteString) {
		System.out.println("DELETE METHOD");
		System.out.println("Parsing the string:\"" + deleteString + "\"");

		String[] tokens=deleteString.split(" ");
		String table = tokens[3];
		String[] temp = deleteString.split("where");
		String cmpTemp = temp[1];
		
		// returns the condition based on which delete is to be performed.
		String[] cmp = Parser.parserEquation(cmpTemp);
		
		if(!DavisbaseUtility.tablePresent(table)) {
			System.out.println("Table " + table + " does not exist");
		} else {
			delete(table, cmp);
		}
	}
	
	public static void delete(String table, String[] cmp){
		try{

			int key = Integer.parseInt(cmp[2]);

			RandomAccessFile deleteTableFile = new RandomAccessFile("data/" + table + ".tbl", "rw");
			
			int pageCount = Table.pages(deleteTableFile);
			int pageToDelete = 0;
			
			int currentPage = 1;
			while(currentPage <= pageCount) {
				if(Page.hasKey(deleteTableFile, currentPage, key) && 
						Page.getPageType(deleteTableFile, currentPage) == 0x0D){
					pageToDelete = currentPage;
					break;
				}
				currentPage = currentPage++;
			}

			if(pageToDelete == 0) {
				System.out.println("The given key value does not exist");
				return;
			}

			short[] cellsAddr = Page.getCellArray(deleteTableFile, pageToDelete);
			int k = 0;
			for(int i = 0; i < cellsAddr.length; i++) {
				long loc = Page.getCellLoc(deleteTableFile, pageToDelete, i);
				String[] vals = Table.retrieveValues(deleteTableFile, loc);
				int x = Integer.parseInt(vals[0]);
				if(x != key) {
					Page.setCellOffset(deleteTableFile, pageToDelete, k, cellsAddr[i]);
					k++;
				}
			}
			Page.setCellNumber(deleteTableFile, pageToDelete, (byte)k);

		}catch(Exception e) {
			System.out.println(e);
		}

	}

}
