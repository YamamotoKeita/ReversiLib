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
			
			// パワーを設定
//			testAI.setPower(0, StoneCountEvaluator.class);
//			testAI.setPower(power, PositionEvaluator.class);
//			testAI.setPower(0, OpenValueEvaluator.class);
			
			for (int i = 0; i < 2; i++) {
				// 手番を設定
				if (i == 0) {
					baseAI.setMyStone(Stone.BLACK);
					testAI.setMyStone(Stone.WHITE);
				} else {
					baseAI.setMyStone(Stone.WHITE);
					testAI.setMyStone(Stone.BLACK);
				}
				
				// 初期盤面作成
				Board board = Board.createFirstBoard();
				JosekiSearcher josekiSearcher = new JosekiSearcher();
				josekiSearcher.loadJosekiData(JOSEKI_PATH);
				
				
				// 試合開始
				while (!board.isFull() && !board.isNoFuture()) {
					// 打ち手を決める
					AIPlayer player;
					if (baseAI.getMyStone() == board.getMoveStone()) {
						player = baseAI;
					} else {
						player = testAI;
					}

					//System.out.println(board);

					// 打つ場所を決める
					int index = player.nextStoneIndex(board, josekiSearcher);
					josekiSearcher.record(index);
					//System.out.println(Board.indexToCode(index));
					
					board = board.createNextBoard(index);
					
					// 次の打ち手を判定
					board.fixNextPlayer();
				}
				
				stoneDiff[i] = board.getStoneCount(testAI.getMyStone()) - board.getStoneCount(baseAI.getMyStone());
			}
			
			System.out.println("power:" + power + "   --------------------------------------");
			System.out.println((0 < stoneDiff[0] ? "勝ち" : "負け") + ":" + stoneDiff[0]);
			System.out.println((0 < stoneDiff[1] ? "勝ち" : "負け") + ":" + stoneDiff[1]);
		}

	}
}
