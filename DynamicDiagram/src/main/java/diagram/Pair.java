package diagram;

public class Pair<E,T> {
	E first;
	T second;
	public Pair(E f, T s) {
		this.first = f;
		this.second = s;
	}
	public E getFirst() {
		return first;
	}
	public void setFirst(E first) {
		this.first = first;
	}
	public T getSecond() {
		return second;
	}
	public void setSecond(T second) {
		this.second = second;
	}
}
