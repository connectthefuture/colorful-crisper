package com.zydecx.study.test.chesssequnce;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int[] p) {
		if (p.length < 2) {
			throw new IndexOutOfBoundsException();
		}
		
		this.x = p[0];
		this.y = p[1];
	}
	
	public Point() {
		this.x = -1;
		this.y = -1;
	}
	
	public Point setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public int[] getPosition() {
		int[] p = {this.x, this.y};
		return p;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Point)) {
			return false;
		}
		
		Point p = (Point) o;
		
		return p.x == x && p.y == y;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + x;
		result = 31 * result + y;
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("[%d,  %d]", x, y);
	}
}
