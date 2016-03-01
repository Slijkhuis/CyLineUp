package nl.bioinformatics.cylineup.tasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.ExportCanvasSettings;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.VisualSettings;

import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.freehep.graphicsio.svg.SVGGraphics2D;

public class PreviewTask extends AbstractTask {

	private boolean forceDimensionsRefresh = false;
	private CyLineUpReferences refs;
	
	public PreviewTask(CyLineUpReferences refs, boolean forceDimensionsRefresh) {
		this.forceDimensionsRefresh = forceDimensionsRefresh;
	}
	
	public PreviewTask(CyLineUpReferences refs) {
		this.refs = refs;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		// Set task title
		taskMonitor.setTitle("Create preview");
		
		// This mostly goes very fast, so an indefinite progress indicator will do
		taskMonitor.setProgress(-1.0);
		
		// Set status message
		taskMonitor.setStatusMessage("Creating export preview");
		
		// Set initial values
		int frameCount = refs.smallMultiples.size();
		int cols = 1;
		int rows = 1;
		int col = 0;
		int row = 0;
		int h = 0;
		int w = 0;
		int font_h = 0;
		
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
				
		// Create preview
				
		Graphics preview = null;
		SVGGraphics2D svg = null;
		Dimension size = refs.desktopManager.getDesktopViewAreaSize();
		File file = File.createTempFile("network-view", ".svg");
		svg = new SVGGraphics2D(file, size);
		svg.startExport();
		
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Set width and height if not already set
			if(w == 0 || h == 0) {
				w = sm.getView().getVisualProperty(BasicVisualLexicon.NETWORK_WIDTH).intValue();
				h = sm.getView().getVisualProperty(BasicVisualLexicon.NETWORK_HEIGHT).intValue();
			}
			
			
			// Minimum dimensions are 10x10
			if(w < 10) { w = 10; }
			if(h < 10) { h = 10; }
			
			// Create image if not already created
			if(preview == null || forceDimensionsRefresh) {
				
				// Calculate total width and height
				int totalWidth = cols * (w + refs.export.getMargins());
				int totalHeight = rows * (h + refs.export.getMargins());
				
				// If titles are shown, the font height should be added as well
				if(refs.export.isTitles()) {
					// Create temporary image and graphics to obtain the font metrics
					refs.preview = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
					preview = refs.preview.getGraphics();
					
					// Calculate font height
					font_h = preview.getFontMetrics().getHeight() + preview.getFontMetrics().getMaxAscent() + preview.getFontMetrics().getMaxDescent();
					
					// Increase total height
					totalHeight += font_h * rows;
				}
				
				// Create new preview image
				refs.preview = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
				
				// Save calculated size
				refs.settings.calculatedSize = new Dimension(totalWidth, totalHeight);
				
				// Get graphics object to draw on
				preview = refs.preview.getGraphics();
				
				// Draw background color
				preview.setColor(new Color(255, 255, 255));
				svg.setColor(new Color(255, 255, 255));
				preview.fillRect(0, 0, totalWidth, totalHeight);
				svg.fillRect(0, 0, totalWidth, totalHeight);
			}
			
			// Set drawing color
			preview.setColor(new Color(0, 0, 0));
			svg.setColor(new Color(0, 0, 0));
			
			// Calculate x and y
			int x = col * w + col * refs.export.getMargins();
			int y = row * h + row * refs.export.getMargins();
			
			// Check if we need to draw the title
			if(refs.export.isTitles()) {
				
				// Set title position
				int title_x = x;
				int title_y = y;
				
				// Check location of title
				if(refs.export.getTitlePosition() == ExportCanvasSettings.POSITION_TOP) {
					// Title is on top of the small multiple (title position is calculated from bottom left)
					title_y += font_h * row;
					
					// Push down the small multiple
					y += font_h + font_h * row;
				} else {
					// Title is on the bottom of the small multiple
					title_y = y + h + font_h * row;
					
					// Push down small multiple so that it doesn't overlap with previous title
					y += font_h * row;
				}
				
				// Draw title
				preview.drawString(sm.getName(), title_x, title_y + preview.getFontMetrics().getHeight());
				svg.drawString(sm.getName(), title_x, title_y + preview.getFontMetrics().getHeight());
			}
			
			// Read image file
			//Image image = ImageIO.read(new File(sm.getTempRaster()));
			
			// Get view
			CyNetworkView view = sm.getView();
						
			// Get rendering engine
			RenderingEngine<?> engine = (RenderingEngine<?>) refs.renderingManager.getRenderingEngines(view).toArray()[0];
						
			//Image image = engine.createImage(w,h);
			
			
			// Draw image
			//preview.drawImage(image, x, y, null);
			preview.translate(x, y);
			svg.translate(x, y);
			engine.printCanvas(svg);
			engine.printCanvas(preview);
			
			
			// Check if we need to draw a border
			if(refs.export.isBorder()) {
				preview.drawRect(x, y, w, h);
				svg.drawRect(x, y, w, h);
			}
			
			// Go to next column
			col++;
			
			// Go to next row if this row is full
			if(col==cols) {
				col = 0;
				row++;
			}
		}
		
		// Done
		taskMonitor.setProgress(1.0);
		svg.endExport();
		
	}

}
