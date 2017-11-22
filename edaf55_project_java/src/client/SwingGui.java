package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class SwingGui extends javax.swing.JFrame {

	private JRadioButton autoRadioButton;
	private JLabel delayLabel1;
	private JLabel delayLabel2;
	private JRadioButton idleRadioButton;
	private JLabel image1;
	private JLabel image2;
	private JLabel modeLabel;
	private JRadioButton movieRadioButton;
	private JCheckBox syncCheckBox;
	private JLabel syncLabel;
	private ImageIcon imageIcon1, imageIcon2;

	private boolean newInput;
	private int mode, syncMode;

	public void StartGui() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SwingGui.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(SwingGui.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(SwingGui.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(SwingGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new SwingGui().setVisible(true);
			}
		});
	}

	public SwingGui() {
		initComponents();
		newInput = false;
		mode = ClientMonitor.MODE_AUTO;
	}

	private void initComponents() {

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./test.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon icon = new ImageIcon(img);
		imageIcon1 = null;
		imageIcon2 = null;

		delayLabel1 = new JLabel();
		syncCheckBox = new JCheckBox();
		autoRadioButton = new JRadioButton();
		idleRadioButton = new JRadioButton();
		modeLabel = new JLabel();
		movieRadioButton = new JRadioButton();
		syncLabel = new JLabel();
		delayLabel2 = new JLabel();
		image1 = new JLabel(icon);
		image2 = new JLabel(icon);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		delayLabel1.setText("20 ms");

		syncCheckBox.setText("Synchronize");
		syncCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				syncCheckBoxActionPerformed(evt);
			}
		});

		autoRadioButton.setText("Auto");
		autoRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				autoRadioButtonActionPerformed(evt);
			}
		});

		idleRadioButton.setText("Idle");
		idleRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				idleRadioButtonActionPerformed(evt);
			}
		});

		modeLabel.setText("Mode");

		movieRadioButton.setText("Movie");
		movieRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				movieRadioButtonActionPerformed(evt);
			}
		});

		syncLabel.setText("synchronized");

		delayLabel2.setText("32 ms");

		generateLayout();
	}

	private void generateLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(image1).addComponent(delayLabel1))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(delayLabel2).addComponent(image2)))
										.addGroup(layout.createSequentialGroup().addComponent(autoRadioButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(idleRadioButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(movieRadioButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 95,
														Short.MAX_VALUE)
												.addComponent(syncLabel))
										.addGroup(layout.createSequentialGroup().addComponent(modeLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(syncCheckBox)))
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(image1).addComponent(image2))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(delayLabel1).addComponent(delayLabel2))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(modeLabel).addComponent(syncCheckBox))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(autoRadioButton).addComponent(idleRadioButton)
												.addComponent(movieRadioButton))
										.addComponent(syncLabel))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pack();
	}

	private void updateImage(byte[] image, JLabel label) {
		try {
			label.setIcon(new ImageIcon(ImageIO.read(new ByteArrayInputStream(image))));
			label.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateImage2(byte[] image) {
		updateImage(image, image2);
	}
	
	public void updateImage1(byte[] image) {
		updateImage(image, image1);
		System.out.println("Image updated in gui");
	}
	
	public void setSyncLabel(boolean synced){
		if(synced){
			syncLabel = new JLabel("Synchronous");
		}else{
			syncLabel = new JLabel("Asynchronous");
		}
		repaint();
	}

	/*
	 * @return a vector containing {Mode, Synchronized}
	 */
	public int[] getInput() {
		try {
			while (!newInput)
				wait();
			newInput = false;
			
			return new int[] {mode,syncMode};

		} catch (InterruptedException e) {
			return null;
		}
	}

	private void autoRadioButtonActionPerformed(ActionEvent evt) {
		idleRadioButton.setSelected(false);
		movieRadioButton.setSelected(false);
		newInput = true;
		mode = ClientMonitor.MODE_AUTO;
		notifyAll();
	}

	private void idleRadioButtonActionPerformed(ActionEvent evt) {
		autoRadioButton.setSelected(false);
		movieRadioButton.setSelected(false);
		newInput = true;
		mode = ClientMonitor.MODE_IDLE;
		notifyAll();
	}

	private void movieRadioButtonActionPerformed(ActionEvent evt) {
		autoRadioButton.setSelected(false);
		idleRadioButton.setSelected(false);
		newInput = true;
		mode = ClientMonitor.MODE_MOVIE;
		notifyAll();
	}

	private void syncCheckBoxActionPerformed(ActionEvent evt) {
		newInput = true;
		syncMode = syncCheckBox.isSelected() ? ClientMonitor.MODE_ASYNC : ClientMonitor.MODE_SYNC;
	}
	
	public static void main(String[] args){
		
		SwingGui gui = new SwingGui();
		gui.StartGui();
		byte[] a = {(byte) 0x99,0x57,0x42,0x23,0x67,0x23,0x53,0x24,(byte) 0xbb};
		gui.updateImage1(a);
		
	}
	
}