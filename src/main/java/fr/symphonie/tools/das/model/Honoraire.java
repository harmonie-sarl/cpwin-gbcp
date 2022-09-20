package fr.symphonie.tools.das.model;

import java.math.BigDecimal;

import org.apache.logging.log4j.util.Strings;

import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.common.util.Util;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.ConfigImport;
import fr.symphonie.impor.Importable;
import fr.symphonie.util.model.Trace;

public class Honoraire implements Comparable<Honoraire>,Importable {
	private Integer exercice;
	private String codeTiers;
	
	//Rémunération versée
	private BigDecimal honoraires;
	private BigDecimal commissions;
	private BigDecimal courtages;
	private BigDecimal ristournes;
	private BigDecimal jetonsPresence;
	private BigDecimal droitsAuteur;
	private BigDecimal droitsInventeur;
	private BigDecimal autresRemunerations;
	private BigDecimal indemnitesRemb;
	private BigDecimal avantagesEnNature;
	private BigDecimal retenueAlaSource;
	//nature des avantages en nature
	private String nourriture;
	private String logement;
	private String voiture;
	private String autres;
	private String outilsNTIC;
	//Modalités de prise en charge des indemnités et remboursements
	private String allocForfaitaire;
	private String remboursements;
	private String priseEnChargeDirecteEmployeur;
	//Taux retenue à la source
	private String tauxReduit;
	private String dispense;
	
	private BigDecimal tvaNetteDroitsAuteur;
	private Trace trace;
	/**
	 * Relationship
	 */
	
	private TiersDas beneficiaire;

	public Honoraire() {
		super();
		this.beneficiaire=new TiersDas();
	}

	public TiersDas getBeneficiaire() {
		if((beneficiaire!=null)&&(beneficiaire.getNumero()==null))
			beneficiaire.setNumero(getCodeTiers());
		return beneficiaire;
	}

	public Integer getExercice() {
		return exercice;
	}

	public void setExercice(Integer exercice) {
		this.exercice = exercice;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public void setBeneficiaire(TiersDas beneficiaire) {
		this.beneficiaire = beneficiaire;
	}

	public BigDecimal getHonoraires() {
		return honoraires;
	}

	public void setHonoraires(BigDecimal honoraires) {
		this.honoraires = honoraires;
	}

	public BigDecimal getCommissions() {
		return commissions;
	}

	public void setCommissions(BigDecimal commissions) {
		this.commissions = commissions;
	}

	public BigDecimal getCourtages() {
		return courtages;
	}

	public void setCourtages(BigDecimal courtages) {
		this.courtages = courtages;
	}

	public BigDecimal getRistournes() {
		return ristournes;
	}

	public void setRistournes(BigDecimal ristournes) {
		this.ristournes = ristournes;
	}

	public BigDecimal getJetonsPresence() {
		return jetonsPresence;
	}

	public void setJetonsPresence(BigDecimal jetonsPresence) {
		this.jetonsPresence = jetonsPresence;
	}

	public BigDecimal getDroitsAuteur() {
		return droitsAuteur;
	}

	public void setDroitsAuteur(BigDecimal droitsAuteur) {
		this.droitsAuteur = droitsAuteur;
	}

	public BigDecimal getDroitsInventeur() {
		return droitsInventeur;
	}

	public void setDroitsInventeur(BigDecimal droitsInventeur) {
		this.droitsInventeur = droitsInventeur;
	}

	public BigDecimal getAutresRemunerations() {
		return autresRemunerations;
	}

	public void setAutresRemunerations(BigDecimal autresRemunerations) {
		this.autresRemunerations = autresRemunerations;
	}

	public BigDecimal getIndemnitesRemb() {
		return indemnitesRemb;
	}

	public void setIndemnitesRemb(BigDecimal indemnitesRemb) {
		this.indemnitesRemb = indemnitesRemb;
	}

	public BigDecimal getAvantagesEnNature() {
		return avantagesEnNature;
	}

	public void setAvantagesEnNature(BigDecimal avantagesEnNature) {
		this.avantagesEnNature = avantagesEnNature;
	}

	public BigDecimal getRetenueAlaSource() {
		return retenueAlaSource;
	}

	public void setRetenueAlaSource(BigDecimal retenueAlaSource) {
		this.retenueAlaSource = retenueAlaSource;
	}

	public String getNourriture() {
		return nourriture;
	}

	public void setNourriture(String nourriture) {
		this.nourriture = nourriture;
	}

	public String getLogement() {
		return logement;
	}

	public void setLogement(String logement) {
		this.logement = logement;
	}

	public String getVoiture() {
		return voiture;
	}

	public void setVoiture(String voiture) {
		this.voiture = voiture;
	}

	public String getAutres() {
		return autres;
	}

	public void setAutres(String autres) {
		this.autres = autres;
	}

	public String getOutilsNTIC() {
		return outilsNTIC;
	}

	public void setOutilsNTIC(String outilsNTIC) {
		this.outilsNTIC = outilsNTIC;
	}

	public String getAllocForfaitaire() {
		return allocForfaitaire;
	}

	public void setAllocForfaitaire(String allocForfaitaire) {
		this.allocForfaitaire = allocForfaitaire;
	}

	public String getRemboursements() {
		return remboursements;
	}

	public void setRemboursements(String remboursements) {
		this.remboursements = remboursements;
	}

	public String getPriseEnChargeDirecteEmployeur() {
		return priseEnChargeDirecteEmployeur;
	}

	public void setPriseEnChargeDirecteEmployeur(String priseEnChargeDirecteEmployeur) {
		this.priseEnChargeDirecteEmployeur = priseEnChargeDirecteEmployeur;
	}

	public String getTauxReduit() {
		return tauxReduit;
	}

	public void setTauxReduit(String tauxReduit) {
		this.tauxReduit = tauxReduit;
	}

	public String getDispense() {
		return dispense;
	}

	public void setDispense(String dispense) {
		this.dispense = dispense;
	}

	public BigDecimal getTvaNetteDroitsAuteur() {
		return tvaNetteDroitsAuteur;
	}

	public void setTvaNetteDroitsAuteur(BigDecimal tvaNetteDroitsAuteur) {
		this.tvaNetteDroitsAuteur = tvaNetteDroitsAuteur;
	}
	
	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public void setValue(int index, String value) {
		HonoraireStruct struct=HonoraireStruct.parse(index);
		switch (struct) {
		case CODE_TIERS:
			this.getBeneficiaire().setNumero(value);
			this.setCodeTiers(value);
			break;
		case allocForfaitaire:
			setAllocForfaitaire(formatValue(value, struct));
			break;
		case autres:
			setAutres(formatValue(value, struct));
			break;
		case autresRemunerations:
			setAutresRemunerations(formatMontant(value));
			break;
		case avantagesEnNature:
			setAvantagesEnNature(formatMontant(value));
			break;
		case commissions:
			this.setCommissions(formatMontant(value));
			break;
		case courtages:
			this.setCourtages(formatMontant(value));
			break;
		case dispense:
			break;
		case droitsAuteur:
			this.setDroitsAuteur(formatMontant(value));
			break;
		case droitsInventeur:
			this.setDroitsInventeur(formatMontant(value));
			break;
		case honoraires:
			setHonoraires(formatMontant(value));
			break;
		case indemnitesRemb:
			setIndemnitesRemb(formatMontant(value));
			break;
		case jetonsPresence:
			setJetonsPresence(formatMontant(value));
			break;
		case logement:
			this.setLogement(formatValue(value,struct));
			break;
		case nourriture:
			setNourriture(formatValue(value,struct));
			break;
		case outilsNTIC:
			setOutilsNTIC(formatValue(value,struct));
			break;
		case priseEnChargeDirecteEmployeur:
			setPriseEnChargeDirecteEmployeur(formatValue(value,struct));
			break;
		case remboursements:
			setRemboursements(formatValue(value,struct));
			break;
		case retenueAlaSource:
			setRetenueAlaSource(formatMontant(value));
			break;
		case ristournes:
			setRistournes(formatMontant(value));
			break;
		case tauxReduit:
			setTauxReduit(formatValue(value,struct));
			break;
		case tvaNetteDroitsAuteur:
				setTvaNetteDroitsAuteur(formatMontant(value));
			break;
		case voiture:
			setVoiture(formatValue(value,struct));
			break;
		default:
			break;

		}
	}

	private String formatValue(String value, HonoraireStruct struct) {

		if(Strings.isBlank(value))return "";
		else {
			return struct.valeurAutorise;
		}
	}

	private BigDecimal formatMontant(String value) {
		BigDecimal bd=new BigDecimal(0);
		if(Strings.isBlank(value)) return bd;		
		
		try{
			bd=Util.arrondisAEuro(value);
		}catch(Exception e){
		
		}
		
		return bd;
	}
	private BigDecimal formatMontant(BigDecimal value) {
		BigDecimal bd=new BigDecimal(0);
		
		
		try{
			bd=Util.arrondisAEuro(value.doubleValue());
		}catch(Exception e){
		
		}
		
		return bd;
	}

	@Override
	public String toString() {
		return "Honoraire [beneficiaire=" + beneficiaire + ", honoraires=" + honoraires + ", commissions=" + commissions
				+ ", nourriture=" + nourriture + ", logement=" + logement + ", tauxReduit=" + tauxReduit + ", dispense="
				+ dispense + ", tvaNetteDroitsAuteur=" + tvaNetteDroitsAuteur + "]";
	}

	@Override
	public int compareTo(Honoraire o) {
		Integer i1=Integer.valueOf(this.getBeneficiaire().getNumero());
		Integer i2=Integer.valueOf(o.getBeneficiaire().getNumero());
		return i1.compareTo(i2);
	}

	public boolean isTierEntreprise()
	{
		return getBeneficiaire().isEntreprise();
	}
	public void reset(){
		for(HonoraireStruct hs:HonoraireStruct.values()){
			this.setValue(hs.position, null);
		}
	}
	public Object getValue(int index) {
		Object value=null;
		HonoraireStruct struct=HonoraireStruct.parse(index);
		switch (struct) {
		case CODE_TIERS:
			value=this.getCodeTiers();
			break;
		case allocForfaitaire:
			value=getAllocForfaitaire();
			break;
		case autres:
			value=getAutres();
			break;
		case autresRemunerations:
			value=getAutresRemunerations();
			break;
		case avantagesEnNature:
			value=getAvantagesEnNature();
			break;
		case commissions:
			value=getCommissions();
			break;
		case courtages:
			value=getCourtages();
			break;
		case dispense:
			break;
		case droitsAuteur:
			value=getDroitsAuteur();
			break;
		case droitsInventeur:
			value=getDroitsInventeur();
			break;
		case honoraires:
			value=getHonoraires();
			break;
		case indemnitesRemb:
			value=getIndemnitesRemb();
			break;
		case jetonsPresence:
			value=getJetonsPresence();
			break;
		case logement:
			value=getLogement();
			break;
		case nourriture:
			value=getNourriture();
			break;
		case outilsNTIC:
			value=getOutilsNTIC();
			break;
		case priseEnChargeDirecteEmployeur:
			value=getPriseEnChargeDirecteEmployeur();
			break;
		case remboursements:
			value=getRemboursements();
			break;
		case retenueAlaSource:
			value=getRetenueAlaSource();
			break;
		case ristournes:
			value=getRistournes();
			break;
		case tauxReduit:
			value=getTauxReduit();
			break;
		case tvaNetteDroitsAuteur:
			value=getTvaNetteDroitsAuteur();
			break;
		case voiture:
			value=getVoiture();
			break;
		default:
			break;

		}
		return value;
	}

	@Override
	public void setValue(AttributImport attribut, Object value) {
		String strValue=null;
		BigDecimal bdValue=null;
		if(value instanceof String)
			strValue=(value!=null)?((String) value).trim():null;
			else if(value instanceof  BigDecimal)
				bdValue=(value!=null)?(BigDecimal) value:null;
		int  index=	attribut.getIndex();	
				HonoraireStruct struct=HonoraireStruct.parse(index);
				switch (struct) {
				case CODE_TIERS:
					this.getBeneficiaire().setNumero(strValue);
					this.setCodeTiers(strValue);
					break;
				case allocForfaitaire:
					setAllocForfaitaire(formatValue(strValue, struct));
					break;
				case autres:
					setAutres(formatValue(strValue, struct));
					break;
				case autresRemunerations:
					setAutresRemunerations(formatMontant(bdValue));
					break;
				case avantagesEnNature:
					setAvantagesEnNature(formatMontant(bdValue));
					break;
				case commissions:
					this.setCommissions(formatMontant(bdValue));
					break;
				case courtages:
					this.setCourtages(formatMontant(bdValue));
					break;
				case dispense:
					break;
				case droitsAuteur:
					this.setDroitsAuteur(formatMontant(bdValue));
					break;
				case droitsInventeur:
					this.setDroitsInventeur(formatMontant(bdValue));
					break;
				case honoraires:
					setHonoraires(formatMontant(bdValue));
					break;
				case indemnitesRemb:
					setIndemnitesRemb(formatMontant(bdValue));
					break;
				case jetonsPresence:
					setJetonsPresence(formatMontant(bdValue));
					break;
				case logement:
					this.setLogement(formatValue(strValue,struct));
					break;
				case nourriture:
					setNourriture(formatValue(strValue,struct));
					break;
				case outilsNTIC:
					setOutilsNTIC(formatValue(strValue,struct));
					break;
				case priseEnChargeDirecteEmployeur:
					setPriseEnChargeDirecteEmployeur(formatValue(strValue,struct));
					break;
				case remboursements:
					setRemboursements(formatValue(strValue,struct));
					break;
				case retenueAlaSource:
					setRetenueAlaSource(formatMontant(bdValue));
					break;
				case ristournes:
					setRistournes(formatMontant(bdValue));
					break;
				case tauxReduit:
					setTauxReduit(formatValue(strValue,struct));
					break;
				case tvaNetteDroitsAuteur:
						setTvaNetteDroitsAuteur(formatMontant(bdValue));
					break;
				case voiture:
					setVoiture(formatValue(strValue,struct));
					break;
				default:
					break;

				}		
		
	}

	@Override
	public ConfigImport getConfigImport() {
		if(configImport==null) configImport=BudgetHelper.loadConfigImport("das.honoraire.import.config");
		return configImport;
	}
	private static ConfigImport configImport=null;

	@Override
	public void setRowNum(Integer rowNum) {
		
	}
	@Override
	public Integer getRowNum() {
		return null;
	}

}