package org.beh.charart;

import java.awt.Color;

public class ColorMap {
	protected int sizeX;
	protected int sizeY;
	protected Color[][] colors;
	
	@Deprecated
	public ColorMap(){
		init(100, 100);
	}
	
	public ColorMap(int x, int y){
		init(x, y);
	}
	
	private void init(int x, int y){
		if (x<=0 || y<=0) return;
		sizeX = x;
		sizeY = y;
		colors = new Color[sizeX][sizeY];
	}
	
	public boolean setColor(int x, int y, Color c){
		if (x>=0 && x<sizeX && y>=0 && y<sizeY){
			colors[x][y] = c;
			return true;
		}
		return false;
	}
}
