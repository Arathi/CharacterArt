package org.beh.charart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;

public class ImageLoader {
	protected BufferedImage originImage;
	protected BufferedImage[][] tiles;
	protected ColorMap colorMap;
	
	protected int sizeX, sizeY;
	protected int remainderX, remainderY;
	protected int tileLength;
	
	protected String dir;
	
	public ImageLoader(String path, int tileLength){
		try {
			this.tileLength = tileLength;
			System.out.println("设置Tile边长为" + tileLength);
			
			originImage = ImageIO.read(new File(path));
			sizeX = originImage.getWidth()/tileLength;
			sizeY = originImage.getHeight()/tileLength;
			System.out.println("设置Tile数量为" + sizeX +"*"+sizeY);
			
			remainderX = originImage.getWidth()%tileLength;
			remainderY = originImage.getHeight()%tileLength;
			System.out.println("设置边角Tile大小为" + remainderX +"/"+remainderY);
			
			if (remainderX!=0){
				sizeX++;
			}
			if (remainderY!=0){
				sizeY++;
			}
			colorMap = new ColorMap(sizeX, sizeY);
			tiles = new BufferedImage[sizeX][sizeY];
			System.out.println("初始化完成！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getSizeX(){
		return sizeX;
	}
	public int getSizeY(){
		return sizeY;
	}
	public int getRemainderX(){
		return remainderX;
	}
	public int getRemainderY(){
		return remainderY;
	}
	
	public void split(){
		System.out.println("开始分离Tiles");
		int x, y;
		for (y=0; y<sizeY; y++){
			int h = tileLength;
			if (y==sizeY-1 && remainderY!=0){
				h = remainderY;
			}
			for (x=0; x<sizeX; x++){
				int startX = x*tileLength;
				int startY = y*tileLength;
				int w = tileLength;
				if (x==sizeX-1 && remainderX!=0){
					w = remainderX;
				}
				tiles[x][y] = originImage.getSubimage(startX, startY, w, h);
			}
		}
		System.out.println("分离Tiles完成！");
	}
	
	public Color monocolour(BufferedImage image){
		Color color = null;
		int x,y;
		int sx=image.getWidth(), sy=image.getHeight();
		int pixels = sx*sy;
		int sumR=0, sumG=0, sumB=0;
		for (y=0; y<sy; y++){
			for (x=0; x<sx; x++){
				int colorInt = image.getRGB(x, y);
				//int a = (colorInt>>24);
				int r = (colorInt>>16) & 0xFF;
				int g = (colorInt>>8) & 0xFF;
				int b = (colorInt & 0xFF);
				sumR += r;
				sumG += g;
				sumB += b;
			}
		}
		int avgR=sumR/pixels, avgG=sumG/pixels, avgB=sumB/pixels;
		color = new Color(avgR,avgG,avgB);
		return color;
	}
	
	public void saveSplitedImages(String base){
		int x, y;
		try{
			for (y=0; y<sizeY; y++){
				for (x=0; x<sizeX; x++){
					ImageIO.write(tiles[x][y], "png", new File(base+"_"+x+"_"+y+".png"));
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void saveMosaic(String base){
		BufferedImage image = new BufferedImage(sizeX*tileLength, sizeY*tileLength, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		int x, y;
		for (y=0; y<sizeY; y++){
			for (x=0; x<sizeX; x++){
				g.setColor( monocolour(tiles[x][y]) );
				g.fillRect(x*tileLength, y*tileLength, tileLength, tileLength);
			}
		}
		try {
			ImageIO.write(image, "png", new File(base+"output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void savePicture(String path, String format, String str, int fontSize){
//		int fontSize = 20;
		BufferedImage image = new BufferedImage(sizeX*fontSize, sizeY*fontSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		char[] strArr = str.toCharArray();
		int strLength = str.length();
		int x, y, counter=0;
		Font font = new Font("simsun", Font.PLAIN, fontSize);
		g.setFont(font);
		for (y=0; y<sizeY; y++){
			for (x=0; x<sizeX; x++){
				int dx = x*fontSize, dy = y*fontSize;
				g.setColor( monocolour(tiles[x][y]) );
				g.drawString(strArr[counter%strLength]+"", dx, dy);
				counter++;
			}
		}
		try {
			ImageIO.write(image, format, new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected String colorToString(Color color){
		String colorString = "#";
		String r = Integer.toHexString(color.getRed());
		String g = Integer.toHexString(color.getGreen());
		String b = Integer.toHexString(color.getBlue());
		if (r.length()<2) r="0"+r;
		if (g.length()<2) g="0"+g;
		if (b.length()<2) b="0"+b;
		colorString = "#" + r + g + b;
		return colorString;
	}
	
	public String toHTML(String str){
		String html="";
		char[] strArr = str.toCharArray();
		int strLength = str.length();
		int x, y, counter=0;
		for (y=0; y<sizeY; y++){
			for (x=0; x<sizeX; x++){
				Color color = monocolour(tiles[x][y]);
				String colorString = colorToString(color);
				char ch = strArr[counter%strLength];
				html+="<font color=\""+colorString+"\">"+ch+"</font>";
				counter++;
			}
			html+="<br/>";
		}
		//=====
		html = "<html>\n"
				+ "<head><title>CharacterArt</title></head>\n"
				+ "<body>\n"
				+ html+"\n"
				+ "</body>\n"
				+ "</html>";
		return html;
	}
	
	public void saveHTML(String path, String str){
		String html = toHTML(str);
		File file = new File(path);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.write(html);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDir(){
		return dir;
	}
	public void setDir(String dir){
		this.dir = dir;
	}
}
