package com.editForms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import javax.swing.*;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.Klient;

import net.miginfocom.swing.MigLayout;

public class EdSKlientDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление нового клиента";
	private final static String title_ed = "Редактирование клиента";
	private Klient klient = null;
	private boolean isNewRow = false;
	private JTextField edKodKl;
	private JTextField edNaimKl;
	private JTextField edDoc;
	private JTextField edTel;
	private JButton btnOk;
	private JButton btnCancel;

	public EdSKlientDialog(Window parent, Klient klient, DBManager manager) {
		this.manager = manager;
		isNewRow = klient == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.klient = klient;
			old_key = klient.getKodKl();
		} else
			this.klient = new Klient();
		
		createGui();
		bindListeners();
		loadData();
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	private void bindListeners() {
		setKeyListener(this, new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					setDialogResult(JDialogResult.Cancel);
					close();
					e.consume();
				} else
					super.keyPressed(e);
			}
		});
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		});

		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (!constructKlient())
					return;
				if (isNewRow) {
					if (manager.addKlient(klient)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateKlient(klient, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodKl = new JTextField(25);
		edNaimKl = new JTextField(25);
		edDoc = new JTextField(25);
		edTel = new JTextField(25);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Код"));
		pnl.add(edKodKl,"span");
		pnl.add(new JLabel("Наименование"));
		pnl.add(edNaimKl,"span");
		pnl.add(new JLabel("Номер док."));
		pnl.add(edDoc,"span");
		pnl.add(new JLabel("Телефон"));
		pnl.add(edTel,"span");
		
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.CENTER);
	}
	
	private void setKeyListener(Component c, KeyListener kl) {
		c.addKeyListener(kl);
		if (c instanceof Container)
			for (Component comp:((Container)c).getComponents())
				setKeyListener(comp, kl);
	}
	
	private void loadData() {
		if (!isNewRow){
			edKodKl.setText(klient.getKodKl().toString());
			edNaimKl.setText(klient.getNaimKl());
			edDoc.setText(klient.getDoc());
			edTel.setText(klient.getTel());
		}
	}
	
	private boolean constructKlient()	{
		try {
			klient.setKodKl(edKodKl.getText().equals("") ? 
					null : new BigDecimal(edKodKl.getText()));
			klient.setNaimKl(edNaimKl.getText());
			klient.setDoc(edDoc.getText());
			klient.setTel(edTel.getText());
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public Klient getKlient() {
		return klient;
	}
}
