package jp.co.altonotes.reversi.algorithm;

import jp.co.altonotes.reversi.model.Board;

/**
 * @author Yamamoto Keita
 *
 */
public class NegaScout extends SearchAlgorithm {

	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.algorithm.SearchAlgorithm#search(jp.co.altonotes.reversi.model.Board)
	 */
	@Override
	protected int search(Board board) {
		return 0;
	}

	public int search(Board node, int α, int β, int depth ) {
		if ( depth == maxDepth ) {
			return strategy.evaluate( node ); 
		}
		
		Board[] childNodes = node.createChildNodes();
		
		if (node.isNoFuture()) {
			return strategy.evaluate( node ); 
		}
		
		// 手を並べ替える
		order(childNodes);

		Board bestChild = childNodes[0]; 
		
		int v = -search ( bestChild, -β, -α, depth + 1 ); /* 最善候補を通常の窓で探索 */
		int max = v;
		if ( β <= v ) return v; /* カット */
		if ( α < v ) α = v;

		for (Board child : childNodes) {
			v = -search ( child, -α - 1, -α, depth + 1 ); /* Null Window Search */
			if ( β <= v ) return v; /* カット */
			if ( α < v ) {
				α = v;
				v = -search( child, -β, -α, depth + 1 ); /* 通常の窓で再探索 */
				if ( β <= v ) return v; /* カット */
				if ( α < v ) α = v;
			}
			if( max < v ) max = v;
		}
		
		return max; /* 子ノードの最大値を返す (fail-soft) */
	}

	/**
	 * @param childNodes
	 */
	private void order(Board[] childNodes) {
		// TODO 盤面を評価値で並べ替える
		
	}
	
}
