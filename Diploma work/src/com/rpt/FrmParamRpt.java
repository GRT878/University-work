package com.rpt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Label;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.helper.HelperConverter;

public class FrmParamRpt extends JRDialog{
	private DBManager manager;
	private JFormattedTextField edBegDate;
	private JFormattedTextField edEndDate;
	private JButton btnOk;
	private JButton btnCancel;
	private RptParams params;
	
	public FrmParamRpt(Window parent, DBManager manager) {
		this.manager = manager;
		params = new RptParams();
		setTitle("Параметры для отчета");
		
		createGui();
		bindListeners();
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
				if (constructParams() != false){
					manager.ShowRpt(params);
					setDialogResult(JDialogResult.OK);
					close();
				}
			}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]","[]5[]5[]"));
		edBegDate = new JFormattedTextField(HelperConverter.createFormatter("##-##-####"));
		edEndDate = new JFormattedTextField(HelperConverter.createFormatter("##-##-####"));
		
		btnOk = new JButton("Вывод");
		btnCancel = new JButton("Отмена");
		
		pnl.add(new Label("Период"), "span");
		pnl.add(new JLabel("Начало"));
		pnl.add(edBegDate,"span, w 100");
		pnl.add(new JLabel("Конец"));
		pnl.add(edEndDate,"span, w 100");
		
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.CENTER);
	}
	
	private boolean constructParams() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			params.begDate = formatter.parse(edBegDate.getText());
			params.endDate = formatter.parse(edEndDate.getText());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private void setKeyListener(Component c, KeyListener kl) {
		c.addKeyListener(kl);
		if (c instanceof Container)
			for (Component comp:((Container)c).getComponents())
				setKeyListener(comp, kl);
	}
}
