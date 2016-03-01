package nl.bioinformatics.cylineup.tasks;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.ExportCanvasSettings;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.VisualSettings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.cytoscape.util.swing.FileChooserFilter;
import org.cytoscape.util.swing.FileUtil;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.freehep.graphicsio.svg.SVGGraphics2D;

public class ExportWindowTask extends AbstractTask {

	private CyLineUpReferences refs;

	private File svgFile = null;
	
	@Tunable(description="SVG file", params="fileCategory=unspecified;input=false")
	public File getInput() {
		return svgFile;
	}
	public void setInput(File f) {
		svgFile = f;
	}
	public ExportWindowTask(CyLineUpReferences refs) {
		this.refs = refs;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Save small multiples to SVG");
		taskMonitor.setProgress(0.1);
		
		try {
			save2SVG(getInput());
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
			taskMonitor.showMessage(TaskMonitor.Level.ERROR,"Could not save the SVG file: " + ioexception.getMessage());
		}
		
		// Done
		taskMonitor.setProgress(1.0);
	}

	void save2SVG(File file) throws IOException {
		if ( file == null) {
			return;
		}
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
		if (refs.settings.getGridMode() == VisualSettings.GRID_FIX_COLUMNS) {
			// Fixed amount of columns, get as many rows as we need
			cols = refs.settings.getGridFixed();
			while (cols * rows < frameCount) {
				rows = rows + 1;
			}
		} else if (refs.settings.getGridMode() == VisualSettings.GRID_FIX_ROWS) {
			// Fixed amount of rows, get as many columns as we need
			rows = refs.settings.getGridFixed();
			while (cols * rows < frameCount) {
				cols = cols + 1;
			}
		} else { // GRID_AUTO
			// Increase column count first and then row count until we have
			// enough space
			while (cols * rows < frameCount) {
				if (cols > rows) {
					rows = rows + 1;
				} else {
					cols = cols + 1;
				}
			}
		}

		// create svg canvas

		SVGGraphics2D svg = null;
		// Dimension size = refs.desktopManager.getDesktopViewAreaSize();

		for (SmallMultiple sm : refs.smallMultiples) {
			// initialize with first small multiple
			if (svg == null) {
				w = sm.getView().getVisualProperty(BasicVisualLexicon.NETWORK_WIDTH).intValue();
				h = sm.getView().getVisualProperty(BasicVisualLexicon.NETWORK_HEIGHT).intValue();
				int totalWidth = cols * (w + refs.export.getMargins());
				int totalHeight = rows * (h + refs.export.getMargins());
				// Save calculated size
				refs.settings.calculatedSize = new Dimension(totalWidth, totalHeight);

				svg = new SVGGraphics2D(file, refs.settings.calculatedSize);

				svg.startExport();
				svg.setColor(new Color(255, 255, 255));

				svg.fillRect(0, 0, totalWidth, totalHeight);
				svg.setColor(new Color(0, 0, 0));
				font_h = svg.getFontMetrics().getHeight();
			}

			// Minimum dimensions are 10x10
			if (w < 10) {
				w = 10;
			}
			if (h < 10) {
				h = 10;
			}

			// Calculate x and y
			int x = col * w + col * refs.export.getMargins();
			int y = row * h + row * refs.export.getMargins();

			// draw the title
			// Set title position
			int title_x = x;
			int title_y = y;

			// Title is on top of the small multiple (title position is
			// calculated from bottom left)
			title_y += font_h * (row + 1);

			// Push down the small multiple
			y += font_h + font_h * (row + 1);

			// Get view
			CyNetworkView view = sm.getView();

			// Get rendering engine
			RenderingEngine<?> engine = (RenderingEngine<?>) refs.renderingManager.getRenderingEngines(view)
					.toArray()[0];

			// Draw image
			svg.translate(x, y);
			engine.printCanvas(svg);
			svg.translate(-x, -y);

			// Draw title
			svg.setColor(new Color(0, 0, 0));
			svg.drawString(sm.getName(), title_x, title_y);

			// Go to next column
			col++;

			// Go to next row if this row is full
			if (col == cols) {
				col = 0;
				row++;
			}
		}

		// Done
		svg.endExport();
	}

	@Override
	public void cancel() {
		
	}
	
}
