package jp.co.altonotes.reversi.joseki;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.co.altonotes.reversi.model.Board;

/**
 * ��΂���őP�����������B
 * �O��Ƃ��āA��΂ɂ̓p�X�����݂��Ă͂����Ȃ��Ƃ�������������B
 * 
 * @author Yamamoto Keita
 */
public class JosekiSearcher {
	
	/** �ŐΏꏊ�̃��X�g	*/
	ArrayList<Integer> indexList = new ArrayList<Integer>();

	/** ���ۂ̏��� */
	private FirstHand firstHand = FirstHand.F5;
	
	/** ��΂̃��[�g�m�[�h */
	private JosekiNode rootNode;

	/** ���̍őP�� */
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
	 * �őP��̃C���f�b�N�X���擾����
	 * @return
	 */
	public int getBestIndex() {
		if (rootNode == null || rootNode.isLeafNode()) {
			return -1;
		}
		
		// ����
		if (indexList.size() == 0) {
			return firstHand.getIndex();
		}
		
		int index = searchBestIndex(indexList.size() % 2 == 0);
		index = convertJosekiIndex(firstHand, index);
		
		return index;
	}

	/**
	 * ��΃f�[�^�͏���C4�O��Ȃ̂ŁA���ۂ̏��肪C4�ȊO�̏ꍇ�A�C���f�b�N�X�̕ϊ����s��
	 * @param index
	 * @return
	 */
	private static int convertJosekiIndex(FirstHand firstHand, int index) {
		boolean xyReverse = false;
		boolean negaPosiReverse = false;
		
		switch (firstHand) {
		case C4:
			break;
		case D3: // XY���]
			xyReverse = true;
			break;
		case F5: // �Ώ�
			negaPosiReverse = true;
			break;
		case E6: // XY���]+�Ώ�
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
		
		// �ǂݍŐ[��
		if (node.isLeafNode()) {
			return node.getValue();
		}

		//���Ֆʐ���
		JosekiNode[] childNodes = node.getChildList();
		
		// ����Ԃ̂Ƃ�
		if (isBlack) {
			for (JosekiNode child : childNodes) {
				int value = alphaBeta(child, alpha, beta, !isBlack, depth+1);
				if (depth == 0 && alpha < value) {
					bestNextIndex = child.getIndex();
				}
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					return beta; // ���J�b�g
				}
			}
			return alpha;
		}
		// ����Ԃ̏ꍇ
		else {
			for (JosekiNode child : childNodes) {
				int value = alphaBeta(child, alpha, beta, !isBlack, depth+1);
				if (depth == 0 && value < beta) {
					bestNextIndex = child.getIndex();
				}
				beta = Math.min(beta, value);
				if (beta <= alpha) {
					return alpha; // ���J�b�g
				}
			}
			return beta;
		}
	}
	
	/**
	 * ����̍��΂̈ʒu�i�S�p�^�[�������Ȃ��j
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
		// ���������
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
				System.out.println("��ΐ؂�");
			}
		}

		indexList.add(index);
	}
	
}
