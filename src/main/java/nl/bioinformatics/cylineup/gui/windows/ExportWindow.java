package nl.bioinformatics.cylineup.gui.windows;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.ExportCanvas;
import nl.bioinformatics.cylineup.gui.ExportCanvasSettings;
import nl.bioinformatics.cylineup.gui.LayoutHelper;
import nl.bioinformatics.cylineup.gui.SwingHelper;
import nl.bioinformatics.cylineup.tasks.PreviewTask;
import nl.bioinformatics.cylineup.tasks.RepaintTask;

import org.cytoscape.work.TaskIterator;

public class ExportWindow extends JFrame {
	
	private static final long serialVersionUID = -5173666802083389912L;
	
	private CyLineUpReferences refs;
	
	private void updateCanvas(ExportCanvas canvas) {
		refs.taskManager.execute(new TaskIterator(
				new PreviewTask(refs),
				new RepaintTask(canvas)
			));
	}
	
	public ExportWindow(final CyLineUpReferences refs) {
		
		this.refs = refs;
		
		/** Create GUI elements **/
		final ExportCanvas canvas = new ExportCanvas(refs);
		final JScrollPane scrollPane = new JScrollPane(canvas);
		
		final JSlider margins = new JSlider(0, 50);
		
		final JCheckBox border = new JCheckBox("Display border");
		final JCheckBox titles = new JCheckBox("Display titles");
		final JPanel titlePosition = new JPanel();
		final ButtonGroup titlePositionGroup = new ButtonGroup();
		final JRadioButton titlePositionTop = new JRadioButton("Top");
		final JRadioButton titlePositionBottom = new JRadioButton("Bottom");
		
		final JButton exportButton = new JButton("<html><p>Export PNG</p></html>");
		
		titlePositionGroup.add(titlePositionTop);
		titlePositionGroup.add(titlePositionBottom);
		
		titlePosition.add(titlePositionTop);
		titlePosition.add(titlePositionBottom);
		
		/** Create layout **/
		LayoutHelper layout = new LayoutHelper(this);
		
		layout.addRow(true, new float[] {1.0f}, scrollPane);
		layout.addRow(new JLabel("<html><p>Margins</p></html>"));
		layout.addRow(margins);
		layout.addRow(new JLabel("<html><p>Border</p></html>"));
		layout.addRow(border);
		layout.addRow(new float[] { 1.0f, 1.0f }, new JLabel("<html><p>Titles</p></html>"), new JLabel("<html><p>Title position</p></html>"));
		layout.addRow(new float[] { 1.0f, 1.0f }, titles, titlePosition);
		layout.addRow(exportButton);
		
		/** Set actions and (default) values **/
		
		// Set canvas background color
		canvas.setBackground(new Color(255,255,255));
		canvas.setOpaque(true);
		
		// Set canvas size
		if(refs.settings.isAutoSize() || refs.settings.calculatedSize == null) {
			canvas.setPreferredSize(refs.desktopManager.getDesktopViewAreaSize());
		} else {
			canvas.setPreferredSize(refs.settings.calculatedSize);
		}
		
		// Set margins default value
		margins.setValue(refs.export.getMargins());
		
		// Set margins change listener
		margins.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				refs.export.setMargins(margins.getValue());
				updateCanvas(canvas);
			}
		});
		
		// Set border default value
		border.setSelected(refs.export.isBorder());
		
		// Set border action listener
		border.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refs.export.setBorder(border.isSelected());
				updateCanvas(canvas);
			}
		});
		
		// Set titles default value
		titles.setSelected(refs.export.isTitles());
		
		// Set border action listener
		titles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refs.export.setTitles(titles.isSelected());
				updateCanvas(canvas);
			}
		});
		
		// Set title position default value
		if(refs.export.getTitlePosition() == ExportCanvasSettings.POSITION_TOP) {
			titlePositionTop.setSelected(true);
			titlePositionBottom.setSelected(false);
		} else {
			titlePositionTop.setSelected(false);
			titlePositionBottom.setSelected(true);
		}
		
		// Set title position action listeners
		titlePositionTop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refs.export.setTitlePosition(ExportCanvasSettings.POSITION_TOP);
				updateCanvas(canvas);
			}
		});
		titlePositionBottom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refs.export.setTitlePosition(ExportCanvasSettings.POSITION_BOTTOM);
				updateCanvas(canvas);
			}
		});
		
		// Export button action
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ExportWindow.this.setVisible(false);
				
				FileDialog fd = new FileDialog(ExportWindow.this, "Save as...", FileDialog.SAVE);
				
				SwingHelper.centerWindow(fd, 250, 350);
				fd.pack();
				fd.setVisible(true);
				fd.setAlwaysOnTop(true);
				
				String file = fd.getFile();
				String dir = fd.getDirectory();
				
				String filename = dir + file;
				
				if(!filename.equals("")) {
					
					// Add extension to the filename if not added already
					if(!filename.endsWith(".png")) {
						filename += ".png";
					}
					
					BufferedImage im = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
					canvas.paint(im.getGraphics());
					try {
						ImageIO.write(im, "PNG", new File(filename));
					} catch (IOException e1) {
						// TODO: display error (probably things like write permission errors occur here)
						e1.printStackTrace();
					}
				}
				
				ExportWindow.this.setVisible(true);
			}
		});
		
		/** Show the window **/
		SwingHelper.centerWindow(this);
		pack();
		setVisible(true);
		setAlwaysOnTop(true);
		
		// Repaint canvas
		updateCanvas(canvas);
		
	}
	
}
