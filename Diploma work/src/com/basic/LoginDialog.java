package com.basic;

import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class LoginDialog extends JRDialog {
	private JTextField txtLogin;
	private JPasswordField txtPassword;
	private JButton btnEnter;
	private JButton btnCancel;
	private Connection con;
	private DBManager manager;
	private String url_db;
	
	private String log;
	private String pass;
	
	public LoginDialog(DBManager manager) {
		this.manager=manager;
		createGUI();
		setTitle("���������� � ��");
		
		FileInputStream fprop;
		Properties property = new Properties();
		try {
			fprop = new FileInputStream("conf.prop");
			property.load(fprop);
			
			url_db = property.getProperty("URL_DB");
			
			log = property.getProperty("log");
			pass = property.getProperty("pass");
			
			txtLogin.setText(log);
			txtPassword.setText(pass);
			
		} catch (IOException e) {
			System.err.println("������: ���� ������� ����������!");
		}
	}
	
	private void createGUI() {
		MigLayout layout = new MigLayout("insets 12, gapy 5", "[]12[grow, fill][]");
		JPanel res = new JPanel(layout);
		
		res.add(new JLabel("�����:"));
		txtLogin = new JTextField(15);
		res.add(txtLogin, "span");

		res.add(new JLabel("������:"));
		txtPassword = new JPasswordField(15);
		res.add(txtPassword, "span");
     
		btnEnter = new JButton("�����");
		btnCancel = new JButton("������");
		res.add(btnEnter, "gaptop 8,span,split 2,right");
		res.add(btnCancel);
		getContentPane().add(res);
		
		getRootPane().setDefaultButton(btnEnter);
		pack();
		setResizable(false);
		bindListeners();
	}
	
	private void bindListeners() {
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		});
		
		btnEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if (authenticate()) {
					setDialogResult(JDialogResult.OK);
					close();
				}
			}
		});
	}
	
	protected boolean authenticate() {
		String login = null;
		char[] password = null;
		try{
			login = txtLogin.getText().trim();
			password = txtPassword.getPassword();
			if (login.length() == 0 || password.length == 0)
				throw new Exception("����� �/��� ������ �� �������.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"������ ����� ������", JOptionPane.ERROR_MESSAGE);
			txtPassword.setText(null);
			return false;
		}
		
		try {
			String pwd = new String(password);
			con = connect(login, pwd);
			if(con == null)
				throw new Exception("�����,������ ��� ��� �� ������� �� �����");
			manager.setConnection(con);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"������ ����� � �������", JOptionPane.ERROR_MESSAGE);
			txtPassword.setText(null);
			return false;
		}
		return true;
	}
	
	public Connection connect(String log, String pass){
		Locale.setDefault(Locale.ENGLISH);
		try {
			try {
				Class.forName("ianywhere.ml.jdbcodbc.jdbc3.IDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			con=DriverManager.getConnection(url_db+";uid="+log+";pwd="+pass);
			con.setAutoCommit(false);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

