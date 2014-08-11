package com.zydecx.study.test.chesssequnce;

import java.util.Random;

public class RandomArray {
	private int[][] array;
	private int row;
	private int column;
	
	private RandomArray(int row, int column) {
		this.row = row;
		this.column = column;
		array = new int[row][column];
		
		allocateArray(array, row, column);
	}
	
	private void allocateArray(int[][] arr, int r, int c) {
		int istart, iend, idirect;
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				if (i % 2 == 0) {
					arr[i][j] = i * c + j + 1;
				} else {
					arr[i][c - j - 1] = i * c + j + 1;
				}
			}
		}
		
		Random rand = new Random(System.currentTimeMillis());
		Point fromp = new Point(), 
			top = new Point();
		int randomTime = (int) (r * c * 0.5);
		System.out.print("Random time = " + randomTime);
		for (int i = 0; i < randomTime; i++) {
			fromp.setPosition(rand.nextInt(c), rand.nextInt(r));
			top.setPosition(rand.nextInt(c), rand.nextInt(r));
			/*fromp.setPosition(25, 0);
			top.setPosition(59, 1);*/
			exchange(fromp, top);
		}
	}
	
	private void exchange(Point from, Point to) {
		int temp = get(from);
		set(from, get(to));
		set(to, temp);
	}
	
	public int get(int row, int column) {
		return get(new Point(column, row));
	}
	
	public int get(Point p) {
		checkPosition(p);
		
		return array[p.getY()][p.getX()];
	}
	
	private void set(Point p, int value) {
		checkPosition(p);
		
		array[p.getY()][p.getX()] = value;
	}
	
	private boolean checkPosition(Point p) {
		int r = p.getY();
		int c = p.getX();
		if (r >= row || r < 0 
				|| c >= column || c < 0) {
			throw new IndexOutOfBoundsException();
		}
		
		return true;
	}
	
	public static RandomArray getInstance(int row, int column) {
		return new RandomArray(row, column);
	}
	
	public void printArray() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				System.out.printf("%6d", array[i][j]);
			}
			System.out.print("\n");
		}
	}
}
