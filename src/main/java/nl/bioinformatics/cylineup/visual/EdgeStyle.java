package nl.bioinformatics.cylineup.visual;

import org.cytoscape.model.CyEdge;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class EdgeStyle {
	
	public View<CyEdge> e;
	
	public EdgeStyle(View<CyEdge> edge) {
		e = edge;
	}
	
	public void copyStylesTo(View<CyEdge> edge) {
		edge.setLockedValue(BasicVisualLexicon.EDGE, e.getVisualProperty(BasicVisualLexicon.EDGE));
		edge.setLockedValue(BasicVisualLexicon.EDGE_BEND, e.getVisualProperty(BasicVisualLexicon.EDGE_BEND));
		
		edge.setLockedValue(BasicVisualLexicon.EDGE_PAINT, e.getVisualProperty(BasicVisualLexicon.EDGE_PAINT));
		edge.setLockedValue(BasicVisualLexicon.EDGE_UNSELECTED_PAINT, e.getVisualProperty(BasicVisualLexicon.EDGE_UNSELECTED_PAINT));
		
		edge.setLockedValue(BasicVisualLexicon.EDGE_LINE_TYPE, e.getVisualProperty(BasicVisualLexicon.EDGE_LINE_TYPE));
		edge.setLockedValue(BasicVisualLexicon.EDGE_STROKE_SELECTED_PAINT, e.getVisualProperty(BasicVisualLexicon.EDGE_STROKE_SELECTED_PAINT));
		edge.setLockedValue(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT, e.getVisualProperty(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT));
		
		edge.setLockedValue(BasicVisualLexicon.EDGE_SOURCE_ARROW_SHAPE, e.getVisualProperty(BasicVisualLexicon.EDGE_SOURCE_ARROW_SHAPE));
		edge.setLockedValue(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE, e.getVisualProperty(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE));
		
		edge.setLockedValue(BasicVisualLexicon.EDGE_WIDTH, e.getVisualProperty(BasicVisualLexicon.EDGE_WIDTH));
		
		edge.setLockedValue(BasicVisualLexicon.EDGE_VISIBLE, e.getVisualProperty(BasicVisualLexicon.EDGE_VISIBLE));
	}
	
}
