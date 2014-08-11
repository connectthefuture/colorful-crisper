package com.zydecx.study.test.boxsorting;

import java.util.Random;

public class RandomArray {
	private int[] container;
	private int size;
	
	private RandomArray(int n) {
		size = n;
		container = new int[size + 1];
		
		allocateBox(container, size);
		container[size] = -1;
	}
	
	private boolean allocateBox(int[] c, int size) {
		for (int i = 1; i <= size; i++) {
			c[i - 1] = i;
		}
		
		int randomTime = size * 2;
		int p, q;
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < randomTime; i++) {
			p = r.nextInt(size);
			q = r.nextInt(size);
			exchange(p, q);
		}
		return true;
	}
	
	public static RandomArray getInstance(int size) {
		return new RandomArray(size);
	}
	
	public int get(int p) throws IndexOutOfBoundsException {
		checkIndex(p);
		return container[p - 1];
	}
	
	public boolean move(int p, int q) throws IndexOutOfBoundsException {
		checkIndex(p);
		checkIndex(q);
		
		return moveTo(p - 1, q - 1);
	}
	
	private boolean moveTo(int p, int q) {
		if (container[q] != -1) return false;
		
		container[q] = container[p];
		container[p] = -1;
		return true;
	}
	
	private void exchange(int p, int q) {
		int tmp = container[p];
		container[p] = container[q];
		container[q] = tmp;
	}
	
	private boolean checkIndex(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > size + 1)
			throw new IndexOutOfBoundsException("Accessing box " + i + " doesnot exist.");
		return true;
	}
}
