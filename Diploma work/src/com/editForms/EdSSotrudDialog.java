package com.editForms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.Sotrud;
import com.data.Dolj;

import net.miginfocom.swing.MigLayout;

public class EdSSotrudDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление нового сотрудника";
	private final static String title_ed = "Редактирование сотрудника";
	private Sotrud sotrud = null;
	private boolean isNewRow = false;
	private JTextField edKodSotr;
	private JTextField edFIO;
	private JTextField edDoljKod;
	private JComboBox cmbDolj;
	private JButton btnOk;
	private JButton btnCancel;

	public EdSSotrudDialog(Window parent, Sotrud sotrud, DBManager manager) {
		this.manager = manager;
		isNewRow = sotrud == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.sotrud = sotrud;
			old_key = sotrud.getKodSotr();
		} else
			this.sotrud = new Sotrud();
		
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
				if (!constructSotrud())
					return;
				if (isNewRow) {
					if (manager.addSotrud(sotrud)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateSotrud(sotrud, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
		
		cmbDolj.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						Dolj dolj = (Dolj) e.getItem();
						edDoljKod.setText(dolj.getKodDol().toString());
					}
				}
			}
		});
		
		edDoljKod.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbDolj.getModel();
				BigDecimal doljKod = new BigDecimal(edDoljKod.getText());
				setCmbItem(model, doljKod);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});

	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodSotr = new JTextField(25);
		edFIO = new JTextField(25);
		edDoljKod = new JTextField(5);
		cmbDolj = new JComboBox();
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Код"));
		pnl.add(edKodSotr, "span");
		pnl.add(new JLabel("ФИО"));
		pnl.add(edFIO, "span");
		pnl.add(new JLabel("Должность"));
		pnl.add(edDoljKod, "split 2");
		pnl.add(cmbDolj, "growx, wrap, h 20!");
		
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
			edKodSotr.setText(sotrud.getKodSotr().toString());
			edFIO.setText(sotrud.getFio());
			edDoljKod.setText(sotrud.getDolj().getKodDol() == null ? "" : sotrud.getDolj().getKodDol().toString());
		}
		ArrayList<Dolj> lst = manager.loadDoljForCmb();
		if (lst != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lst.toArray());
			cmbDolj.setModel(model);
			BigDecimal doljKod = (isNewRow? null : sotrud.getDolj().getKodDol());
			setCmbItem(model, doljKod);
		}
	}
	
	private void setCmbItem(DefaultComboBoxModel model, BigDecimal doljKod) {
		cmbDolj.setSelectedItem(null);
		if (doljKod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Dolj)model.getElementAt(i)).getKodDol().equals(doljKod)) {
					cmbDolj.setSelectedIndex(i);
					break;
				}
	}
	
	private boolean constructSotrud()	{
		try {
			sotrud.setKodSotr(edKodSotr.getText().equals("") ? null : new BigDecimal(edKodSotr.getText()));
			sotrud.setFio(edFIO.getText());
			Object obj = cmbDolj.getSelectedItem();
			Dolj dolj = (Dolj) obj;
			sotrud.setDolj(dolj);

			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public Sotrud getSotrud() {
		return sotrud;
	}
}
