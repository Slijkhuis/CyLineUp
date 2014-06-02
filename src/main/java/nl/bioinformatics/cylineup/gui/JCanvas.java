package nl.bioinformatics.cylineup.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.JPanel;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.VisualSettings;

import org.cytoscape.view.presentation.RenderingEngine;

public class JCanvas extends JPanel {
	
	private static final long serialVersionUID = -7868655399462985407L;
	private final CyLineUpReferences refs;
	
	public JCanvas(final CyLineUpReferences refs) {
		this.refs = refs;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		// Paint background
		super.paintComponent(g);
		
		// Set initial values
		int frameCount = refs.smallMultiples.size();
		int cols = 1;
		int rows = 1;
		int col = 0;
		int row = 0;
		int h = 0;
		int w = 0;
		
		// Set number of columns and rows
		if(refs.settings.getGridMode() == VisualSettings.GRID_FIX_COLUMNS) {
			// Fixed amount of columns, get as many rows as we need
			cols = refs.settings.getGridFixed();
			while(cols * rows < frameCount) {
				rows = rows + 1;
			}
		} else if (refs.settings.getGridMode() == VisualSettings.GRID_FIX_ROWS) {
			// Fixed amount of rows, get as many columns as we need
			rows = refs.settings.getGridFixed();
			while(cols * rows < frameCount) {
				cols = cols + 1;
			}
		} else { // GRID_AUTO
			// Increase column count first and then row count until we have enough space
			while(cols * rows < frameCount) {
				if(cols > rows) {
					rows = rows + 1;
				} else {
					cols = cols + 1;
				}
			}
		}
		
		// Loop over the small multiples
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Get rendering engines
			Collection<RenderingEngine<?>> engines = refs.renderingManager.getRenderingEngines(sm.getView());
			
			// Set width and height
			if(h == 0 || w == 0) {
				w = refs.desktopManager.getBounds(sm.getView()).width - refs.export.getWidthCorrection();
				h = refs.desktopManager.getBounds(sm.getView()).height - refs.export.getHeightCorrection();
			}
			
			if(w < 10) { w = 10; }
			if(h < 10) { h = 10; }
			
			// Look for the ding rendering engine
			for(RenderingEngine<?> engine : engines) {
				if(engine.getRendererId().equals("org.cytoscape.ding")) {
					
					// We'll only use black this drawing session
					g.setColor(new Color(0,0,0));
					
					// Set x and y
					int x = col * w + col * refs.export.getMargins();
					int y = row * h + row * refs.export.getMargins();
					
					// Check if we need to draw the title
					if(refs.export.isTitles()) {
						
						int font_h = g.getFontMetrics().getHeight() + g.getFontMetrics().getMaxDescent();
						
						int title_x = x;
						int title_y = y;
						
						if(refs.export.getTitlePosition() == ExportCanvasSettings.POSITION_TOP) {
							title_y += font_h * row;
							y += font_h + font_h * row;
						} else {
							title_y = y + h + font_h * row;
							y += font_h * row;
						}
						
						// Draw title
						g.drawString(sm.getName(), title_x, title_y + font_h - g.getFontMetrics().getMaxDescent());
						
					}
					
					// Create image
					BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
					engine.printCanvas(img.createGraphics());
					
					// Draw image on canvas
					g.drawImage(img, x, y, null);
					
					// Check if we need to draw a border
					if(refs.export.isBorder()) {
						g.drawRect(x, y, w, h);
					}
					
					// Go to next column
					col++;
					
					// Go to next row if this row is full
					if(col==cols) {
						col = 0;
						row++;
					}
				}
			}
		}
		
	}
	
}
