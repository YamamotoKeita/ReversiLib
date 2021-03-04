package jp.co.altonotes.reversi.joseki;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.util.ByteUtils;

/**
 * 定石データ
 * @author Yamamoto Keita
 *
 */
public class Joseki {
	public final static byte SEPARATOR_BYTE = 64;
	
	private final static String SEPARATOR = ";";


	/** 棋譜。石の打ち場所インデックスの配列 */
	private byte[] indexList;

	/** 評価値 */
	private int value;
	
	public Joseki() {
	}
	
	/**
	 * コンストラクター
	 * @param line
	 */
	public Joseki(String line) {
		String[] values = line.split(SEPARATOR);
		String kifu = values[0].trim();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		int offset = 0;
		// 棋譜を書き出し
		while (offset < kifu.length()) {
			String posCode = kifu.substring(offset, offset + 2);
			int posIndex = Board.codeToIndex(posCode);
			out.write(posIndex);
			offset += 2;
		}
		indexList = out.toByteArray();

		// 評価値を書き込み
		double value = Double.parseDouble(values[1].trim()) * 1000.0;
		this.value = (int)value;
	}
	
	/**
	 * @param input
	 * @return
	 * @throws IOException 
	 */
	public static Joseki load(InputStream input) throws IOException {
		Joseki joseki = new Joseki();
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		// 棋譜を追加
		while (true) {
			int data = input.read();
			if (data == -1) {
				return null;
			}
			if (data == SEPARATOR_BYTE) {
				break;
			}
			list.add(data);
		}
		byte[] indexList = new byte[list.size()];
		for (int i = 0; i < indexList.length; i++) {
			indexList[i] = (byte)(int)list.get(i);
		}
		joseki.indexList = indexList;
		
		// 評価値を読み込み
		byte[] bytes = new byte[4];
		input.read(bytes, 0, bytes.length);
		
		int value = ByteUtils.bytesToInt(bytes);
		joseki.value = value;
		
		return joseki;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @return the indexList
	 */
	public byte[] getIndexList() {
		return indexList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (byte index : indexList) {
			sb.append(Board.indexToCode(index));
		}
		sb.append(" ; ");
		sb.append(value);
		return sb.toString();
	}

}
