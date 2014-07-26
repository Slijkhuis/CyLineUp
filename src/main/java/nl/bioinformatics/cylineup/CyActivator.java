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
import org.cytoscape.io.write.PresentationWriterManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {
	
	public CyActivator() {
		super();
	}
	
	public void start(BundleContext bc) {
		
		System.out.println("CyLineUp started.");
		
		// Create reference object
		CyLineUpReferences refs = new CyLineUpReferences();
		refs.desktopApp = getService(bc,CySwingApplication.class);
		refs.networkViewManager = getService(bc, CyNetworkViewManager.class);
		refs.desktopManager = getService(bc, CyNetworkViewDesktopMgr.class);
		refs.appManager = getService(bc, CyApplicationManager.class);
		refs.networkFactory = getService(bc, CyNetworkViewFactory.class);
		refs.renderingManager = getService(bc,RenderingEngineManager.class);
		refs.presentationWriterManager = getService(bc, PresentationWriterManager.class);
		refs.taskManager = getService(bc, DialogTaskManager.class);
		
		// Add import action
		CyLineUpImportAction importAction = new CyLineUpImportAction(refs);
		registerService(bc, importAction, CyAction.class, new Properties());
		
		// Add update action
		CyLineUpUpdateAction updateAction = new CyLineUpUpdateAction(refs);
		registerService(bc, updateAction, CyAction.class, new Properties());
		
		// Add actions to reference object
		refs.importAction = importAction;
		refs.updateAction = updateAction;
		
		System.out.println("CyLineUp initialized.");
		
		// Add panel tab
		CyLineUpPanel panel = new CyLineUpPanel(refs);
		registerService(bc, panel, CytoPanelComponent.class, new Properties());
		
		System.out.println("CyLineUp panel added");
	}
	
}
