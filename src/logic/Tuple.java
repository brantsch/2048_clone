package logic;

/**
 * Store 2-tuples of elements of type T.
 * @author Peter Brantsch
 * 
 * @param <T>
 *            The type the elements of the tuple should be of.
 */
public class Tuple<T> {
	private T x;
	private T y;

	/**
	 * Create a new tuple with the elements a and b.
	 */
	public Tuple(T a, T b) {
		x = a;
		y = b;
	}

	public T getX() {
		return x;
	}

	public void setX(T t) {
		x = t;
	}

	public T getY() {
		return y;
	}

	public void setY(T t) {
		y = t;
	}
}
