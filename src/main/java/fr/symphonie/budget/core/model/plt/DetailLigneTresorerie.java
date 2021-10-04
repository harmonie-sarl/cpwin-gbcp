package fr.symphonie.budget.core.model.plt;

import java.math.BigDecimal;

import fr.symphonie.cpwin.model.Visa;
import fr.symphonie.cpwin.model.cpt.DebitCreditEnum;

public class DetailLigneTresorerie extends GenericLigneTresorerie {
	//private static final Logger logger = LoggerFactory.getLogger(DetailLigneTresorerie.class);
	protected Integer exercice;
	protected Integer numero;	
	private Integer periode;
	private String etat;
	private Visa realise;
	private Visa previsionnel;
	private BigDecimal montant13;
	
	private BigDecimal creditOuvert;
	private BigDecimal montantInitiale;
	private BigDecimal projetDM;
	
	public DetailLigneTresorerie(LigneTresorerie ligne) {
		this();
		setLigne(ligne);
		setNumero(ligne.getNumero());
	}
	
	public DetailLigneTresorerie() {
		super();
		this.creditOuvert=new BigDecimal(0);
		this.montant13=BigDecimal.ZERO;
		this.montantInitiale=BigDecimal.ZERO;
		this.projetDM=BigDecimal.ZERO;
	}
	public DetailLigneTresorerie(GenericLigneTresorerie generic) {
		
		this(generic.getLigne());
		for(int i=1;i<=12;i++){
			this.setMontant(i, generic.getMontant(i));
		}
		this.setDetailList(generic.getDetailList());
		
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
@Override
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public BigDecimal getMontant13() {
		return montant13;
	}

	public void setMontant13(BigDecimal montant13) {
		this.montant13 = montant13;
	}
	public Integer getPeriode() {
		return periode;
	}
	public void setPeriode(Integer periode) {
		this.periode = periode;
	}

	public EtatPeriodeEnum getEtat() {
		return EtatPeriodeEnum.parse(etat);
	}
	public void setEtat(EtatPeriodeEnum etat) {
		this.etat = etat.getValue();
	}
	public Visa getRealise() {
		return realise;
	}
	public void setRealise(Visa realise) {
		this.realise = realise;
	}
	public Visa getPrevisionnel() {
		return previsionnel;
	}
	public void setPrevisionnel(Visa previsionnel) {
		this.previsionnel = previsionnel;
	}

	public BigDecimal getCreditOuvert() {
		return creditOuvert;
	}
	public void setCreditOuvert(BigDecimal creditOuvert) {
		this.creditOuvert = creditOuvert;
	}
	public BigDecimal getMontantInitiale() {
		return montantInitiale;
	}
	public void setMontantInitiale(BigDecimal montantInitiale) {
		this.montantInitiale = montantInitiale;
	}
	public BigDecimal getProjetDM() {
		return projetDM;
	}
	public void setProjetDM(BigDecimal projetDM) {
		this.projetDM = projetDM;
	}
	@Override
	public BigDecimal getEcart() {
		BigDecimal totAnnuelle=getTotaleVariationAnnuelle();
		BigDecimal co=getCreditOuvert();
		//logger.debug("getEcart: CO={}",getCreditOuvert());
		BigDecimal ecart=co.subtract(totAnnuelle).setScale(2, BigDecimal.ROUND_HALF_UP);
		//logger.debug("getEcart: ecart={}=CO-TotaleVariationAnnuelle={} - {}",ecart,co,totAnnuelle);
		return ecart;
	}

	public void calculateRealisation() {
		Integer periodeRealisation=getPeriode();
		double montantRealise=getMontantRealise();
		setMontant(periodeRealisation, new BigDecimal(montantRealise));
	
		//logger.debug("calculateRealisation: noLigne={} / periode={} : Realisé={} }",getNumero(),periodeRealisation,montantRealise);
	}
	
	private double getMontantRealise(){
		double result=0;
		for(Ecriture ecr:getEcritureList()){
			if((ecr.getNumeroLigne1()!=null)&&(ecr.getNumeroLigne1()==getNumero()))
				result+=ecr.getMontantLigne1();
			else if((ecr.getNumeroLigne2()!=null)&&(ecr.getNumeroLigne2()==getNumero()))
				result+=ecr.getMontantLigne2();
		}
		//logger.debug("getMontantRealise ligne {} -> {}",getNumero(),result);
		return result;
	}

	
	public boolean ecritureEnabled(int numMois)
	{
		return (!isGlobal())&&(getPeriode()==numMois)&&(getMontant(numMois).doubleValue()!=0);
	}

	public boolean isEcartNull(){
		boolean result=getEcart().compareTo(BigDecimal.ZERO)==0;
		//logger.debug("public boolean isEcartNull(): "+result);
		return result;
		
	}

	public boolean isSameSens(DebitCreditEnum sens) {		
		return getLigne().isSameSens(sens);
	}

	@Override
	public DetailLigneTresorerie clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		DetailLigneTresorerie dlt= (DetailLigneTresorerie) super.clone();
		
		return dlt;
	}
	
}
