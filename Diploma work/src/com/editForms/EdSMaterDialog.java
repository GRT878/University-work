package com.editForms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.Mater;

public class EdSMaterDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление нового материала";
	private final static String title_ed = "Редактирование материала";
	private Mater mater = null;
	private boolean isNewRow = false;
	private JTextField edKodM;
	private JTextField edNaimM;
	private JTextField edCenaEd;
	private JButton btnOk;
	private JButton btnCancel;

	public EdSMaterDialog(Window parent, Mater mater, DBManager manager) {
		this.manager = manager;
		isNewRow = mater == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.mater = mater;
			old_key = mater.getKodM();
		} else
			this.mater = new Mater();
		
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
				if (!constructMater())
					return;
				if (isNewRow) {
					if (manager.addMater(mater)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateMater(mater, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodM = new JTextField(25);
		edNaimM = new JTextField(25);
		edCenaEd = new JTextField(25);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Код"));
		pnl.add(edKodM,"span");
		pnl.add(new JLabel("Наименование"));
		pnl.add(edNaimM,"span");
		pnl.add(new JLabel("Цена за ед."));
		pnl.add(edCenaEd,"span");
		
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
			edKodM.setText(mater.getKodM().toString());
			edNaimM.setText(mater.getNaimM());
			edCenaEd.setText(mater.getCenaEd().toString());
		}
	}
	
	private boolean constructMater()	{
		try {
			mater.setKodM(edKodM.getText().equals("") ? null : new BigDecimal(edKodM.getText()));
			mater.setNaimM(edNaimM.getText());
			mater.setCenaEd(edCenaEd.getText().equals("") ? null : new BigDecimal(edCenaEd.getText()));
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public Mater getMater() {
		return mater;
	}
}
