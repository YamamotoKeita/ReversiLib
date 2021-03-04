package jp.co.altonotes.reversi.util;

/**
 * �o�C�i���f�[�^�Ɋւ��郆�[�e�B���e�B�[�B
 *
 * @author Yamamoto Keita
 *
 */
public class ByteUtils {
	
	/** 2��10��	= 1024*/
	public static long KILO = 1 << 10;
	/** 2��20��	= 1048576 */
	public static long MEGA = 1 << 20;
	/** 2��30��	*/
	public static long GIGA = 1 << 30;

	/**
	 * �o�C�g�T�C�Y���A�L���A���K�A�M�K�P�ʂ��̕�������쐬����B<br>
	 * ���̍ہA�����_�ȉ��̃T�C�Y�͎l�̌ܓ�����B
	 *
	 * @param size
	 * @return �P�ʂ��̃T�C�Y
	 */
	public static String formatSize(long size) {
		float fsize = size;
		String suffix = null;

		if (size < KILO) {
			suffix = "B";
		} else if (size < MEGA) {//K
			fsize /= KILO;
			suffix = "KB";
		} else if (size < GIGA) {//M
			fsize /= MEGA;
			suffix = "MB";
		} else {
			fsize /= GIGA;
			suffix = "GB";
		}

		return String.valueOf(Math.round(fsize)) + suffix;
	}

	/**
	 * ���g���G���f�B�A����int���o�C�g�z��ɂ���B
	 *
	 * @param val
	 * @return int��ϊ������o�C�g�z��
	 */
	public static byte[] intToBytes(int val) {
		byte[] bytes = new byte[4];

		for (int i = 0; i < bytes.length; i++) {
			long shift = i * 8;
			long mask = 0xFF << shift;
			bytes[i] = (byte) ((val & mask) >> shift);
		}

		return bytes;
	}
	
	/**
	 * ���g���G���f�B�A���̃o�C�g�z���int�ɂ���B
	 *
	 * @param bytes
	 * @return �o�C�g�z���ϊ�����int�l
	 */
	public static int bytesToInt(byte[] bytes) {
		int result = 0;

		for (int i = 0; i < bytes.length ; i++) {
			int shift = i * 8;
			int val = bytes[i] & 0xFF;
			result += val << shift;
		}

		return result;
	}
	
	/**
	 * ���g���G���f�B�A����long���o�C�g�z��ɂ���B
	 *
	 * @param l
	 * @return long��ϊ������o�C�g�z��
	 */
	public static byte[] longToBytes(long l) {
		byte[] bytes = new byte[8];

		for (int i = 0; i < bytes.length; i++) {
			long shift = i * 8;
			long mask = 0xFFL << shift;
			bytes[i] = (byte) ((l & mask) >> shift);
		}

		return bytes;
	}

	/**
	 * �o�C�g�z������g���G���f�B�A����long�ɂ���B
	 *
	 * @param bytes
	 * @return �o�C�g�z���ϊ�����long�l
	 */
	public static long bytesToLong(byte[] bytes) {
		long l = 0;

		for (int i = 0; i < bytes.length ; i++) {
			long shift = i * 8;
			long val = bytes[i] & 0xFF;
			l += val << shift;
		}

		return l;
	}

	/**
	 * �o�C�g�z����R�s�[����B
	 *
	 * @param src
	 * @param srcPos
	 * @param dest
	 */
	public static void copy(byte[] src, int srcPos, byte[] dest) {
		int length = dest.length;
		int srcLength = src.length - srcPos;
		if (srcLength < length) {
			length = srcLength;
		}
		System.arraycopy(src, srcPos, dest, 0, length);
	}

	/**
	 * 16�i��������ɕϊ�����B
	 *
	 * @param b
	 * @return ������byte�l��\��16�i��
	 */
	public static String toHexString(byte b) {
		int i = b & 0xFF;
		String str = Integer.toHexString(i).toUpperCase();
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * �J���}��؂��16�i��������ɕϊ�����B
	 *
	 * @param data
	 * @return ������byte�z���\��16�i��(�J���}��؂�)
	 */
	public static String toHexStringWithComma(byte[] data) {
		StringBuffer buf = new StringBuffer(data.length*2);

		for (int i = 0; i < data.length; i++) {
			if (i != 0) {
				buf.append(",");
			}
			buf.append(toHexString(data[i]));
		}
		return buf.toString();
	}

	/**
	 * 16�i��������ɕϊ�����B
	 *
	 * @param data
	 * @return ������byte�z���\��16�i��
	 */
	public static String toHexString(byte[] data) {
		StringBuffer buf = new StringBuffer(data.length*2);

		for (int i = 0; i < data.length; i++) {
			buf.append(toHexString(data[i]));
		}
		return buf.toString();
	}
	
	/**
	 * 2�i��������ɕϊ�����
	 * 
	 * @param b
	 * @return ������byte��\��2�i��������
	 */
	public static String toBinaryString(byte b) {
		int i = b & 0xFF;
		String str = Integer.toBinaryString(i);
		
		while (str.length() < 8) {
			str = "0" + str;
		}
		return str;
	}
	
	/**
	 * 2�i���������byte�ɕϊ�����
	 * 
	 * @param binary
	 * @return 2�i���������\��byte
	 */
	public static byte binaryToByte(String binary) {
		return (byte) (int) Integer.valueOf(binary, 2);
	}

	/**
	 * 16�i���̕�������o�C�g�z��ɕϊ�����B
	 * 
	 * @param hex 16�i���̕�����
	 * @return �o�C�g�z��
	 */
	public static byte[] hexToByteArray(String hex) {
		// �����񒷂�1/2�̒����̃o�C�g�z��𐶐��B
		byte[] bytes = new byte[hex.length() / 2];

		// �o�C�g�z��̗v�f�����A�������J��Ԃ��B
		for (int index = 0; index < bytes.length; index++) {
			// 16�i����������o�C�g�ɕϊ����Ĕz��Ɋi�[
			String str = hex.substring(index * 2, (index + 1) * 2);
			bytes[index] = (byte) Integer.parseInt(str, 16);
		}

		// �o�C�g�z���Ԃ��B
		return bytes;
	}
}
