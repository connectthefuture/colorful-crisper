package com.zydecx.study.test.chesssequnce;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * this demo works for array in which neither of two number are the same, i.g.
 * 1 2 5
 * 6 3 4
 * @author chuff
 *
 */
public class CopyOfMaxChessSequenceOfNonRepeatArray {
	public static void main(String[] args) {
		new CopyOfMaxChessSequenceOfNonRepeatArray().runDemo();
	}
	
	public void runDemo() {
		int column;
		int row;
		Point[] path;
		RandomArray ra;

		row = 40;
		column = 80;
		ra = RandomArray.getInstance(row, column);
		
		long fromTime = System.currentTimeMillis(),
				toTime;
		
		path = getMaxSequence(ra, row, column);
		
		toTime = System.currentTimeMillis();
		System.out.println("number array: ");
//		ra.printArray();
		System.out.println("max path: ");
//		printPath(ra, path, path.length);
		System.out.println("\nrunning infomation: ");
		System.out.printf("row = %d, column = %d, max sequence = %d, "
				+ "costing time %d ms\n", row, column, path.length, toTime - fromTime); 
	}
	
	private void printPath(RandomArray ra, Point[] path, int length) {
		Point p;
		for (int i = 0; i < length; i++) {
			p = path[i];
			if (i > 0) System.out.print(" ==>> ");
			System.out.printf("r%dc%d : %d", p.getY() + 1, p.getX() + 1, ra.get(p));
		}
	}
	
	
	public Point[] getMaxSequence(RandomArray ra, int row, int column) {
		int moveCount = 0;	// total number of move
		Map<Point, PointBox> chessInfoMap = new HashMap<>();	// map of each point and it's PointBox
		
		/**
		 * init PointBox of all points
		 */
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
//				moveCount++;
				
				Point p = new Point(j, i),
						nextP;
				PointBox pb = chessInfoMap.get(p), 
						nextPb;
				if (pb == null) {
					pb = new PointBox(p, ra.get(p));
					chessInfoMap.put(p, pb);
				}

				nextP = Direction.UP.nextPoint(p, 0); 		pb.setNext(Direction.UP, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				nextP = Direction.DOWN.nextPoint(p, row - 1); 	pb.setNext(Direction.DOWN, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				nextP = Direction.LEFT.nextPoint(p, 0); 		pb.setNext(Direction.LEFT, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				nextP = Direction.RIGHT.nextPoint(p, column - 1);	pb.setNext(Direction.RIGHT, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				
				if (pb.hasAdjacent()) {
					nextP = pb.getNextPoint();
					nextPb = chessInfoMap.get(nextP);
					if (nextPb == null) {
						nextPb = new PointBox(nextP, ra.get(nextP));
						chessInfoMap.put(nextP, nextPb);
					}
					pb.setNextPointBox(nextPb);
				}
			}
		}
		
		
		int max = -1;	// size of max number sequence
		Point maxPathStart = null;	// start point of max number sequence
		PointBox stackPb;	// temporary variable for searching number sequence starting from each point
		Stack<PointBox> pathStack = new Stack<>();	// temporary variable for searching number sequence starting from each point
		
		/**
		 * loop all points to get the max number sequence
		 */
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
//				moveCount++;
				
				Point p = new Point(j, i);
				PointBox pb = chessInfoMap.get(p),
						tempPb;
				
				int pointMax = pb.getMaxDepth(),
						lastPointMax;
				/**
				 * maxDepth of PointBox is -1 by default; 
				 * otherwise, it's max number sequence has been calculated 
				 * 			and the value could be compared with max directly
				 */
				if (pointMax <= 0) {
					/**
					 * recursion of all the available number sequences starting from current point
					 */
					pathStack.clear();
					tempPb = pb;
					do {
						moveCount++;
						pathStack.push(tempPb);
						
						if (tempPb.getMaxDepth() > 0) {
							break;
						}
						tempPb = tempPb.getNextPointBox();
					} while (tempPb != null);
					
					moveCount++;
					tempPb = pathStack.pop();
					if ((lastPointMax = tempPb.getMaxDepth()) <= 0) {
						lastPointMax = 1;
						tempPb.setMaxDepth(1);
					}
					while (!pathStack.empty()) {
						moveCount++;
						pathStack.pop().setMaxDepth(++lastPointMax);
					}
					
					if (lastPointMax > max) {
						max = pointMax;
						maxPathStart = p;
					}
				} else if (pointMax > max) {
					max = pointMax;
					maxPathStart = p;
				}
			}
		}
		
//		chessInfoMap.get(maxPathStart).printMaxPath();
		System.out.printf("\nTotal move = %d\n", moveCount);
		return chessInfoMap.get(maxPathStart).getMaxPath();
		
	}
	
	/**
	 * Direction - for each point in the chessboard, it has four adjacent point, 
	 * 				with directions of UP, DOWN, LEFT and RIGHT
	 * @author chuff
	 *
	 */
	private enum Direction {
		UP {
			@Override
			public Point nextPoint(Point p, int threshold) { return p.getY() <= threshold ? null : new Point(p.getX(), p.getY() - 1); }
		},
		DOWN {
			@Override
			public Point nextPoint(Point p, int threshold) { return p.getY() >= threshold ? null : new Point(p.getX(), p.getY() + 1); }
		},
		LEFT {
			@Override
			public Point nextPoint(Point p, int threshold) { return p.getX() <= threshold ? null : new Point(p.getX() - 1, p.getY()); }
		},
		RIGHT {
			@Override
			public Point nextPoint(Point p, int threshold) { return p.getX() >= threshold ? null : new Point(p.getX() + 1, p.getY()); }
		};
		
		/**
		 * get the Point object of the adjacent point of p in current direction; 
		 * return null if p locates at the border
		 * @param p
		 * @param threshold - the threshold axis of current direction
		 * @return 
		 */
		public abstract Point nextPoint(Point p, int threshold);
	}
	
	/**
	 * PointBox - a tool box for each point on the chessboard to help get the maximum number sequence
	 * @author chuff
	 *
	 */
	private class PointBox {
		private Point p;
		private int value;
		private int maxDepth;	// length of longest number sequence path from current point
		private Direction nextDirection;// direction of the adjacent point whose value is 1 bigger than current point
		private Point nextPoint;		// the adjacent point whose value is 1 bigger than current point
		private PointBox nextPointBox;	// redundancy for more information of the adjacent point
		
		public PointBox(Point p, int value) {
			this.p = p;
			this.value = value;
			this.maxDepth = -1;
			this.nextDirection = null;
			this.nextPoint = null;
		}
		
		public void setNext(Direction d, Point p) {
			if (p != null) {
				this.nextDirection = d;
				this.nextPoint = p;
			}
		}
		
		public Point getNextPoint() {
			return this.nextPoint;
		}
		
		public Direction getNextDirection() {
			return this.nextDirection;
		}
		
		public boolean hasAdjacent() {
			return !(this.nextDirection == null);
		}
		
		public Point getPoint() {
			return this.p;
		}

		public int getValue() {
			return value;
		}
		
		public int getMaxDepth() {
			return maxDepth;
		}

		public void setMaxDepth(int maxDepth) {
			this.maxDepth = maxDepth;
		}

		public PointBox getNextPointBox() {
			return nextPointBox;
		}

		public void setNextPointBox(PointBox nextPointBox) {
			this.nextPointBox = nextPointBox;
		}
		
		public Point[] getMaxPath() {
			Point[] path = new Point[maxDepth];
			int i = 0;
			PointBox pb = this;
			do {
				path[i++] = pb.getPoint();
				pb = pb.getNextPointBox();
			} while (pb != null);

			return path;
		}
		
		public void printMaxPath() {
			PointBox pb = this;
			do {
				if (pb != this) System.out.print(" ==>> ");
				System.out.printf("r%dc%d : %d", pb.getPoint().getY() + 1, pb.getPoint().getX() + 1, pb.getValue());
				pb = pb.getNextPointBox();
			} while (pb != null);
			System.out.print("\n");
		}
	}
}
