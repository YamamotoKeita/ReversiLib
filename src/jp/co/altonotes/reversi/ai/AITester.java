package jp.co.altonotes.reversi.ai;

import java.io.IOException;

import jp.co.altonotes.reversi.joseki.JosekiSearcher;
import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.type.Stone;

/**
 * @author Yamamoto Keita
 *
 */
public class AITester {

	private final static String JOSEKI_PATH = "C:\\DevTools\\eclipse-3.7-juno-64\\workspace\\ReversiLib\\data\\small";

	public static void main(String[] args) throws IOException {
		AIPlayer baseAI = new AIPlayer();
		AIPlayer testAI = new AIPlayer();
		
		for (int power = 0; power < 1; power++) {
			
			int[] stoneDiff = new int[2];
			
			// �p���[��ݒ�
//			testAI.setPower(0, StoneCountEvaluator.class);
//			testAI.setPower(power, PositionEvaluator.class);
//			testAI.setPower(0, OpenValueEvaluator.class);
			
			for (int i = 0; i < 2; i++) {
				// ��Ԃ�ݒ�
				if (i == 0) {
					baseAI.setMyStone(Stone.BLACK);
					testAI.setMyStone(Stone.WHITE);
				} else {
					baseAI.setMyStone(Stone.WHITE);
					testAI.setMyStone(Stone.BLACK);
				}
				
				// �����Ֆʍ쐬
				Board board = Board.createFirstBoard();
				JosekiSearcher josekiSearcher = new JosekiSearcher();
				josekiSearcher.loadJosekiData(JOSEKI_PATH);
				
				
				// �����J�n
				while (!board.isFull() && !board.isNoFuture()) {
					// �ł�������߂�
					AIPlayer player;
					if (baseAI.getMyStone() == board.getMoveStone()) {
						player = baseAI;
					} else {
						player = testAI;
					}

					//System.out.println(board);

					// �łꏊ�����߂�
					int index = player.nextStoneIndex(board, josekiSearcher);
					josekiSearcher.record(index);
					//System.out.println(Board.indexToCode(index));
					
					board = board.createNextBoard(index);
					
					// ���̑ł���𔻒�
					board.fixNextPlayer();
				}
				
				stoneDiff[i] = board.getStoneCount(testAI.getMyStone()) - board.getStoneCount(baseAI.getMyStone());
			}
			
			System.out.println("power:" + power + "   --------------------------------------");
			System.out.println((0 < stoneDiff[0] ? "����" : "����") + ":" + stoneDiff[0]);
			System.out.println((0 < stoneDiff[1] ? "����" : "����") + ":" + stoneDiff[1]);
		}

	}
}
