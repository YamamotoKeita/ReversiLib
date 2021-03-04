package jp.co.altonotes.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.altonotes.reversi.type.Stone;

/**
 * �I�Z���̔ՖʁB�΂̔z�u��Ԃ����B
 * 
 * @author Yamamoto Keita
 *
 */
public class Board {
	/** �C���f�b�N�X ���� ���W 		*/ 
	public final static int[][]	POSITION_TABLE = {
		{0,0},{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},
		{1,0},{1,1},{1,2},{1,3},{1,4},{1,5},{1,6},{1,7},
		{2,0},{2,1},{2,2},{2,3},{2,4},{2,5},{2,6},{2,7},
		{3,0},{3,1},{3,2},{3,3},{3,4},{3,5},{3,6},{3,7},
		{4,0},{4,1},{4,2},{4,3},{4,4},{4,5},{4,6},{4,7},
		{5,0},{5,1},{5,2},{5,3},{5,4},{5,5},{5,6},{5,7},
		{6,0},{6,1},{6,2},{6,3},{6,4},{6,5},{6,6},{6,7},
		{7,0},{7,1},{7,2},{7,3},{7,4},{7,5},{7,6},{7,7},
	};
	
	/** ���W ����C���f�b�N�X	*/ 
	public final static int[][]	INDEX_TABLE = {
		{0,  1, 2, 3, 4, 5, 6, 7},
		{8,  9,10,11,12,13,14,15},
		{16,17,18,19,20,21,22,23},
		{24,25,26,27,28,29,30,31},
		{32,33,34,35,36,37,38,39},
		{40,41,42,43,44,45,46,47},
		{48,49,50,51,52,53,54,55},
		{56,57,58,59,60,61,62,63}
	};
	
	/** �W������{�x�N�g��		*/
	public final static int[][]	VECTORS = {{0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}};

	/** bit �t���O�폜�p�z��	*/ 
	private final static byte[]	REMOVE_BIT = {(byte)0xFE, (byte)0xFD, (byte)0xFB, (byte)0xF7, (byte)0xEF, (byte)0xDF, (byte)0xBF, (byte)0x7F};

	/** �q�m�[�h�̔Ֆ� */
	private static ArrayList<Board> workArray = new ArrayList<Board>(40);

	/** ���΂̔z�u */
	private byte[] blackStones = new byte[8];
	
	/** ���΂̔z�u */
	private byte[] whiteStones = new byte[8];

	/** �΂̐� */
	private int stoneCount;
	
	/** ��Ԃ̐� */
	private Stone moveStone = Stone.BLACK;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	/**
	 * ������Ԃ̔Ֆʂ��쐬����
	 * @return ������Ԃ̔Ֆ�
	 */
	public static Board createFirstBoard() {
		Board bd = new Board();
		bd.addStone(Stone.WHITE, 3, 3);
		bd.addStone(Stone.WHITE, 4, 4);
		bd.addStone(Stone.BLACK, 3, 4);
		bd.addStone(Stone.BLACK, 4, 3);
		return bd;
	}
	
	/**
	 * @return �Ֆʂ̃R�s�[
	 */
	public Board copy() {
		Board copy = new Board();
		System.arraycopy(blackStones, 0, copy.blackStones, 0, blackStones.length);
		System.arraycopy(whiteStones, 0, copy.whiteStones, 0, whiteStones.length);
		copy.stoneCount = stoneCount;
		copy.moveStone = moveStone;
		return copy;
	}
	
	/**
	 * ���̈��𑫂����Ֆʂ��쐬����
	 * @param index
	 * @return
	 */
	public Board createNextBoard(int index) {
		if (getStone(index) != Stone.NONE) {
			throw new IllegalArgumentException("���̈ʒu�ɐ΂�u���܂���:" + index);
		}
		
		Board[] childNodes = createChildNodes();
		for (Board board : childNodes) {
			Stone stone = board.getStone(index);
			if (stone != Stone.NONE) {
				return board;
			}
		}
		
		throw new IllegalArgumentException("���̈ʒu�ɐ΂�u���܂���:" + index);
	}
	
	/**
	 * �w�肵���ʒu�ɐ΂��u���邩���肷��
	 * @param index
	 * @return
	 */
	public boolean isSettable(int index) {
		if (getStone(index) != Stone.NONE) {
			return false;
		}
		
		Board[] childNodes = createChildNodes();
		for (Board board : childNodes) {
			Stone stone = board.getStone(index);
			if (stone != Stone.NONE) {
				return true;
			}
		}
		return false;
	}


	/**
	 * �Ō�ɒu�����΂̃C���f�b�N�X���擾����
	 * @param preBoard
	 * @return
	 */
	public int getLastIndex(Board preBoard) {
		//�W�~�W�}�X�T��
		for (int i = 0; i < POSITION_TABLE.length; i++) {
			int[] pos = POSITION_TABLE[i];
			int x = pos[0];
			int y = pos[1];
			
			if (preBoard.getStone(x, y) == Stone.NONE
					&& getStone(x, y) != Stone.NONE) {
				return posToIndex(pos);
			}
		}
		return -1;
	}

	/**
	 * ���̑ł�������肷��
	 */
	public void fixNextPlayer() {
		createChildNodes();
	}

	/**
	 * @return�@�q�m�[�h�̔Ֆ�
	 */
	public Board[] createChildNodes() {
		workArray.clear();
		prepareChildren();

		if (workArray.size() == 0) {
			moveStone = moveStone.oppositeStone();
			prepareChildren();
		}

		if (workArray.size() == 0) {
			moveStone = Stone.NONE;
		}

		return workArray.toArray(new Board[workArray.size()]);
	}
	
	/**
	 * �q�m�[�h�̔Ֆʂ��쐬����
	 */
	private void prepareChildren(){
		
		Stone oppositeStone = moveStone.oppositeStone();
		
		//�W�~�W�}�X�T��
		for (int i = 0; i < POSITION_TABLE.length; i++) {
			Board newBoard = null;
			int x = POSITION_TABLE[i][0];
			int y = POSITION_TABLE[i][1];
			
			if (getStone(x, y) != Stone.NONE) {
				continue;
			}
			
			// ���͂W�����ɂ��T��
			for (int j = 0; j < VECTORS.length; j++) {
				int vx = VECTORS[j][0];
				int vy = VECTORS[j][1];
				
				// �G�΂ł������i�߂�
				int tx = x + vx;
				int ty = y + vy;
				while (getStone(tx, ty) == oppositeStone) {
					tx += vx;
					ty += vy;
					if (tx < 0 || 7 < tx || ty < 0 || 7 < ty) {
						break;
					}
				}
				
				if (tx == x + vx && ty == y + vy) {
					continue;
				}
				
				// �����̐΂Ȃ�Ђ�����Ԃ�
				if (getStone(tx, ty) == moveStone) {
					if (newBoard == null) {
						// �V�K�Ֆʐ���
						newBoard = copy();
						workArray.add(newBoard);

						newBoard.moveStone = oppositeStone;
						newBoard.addStone(moveStone, x, y);
					}
					
					// �P�߂���
					tx-= vx;
					ty-= vy;
					
					// ���̈ʒu�ɖ߂�܂łЂ�����Ԃ�
					while (!(tx==x && ty==y)) {
						newBoard.reverse(tx, ty);
						tx -= vx;
						ty -= vy;
					}
				}
			}
		}
	}
	
	/**
	 * �w����W�ɐ΂�u��
	 * @param stone
	 * @param x
	 * @param y
	 */
	public void addStone(Stone stone, int x, int y) {
		stoneCount++;
		switch (stone) {
		case BLACK:
			blackStones[y] = (byte) ( blackStones[y] | (1<<x) );
			break;
		case WHITE:
			whiteStones[y] = (byte) ( whiteStones[y] | (1<<x) );
			break;
		default:
			break;
		}
	}

	/**
	 * �΂��Ђ�����Ԃ�
	 * @param x
	 * @param y
	 */
	public void reverse(int x, int y) {
		Stone stone = getStone(x, y);
		if (stone == Stone.BLACK) {
			blackStones[y] = (byte) ( blackStones[y] & REMOVE_BIT[x] );
			whiteStones[y] = (byte) ( whiteStones[y] | (1<<x) );
		} 
		else if (stone == Stone.WHITE) {
			whiteStones[y] = (byte) ( whiteStones[y] & REMOVE_BIT[x] );
			blackStones[y] = (byte) ( blackStones[y] | (1<<x) );
		}
	}

	/**
	 * �w����W�ʒu�̐΂��擾����
	 * @param x
	 * @param y
	 * @return �w����W�ʒu�̐�
	 */
	public Stone getStone(int x, int y) {
		if (x < 0 || 7 < x || y < 0 || 7 < y) {
			return Stone.NONE;
		}
		
		if (0 < (blackStones[y] & (1<<x))) {
			return Stone.BLACK;
		}
		else if (0 < (whiteStones[y] & (1<<x))) {
			return Stone.WHITE;
		} 
		else {
			return Stone.NONE;
		}
	}
	
	/**
	 * �w��C���f�b�N�X�ʒu�̐΂��擾����
	 * @param index
	 * @return
	 */
	public Stone getStone(int index) {
		int[] pos = indexToPos(index);
		return getStone(pos[0], pos[1]);
	}

	/**
	 * @return �w�肵���F�̐΂̐�
	 */
	public int getStoneCount(Stone stone) {
		byte[] array;
		if (stone == Stone.BLACK) {
			array = blackStones;
		} else if (stone == Stone.WHITE) {
			array = whiteStones;
		} else {
			throw new IllegalArgumentException("Don't set Stone.None !!");
		}
		
		int count = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if ((0 < (array[y] & (1<<x)))) {
					count ++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * @return �΂̐�
	 */
	public int getStoneCount() {
		return stoneCount;
	}

	/**
	 * @return �Ֆʂ��S�Ė��܂����ꍇ true
	 */
	public boolean isFull() {
		return stoneCount == 64;
	}

	/**
	 * @return ��F�őS�Ė��܂�A���̎肪�łĂȂ��ꍇ true
	 */
	public boolean isNoFuture() {
		return moveStone == Stone.NONE;
	}

	/**
	 * @return ��Ԃ̐΂̐F
	 */
	public Stone getMoveStone() {
		return moveStone;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + Arrays.hashCode(blackStones);
		result = prime * result + Arrays.hashCode(whiteStones);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.equals(blackStones, other.blackStones))
			return false;
		if (!Arrays.equals(whiteStones, other.whiteStones))
			return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(80);
		
		for (int y = 0; y < blackStones.length; y++) {
			for (int x = 0; x < 8; x++) {
				Stone stone = getStone(x, y);
				if (stone == Stone.BLACK) {
					sb.append("�� ");
				}
				else if (stone == Stone.WHITE) {
					sb.append("�� ");
				}
				else {
					sb.append("�\ ");
				}
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append("\r\n");
		}
		
		return sb.toString();
	}

	/**
	 * 
	 * @param indentNum
	 * @return �C���f���g�t���̔Ֆʕ�����
	 */
	public String toString(int indentNum) {
		StringBuilder sb = new StringBuilder(80);
		
		String indent = "";
		for (int i = 0; i < indentNum; i++) {
			indent += "   ";
		}
		
		for (int y = 0; y < blackStones.length; y++) {
			sb.append(indent);
			for (int x = 0; x < 8; x++) {
				Stone stone = getStone(x, y);
				if (stone == Stone.BLACK) {
					sb.append("�� ");
				}
				else if (stone == Stone.WHITE) {
					sb.append("�� ");
				}
				else {
					sb.append("�� ");
				}
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append("\r\n");
		}
		
		return sb.toString();
	}

	/**
	 * "C4" �Ȃǂ̍��W��\��������� 0�`63�̃C���f�b�N�X�ɕϊ�����
	 * @param code
	 * @return
	 */
	public static int codeToIndex(String code) {
		int[] pos = codeToPosition(code);
		int index = posToIndex(pos);
		return index;
	}
	
	/**
	 * {0, 0} �` {7, 7} �̍��W�l�� 0�`63�̃C���f�b�N�X�ɕϊ�����
	 * @param pos
	 * @return
	 */
	public static int posToIndex(int[] pos) {
		int x = pos[0];
		int y = pos[1];
		return INDEX_TABLE[x][y];
	}

	public static int posToIndex(int x, int y) {
		return INDEX_TABLE[x][y];
	}

	/**
	 * 0�`63�̃C���f�b�N�X�l�� {2, 3} �Ȃǂ̍��W�l�ɕϊ�����
	 * @param index
	 * @return
	 */
	public static int[] indexToPos(int index) {
		return POSITION_TABLE[index];
	}

	/**
	 * "C4" �Ȃǂ̍��W��\��������� {2, 3} �Ȃǂ� int �z��̍��W�l�ɕϊ�����
	 * @param code
	 * @return
	 */
	public static int[] codeToPosition(String code) {
		String col = code.substring(0, 1);
		String row = code.substring(1, 2);
		
		int x = colCodeToIndex(col);
		int y = Integer.parseInt(row) - 1;
		return new int[]{x, y};
	}

	/**
	 * 0�`63�̃C���f�b�N�X�l�� "C4" �Ȃǂ̕�����ɕϊ�����
	 * @param index
	 * @return
	 */
	public static String indexToCode(int index) {
		int[] pos = indexToPos(index);
		String col = indexToColCode(pos[0]);
		String row = String.valueOf(pos[1] + 1);
		return col + row;
	}

	/**
	 * 0�`7 �̃C���f�b�N�X�l��A�`H�̗��\��������ɕϊ�����
	 * @param i
	 * @return
	 */
	private static String indexToColCode(int i) {
		int c = 'A' + i;
		return String.valueOf((char)c);
	}

	/**
	 * A�`H�̗��\��������� 0�`7 �̃C���f�b�N�X�l�ɕϊ�����
	 * @param code
	 * @return
	 */
	private static int colCodeToIndex(String code) {
		char c = code.toUpperCase().charAt(0);
		return c - 'A';
	}
}
