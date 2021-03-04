package jp.co.altonotes.reversi.model;

import java.util.ArrayList;
import java.util.Arrays;

import jp.co.altonotes.reversi.type.Stone;

/**
 * オセロの盤面。石の配置状態を持つ。
 * 
 * @author Yamamoto Keita
 *
 */
public class Board {
	/** インデックス から 座標 		*/ 
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
	
	/** 座標 からインデックス	*/ 
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
	
	/** ８方向基本ベクトル		*/
	public final static int[][]	VECTORS = {{0,-1}, {1,-1}, {1,0}, {1,1}, {0,1}, {-1,1}, {-1,0}, {-1,-1}};

	/** bit フラグ削除用配列	*/ 
	private final static byte[]	REMOVE_BIT = {(byte)0xFE, (byte)0xFD, (byte)0xFB, (byte)0xF7, (byte)0xEF, (byte)0xDF, (byte)0xBF, (byte)0x7F};

	/** 子ノードの盤面 */
	private static ArrayList<Board> workArray = new ArrayList<Board>(40);

	/** 黒石の配置 */
	private byte[] blackStones = new byte[8];
	
	/** 白石の配置 */
	private byte[] whiteStones = new byte[8];

	/** 石の数 */
	private int stoneCount;
	
	/** 手番の石 */
	private Stone moveStone = Stone.BLACK;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	/**
	 * 初期状態の盤面を作成する
	 * @return 初期状態の盤面
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
	 * @return 盤面のコピー
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
	 * 次の一手を足した盤面を作成する
	 * @param index
	 * @return
	 */
	public Board createNextBoard(int index) {
		if (getStone(index) != Stone.NONE) {
			throw new IllegalArgumentException("この位置に石を置けません:" + index);
		}
		
		Board[] childNodes = createChildNodes();
		for (Board board : childNodes) {
			Stone stone = board.getStone(index);
			if (stone != Stone.NONE) {
				return board;
			}
		}
		
		throw new IllegalArgumentException("この位置に石を置けません:" + index);
	}
	
	/**
	 * 指定した位置に石が置けるか判定する
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
	 * 最後に置いた石のインデックスを取得する
	 * @param preBoard
	 * @return
	 */
	public int getLastIndex(Board preBoard) {
		//８×８マス探索
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
	 * 次の打ち手を決定する
	 */
	public void fixNextPlayer() {
		createChildNodes();
	}

	/**
	 * @return　子ノードの盤面
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
	 * 子ノードの盤面を作成する
	 */
	private void prepareChildren(){
		
		Stone oppositeStone = moveStone.oppositeStone();
		
		//８×８マス探索
		for (int i = 0; i < POSITION_TABLE.length; i++) {
			Board newBoard = null;
			int x = POSITION_TABLE[i][0];
			int y = POSITION_TABLE[i][1];
			
			if (getStone(x, y) != Stone.NONE) {
				continue;
			}
			
			// 周囲８方向につき探索
			for (int j = 0; j < VECTORS.length; j++) {
				int vx = VECTORS[j][0];
				int vy = VECTORS[j][1];
				
				// 敵石である限り進める
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
				
				// 自分の石ならひっくり返す
				if (getStone(tx, ty) == moveStone) {
					if (newBoard == null) {
						// 新規盤面生成
						newBoard = copy();
						workArray.add(newBoard);

						newBoard.moveStone = oppositeStone;
						newBoard.addStone(moveStone, x, y);
					}
					
					// １つ戻って
					tx-= vx;
					ty-= vy;
					
					// 元の位置に戻るまでひっくり返す
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
	 * 指定座標に石を置く
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
	 * 石をひっくり返す
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
	 * 指定座標位置の石を取得する
	 * @param x
	 * @param y
	 * @return 指定座標位置の石
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
	 * 指定インデックス位置の石を取得する
	 * @param index
	 * @return
	 */
	public Stone getStone(int index) {
		int[] pos = indexToPos(index);
		return getStone(pos[0], pos[1]);
	}

	/**
	 * @return 指定した色の石の数
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
	 * @return 石の数
	 */
	public int getStoneCount() {
		return stoneCount;
	}

	/**
	 * @return 盤面が全て埋まった場合 true
	 */
	public boolean isFull() {
		return stoneCount == 64;
	}

	/**
	 * @return 一色で全て埋まり、次の手が打てない場合 true
	 */
	public boolean isNoFuture() {
		return moveStone == Stone.NONE;
	}

	/**
	 * @return 手番の石の色
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
					sb.append("● ");
				}
				else if (stone == Stone.WHITE) {
					sb.append("○ ");
				}
				else {
					sb.append("― ");
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
	 * @return インデント付きの盤面文字列
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
					sb.append("● ");
				}
				else if (stone == Stone.WHITE) {
					sb.append("○ ");
				}
				else {
					sb.append("□ ");
				}
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append("\r\n");
		}
		
		return sb.toString();
	}

	/**
	 * "C4" などの座標を表す文字列を 0～63のインデックスに変換する
	 * @param code
	 * @return
	 */
	public static int codeToIndex(String code) {
		int[] pos = codeToPosition(code);
		int index = posToIndex(pos);
		return index;
	}
	
	/**
	 * {0, 0} ～ {7, 7} の座標値を 0～63のインデックスに変換する
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
	 * 0～63のインデックス値を {2, 3} などの座標値に変換する
	 * @param index
	 * @return
	 */
	public static int[] indexToPos(int index) {
		return POSITION_TABLE[index];
	}

	/**
	 * "C4" などの座標を表す文字列を {2, 3} などの int 配列の座標値に変換する
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
	 * 0～63のインデックス値を "C4" などの文字列に変換する
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
	 * 0～7 のインデックス値をA～Hの列を表す文字列に変換する
	 * @param i
	 * @return
	 */
	private static String indexToColCode(int i) {
		int c = 'A' + i;
		return String.valueOf((char)c);
	}

	/**
	 * A～Hの列を表す文字列を 0～7 のインデックス値に変換する
	 * @param code
	 * @return
	 */
	private static int colCodeToIndex(String code) {
		char c = code.toUpperCase().charAt(0);
		return c - 'A';
	}
}
