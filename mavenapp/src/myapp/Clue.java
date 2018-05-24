package myapp;

import java.util.ArrayList;
import java.util.List;

public class Clue {
	private Matrix matrix;
	private Integer number;
	private List<Integer> rowCandicates;
	private List<Integer> columnCandicates;
	private List<Integer>[] blockCandicates;

	public Clue(Matrix matrix, Integer value) {
		this.matrix = matrix;
		this.number = value;
		this.rowCandicates = new ArrayList<Integer>();
		this.columnCandicates = new ArrayList<Integer>();
		this.blockCandicates = new List[9];
		for (int i = 0; i < 9; i++) {
			this.rowCandicates.add(new Integer(i));
			this.columnCandicates.add(new Integer(i));
			blockCandicates[i] = new ArrayList<Integer>();
			for (int j = 0; j < 9; j++){
				blockCandicates[i].add(new Integer(j));
			}
		}
		refresh();
	}
	
	public void refresh(){
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Cell cell = matrix.getCell(i, j);
				if (cell.isFilled()) {
					if (cell.getValue().equals(number)) {
						eliminateRow(i);
						eliminateColumn(j);
						elimitedBlockCandicate(i, j);
					} else{
						int blockId=matrix.getBlockNumber(i, j); //(i / 3) * 3 + j / 3
						int cellIdInBlock=(i%3)*3+(j%3);
						blockCandicates[blockId].remove(new Integer(cellIdInBlock));
					}

				}
			}
		}
		
	}
	
	public List<Integer> getBlockCandicate(Integer block) {
		return blockCandicates[block];
	}

	private void elimitedBlockCandicate(int row, int column) {
		for (int blockRow = 0; blockRow < 3; blockRow++) {
			for (int blockColumn = 0; blockColumn < 3; blockColumn++) {
				if ((blockRow * 3 <= row && row < blockRow*3 + 3)
						&& (blockColumn * 3 <= column && column < blockColumn*3 + 3)) {
					int blockId = blockRow * 3 + blockColumn;
					for (int k = 0; k < 9; k++)
						blockCandicates[blockId].clear();
					;
				} else {
					if (blockRow * 3 <= row && row < blockRow*3 + 3) {
						int blockId = blockRow * 3 + blockColumn;
						for (int k = 0; k < 3; k++) {
							int cellIdInblock = (row % 3) * 3 + k;
							blockCandicates[blockId].remove(new Integer(cellIdInblock));
						}
					}

					if (blockColumn * 3 <= column && column < blockColumn*3 + 3) {
						int blockId = blockRow * 3 + blockColumn;
						for (int k = 0; k < 3; k++) {
							int cellIdInblock = column % 3 + 3 * k;
							blockCandicates[blockId].remove(new Integer(cellIdInblock));
						}
					}

				}

			}
		}

	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer value) {
		this.number = value;
	}

	public List<Integer> getRowCandicates() {
		return rowCandicates;
	}

	public void eliminateRow(Integer row) {
		this.rowCandicates.remove(row);
	}

	public List<Integer> getColumnCandicates() {
		return columnCandicates;
	}

	public void eliminateColumn(Integer column) {
		this.columnCandicates.remove(column);
	}
}
