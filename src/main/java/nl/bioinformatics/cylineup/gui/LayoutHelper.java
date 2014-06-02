package nl.bioinformatics.cylineup.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class LayoutHelper {
	
	private int row = 0;
	private Container c;
	
	public LayoutHelper(Container container) {
		c = container;
		c.setLayout(new GridBagLayout());
	}
	
	public void addRow(boolean fillY, float[] weights, Component... components) {
		
		// Create panel for this row
		JPanel panel = new JPanel();
		
		// Create new constraints
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Set GBC layout manager to panel
		panel.setLayout		(new GridBagLayout());
		
		// Set anchor and insets
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// Check what space we need to fill
		if(fillY) {
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weighty = 1.0f;
		} else {
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 0.0f;
		}
		
		// Add components
		for(int i = 0; i < components.length; i++) {
			if(weights[i] == 0.0f) {
				gbc.weightx = 1.0f;
			} else {
				gbc.weightx = weights[i];
			}
			gbc.gridx = i;
			panel.add(components[i], gbc);
		}
		
		// Reset x-weight for adding the row
		gbc.weightx = 1.0f;
		
		// Add row
		gbc.insets = new Insets(0, 0, 0, 0);;
		gbc.gridx = 0;
		gbc.gridy = row++;
		c.add(panel, gbc);
		
	}
	
	public void addRow(float[] weights, Component... components) {
		addRow(false, weights, components);
	}
	
	public void addRow(Component... components) {
		float[] weights = new float[components.length];
		addRow(weights, components);
	}
	
}
