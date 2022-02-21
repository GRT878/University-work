package com.basic;

import java.awt.event.*;
import javax.swing.*;

public class JRDialog extends JDialog{
	private JDialogResult dialogResult = JDialogResult.None;
	public JDialogResult getDialogResult() {
		return dialogResult;
	}
	
	public void setDialogResult(JDialogResult dialogResult) {
		this.dialogResult = dialogResult;
	}
	
	public JDialogResult showDialog() {
		setLocationRelativeTo(JRDialog.this.getParent());
		setModal(true);
		addWindowListener(new WindowAdapter() {
		@Override
			public void windowClosing(WindowEvent e)
			{
				setDialogResult(JDialogResult.Cancel);
				dispose();
			}
		});
		setVisible(true);
		return getDialogResult();
	}
	
	public void close() {
		dispose();
	} 
}
