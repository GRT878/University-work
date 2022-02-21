package com.basic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class WaitingDialog extends JDialog{
	private static final String DEFAULT_MSG = "ќжидание загрузки...";
	public WaitingDialog(JDialog parent, String Message) {
		super(parent);
		setUndecorated(true);
		setFocusableWindowState(false);
		JPanel pnlContent = new JPanel(new MigLayout("insets 7", "", ""));
		JLabel lblContent = new JLabel();
		lblContent.setText(Message == null ? DEFAULT_MSG : Message);
		pnlContent.add(lblContent);
		pnlContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		getContentPane().add(pnlContent);
		pack();
		setModal(true);
		setLocationRelativeTo(this.getParent());
	}

	public WaitingDialog(String Message) {
		this(null, Message);
	}

	public WaitingDialog() {
		this(null);
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b)
			new Thread() {
				public void run(){
					WaitingDialog.super.setVisible(true);
				};
			}.start();
		else {
			if (SwingUtilities.isEventDispatchThread()) {
				super.setVisible(false);
				dispose();
			} else
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							WaitingDialog.super.setVisible(false);
							dispose();
						}
					});
				} catch (Exception e) {}
		}
	}
}

