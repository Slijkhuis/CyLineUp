package nl.bioinformatics.cylineup.tasks;

import java.awt.Component;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class RepaintTask extends AbstractTask {

	private Component component;
	
	public RepaintTask(Component component) {
		this.component = component;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		component.repaint();
		taskMonitor.setProgress(1.0);
	}

}
