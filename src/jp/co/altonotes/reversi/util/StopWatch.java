package jp.co.altonotes.reversi.util;

import java.util.ArrayList;

/**
 * ���Ԃ��v������B<br>
 * �ȈՃp�t�H�[�}���X�e�X�g�p�B
 *
 * <p>�g�p��</p>
 * <pre>
 * StopWatch watch = new StopWatch();
 * watch.start();
 * // ���炩�̏���
 * watch.stop();
 * watch.printLastTime();
 * </pre>
 *
 * @author Yamamoto Keita
 *
 */
public class StopWatch {

	private static final int UNDEFINED = -1;

	private ArrayList<Long> recordList = new ArrayList<Long>();
	private long startTime = UNDEFINED;
	private long endTime = UNDEFINED;

	/**
	 * ���Ԃ̌v�����J�n����B
	 */
	public void start() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * ���Ԃ̌v�����I������B
	 */
	public void stop() {
		if (startTime != UNDEFINED) {
			endTime = System.currentTimeMillis();
			recordList.add(endTime - startTime);
			startTime = UNDEFINED;
		}
	}

	/**
	 * �Ō�Ɍv���������Ԃ��~���b�Ŏ擾����B
	 *
	 * @return �Ō�Ɍv����������
	 */
	public long lastTime() {
		if (recordList.size() == 0) {
			throw new IllegalStateException("�v�����ꂽ���Ԃ��������܂���B");
		}
		return recordList.get(recordList.size()-1);
	}

	/**
	 * �v�������S�Ă̎��Ԃ��~���b�Ŏ擾����B
	 *
	 * @return �v�������S�Ă̎���
	 */
	public long[] records() {
		long[] res = new long[recordList.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = recordList.get(i);
		}
		return res;
	}

	/**
	 * �v�������S�Ă̎��Ԃ�System�ɏo�͂���
	 */
	public void printLapTime() {
		if (recordList.size() == 0) {
			throw new IllegalStateException("�v�����ꂽ���Ԃ��������܂���B");
		}
		long[] records = records();
		int maxFigure = String.valueOf(records.length).length();
		for (int i = 0; i < records.length; i++) {
			String num = String.valueOf(i);
			num = padLeft(num, maxFigure, '0');
			
			String time = String.valueOf(records[i]);
			time = padLeft(time, 10, ' ');
			System.out.println(num + ":" + time + "ms");
		}
	}

	private static String padLeft(String source, int size, char padding) {
		int srcLen = source.length();
		if (size < srcLen) {
			source = source.substring(srcLen - size, srcLen);
		} else if (srcLen < size) {
			StringBuilder sb = new StringBuilder(size);
			for (int j = 0; j < size - srcLen; j++) {
				sb.append(padding);
			}
			sb.append(source);
			source = sb.toString();
		}
		return source;
	}
	
	/**
	 * �Ō�Ɍv���������Ԃ�System�ɏo�͂���
	 */
	public void printLastTime() {
		if (recordList.size() == 0) {
			throw new IllegalStateException("�v�����ꂽ���Ԃ��������܂���B");
		}
		System.out.println(lastTime() + "ms");
	}

	/**
	 * �v���������Ԃ��N���A����
	 */
	public void reset() {
		recordList = new ArrayList<Long>();
	}
}
