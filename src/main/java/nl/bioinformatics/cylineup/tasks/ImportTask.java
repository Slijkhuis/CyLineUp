package nl.bioinformatics.cylineup.tasks;

import java.io.File;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.windows.ParserWindow;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

public class ImportTask extends AbstractTask {

	private CyLineUpReferences refs;
	private File input = null;
	
	@Tunable(description="Input file (data file, only CSV files are supported)", params="fileCategory=unspecified;input=true")
	public File getInput() {
		return input;
	}
	public void setInput(File f) {
		input = f;
	}
	
	public ImportTask(CyLineUpReferences refs) {
		this.refs = refs;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		
		taskMonitor.setTitle("Choose input file");
		
		new ParserWindow(getInput(), refs);
		
		taskMonitor.setProgress(1.0);
		
	}

}
