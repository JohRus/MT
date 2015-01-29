package gui;

import javax.swing.SwingUtilities;


public class GUI {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SystemFrame frame = new SystemFrame();
				frame.showUI();
			}
		});
	}
}
