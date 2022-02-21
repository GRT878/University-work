package com.basic;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.rpt.FrmParamRpt;
import com.viewingForms.FrmBookZakaz;
import com.viewingForms.FrmBookZakazN;
import com.viewingForms.FrmSDolj;
import com.viewingForms.FrmSKlient;
import com.viewingForms.FrmSMater;
import com.viewingForms.FrmSRabota;
import com.viewingForms.FrmSSotrud;
import com.viewingForms.FrmSVidRab;
import com.viewingForms.FrmSimpleZapr;

import net.miginfocom.swing.MigLayout;

public class MainWindow extends JFrame {
	private DBManager manager;
	
	JMenuBar mainMenu;
	JMenuItem miExit;
	JMenu mSpr;
	JMenuItem miSMater;
	JMenuItem miSVidRab;
	JMenuItem miSRabota;
	JMenuItem miSDolj;
	JMenuItem miSSotrud;
	JMenuItem miSKlient;
	
	JMenu mOper;
	JMenuItem miZapr;
	JMenuItem miZakazNBook;
	JMenuItem miZakazBook;
	JMenuItem miStat;

	public MainWindow(DBManager manager) {
		this.manager = manager;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testConn();
		manager.setPath();
		createGUI();
		bindListeners();
		pack();
		setTitle("Учет ремонтных работ автосервиса");
		setLocationRelativeTo(null);
	}
	
	private void bindListeners() {
		miExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);             
			}
		});	
		
		miZapr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowZapr();
			}
		});

		miSMater.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowSMater();
			}
		});
		
		miSDolj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowSDolj();
			}
		});
		
		miSKlient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowSKlient();
			}
		});
		
		miSVidRab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowSVidRab();
			}
		});
		
		miSSotrud.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowSSotrud();
			}
		});
		
		miSRabota.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowSRabota();
			}
		});
		
		miZakazNBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowZakazNBook();
			}
		});
		
		miZakazBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowZakazBook();
			}
		});
		
		miStat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowRptParams();
			}
		});
	}

	protected void ShowZapr() {
		FrmSimpleZapr frm = new FrmSimpleZapr(manager);
		frm.setVisible(true);
	}
	
	protected void ShowSMater() {
		FrmSMater frm = new FrmSMater(manager);
		frm.setVisible(true);
	}
	
	protected void ShowSDolj() {
		FrmSDolj frm = new FrmSDolj(manager);
		frm.setVisible(true);
	}
	
	protected void ShowSKlient() {
		FrmSKlient frm = new FrmSKlient(manager);
		frm.setVisible(true);
	}
	
	protected void ShowSVidRab() {
		FrmSVidRab frm = new FrmSVidRab(manager);
		frm.setVisible(true);
	}
	
	protected void ShowSSotrud() {
		FrmSSotrud frm = new FrmSSotrud(manager);
		frm.setVisible(true);
	}
	
	protected void ShowSRabota() {
		FrmSRabota frm = new FrmSRabota(manager);
		frm.setVisible(true);
	}
	
	protected void ShowZakazNBook() {
		FrmBookZakazN frm = new FrmBookZakazN(manager);
		frm.setVisible(true);
	}
	
	protected void ShowZakazBook() {
		FrmBookZakaz frm = new FrmBookZakaz(manager);
		frm.setVisible(true);
	}
	
	public void ShowRptParams() {
		FrmParamRpt frm = new FrmParamRpt(this, manager);
		frm.setVisible(true);
	}

	private void testConn() {
		String ver = manager.getVersion();
		System.out.println(ver);
	}
	
	private void createGUI() {
		JPanel pnlImg = new JPanel(new BorderLayout());
		
		URL url = this.getClass().getResource("/images/car1.jpg");
		BufferedImage wPic=null;
	    try {
	    	wPic = ImageIO.read(url);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    JLabel wIcon = new JLabel(new ImageIcon(wPic));
	    pnlImg.add(wIcon, BorderLayout.CENTER);
		
	    getContentPane().setLayout(new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]", "[grow, fill]"));
	    getContentPane().add(pnlImg, "grow");
	    
	    setJMenuBar(createMenu());
	}
	
	private JMenuBar createMenu(){
		mainMenu = new JMenuBar();
		
		mSpr = new JMenu("Справочники");
		miSMater = new JMenuItem("Материалы");
		miSDolj = new JMenuItem("Должности");
		miSKlient = new JMenuItem("Клиенты");
		miSRabota = new JMenuItem("Работы");
		miSSotrud = new JMenuItem("Сотрудники");
		miSVidRab = new JMenuItem("Виды работ");

		mOper = new JMenu("Операции");
		miZapr = new JMenuItem("Простой запрос");
		miZakazNBook = new JMenuItem("Книга заказ-нарядов");
		miZakazBook = new JMenuItem("Книга заказов");
		miStat = new JMenuItem("Отчет");
		
		miExit = new JMenuItem("Выход");
		
		mSpr.add(miSMater);
		mSpr.addSeparator();
		mSpr.add(miSDolj);
		mSpr.add(miSSotrud);
		mSpr.addSeparator();
		mSpr.add(miSVidRab);
		mSpr.add(miSRabota);
		mSpr.addSeparator();
		mSpr.add(miSKlient);
		
		mOper.add(miZapr);
		mOper.addSeparator();
		mOper.add(miZakazNBook);
		mOper.add(miZakazBook);
		mOper.addSeparator();
		mOper.add(miStat);
		
		mainMenu.add(mSpr);
		mainMenu.add(mOper);
		mainMenu.add(miExit);

		return mainMenu;
	}


	public void initialize() {
		Locale.setDefault(new Locale("ru"));
	}
}

