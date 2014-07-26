package nl.bioinformatics.cylineup.tasks;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.windows.ExportWindow;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class ExportWindowTask extends AbstractTask {

	private CyLineUpReferences refs;
	
	public ExportWindowTask(CyLineUpReferences refs) {
		this.refs = refs;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		// Show export window
		new ExportWindow(refs);
		
		// Done
		taskMonitor.setProgress(1.0);
		
	}

}
