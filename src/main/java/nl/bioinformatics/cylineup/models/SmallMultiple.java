package nl.bioinformatics.cylineup.models;

import org.cytoscape.view.model.CyNetworkView;

import nl.bioinformatics.cylineup.gui.ColumnItem;

public class SmallMultiple {
	
	private String name;
	private int order;
	private ColumnItem idColumn;
	private ColumnItem valueColumn;
	private ColumnItem pValueColumn;
	private boolean isControl;
	private CyNetworkView view;
	
	public SmallMultiple() {
		this("-", 0, new ColumnItem("-", 0), new ColumnItem("-", 0));
	}
	
	public SmallMultiple(String name, int order, ColumnItem idColumn, ColumnItem valueColumn) {
		this(name, order, idColumn, valueColumn, new ColumnItem("-", 0), false);
	}
	
	public SmallMultiple(String name, int order, ColumnItem idColumn, ColumnItem valueColumn, ColumnItem pValueColumn, boolean isControl) {
		this.name = name;
		this.order = order;
		this.idColumn = idColumn;
		this.valueColumn = valueColumn;
		this.pValueColumn = pValueColumn;
		this.isControl = isControl;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public ColumnItem getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(ColumnItem idColumn) {
		this.idColumn = idColumn;
	}
	public ColumnItem getValueColumn() {
		return valueColumn;
	}
	public void setValueColumn(ColumnItem value) {
		this.valueColumn = value;
	}
	public ColumnItem getPValueColumn() {
		return pValueColumn;
	}
	public void setPValueColumn(ColumnItem pValueColumn) {
		this.pValueColumn = pValueColumn;
	}
	public boolean isControl() {
		return isControl;
	}
	public void setControl(boolean isControl) {
		this.isControl = isControl;
	}

	public CyNetworkView getView() {
		return view;
	}

	public void setView(CyNetworkView view) {
		this.view = view;
	}
	
	
	
}
