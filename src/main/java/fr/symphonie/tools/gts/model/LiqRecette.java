package fr.symphonie.tools.gts.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.Trace;

public class LiqRecette {
	//private static final Logger logger = LoggerFactory.getLogger(LiqRecette.class);
	
	private Integer exercice;
	private Integer numero;
	private Integer numPeriode;
	private String libelle;
	private String tiersOrigine;
	private String debiteur;
	private Integer noAdresse;
	private String imputHt;
	private String imputTtc;
	private String imputTva;
	private Integer noLbi;
	private String refCommande;
	private String modePaie;
	private boolean differe;
	
	private List<LigneRecette> details;
	private Trace trace;
	@Transient
	private ClientGts tiers;
	
	@Transient
	private String destination;
	@Transient
	private String nature;
	@Transient
	private String service;
	@Transient
	private String programme;
	
	@Transient
	private Tiers tiersCpwin;

	public LiqRecette(ImportedData data) {
		this();
		this.exercice=data.getExercice();
		this.tiersOrigine=data.getClientGts().getCodeCpwin();
		this.debiteur=data.getClientGts().getCodeCpwin();
		this.noAdresse=data.getClientGts().getNoAdresseCpwin();
		ArticleDetails articleDetail=data.getArticle().getDetail();
		this.imputHt=articleDetail.getCompteProduit().getCode();
		this.imputTtc=data.getClientGts().isRegisseur()?"4711":articleDetail.getImputTtc();
		this.imputTva=articleDetail.getImputTva();
		this.noLbi=articleDetail.getNoLbi();
		this.refCommande=data.getRefCommande();
		this.numPeriode=data.getNumero();
		setTiers(data.getClientGts());
		setDiffere(false);
	}
	
	public LiqRecette() {
		super();
		this.details=new ArrayList<>();
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
		for(LigneRecette l:getDetails())l.setNoLiqRec(numero);
		
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getTiersOrigine() {
		return tiersOrigine;
	}
	public void setTiersOrigine(String tiersOrigine) {
		this.tiersOrigine = tiersOrigine;
	}
	public String getDebiteur() {
		return debiteur;
	}
	public void setDebiteur(String debiteur) {
		this.debiteur = debiteur;
	}
	public Integer getNoAdresse() {
		return noAdresse;
	}
	public void setNoAdresse(Integer noAdresse) {
		this.noAdresse = noAdresse;
	}
	public String getImputHt() {
		return imputHt;
	}
	public void setImputHt(String imputHt) {
		this.imputHt = imputHt;
	}
	public String getImputTtc() {
		return imputTtc;
	}
	public void setImputTtc(String imputTtc) {
		this.imputTtc = imputTtc;
	}
	public String getImputTva() {
		return imputTva;
	}
	public void setImputTva(String imputTva) {
		this.imputTva = imputTva;
	}
	public Integer getNoLbi() {
		return noLbi;
	}
	public void setNoLbi(Integer noLbi) {
		this.noLbi = noLbi;
	}
	public String getRefCommande() {
		return refCommande;
	}
	public void setRefCommande(String refCommande) {
		this.refCommande = refCommande;
	}
	public String getModePaie() {
		return modePaie;
	}
	public void setModePaie(String modePaie) {
		this.modePaie = modePaie;
	}
	public List<LigneRecette> getDetails() {
		return details;
	}
	public void setDetails(List<LigneRecette> details) {
		this.details = details;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	
	
	
	public String getDescription() {
		String SEPARATOR = "-";
		String separatorLbi = (this.numero == null) ? "" : SEPARATOR;
		StringBuffer description = new StringBuffer();
		description.append((this.numero == null) ? "" : "No lrec=" + this.numero);
		description.append((this.noLbi == null) ? "" : separatorLbi + "No lbi=" + this.noLbi);
		description.append((this.tiersOrigine == null) ? "" : SEPARATOR + "Tiers origine=" + this.tiersOrigine);
		description.append((this.refCommande == null) ? "" : SEPARATOR + "Ref commande=" + this.refCommande);
		String returnString = description.toString();
		return returnString;
	}

	public Integer getNumPeriode() {
		return numPeriode;
	}

	public void setNumPeriode(Integer numPeriode) {
		this.numPeriode = numPeriode;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

//	public LigneBudgetaireAE getLbi() {
//		return lbi;
//	}
//
//	public void setLbi(LigneBudgetaireAE lbi) {
//		this.lbi = lbi;
//	}

	public BigDecimal getTotalMntHtLiqReq() {
		BigDecimal result=new BigDecimal(0);
		if(details==null)return result;
		for (LigneRecette ligneRecette : details) {
			result=result.add(ligneRecette.getMntHt());
		}
		return result;
	}

	public BigDecimal getTotalMntRemiseLiqReq() {
		BigDecimal result=new BigDecimal(0);
		if(details==null)return result;
		for (LigneRecette ligneRecette : details) {
			result=result.add(ligneRecette.getMntRemise());
		}
		return result;
	}

	public BigDecimal getTotalMntTtcLiqReq() {
		BigDecimal result=new BigDecimal(0);
		if(details==null)return result;
		for (LigneRecette ligneRecette : details) {
			result=result.add(ligneRecette.getMntTtc());
		}
		return result;
	}

	public BigDecimal getTotalMntTvaLiqReq() {
		BigDecimal result=new BigDecimal(0);
		if(details==null)return result;
		for (LigneRecette ligneRecette : details) {
			result=result.add(ligneRecette.getMntTva());
		}
		return result;
	}

	public ClientGts getTiers() {
		return tiers;
	}

	public void setTiers(ClientGts tiers) {
		this.tiers = tiers;
	}

	public boolean isDiffere() {
		return differe;
	}

	public void setDiffere(boolean differe) {
		this.differe = differe;
	}

	

	public Tiers getTiersCpwin() {
		return tiersCpwin;
	}

	public void setTiersCpwin(Tiers tiersCpwin) {
		this.tiersCpwin = tiersCpwin;
	}

	
	
}
