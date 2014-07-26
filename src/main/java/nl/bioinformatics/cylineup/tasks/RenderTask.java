package nl.bioinformatics.cylineup.tasks;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.models.SmallMultiple;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.freehep.graphicsio.svg.SVGGraphics2D;

public class RenderTask extends AbstractTask {

	private CyLineUpReferences refs;
	
	public RenderTask(CyLineUpReferences refs) {
		this.refs = refs;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		double progress = 0.000;
		double progressStep = 0.999 / (refs.smallMultiples.size() * 2);
		
		taskMonitor.setTitle("Rendering output preview");
		
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Update status
			taskMonitor.setStatusMessage("Rendering vector output for " + sm.getName());
			
			// Get view
			CyNetworkView view = sm.getView();
			
			// Get rendering engine
			RenderingEngine<?> engine = (RenderingEngine<?>) refs.renderingManager.getRenderingEngines(view).toArray()[0];
			
			// Create temporary file
			File file = File.createTempFile("network-view", ".svg");
			
			// Set dimensions
			Dimension size = new Dimension(
					view.getVisualProperty(BasicVisualLexicon.NETWORK_WIDTH).intValue(),
					view.getVisualProperty(BasicVisualLexicon.NETWORK_HEIGHT).intValue()
				);
			
			/** Using Freehep (as Cytoscape does) for rendering the SVG file: **/
			
			// Create graphics object
			SVGGraphics2D svg = new SVGGraphics2D(file, size);
			
			// Render SVG
			svg.startExport();
			engine.printCanvas(svg);
			svg.endExport();
			
			// Increase progress
			progress += progressStep;
			taskMonitor.setProgress(progress);
			
			/** Create rasterized image of the vector file (used in preview) **/
			
			// Update status 
			taskMonitor.setStatusMessage("Rendering raster output for " + sm.getName());
			
			// Remember temporary file names
			sm.setTempVector(file.toURI().toString());
			sm.setTempRaster(file.toString() + ".png");
			
			// Convert to PNG
			PNGTranscoder pngTrans = new PNGTranscoder();
			TranscoderInput transInput = new TranscoderInput(sm.getTempVector());
			OutputStream out = new FileOutputStream(sm.getTempRaster());
			TranscoderOutput transOutput = new TranscoderOutput(out);
			pngTrans.transcode(transInput, transOutput);
			out.flush();
			out.close();
			
			// Increase progress
			progress += progressStep;
			taskMonitor.setProgress(progress);
			
		}
		
		taskMonitor.setProgress(1.0);
		
	}

}
