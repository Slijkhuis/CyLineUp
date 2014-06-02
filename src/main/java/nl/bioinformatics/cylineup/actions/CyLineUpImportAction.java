package nl.bioinformatics.cylineup.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.windows.ImportWindow;
import nl.bioinformatics.cylineup.models.SmallMultiple;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.view.model.CyNetworkView;

public class CyLineUpImportAction extends AbstractCyAction {
	
	private static final long serialVersionUID = 5025168186338724208L;

	private CyLineUpReferences refs;
	
	public CyLineUpImportAction(CyLineUpReferences refs){
		super("Update network views");
		this.refs = refs;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// Delete old networks
		if(refs.smallMultiples.size() > 0) {
			
			// Get confirmation
			int answer = JOptionPane.showConfirmDialog(
					this.refs.desktopApp.getJFrame(),
					"This will delete the current network views, continue?",
					"Delete network views",
					JOptionPane.YES_NO_OPTION
				);
			
			// Delete network views, or do nothing
			if(answer == JOptionPane.OK_OPTION) {
				for(SmallMultiple sm : refs.smallMultiples) {
					if(sm.getView() != null) {
						refs.networkViewManager.destroyNetworkView(sm.getView());
					}
				}
				refs.smallMultiples.clear();
			} else {
				return;
			}
			
		}
		
		CyNetworkView nv = refs.appManager.getCurrentNetworkView();
		
		if(nv == null) {
			JOptionPane.showMessageDialog(
					this.refs.desktopApp.getJFrame(),
					"You have not selected a network.",
					"LineUp",
					JOptionPane.ERROR_MESSAGE
				);
			return;
		}
		
		new ImportWindow(refs);
	}
	
}
