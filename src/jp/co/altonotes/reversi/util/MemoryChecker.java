package jp.co.altonotes.reversi.util;

import java.lang.management.ManagementFactory;

/**
 * �q�[�v�i�������j�󋵂̊m�F���s���B
 *
 * @author Yamamoto Keita
 *
 */
public class MemoryChecker {

	/**
	 * Java VM���g�p�ł���ő僁�����ʂ�Ԃ��B
	 * -Xmx256M �Ȃǂ�VM�����Ɏw�肳���l�B
	 *
	 * @return VM���g�p�ł���ő僁������
	 */
	public static long maxHeap() {
		return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
	}

	/**
	 * Java VM���g�p�ł���ő僁�����ʂ����K�o�C�g�P�ʂŕԂ��B
	 * -Xmx256M �Ȃǂ�VM�����Ɏw�肳���l�B
	 *
	 * @return VM���g�p�ł���ő僁�����ʁi���K�o�C�g�P�ʁj
	 */
	public static int maxHeapAsMegaByte() {
		return (int) (maxHeap() >> 20);
	}

	/**
	 * Java VM���N������OS�ɗv�����郁�����ʂ�Ԃ��B
	 * -Xms128M �Ȃǂ�VM�����Ɏw�肳���l�B
	 *
	 * @return VM���N������OS�ɗv�����郁������
	 */
	public static long initialHeap() {
		return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit();
	}

	/**
	 * Java VM���N������OS�ɗv�����郁�����ʂ����K�o�C�g�P�ʂŕԂ��B
	 * -Xms128M �Ȃǂ�VM�����Ɏw�肳���l�B
	 *
	 * @return VM���N������OS�ɗv�����郁�����ʁi���K�o�C�g�P�ʁj
	 */
	public static int initialHeapAsMegaByte() {
		return (int) (initialHeap() >> 20);
	}

	/**
	 * ���ݎg�p���Ă��郁�����ʂ�Ԃ��B
	 *
	 * @return ���ݎg�p���Ă��郁������
	 */
	public static long usingHeap() {
		return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
	}

	/**
	 * ���ݎg�p���Ă��郁�����ʂ����K�o�C�g�̒P�ʂŕԂ��B
	 *
	 * @return ���ݎg�p���Ă��郁�����ʁi���K�o�C�g�P�ʁj
	 */
	public static int usingHeapAsMegaByte() {
		return (int) (usingHeap() >> 20);
	}

	/**
	 * ���ݎg�p���{�g�p�\��ς݂̃������ʂ�Ԃ��B
	 *
	 * @return ���ݎg�p���{�g�p�\��ς݂̃�������
	 */
	public static long committedHeap() {
		return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted();
	}

	/**
	 * ���ݎg�p���{�g�p�\��ς݂̃������ʂ����K�o�C�g�P�ʂŕԂ��B
	 *
	 * @return ���ݎg�p���{�g�p�\��ς݂̃������ʁi���K�o�C�g�P�ʁj
	 */
	public static int committedHeapAsMegaByte() {
		return (int) (committedHeap() >> 20);
	}
}
