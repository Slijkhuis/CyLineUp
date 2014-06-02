package nl.bioinformatics.cylineup.data;

import java.util.TreeMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.ColumnItem;

public abstract class Data {
	
	/**
	 * Reference object that contains all services that LineUp requires
	 */
	public CyLineUpReferences refs;
	
	/**
	 * The cached results of the parsed data
	 */
	protected TreeMap<Integer, String[]> currentlyParsed = null;
	
	/**
	 * Parse the input file, this method should be implemented for each different file type
	 * 
	 * @param example		Whether to parse the entire file, or just the first few rows (for previewing)
	 * @param forceRefresh	Whether to use cached results or force a re-parse
	 * @return				Returns parsed data
	 */
	abstract protected TreeMap<Integer, String[]> parseFile(boolean example, boolean forceRefresh);
	
	/**
	 * Get a ComboBoxModel that contains the header columns
	 * 
	 * @param headers	Whether the first column contains header names
	 * @return			A ComboBoxModel with ColumnItems that contain header names
	 */
	public ComboBoxModel<ColumnItem> getColumnComboModel(boolean headers) {
		
		final TreeMap<Integer, String[]> output = parseFile(true, false);
		final String[] cols = output.get(0);
		
		DefaultComboBoxModel<ColumnItem> model = new DefaultComboBoxModel<ColumnItem>();
		
		for(int i = 0; i < cols.length; i++) {
			if(headers) {
				model.addElement(new ColumnItem(cols[i], i));
			} else {
				model.addElement(new ColumnItem("Column " + (i + 1), i));
			}
		}
		
		return model;
	}
	
	/**
	 * Get a ListModel that contains header columns
	 * 
	 * @param headers	Whether the first column contains header names 
	 * @return			A ListModel with ColumnItems that contain header names
	 */
	public DefaultListModel<ColumnItem> getColumnListModel(boolean headers) {
		final TreeMap<Integer, String[]> output = parseFile(true, false);
		final String[] cols = output.get(0);
		
		DefaultListModel<ColumnItem> model = new DefaultListModel<ColumnItem>();
		
		for(int i = 0; i < cols.length; i++) {
			if(headers) {
				model.addElement(new ColumnItem(cols[i], i));
			} else {
				model.addElement(new ColumnItem("Column " + (i + 1), i));
			}
		}
		
		return model;
	}
	
	/**
	 * Get a table model containing a preview of how the data was parsed
	 * 
	 * @param forceRefresh	Force a re-parse of the input file
	 * @return				A new AbstractTableModel
	 */
	public TableModel getTableModel(boolean forceRefresh) {
		
		final TreeMap<Integer, String[]> output = parseFile(true, forceRefresh);
		
		final int cols = output.get(0).length;
		final int rows = output.size();
		
		return new AbstractTableModel() {
			
			private static final long serialVersionUID = 2999558794006272908L;

			@Override
			public Object getValueAt(int row, int col) {
				String[] val = output.get(row);
				if(val == null) {
					return " ";
				} else {
					if(col >= val.length || val[col] == null) {
						return " ";
					} else {
						return val[col];
					}
				}
			}
			
			@Override
			public int getRowCount() {
				return rows;
			}
			
			@Override
			public int getColumnCount() {
				return cols;
			}
		};
	}
	
	/**
	 * Get a forced re-parsed TreeMap of the data
	 * 
	 * @return	TreeMap with the parsed data
	 */
	public TreeMap<Integer, String[]> getData() {
		return parseFile(false, true);
	}
	
	/**
	 * Get a forced re-parsed associative TreeMap of the data
	 * 
	 * @return	Associative TreeMap with the parsed data 
	 */
	public TreeMap<String, String[]> getAssocData(int idCol) {
		TreeMap<Integer, String[]> data = getData();
		TreeMap<String, String[]> output = new TreeMap<String, String[]>();
		
		for(int i = 0; i < data.size(); i++) {
			String[] entry = data.get(i);
			if(!output.containsKey(entry[idCol])) {
				output.put(processName(entry[idCol]), entry);
			}
		}
		
		return output;
	}

	/**
	 * Get the first row from the cached parsed data
	 * 
	 * @return	String array containing the values of the cells in the first row 
	 */
	public String[] getColumnNames(boolean headers) {
		
		String[] firstRow = currentlyParsed.get(0);
		
		if(!headers) {
			for(int i = 0; i < firstRow.length; i++) {
				firstRow[i] = "Column " + (i+1);
			}
		}
		
		return firstRow;
	}
	
	/**
	 * Get first row from the cached parsed data as a ColumnItem array
	 * 
	 * @return	ColumnItem array containing the values of the cells in the first row
	 */
	public ColumnItem[] getColumnItems(boolean headers) {
		String[] names = getColumnNames(headers);
		ColumnItem[] output = new ColumnItem[names.length];
		for(int i = 0; i < names.length; i++) {
			output[i] = new ColumnItem(names[i], i);
		}
		return output;
	}
	
	/**
	 * Processes a identifier column. '.1', '.2', etc is removed and, in case
	 * comma-separated values, only the first one is returned.
	 * 
	 * TODO: Make a link with the bridge db?
	 * 
	 * @param string
	 * @return
	 */
	public String processName(String string) {
		String[] strings = string.split("[,.]");
		return strings[0].trim();
	}
	
}
