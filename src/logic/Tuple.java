package logic;

public class Tuple<T> {
	private T x;
	private T y;
	public Tuple(T a, T b){
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
