package nl.bioinformatics.cylineup.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ColorPreviewPanel extends JPanel {
	
	private static final long serialVersionUID = -5752231353182923885L;
	private Color color;
	
	public ColorPreviewPanel(Color c) {
		super();
		color = c;
		setMinimumSize(new Dimension(50, 50));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getPreferredSize());
	}
	
	public void setColor(Color c) {
		color = c;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(color == null) { return; }
		
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color start = color.darker().darker().darker();
        GradientPaint gp = new GradientPaint(0, 0, start, 0, h, color);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
	}

}
