package com.viewingForms;

import java.awt.FlowLayout;
import java.sql.*;

import javax.swing.*;

import com.basic.DBManager;
import com.data.Mater;

public class FrmSimpleZapr extends JDialog {
	DBManager manager;
	JPanel pnl;
	JTextArea ta;
	
	public FrmSimpleZapr(DBManager manager){
		super();
		this.manager=manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		createGUI();
		loadData();
		pack();
		setTitle("Окно с результатом запроса");
		setLocationRelativeTo(this);
	}
	
	private void createGUI() {
		pnl = new JPanel(new FlowLayout());
		ta = new JTextArea(10,20);
		pnl.add(ta);
		getContentPane().add(pnl);
	}
	
	private void loadData() {
		Connection conn = manager.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_m, naim_m, cena_ed FROM Mater");		
			while (res.next())   {
				Mater m = new Mater();
				m.setKodM(res.getBigDecimal(1));
				m.setNaimM(res.getString(2));
				m.setCenaEd(res.getBigDecimal(3));
				ta.append(m.toString()+"\n");
			}	
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка получения данных",JOptionPane.ERROR_MESSAGE);
		}
	}
}
