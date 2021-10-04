package fr.symphonie.tools.meta4dai.model;

public class InternalEngagement {
	private Integer exercice;
	private Integer numero;
	private Integer noLBI;
	private Integer noLMI;
	private Integer noLBC;
	
	public InternalEngagement() {
		super();
	}
	public InternalEngagement(PaymentItem payment,Integer noLbiStr) {
		this();
		this.exercice=payment.getExercice();
		this.numero=payment.getNoEI();
		this.noLBI=payment.getNoLBI();
		this.noLMI=payment.getNoLMI();
		this.noLBC=noLbiStr;
	}
	public Integer getExercice() {
		return exercice;
	}
	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getNoLBI() {
		return noLBI;
	}
	public void setNoLBI(Integer noLBI) {
		this.noLBI = noLBI;
	}
	public Integer getNoLMI() {
		return noLMI;
	}
	public void setNoLMI(Integer noLMI) {
		this.noLMI = noLMI;
	}
	public Integer getNoLBC() {
		return noLBC;
	}
	public void setNoLBC(Integer noLBC) {
		this.noLBC = noLBC;
	}

}
