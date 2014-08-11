package com.zydecx.study.test.boxsorting;

public class SortBox {
	public static void main(String[] args) {
		int size;
		int totalMove;
		
		long fromTime = System.currentTimeMillis(),
				toTime;
		
		size = 100000;
		totalMove = sortBox(size);
		
		toTime = System.currentTimeMillis();
		System.out.printf("size = %d, total move = %d, costing time %d ms", size, totalMove, (toTime - fromTime));
	}
	
	/**
	 * 
	 * @param size total box number
	 * @return number of move step
	 */
	public static int sortBox(int size) {
		int transmitBox = size + 1;	// the box for transmitting
		RandomArray boxes = RandomArray.getInstance(size);	// get boxes of number of size + 1, with the last one for transmitting
		
/*		for (int i = 1; i <= size; i++) {
			System.out.printf("%d: %d | ", i, boxes.get(i));
		}*/
		
		int moveCount = 0,	// total number of move
				tmpMoveCount = 0;
		
		int tmpValue;
		for (int i = 1; i <= size; i++) {
			tmpMoveCount = 1;	// regards access operation as one move 
			if ((tmpValue = boxes.get(i)) != i) {
				/**
				 * if the ith box doesn't contain i, loop until it does.
				 * for each loop, exchange value between ith and tmpValueth box, with help from tranmit box.
				 * if value of ith box after exchange still doesn't contain i, keep looping.
				 */
				do {
//					System.out.printf("%d; ", tmpValue);
					boxes.move(i, transmitBox);
					boxes.move(tmpValue, i);
					boxes.move(transmitBox, tmpValue);
					tmpMoveCount += 1;
				} while ((tmpValue = boxes.get(i)) != i);
			}
			
//			System.out.printf("\ni = %d, move = %d", i, tmpMoveCount);
			moveCount += tmpMoveCount;
		}

//		System.out.printf("\nTotal move = %d\n", moveCount);
/*		for (int i = 1; i <= size; i++) {
			System.out.printf("%d: %d | ", i, boxes.get(i));
		}*/
		
		return moveCount;
	}
}
