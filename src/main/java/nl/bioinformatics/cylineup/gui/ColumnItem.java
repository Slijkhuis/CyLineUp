package nl.bioinformatics.cylineup.gui;

public class ColumnItem {
	
	private String title;
	private int value;
	
	public ColumnItem(String t, int v) {
		title = t;
		value = v;
	}
	
	public String toString() {
		return title;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getTitle() {
		return title;
	}
	
}
