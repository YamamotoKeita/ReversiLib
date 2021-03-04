package jp.co.altonotes.reversi.algorithm;

import java.util.HashMap;

import jp.co.altonotes.reversi.evaluate.EvaluateStrategy;
import jp.co.altonotes.reversi.evaluate.evaluator.Evaluator;
import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.util.StopWatch;

/**
 * MTD(f) 探索アルゴリズム。
 * @author Yamamoto Keita
 *
 */
public class MTDf extends SearchAlgorithm {
	
	final private static int UPPER = 0;
	final private static int LOWER = 1;

	/** ノードキャッシュ	*/
	private NodeTable table = new NodeTable();
	
	private int nodeCount;

	/**
	 * メインメソッド（テスト用）
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
		System.out.println("ノード数: " + mtdf.nodeCount);

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
	 * 最善手の探索を行う
	 * @param board
	 * @param f
	 * @param depth
	 * @return 最善手の評価値
	 */
	private int search(Board board, int f) {
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		int β;
		int g = f;
		
		while ( lowerBound < upperBound ) {
			if ( g == lowerBound ) {
				β = g + 1;
			} else {
				β = g;
			}
			
			g =  alphaBetaWithMemory2(board, β - 1, β, 0);
			
			if ( g < β ) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		table.clear();
		return g;
	}
	 
	/**
	 * 置換表付きアルファ・ベータ探索を行う。
	 * Wikipediaの擬似コードを丸コピ。
	 * 
     * FIXME 値がおかしい。どこかバグってる。
	 *  
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaWithMemory(Board node, int alpha, int beta, int depth) {
		// 置換表からロード
		if (table.contains( node ) ) {
			int lowerBound = table.getLowerBound( node );
			
			/* fail-high */
			if ( beta <= lowerBound ) {
				return lowerBound; // カット
			}
			int upperBound = table.getUpperBound( node );
			/* fail-low */
			if ( upperBound <= alpha ) {
				return upperBound; // カット
			}
			
			// 探索窓を狭める
			alpha = Math.max( alpha, lowerBound );
			beta = Math.min( beta, upperBound );
		}
		
		nodeCount++;
		
		int g;
		int a, b;
		if ( depth == maxDepth || node.isFull() ) {
			// 現在のノードを評価する。
			g = strategy.evaluate( node );
		} else {
			Board[] childNodes = node.createChildNodes();
			if (node.isNoFuture()) {
				g = strategy.evaluate( node );
			} else {
				if ( node.getMoveStone() == Evaluator.MY_STONE ) {
					// node が自分のノード。子ノードを探索し最大値を得る。
					g = Integer.MIN_VALUE;
					a = alpha;
					
					for (Board child : childNodes) {
						int val = alphaBetaWithMemory( child, a, beta, depth + 1 );
						if (depth == 0 && g < val) {
							bestNextNode = child;
						}
						g = Math.max( g, val );
						if ( beta <= g ) { // fail high
							// ミニマックス値は g 以上なので g を下限値として置換表に登録する。
							table.storeLowerBound( node, g );								
							return g; // カット
						}
						a = Math.max( a, g );
					}
				} else {
					// node が相手のノード。子ノードを探索し最小値を得る。
					g = Integer.MAX_VALUE;
					b = beta;
					for (Board child : childNodes) {
						g = Math.min( g, alphaBetaWithMemory( child, alpha, b, depth + 1 ) );
						if ( g <= alpha ){ /* fail low */
							// ミニマックス値は g 以下なので g を上限値として置換表に登録する。
							table.storeUpperBound( node, g );
							return g; // カット
						}
						b = Math.min( b, g );
					}
				}
			}
		}
		
	    // α < g < β の場合。 Null Window Search の場合にここが実行される事は無い。
		table.storeUpperBound( node, g );
		table.storeLowerBound( node, g );
		
		return g;
	}
	
	/**
	 * 置換表付きアルファ・ベータ探索を行う。
	 * Wikiの説明を元に自前実装。
	 * 
	 * 値はおおむね正しそうだが、これでMTD(f)をやっても普通のαβより遅くなる。
	 * 
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaWithMemory2(Board node, int alpha, int beta, int depth) {
		
		// 置換表に前回の計算結果があれば利用する。
		if ( table.contains( node ) ) {
			int lowerBound = table.getLowerBound( node );
			
			/* fail-high */
			if ( beta <= lowerBound ) {
				return lowerBound; // カット
			}
			int upperBound = table.getUpperBound( node );
			/* fail-low */
			if ( upperBound <= alpha ) {
				return upperBound; // カット
			}
			
			// 探索窓を狭められる事がある。
			alpha = Math.max( alpha, lowerBound );
			beta = Math.min( beta, upperBound );
		}

		nodeCount++;

		// 読み最深部 or 盤が全て埋まった場合
		if (depth == maxDepth || node.isFull()) {
			int val = strategy.evaluate(node);
			return val;
		}

		//次盤面生成
		Board[] childNodes = node.createChildNodes();
		if (node.isNoFuture()) {
			return strategy.evaluate(node);
		}
		
		// 自分手番のとき
		if (node.getMoveStone() == Evaluator.MY_STONE) {
			for (Board child : childNodes) {
				int value = alphaBetaWithMemory2(child, alpha, beta, depth + 1);
				if (depth == 0 && alpha < value) {
					bestNextNode = child;
				}
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					table.storeLowerBound( node, alpha );
					return beta; // βカット
				}
			}
			return alpha;
		}
		// 相手手番の場合
		else {
			for (Board child : childNodes) {
				beta = Math.min(beta, alphaBetaWithMemory2(child, alpha, beta, depth + 1));
				if (beta <= alpha) {
					table.storeUpperBound(node, beta);
					return alpha; // αカット
				}
			}
			return beta;
		}
	}
	
	/**
	 * 置換表付きアルファ・ベータ探索を行う。
	 * ネットで拾った誰かのコード。
	 * 
	 * 値は正しいっぽいが、激遅プンプン丸。
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
	 * 置換表なしの普通のアルファベータ探索
	 * @param node
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	protected int alphaBetaNormal(Board node, int alpha, int beta, int depth){
		
		// 読み最深部 or 盤が全て埋まった場合
		if (depth == maxDepth || node.isFull()) {
			int val = strategy.evaluate(node);
			return val;
		}

		//次盤面生成
		Board[] childNodes = node.createChildNodes();
		if (node.isNoFuture()) {
			return strategy.evaluate(node);
		}
		
		// 自分手番のとき
		if (node.getMoveStone() == Evaluator.MY_STONE) {
			for (Board child : childNodes) {
				int value = alphaBetaNormal(child, alpha, beta, depth + 1);
				if (depth == 0 && alpha < value) {
					bestNextNode = child;
				}
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					return beta; // βカット
				}
			}
			return alpha;
		}
		// 相手手番の場合
		else {
			for (Board child : childNodes) {
				beta = Math.min(beta, alphaBetaNormal(child, alpha, beta, depth + 1));
				if (beta <= alpha) {
					return alpha; // αカット
				}
			}
			return beta;
		}
	}
	
	/**
	 * ノードキャッシュ用テーブル
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
