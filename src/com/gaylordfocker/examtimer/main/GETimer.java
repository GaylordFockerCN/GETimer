package com.gaylordfocker.examtimer.main;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
//import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MenuItem;
//import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import com.gaylordfocker.examtimer.auxiliary.*;

/**
 * 
 * 考试Σ(っ°Д°;)っ计时器
 * @author GaylordFocker
 * 
 */

@SuppressWarnings(value = {"deprecation","serial","static-access"})

public class GETimer extends JDialog{
	
	////////////////////
	private static final String version = "2.5.0F";
	////////////////////
	
	private MyImage cheerImg,bgImg,Tip,settingBtn,hundreds,tens,ones,day;
	
	private Image oneToNineAndX[],cheerImgH,cheerImgM,bg1,bg2,bg3,
			btn1,btn1s1,btn1s2,btn1s3,btn2,custonImg;
	
	private JPanel settingPn;
	
	private MyPanel myPanel;
	
	private SplashPanel splash;
	
	private JLabel markLb,nowsize,colorsample,forealphaLb,bgalphaLb;
	
	private JTextField examTypeTf,yearTf,monthTf,dayTf,hourTf,minTf,imgName;
	
	private JTextArea updatedialog;
	
	private JCheckBox AlwaysOnTopCb,SetAsDesktopCb,startUpCb,
	authorHandsomeCb,drawCheerCb,doubleClickCloseCb,dayUpCb;
	
	private ButtonGroup bggroup,typegroup;
	
	private JRadioButton bg1btn,bg2btn,bg3btn,highbtn,midbtn,customTypeBtn,custombtn;
	
	private Preferences node;
	
	private JButton OK,cancel,filechoose,colorchoose;
	
	private JFileChooser filechooser;
	
	private JColorChooser colorchooser;
	
	private JSlider size,bgalpha,forealpha;
	
	private TrayIcon trayIcon;
	
	private SystemTray tray;
	
	private File today,img;
	
//	private TimerFrame frame;
	
	public static final int DEFAULT_HEIGHT = 800,DEFAULT_WIDTH = 1200,PANEL_HEIGHT = 300,PANEL_WIDTH = 450;
	
	private int x,y,fontSize = 18,stringX = 35,stringY = 280,nowfontSize=fontSize,nowstringX=stringX,nowstringY=stringY,nowDay;
	
	private boolean drawCheerBorder = true,drawTip = false,complete = false,btnHide = false,
			isSetting = false,bug = false,systemSupport = false,sleep = true;
	
	private String poorTIME = "",examType = "高",todayStr = "today.bmp",loadingpercent = "0";
	
	private Date examDate,nowDate;//为啥要用Date不用Calendar呢？因为我懒。觉得Date简单明了
	
	private Color nowColor;
	
	private FileOutputStream out;
	
	private JDialog dialog;
	
//	private Point p;
	
	public GETimer() {

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//		setTitle("GETimer"+version);
		setIconImage(new ImageIcon("image/icon/timer.png").getImage());
		setUndecorated(true);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(0,0,0,0));

        setTray();
		
		today = new File(todayStr);
		
		node = Preferences.userRoot().node("ExamTimer");
		
		try {
			node.clear();//清除缓存，调试用
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}
		
		if(new File("data.xml").exists()) {
			try {
				node.importPreferences(new FileInputStream("data.xml"));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "导入设置异常！", "错误", JOptionPane.ERROR_MESSAGE);
			} catch (InvalidPreferencesFormatException e1) {
				JOptionPane.showMessageDialog(null, "data文件损坏！无法读取手动更改的设置！", "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
		node.put(version, new File("").getAbsolutePath());//后期更新用

//		try {
//			node.clear();//清除缓存，调试用
//		} catch (BackingStoreException e1) {
//			e1.printStackTrace();
//		}
		
		
		examDate = new Date();
		
		myPanel = new MyPanel();
		
		initSettingPanel();
		dialog = new JDialog(this,"设置",true);
		dialog.add(settingPn);
//		dialog.setUndecorated(true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setSize(PANEL_WIDTH+30, PANEL_HEIGHT+30);
		dialog.setLocationRelativeTo(null);
		dialog.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				readSettings();
				cancel();
			}
		});
		
		initImage();
		readLocation();
		readSettings();

		JPopupMenu pop = new JPopupMenu();
		JMenuItem toSet = new JMenuItem("         进入设置");
		JMenuItem hide = new JMenuItem("显示/隐藏 设置图标");
		btnHide = node.getBoolean("btnHide", false);
		toSet.addActionListener(e->{
			dialog.setVisible(true);
		});
		hide.addActionListener(e->{
			btnHide = !btnHide;
			node.putBoolean("btnHide", btnHide);
		});
		pop.add(toSet);
		pop.addSeparator();
		pop.add(hide);
		
		addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseExited(MouseEvent e) {
				drawTip = false;
				sleep = true;
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				drawTip = true;
			}
			@Override
			public void mousePressed(MouseEvent e){                 
				x = e.getX();
                y = e.getY();
            }
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					pop.show(myPanel, x, y);
				}
				if(e.getClickCount()==2&&e.getButton()==MouseEvent.BUTTON1) {
					if(doubleClickCloseCb.isSelected()) {
						export();
						System.exit(0);
					}
					setVisible(false);
				}
				if(!btnHide&&e.getButton() == MouseEvent.BUTTON1
						&&settingBtn.getBounds().contains(e.getPoint())) {
					toSet();
				}
			}
		});
        addMouseMotionListener(new MouseMotionAdapter(){
        	@Override
        	public void mouseDragged(MouseEvent e){
        		setLocation((e.getXOnScreen()-x),(e.getYOnScreen()-y));
        		node.putInt("x", getX());//及时保存坐标。
        		node.putInt("y", getY());
        	}
      
			@Override
        	public void mouseMoved(MouseEvent e) {
				if(!isSetting){
	        		if(!btnHide&&settingBtn.getBounds().contains(e.getPoint())) {
	        			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        			settingBtn.setImage(btn2);
	        			sleep = false;
	        		}
	        		else {
	        			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	        			sleep = true;
	        		}
				}
        	}
        });
        
        new Thread(()->{
        	try {
        		while(true) {
        			if(!isSetting){
        				poorTIME = getDatePoor();	
        			}
        			Thread.sleep(100);//防止无意义死循环导致死机
        			repaint();
        		}
        	}
        	catch(InterruptedException e) {
        	}
        }).start();
        
        setEnabled(true);
        add(myPanel);
        
        if(node.getBoolean("isFirstRun", true)) {
        	String osName = System.getProperty("os.name");
        	systemSupport = osName.equals("Windows XP")||osName.equals("Windows 7");
        	String tip = systemSupport?"您的系统应该可以正常运行此程序":"您的系统恐怕无法正常使用此程序";
        	JOptionPane.showMessageDialog(this, "初次运行...请先完成设置..."+tip, "", JOptionPane.CANCEL_OPTION);
        	node.putBoolean("isFirstRun", false);
        	node.putBoolean("systemSupport", systemSupport);
        	setCompsEnabled(systemSupport);//readSetting中默认为系统支持。
        	toSet();
        }else {
        	checkUpdate();
        }
        
        setVisible(true);
        
	}
	
	public void toSet() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		isSetting = true;
		dialog.setVisible(true);
	}
	
	public void checkUpdate() {//从2.5.0开始
		File v2_5_0 = new File(node.get("path", "不存在"));
		if(v2_5_0.exists()&&JOptionPane.showConfirmDialog(null, "在您的系统上发现旧版本("+v2_5_0.getName()+")，是否删除旧版本？", 
				"更新提醒", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			if(MyToolKit.delFiles(v2_5_0)) {
				JOptionPane.showMessageDialog(null,"成功删除~", "成功", JOptionPane.DEFAULT_OPTION);
			}else {
				JOptionPane.showMessageDialog(null,"无法成功删除，发生未知错误", "错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void setTray() {
		
		tray = SystemTray.getSystemTray();
		
		PopupMenu pop = new PopupMenu();
		
		MenuItem about = new MenuItem("About us..");
		about.addActionListener((e)->{
			setAlwaysOnTop(false);//防止弹窗被遮挡
			JOptionPane.showMessageDialog(null, "程序猿：Gaylord Focker..."
					+ "美工：Gaylord Focker...QQ:1070402601...", "关于", JOptionPane.DEFAULT_OPTION);
			setAlwaysOnTop(AlwaysOnTopCb.isSelected());
		});
		pop.add(about);
		
		pop.addSeparator();
		
		MenuItem exit = new MenuItem("EXIT (Cruelly!)");
		exit.addActionListener((e)->{
			export();
			System.exit(0);
		});
		pop.add(exit);
		trayIcon = new TrayIcon(new ImageIcon("image/icon/timer.png").getImage(), "version-"+version+"\n考试倒计时器随时待命.\n距离"+examType+"还有"+nowDay+"天", pop);
		trayIcon.setImageAutoSize(true);//图片太大会显示不了！
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					setVisible(true);
				}
			}
		});
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		dispose();
		
	}
	
	class MyPanel extends JPanel{
		
		@Override
		public void paint(Graphics g1) {
			Graphics2D g = (Graphics2D)g1;
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
	                    RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
			BufferedImage time = new BufferedImage(bgImg.getBounds().width+hundreds.getBounds().x, bgImg.getBounds().height+hundreds.getBounds().y, BufferedImage.TYPE_INT_ARGB);
			Graphics tg = time.getGraphics();
			hundreds.draw(tg);
			tens.draw(tg);
			ones.draw(tg);
			day.draw(tg);
			//有关透明度的操作:
			if(bgImg.getImage()!=null) {//包括 选择 透明背景 和 选择 未选择的自定义背景 两种情况
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP,(float)bgalpha.getValue()/100));
				bgImg.draw(g);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,(float)forealpha.getValue()/100));
			}else {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP,(float)forealpha.getValue()/100));
			}
			g.drawImage(time, 0, 0, null);
			g.setFont(new Font("System",Font.BOLD,nowfontSize));
			g.setColor(nowColor);
			g.drawString("距"+(customTypeBtn.isSelected()?"":yearTf.getText())+examType+"还有"+poorTIME, nowstringX, nowstringY);

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			if(!btnHide) {
				settingBtn.draw(g);
				if(drawTip) {
					Tip.draw(g);
				}
			}
			if(drawCheerBorder) {
				cheerImg.draw(g);
			}
		}
	}
	
	class SplashPanel extends JPanel{
		
		private Image img;
		
		private int x = Toolkit.getDefaultToolkit().getScreenSize().width/2-150;
		private int y = Toolkit.getDefaultToolkit().getScreenSize().height/2-100;
		
		
		public SplashPanel(String imgpath){
			img = new ImageIcon(imgpath).getImage();
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(img, x, y,300,200, null);
			g.setFont(new Font("System",Font.BOLD,20));
			g.setColor(Color.black);
			g.drawString("Loading..."+loadingpercent+"%", x+145, y+60);
		}
		
	}

	public void changeSize(int percent) {
		bgImg.changeSize(percent);
		settingBtn.changeSize(percent);
		hundreds.changeSize(percent);
		tens.changeSize(percent);
		ones.changeSize(percent);
		Tip.changeSize(percent);
		day.changeSize(percent);
		cheerImg.changeSize(percent);
		nowfontSize = fontSize * percent / 100;
		nowstringX = stringX * percent / 100;
		nowstringY = stringY * percent / 100;
	}
	
	public void initImage() {
		
		cheerImg = new MyImage(0, 30, PANEL_WIDTH+30, PANEL_HEIGHT+30, "");
		cheerImgH = new ImageIcon("image/cheer/cheer_high.png").getImage();
		cheerImgM = new ImageIcon("image/cheer/cheer_mid.png").getImage();
		bgImg = new MyImage(20, 80, PANEL_WIDTH-40, PANEL_HEIGHT-80, "");
		bg1 = new ImageIcon("image/bg/bg1.png").getImage();
		bg2 = new ImageIcon("image/bg/bg2.png").getImage();
		bg3 = new ImageIcon(/*透明*/).getImage();
	
		Tip = new MyImage(120, -15, 150, 80, "image/tip/tip.png");
		
		day = new MyImage(20, 80, PANEL_WIDTH-40, PANEL_HEIGHT-80, "image/bg/day.png");
		
		settingBtn = new MyImage(50, 20, 100, 100, "");
		
		btn1 = new ImageIcon("image/role/down.png").getImage();
//		btn1s = new ImageIcon("image/role/down.gif").getImage();//睡眠特效(gif效果不好)
		btn1s1 = new ImageIcon("image/role/down1.png").getImage();
		btn1s2 = new ImageIcon("image/role/down2.png").getImage();
		btn1s3 = new ImageIcon("image/role/down3.png").getImage();
		btn2 = new ImageIcon("image/role/up.png").getImage();
		new Thread(()->{
			try {
				int t = 1;
				while(true) {
					Thread.sleep(400);
					t++;
					if(t>3) {
						t = 1;
					}
					switch(t) {
						case 1:btn1 = btn1s1;break;
						case 2:btn1 = btn1s2;break;
						case 3:btn1 = btn1s3;
					}
					if(sleep) {
						settingBtn.setImage(btn1);
					}
				}
			} catch (InterruptedException e) {}
		}).start();
		
		hundreds = new MyImage(78, 100, 120, 180, "");
		tens = new MyImage(170, 100, 120, 180, "");
		ones = new MyImage(260, 100, 120, 180, "");
		oneToNineAndX = new Image[11];
		for(int i = 0;i < 11; i++) {
			oneToNineAndX[i] = new ImageIcon("image/oneToNineAndX/"+i+".png").getImage();
		}
		
	}
	
	public void initSettingPanel() {
		
		settingPn = new JPanel();
		settingPn.setLayout(new BorderLayout());
		
		JTabbedPane jtp = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		
		JPanel ordinaryPn = new JPanel();
		JPanel preferencePn = new JPanel();
		
		markLb = new JLabel("Copyright  ©  2019-2020 Gaylord Focker 版权所有                         QQ:1070402601");
		settingPn.add(markLb,BorderLayout.NORTH);
		
		AlwaysOnTopCb = new JCheckBox("置顶");
		AlwaysOnTopCb.setToolTipText("设置后窗口将会排在其他窗口之前");
		
		SetAsDesktopCb = new JCheckBox("设为桌面壁纸");
		
		startUpCb = new JCheckBox("开机启动");
		startUpCb.setToolTipText("设置后将在开机时自动打开此程序");
		
		doubleClickCloseCb = new JCheckBox("双击退出");
		doubleClickCloseCb.setToolTipText("设置后双击将退出程序，否则双击将最小化到托盘");
		
		drawCheerCb = new JCheckBox(" \"加油\"边框 ");
		drawCheerCb.setToolTipText("设置后将展示\"加油\"字样，仅限高考中考（懒惰成性）");
		
		authorHandsomeCb = new JCheckBox("作者真帅");//自嗨
		authorHandsomeCb.setSelected(true);
		
		dayUpCb = new JCheckBox("天数进一(不足一天按一天算)");
		
		bggroup = new ButtonGroup();
		
		bg1btn = new JRadioButton("君の名は");
		bg2btn = new JRadioButton("小清新~");
		bg3btn = new JRadioButton("透明背景");
		custombtn = new JRadioButton("自定义背景");
		
		bggroup.add(bg1btn);
		bggroup.add(bg2btn);
		bggroup.add(bg3btn);
		bggroup.add(custombtn);
		
		typegroup = new ButtonGroup();
		
		highbtn = new JRadioButton("高考");
		midbtn = new JRadioButton("中考");
		customTypeBtn = new JRadioButton("自定义");
		examTypeTf = new JTextField(6);
		examTypeTf.addKeyListener(new KeyAdapter() {//限制输入长度
			public void keyTyped(KeyEvent e) {
				if(examTypeTf.getText().length() >= 5) {
					e.consume();
				}
			}
		});

		typegroup.add(highbtn);
		typegroup.add(midbtn);
		typegroup.add(customTypeBtn);
		
		yearTf = new JTextField(3);
		monthTf = new JTextField(2);
		dayTf = new JTextField(2);
		hourTf = new JTextField(2);
		minTf = new JTextField(2);
		
		imgName  = new JTextField(18);
		imgName.setEditable(false);
		
		size = new JSlider(50,200,100);
		nowsize = new JLabel("100%");
		size.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				nowsize.setText(size.getValue()+"%");
			}
		});
		
		forealpha = new JSlider(10,100,100);
		forealphaLb = new JLabel("100%");
		forealpha.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				forealphaLb.setText(forealpha.getValue()+"%");
			}
		});
		
		bgalpha = new JSlider(10, 100, 100);
		bgalphaLb = new JLabel("100%");
		bgalpha.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				bgalphaLb.setText(bgalpha.getValue()+"%");
			}
		});
		
		JPanel southPn = new JPanel();

		filechooser = new JFileChooser();
		filechooser.setDialogTitle("选择图片文件");
        JLabel aImg = new JLabel();
		filechooser.setAccessory(aImg);
        filechooser.addPropertyChangeListener(new PropertyChangeListener(){
			
			@Override
			public void propertyChange(PropertyChangeEvent pe){
				
				if(pe.getPropertyName().equals( JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)){ 
					File f = (File)pe.getNewValue();
					if(f!=null && f.isFile()){ 
						aImg.setIcon(new ImageIcon(new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(150, 100, Image.SCALE_FAST)));
					}
				}
			}
		});
        
        FileFilter filter = new FileFilter() {

			@Override
			public String getDescription() {
				return "图片文件 (*.JPG;*.PNG)";
			}
			
			@Override
			public boolean accept(File f) {
				String s = f.getName().toLowerCase();
				return s.endsWith(".png")||s.endsWith(".jpg")||f.isDirectory();
			}
        };
		filechooser.setFileFilter(filter);

		filechoose = new JButton("浏览");
		filechoose.addActionListener((e)->{
			if(filechooser.showOpenDialog(GETimer.this) == JFileChooser.APPROVE_OPTION&&filechooser.getSelectedFile().exists()/*防止乱输入文件名导致文件不存在*/) {
				if(filter.accept(filechooser.getSelectedFile()/*防止文件选择错误格式*/)){
					JOptionPane.showConfirmDialog(null, "文件选择格式错误！", "错误", JOptionPane.OK_OPTION);
				}else {
					img = filechooser.getSelectedFile();
					imgName.setText(img.getName());
					custombtn.setSelected(true);
				}
				
			}
		});
		
		colorchooser = new JColorChooser();
		
		colorchoose = new JButton("选择颜色");
		colorchoose.addActionListener((e)->{
			nowColor = colorchooser.showDialog(this, "请选择颜色", Color.black);
			if(nowColor!=null) {
				colorsample.setForeground(nowColor);
			}else {
				return;
			}
		});
		
		colorsample = new JLabel("当前颜色");
		
		cancel = new JButton("取消");
		cancel.addActionListener((e)->{
			readSettings();
			cancel();
		});
		
		OK = new JButton("确定");
		OK.addActionListener((e)->{
			check();
			if(!bug) {
				saveSettings();//及时保存并导出防止异常关闭（比如你忽略它而直接关机→_→）
				export();
				readSettings();
				cancel();
			}
		});
		
		southPn.add(OK);
		southPn.add(cancel);
		settingPn.add(southPn,BorderLayout.SOUTH);
		settingPn.setBorder(BorderFactory.createTitledBorder("current_version——"+version));
		ordinaryPn.setLayout(new GridBagLayout());
		preferencePn.setLayout(new GridBagLayout());
		
		ordinaryPn.add(new JLabel("倒计时类型:"),new GBC(0,0));
		ordinaryPn.add(highbtn,new GBC(1,0,1,1));
		ordinaryPn.add(midbtn,new GBC(2,0,2,1).setAnchor(GBC.WEST));
		ordinaryPn.add(customTypeBtn,new GBC(4,0,2,1).setAnchor(GBC.WEST));
		ordinaryPn.add(examTypeTf,new GBC(6,0,2,1).setAnchor(GBC.WEST));
		
		ordinaryPn.add(new JLabel("倒计时时间:"),new GBC(0,1));
		ordinaryPn.add(yearTf,new GBC(1,1).setAnchor(GBC.EAST));
		ordinaryPn.add(new JLabel("年"),new GBC(2,1).setAnchor(GBC.WEST));
		ordinaryPn.add(monthTf,new GBC(3,1).setAnchor(GBC.CENTER));
		ordinaryPn.add(new JLabel("月      "/*战术空格*/),new GBC(3,1).setAnchor(GBC.EAST));
		ordinaryPn.add(dayTf,new GBC(4,1).setAnchor(GBC.EAST));
		ordinaryPn.add(new JLabel("日"),new GBC(5,1).setAnchor(GBC.WEST));
		ordinaryPn.add(hourTf,new GBC(5,1).setAnchor(GBC.EAST));
		ordinaryPn.add(new JLabel("时"),new GBC(6,1).setAnchor(GBC.WEST));
		ordinaryPn.add(minTf,new GBC(6,1).setAnchor(GBC.EAST));
		ordinaryPn.add(new JLabel("分"),new GBC(7,1).setAnchor(GBC.WEST));
		
		ordinaryPn.add(new JLabel("尺寸:"),new GBC(0,2));
		ordinaryPn.add(size,new GBC(1,2,5,1));
		ordinaryPn.add(nowsize,new GBC(6,2));
		
		ordinaryPn.add(AlwaysOnTopCb,new GBC(0,3));
		ordinaryPn.add(SetAsDesktopCb,new GBC(2,3,2,1));
		ordinaryPn.add(startUpCb,new GBC(5,3,2,1));
		ordinaryPn.add(doubleClickCloseCb,new GBC(0,4));
		ordinaryPn.add(drawCheerCb,new GBC(2,4,2,1));
		ordinaryPn.add(authorHandsomeCb,new GBC(5,4,2,1));
		
		ordinaryPn.add(new JLabel("天数偏好设置:"), new GBC(0,5));
		ordinaryPn.add(dayUpCb,new GBC(3,5,4,1));
		
		preferencePn.add(new JLabel("背景选择："),new GBC(0,0));
		preferencePn.add(bg1btn,new GBC(1,0,2,1));
		preferencePn.add(bg2btn,new GBC(3,0,2,1));
		preferencePn.add(bg3btn,new GBC(5,0,2,1));
		
		preferencePn.add(custombtn,new GBC(0,1));
		preferencePn.add(imgName, new GBC(1,1,4,1));
		preferencePn.add(filechoose,new GBC(5,1,2,1));
		
		preferencePn.add(new JLabel("前景字颜色:"),new GBC(0,2,2,1));
		preferencePn.add(colorsample,new GBC(2,2,3,1));
		preferencePn.add(colorchoose,new GBC(5,2,2,1));
		
		preferencePn.add(new JLabel("前景不透明度:"),new GBC(0,3));
		preferencePn.add(forealpha,new GBC(2,3,3,1));
		preferencePn.add(forealphaLb,new GBC(5,3,2,1));
		
		preferencePn.add(new JLabel("综合不透明度:"),new GBC(0,4));
		preferencePn.add(bgalpha,new GBC(2,4,3,1));
		preferencePn.add(bgalphaLb,new GBC(5,4,2,1));
		
		jtp.add("常规设置", ordinaryPn);
		jtp.add("个性化",preferencePn);
		Image img = new ImageIcon("image/receive.png").getImage().getScaledInstance(200, 200, Image.SCALE_FAST);
		jtp.add("打点赏呗(づ￣ 3￣)づ", new JLabel(new ImageIcon(img)));
		
		JTextArea instruction = new JTextArea("基本操作：\r\n" + 
				"\r\n" + 
				"右键计时面板可以选择隐藏进入设置的按钮或进入设置\r\n" + 
				"面板可拖动\r\n" + 
				"右键托盘图标可退出\r\n" + 
				"单击托盘可显示\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"常见问题：\r\n" + 
				"1.打开时显示“JAVA_HOME”找不到：未安装Java虚拟机。\r\n" + 
				"\r\n" + 
				"2.打开后显示不了配图：移动了配套的image文件夹,或者把应用程序乱移动;\r\n" + 
				"\r\n" + 
				"3.设置不了桌面：移动了desktop.bat或系统权限不足;\r\n" + 
				"\r\n" + 
				"4.程序启动后莫名其妙被隐藏：使用了系统自带的“显示桌面”功能，单击托盘内相应图标即可（勾选“置顶”则无此问题）;\r\n" + 
				"\r\n" + 
				"5.开机无法自启动:\r\n" + 
				"    移动了快捷方式所指向的文件夹：启动安装目录下的源程序->取消勾选“开机启动”->确定->再打开->再勾选“开机启动”->确定，就可以了。\r\n" + 
				"    或安装路径含有中文：将安装目录移动到不含英文的路径，再重复上面步骤。\r\n" + 
				"\r\n" + 
				"遇到其他问题请联系作者。qq1070402601");
		instruction.setLineWrap(true);
		instruction.setEditable(false);
		JScrollPane sp = new JScrollPane(instruction);
		jtp.add("使用说明",sp);
		
		updatedialog = new JTextArea(
				"                           CURRENT VERSION:"+version+"\n\n\n\n\n2019 3 1 examtimer1.0问世，当时只有光秃秃的一个计时面板...还得通过代码设置时间..."
				+ "\n===========华丽分界线===========\n\n\n经过一个月的努力，2019 4 1 examtimer2.0出现！有了设置面板"
				+ "\n===========华丽分界线===========\n\n\n后来学业繁忙,2019 10 25才有时间更新，于是2.2.0问世：\r\n" + 
				"完善了一些功能：\r\n" + 
				"1.让可能无法运行“设为桌面”的电脑打开时无法勾选！\r\n" + 
				"2.把“悬浮”选项改名为“置顶”!\r\n" + 
				"3.增加了自定义考试内容功能！\r\n" + 
				"4.增加了“双击退出”选项！\r\n" + 
				"5.略改了托盘内容。\r\n" + 
				"6.在初次启动时直接进入设置面板，无需看到负的时间差！\r\n" + 
				"7.调整了窗体位置，让窗体不再偏向左上角！\r\n" + 
				"8.调整了天数数字大小，看起来更醒目！\r\n" + 
				"9.这次安装目录内自带jre8u231的安装包，为了节省体积，安装包为联网安装包，使用时需联网。\r\n" + 
				"10.增加了将设置导入导出的功能！\r\n" + 
				"\n===========华丽分界线===========\n\n\n再后来嘛,\r\n" + 
				"2019 12 20 实践出bug... 2.1.1修复了支持设为桌面壁纸的操作系统开机自动勾选设为桌面壁纸的bug"
				+ "\n===========华丽分界线=========== \n\n\n2020元旦  2.2.0小更，新增加了透明背景功能，删去背景库中bg2（嫌它丑了），美化了角色图片\r\n" + 
				"\r\n" + 
				"examtimer正式改名getimer(GaylordFocker's examtimer 啦~)\r\n" + 
				"\n===========华丽分界线===========\n\n\n2.3.0修复了开机无法自启动的bug(因为改名而产生的bug...)，并加入了选项卡，使得程序更美观！删除了“随机背景”选项！\r\n" + 
				"不再默认选择\"置顶\"！还更新了未知的bug(滑稽)！"
				+ "\n===========华丽分界线==========="
				+ "\n\n\n2.5.0挺大的一次更新所以跳过2.4.0（造物主猥琐欲为~）\r\n" + 
				"\r\n" + 
				"修改了设置的图标（更卡哇伊！）处理了tip没扣干净的左下角“我猜你一直没发现”\n"+
				"修复了之前“设为桌面壁纸”组件提示中“可以使用”无显示的bug，还有文本框老是自己变形的bug。\r\n" + 
				"\r\n" + 
				"新增加了splash screen(启动界面)还有透明度设置（这个功能累死我了，不过再也不用担心挡住背景了~）\r\n" + 
				"\r\n" + 
				"还有现在可以精确到分钟啦！\r\n" + 
				"\r\n" + 
				"多了这个没啥用的更新日志。。\n还有一个天数偏好设置，符合人们习惯！\n"
				+ "\n\n\n\n2020.1.17寒假啦"
				+ "\n又开始琢磨着更新了，于是2.5.0F(fixed)更新了~"
				+ "\n这个版本有啥子新特性？？这是个彻底的悬浮窗了，在任务栏不会显示"
				+ "\n外观大体不变，但设置面板变成弹窗了"
				+ "\n多了隐藏设置按钮的功能（某些人觉得丑）"
				+ "\n懒得讲了自己去发现吧"
				+ "\n当然了，还更新了未知的bug!"
				+ "\n\n====----=======最华丽的分界线=======----====\n\n感谢您一路陪伴支持！");
		updatedialog.setLineWrap(true);
		updatedialog.setEditable(false);
		JScrollPane sp2 = new JScrollPane(updatedialog);
		jtp.add("更新日志",sp2);
		
		settingPn.add(jtp,BorderLayout.CENTER);
		
	}
	
	public void export() {
		try {
			out = new FileOutputStream("data.xml");
			node.exportNode(out);
			out.close();
		} catch (IOException | BackingStoreException e1) {
			JOptionPane.showMessageDialog(null, "导出设置异常！", "错误", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancel() {
		dialog.setVisible(false);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		isSetting = false;
	}
	
	public void readLocation() {//独立作为方法，与readSettings对应（好看）
		int x = node.getInt("x", -142857);
		int y = node.getInt("y", -142857);
		if(x!=-142857) {
			setLocation(x,y);
		} else {
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int)screensize.getWidth();
			int height = (int)screensize.getHeight();
			setLocation(width/2-PANEL_WIDTH/2,height/2-PANEL_HEIGHT/2);
		}
	}
	
	public void readSettings() {
		
		systemSupport = node.getBoolean("systemSupport", true);
		SetAsDesktopCb.setToolTipText("设置后将把天数及背景设置为电脑桌面壁纸，取消后需手动设置为原桌面壁纸。"+(systemSupport?"您的电脑可以使用此功能":"可惜您的电脑无法使用此功能"));
		
		size.setValue(node.getInt("percent", 100));
		forealpha.setValue(node.getInt("forealpha", 100));
		bgalpha.setValue(node.getInt("bgalpha", 100));
		
		img = new File(node.get("imgPath", "文件未选择..."));
		
		yearTf.setText(node.get("year", "2020"));
		monthTf.setText(node.get("month", "01"));
		dayTf.setText(node.get("day", "01"));
		hourTf.setText(node.get("hour", "00"));
		minTf.setText(node.get("min", "00"));
		
		highbtn.setSelected(node.getBoolean("highbtnsel", true));
		midbtn.setSelected(node.getBoolean("midbtnsel", false));
		customTypeBtn.setSelected(node.getBoolean("customtypesel", false));
		examTypeTf.setText(node.get("examtype", "例如:\"一调\""));
		
		AlwaysOnTopCb.setSelected(node.getBoolean("AlwaysOnTopsel", false));
		SetAsDesktopCb.setSelected(node.getBoolean("SetAsDesktopsel", false));
		startUpCb.setSelected(node.getBoolean("startUpsel", true));
		doubleClickCloseCb.setSelected(node.getBoolean("doubleclickclosesel", false));
		drawCheerCb.setSelected(node.getBoolean("drawCheersel", true));
		dayUpCb.setSelected(node.getBoolean("dayUpsel", false));
		authorHandsomeCb.setSelected(true);
		
		bg1btn.setSelected(node.getBoolean("bg1btnsel", true));
		bg2btn.setSelected(node.getBoolean("bg2btnsel", false));
		bg3btn.setSelected(node.getBoolean("bg3btnsel", false));
		custombtn.setSelected(node.getBoolean("custombtnsel", false));
		
		nowColor = new Color(node.getInt("rgb", -16777216/*黑*/));
		colorsample.setForeground(nowColor);
		
		setCompsEnabled(systemSupport);

		check();
	}
	
	public void setCompsEnabled(boolean systemSupport) {
		SetAsDesktopCb.setEnabled(systemSupport);
		
		//更多待开发的由于系统原因不能使用的功能
	}
	
	public void check() {
		
		setTime();
		
		if(!authorHandsomeCb.isSelected()&&!bug) {
			bug = true;
			JOptionPane.showMessageDialog(null, "            想想你哪里设置错了？！", "错误", JOptionPane.CANCEL_OPTION);
		}
		
		if(bug) {
			return;
		}
		
		long diff = examDate.getTime()-new Date().getTime();
		long day = diff / (1000 * 24 * 60 * 60);
		int d = dayUpCb.isSelected()?(int)day+1:(int)day;
		nowDay = d;
		
		if(d > 1000||d<0) {
			Image x = oneToNineAndX[10];
			hundreds.setImage(x);
			tens.setImage(x);
			ones.setImage(x);
		}else {
			hundreds.setImage(oneToNineAndX[d/100]);
			tens.setImage(oneToNineAndX[d/10%10]);//简单取位~
			ones.setImage(oneToNineAndX[d%10]);
		}
		
		if(trayIcon != null) {
			trayIcon.setToolTip("version-"+version+"\n考试倒计时器随时待命。\n距离"+examType+"还有"+nowDay+"天");
		}
		
		if(customTypeBtn.isSelected()){
			examType = examTypeTf.getText();
			drawCheerCb.setSelected(false);
			drawCheerCb.setEnabled(false);
		} else {
			drawCheerCb.setEnabled(true);
			if(highbtn.isSelected()) {
				examType = "高考";
				cheerImg.setImage(cheerImgH);
			} else {
				examType = "中考";
				cheerImg.setImage(cheerImgM);
			}
		}
		changeSize(size.getValue());
		
		setAlwaysOnTop(AlwaysOnTopCb.isSelected());
		
		drawCheerBorder = drawCheerCb.isSelected();
		
		if(img.exists()) {//防止启动时原自定义图片丢失
			custonImg = new ImageIcon(img.getAbsolutePath()).getImage();
		}
		else {
			img = new File("文件未选择...");
		}
		imgName.setText(img.getName());
		
		if(bg1btn.isSelected()) {
			bgImg.setImage(bg1);
		}
		else if(bg2btn.isSelected()) {
			bgImg.setImage(bg2);
		}
		else if(bg3btn.isSelected()){
			bgImg.setImage(bg3);
		}
		else {
			bgImg.setImage(custonImg);
		}
	
		if(SetAsDesktopCb.isSelected()) {
			creatImage();
			MyToolKit.setAsDesktop(todayStr);
		}
		if(startUpCb.isSelected()) {
			MyToolKit.createShortCut(MyToolKit.getStartMenuPath(), "getimer.exe", new File("getimer"+version+".exe").getAbsolutePath());
		}else {
			new File(MyToolKit.getStartMenuPath()+"/getimer.exe.lnk").delete();
		}
	}
	

	public void setTime() {
		
		bug = false;
		
		try {
			int year = Integer.parseInt(yearTf.getText());
			int month = Integer.parseInt(monthTf.getText());
			int day = Integer.parseInt(dayTf.getText());
			int hour = Integer.parseInt(hourTf.getText());
			int min = Integer.parseInt(minTf.getText());
			if(month>12||month<1||day>31||day<1||hour>23||hour<0||min>59||min<0) {//简单判断
				bug = true;
				return;
			}
			examDate.setYear(year-1900);//Date的弊端
			examDate.setMonth(month-1);
			examDate.setDate(day);
			examDate.setHours(hour);
			examDate.setMinutes(min);
			examDate.setSeconds(0);
		}catch(NumberFormatException nfe) {
			bug = true;
		}finally {
			if(bug) {
				JOptionPane.showMessageDialog(null, "            输入时间错误！", "错误", JOptionPane.CANCEL_OPTION);
			}
		}
	}
	
	public void creatImage() {//画一张要设置为桌面的图
		
		changeSize(200);//保证画出来的图片最清晰
		
		Rectangle r = bgImg.getBounds(),numR = hundreds.getBounds();
		
		int width = r.width,height = r.height,x = r.x,y = r.y,
			numW = numR.width,numH = numR.height;
		
		BufferedImage bi = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);//双缓冲
		Graphics g = bi.getGraphics();
		//不禁感叹比例的实用性！
		g.drawImage(bgImg.getImage(), 0, 0,width,height,null);
		g.drawImage(day.getImage(), 0, 0, width, height, null);
		g.drawImage(hundreds.getImage(), hundreds.getBounds().x - x, hundreds.getBounds().y - y,numW, numH, null);
		g.drawImage(tens.getImage(), tens.getBounds().x - x, tens.getBounds().y-y,numW,numH,null);
		g.drawImage(ones.getImage(), ones.getBounds().x - x, ones.getBounds().y-y,numW,numH,null);
		g.setFont(new Font("System",Font.BOLD,nowfontSize));
		g.setColor(nowColor);
		g.drawString("距"+yearTf.getText()+examType+"还有", nowstringX - x, nowstringY - y);
		
		MyToolKit.checkFile(today);
		try {
			ImageIO.write(bi, "bmp",today);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		changeSize(size.getValue());
	}
	
	public void saveSettings() {
		
		node.putBoolean("sleep special effects", node.getBoolean("sleep special effects", false));
		
		node.putInt("percent", size.getValue());
		node.putInt("forealpha", forealpha.getValue());
		node.putInt("bgalpha", bgalpha.getValue());
		
		node.put("imgPath", img.getAbsolutePath());
		
		node.put("year", yearTf.getText());		
		node.put("month",checkStr(monthTf.getText()));
		node.put("day", checkStr(dayTf.getText()));
		node.put("hour", checkStr(hourTf.getText()));
		node.put("min", checkStr(minTf.getText()));
		
		node.putBoolean("highbtnsel", highbtn.isSelected());
		node.putBoolean("midbtnsel", midbtn.isSelected());
		node.putBoolean("customtypesel", customTypeBtn.isSelected());
		node.put("examtype", examTypeTf.getText());
		
		node.putBoolean("AlwaysOnTopsel", AlwaysOnTopCb.isSelected());
		node.putBoolean("SetAsDesktopsel", SetAsDesktopCb.isSelected());
		node.putBoolean("startUpsel", startUpCb.isSelected());
		node.putBoolean("doubleclickclosesel", doubleClickCloseCb.isSelected());
		node.putBoolean("drawCheersel", drawCheerCb.isSelected());
		node.putBoolean("dayUpsel", dayUpCb.isSelected());
		
		node.putBoolean("bg1btnsel", bg1btn.isSelected());
		node.putBoolean("bg2btnsel", bg2btn.isSelected());
		node.putBoolean("bg3btnsel", bg3btn.isSelected());
		node.putBoolean("custombtnsel", custombtn.isSelected());
		
		node.putInt("rgb", nowColor.getRGB());
		
	}

	private String checkStr(String s) {
		if(s.length()==1) {
			return "0"+s;
		}
		if(s.startsWith("0")) {//删除前导0
			return checkStr(s.substring(1));
		}
		return s;
	}
	
	public String getDatePoor() {
		
		nowDate = new Date();
	    
		long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    long ns = 1000;
	    
	    long diff = examDate.getTime() - nowDate.getTime();
	   
	    long day = diff / nd;
	    
	    if(nowDay > (int)day){//运行中日期变更时背景天数也要变化,这就是这个方法在这个类的原因
	    	check();//虽然会进行一些无意义的判断。。。
	    }
	    
	    long hour = diff % nd / nh;
	    
	    long min = diff % nd % nh / nm;
	    
	    long sec = diff % nd % nh % nm / ns;
	    
	    return day + "天" + hour + "小时" + min + "分钟"+sec+"秒";
	}
	
	public static void main(String[] args) {
//		EventQueue.invokeLater(()->{//splash用不了
		if(SystemTray.isSupported()) {
			new GETimer();
		}else {
			JOptionPane.showMessageDialog(null, "您的系统用不了托盘，此程序不配在您电脑上运行。。", "不好意思",JOptionPane.OK_OPTION);
		}
//		});
	}

}