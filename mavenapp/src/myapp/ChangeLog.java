package myapp;

public class ChangeLog {
	public int row;
	public int column;
	public int removedCandicate;

	public ChangeLog(int i, int j, int value) {
		row = i;
		column = j;
		removedCandicate = value;
	}
}
