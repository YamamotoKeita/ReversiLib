package jp.co.altonotes.reversi.evaluate.evaluator;

import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.type.Stone;

/**
 * 特定の指針に基づいて盤面を評価する
 * @author Yamamoto Keita
 *
 */
public abstract class Evaluator {

	/** 評価の基準となる石。この石が有利な状況が評価点プラス。	*/
	final public static Stone MY_STONE = Stone.BLACK;

	/** 敵の石の色	*/
	final public static Stone ENEMY_STONE = Stone.WHITE;
	
	protected int power = 10;

	
	/**
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * 
	 * @param board
	 * @return
	 */
	public int getValue(Board board) {
		return power * evaluate(board);
	}
	
	/**
	 * 
	 * @param board
	 * @return
	 */
	abstract public int evaluate(Board board);
}
