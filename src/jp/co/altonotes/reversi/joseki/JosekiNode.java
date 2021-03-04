package jp.co.altonotes.reversi.joseki;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import jp.co.altonotes.reversi.model.Board;
import jp.co.altonotes.reversi.util.LineIterator;

/**
 * @author Yamamoto Keita
 */
public class JosekiNode implements Serializable {

	private static final long serialVersionUID = 8108211304899067617L;
	
	private byte index;

	private Object param;
	
	public JosekiNode() {
	}
	
	/**
	 * テキストファイルからルートノードを作成する
	 * @param srcPath
	 * @return
	 * @throws IOException
	 */
	public static JosekiNode createByText(String srcPath) throws IOException {
		JosekiNode rootNode = new JosekiNode();
		LineIterator iterator = null;
		try {
			iterator = LineIterator.createByFilePath(srcPath, "UTF-8");
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
	 * 独自フォーマットのデータからルートノードを作成する
	 * @param input
	 * @throws IOException
	 */
	public static JosekiNode createByData(String srcPath) throws IOException {
		File file = new File(srcPath);
		FileInputStream fileInput = null;
		
		try {
			fileInput = new FileInputStream(file);
			return createByInput(fileInput);
		} finally {
			if (fileInput != null) {fileInput.close();}
		}
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static JosekiNode createByInput(InputStream input) throws IOException {
		JosekiNode rootNode = new JosekiNode();

		// パフォーマンスを考慮してバッファする
		if (!(input instanceof BufferedInputStream)) {
			input = new BufferedInputStream(input);
		}
		
		try {
			Joseki joseki = null;
			while ((joseki = Joseki.load(input)) != null) {
				rootNode.addLinkLine(joseki, 0);
			}
		} finally {
			if (input != null) {input.close();}
		}
		return rootNode;
	}

	/**
	 * このノードから始まる定石を追加する
	 * @param joseki
	 */
	public void addJoseki(Joseki joseki) {
		addLinkLine(joseki, 0);
	}

	/**
	 * 定石からノードチェインを作成する
	 * @param joseki
	 */
	private void addLinkLine(Joseki joseki, int depth) {
		
		byte[] indexList = joseki.getIndexList();
		this.index = indexList[depth];

		// 終端の場合
		if (depth == indexList.length - 1) {
			param = joseki.getValue();
			return;
		}
		
		JosekiNode nextNode = null;
		byte nextIndex = indexList[depth + 1];
		if (this.param != null) {
			JosekiNode[] childList = (JosekiNode[])this.param;
			for (JosekiNode child : childList) {
				if (child.getIndex() == nextIndex) {
					nextNode = child;
					break;
				}
			}
		}
		
		if (nextNode == null) {
			nextNode = new JosekiNode();
			addChild(nextNode);
		}
		
		nextNode.addLinkLine(joseki, depth + 1);
	}

	/**
	 * 子ノードを追加する
	 * @param nextNode
	 */
	private void addChild(JosekiNode nextNode) {
		if (param == null) {
			param = new JosekiNode[]{nextNode};
		} else {
			JosekiNode[] childList = (JosekiNode[])param;
			JosekiNode[] newList = new JosekiNode[childList.length + 1];
			System.arraycopy(childList, 0, newList, 0, childList.length);
			newList[newList.length - 1] = nextNode;
			param = newList;
		}
	}
	
	/**
	 * 末端ノードか判定する
	 * @return 末端ノードの場合 true
	 */
	public boolean isLeafNode() {
		return param instanceof Integer;
	}

	/**
	 * @return このノード配下にある末端ノードの数
	 */
	public int leafCount() {
		if (param != null && param instanceof Integer) {
			return 1;
		} else {
			int count = 0;
			if (param != null) {
				JosekiNode[] childList = (JosekiNode[])param;
				for (JosekiNode child : childList) {
					count += child.leafCount();
				}
			}
			return count;
		}
	}
	
	/**
	 * @return 子ノードの配列
	 */
	public JosekiNode[] getChildList() {
		if (param != null && !(param instanceof Integer)) {
			return (JosekiNode[])param;
		} else {
			return null;
		}
	}
	
	/**
	 * @return the index
	 */
	public byte getIndex() {
		return index;
	}
	
	/**
	 * @return
	 */
	public int getValue() {
		return (Integer)param;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(Board.indexToCode(index));
		
		if (param != null && param instanceof Integer) {
			sb.append(" ; ");
			sb.append(String.valueOf(param));
		}
		
		return sb.toString();
	}
	
}
