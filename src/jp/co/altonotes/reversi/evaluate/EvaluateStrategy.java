package jp.co.altonotes.reversi.evaluate;

import java.util.ArrayList;

import jp.co.altonotes.reversi.evaluate.evaluator.Evaluator;
import jp.co.altonotes.reversi.evaluate.evaluator.OpenValueEvaluator;
import jp.co.altonotes.reversi.evaluate.evaluator.PositionEvaluator;
import jp.co.altonotes.reversi.evaluate.evaluator.StoneCountEvaluator;
import jp.co.altonotes.reversi.model.Board;

/**
 * î’ñ Çï]âøÇ∑ÇÈÅB
 * ï€éùÇ∑ÇÈ Evaluator ÇÃï]âøÇÃçáåvÇ≈ï]âøÇ∑ÇÈÅB
 * @author Yamamoto Keita
 *
 */
public class EvaluateStrategy {
	
	ArrayList<Evaluator> evaluatorList = new ArrayList<Evaluator>();
	
	public EvaluateStrategy() {
		addEvaluator(new OpenValueEvaluator(), 12);
		addEvaluator(new PositionEvaluator(), 22);
		addEvaluator(new StoneCountEvaluator(), 11);
	}
	
	/**
	 * 
	 * @param board
	 * @return
	 */
	public int evaluate(Board board) {
		int value = 0;
		
		for (Evaluator evaluator : evaluatorList) {
			value += evaluator.getValue(board);
		}
		
		return value;
	}
	
	/**
	 * 
	 * @param eval
	 * @param power
	 */
	public void addEvaluator(Evaluator eval, int power) {
		eval.setPower(power);
		evaluatorList.add(eval);
	}

	/**
	 * @param power
	 * @param class1
	 */
	public void setPower(int power, Class<? extends Evaluator> class1) {
		for (Evaluator evaluator : evaluatorList) {
			if (class1.isInstance(evaluator)) {
				evaluator.setPower(power);
				break;
			}
		}
	}

	/**
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getEvaluator(Class<T> class1) {
		for (Evaluator evaluator : evaluatorList) {
			if (class1.isInstance(evaluator)) {
				return (T)evaluator;
			}
		}
		return null;
	}
}
