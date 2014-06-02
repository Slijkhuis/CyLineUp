package nl.bioinformatics.cylineup;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import nl.bioinformatics.cylineup.data.Data;
import nl.bioinformatics.cylineup.gui.ExportCanvasSettings;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.EdgeStyle;
import nl.bioinformatics.cylineup.visual.NodeStyle;
import nl.bioinformatics.cylineup.visual.VisualSettings;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyNetworkViewDesktopMgr;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineFactory;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;

public class CyLineUpReferences {
	
	public CyNetworkManager networkManager;
	public CyNetworkViewManager networkViewManager;
	public CySwingApplication desktopApp;
	public CyNetworkViewDesktopMgr desktopManager;
	public CyApplicationManager appManager;
	public CyNetworkViewFactory networkFactory;
	public VisualMappingManager vmmServiceRef;
	public VisualStyleFactory visualStyleFactoryServiceRef;
	public VisualMappingFunctionFactory vmfFactory;
	
	public RenderingEngineFactory<CyNetworkView> renderingFactory;
	public RenderingEngineManager renderingManager;
	
	public ArrayList<SmallMultiple> smallMultiples = new ArrayList<SmallMultiple>();
	public String idColName = "name";
	public int idColPos = 0;
	public VisualSettings settings = new VisualSettings();
	public HashMap<Long, NodeStyle> nodeStyles;
	public HashMap<Long, EdgeStyle> edgeStyles;
	public Data data;
	
	public ExportCanvasSettings export = new ExportCanvasSettings();
	
	public ActionListener importAction;
	public ActionListener updateAction;
	
	
}