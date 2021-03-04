package jp.co.altonotes.reversi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import jp.co.altonotes.reversi.joseki.Joseki;
import jp.co.altonotes.reversi.joseki.JosekiNode;
import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.util.ByteUtils;
import jp.co.altonotes.reversi.util.LineIterator;

/**
 * @author Yamamoto Keita
 *
 */
public class JosekiDataConverter {
	
//	private final static String DIR_PATH = "D:\\04 ITプロジェクト\\java\\eclipse3.7\\workspace\\ReversiLib\\data\\";
	private final static String DIR_PATH = "C:\\DevTools\\eclipse-3.7-juno-64\\workspace\\ReversiLib\\data\\";
	
	private final static String BREAK = "\n";
	private final static String SEPARATOR = ";";

	static Runtime runtime = Runtime.getRuntime();
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		JosekiDataConverter proc = new JosekiDataConverter();
		
		proc.printData("small");
	}
	
	/**
	 * 
	 * @param srcFile
	 * @return
	 * @throws IOException
	 */
	protected JosekiNode createNode(String srcFile) throws IOException {
		JosekiNode rootNode = new JosekiNode();
		LineIterator iterator = null;
		try {
			iterator = LineIterator.createByFilePath(DIR_PATH + srcFile, "UTF-8");
			while (iterator.hasNext()) {
				Joseki joseki = new Joseki(iterator.nextLine());
				rootNode.addJoseki(joseki);
			}
		} finally {
			if (iterator != null) {iterator.close();}
		}
		
		return rootNode;
	}

	/**
	 * テキストの定石データを、無駄の無いデータフォーマットに変換する
	 * @throws IOException
	 */
	protected void format(String srcFile) throws IOException {
		File file = new File(DIR_PATH + srcFile);
		LineIterator iterator = null;
		FileOutputStream output = null;
		try {
			iterator = LineIterator.createByFilePath(file.getAbsolutePath(), "UTF-8");
			output = new FileOutputStream(new File(DIR_PATH + file.getName().replace(".txt", "")));

			while (iterator.hasNext()) {
				String line = iterator.nextLine();
				byte[] lineData = createLineData(line);
				output.write(lineData);
			}
			
		} finally {
			if (iterator != null) {iterator.close();}
			if (output != null) {output.close();}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	protected void printData(String file) throws IOException {
		FileInputStream input = null;
		
		try {
			input = new FileInputStream(new File(DIR_PATH + file));
			String line = null;
			int count = 0;
			while ((line = readLine(input)) != null) {
				if (count < 1000) {
					System.out.println(line);
				}
				count ++;
			}
			System.out.println(count + "行");
		} finally {
			if (input != null) {input.close();}
		}
	}
	
	private String readLine(FileInputStream input) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		// 棋譜を追加
		while (true) {
			int data = input.read();
			if (data == -1) {
				return null;
			}
			if (data == Joseki.SEPARATOR_BYTE) {
				break;
			}
			String posCode = Board.indexToCode(data);
			sb.append(posCode);
		}
		
		sb.append(" ; ");
		
		// 評価値を読み込み
		byte[] bytes = new byte[4];
		input.read(bytes, 0, bytes.length);
		
		int iVal = ByteUtils.bytesToInt(bytes);
		double value = iVal / 1000.0;
		sb.append(String.valueOf(value));
		
		return sb.toString();
	}
	
	/**
	 * テキストの定石データ１行を、無駄の無いデータフォーマットに変換する
	 * @param line
	 * @return
	 * @throws IOException 
	 */
	private byte[] createLineData(String line) throws IOException {
		String[] split = line.split(SEPARATOR);
		String kifu = split[0].trim();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		int offset = 0;
		// 棋譜を書き出し
		while (offset < kifu.length()) {
			String posCode = kifu.substring(offset, offset + 2);
			int posIndex = Board.codeToIndex(posCode);
			out.write(posIndex);
			offset += 2;
		}
		
		// セパレーター書き込み
		out.write(Joseki.SEPARATOR_BYTE);

		// 評価値を書き込み
		double value = Double.parseDouble(split[1].trim()) * 1000.0;
		int iValue = (int)value;
		out.write(ByteUtils.intToBytes(iValue));
		
		return out.toByteArray();
	}
	
	/**
	 * 指定の手数より長い棋譜をカットする
	 * @param srcFile
	 * @param dstFile
	 * @param limit
	 * @throws IOException
	 */
	protected void reduce(String srcFile, String dstFile, int limit) throws IOException {
		LineIterator iterator = null;
		FileWriter fw = null;
		
		limit *= 2;
		
		try {
			iterator = LineIterator.createByFilePath(DIR_PATH + srcFile, "UTF-8");
			fw = new FileWriter(new File(DIR_PATH + dstFile));
			while (iterator.hasNext()) {
				String line = iterator.nextLine();
				String kifu = line.split(SEPARATOR)[0].trim();
				
				if (kifu.length() < limit) {
					fw.write(line + BREAK);
				}
			}
		} finally {
			if (fw != null) {fw.close();}
			if (iterator != null) {iterator.close();}
		}
	}
	

}
