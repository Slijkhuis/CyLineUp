package nl.bioinformatics.cylineup.models;

import javax.swing.DefaultComboBoxModel;

import nl.bioinformatics.cylineup.CyLineUpReferences;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyTable;

public class NodeTableColumnsComboModel extends DefaultComboBoxModel<String> {

	private static final long serialVersionUID = 5056294921027066582L;
	
	public NodeTableColumnsComboModel(CyLineUpReferences refs) {
		
		// Get table
		CyTable table = refs.appManager.getCurrentNetwork().getDefaultNodeTable();
		
		// Add columns
		for(CyColumn col : table.getColumns()) { 
			addElement(col.getName());
		}
		
	}
	
}
