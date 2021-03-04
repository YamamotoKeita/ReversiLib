package jp.co.altonotes.reversi.type;

/**
 * ���΁A����
 * @author Yamamoto Keita
 *
 */
public enum Stone {
	NONE,
	BLACK,
	WHITE;
	
	/**
	 * @return ����̐΂̐F
	 */
	public Stone oppositeStone() {
		if (this == BLACK) {
			return WHITE;
		}
		else if (this == WHITE) {
			return BLACK;
		}
		else {
			return NONE;
		}
	}
}
