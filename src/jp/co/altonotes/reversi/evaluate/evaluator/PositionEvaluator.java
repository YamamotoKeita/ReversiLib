package jp.co.altonotes.reversi.evaluate.evaluator;

import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.type.Stone;

/**
 * 石の位置による盤面評価を行う
 * @author Yamamoto Keita
 *
 */
public class PositionEvaluator extends Evaluator {

	/** 各位置の評価値	*/
	private static int[][] VALUE_TABLE = {
			{ 45, -11,  4, -1, -1,  4, -11,  45},
			{-11, -16, -1, -3, -3, -1, -16, -11},
			{  4,  -1,  2, -1, -1,  2,  -1,   4},
			{ -1,  -3, -1,  0,  0, -1,  -3,  -1},
			{ -1,  -3, -1,  0,  0, -1,  -3,  -1},
			{  4,  -1,  2, -1, -1,  2,  -1,   4},
			{-11, -16, -1, -3, -3, -1, -16, -11},
			{ 45, -11,  4, -1, -1,  4, -11,  45},
		};

	int limit = 58;
	
	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.evaluate.evaluator.Evaluator#evaluate(jp.co.altonotes.reversi.model.Board, jp.co.altonotes.reversi.type.Stone, int)
	 */
	@Override
	public int evaluate(Board board) {
		int value = 0;
		
		// 終盤は無視
		if (limit <= board.getStoneCount() || board.isNoFuture()) {
			return value;
		}
		
		value += positionValue(board);
		value += adjustVlaue(board);
		return value;
	}
	
	/**
	 * 固定値の盤面評価を行う
	 * @param board
	 * @param mainStone
	 * @return
	 */
	private int positionValue(Board board) {
		int value = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				value += value(board, x, y);
			}
		}
		return value;
	}
	
	/**
	 * 角に石がある場合は角の隣のマイナスをなくす
	 * @param board
	 * @param mainStone
	 * @return
	 */
	private int adjustVlaue(Board board) {
		int value = 0;
		
		Stone stone = board.getStone(0, 0);
		if (stone != Stone.NONE) {
			value -= value(board, 0, 1);
			value -= value(board, 1, 0);
			value -= value(board, 1, 1);
		}
		
		stone = board.getStone(0, 7);
		if (stone != Stone.NONE) {
			value -= value(board, 0, 6);
			value -= value(board, 1, 7);
			value -= value(board, 1, 6);
		}

		stone = board.getStone(7, 0);
		if (stone != Stone.NONE) {
			value -= value(board, 6, 0);
			value -= value(board, 7, 1);
			value -= value(board, 6, 1);
		}

		stone = board.getStone(7, 7);
		if (stone != Stone.NONE) {
			value -= value(board, 7, 6);
			value -= value(board, 6, 7);
			value -= value(board, 6, 6);
		}

		return value;
	}

	/**
	 * 指定座標の評価値を取得する
	 * @param board
	 * @param x
	 * @param y
	 * @return
	 */
	private int value(Board board, int x, int y) {
		Stone stone = board.getStone(x, y);
		if (stone == Stone.NONE) {
			return 0;
		}
		if (stone == MY_STONE) {
			return VALUE_TABLE[y][x];
		} else {
			return -VALUE_TABLE[y][x];
		}
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
