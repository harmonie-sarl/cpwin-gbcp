package fr.symphonie.budget.core.model.edition;

import java.util.ArrayList;
import java.util.List;

import fr.symphonie.budget.core.model.edition.util.Operation;


public class Tab5 {
//	 private static final Logger logger = LoggerFactory.getLogger(Tab5.class);
	private List<Operation> operationsBudg;
	private List<Operation> operationsnoBudg;

	public Tab5(Edition e) {
		super();
		this.operationsBudg=new ArrayList<Operation>();
		this.operationsnoBudg=new ArrayList<Operation>();
	}

	public List<Operation> getOperationsBudg() {
	
		return operationsBudg;
	}

	public void setOperationsBudg(List<Operation> operations) {
		this.operationsBudg = operations;
	}

	public List<Operation> getOperationsnoBudg() {
		return operationsnoBudg;
	}

	public void setOperationsnoBudg(List<Operation> operationsnoBudg) {
		this.operationsnoBudg = operationsnoBudg;
	}
	
	public double getTotDebitBudg(){
		double result=0d;
		double mnt=0;
		for (Operation operation : getOperationsBudg()) {
			mnt=operation.getDebitDouble();
//			logger.debug("getTotDebitBudg: mnt="+mnt);
			result+=mnt;
		}
		return result;		
	}
	
	public double getTotCreditBudg(){
		double result=0d;
		double mnt=0;
		for (Operation operation : getOperationsBudg()) {
			mnt=operation.getCreditDouble();
//			logger.debug("getTotCreditBudg: mnt="+mnt);
			result+=mnt;
		}
		return result;		
	}
	
	public double getTotDebitNoBudg(){
		double result=0d;
		for (Operation operation : getOperationsnoBudg()) {
			result+=operation.getDebitDouble();
		}
		return result;		
	}
	
	public double getTotCreditNoBudg(){
		double result=0d;
		for (Operation operation : getOperationsnoBudg()) {
			result+=operation.getCreditDouble();
		}
		return result;			
	}

}
