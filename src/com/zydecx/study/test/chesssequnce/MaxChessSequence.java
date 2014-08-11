package com.zydecx.study.test.chesssequnce;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MaxChessSequence {
	public static void main(String[] args) {
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
		System.out.printf("row = %d, column = %d, max sequence = %d, costing time %d ms\n", row, column, path.length, toTime - fromTime); 
	}
	
	private static void printPath(RandomArray ra, Point[] path, int length) {
		Point p;
		for (int i = 0; i < length; i++) {
			p = path[i];
			if (i > 0) System.out.print(" ==>> ");
			System.out.printf("r%dc%d : %d", p.getY() + 1, p.getX() + 1, ra.get(p));
		}
	}
	
	
	public static Point[] getMaxSequence(RandomArray ra, int row, int column) {
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
				PointBox pb = new PointBox(p, ra.get(p));
				chessInfoMap.put(p, pb);

				nextP = Direction.UP.nextPoint(p, 0); 		pb.setAdjacentSeq(Direction.UP, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				nextP = Direction.DOWN.nextPoint(p, row - 1); 	pb.setAdjacentSeq(Direction.DOWN, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				nextP = Direction.LEFT.nextPoint(p, 0); 		pb.setAdjacentSeq(Direction.LEFT, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
				nextP = Direction.RIGHT.nextPoint(p, column - 1);	pb.setAdjacentSeq(Direction.RIGHT, nextP != null && ra.get(nextP) - ra.get(p) == 1 ? nextP : null);
			}
		}
		
		
		int max = -1;	// max length of path
		Point maxPathStart = null;	// start point of longest path
		PointBox stackPb;	// temporary variable for searching number sequence path starting from each point
		Stack<PointNonius> pathStack = new Stack<>();	// temporaty variable for searching number sequence path starting from each point
		
		/**
		 * loop all points to get the longest sequence path
		 */
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
//				moveCount++;
				
				Point p = new Point(j, i);
				PointBox pb = chessInfoMap.get(p);
				int pointMax = pb.getMaxDepth();
				/**
				 * maxDepth of PointBox is -1 by default; otherwise, it's longest path has been calculated and could be compared with max directly
				 */
				if (pointMax <= 0) {
					/**
					 * recursion of all the available number sequences starting from current point
					 */
					pathStack.clear();
					pathStack.push(new PointNonius(pb, null, null, null, 1));	// default depth is 1, which means itself
					while (!pathStack.empty()) {
						moveCount++;
						
						PointNonius pn = pathStack.pop();
						stackPb = pn.pointBox;
						int tempMax = -1,
								tempPointMax = -1;
						/**
						 * if a point doesn't have adjacent points(whose number's 1 bigger) or has been processes already, current path ends;
						 * else, put all adjacent points into the stack, and add depth by 1
						 */
						if (stackPb.getMaxDepth() > 0 || !stackPb.hasAdjacent()) {
							/**
							 * compare depth of current path with max so far, update it if here comes a bigger one
							 */
							if (stackPb.getMaxDepth() > 0) {
								tempPointMax = stackPb.getMaxDepth() + pn.depth - 1;
							} else {
								tempPointMax = pn.depth;
							}
							
							if (tempPointMax >  max) {
								max = tempPointMax;
								maxPathStart = p;
							}
							
							/**
							 * for point who doesn't have adjacent points, it's max number sequence is 1, and of course, has no direction for adjacent point
							 */
							pn.pointBox.setMax(null, 1, null, null);
							
							/**
							 * loop current path from end to start, update max number sequence of points in it;
							 * so the next time when they're accessed, we don't have to calculate again
							 */
							PointNonius tempNextPn = pn;
							PointNonius tempPn = tempNextPn.prev;
							while (tempPn != null) {
//								moveCount++;
								
								tempMax = tempPointMax - tempPn.depth + 1;
								if (tempMax > tempPn.pointBox.getMaxDepth()) {
									tempPn.pointBox.setMax(tempNextPn.direction, tempMax, null, tempNextPn.pointBox);
								}
								tempNextPn = tempPn;
								tempPn = tempNextPn.prev;
							}
						} else {
							for (Direction d : stackPb.getAllAdjacentSeq()) {
								PointBox tempNextPb = chessInfoMap.get(stackPb.getAdjacentSeq(d));
								pathStack.push(new PointNonius(tempNextPb, pn, null, d, pn.depth + 1));	// depth added by 1
							}
						}
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
	 * Direction - for each point in the chessboard, it has four adjacent point, with directions of UP, DOWN, LEFT and RIGHT
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
		 * get the Point object of the adjacent point of p in current direction; return null if p locates at the border
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
	private static class PointBox {
		private Point p;
		private int value;
		private int maxDepth;	// length of longest number sequence path from current point
		private Direction maxDirection;	// direction to get the longest path
		private PointBox maxNext;		// PointBox of maxDirection
		private Map<Direction, Point> adjacentSeq;	// adjacent points which are 1 bigger than current point
		private PointBox maxPrev;
		
		public PointBox(Point p, int value) {
			this.p = p;
			this.value = value;
			this.maxDepth = -1;
			this.adjacentSeq = new HashMap<>();
			this.maxDirection = null;
			this.maxPrev = null;
			this.maxNext = null;
		}
		
		public void setAdjacentSeq(Direction d, Point p) {
			if (p != null) {
				this.adjacentSeq.put(d, p);
			}
		}
		
		public Point getAdjacentSeq(Direction d) {
			return this.adjacentSeq.get(d);
		}
		
		public Set<Direction> getAllAdjacentSeq() {
			return (Set<Direction>) this.adjacentSeq.keySet();
		}
		
		public boolean hasAdjacent() {
			return !this.adjacentSeq.isEmpty();
		}
		
		public Point getPoint() {
			return this.p;
		}
		
		public void setMax(Direction direction, int depth, PointBox prev, PointBox next) {
			this.maxDirection = direction;
			this.maxDepth = depth;
			this.maxPrev = prev;
			this.maxNext = next;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		
		public int getMaxDepth() {
			return maxDepth;
		}

		public void setMaxDepth(int maxDepth) {
			this.maxDepth = maxDepth;
		}

		public Direction getMaxDirection() {
			return maxDirection;
		}

		public void setMaxDirection(Direction maxDirection) {
			this.maxDirection = maxDirection;
		}

		public PointBox getMaxPrev() {
			return maxPrev;
		}

		public void setMaxPrev(PointBox maxPrev) {
			this.maxPrev = maxPrev;
		}

		public PointBox getMaxNext() {
			return maxNext;
		}

		public void setMaxNext(PointBox maxNext) {
			this.maxNext = maxNext;
		}
		
		public Point[] getMaxPath() {
			Point[] path = new Point[maxDepth];
			int i = 0;
			PointBox pb = this;
			do {
				path[i++] = pb.getPoint();
				pb = pb.getMaxNext();
			} while (pb != null);

			return path;
		}
		
		public void printMaxPath() {
			PointBox pb = this;
			do {
				if (pb != this) System.out.print(" ==>> ");
				System.out.printf("r%dc%d : %d", pb.getPoint().getY() + 1, pb.getPoint().getX() + 1, pb.getValue());
				pb = pb.getMaxNext();
			} while (pb != null);
			System.out.print("\n");
		}
	}
	
	/**
	 * PointNonius - a nonius tool to help search the number sequence path of each point
	 * @author chuff
	 *
	 */
	private static class PointNonius {
		public PointBox pointBox;
		public PointNonius prev;
		public PointNonius next;
		public Direction direction;	// direction to get to current nonius from prev
		public int depth;	// depth of number sequence top from the start point
		
		public PointNonius(PointBox point, PointNonius prev, PointNonius next, Direction direction, int depth) {
			this.pointBox = point;
			this.prev = prev;
//			this.next = next;
			this.direction = direction;
			this.depth = depth;
		}
	}
}
