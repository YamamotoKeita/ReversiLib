package jp.co.altonotes.reversi.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;


/**
 * �f�[�^���o�́A�t�@�C�����쑍�����[�e�B���e�B�[�B
 * Commons IOUtils, FileUtils�ɂقړ����̋@�\������B
 *
 * @author Yamamoto Keita
 *
 */
public class IOUtils {

	/**
	 * �J�����g�f�B���N�g����File�C���X�^���X�Ƃ��Ď擾����B
	 *
	 * @return �J�����g�f�B���N�g��
	 */
	public static File getCurrentDirectory() {
		return new File(".").getAbsoluteFile().getParentFile();
	}

	/**
	 * �J�����g�f�B���N�g���̃p�X���擾����B
	 *
	 * @return �J�����g�f�B���N�g���̃p�X
	 */
	public static String currentPath() {
		return getCurrentDirectory().getAbsolutePath();
	}

	/**
	 * �t�@�C������o�C�i���f�[�^��ǂݎ��B
	 * #FileUtils.readFileToByteArray(file)
	 *
	 * @param file
	 * @return �t�@�C������ǂݎ�����o�C�i���f�[�^
	 * @throws IOException
	 */
	public static byte[] read(File file) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			byte[] buf = new byte[1024];
			int size = 0;
			in = new FileInputStream(file);
			while ((size = in.read(buf)) > 0) {
				baos.write(buf, 0, size);
			}
		} finally {
			close(in);
		}

		return baos.toByteArray();
	}

	/**
	 * �����̃p�X�ɂ�����t�@�C������o�C�i���f�[�^��ǂݎ��B
	 *
	 * @param path
	 * @return �t�@�C������ǂݎ�����o�C�i���f�[�^
	 * @throws IOException
	 */
	public static byte[] read(String path) throws IOException{
		return read(new File(path));
	}

	/**
	 * �t�@�C�����當�����ǂݎ��B
	 *
	 * @param file
	 * @param charset
	 * @return �t�@�C������ǂݎ����������
	 * @throws IOException
	 */
	public static String readString(File file, String charset) throws IOException {
		byte[] data = read(file);
		return new String(data, charset);
	}

	/**
	 * �����̃p�X�̃t�@�C�����當�����ǂݎ��B
	 *
	 * @param path
	 * @param charset
	 * @return �t�@�C������ǂݎ����������
	 * @throws IOException
	 */
	public static String readStringFromPath(String path, String charset) throws IOException {
		byte[] data = read(path);
		return new String(data, charset);
	}

	/**
	 * InputStream���當�����ǂݍ���
	 *
	 * @param in
	 * @param charset
	 * @return �ǂݍ��񂾕�����
	 * @throws IOException
	 */
	public static String readString(InputStream in, String charset) throws IOException {
		byte[] data = read(in);
		return new String(data, charset);
	}

	/**
	 * InputStream����ǂݎ���S�o�C�i���f�[�^��ǂݎ��A�ǂݎ���Close����B
	 *
	 * @param in
	 * @return InputStream����ǂݎ�����o�C�i���f�[�^
	 * @throws IOException
	 */
	public static byte[] read(InputStream in) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			byte[] buf = new byte[1024];
			int size = 0;
			while((size = in.read(buf)) > 0){
				baos.write(buf, 0, size);
			}
		} finally {
			close(in);
		}

		return baos.toByteArray();
	}

	/**
	 * �t�@�C������I�u�W�F�N�g��ǂݍ��ށB
	 *
	 * @param file
	 * @return �t�@�C������ǂݎ�����I�u�W�F�N�g
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(File file) throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		Object obj = null;
		try {
			in = new ObjectInputStream(new FileInputStream(file));
			obj = in.readObject();
		} finally {
			close(in);
		}
		return obj;
	}

	/**
	 * �t�@�C������v���p�e�B��ǂݍ��ށB
	 *
	 * @param file
	 * @return �t�@�C������ǂݎ�����v���p�e�B
	 * @throws IOException
	 */
	public static Properties readProperties(File file) throws IOException {
		Properties prop = new Properties();

		InputStream in = null;

		try {
			in = new FileInputStream(file);
			prop.load(in);
		} finally {
			close(in);
		}

		return prop;
	}

	/**
	 * �t�@�C���Ƀo�C�i���f�[�^���������ށB
	 *
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	public static void write(File file, byte[] data) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);

		try {
			fout.write(data, 0, data.length);
		} finally {
			close(fout);
		}
	}

	/**
	 * �t�@�C���̏I�[�Ƀf�[�^��ǋL����B
	 * @param file
	 * @param str
	 * @param charset 
	 * @throws IOException
	 */
	public static void append(File file, String str, String charset) throws IOException {
		FileOutputStream fo = null;

		try {
			fo = new FileOutputStream(file, true); //�ǋL�t���O true �ō쐬
			fo.write(str.getBytes(charset));
		} finally {
			close(fo);
		}
	}

	/**
	 * �t�@�C���Ƀo�C�i���f�[�^���������ށB
	 *
	 * @param path
	 * @param data 
	 * @throws IOException
	 */
	public static void write(String path, byte[] data) throws IOException {
		write(new File(path), data);
	}

	/**
	 * �t�@�C���ɕ�����f�[�^���������ށB
	 *
	 * @param file
	 * @param str
	 * @param charset
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writeString(File file, String str, String charset) throws IOException {
		write(file, str.getBytes(charset));
	}

	/**
	 * �t�@�C���ɃI�u�W�F�N�g���������ށB
	 *
	 * @param file
	 * @param obj
	 * @throws IOException
	 */
	public static void writeObject(File file, Object obj) throws IOException {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(obj);
		} finally {
			close(out);
		}
	}

	/**
	 * OutputStream�Ƀf�[�^���������݁A�������݌�Close����B
	 *
	 * @param out
	 * @param data
	 * @throws IOException
	 */
	public static void write(OutputStream out, byte[] data) throws IOException{
		try {
			out.write(data);
		} finally {
			close(out);
		}
	}

	/**
	 * ���\�[�X��byte[]�Ŏ擾����B
	 *
	 * @param klass
	 * @param path
	 * @return �ǂݍ��񂾃��\�[�X�̃o�C�i���f�[�^
	 * @throws IOException
	 */
	public static byte[] loadResource(Class<?> klass, String path) throws IOException{
		InputStream in = null;
		byte[] data = null;
		in = klass.getResourceAsStream(path);

		if (in == null) {
			URL url = null;
			try {
				url = klass.getResource(path);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (url != null) {
				throw new FileNotFoundException(url + " ��������܂���B");
			} else {
				throw new FileNotFoundException(path + " ��������܂���B");
			}
		}

		data = read(in);
		return data;
	}

	/**
	 * ���\�[�X��byte[]�Ŏ擾����B
	 *
	 * @param loader
	 * @param path
	 * @return �ǂݍ��񂾃��\�[�X�̃o�C�i���f�[�^
	 * @throws IOException
	 */
	public static byte[] loadResource(ClassLoader loader, String path) throws IOException{
		InputStream in = null;
		byte[] data = null;
		in = loader.getResourceAsStream(path);

		if (in == null) {
			URL url = null;
			try {
				url = loader.getResource(path);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (url != null) {
				throw new FileNotFoundException(url + " ��������܂���B");
			} else {
				throw new FileNotFoundException(path + " ��������܂���B");
			}
		}

		data = read(in);
		return data;
	}

	/**
	 * �����̃p�X���\�����\�[�X�Ƀf�[�^����������
	 *
	 * @param klass
	 * @param path
	 * @param data
	 * @throws IOException
	 */
	public static void writeResource(Class<?> klass, String path, byte[] data) throws IOException {
		File file = new File(klass.getResource(path).getPath());
		write(file, data);
	}

	/**
	 * InputStream��close����B
	 * ������null�̏ꍇ�������Ȃ��B
	 * IOException���������Ă�Exception���X���[���Ȃ��B
	 *
	 * @param in
	 */
	public static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * OutputStream��close����B
	 * ������null�̏ꍇ�������Ȃ��B
	 * IOException���������Ă�Exception���X���[���Ȃ��B
	 *
	 * @param out
	 */
	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Writer���N���[�Y����B
	 * ������null�̏ꍇ�������Ȃ��B
	 * IOException���������Ă�Exception���X���[���Ȃ��B
	 *
	 * @param writer
	 */
	public static void close(Writer writer) {
		if (writer == null) {
			return;
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reader��close����B
	 * ������null�̏ꍇ�������Ȃ��B
	 * IOException���������Ă�Exception���X���[���Ȃ��B
	 *
	 * @param reader
	 */
	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {}
		}
	}
}
