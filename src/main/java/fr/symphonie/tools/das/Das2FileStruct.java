package fr.symphonie.tools.das;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.symphonie.common.util.Constant;
import fr.symphonie.common.util.Util;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.TypeEnum;
import fr.symphonie.tools.das.model.Honoraire;
import fr.symphonie.tools.das.model.TiersDas;


public class Das2FileStruct {
	private static final Logger logger = LoggerFactory.getLogger(Das2FileStruct.class);
	
	static final String DAS_SEPARATOR = " ";
	private ILine enregistrement010;
	private ILine enregistrement020;
	List<ILine> enregistrements210;
	private ILine enregistrement300;
	private ILine enregistrement310;
	private Honoraire totale;
	private String[] params;
	
	
	public Das2FileStruct(String[] etabliParams,List<Honoraire> honoraires) {
		super();
		this.params=etabliParams;
		this.enregistrement010=createEnreg010(etabliParams);
		this.enregistrement020=createEnreg020(etabliParams);
		this.enregistrements210=new ArrayList<>();
		totale=new Honoraire();
		totale.reset();
		for(Honoraire h: honoraires){
			logger.debug("Honoraire: codetiers={}",h.getCodeTiers());
			enregistrements210.add(createEnreg210(etabliParams,h));
			addToTotale(h);
		}
		this.enregistrement300=createEnreg300(etabliParams,totale);
		this.enregistrement310=createEnreg310(etabliParams,totale,1,honoraires.size());
	}
	private ILine createEnreg310(final String[] etabliParams, final Honoraire tot,final int nEnreg020,final int nEnreg210) {
		
		 return new Enregistrement(){
			@Override
			public AttributImport[] getAttributs() {
				int i=0;
				AttributImport[] attributs={
						new AttributImport(i++,TypeEnum.Alpha,9,etabliParams[Constant.SIREN]),
						new AttributImport(i++,TypeEnum.Numeric,12,"999999999999"),
						new AttributImport(i++,TypeEnum.Numeric,3,"310"),
						new AttributImport(i++,TypeEnum.Numeric,5,""+nEnreg020),//TODO
						new AttributImport(i++,TypeEnum.Numeric,6,"0"),
						new AttributImport(i++,TypeEnum.Numeric,6,""+nEnreg210),//TODO
						new AttributImport(i++,TypeEnum.Numeric,18,"0"),
						new AttributImport(i++,TypeEnum.Alpha,18,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,108,"0"),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getHonoraires())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getCommissions())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getCourtages())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getRistournes())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getJetonsPresence())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getDroitsAuteur())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getDroitsInventeur())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getAutresRemunerations())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getIndemnitesRemb())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getAvantagesEnNature())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getRetenueAlaSource())),
						//new AttributImport(i++,TypeEnum.Numeric,36,"0"),
						new AttributImport(i++,TypeEnum.Alpha,12,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,12,"0"),
						new AttributImport(i++,TypeEnum.Alpha,12,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,6,"0"),
						new AttributImport(i++,TypeEnum.Numeric,72,"0"),
						new AttributImport(i++,TypeEnum.Alpha,241,DAS_SEPARATOR),
				};
				return attributs;
			}
		};
	}
	private ILine createEnreg300(final String[] etabliParams, final Honoraire tot) {
		return new Enregistrement(){
			@Override
			public AttributImport[] getAttributs() {
				int i=0;
				AttributImport[] attributs={
						new AttributImport(i++,TypeEnum.Alpha,14,etabliParams[Constant.SIRET]),
						new AttributImport(i++,TypeEnum.Numeric,2,"01"),
						new AttributImport(i++,TypeEnum.Numeric,4,etabliParams[Constant.EXERCICE]),
						new AttributImport(i++,TypeEnum.Numeric,1,"4"),
						new AttributImport(i++,TypeEnum.Numeric,3,"300"),
						new AttributImport(i++,TypeEnum.Alpha,36,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,108,"0"),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getHonoraires())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getCommissions())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getCourtages())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getRistournes())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getJetonsPresence())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getDroitsAuteur())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getDroitsInventeur())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getAutresRemunerations())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getIndemnitesRemb())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getAvantagesEnNature())),
						new AttributImport(i++,TypeEnum.Numeric,12,Util.format(tot.getRetenueAlaSource())),
						new AttributImport(i++,TypeEnum.Alpha,12,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,12,"0"),
						new AttributImport(i++,TypeEnum.Alpha,12,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,78,"0"),
						new AttributImport(i++,TypeEnum.Alpha,62,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Alpha,50,null),
						new AttributImport(i++,TypeEnum.Alpha,10,null),
						new AttributImport(i++,TypeEnum.Alpha,60,null),
						new AttributImport(i++,TypeEnum.Alpha,76,DAS_SEPARATOR),
				};
				return attributs;
			}
		};
	}
	private void addToTotale(Honoraire h){
		for(int i=1;i<=11;i++){
			BigDecimal bd=((BigDecimal)totale.getValue(i)).add((BigDecimal)h.getValue(i));
			totale.setValue(i,bd.toString());
		}
	}
	private ILine createEnreg210(final String[] etabliParams,final Honoraire honoraire) {
		final TiersDas tiers=honoraire.getBeneficiaire();
		logger.debug("createEnreg210: tiers= {}",tiers);
		logger.debug("createEnreg210: adresse= {}",tiers.getAdresse());
		logger.debug("createEnreg210: voie= {}",tiers.getAdresse().getVoie());
		return new Enregistrement(){
			@Override
			public AttributImport[] getAttributs() {
				int i=0;
				AttributImport[] attributs={
						new AttributImport(i++,TypeEnum.Alpha,14,etabliParams[Constant.SIRET]),
						new AttributImport(i++,TypeEnum.Numeric,2,"01"),
						new AttributImport(i++,TypeEnum.Numeric,4,etabliParams[Constant.EXERCICE]),
						new AttributImport(i++,TypeEnum.Numeric,1,"4"),
						new AttributImport(i++,TypeEnum.Numeric,3,"210"),
						new AttributImport(i++,TypeEnum.Alpha,14,honoraire.isTierEntreprise()?tiers.getSiret():null),
						new AttributImport(i++,TypeEnum.Alpha,30,(!honoraire.isTierEntreprise())?tiers.getNom():null),
						new AttributImport(i++,TypeEnum.Alpha,20,(!honoraire.isTierEntreprise())?tiers.getPrenom():null),
						new AttributImport(i++,TypeEnum.Alpha,50,honoraire.isTierEntreprise()?tiers.getRs():null),
						new AttributImport(i++,TypeEnum.Alpha,30,tiers.getProfession()),
						new AttributImport(i++,TypeEnum.Alpha,32,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Numeric,4,tiers.getAdresse().getVoie().getNumero()),
						new AttributImport(i++,TypeEnum.Alpha,1,tiers.getAdresse().getVoie().getType()),
						new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Alpha,26,tiers.getAdresse().getVoie().getNatureEtNom()),
						new AttributImport(i++,TypeEnum.Numeric,5,tiers.getAdresse().getCodeINSEE()),
						new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Alpha,26,tiers.getAdresse().getCommune()),
						new AttributImport(i++,TypeEnum.Numeric,5,tiers.getAdresse().getCodePostale()),
						
						new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Alpha,26,tiers.getAdresse().getBureau()),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getHonoraires())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getCommissions())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getCourtages())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getRistournes())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getJetonsPresence())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getDroitsAuteur())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getDroitsInventeur())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getAutresRemunerations())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getIndemnitesRemb())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getAvantagesEnNature())),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getRetenueAlaSource())),
						//new AttributImport(i++,TypeEnum.Alpha,10,DAS_SEPARATOR),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getNourriture()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getLogement()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getVoiture()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getAutres()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getOutilsNTIC()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getAllocForfaitaire()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getRemboursements()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getPriseEnChargeDirecteEmployeur()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getTauxReduit()),
						new AttributImport(i++,TypeEnum.Alpha,1,honoraire.getDispense()),
						new AttributImport(i++,TypeEnum.Numeric,10,Util.format(honoraire.getTvaNetteDroitsAuteur())),
						new AttributImport(i++,TypeEnum.Alpha,245,DAS_SEPARATOR)
				};
				return attributs;
			}
		};
	}
	private ILine createEnreg020(final String[] etabliParams) {
		return new Enregistrement(){

			@Override
			public AttributImport[] getAttributs() {
				int i=0;
				AttributImport[] attributs={
					new AttributImport(i++,TypeEnum.Alpha,14,etabliParams[Constant.SIRET]),
					new AttributImport(i++,TypeEnum.Numeric,2,"01"),
					new AttributImport(i++,TypeEnum.Numeric,4,etabliParams[Constant.EXERCICE]),
					new AttributImport(i++,TypeEnum.Numeric,1,"4"),
					new AttributImport(i++,TypeEnum.Numeric,3,"020"),
					new AttributImport(i++,TypeEnum.Alpha,14,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,5,etabliParams[Constant.CODE_APE]),
					new AttributImport(i++,TypeEnum.Alpha,14,"00000000000000"),
					new AttributImport(i++,TypeEnum.Alpha,41,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,50,etabliParams[Constant.RAISON_SOCIALE]),			
					new AttributImport(i++,TypeEnum.Alpha,32,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Numeric,4,etabliParams[Constant.ADRESSE_N_VOIE]),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR), 
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,etabliParams[Constant.NATURE_ET_NOM_VOIE]), 
					new AttributImport(i++,TypeEnum.Numeric,5,"0"), 	
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR), 	
					new AttributImport(i++,TypeEnum.Alpha,26,DAS_SEPARATOR), 	
					new AttributImport(i++,TypeEnum.Numeric,5,etabliParams[Constant.CODE_POSTAL_BUR_DISTR]), 
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR), 
					new AttributImport(i++,TypeEnum.Alpha,26,etabliParams[Constant.BUREAU_DISTRIBUTEUR]),
					new AttributImport(i++,TypeEnum.Alpha,40,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,53,DAS_SEPARATOR),	
					new AttributImport(i++,TypeEnum.Alpha,5,"NNNNN"),	
					new AttributImport(i++,TypeEnum.Alpha,297,DAS_SEPARATOR),	
				};
				return attributs;
			}
		};
		
	}
	private ILine createEnreg010(final String[] etabliParams) {
		return new Enregistrement(){

			@Override
			public AttributImport[] getAttributs() {
				int i=0;
				AttributImport[] attributs={
					new AttributImport(i++,TypeEnum.Alpha,9,etabliParams[Constant.SIREN]),
					new AttributImport(i++,TypeEnum.Numeric,12,"0"),
					new AttributImport(i++,TypeEnum.Numeric,3,"010"),
					new AttributImport(i++,TypeEnum.Alpha,14,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,5,etabliParams[Constant.CODE_APE]),
					new AttributImport(i++,TypeEnum.Alpha,4,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,50,etabliParams[Constant.RAISON_SOCIALE]),
					new AttributImport(i++,TypeEnum.Alpha,32,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Numeric,4,etabliParams[Constant.ADRESSE_N_VOIE]),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,etabliParams[Constant.NATURE_ET_NOM_VOIE]),
					new AttributImport(i++,TypeEnum.Numeric,5,"0"),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,5,etabliParams[Constant.CODE_POSTAL_BUR_DISTR]),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,etabliParams[Constant.BUREAU_DISTRIBUTEUR]),
					new AttributImport(i++,TypeEnum.Alpha,8,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1,"X"),
					new AttributImport(i++,TypeEnum.Alpha,14,etabliParams[Constant.SIRET]),
					new AttributImport(i++,TypeEnum.Alpha,5,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,32,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Numeric,4,etabliParams[Constant.ADRESSE_N_VOIE]),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,etabliParams[Constant.NATURE_ET_NOM_VOIE]),
					new AttributImport(i++,TypeEnum.Numeric,5,"0"),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Numeric,5,etabliParams[Constant.CODE_POSTAL_BUR_DISTR]),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,26,etabliParams[Constant.BUREAU_DISTRIBUTEUR]),
					new AttributImport(i++,TypeEnum.Alpha,1,DAS_SEPARATOR),
					new AttributImport(i++,TypeEnum.Alpha,288,DAS_SEPARATOR),
				};
				return attributs;
			}
			
		};
		
	}
	public ILine getEnregistrement010() {
		return enregistrement010;
	}
	public void setEnregistrement010(ILine enregistrement010) {
		this.enregistrement010 = enregistrement010;
	}
	public ILine getEnregistrement020() {
		return enregistrement020;
	}
	public void setEnregistrement020(ILine enregistrement020) {
		this.enregistrement020 = enregistrement020;
	}
	public List<ILine> getEnregistrements210() {
		return enregistrements210;
	}
	public void setEnregistrements210(List<ILine> enregistrements210) {
		this.enregistrements210 = enregistrements210;
	}
	public ILine getEnregistrement300() {
		return enregistrement300;
	}
	public void setEnregistrement300(ILine enregistrement300) {
		this.enregistrement300 = enregistrement300;
	}
	public ILine getEnregistrement310() {
		return enregistrement310;
	}
	public void setEnregistrement310(ILine enregistrement310) {
		this.enregistrement310 = enregistrement310;
	}
	
	
public interface ILine {
		AttributImport[] getAttributs();
	}
	public class Enregistrement implements ILine{

		@Override
		public AttributImport[] getAttributs() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public String getGeneratedFileName(){
		String separator="_";
		StringBuilder name=new StringBuilder("DAS2");
		name.append(separator);
		if(params[Constant.Mode]!=null)
		name.append(params[Constant.Mode]);
		name.append(separator);
		name.append(params[Constant.EXERCICE]);
		name.append(separator);
		name.append(params[Constant.SIRET]);
		name.append(".txt");
		
				

		return name.toString();
	}

}
