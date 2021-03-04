package jp.co.altonotes.reversi.type;

/**
 * •ÎA”’Î
 * @author Yamamoto Keita
 *
 */
public enum Stone {
	NONE,
	BLACK,
	WHITE;
	
	/**
	 * @return ‘Šè‚ÌÎ‚ÌF
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
