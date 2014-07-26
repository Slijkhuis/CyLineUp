package nl.bioinformatics.cylineup.gui;

public class ExportCanvasSettings {
	
	public static final int POSITION_TOP = 1;
	public static final int POSITION_BOTTOM = 2;
	
	private int margins = 10;
	private boolean border = true;
	private boolean titles = true;
	private int titlePosition = POSITION_BOTTOM;
	
	public int getMargins() {
		return margins;
	}
	public void setMargins(int margins) {
		this.margins = margins;
	}
	public boolean isBorder() {
		return border;
	}
	public void setBorder(boolean border) {
		this.border = border;
	}
	public boolean isTitles() {
		return titles;
	}
	public void setTitles(boolean titles) {
		this.titles = titles;
	}
	public int getTitlePosition() {
		return titlePosition;
	}
	public void setTitlePosition(int titlePosition) {
		this.titlePosition = titlePosition;
	}
}
