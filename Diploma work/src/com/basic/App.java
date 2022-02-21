package com.basic;

import java.awt.Color;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.gui.LookAndFillUtil;

public class App {
	private DBManager manager=null;
	
	public App() {}
	 
	public void run(){
		if (showLoginWnd()) {
			showMainWnd();}
		else {
			closeApplication();
		}
	}
	
	private boolean showLoginWnd() {
		try {
			manager = new DBManager();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		LoginDialog dlg = new LoginDialog(manager);
		JDialogResult res = dlg.showDialog();
		return (res == JDialogResult.OK);
	}
	
	private void showMainWnd() {
		WaitingDialog wd = new WaitingDialog("Загружается программа...");
		wd.setVisible(true);
		MainWindow mainWindow = null;
		try {
			mainWindow = createMainWindow(manager);
			mainWindow.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) 	{
					closeApplication();
				}
			});
			mainWindow.initialize();
			wd.setVisible(false);
			wd.dispose();
			mainWindow.setVisible(true);
		} catch (Exception ex) {
			String s = ex.getMessage();
			JOptionPane.showMessageDialog(null, s, "Ошибка", JOptionPane.ERROR_MESSAGE);
			wd.setVisible(false);
			wd.dispose();
		}
	}
	
	private MainWindow createMainWindow(DBManager manager){
		return new MainWindow(manager);
	}
	
	private void closeApplication() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		App app=new App();
		app.run();  
	}   
}

