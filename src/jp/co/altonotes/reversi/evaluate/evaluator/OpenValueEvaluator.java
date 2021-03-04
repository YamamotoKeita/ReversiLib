package jp.co.altonotes.reversi.evaluate.evaluator;

import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.type.Stone;

/**
 * �J���x��]������B
 * �����̐΂ׂ̗��󂢂Ă�����A���肪�u����ꏊ�������s���ɂȂ�Ƃ����l���B
 * 
 * @author Yamamoto Keita
 *
 */
public class OpenValueEvaluator extends Evaluator {
	
	/** �J���x�_				*/
	private final static int OPEN_POINT = -5;
	
	private int limit = 58;

	/* (non-Javadoc)
	 * @see jp.co.altonotes.reversi.evaluate.evaluator.Evaluator#evaluate(jp.co.altonotes.reversi.model.Board, jp.co.altonotes.reversi.type.Stone, int)
	 */
	@Override
	public int evaluate(Board board) {
		int value = 0;
		// �I�Ղ͖���
		if (limit <= board.getStoneCount() || board.isNoFuture()) {
			return value;
		}
		
		int loopCount = Board.POSITION_TABLE.length;
		
		for (int i = 0; i < loopCount; i++) {
			int[] position = Board.POSITION_TABLE[i];
			int x = position[0];
			int y = position[1];
			
			// �󂫃}�X�̏ꍇ
			if (board.getStone(x, y) == Stone.NONE) {
				for (int j = 0; j < Board.VECTORS.length; j++) {//�ׂ�
					int[] vec = Board.VECTORS[j];
					int tx = x + vec[0];
					int ty = y + vec[1];
					
					// �ՖʊO�Ȃ�X�L�b�v
					if (tx < 0 || 7 < tx || ty < 0 || 7 < ty) {
						continue;
					}
					
					Stone stone = board.getStone(tx, ty);
					if (stone != Stone.NONE) {
						// �����̐F�Ȃ�
						if (stone == MY_STONE) {
							value += OPEN_POINT;
						}
						// ����̐F�Ȃ�
						else {
							value -= OPEN_POINT;
						}
					}
				}
			}
		}
		
		return value;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
