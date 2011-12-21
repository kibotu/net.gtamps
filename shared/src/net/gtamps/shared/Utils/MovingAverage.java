package net.gtamps.shared.Utils;


public interface MovingAverage<T extends Number> {
	public void addValue(T value);
	public T getAverage();
}
