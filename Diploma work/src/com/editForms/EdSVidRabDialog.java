package com.editForms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.VidRab;

import net.miginfocom.swing.MigLayout;

public class EdSVidRabDialog extends JRDialog{
	private DBManager manager;
	private String old_key;
	private final static String title_add = "Добавление нового вида работ";
	private final static String title_ed = "Редактирование вида работ";
	private VidRab vidRab = null;
	private boolean isNewRow = false;
	private JTextField edVidRab;
	private JButton btnOk;
	private JButton btnCancel;

	public EdSVidRabDialog(Window parent, VidRab vidRab, DBManager manager) {
		this.manager = manager;
		isNewRow = vidRab == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.vidRab = vidRab;
			old_key = vidRab.getVidRab();
		} else
			this.vidRab = new VidRab();
		
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
				if (!constructVidRab())
					return;
				if (isNewRow) {
					if (manager.addVidRab(vidRab)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateVidRab(vidRab, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edVidRab = new JTextField(25);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Вид работы"));
		pnl.add(edVidRab,"span");
		
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
			edVidRab.setText(vidRab.getVidRab());
		}
	}
	
	private boolean constructVidRab()	{
		try {
			vidRab.setVidRab(edVidRab.getText().equals("") ? null : edVidRab.getText());
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public VidRab getVidRab() {
		return vidRab;
	}
}
