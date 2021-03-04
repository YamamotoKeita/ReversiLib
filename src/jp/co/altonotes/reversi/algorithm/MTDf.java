package jp.co.altonotes.reversi.algorithm;

import java.util.HashMap;

import jp.co.altonotes.reversi.evaluate.EvaluateStrategy;
import jp.co.altonotes.reversi.evaluate.evaluator.Evaluator;
import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.util.StopWatch;

/**
 * MTD(f) �T���A���S���Y���B
 * @author Yamamoto Keita
 *
 */
public class MTDf extends SearchAlgorithm {
	
	final private static int UPPER = 0;
	final private static int LOWER = 1;

	/** �m�[�h�L���b�V��	*/
	private NodeTable table = new NodeTable();
	
	private int nodeCount;

	/**
	 * ���C�����\�b�h�i�e�X�g�p�j
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = Board.createFirstBoard();
		MTDf mtdf = new MTDf();
		StopWatch watch = new StopWatch();
		EvaluateStrategy strategy = new EvaluateStrategy();
		int value;
		

		watch.start();
		mtdf.nodeCount = 0;
		//value = mtdf.search(board, 10, strategy, myStone);
		value = mtdf.searchAlphaBeta(board, 9, strategy, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		watch.stop();
		System.out.println(10 + ": " + value);
		watch.printLastTime();
		System.out.println("�m�[�h��: " + mtdf.nodeCount);

		//System.out.println(MemoryChecker.usingHeapAsMegaByte() + "MB");
	}
	
	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.algorithm.SearchAlgorithm#search(jp.co.altonotes.reversi.model.Board)
	 */
	@Override
	protected int search(Board board) {
		int f = 300;
		return search(board, f);
	}
	
	public int searchAlphaBeta(Board board, int maxDepth, EvaluateStrategy strategy, int min, int max) {
		prepare(maxDepth, strategy);
		int value = alphaBetaWithMemory2(board, min, max, 0);
		table.clear();		
		return value;
	}

	/**
	 * �őP��̒T�����s��
	 * @param board
	 * @param f
	 * @param depth
	 * @return �őP��̕]���l
	 */
	private int search(Board board, int f) {
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		int ��;
		int g = f;
		
		while ( lowerBound < upperBound ) {
			if ( g == lowerBound ) {
				�� = g + 1;
			} else {
				�� = g;
			}
			
			g =  alphaBetaWithMemory2(board, �� - 1, ��, 0);
			
			if ( g < �� ) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		table.clear();
		return g;
	}
	 
	/**
	 * �u���\�t���A���t�@�E�x�[�^�T�����s���B
	 * Wikipedia�̋[���R�[�h���ۃR�s�B
	 * 
     * FIXME �l�����������B�ǂ����o�O���Ă�B
	 *  
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaWithMemory(Board node, int alpha, int beta, int depth) {
		// �u���\���烍�[�h
		if (table.contains( node ) ) {
			int lowerBound = table.getLowerBound( node );
			
			/* fail-high */
			if ( beta <= lowerBound ) {
				return lowerBound; // �J�b�g
			}
			int upperBound = table.getUpperBound( node );
			/* fail-low */
			if ( upperBound <= alpha ) {
				return upperBound; // �J�b�g
			}
			
			// �T���������߂�
			alpha = Math.max( alpha, lowerBound );
			beta = Math.min( beta, upperBound );
		}
		
		nodeCount++;
		
		int g;
		int a, b;
		if ( depth == maxDepth || node.isFull() ) {
			// ���݂̃m�[�h��]������B
			g = strategy.evaluate( node );
		} else {
			Board[] childNodes = node.createChildNodes();
			if (node.isNoFuture()) {
				g = strategy.evaluate( node );
			} else {
				if ( node.getMoveStone() == Evaluator.MY_STONE ) {
					// node �������̃m�[�h�B�q�m�[�h��T�����ő�l�𓾂�B
					g = Integer.MIN_VALUE;
					a = alpha;
					
					for (Board child : childNodes) {
						int val = alphaBetaWithMemory( child, a, beta, depth + 1 );
						if (depth == 0 && g < val) {
							bestNextNode = child;
						}
						g = Math.max( g, val );
						if ( beta <= g ) { // fail high
							// �~�j�}�b�N�X�l�� g �ȏ�Ȃ̂� g �������l�Ƃ��Ēu���\�ɓo�^����B
							table.storeLowerBound( node, g );								
							return g; // �J�b�g
						}
						a = Math.max( a, g );
					}
				} else {
					// node ������̃m�[�h�B�q�m�[�h��T�����ŏ��l�𓾂�B
					g = Integer.MAX_VALUE;
					b = beta;
					for (Board child : childNodes) {
						g = Math.min( g, alphaBetaWithMemory( child, alpha, b, depth + 1 ) );
						if ( g <= alpha ){ /* fail low */
							// �~�j�}�b�N�X�l�� g �ȉ��Ȃ̂� g ������l�Ƃ��Ēu���\�ɓo�^����B
							table.storeUpperBound( node, g );
							return g; // �J�b�g
						}
						b = Math.min( b, g );
					}
				}
			}
		}
		
	    // �� < g < �� �̏ꍇ�B Null Window Search �̏ꍇ�ɂ��������s����鎖�͖����B
		table.storeUpperBound( node, g );
		table.storeLowerBound( node, g );
		
		return g;
	}
	
	/**
	 * �u���\�t���A���t�@�E�x�[�^�T�����s���B
	 * Wiki�̐��������Ɏ��O�����B
	 * 
	 * �l�͂����ނː������������A�����MTD(f)������Ă����ʂ̃������x���Ȃ�B
	 * 
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaWithMemory2(Board node, int alpha, int beta, int depth) {
		
		// �u���\�ɑO��̌v�Z���ʂ�����Η��p����B
		if ( table.contains( node ) ) {
			int lowerBound = table.getLowerBound( node );
			
			/* fail-high */
			if ( beta <= lowerBound ) {
				return lowerBound; // �J�b�g
			}
			int upperBound = table.getUpperBound( node );
			/* fail-low */
			if ( upperBound <= alpha ) {
				return upperBound; // �J�b�g
			}
			
			// �T���������߂��鎖������B
			alpha = Math.max( alpha, lowerBound );
			beta = Math.min( beta, upperBound );
		}

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
				int value = alphaBetaWithMemory2(child, alpha, beta, depth + 1);
				if (depth == 0 && alpha < value) {
					bestNextNode = child;
				}
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					table.storeLowerBound( node, alpha );
					return beta; // ���J�b�g
				}
			}
			return alpha;
		}
		// �����Ԃ̏ꍇ
		else {
			for (Board child : childNodes) {
				beta = Math.min(beta, alphaBetaWithMemory2(child, alpha, beta, depth + 1));
				if (beta <= alpha) {
					table.storeUpperBound(node, beta);
					return alpha; // ���J�b�g
				}
			}
			return beta;
		}
	}
	
	/**
	 * �u���\�t���A���t�@�E�x�[�^�T�����s���B
	 * �l�b�g�ŏE�����N���̃R�[�h�B
	 * 
	 * �l�͐��������ۂ����A���x�v���v���ہB
	 * 
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaWithMemory3(Board node, int alpha, int beta, int depth) {
		int g;
		
		if (table.contains(node)) {
			int[] bounds = table.get(node);
			
			alpha = Math.max( alpha, bounds[LOWER] );
			beta = Math.min( beta, bounds[UPPER] );
		}
		
		if ( depth == maxDepth || node.isFull() ) {
			g = strategy.evaluate(node);
		} else {
			Board[] childNodes = node.createChildNodes();
			
			if (node.isNoFuture()) {
				g = strategy.evaluate(node);
			} else {
				if ( node.getMoveStone() == Evaluator.MY_STONE ) {
					g = Integer.MIN_VALUE;
					int a = alpha;
					for (Board child : childNodes) {
						int val = alphaBetaWithMemory3(child, a, beta, depth+1);
						g = Math.max(g, val);
						a = Math.max(g, a);
					}
				} else {
					g = Integer.MAX_VALUE; 
					int b = beta;
					for (Board child : childNodes) {
						int val = alphaBetaWithMemory3(child, alpha, b, depth+1);
						g = Math.min(g, val);
						b = Math.min(g, b);
					}
				}
			}
		}
		
		int[] bounds = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE};
		
		if (g <= alpha) {
			bounds[UPPER] = g;
		}
		
		if (g > alpha && g < beta) {
			bounds[LOWER] = g;
			bounds[UPPER] = g;
		}
		
		if (g >= beta) {
			bounds[LOWER] = g;
		}
		
		table.put(node, bounds);
		
		return g;
	}

	/**
	 * �u���\�Ȃ��̕��ʂ̃A���t�@�x�[�^�T��
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaNormal(Board node, int alpha, int beta, int depth){
		
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
				int value = alphaBetaNormal(child, alpha, beta, depth + 1);
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
				beta = Math.min(beta, alphaBetaNormal(child, alpha, beta, depth + 1));
				if (beta <= alpha) {
					return alpha; // ���J�b�g
				}
			}
			return beta;
		}
	}
	
	/**
	 * �m�[�h�L���b�V���p�e�[�u��
	 * @author Yamamoto Keita
	 *
	 */
	private static class NodeTable {
		
		private HashMap<Integer, int[]> table = new HashMap<Integer, int[]>();
		
		/**
		 * @param node
		 * @return
		 */
		public boolean contains(Board node) {
			return table.containsKey(node.hashCode());
		}

		/**
		 * @param node
		 * @param bounds
		 */
		public void put(Board node, int[] bounds) {
			table.put(node.hashCode(), bounds);
		}

		/**
		 * @param node
		 * @return
		 */
		public int[] get(Board node) {
			return table.get(node.hashCode());
		}

		/**
		 * 
		 */
		public void clear() {
			table.clear();
		}

		/**
		 * @param node
		 * @param g
		 */
		public void storeUpperBound(Board node, int g) {
			store(node, g, UPPER);
		}

		/**
		 * @param node
		 * @param g
		 */
		public void storeLowerBound(Board node, int g) {
			store(node, g, LOWER);
		}
		
		private void store(Board node, int g, int index) {
			int hash = node.hashCode();
			int[] values = table.get(hash);
			if (values == null) {
				values = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE};
				table.put(hash, values);
			}
			values[index] = g;
		}

		/**
		 * @param node
		 * @return
		 */
		public int getUpperBound(Board node) {
			return getBound(node, UPPER);
		}

		/**
		 * @param node
		 * @return
		 */
		public int getLowerBound(Board node) {
			return getBound(node, LOWER);
		}
		
		public int getBound(Board node, int index) {
			int[] values = table.get(node.hashCode());
			int value = values[index];

			return value;
		}
	}
}
