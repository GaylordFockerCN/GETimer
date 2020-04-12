package com.gaylordfocker.examtimer.auxiliary;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class MyImage {
	
	private int x,y,width,height,default_x,default_y,default_width,default_height;//默认值供缩放时参考
	
	private Image img;
	
	private Rectangle rec;

	public MyImage(int x, int y, int width, int height, String imagePath) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.default_x = x;
		this.default_y = y;
		this.default_width = width;
		this.default_height = height;
		
		this.img = new ImageIcon(imagePath).getImage();
		rec = new Rectangle(x, y, width, height);
	}
	
	public void changeSize(int percent) {//根据比例缩放
		x = default_x*percent/100;
		y = default_y*percent/100;
		width = default_width*percent/100;
		height = default_height*percent/100;
		
		rec.x = x;
		rec.y = y;
		rec.width = width;
		rec.height = height;
	}
	
	public void changeAlpha(int alpha) {
		BufferedImage bi = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(img, 0, 0, null);
		img = MyToolKit.img_alpha(bi, alpha);
	}
	
	public Image getImage() {
		return img;
	}
	
	public void setImage(Image img) {
		this.img = img;
	}
	
	public Rectangle getBounds() {
		return rec;
	}
	
	public void draw(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
	}
	
}
