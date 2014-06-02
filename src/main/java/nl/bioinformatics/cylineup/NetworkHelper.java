package nl.bioinformatics.cylineup;

import java.util.HashMap;
import java.util.TreeMap;

import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.EdgeStyle;
import nl.bioinformatics.cylineup.visual.NodeStyle;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class NetworkHelper {

	/**
	 * This method will create the small multiple network views
	 * It will perform the following tasks:
	 *  - Save (backup) node styles
	 *  - Save (backup) edge styles
	 *  - Create table columns if they do not exist already
	 *  - Create network views based on currently selected network
	 *  - Add network views through the CyNetworkView manager
	 *  - Add all data from the input file to the network's node table
	 *  - Keep track of minimum and maximum values
	 *  
	 * @param refs
	 */
	public static void createNetworks(CyLineUpReferences refs) {
		
		// Get template network
		CyNetworkView currentView = refs.appManager.getCurrentNetworkView();
		CyNetwork currentNetwork = currentView.getModel();
		
		// Get table
		CyTable table = currentNetwork.getDefaultNodeTable();
		
		// Save node styles
		refs.nodeStyles = new HashMap<Long, NodeStyle>();
		for(View<CyNode> node : currentView.getNodeViews()) {
			refs.nodeStyles.put(node.getModel().getSUID(), new NodeStyle(node));
		}
		
		// Save edge styles
		refs.edgeStyles = new HashMap<Long, EdgeStyle>();
		for(View<CyEdge> edge : currentView.getEdgeViews()) {
			refs.edgeStyles.put(edge.getModel().getSUID(), new EdgeStyle(edge));
		}
		
		// Create networks and table columns
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Add table columns
			String columnName = sm.getValueColumn().getTitle();
			if(columnName != null && currentNetwork.getDefaultNodeTable().getColumn(columnName) == null) {
				table.createColumn(columnName, Double.class, false);
			}
			columnName = sm.getPValueColumn().getTitle();
			if(columnName != null && currentNetwork.getDefaultNodeTable().getColumn(columnName) == null) {
				table.createColumn(columnName, Double.class, false);
			}
			columnName = null; // Make available for GC
			
			// Create network view
			sm.setView(refs.networkFactory.createNetworkView(currentNetwork));
			sm.getView().setVisualProperty(BasicVisualLexicon.NETWORK_TITLE, sm.getName());
			
			// Add network
			refs.networkViewManager.addNetworkView(sm.getView());
		}
		
		// Flags to check if initial values have been set
		boolean isInitialValueSet = false;
		boolean isInitialPValueSet = false;
		
		// Loop over all small multiples
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Get associative data (GeneID > Values)
			TreeMap<String, String[]> parsed = refs.data.getAssocData(sm.getIdColumn().getValue());
			
			// Assign values to rows
			for(View<CyNode> node : sm.getView().getNodeViews()) {
				// Get row
				CyRow row = table.getRow(node.getModel().getSUID());
				
				// Get identifier
				String id = (String) row.getAllValues().get(refs.idColName);
				
				if(id != null) {
					// Look for values
					String[] values = parsed.get(id);
					if(values != null) {
						
						// Set value column
						int valueIndex = sm.getValueColumn().getValue();
						if(valueIndex < values.length && table.getColumn(sm.getValueColumn().getTitle()) != null) {
							
							// Get value in Double-type
							double value = Double.valueOf(values[valueIndex]);
							
							// Convert negative fold change values
							if(value < 0) {
								value = -1 / value;
							}
							
							// Set row's value
							row.set(sm.getValueColumn().getTitle(), value);
							
							// Keep track of minimum / maximum values 
							if(isInitialValueSet) {
								if(value < refs.settings.getMinValue()) { refs.settings.setMinValue(value); }
								if(value > refs.settings.getMaxValue()) { refs.settings.setMaxValue(value); }
							} else {
								refs.settings.setMinValue(value);
								refs.settings.setMaxValue(value);
								isInitialValueSet = true;
							}
						}
						
						// Set p-value column
						int pValueIndex = sm.getPValueColumn().getValue();
						if(pValueIndex < values.length && table.getColumn(sm.getPValueColumn().getTitle()) != null) {
							double pValue = Double.valueOf(values[pValueIndex]);
							row.set(sm.getPValueColumn().getTitle(), pValue);
							
							// Keep track of minimum / maximum values 
							if(isInitialPValueSet) {
								if(pValue < refs.settings.getMinPValue()) { refs.settings.setMinPValue(pValue); }
								if(pValue > refs.settings.getMaxPValue()) { refs.settings.setMaxPValue(pValue); }
							} else {
								refs.settings.setMinPValue(pValue);
								refs.settings.setMaxPValue(pValue);
								isInitialPValueSet = true;
							}
							
						}
						
					}
				}
				
			}
			
		}
		
		/*
		
		// Loop through rows
		for(CyRow row : table.getAllRows()) {
			// Get identifier
			String id = row.get(refs.idColName, String.class);
			
			// Check if identifier is set
			if(id != null && !id.equals("")) {
				
				// Get associative data (GeneID > Values)
				TreeMap<String, String[]> parsed = refs.data.getAssocData(sm.);
				
				// Get values
				String[] values = parsed.get(refs.data.processName(id));
				
				// Check if values are set
				if(values != null) {
					System.out.println("Values have been found for "+id+". Length: " + values.length);
					for(String val : values) {
						System.out.println("\t\tValue = " + val);
					}
					// Set values
					for(SmallMultiple sm : refs.smallMultiples) {
						
						// Set value column
						int valueIndex = sm.getValueColumn().getValue();
						if(valueIndex < values.length && table.getColumn(sm.getValueColumn().getTitle()) != null) {
							double value = Double.valueOf(values[valueIndex]);
							row.set(sm.getValueColumn().getTitle(), value);
						}
						
						// Set p-value column
						int pValueIndex = sm.getPValueColumn().getValue();
						if(pValueIndex < values.length && table.getColumn(sm.getPValueColumn().getTitle()) != null) {
							double pValue = Double.valueOf(values[pValueIndex]);
							row.set(sm.getPValueColumn().getTitle(), pValue);
						}
					}
				}
			}
		}*/
		
		
	}
	
}
