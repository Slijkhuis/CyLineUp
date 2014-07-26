package nl.bioinformatics.cylineup.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import nl.bioinformatics.cylineup.CyLineUpReferences;


public class ExportCanvas extends JPanel {
	
	private static final long serialVersionUID = 5329179432601377150L;
	private CyLineUpReferences refs;
	
	public ExportCanvas(CyLineUpReferences refs) {
		this.refs = refs;
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(refs.preview != null) {
			g.drawImage(refs.preview, 0, 0, null);
		}
		
	}
	
}
