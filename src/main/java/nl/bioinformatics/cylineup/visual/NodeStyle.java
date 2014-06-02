package nl.bioinformatics.cylineup.visual;

import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class NodeStyle {
	
	public View<CyNode> n;
	
	public NodeStyle(View<CyNode> node) {
		n = node;
	}
	
	public void copyStylesTo(View<CyNode> node) {
		
		node.setLockedValue(BasicVisualLexicon.NODE_X_LOCATION, n.getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION));
		node.setLockedValue(BasicVisualLexicon.NODE_Y_LOCATION, n.getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION));
		
		node.setLockedValue(BasicVisualLexicon.NODE_BORDER_PAINT, n.getVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT));
		node.setLockedValue(BasicVisualLexicon.NODE_BORDER_WIDTH, n.getVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH));
		
		node.setLockedValue(BasicVisualLexicon.NODE_TRANSPARENCY, n.getVisualProperty(BasicVisualLexicon.NODE_TRANSPARENCY));
		node.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, n.getVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR));
		
		node.setLockedValue(BasicVisualLexicon.NODE_LABEL, n.getVisualProperty(BasicVisualLexicon.NODE_LABEL));
		node.setLockedValue(BasicVisualLexicon.NODE_LABEL_COLOR, n.getVisualProperty(BasicVisualLexicon.NODE_LABEL_COLOR));
		node.setLockedValue(BasicVisualLexicon.NODE_LABEL_FONT_SIZE, n.getVisualProperty(BasicVisualLexicon.NODE_LABEL_FONT_SIZE));
		
		node.setLockedValue(BasicVisualLexicon.NODE_SHAPE, n.getVisualProperty(BasicVisualLexicon.NODE_SHAPE));
		
		node.setLockedValue(BasicVisualLexicon.NODE_WIDTH, n.getVisualProperty(BasicVisualLexicon.NODE_WIDTH));
		node.setLockedValue(BasicVisualLexicon.NODE_HEIGHT, n.getVisualProperty(BasicVisualLexicon.NODE_HEIGHT));
	}
	
}
