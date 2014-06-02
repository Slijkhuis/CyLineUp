package nl.bioinformatics.cylineup.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.TreeMap;

import nl.bioinformatics.cylineup.CyLineUpReferences;

public class CsvData extends Data {

	private char delimiter = ',';
	private File f;
	
	public CsvData(File f, CyLineUpReferences refs) {
		this.f = f;
		this.refs = refs;
	}
	
	protected TreeMap<Integer, String[]> parseFile(boolean example, boolean forceRefresh) {
		
		if(currentlyParsed != null && !forceRefresh) {
			return currentlyParsed;
		}
		
		TreeMap<Integer, String[]> output = new TreeMap<Integer, String[]>();
		String line;
		Integer i = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			while((line = br.readLine()) != null) {
				String[] columns;
				if(delimiter == '\u0000') {
					columns = line.split("\\s+");
				} else {
					columns = line.split(String.valueOf(delimiter));
				}
				for(int index = 0; index < columns.length; index++) {
					columns[index] = columns[index].replaceAll("^\"|\"$", "");
				}
				
				// If it's just an example, stop at 15 columns
				if(columns.length > 15) {
					columns = Arrays.copyOf(columns, 15);
				}
				
				output.put(i, columns);
				i++;
				
				// If it's just an example, stop at 20 rows
				if(i == 20 && example) {
					break;
				}
				
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		currentlyParsed = output;
		
		return output;
	}
	
	public void setDelimiter(char delimiter_new) {
		delimiter = delimiter_new;
	}
}
