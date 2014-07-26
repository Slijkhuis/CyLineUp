package nl.bioinformatics.cylineup.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.visual.VisualSettings;

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
		
		// Get desktop size
		final Dimension desktopSize = refs.desktopManager.getDesktopViewAreaSize();
		
		// Check if we need to override desktop size
		if(!refs.settings.isAutoSize()) {
			desktopSize.setSize(new Dimension(refs.settings.getSizeWidth(), refs.settings.getSizeHeight()));
		}
		
		// Get the total frame count
		int frameCount = refs.smallMultiples.size(); 
		if ( frameCount == 0)
			return;
		
		// Default column and row count
		int cols = 1;
		int rows = 1;
		
		// Get grid mode and calculate amount of rows and columns
		if(refs.settings.getGridMode() == VisualSettings.GRID_FIX_COLUMNS) {
			
			// Fixed amount of columns, get as many rows as we need
			cols = refs.settings.getGridFixed();
			while(cols * rows < frameCount) {
				rows = rows + 1;
			}
			
		} else if (refs.settings.getGridMode() == VisualSettings.GRID_FIX_ROWS) {
			
			// Fixed amount of rows, get as many columns as we need
			rows = refs.settings.getGridFixed();
			while(cols * rows < frameCount) {
				cols = cols + 1;
			}
			
		} else { // GRID_AUTO
			
			// Increase column count first and then row count until we have enough space
			while(cols * rows < frameCount) {
				if(cols > rows) {
					rows = rows + 1;
				} else {
					cols = cols + 1;
				}
			}
		}
		
		// Calculate cell width and height
		int width = desktopSize.width / cols;
		int height = desktopSize.height / rows;
		
		// Initial position (left top)
		int current_col = 0;
		int current_row = 0;
		
		// Loop over the sorted keys
		for(SmallMultiple sm : refs.smallMultiples) {
			
			// Calculate network view position
			int x = current_col * width;
			int y = current_row * height;
			
			// Set network view bounds
			refs.desktopManager.setBounds(sm.getView(), new Rectangle(x, y, width, height));
			//viewMap.get(sortedView).fitContent();
			
			// Go to next column
			current_col++;
			
			// Go to next row if this row is full
			if(current_col==cols) {
				current_col = 0;
				current_row++;
			}
		}
	}
}
