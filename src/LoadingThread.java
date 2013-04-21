import java.io.File;
import java.net.URL;
import javax.swing.*;

public class LoadingThread extends Thread {

	private URL url;

	public void run() {
		try {
			url = LoadingThread.class.getResource(("/GIFResource/loadingscreen.gif"));

			Icon icon = new ImageIcon(url);
			JLabel label = new JLabel(icon);

			JFrame f = new JFrame("Loading...please wait.");
			f.getContentPane().add(label);
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setResizable(false);
			f.pack();
			f.setLocationRelativeTo(null);
			f.setVisible(true);
			while(Driver.loading) {
				Thread.sleep(10);
			}
			f.dispose();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}