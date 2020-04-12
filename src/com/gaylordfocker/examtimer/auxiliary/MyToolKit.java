package com.gaylordfocker.examtimer.auxiliary;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.Date;

import javax.swing.JOptionPane;

import net.jimmc.jshortcut.JShellLink;

//此类参考https://blog.csdn.net/rico_zhou/article/details/81301261，作者也联系了rico_zhou学习..(卑微)

public class MyToolKit {
	
	public static final long nd = 1000 * 24 * 60 * 60;
	public static final long nh = 1000 * 60 * 60;
	public static final long nm = 1000 * 60;
	public static final long ns = 1000;
	
	public static boolean delFiles(File f) {
		boolean success = false;
		if(f.isDirectory()) {
			for(File fi : f.listFiles()) {
				success = delFiles(fi);
			}
			success = f.delete();//删除文件夹本身
		}else {
			return f.delete();
		}
		return success;
	}
	
	public static void setAsDesktop(String imgPath) {

		File f = new File("desktop.bat");
		if(!f.exists()){
			JOptionPane.showMessageDialog(null, "desktop.bat不存在！无法设为桌面！", "错误", JOptionPane.CANCEL_OPTION);
			return;
		}
		String[] cmd = {f.getAbsolutePath(), imgPath};
        try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	public static String getDatePoor(Date date1,Date date2) {
	    
	    long diff = date1.getTime() - date2.getTime();
	   
	    long day = diff / nd;
	    
	    long hour = diff % nd / nh;
	    
	    long min = diff % nd % nh / nm;
	    
	    long sec = diff % nd % nh % nm / ns;
	    
	    return day + "天" + hour + "小时" + min + "分钟"+sec+"秒";
	    
	}
	
	public static boolean checkFile(File f) {
		if(f.exists()) {
			return true;
		}
		else {
			try {
				return f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean createShortCut(String savePath, String appName, String exePath) {
			File f = new File(exePath);
			File f2 = new File(savePath);
			
			if (!f.exists() || !f2.exists()) {
				return false;
			}
			JShellLink link = new JShellLink();
			// 存放路径
			link.setFolder(savePath);
			// 快捷方式名称
			link.setName(appName);
			// 指向的exe
			link.setPath(exePath);
			
			link.save();
			
			return true;
		
	}
	
	public static String getStartMenuPath() {
		String osName = System.getProperty("os.name");
		if (osName.equals("Windows 7") || osName.equals("Windows 8") || osName.equals("Windows 10")
				|| osName.equals("Windows Server 2012 R2") || osName.equals("Windows Server 2014 R2")
				|| osName.equals("Windows Server 2016")) {
			return System.getProperty("user.home")
					+ "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
		}
		if (osName.endsWith("Windows XP")) {
			return System.getProperty("user.home") + "\\「开始」菜单\\程序\\启动";
		}
		return "Can't Find";
	}
	
	// 写入快捷方式 是否自启动，快捷方式的名称，注意后缀是lnk
	public static boolean setAutoStart(boolean yesAutoStart, String lnk) {
		File f = new File(lnk);
		if(!f.exists()) {
//			if(new File("createShortcut.bat").exists()) {
//				try {
//					Runtime.getRuntime().exec("createShortcut.bat");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			else {
//				JOptionPane.showMessageDialog(null, "createShortcut.bat不存在！无法创建快捷方式！", "错误", JOptionPane.CANCEL_OPTION);
//				return false;
//			}
		}
		String p = f.getAbsolutePath();
		String startFolder = "";
		String osName = System.getProperty("os.name");
		if (osName.equals("Windows 7") || osName.equals("Windows 8") || osName.equals("Windows 10")
				|| osName.equals("Windows Server 2012 R2") || osName.equals("Windows Server 2014 R2")
				|| osName.equals("Windows Server 2016")) {
			startFolder = System.getProperty("user.home")
					+ "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
		}
		if (osName.endsWith("Windows XP")) {
			startFolder = System.getProperty("user.home") + "\\「开始」菜单\\程序\\启动";
		}
		if (setRunBySys(yesAutoStart, p, startFolder, lnk)) {
			return true;
		}
		return false;
	}
 
	// 设置是否随系统启动
	public static boolean setRunBySys(boolean b, String path, String path2, String lnk) {
		File file = new File(path2 + "\\" + lnk);
		Runtime run = Runtime.getRuntime();
		File f = new File(lnk);
		
		// 复制
		try {
			if (b) {
				// 写入
				// 判断是否隐藏，注意用系统copy布置为何隐藏文件不生效
				if (f.isHidden()) {
					// 取消隐藏
					try {
						Runtime.getRuntime().exec("attrib -H \"" + path + "\"");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!file.exists()) {
					run.exec("cmd /c copy " + formatPath(path) + " " + formatPath(path2));
				}
				// 延迟0.5秒防止复制需要时间
//				Thread.sleep(500);
			} else {
				// 删除
				if (file.exists()) {
					if (file.isHidden()) {
						// 取消隐藏
						try {
							Runtime.getRuntime().exec("attrib -H \"" + file.getAbsolutePath() + "\"");
						} catch (IOException e) {
							e.printStackTrace();
						}
						Thread.sleep(500);
					}
//					run.exec("cmd /c del " + formatPath(file.getAbsolutePath()));
					file.delete();
				}
			}
			return true;
		} catch (IOException |InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
 
	// 解决路径中空格问题
	private static String formatPath(String path) {
		if (path == null) {
			return "";
		}
		return path.replaceAll(" ", "\" \"");
	}

	 public static BufferedImage img_alpha(BufferedImage imgsrc,int alpha) {
	        try {
	            //创建一个包含透明度的图片,半透明效果必须要存储为png合适才行，存储为jpg，底色为黑色
	            BufferedImage back=new BufferedImage(imgsrc.getWidth(), imgsrc.getHeight(), BufferedImage.TYPE_INT_ARGB);
	            int width = imgsrc.getWidth();  
	            int height = imgsrc.getHeight();  
	            for (int j = 0; j < height; j++) { 
	                for (int i = 0; i < width; i++) { 
	                    int rgb = imgsrc.getRGB(i, j);
	                    Color color = new Color(rgb);
	                    Color newcolor = new Color(color.getRed(), color.getGreen(),color.getBlue(), alpha);
	                    back.setRGB(i,j,newcolor.getRGB());
	                }
	            }
	            return back;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	 public static BufferedImage img_alpha(Image img,int alpha) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(img, 0, 0, null);
	    return img_alpha(bi,alpha);
	 }
	 
}