package nl.bioinformatics.cylineup.actions;

import java.awt.event.ActionEvent;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.SwingHelper;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;


public class CyLineUpUpdateAction extends AbstractCyAction {
	
	private static final long serialVersionUID = 5025168186338724208L;
	
	private CyLineUpReferences refs;

	public CyLineUpUpdateAction(CyLineUpReferences refs){
		super("Update network views");
		this.refs = refs;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Get active network view
		CyNetworkView networkView = refs.appManager.getCurrentNetworkView();
		
		// Loop over all network views to set the zoom and the panning
		for(CyNetworkView nv : refs.networkViewManager.getNetworkViewSet()) {
			if(!nv.equals(networkView)) {
				nv.setVisualProperty(BasicVisualLexicon.NETWORK_SCALE_FACTOR, networkView.getVisualProperty(BasicVisualLexicon.NETWORK_SCALE_FACTOR));
				nv.setVisualProperty(BasicVisualLexicon.NETWORK_CENTER_X_LOCATION, networkView.getVisualProperty(BasicVisualLexicon.NETWORK_CENTER_X_LOCATION));
				nv.setVisualProperty(BasicVisualLexicon.NETWORK_CENTER_Y_LOCATION, networkView.getVisualProperty(BasicVisualLexicon.NETWORK_CENTER_Y_LOCATION));
			}
		}
		
		// Arrange windows
		SwingHelper.arrangeWindows(refs);
		
	}
	
}
