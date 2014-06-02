package nl.bioinformatics.cylineup.visual;

import java.awt.Color;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.SwingHelper;
import nl.bioinformatics.cylineup.models.SmallMultiple;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public class VisualHelper {
	
	public static void updateViews(CyLineUpReferences refs) {
		updateViews(refs, false);
	}
	
	/**
	 * Update all network views based on the user input
	 * 
	 * @param refs		The reference object containing the services and objects necessary for refreshing the views
	 * @param fit		Whether to fit the network in the window or not
	 */
	public static void updateViews(CyLineUpReferences refs, boolean fit) {
		
		// For debugging
		System.out.println("=== Settings ===");
		System.out.println("Scale:\t\t\t" + refs.settings.getScale());
		System.out.println("P-value cutoff:\t\t" + refs.settings.getpValueCutOff());
		System.out.println("useTransparency:\t" + refs.settings.getUseTransparency());
		System.out.println("useFillColor:\t\t" + refs.settings.getUseFillColor());
		System.out.println("useFillColorForUp:\t" + refs.settings.isUseFillColorForUp());
		System.out.println("useFillColorForDown:\t" + refs.settings.isUseFillColorForDown());
		System.out.println("upColor:\t\t" + refs.settings.getUpColor());
		System.out.println("downColor:\t\t" + refs.settings.getDownColor());
		System.out.println("gridMode:\t\t" + refs.settings.getGridMode());
		System.out.println("gridFixed:\t\t" + refs.settings.getGridFixed());
		System.out.println("Max. value:\t\t" + refs.settings.getMaxValue());
		System.out.println("Min. value:\t\t" + refs.settings.getMinValue());
		System.out.println("Max. p-value:\t\t" + refs.settings.getMaxPValue());
		System.out.println("Min. p-value:\t\t" + refs.settings.getMinPValue());
		
		// Get active network
		CyNetworkView networkView = refs.appManager.getCurrentNetworkView();
		
		// Loop over networks
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Get view
			CyNetworkView view = sm.getView();
			
			// Set zoom and panning
			view.setVisualProperty(BasicVisualLexicon.NETWORK_SCALE_FACTOR, networkView.getVisualProperty(BasicVisualLexicon.NETWORK_SCALE_FACTOR));
			view.setVisualProperty(BasicVisualLexicon.NETWORK_CENTER_X_LOCATION, networkView.getVisualProperty(BasicVisualLexicon.NETWORK_CENTER_X_LOCATION));
			view.setVisualProperty(BasicVisualLexicon.NETWORK_CENTER_Y_LOCATION, networkView.getVisualProperty(BasicVisualLexicon.NETWORK_CENTER_Y_LOCATION));
			
			// Get table
			CyTable table = view.getModel().getDefaultNodeTable();
			
			// Set edge styles
			for(View<CyEdge> edge : view.getEdgeViews()) {
				refs.edgeStyles.get(edge.getModel().getSUID()).copyStylesTo(edge);
			}
			
			// Set node styles
			for(View<CyNode> node : view.getNodeViews()) {
				
				// Set default node styles (from parent network)
				refs.nodeStyles.get(node.getModel().getSUID()).copyStylesTo(node);
				
				// Get row
				CyRow row = table.getRow(node.getModel().getSUID());
				
				// Get values
				Double value = row.get(sm.getValueColumn().getTitle(), Double.class);
				Double pValue = row.get(sm.getPValueColumn().getTitle(), Double.class);
				
				// Set styles based on value
				if(value != null) {
					
					// Convert negative fold change values
					if(value < 0) {
						value = -1 / value;
					}
					
					// Set scale
					if(refs.settings.getScale() != 1) {
						double orig_width = node.getVisualProperty(BasicVisualLexicon.NODE_WIDTH);
						double orig_height = node.getVisualProperty(BasicVisualLexicon.NODE_HEIGHT);
						double width = orig_width * getPercentage(refs, value) * refs.settings.getScale();
						double height = orig_height * getPercentage(refs, value) * refs.settings.getScale();
						
						if(width < orig_width) { width = orig_width; }
						if(height < orig_height) { height = orig_height; }
						
						node.setLockedValue(BasicVisualLexicon.NODE_WIDTH,  width);
						node.setLockedValue(BasicVisualLexicon.NODE_HEIGHT,  height);
					}
					
					// Check if fill color should be used
					if(refs.settings.getUseFillColor() == VisualSettings.USE_FOR_FOLDCHANGE) {
						if(value > 1 && refs.settings.isUseFillColorForUp()) {
							Color scaledColor = getColor(refs, refs.settings.getUpColor(), value, false);
							node.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, scaledColor);
						}
						if(value < 1 && refs.settings.isUseFillColorForDown()) {
							Color scaledColor = getColor(refs, refs.settings.getDownColor(), value, false);
							node.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, scaledColor);
						}
					}
					
					// Check if transparency should be used
					if(refs.settings.getUseTransparency() == VisualSettings.USE_FOR_FOLDCHANGE) {
						int iValue = (int) (getPercentage(refs, value) * 255);
						if(iValue < 1) { iValue = 1; }
						if(iValue > 255) { iValue = 255; }
						node.setLockedValue(BasicVisualLexicon.NODE_TRANSPARENCY, iValue);
					}
					
				}
				
				// Set styles based on p-value
				if(pValue != null) {
					
					if(pValue < refs.settings.getpValueCutOff()) {
						
						// Check if fill colour should be used
						if(refs.settings.getUseFillColor() == VisualSettings.USE_FOR_PVALUE) {
							
							// Confirm that there is a fold change value from which we can derive whether it's up or down regulated
							if(value != null) {
								if(value > 1 && refs.settings.isUseFillColorForUp()) {
									Color scaledColor = getColor(refs, refs.settings.getUpColor(), pValue, true);
									node.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, scaledColor);
								}
								if(value < 1 && refs.settings.isUseFillColorForDown()) {
									Color scaledColor = getColor(refs, refs.settings.getDownColor(), pValue, true);
									node.setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, scaledColor);
								}
							}
						}
						
						// Check if transparency should be used
						if(refs.settings.getUseTransparency() == VisualSettings.USE_FOR_PVALUE) {
							
							int iValue = (int) (getPercentage(refs, pValue) * 255);
							if(iValue < 1) { iValue = 1; }
							if(iValue > 255) { iValue = 255; }
							
							node.setLockedValue(BasicVisualLexicon.NODE_TRANSPARENCY, iValue);
						}
					}
					
				}
				
			}
			
		}
		
		// Arrange windows
		SwingHelper.arrangeWindows(refs);
		
		// Update views
		for(SmallMultiple sm : refs.smallMultiples) {
			// Fit networks in windows
			if(fit) { sm.getView().fitContent(); }
			sm.getView().updateView();
		}
		
	}
	
	public static double getPercentage(CyLineUpReferences refs, double value) {
		return getPercentage(refs, value, false);
	}
	
	public static double getPercentage(CyLineUpReferences refs, double value, boolean pValue) {
		
		double max;
		double min;
		double ceil;
		double percent;
		
		if(pValue) {
			max = refs.settings.getMaxPValue();
			min = refs.settings.getMinPValue();
		} else {
			max = refs.settings.getMaxValue();
			min = refs.settings.getMinValue();
		}
		
		ceil = max - min;
		
		percent = (value - min) / ceil;
		
		return percent;
		
	}
	
	public static Color getColor(CyLineUpReferences refs, Color baseColor, double value, boolean pValue) {
		
		double percent = getPercentage(refs, value);
		Color darkColor = baseColor.darker().darker();
		
		return ColorUtil.blend(baseColor, darkColor, percent);
	}
	
}
