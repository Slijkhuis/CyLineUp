package nl.bioinformatics.cylineup;

import java.util.Properties;

import nl.bioinformatics.cylineup.actions.CyLineUpImportAction;
import nl.bioinformatics.cylineup.actions.CyLineUpUpdateAction;
import nl.bioinformatics.cylineup.gui.panels.CyLineUpPanel;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CyNetworkViewDesktopMgr;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {
	
	public CyActivator() {
		super();
	}
	
	public void start(BundleContext bc) {
		
		// Create reference object
		CyLineUpReferences refs = new CyLineUpReferences();
		refs.desktopApp = getService(bc,CySwingApplication.class);
		refs.networkManager = getService(bc, CyNetworkManager.class);
		refs.networkViewManager = getService(bc, CyNetworkViewManager.class);
		refs.desktopManager = getService(bc, CyNetworkViewDesktopMgr.class);
		refs.appManager = getService(bc, CyApplicationManager.class);
		refs.networkFactory = getService(bc, CyNetworkViewFactory.class);
		refs.vmmServiceRef = getService(bc,VisualMappingManager.class);
		refs.visualStyleFactoryServiceRef = getService(bc,VisualStyleFactory.class);
		refs.vmfFactory = getService(bc,VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
		refs.renderingManager = getService(bc,RenderingEngineManager.class);
		
		// Add import action
		CyLineUpImportAction importAction = new CyLineUpImportAction(refs);
		registerService(bc, importAction, CyAction.class, new Properties());
		
		// Add update action
		CyLineUpUpdateAction updateAction = new CyLineUpUpdateAction(refs);
		registerService(bc, updateAction, CyAction.class, new Properties());
		
		// Add actions to reference object
		refs.importAction = importAction;
		refs.updateAction = updateAction;
		
		// Add panel tab
		CyLineUpPanel panel = new CyLineUpPanel(refs);
		registerService(bc, panel, CytoPanelComponent.class, new Properties());
	}
	
}
