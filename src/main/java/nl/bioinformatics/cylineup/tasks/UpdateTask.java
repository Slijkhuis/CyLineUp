package nl.bioinformatics.cylineup.tasks;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.visual.VisualHelper;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class UpdateTask extends AbstractTask {

	private CyLineUpReferences refs;
	
	public UpdateTask(CyLineUpReferences refs) {
		this.refs = refs;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		// Set task title
		taskMonitor.setTitle("Update views");
		
		// Set indefinite loading bar
		taskMonitor.setProgress(-1.0);
		
		// Set status
		taskMonitor.setStatusMessage("Updating views...");
		
		// Update views
		VisualHelper.updateViews(refs);
		
		// Done
		taskMonitor.setProgress(1.0);
	}
	
	@Override
	public void cancel() {
		
	}
}
