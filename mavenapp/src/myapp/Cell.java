package myapp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cell {
	private Matrix  schema;
	private Integer no;
	private Integer value;
	private List<Integer> candicates;
	
	public Cell(Matrix schema,Integer no,Integer value){
		this.schema=schema;
		this.no=no;
		this.value=value ;
		candicates=new CopyOnWriteArrayList<Integer>();
	}
	
	public Matrix getSchema() {
		return schema;
	}
	
	public Integer getNo() {
		return no;
	}


	public void setCandicates(List<Integer> candicates) {
		this.candicates = candicates;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		if(this.value==0 && value>0){
			schema.increaseFilledCount();
		}else if(this.value>0 && value==0){
			schema.decreaseFilledCount();
		}
		this.value = value;
	}
	
	public boolean isFilled() {
		return value>0;
	}

	public List<Integer> getCandicates() {
		return candicates;
	}
	public void addCandicate(Integer candicate) {
		candicates.add(candicate);
	}

	public boolean hasCandicate(Integer candicate){
		return candicates.contains(candicate);
	}
	
	public boolean removeCandicate(Integer candicate){
		return candicates.remove(candicate);
	}
}
