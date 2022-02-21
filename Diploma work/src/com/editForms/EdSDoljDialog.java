package com.editForms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.Dolj;

public class EdSDoljDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление новой должности";
	private final static String title_ed = "Редактирование должности";
	private Dolj dolj = null;
	private boolean isNewRow = false;
	private JTextField edKodDol;
	private JTextField edNaimDol;
	private JButton btnOk;
	private JButton btnCancel;

	public EdSDoljDialog(Window parent, Dolj dolj, DBManager manager) {
		this.manager = manager;
		isNewRow = dolj == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.dolj = dolj;
			old_key = dolj.getKodDol();
		} else
			this.dolj = new Dolj();
		
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
				if (!constructDolj())
					return;
				if (isNewRow) {
					if (manager.addDolj(dolj)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateDolj(dolj, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodDol = new JTextField(25);
		edNaimDol = new JTextField(25);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Код"));
		pnl.add(edKodDol, "span");
		pnl.add(new JLabel("Наименование"));
		pnl.add(edNaimDol, "span");
		
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
			edKodDol.setText(dolj.getKodDol().toString());
			edNaimDol.setText(dolj.getNaimDol());
		}
	}
	
	private boolean constructDolj()	{
		try {
			dolj.setKodDol(edKodDol.getText().equals("") ? 
					null : new BigDecimal(edKodDol.getText()));
			dolj.setNaimDol(edNaimDol.getText());
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public Dolj getDolj() {
		return dolj;
	}
}
