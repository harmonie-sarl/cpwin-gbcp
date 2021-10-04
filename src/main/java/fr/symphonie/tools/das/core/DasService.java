package fr.symphonie.tools.das.core;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.common.model.AppCfgConfig;
import fr.symphonie.common.util.Constant;
import fr.symphonie.exception.MissingConfiguration;
import fr.symphonie.tools.das.model.Honoraire;
import fr.symphonie.tools.das.model.TiersDas;

@Service
public class DasService {
	private DasDao dao;
//	private IJdbcHelper jdbcHelper;
//	
//   @Inject
//	public void setJdbcHelper(IJdbcHelper jdbcHelper) {
//		this.jdbcHelper = jdbcHelper;
//	}
	
   @Inject
	public void setDao(DasDao dao) {
		this.dao = dao;
	}

	
public List<TiersDas> loadTiers(List<String> codeList){
	
	return dao.loadTiers(codeList);
}

@Transactional
public <T extends Object> void saveList(List<T> list) {
	dao.saveList(list);
	
}
public List<Honoraire> loadHonoraires(Integer exercice) {
	return dao.loadHonoraires(exercice);
}

public List<Honoraire> loadHonoraires(Integer exercice, String codeTiers) {
		return dao.loadHonoraires(exercice, codeTiers);
}

public String[] getEtablisConfigParams() throws MissingConfiguration {
	String[] resul=new String[20];
	//String value=null;
	AppCfgConfig appConfig=AppCfgConfig.getInstance();
	for(EtablisConfigParams config:EtablisConfigParams.values()){
		//value=jdbcHelper.getCfgconfigValue(config.getParam(), null);
		switch(config){
		case AdresseLigne1:
			resul[Constant.AdresseLigne1]=appConfig.getAdresseLigne1();
			break;
		case AdresseLigne2:
			resul[Constant.AdresseLigne2]=appConfig.getAdresseLigne2();
			break;
		case CP:
			resul[Constant.CODE_POSTAL_BUR_DISTR]=appConfig.getEtablisCP();
			break;
		case CodeApe:
			resul[Constant.CODE_APE]=appConfig.getCodeApe();
			break;
		case Nom:
			resul[Constant.RAISON_SOCIALE]=appConfig.getEtablisNom();
			break;
		case SIREN:
			resul[Constant.SIREN]=appConfig.geSiren();
			break;
		case SIRET:
			resul[Constant.SIRET]=appConfig.getSiret();
			break;
		case Ville:
			resul[Constant.BUREAU_DISTRIBUTEUR]=appConfig.getEtablisVille();
			break;
		default:
			break;
		
		}
	}
	return resul;
}
public enum EtablisConfigParams{
	AdresseLigne1,
	AdresseLigne2,
	CodeApe,
	CP,
	Nom,
	SIREN,
	SIRET,
	Ville
	;
//	private String param;

	private EtablisConfigParams() {
		//this.param = param;
	}

//	public String getParam() {
//		return param;
//	}
	
	
}

	public List<TiersDas> findLightTiersList(boolean searchAtBegin, String formatedPrefix, String numSiret, boolean withAll) {

		return dao.findLightTiersList(searchAtBegin, formatedPrefix, numSiret,withAll);
	}
	
	public TiersDas findTiers(String codeTiers) {
		return dao.findTiers(codeTiers);
		
	}
	

}
