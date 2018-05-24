package myapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Solve {

	private Map<Integer, Clue> clues;
	private Matrix matrix;

	public void init(String matrixString) {
		this.matrix = Matrix.generateMatrix(matrixString);
		this.clues = new HashMap<Integer, Clue>();
		for (int number = 1; number < 10; number++) {
			clues.put(new Integer(number), new Clue(matrix,new Integer(number)));
		}
	}

	public void phase1() {
		boolean redo=false;
		for (int number = 1; number < 10; number++) {
			Clue clue = clues.get(number);
			if(clue.getColumnCandicates().size()==1){//Implicitly clue.getRowCandicates()==1
				int row=clue.getRowCandicates().get(0);
				int column=clue.getColumnCandicates().get(0);
				matrix.found(row, column, number);
				redo=true;
			}
		
			for (int block = 0; block < 9; block++) {
				if(clue.getBlockCandicate(block).size()==1){
					int row=(block/3)*3+clue.getBlockCandicate(block).get(0)/3;
					int column=(block%3)*3+clue.getBlockCandicate(block).get(0)%3;
					matrix.found(row, column, number);
					redo=true;
				}
			}
		}
		
		if(redo){
			for (int number = 1; number < 10; number++) {
				Clue clue=clues.get(new Integer(number));
				clue.refresh();
			}
			phase1();
		}
	}

	public void phase2() {
		recurse();
	}

	private void recurse() {
		Cell nextCell = chooseNextCell();
		if (nextCell != null) {
			int no = nextCell.getNo();
			for (Iterator<Integer> itr = nextCell.getCandicates().iterator(); itr.hasNext();) {
				int value = itr.next();
				List<ChangeLog> logs = new ArrayList<ChangeLog>();
				int i = no / 9, j = no % 9;
				if (justTry(i, j, value, logs)) {
					if (matrix.solved()) {
						matrix.print();
					} else {
						recurse();
					}
				}
				recover(i,j,logs);
			}
		}
	}

	public boolean justTry(int row, int column, Integer candicateValue, List<ChangeLog> logs) {
		Cell cell = matrix.getCell(row, column);
		if(!cell.hasCandicate(candicateValue))
			return false;
		cell.setValue(candicateValue);
		if (cell.removeCandicate(candicateValue)) {
			ChangeLog log = new ChangeLog(row, column, candicateValue);
			logs.add(log);
		}

		try {
			for (int k = 0; k < 9; k++) {
				Cell cellInSameRow = matrix.getCell(row, k);
				if (cellInSameRow.removeCandicate(candicateValue)) {
					ChangeLog log = new ChangeLog(row, k, candicateValue);
					logs.add(log);
				}
				if (!cellInSameRow.isFilled() && cellInSameRow.getCandicates().isEmpty()) {
					throw new InvalidValueException(row, k, candicateValue);
				}

				Cell cellInSameColumn = matrix.getCell(k, column);
				if (cellInSameColumn.removeCandicate(candicateValue)) {
					ChangeLog log = new ChangeLog(k, column, candicateValue);
					logs.add(log);
				}

				if (!cellInSameColumn.isFilled() && cellInSameColumn.getCandicates().isEmpty()) {
					throw new InvalidValueException(k, column, candicateValue);
				}

			}

			int origin_x = row / 3 * 3, origin_y = column / 3 * 3;
			for (int offset_x = 0; offset_x < 3; offset_x++) {
				for (int offset_y = 0; offset_y < 3; offset_y++) {
					Cell cellInSameBlock = matrix.getCell(origin_x + offset_x, origin_y + offset_y);
					if (cellInSameBlock.removeCandicate(candicateValue)) {
						ChangeLog log = new ChangeLog(origin_x + offset_x, origin_y + offset_y, candicateValue);
						logs.add(log);
					}
					if (!cellInSameBlock.isFilled() && cellInSameBlock.getCandicates().isEmpty()) {
						throw new InvalidValueException(origin_x + offset_x, origin_y + offset_y, candicateValue);
					}
				}
			}
		} catch (InvalidValueException e) {
			return false;
		}
		return true;
	}

	public void recover(int row,int column,List<ChangeLog> logs) {
		Cell cell = matrix.getCell(row, column);
		cell.setValue(0);
		for (Iterator<ChangeLog> itr = logs.iterator(); itr.hasNext();) {
			ChangeLog log = itr.next();
			cell = matrix.getCell(log.row, log.column);
			cell.addCandicate(log.removedCandicate);
		}
	}

	private Cell chooseNextCell() {
		int minChoice = 10;
		Cell minCell = null;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				Cell cell = matrix.getCell(i, j);
				if(cell.isFilled())
					continue;
				int choice = cell.getCandicates().size();
				if (choice < minChoice) {
					minChoice = choice;
					minCell = cell;
				}
			}

		return minCell;
	}

	public void printMatrix() {
		matrix.print();
	}

	public static void main(String[] args) {
		Solve solve = new Solve();
		solve.init("010000024000010000000000081307000100100800500000200000021400060500170300000000010");
		System.out.println("------ORGINAL---------");
		solve.printMatrix();
		solve.phase1();
		System.out.println("----AFTER PHASE 1-----");
		solve.printMatrix();
		System.out.println("----AFTER PHASE 2-----");
		solve.phase2();

	}
	
}
