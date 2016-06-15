package nl.bioinformatics.cylineup.gui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.cytoscape.view.model.CyNetworkView;

import com.sun.glass.events.KeyEvent;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.models.SmallMultiple;

public class SwingHelper {
	
	/**
	 * Centers a Frame in the middle of the screen. Also sets minimum size
	 * as 30% of the entire screen and preferred size as 80% of the entire
	 * screen.
	 * 
	 * @param frame
	 */
	public static void centerWindow(Frame frame) {
		centerWindow(frame, 0);
	}
	
	public static void centerWindow(Frame frame, int size) {
		centerWindow(frame, size, size);
	}
	
	public static void centerWindow(Frame frame, int width, int height) {
		centerWindow((Window) frame, width, height);
	}
	
	public static void centerWindow(Window window, int width, int height) {
		// Get screen size
		final Toolkit tk = Toolkit.getDefaultToolkit();
		final Dimension screensize = tk.getScreenSize();
		
		// Set window minimum size
		window.setMinimumSize(new Dimension(
				(int) Math.floor(screensize.getWidth() * 0.3),
				(int) Math.floor(screensize.getHeight() * 0.3)
		));
		
		// Set window size
		if(width==0f)
			width = (int) Math.floor(screensize.getWidth() * 0.8);
		
		if(height==0f)
			height = (int) Math.floor(screensize.getHeight() * 0.8);
		
		window.setPreferredSize(new Dimension(width, height));
		
		int left = (int) (screensize.getWidth() - width) / 2;
		int right = (int) (screensize.getHeight() - height) / 2;;
		
		// Center window
		window.setLocation(left, right);
	}
	
	/**
	 * Arrange network view windows in a grid, sorted on the network view title.
	 * 
	 * @param refs References object
	 */
	public static void arrangeWindows(CyLineUpReferences refs) {
		
		List<CyNetworkView> views = new ArrayList<CyNetworkView>();
		for(SmallMultiple sm : refs.smallMultiples) {
			views.add(sm.getView());
		}
		
		if(!views.isEmpty()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						// Select views
						refs.appManager.setSelectedNetworkViews(views);
						
						// Make sure the main Cytoscape window is active
						refs.desktopApp.getJFrame().toFront();
						
						Robot robot = new Robot();
						// Simulate key press for Grid Mode
				        robot.keyPress(KeyEvent.VK_G);
				        robot.keyRelease(KeyEvent.VK_G);
				        // Simulate key press for View Mode
				        robot.keyPress(KeyEvent.VK_V);
				        robot.keyRelease(KeyEvent.VK_V);
					} catch (AWTException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
