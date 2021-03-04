package jp.co.altonotes.reversi.evaluate.evaluator;

import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.type.Stone;

/**
 * ����̎w�j�Ɋ�Â��ĔՖʂ�]������
 * @author Yamamoto Keita
 *
 */
public abstract class Evaluator {

	/** �]���̊�ƂȂ�΁B���̐΂��L���ȏ󋵂��]���_�v���X�B	*/
	final public static Stone MY_STONE = Stone.BLACK;

	/** �G�̐΂̐F	*/
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
