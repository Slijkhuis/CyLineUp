package nl.bioinformatics.cylineup.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.LayoutHelper;
import nl.bioinformatics.cylineup.gui.windows.ExportWindow;
import nl.bioinformatics.cylineup.visual.VisualHelper;
import nl.bioinformatics.cylineup.visual.VisualSettings;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;

public class CyLineUpPanel extends JPanel implements CytoPanelComponent {
	
	private static final long serialVersionUID = 6825548675965333721L;
	
	/**
	 * Constructor for the LineUp panel
	 * 
	 * @param refs Reference object that contains all LineUp services
	 */
	public CyLineUpPanel(final CyLineUpReferences refs) {
		
		
		/** Create GUI elements **/
		
		// Create container
		JPanel container = new JPanel();
		
		// Get layout helper
		LayoutHelper layout = new LayoutHelper(container);
		
		// Create import button
		JButton importBtn = new JButton("Import data");
		importBtn.addActionListener(refs.importAction);
		
		// Create update button
		JButton updateBtn = new JButton("Update network views");
		
		// Create grid buttons
		ButtonGroup ggroup = new ButtonGroup();
		JRadioButton g1 = new JRadioButton("<html><p>Auto</p></html>");
		JRadioButton g2 = new JRadioButton("<html><p>Fixed number of columns</p></html>");
		JRadioButton g3 = new JRadioButton("<html><p>Fixed number of rows</p></html>");
		
		// Add grid buttons to grid button group
		ggroup.add(g1); ggroup.add(g2); ggroup.add(g3);
		
		// Create grid input field
		final JSpinner gridInput = new JSpinner(new SpinnerNumberModel(2, 1, 15, 1));
		
		// Create scale slider
		final JSlider slider = new JSlider(1, 10);
		
		// Create p-value radio buttons
		ButtonGroup pgroup = new ButtonGroup();
		JRadioButton[] pradios = new JRadioButton[] {
				new JRadioButton("0.001"),
				new JRadioButton("0.01"),
				new JRadioButton("0.05"),
				new JRadioButton("0.1")
		};
		
		// Create color check boxes
		final JCheckBox useUpColor = new JCheckBox("<html><p>Use a color for up-regulated genes.</p></html>");
		final JCheckBox useDownColor = new JCheckBox("<html><p>Use a color for down-regulated genes.</p></html>");
		
		// Create color-pick buttons
		JButton pickUpColor = new JButton("<html><p>Pick color</p></html>");
		JButton pickDownColor = new JButton("<html><p>Pick color</p></html>");
		
		// Create color preview panels
		final ColorPreviewPanel upPreview = new ColorPreviewPanel(Color.green);
		final ColorPreviewPanel downPreview = new ColorPreviewPanel(Color.red);
		
		// Create fill radio buttons
		ButtonGroup fgroup = new ButtonGroup();
		JRadioButton f1 = new JRadioButton("<html><p>Don't use fill color</p></html>");
		JRadioButton f2 = new JRadioButton("<html><p>Use fill color to visualize p-value</p></html>");
		JRadioButton f3 = new JRadioButton("<html><p>Use fill color to visualize fold change</p></html>");
		fgroup.add(f1);
		fgroup.add(f2);
		fgroup.add(f3);
		
		// Create transparency radio buttons
		ButtonGroup tgroup = new ButtonGroup();
		JRadioButton t1 = new JRadioButton("<html><p>Don't use transparency</p></html>");
		JRadioButton t2 = new JRadioButton("<html><p>Use transparency to visualize p-value</p></html>");
		JRadioButton t3 = new JRadioButton("<html><p>Use transparency to visualize fold change</p></html>");
		tgroup.add(t1);
		tgroup.add(t2);
		tgroup.add(t3);
		
		// Create export button
		JButton exportButton = new JButton("<html><p>Export PNG</p></html>");
		
		/** Create layout **/
		
		layout.addRow(new JLabel("<html><h1>CyLineUp BETA</h1></html>"));
		layout.addRow(new JLabel("<html><b>DATA</b></html>"));
		layout.addRow(new JLabel("<html><p>Start with opening the network to which you want to map your transcriptome data. Then press the button 'Import data' below to open your datafile and map columns to small multiples of the current network.</p></html>"));
		layout.addRow(importBtn);
		layout.addRow(new JLabel("<html><b>VIEWS</b></html>"));
		layout.addRow(new JLabel("<html><p>Use the button below to sync the zoom and panning of all network views to the currently selected network view and to apply the grid and visual style settings to the networks.</p></html>"));
		layout.addRow(updateBtn);
		layout.addRow(new JLabel("<html><p>Number of fixed columns/rows:</p></html>"));
		layout.addRow(g1);
		layout.addRow(g2);
		layout.addRow(g3);
		layout.addRow(gridInput);
		layout.addRow(new JLabel("<html><b>VISUAL STYLES</b></html>"));
		layout.addRow(new JLabel("<html><p>Use the slider below to set the maximum node scale factor (ranges from no scaling to 10x as big). This control is most usefull after you've imported data.</p></html>"));
		layout.addRow(slider);
		layout.addRow(new JLabel("<html><b>p-value cut-off</b></html>"));
		layout.addRow(new float[] {1f, 1f}, pradios[0], pradios[1]);
		layout.addRow(new float[] {1f, 1f}, pradios[2], pradios[3]);
		layout.addRow(new JLabel("<html><b>Fill color (gradient)</b></html>"));
		layout.addRow(f1);
		layout.addRow(f2);
		layout.addRow(f3);
		layout.addRow(new JLabel("<html><p>Up-regulated genes</p></html>"));
		layout.addRow(useUpColor);
		layout.addRow(new float[] {1f, 1f}, pickUpColor, upPreview);
		layout.addRow(new JLabel("<html><p>Down-regulated genes</p></html>"));
		layout.addRow(useDownColor);
		layout.addRow(new float[] {1f, 1f}, pickDownColor, downPreview);
		layout.addRow(new JLabel("<html><b>Transparency</b></html>"));
		layout.addRow(t1);
		layout.addRow(t2);
		layout.addRow(t3);
		layout.addRow(new JLabel("<html><b>EXPORT</b></html>"));
		layout.addRow(exportButton);
		
		layout.addRow(true, new float[] {1f}, Box.createGlue());
		
		/** Set default values **/
		
		g1.setSelected(true);
		gridInput.setEnabled(false);
		slider.setValue(refs.settings.getScale());
		useUpColor.setSelected(refs.settings.isUseFillColorForUp());
		useDownColor.setSelected(refs.settings.isUseFillColorForDown());
		f3.setSelected(true);
		t1.setSelected(true);
		
		/** Assign actions **/
		
		// Set grid button actions
		g1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gridInput.setEnabled(false);
				refs.settings.setGridMode(VisualSettings.GRID_AUTO);
			}
		});
		g2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridInput.setEnabled(true);
				refs.settings.setGridMode(VisualSettings.GRID_FIX_COLUMNS);
			}
		});
		g3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gridInput.setEnabled(true);
				refs.settings.setGridMode(VisualSettings.GRID_FIX_ROWS);
			}
		});
		gridInput.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				refs.settings.setGridFixed((Integer) gridInput.getValue());
			}
		});
		
		// Slider change action
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				refs.settings.setScale(slider.getValue());
			}
		});
		
		// Loop over all radio buttons
		for(final JRadioButton r : pradios) {
			// Add radio to button group
			pgroup.add(r);
			
			// Check if radio button should be selected
			if(r.getText().equals(String.valueOf(refs.settings.getpValueCutOff()))) {
				r.setSelected(true);
			}
			
			// Add an onChange listener
			r.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					refs.settings.setpValueCutOff(Float.parseFloat(r.getText()));
				}
			});
		}
		
		// Set actions for the fill radio buttons
		f1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseFillColor(VisualSettings.DONT_USE);
			}
		});
		f2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseFillColor(VisualSettings.USE_FOR_PVALUE);
			}
		});
		f3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseFillColor(VisualSettings.USE_FOR_FOLDCHANGE);
			}
		});
		
		// Set colour-pick check box actions
		useUpColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseFillColorForUp(useUpColor.isSelected());
			}
		});
		useDownColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseFillColorForDown(useDownColor.isSelected());
			}
		});
		
		// Set colour-pick button actions
		pickUpColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        Color c = JColorChooser.showDialog(((Component)e.getSource()).getParent(), "Pick a color", Color.green);
		        upPreview.setColor(c);
		        refs.settings.setUpColor(c);
			}
		});
		pickDownColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        Color c = JColorChooser.showDialog(((Component)e.getSource()).getParent(), "Pick a color", Color.red);
		        downPreview.setColor(c);
		        refs.settings.setDownColor(c);
			}
		});
		
		// Set actions for the transparency radio buttons
		t1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseTransparency(VisualSettings.DONT_USE);
			}
		});
		t2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseTransparency(VisualSettings.USE_FOR_PVALUE);
			}
		});
		t3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.settings.setUseTransparency(VisualSettings.USE_FOR_FOLDCHANGE);
			}
		});
		
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Export to PNG.");
				
				// Create a window
				// TODO: create export window
				
				new ExportWindow(refs);
				
			}
		});
		
		/** Set up container **/
		
		// Set container size
		container.setPreferredSize(new Dimension(310, 1300));
		
		// Create scroll pane
		final JScrollPane scrollPane = new JScrollPane(container, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Set (initial) scroll pane size
		scrollPane.setPreferredSize(new Dimension(350, 620));
		
		// Set scroll pane speed
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		
		// Add scroll pane
		this.add(scrollPane);
		
		// Listen for resizes
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				int height = getHeight() - 20;
				scrollPane.setPreferredSize(new Dimension(350, height));
				scrollPane.validate();
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});
		
		// Update button action
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Update views
				VisualHelper.updateViews(refs);
			}
		});
		
	}
	
	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}
	
	@Override
	public String getTitle() {
		return "CyLineUp";
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public Icon getIcon() {
		return null;
	}

}
