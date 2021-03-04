package jp.co.altonotes.reversi.joseki;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.co.altonotes.reversi.model.Board;

/**
 * 定石から最善手を検索する。
 * 前提として、定石にはパスが存在してはいけないという制限がある。
 * 
 * @author Yamamoto Keita
 */
public class JosekiSearcher {
	
	/** 打石場所のリスト	*/
	ArrayList<Integer> indexList = new ArrayList<Integer>();

	/** 実際の初手 */
	private FirstHand firstHand = FirstHand.F5;
	
	/** 定石のルートノード */
	private JosekiNode rootNode;

	/** 次の最善手 */
	private int bestNextIndex;
	

	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void loadJosekiData(String filePath) throws IOException {
		rootNode = JosekiNode.createByData(filePath);
	}

	public void loadJosekiData(InputStream input) throws IOException {
		rootNode = JosekiNode.createByInput(input);
	}

	/**
	 * 最善手のインデックスを取得する
	 * @return
	 */
	public int getBestIndex() {
		if (rootNode == null || rootNode.isLeafNode()) {
			return -1;
		}
		
		// 初手
		if (indexList.size() == 0) {
			return firstHand.getIndex();
		}
		
		int index = searchBestIndex(indexList.size() % 2 == 0);
		index = convertJosekiIndex(firstHand, index);
		
		return index;
	}

	/**
	 * 定石データは初手C4前提なので、実際の初手がC4以外の場合、インデックスの変換を行う
	 * @param index
	 * @return
	 */
	private static int convertJosekiIndex(FirstHand firstHand, int index) {
		boolean xyReverse = false;
		boolean negaPosiReverse = false;
		
		switch (firstHand) {
		case C4:
			break;
		case D3: // XY反転
			xyReverse = true;
			break;
		case F5: // 対称
			negaPosiReverse = true;
			break;
		case E6: // XY反転+対称
			xyReverse = true;
			negaPosiReverse = true;
			break;
		}
		
		int[] pos = Board.indexToPos(index);
		
		int x = pos[0];
		int y = pos[1];
		if (xyReverse) {
			x = pos[1];
			y = pos[0];
		}
		
		if (negaPosiReverse) {
			x = 7 - x;
			y = 7 - y;
		}
		
		return Board.posToIndex(x, y);
	}

	/**
	 * @param b
	 * @return
	 */
	private int searchBestIndex(boolean isBlack) {
		bestNextIndex = -1;
		alphaBeta(rootNode, Integer.MIN_VALUE, Integer.MAX_VALUE, isBlack, 0);
		return bestNextIndex;
	}

	private int alphaBeta(JosekiNode node, int alpha, int beta, boolean isBlack, int depth){
		
		// 読み最深部
		if (node.isLeafNode()) {
			return node.getValue();
		}

		//次盤面生成
		JosekiNode[] childNodes = node.getChildList();
		
		// 黒手番のとき
		if (isBlack) {
			for (JosekiNode child : childNodes) {
				int value = alphaBeta(child, alpha, beta, !isBlack, depth+1);
				if (depth == 0 && alpha < value) {
					bestNextIndex = child.getIndex();
				}
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					return beta; // βカット
				}
			}
			return alpha;
		}
		// 白手番の場合
		else {
			for (JosekiNode child : childNodes) {
				int value = alphaBeta(child, alpha, beta, !isBlack, depth+1);
				if (depth == 0 && value < beta) {
					bestNextIndex = child.getIndex();
				}
				beta = Math.min(beta, value);
				if (beta <= alpha) {
					return alpha; // αカット
				}
			}
			return beta;
		}
	}
	
	/**
	 * 初手の黒石の位置（４パターンしかない）
	 * @author Yamamoto Keita
	 */
	private static enum FirstHand {
		C4,
		D3,
		F5,
		E6,
		;
		
		public int getIndex() {
			return Board.codeToIndex(name());
		}

		/**
		 * @param index
		 * @return
		 */
		public static FirstHand lookup(int index) {
			for (FirstHand value : values()) {
				if (Board.codeToIndex(value.name()) == index) {
					return value;
				}
			}
			return null;
		}
	}

	/**
	 * @param index
	 */
	public void record(int index) {
		// 初手を決定
		if (indexList.size() == 0) {
			firstHand = FirstHand.lookup(index);
		} else if (rootNode != null) {
			index = convertJosekiIndex(firstHand, index);

			JosekiNode nextNode = null;
			JosekiNode[] childList = rootNode.getChildList();
			if (childList != null) {
				for (JosekiNode node : childList) {
					if (node.getIndex() == index) {
						nextNode = node;
						break;
					}
				}
			}
			rootNode = nextNode;
			if (rootNode == null) {
				System.out.println("定石切れ");
			}
		}

		indexList.add(index);
	}
	
}
