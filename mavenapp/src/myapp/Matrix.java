package myapp;

public class Matrix {
	private Cell[][] cells;
	private int filledCount;

	public Matrix() {
		cells = new Cell[9][9];
		filledCount = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell cell = new Cell(this, new Integer(i * 9 + j), new Integer(0));
				for (int k = 0; k < 9; k++)
					cell.addCandicate(new Integer(k + 1));
				cells[i][j] = cell;
			}
		}

	}

	public Cell getCell(int row, int column) {
		return cells[row][column];
	}

	public int getBlockNumber(int i, int j) {
		return i / 3 * 3 + j / 3;
	}

	static Matrix generateMatrix(String str) {
		Matrix matrix = new Matrix();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int num = i * 9 + j;
				Integer value = Integer.valueOf(str.substring(num, num + 1));
				if (value.intValue() > 0) {
					matrix.found(i, j, value);
				}
			}
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell cell = matrix.getCell(i, j);
				if (!cell.isFilled()) {
					if (cell.getCandicates().size() == 1) {
						Integer value = cell.getCandicates().get(0);
						matrix.found(i, j, value);
					}
				}
			}
		}

		return matrix;
	}

	public void increaseFilledCount() {
		filledCount++;
	}

	public void decreaseFilledCount() {
		filledCount--;
	}

	public void found(int i, int j, Integer value) {
		Cell cell = getCell(i, j);
		cell.setValue(value);
		cell.removeCandicate(value);
		for (int k = 0; k < 9; k++) {
			Cell cellInSameRow = getCell(i, k);
			cellInSameRow.removeCandicate(value);
			Cell cellInSameColumn = getCell(k, j);
			cellInSameColumn.removeCandicate(value);
		}
		int x = i / 3 * 3, y = j / 3 * 3;
		for (int m = 0; m < 3; m++) {
			for (int n = 0; n < 3; n++) {
				Cell cellInSameBlock = getCell(x + m, y + n);
				cellInSameBlock.removeCandicate(value);
			}
		}
	}

	public boolean solved() {
		return (filledCount == 81);
	}

	public void print() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell cell = cells[i][j];
				System.out.print(cell.getValue() + " ");
			}
			System.out.println("");
		}
	}

}
