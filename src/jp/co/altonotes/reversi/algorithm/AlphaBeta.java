package jp.co.altonotes.reversi.algorithm;

import jp.co.altonotes.reversi.evaluate.EvaluateStrategy;
import jp.co.altonotes.reversi.evaluate.evaluator.Evaluator;
import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.util.StopWatch;

/**
 * �����T���A���S���Y��
 * @author Yamamoto Keita
 *
 */
public class AlphaBeta extends SearchAlgorithm {
	private int nodeCount;
	
	/**
	 * ���C�����\�b�h�i�e�X�g�p�j
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = Board.createFirstBoard();
		AlphaBeta alphaBeta = new AlphaBeta();
		StopWatch sw = new StopWatch();
		
		for (int i = 10; i <= 10; i++) {
			alphaBeta.nodeCount = 0;
			sw.start();
			int value = alphaBeta.search(board, i, new EvaluateStrategy());
			//int value = ab.search(board, 7, new EvaluateStrategy(), Stone.BLACK, 9, 10);
			sw.stop();

			System.out.println(i + ":" +value);
			//sw.printLastTime();
			System.out.println("�m�[�h���F" + alphaBeta.nodeCount);
		}
	}
	

	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.algorithm.SearchAlgorithm#search(jp.co.altonotes.reversi.model.Board)
	 */
	@Override
	protected int search(Board board) {
		return search(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	}
	
	protected int search(Board board, int maxDepth, EvaluateStrategy strategy, int min, int max) {
		prepare(maxDepth, strategy);
		return search(board, min, max, 0);
	}
	
	/**
	 * �A���t�@�x�[�^�T�����s��
	 * 
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return �Ղ̕]���l
	 */
	private int search(Board node, int alpha, int beta, int depth){
		nodeCount++;
		
		// �ǂݍŐ[�� or �Ղ��S�Ė��܂����ꍇ
		if (depth == maxDepth || node.isFull()) {
			int val = strategy.evaluate(node);
			return val;
		}

		//���Ֆʐ���
		Board[] childNodes = node.createChildNodes();
		if (node.isNoFuture()) {
			return strategy.evaluate(node);
		}
		
		// ������Ԃ̂Ƃ�
		if (node.getMoveStone() == Evaluator.MY_STONE) {
			for (Board child : childNodes) {
				int value = search(child, alpha, beta, depth + 1);
				if (depth == 0 && alpha < value) {
					bestNextNode = child;
				}
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					return beta; // ���J�b�g
				}
			}
			return alpha;
		}
		// �����Ԃ̏ꍇ
		else {
			for (Board child : childNodes) {
				int value = search(child, alpha, beta, depth + 1);
				if (depth == 0 && value < beta) {
					bestNextNode = child;
				}
				beta = Math.min(beta, value);
				if (beta <= alpha) {
					return alpha; // ���J�b�g
				}
			}
			return beta;
		}
	}

}
