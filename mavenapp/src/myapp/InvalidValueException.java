package myapp;

public class InvalidValueException extends Exception {
	private int row;
	private int column;
	private int value;

	public InvalidValueException(int i, int j, int value) {
		this.row = i;
		this.column = j;
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public int getValue() {
		return value;
	}

}
