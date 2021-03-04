package jp.co.altonotes.reversi.algorithm;

import jp.co.altonotes.reversi.evaluate.EvaluateStrategy;
import jp.co.altonotes.reversi.model.Board;

/**
 * �T���A���S���Y��
 * @author Yamamoto Keita
 */
public abstract class SearchAlgorithm {

	/** �Ֆʕ]���������	*/
	protected EvaluateStrategy strategy;

	/** �őP��	*/
	protected Board bestNextNode;
	
	/** �Ő[�T���m�[�h	*/
	protected int maxDepth;

	/**
	 * �őP����擾����
	 * @param board
	 * @param maxDepth
	 * @param strategy
	 * @param myStone
	 * @return
	 */
	public int searchBestIndex(Board board, int maxDepth, EvaluateStrategy strategy) {
		prepare(maxDepth, strategy);
		bestNextNode = null;
		search(board);
		return bestNextNode.getLastIndex(board);
	}
	
	/**
	 * �T�����s��
	 * @param board
	 * @param maxDepth
	 * @param strategy
	 * @param myStone
	 * @return
	 */
	public int search(Board board, int maxDepth, EvaluateStrategy strategy) {
		prepare(maxDepth, strategy);
		bestNextNode = null;
		return search(board);
	}
	
	/**
	 * @param maxDepth
	 * @param strategy
	 * @param myStone
	 */
	protected void prepare(int maxDepth, EvaluateStrategy strategy) {
		this.maxDepth = maxDepth;
		this.strategy = strategy;
	}

	/**
	 * @param board
	 * @return
	 */
	abstract protected int search(Board board);

	/**
	 * @return �őP��
	 */
	public Board getBestNode() {
		return bestNextNode;
	}
}
