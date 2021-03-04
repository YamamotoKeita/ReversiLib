package jp.co.altonotes.reversi.algorithm;

import jp.co.altonotes.reversi.evaluate.EvaluateStrategy;
import jp.co.altonotes.reversi.model.Board;

/**
 * ’TõƒAƒ‹ƒSƒŠƒYƒ€
 * @author Yamamoto Keita
 */
public abstract class SearchAlgorithm {

	/** ”Õ–Ê•]‰¿‚·‚é‚à‚Ì	*/
	protected EvaluateStrategy strategy;

	/** Å‘Pè	*/
	protected Board bestNextNode;
	
	/** Å[’Tõƒm[ƒh	*/
	protected int maxDepth;

	/**
	 * Å‘Pè‚ğæ“¾‚·‚é
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
	 * ’Tõ‚ğs‚¤
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
	 * @return Å‘Pè
	 */
	public Board getBestNode() {
		return bestNextNode;
	}
}
