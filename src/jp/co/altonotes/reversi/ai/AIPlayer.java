package jp.co.altonotes.reversi.ai;

import jp.co.altonotes.reversi.algorithm.AlphaBeta;
import jp.co.altonotes.reversi.algorithm.SearchAlgorithm;
import jp.co.altonotes.reversi.evaluate.EvaluateStrategy;
import jp.co.altonotes.reversi.evaluate.evaluator.Evaluator;
import jp.co.altonotes.reversi.joseki.JosekiSearcher;
import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.type.Stone;

/**
 * @author Yamamoto Keita
 *
 */
public class AIPlayer {
	
	/** �����̐΂̐F	*/
	private Stone myStone;
	
	/** �T���[�x	*/
	private int maxDepth = 6;
	
	/** �ŏI�ǖʂł̒T���[�x	*/
	private int lastDepth = 13;

	/** �Ֆʕ]��	*/
	private EvaluateStrategy strategy = new EvaluateStrategy();

	/** �T���A���S���Y��	*/
	private SearchAlgorithm algorithm = new AlphaBeta();
	
	/**
	 * ���̎�̑ł��ꏊ�����߂�
	 * @param board
	 * @param record 
	 * @return
	 */
	public int nextStoneIndex(Board board, JosekiSearcher josekiSearcher) {

		int bestIndex = -1;

		// ��΂��玟�̎���擾
		if (josekiSearcher != null) {
			bestIndex = josekiSearcher.getBestIndex();
			if (bestIndex != -1) {
				return bestIndex;
			}
		}
		
		// �I�Փǂ݂��胂�[�h����
		int depth = maxDepth;
		if (64 <= board.getStoneCount() + lastDepth) {
			depth = lastDepth;
		}
		
//		StopWatch sw = new StopWatch();
//		sw.start();
		
		bestIndex = algorithm.searchBestIndex(board, depth, strategy);
		
//		sw.stop();
//		System.out.println(board.getStoneCount() + ":" + sw.lastTime());
		
		return bestIndex;
	}
	
	/**
	 * @return the algorithm
	 */
	public SearchAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(SearchAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return the myStone
	 */
	public Stone getMyStone() {
		return myStone;
	}

	/**
	 * @param myStone the myStone to set
	 */
	public void setMyStone(Stone myStone) {
		this.myStone = myStone;
	}

	/**
	 * @param power
	 * @param class1
	 */
	public void setPower(int power, Class<? extends Evaluator> class1) {
		strategy.setPower(power, class1);
	}

	/**
	 * @param maxDepth
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * @param lastDepth the lastDepth to set
	 */
	public void setLastDepth(int lastDepth) {
		this.lastDepth = lastDepth;
	}

	/**
	 * @return the strategy
	 */
	public EvaluateStrategy getStrategy() {
		return strategy;
	}


}
