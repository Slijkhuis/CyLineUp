package nl.bioinformatics.cylineup.tasks;

import java.util.List;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.NetworkHelper;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.VisualHelper;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class CreateTask extends AbstractTask {

	public CyLineUpReferences refs;
	
	public CreateTask(List<SmallMultiple> smallMultiples, CyLineUpReferences refs) {
		
		this.refs = refs;
		
		// Retrieve the small multiples from the table model and copy them while saving the order
		int i = 1;
		for(SmallMultiple sm : smallMultiples) {
			sm.setOrder(i);
			refs.smallMultiples.add(sm);
			i++;
		}
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		// Set task title
		taskMonitor.setTitle("Creating network views");
		
		// Set indefinite loading bar
		taskMonitor.setProgress(-1.0);
		
		// Create networks
		taskMonitor.setStatusMessage("Creating networks");
		NetworkHelper.createNetworks(refs);
		
		// Set visual styles and fit network in views
		taskMonitor.setStatusMessage("Updating visual styles");
		VisualHelper.updateViews(refs, true);
		
		// Done
		taskMonitor.setProgress(1.0);
		
	}

}
