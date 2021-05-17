package tools;

import cartago.Artifact;
import cartago.OPERATION;

public class ElectronicMachine extends Artifact{
	
	@OPERATION
	public void init() {
		defineObsProperty("task", 0);
	}
	
	@OPERATION
	public void transfer(String teste) {
		System.out.println("Apenas testando..");
	}
	
	
	@OPERATION
	public void transferBook() {
		System.out.println("Apenas testando...  transferBook");
	}
	
	@OPERATION
	public void receiveAnything() {
		System.out.println("Apenas testando...  receiveAnything");
	}
	
	@OPERATION
	public void putBookOnShelf() {
		System.out.println("Apenas testando...  putBookOnShelf");
	}
	
	@OPERATION
	public void signLoan() {
		System.out.println("Apenas testando...  signLoan");
	}
	


}
