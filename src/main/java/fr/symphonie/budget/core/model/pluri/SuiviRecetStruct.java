package fr.symphonie.budget.core.model.pluri;

import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.util.model.SimpleEntity;

public class SuiviRecetStruct extends GlobalSuiviStruct implements Comparable<SuiviRecetStruct>{
		
	private Integer execOrigin;
	private Integer noTitrec;	
	private Tiers tiers;	
	private String objet;
	
	private SimpleEntity origin;
	private SimpleEntity service;	
	private SimpleEntity natGrp;	
	private SimpleEntity programme;
	
	public SuiviRecetStruct() {
		super();
   }

	public SuiviRecetStruct(int execOrigin, int noTitrec, String tiers, String objet,String origin, String service,
			String natGrp, String programme, MontantHtTtc initial, MontantHtTtc rar, MontantHtTtc solde) {
		super();
		this.execOrigin = execOrigin;
		this.noTitrec = noTitrec;
		this.tiers = new Tiers(tiers!=null?tiers.trim():"", null);
		this.objet = objet;
		this.origin= new SimpleEntity(origin, "");
		this.service = new SimpleEntity(service, "");
		this.natGrp =  new SimpleEntity(natGrp, "");;
		this.programme =  new SimpleEntity(programme, "");
		setInitial(initial);
		setRar(rar);
		setSolde(solde);
	}

	public Integer getExecOrigin() {
		return execOrigin;
	}

	public void setExecOrigin(Integer execOrigin) {
		this.execOrigin = execOrigin;
	}

	public Integer getNoTitrec() {
		return noTitrec;
	}

	public void setNoTitrec(Integer noTitrec) {
		this.noTitrec = noTitrec;
	}
	
	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public Tiers getTiers() {
		return tiers;
	}

	public void setTiers(Tiers tiers) {
		this.tiers = tiers;
	}

	public SimpleEntity getService() {
		return service;
	}

	public void setService(SimpleEntity service) {
		this.service = service;
	}

	public SimpleEntity getNatGrp() {
		return natGrp;
	}

	public void setNatGrp(SimpleEntity natGrp) {
		this.natGrp = natGrp;
	}

	public SimpleEntity getProgramme() {
		return programme;
	}

	public void setProgramme(SimpleEntity programme) {
		this.programme = programme;
	}


	public SimpleEntity getOrigin() {
		return origin;
	}


	public void setOrigin(SimpleEntity origin) {
		this.origin = origin;
	}

	public String getCodeTiers() {
		if (getTiers() != null)
			return getTiers().getCode();
		return null;
	}

	public String getCodeOrigin() {
		if (getOrigin() != null)
			return getOrigin().getCode();
		return null;
	}

	public String getCodeService() {
		if (getService() != null)
			return getService().getCode();
		return null;
	}
	
	public String getCodeNatGrp() {
		if (getNatGrp() != null)
			return getNatGrp().getCode();
		return null;
	}
	
	public String getCodeProgramme() {
		if (getProgramme() != null)
			return getProgramme().getCode();
		return null;
	}

	@Override
	public int compareTo(SuiviRecetStruct o) {
		int c=this.execOrigin.compareTo(o.execOrigin);
		if(c!=0)return c;
		return this.noTitrec.compareTo(o.noTitrec);
		
	}
	
}
