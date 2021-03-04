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

	public int search(Board node, int ��, int ��, int depth ) {
		if ( depth == maxDepth ) {
			return strategy.evaluate( node ); 
		}
		
		Board[] childNodes = node.createChildNodes();
		
		if (node.isNoFuture()) {
			return strategy.evaluate( node ); 
		}
		
		// �����בւ���
		order(childNodes);

		Board bestChild = childNodes[0]; 
		
		int v = -search ( bestChild, -��, -��, depth + 1 ); /* �őP����ʏ�̑��ŒT�� */
		int max = v;
		if ( �� <= v ) return v; /* �J�b�g */
		if ( �� < v ) �� = v;

		for (Board child : childNodes) {
			v = -search ( child, -�� - 1, -��, depth + 1 ); /* Null Window Search */
			if ( �� <= v ) return v; /* �J�b�g */
			if ( �� < v ) {
				�� = v;
				v = -search( child, -��, -��, depth + 1 ); /* �ʏ�̑��ōĒT�� */
				if ( �� <= v ) return v; /* �J�b�g */
				if ( �� < v ) �� = v;
			}
			if( max < v ) max = v;
		}
		
		return max; /* �q�m�[�h�̍ő�l��Ԃ� (fail-soft) */
	}

	/**
	 * @param childNodes
	 */
	private void order(Board[] childNodes) {
		// TODO �Ֆʂ�]���l�ŕ��בւ���
		
	}
	
}
