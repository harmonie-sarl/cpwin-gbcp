package fr.symphonie.budget.core.dao.jdbc;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.budget.core.model.DepRecEnum;
import fr.symphonie.budget.core.model.Ventillation;
import fr.symphonie.budget.core.model.edition.Tab6;
import fr.symphonie.budget.core.model.edition.util.CAF;
import fr.symphonie.budget.core.model.edition.util.CRP;
import fr.symphonie.budget.core.model.plt.Ecriture;
import fr.symphonie.budget.core.model.plt.Periode;
import fr.symphonie.budget.core.model.plt.PeriodeEnum;
import fr.symphonie.budget.core.model.pluri.BudgModification;
import fr.symphonie.budget.core.model.pluri.BudgetCpDest;
import fr.symphonie.budget.core.model.pluri.ComplexEntity;
import fr.symphonie.budget.core.model.pluri.CpGestionnaire;
import fr.symphonie.budget.core.model.pluri.EtatEnum;
import fr.symphonie.budget.core.model.pluri.GlobalSuiviStruct;
import fr.symphonie.budget.core.model.pluri.ModificationCpLigne;
import fr.symphonie.budget.core.model.pluri.MontantHtTtc;
import fr.symphonie.budget.core.model.pluri.SuiviRecetStruct;
import fr.symphonie.common.util.BudgetHelper;
import fr.symphonie.cpwin.model.cpt.DebitCreditEnum;
import fr.symphonie.cpwin.model.nantes.EtudiantPj;
import fr.symphonie.tools.meta4dai.model.LbData;
import fr.symphonie.util.Helper;
import fr.symphonie.util.model.SimpleEntity;
@Repository
public class JdbcHelper implements IJdbcHelper{

	
	
	/**
	 * Logger for this class 
	 */
	private static final Logger logger = LoggerFactory.getLogger(JdbcHelper.class);

	private JdbcTemplate jdbcTemplate;
	
	
	@Inject
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
	@Override
	public List<Integer> getExercices(boolean onlyNotClosed) {
		String sqlQuery = "select distinct num_exec from budget";
		if(onlyNotClosed) {
			sqlQuery+=" where cloture2='N'";
		}
			
		List<Integer> returnedList = jdbcTemplate.queryForList(sqlQuery,
				new Object[] {}, Integer.class);
		return returnedList;
	}
	
	@Override
	public List<String> getExercicesList(boolean onlyNotClosed) {
		String sqlQuery = "select distinct num_exec from budget";
		if(onlyNotClosed) {
			sqlQuery+=" where cloture2='N'";
		}
			
		List<String> returnedList = jdbcTemplate.queryForList(sqlQuery,
				new Object[] {}, String.class);
		return returnedList;
	}
	@Override
	public List<String> getExercicesAeList() {
		String sqlQuery = "select distinct num_exec from CfgConfig where param ='fGBCP' and valeur ='1'";
		List<String> returnedList = jdbcTemplate.queryForList(sqlQuery,
				new Object[] {}, String.class);
		return returnedList;
	}
	@Override
	public List<String> getCodeBudgList(int numExec) {
		String sqlQuery = "select distinct code_budg from budget where num_exec=? ";
		List<String> returnedList = jdbcTemplate.queryForList(sqlQuery,
				new Object[] { numExec }, String.class);

		return returnedList;
	}

	@Override
	public List<ComplexEntity> getGroupDestNat(int exercice) {
	
		String requete = "select distinct b.dest_grp as dest_grp,d.nom_dest_grp as nom_dest_grp ,b.nat_grp as nat_grp ,n.nom_nat_grp as nom_nat_grp"+
		" from budginterneenveloppe b,BudgNatureGroupe n,BudgDestinationGroupe d "+
			" where b.dest_grp=d.dest_grp and b.nat_grp=n.nat_grp and b.num_exec=? and b.dep_rec='D'" ;
		
		List<ComplexEntity> returnedList = (List<ComplexEntity>) this.jdbcTemplate
				.query(requete, new Object[] { exercice }, new RowMapper<ComplexEntity>() {
					public ComplexEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						ComplexEntity cpe = new ComplexEntity();
						cpe.getEntity1().setCode(rs.getString("dest_grp"));
						cpe.getEntity1().setDesignation(
								rs.getString("nom_dest_grp"));
						cpe.getEntity1().setDesignation(
								rs.getString("nom_dest_grp"));
						
						cpe.getEntity2().setCode(rs.getString("nat_grp"));
						cpe.getEntity2().setDesignation(
								rs.getString("nom_nat_grp"));
						cpe.getEntity2().setDesignation(
								rs.getString("nom_nat_grp"));
						return cpe;

					}
				});
		return returnedList;
	}

	@Override
	public List<ComplexEntity> getDestNat(int exercice,String destGrp, String natGrp,  String codeBudg) {
		String requete = "select distinct b.code_dest,b.code_nature"+
		" from budginterneenveloppe b"+
			" where  b.num_exec=? and b.dest_grp=? and b.nat_grp=?  and b.dep_rec='D'" ;		
		List<ComplexEntity> returnedList = (List<ComplexEntity>) this.jdbcTemplate
				.query(requete, new Object[] { exercice,destGrp,natGrp }, new RowMapper<ComplexEntity>() {
					public ComplexEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						ComplexEntity cpe = new ComplexEntity();
						cpe.getEntity1().setCode(rs.getString("code_dest"));

						
						cpe.getEntity2().setCode(rs.getString("code_nature"));

						return cpe;

					}
				});
		return loadNatureAndDestLibelle(returnedList, exercice, codeBudg);
		
	}
	
	private List<ComplexEntity> loadNatureAndDestLibelle(List<ComplexEntity>  destNatList,int exercice,String codeBudg)
	{
		SimpleEntity entity=null;
		if(destNatList==null)return destNatList;
		for (ComplexEntity ce : destNatList) {
			entity=getBudgDestination(exercice, ce.getEntity1().getCode(), codeBudg);
			if(entity!=null)ce.getEntity1().setDesignation(entity.getDesignation());
			entity=getNature(exercice, ce.getEntity2().getCode(), codeBudg);
			if(entity!=null)ce.getEntity2().setDesignation(entity.getDesignation());
		}
		return destNatList;
	}

	@Override
	public List<SimpleEntity> getProgramList() {
		
		String requete = "select distinct code_prog,nom_prog from budgprogramme where ouvert=1";

		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("code_prog"));
						entity.setDesignation(rs.getString("nom_prog"));
						return entity;
					}
				});
		return returnedList;
	}

	@Override
	public List<SimpleEntity> getServiceList() {
	String requete = "select distinct code_service,nom_service from DroitService where ouvert=1";

		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("code_service"));
						entity.setDesignation(rs.getString("nom_service"));
						entity.getLibelle().setLibelleDE(
								rs.getString("nom_service"));
						return entity;
					}
				});
		return returnedList;
	}
	
	@Override
	public SimpleEntity getNatureGroupe(String natureGroupe) {


		String requete = "select distinct nat_grp, nom_nat_grp from BudgNatureGroupe where nat_grp=? ";

		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {natureGroupe}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("nat_grp"));						
						entity.setDesignation(
								rs.getString("nom_nat_grp"));
						return entity;
					}
				});
		
		if((returnedList==null)||(returnedList.isEmpty()))return null;
		return returnedList.get(0);
	}
	
	
	@Override
	public SimpleEntity getDestinationGroupe(String destinationGroupe) {
		

		String requete = " select distinct dest_grp, nom_dest_grp from BudgDestinationGroupe where dest_grp=? ";

		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {destinationGroupe}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("dest_grp"));						
						entity.setDesignation(
								rs.getString("nom_dest_grp"));
						return entity;
					}
				});
		
		if((returnedList==null)||(returnedList.isEmpty()))return null;
		return returnedList.get(0);
	}
	
	@Override
	public SimpleEntity getNature(int exercice,String codeNature,  String codeBudg) {		
		String requete = "select distinct b.code_nature ,b.nom_nature"+
		" from BudgNature b where  b.num_exec=? and b.code_nature=?   and b.dep_rec='D'" ;
		
		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] { exercice,codeNature }, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity cpe = new SimpleEntity();
						cpe.setCode(rs.getString("code_nature"));						
						cpe.setDesignation(rs.getString("nom_nature"));
						return cpe;
					}
				});
		if((returnedList==null)||(returnedList.isEmpty()))return null;
		return returnedList.get(0);
	}
	
	@Override
	public SimpleEntity  getBudgDestination(int exercice,String codeDest,  String codeBudg) {
		String requete = "select distinct b.code_dest ,b.nom_dest"+
		" from BudgDestination b where  b.num_exec=? and code_budg=? and b.code_dest=?   " ;
		
		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] { exercice,codeBudg,codeDest }, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity cpe = new SimpleEntity();
						cpe.setCode(rs.getString("code_dest"));						
						cpe.setDesignation(rs.getString("nom_dest"));
						return cpe;
					}
				});
		if((returnedList==null)||(returnedList.isEmpty()))return null;
		return returnedList.get(0);
	}

	
	public double getDepAe(int exercice, String nat_grp) {		
		Double depAe = 0d;		
		String sqlQuery = "select sum(bp) as depAe from budginterneenveloppe where num_exec=? and nat_grp=?  ";
		try {
			depAe = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, nat_grp },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double depenseAE = rst.getDouble("depAe");
							return depenseAE;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			depAe = 0d;
		}

		return depAe;
	}
	public double getDepCp(int exercice_ae, String nat_grp, int num_exec_cp) {		
		Double depCp = 0d;		
		String sqlQuery = "select sum(montant) as depCp from budgcp where num_exec_ae=? and nat_grp=? and num_exec_cp=?  ";
		try {
			depCp = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice_ae, nat_grp,num_exec_cp },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double depenseCP = rst.getDouble("depCp");
							return depenseCP;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			depCp = 0d;
		}

		return depCp;
	}
	public double getRecet(int exercice, String nat_grp) {		
		Double montRec = 0d;		
		String sqlQuery = "select sum(bp) as montRec from budginterneenveloppe where num_exec=? and nat_grp=? ";
		try {
			montRec = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, nat_grp },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montRecette = rst.getDouble("montRec");
							return montRecette;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			montRec = 0d;
		}

		return montRec;
	}

	@Override
	public List<String> getAllExercieCp() {
		String sqlQuery = "select distinct cp.num_exec_cp from budgcp cp,budget b where cp.num_exec_cp=b.num_exec ";
		List<String> returnedList = jdbcTemplate.queryForList(sqlQuery,
				new Object[] {}, String.class);
		return returnedList;
	}

	@Override
	public List<SimpleEntity> getDestinationList(int exercice,String groupNat,Integer niveauDestination) {
		
//		logger.info("public List<SimpleEntity> getDestinationList(int exercice="+exercice+",String groupNat="+groupNat+",Integer niveauDestination="+niveauDestination+") ");
		
		Object[] params=null;
		String requete = "select distinct d.code_dest ,d.nom_dest from budgdestination d,budginterneligne b where d.num_exec=b.num_exec "+
				" and d.num_exec=? and b.code_dest like d.code_dest+'%' ";
		if(niveauDestination!=null){
//			logger.info("	if(niveauDestination!=null)");			
			requete+=" and CHAR_LENGTH(d.code_dest)="+niveauDestination;
		}
			
		else
			requete+=" and d.niveau_dest=1 ";
		if((groupNat!=null)&&(!groupNat.trim().isEmpty())){
			requete +="and b.nat_grp=? "	;
			params=new Object[] {exercice,groupNat};
		}
		else{
			params=new Object[] {exercice};
		}
				
				requete +=" order by d.code_dest";


		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, params, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("code_dest"));
						entity.setDesignation(rs.getString("nom_dest"));
						return entity;
					}
				});
		
//		logger.info("	public List<SimpleEntity> getDestinationList() :returnedList.size()= "+returnedList.size());	
		return returnedList;
	}

	@Override
	public void updateBudgetEtatCp(Integer exercice, String etatCp) {
		logger.info("updateBudgetEtatCp: exercice: "+exercice+" etatCp: "+etatCp);
		String requete="update budget set etat_cp=? where num_exec=?";
		jdbcTemplate.update(requete,new Object[] {etatCp,exercice});	
	}
	@Override
	public EtatEnum getBudgetEtatCp(int exercice){
		String etatCp=null;
		String requete="select etat_cp from budget where num_exec=?";
		etatCp=jdbcTemplate.queryForObject(requete, new Object[] {exercice}, String.class);
		return EtatEnum.parse(etatCp);
	}

	public double getRessourceFAE(int exercice, String dep_rec, String natAgr) {		
		Double montRes = 0d;		
		String sqlQuery = "select sum(bp) as montRes from budginterneligne where num_exec=? and dep_rec=? and code_nature in(select code_nature from budgnature where num_exec=? and dep_rec=? and nat_agr=?)";
		try {
			montRes = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, dep_rec,exercice,dep_rec, natAgr },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montRessource = rst.getDouble("montRes");
							return montRessource;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			montRes = 0d;
		}

		return montRes;
	}
	public List<BudgetCpDest> getAeDestinationList(int exercice,String dep_rec) {
		String requete = "select d.code_dest as code_dest,d.nom_dest as nom_dest,nat_grp as nat_grp,sum(bp)as montDep from budginterneligne b, budgdestination d where b.num_exec=? and d.num_exec=b.num_exec"+
" and b.code_dest like d.code_dest +'%' and d.niveau_dest=1 and b.dep_rec=? group by d.code_dest,d.nom_dest,nat_grp";


		List<BudgetCpDest> returnedList = (List<BudgetCpDest>) this.jdbcTemplate
				.query(requete, new Object[] {exercice,dep_rec}, new RowMapper<BudgetCpDest>() {
					public BudgetCpDest mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						BudgetCpDest entity = new BudgetCpDest();
						entity.setDestination(rs.getString("code_dest"));
						entity.setMontant(new BigDecimal(rs.getDouble("montDep")));
						entity.setGroupNat(rs.getString("nat_grp"));
				//	logger.debug("getAeDestinationList: nom_dest="+rs.getString("nom_dest"));
						entity.setLibelle(rs.getString("nom_dest"));
						return entity;
					}
				});
		return returnedList;
	}
	public Double getAeDestMontant(int exercice,String dep_rec,String code_dest,String nat_grp) {
		Double montDep=0d;
		String requete = "select sum(bp)as montDep from budginterneligne b, budgdestination d where b.num_exec=? and d.num_exec=b.num_exec"+
" and b.dep_rec=? and d.code_dest =? and d.niveau_dest=1 and b.nat_grp=? ";

		try {
			montDep = (Double) jdbcTemplate.queryForObject(requete,
					new Object[] { exercice,dep_rec,code_dest,nat_grp },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montDepCp = rst.getDouble("montDep");
							return montDepCp;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			montDep = 0d;
		}

		return montDep;
	}
	
	@Override
	public SimpleEntity getGestionnaire(String code) {
		String requete = "select  d.code_gest,d.nom_gest from droitgest d where d.code_gest=?";

		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] { code },
						new RowMapper<SimpleEntity>() {
							public SimpleEntity mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								SimpleEntity entity = new SimpleEntity();
								entity.setCode(rs.getString("code_gest"));
								entity.setDesignation(rs.getString("nom_gest"));
								return entity;
							}
						});
		if ((returnedList != null)&&(!returnedList.isEmpty()))
			return returnedList.get(0);
		return null;
	}
	
	@Override
	public List<SimpleEntity> getListFormeSocial() {
		String requete = "select code_poste , nom_l " +
				" from CfgRfTab" +
				" where code_table = 'TieFormSoc' order by no_ordre";


		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("code_poste"));
						entity.setDesignation(rs.getString("nom_l"));
						
						return entity;
					}
				});
		return returnedList;
	}
	public List<BudgetCpDest> getRecetDestList(int exercice,String dep_rec) {
		String requete = "select d.code_dest as code_dest,d.nom_dest as nom_dest,nat_grp as nat_grp,sum(bp)as montRec from budginterneligne b, budgdestination d where b.num_exec=? and d.num_exec=b.num_exec"+
" and b.code_dest like d.code_dest +'%' and d.niveau_dest=1 and b.dep_rec=? group by d.code_dest,d.nom_dest,nat_grp";


		List<BudgetCpDest> returnedList = (List<BudgetCpDest>) this.jdbcTemplate
				.query(requete, new Object[] {exercice,dep_rec}, new RowMapper<BudgetCpDest>() {
					public BudgetCpDest mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						BudgetCpDest entity = new BudgetCpDest();
						entity.setDestination(rs.getString("code_dest"));
						entity.setMontant(new BigDecimal(rs.getDouble("montRec")));
						entity.setGroupNat(rs.getString("nat_grp"));
					//logger.debug("getAeDestinationList: nom_dest="+rs.getString("nom_dest"));
						entity.setLibelle(rs.getString("nom_dest"));
						return entity;
					}
				});
		return returnedList;
	}
	
	public String getCfgconfigValue(String paramName,Integer exercice) {
		String result=null;
		String requete="select valeur from cfgconfig where param=? ";
		if(exercice!=null) requete+=" and num_exec="+exercice;
		
//		List<String> list = this.jdbcTemplate.queryForList(requete,new Object[] { paramName }, String.class);
//		if((list!=null) && (!list.isEmpty()))result=list.get(0);
		result = (String) this.jdbcTemplate.queryForObject(requete, new Object[] { paramName },	new RowMapper<String>() {
			public String mapRow(ResultSet rst, int rowNum)	throws SQLException {
				return rst.getString(1);				
			}
		});
		
		return result;
	}
	@Override
	public List<CpGestionnaire> getCpGestionnaireList(final int exercice,
			final String codeBudget) {
		String requete = "SELECT nat_grp,sum(co) totalCo,sum(br_encours) totalEncours from budgcpgest where num_exec=? and code_budg=? group by nat_grp";


				List<CpGestionnaire> returnedList = (List<CpGestionnaire>) this.jdbcTemplate
						.query(requete, new Object[] {exercice,codeBudget}, new RowMapper<CpGestionnaire>() {
							public CpGestionnaire mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								CpGestionnaire entity = new CpGestionnaire(exercice,codeBudget,null);
								
								entity.setGroupNat(rs.getString("nat_grp"));
								entity.setCreditOuvert(rs.getDouble("totalCo"));
								entity.setBudgetRectificatif(rs.getDouble("totalEncours"));
								return entity;
							}
						});
				return returnedList;
	}
	public double getNoDataBudg1(int exercice, String code_nat) {		
		Double data1 = 0d;		
		String sqlQuery = "select sum(bp) as data1 from budginterneligne where num_exec= ? and code_nature like ?";
		try {
			data1 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("data1");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data1 = 0d;
		}

		return data1;
	}
	@Override
	public double getBrNoDataBudg1(int exercice, Integer niveau, String code_nat) {		
		Double data1 = 0d;		
		//String sqlQuery = "select sum(bp) as data1 from budginterneligne where num_exec= ? and code_nature like ?";
		String sqlQuery = "select sum(b.mt_mb) mt_br,sum(bp) bi "+
				"from  BudgModificationligne b,budginterneligne l where b.num_exec=? and code_nature like ? and l.num_exec=b.num_exec and l.no_lbi=b.no_lbi and "+
				"( "+
					"(type_mb='DM' and no_mb<=? )"+
					"or ( type_mb <>'DM' and "+
					"no_mb in(select no_mb from budgmodification m where m.num_exec=? and m.no_mb_rattach<>0 and m.no_mb_rattach<=? and m.type_mb=b.type_mb) "+
				") "+
				") ";
				
		try {
			data1 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat,niveau,exercice,niveau },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("mt_br")+rst.getDouble("bi");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data1 = 0d;
		}

		return data1;
	}
	public double getNoDataBudg2(int exercice, String code_nat1, String code_nat2) {		
		Double data2 = 0d;		
		String sqlQuery = "select sum(bp) as data2 from budginterneligne where num_exec= ? and code_nature like ? and code_nature !=? ";
		try {
			data2 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat1, code_nat2 },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("data2");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data2 = 0d;
		}

		return data2;
	}
	@Override
	public double getBrNoDataBudg2(int exercice,Integer niveau, String code_nat1, String code_nat2) {		
		Double data2 = 0d;		
		//String sqlQuery = "select sum(bp) as data2 from budginterneligne where num_exec= ? and code_nature like ? and code_nature !=? ";
		String sqlQuery = "select sum(b.mt_mb) mt_br,sum(bp) bi "+
				"from  BudgModificationligne b,budginterneligne l where b.num_exec=? and code_nature like ? and code_nature !=? and l.num_exec=b.num_exec and l.no_lbi=b.no_lbi and "+
				"( "+
					"(type_mb='DM' and no_mb<=? )"+
					"or ( type_mb <>'DM' and "+
					"no_mb in(select no_mb from budgmodification m where m.num_exec=? and m.no_mb_rattach<>0 and m.no_mb_rattach<=? and m.type_mb=b.type_mb) "+
				") "+
				") ";
		try {
			data2 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat1, code_nat2,niveau,exercice,niveau },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("mt_br")+rst.getDouble("bi");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data2 = 0d;
		}

		return data2;
	}
	public double getNoDataBudg3(int exercice, String code_nat, String dep_rec) {		
		Double data3 = 0d;		
		String sqlQuery = "select sum(bp) as data3 from budginterneligne where num_exec= ? and code_nature like ? and dep_rec=?";
		try {
			data3 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat, dep_rec },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("data3");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data3 = 0d;
		}

		return data3;
	}
	public double getNoDataBudg4(int exercice, String code_nat) {		
		Double data4 = 0d;		
		String sqlQuery = "select sum(bp) as data4 from budginterneligne where num_exec= ? and code_nature =?";
		try {
			data4 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("data4");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data4 = 0d;
		}

		return data4;
	}
	@Override
	public double getBrNoDataBudg4(int exercice,Integer niveau, String code_nat) {		
		Double data4 = 0d;		
		//String sqlQuery = "select sum(bp) as data4 from budginterneligne where num_exec= ? and code_nature =?";
		String sqlQuery = "select sum(b.mt_mb) mt_br,sum(bp) bi "+
				"from  BudgModificationligne b,budginterneligne l where b.num_exec=? and code_nature = ? and l.num_exec=b.num_exec and l.no_lbi=b.no_lbi and "+
				"( "+
					"(type_mb='DM' and no_mb<=? )"+
					"or ( type_mb <>'DM' and "+
					"no_mb in(select no_mb from budgmodification m where m.num_exec=? and m.no_mb_rattach<>0 and m.no_mb_rattach<=? and m.type_mb=b.type_mb) "+
				") "+
				") ";
				
		try {
			data4 = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { exercice, code_nat,niveau,exercice,niveau },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double montant = rst.getDouble("mt_br")+rst.getDouble("bi");
							return montant;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			data4 = 0d;
		}

		return data4;
	}


@Override
public List<ComplexEntity> getEnvlpBdgVentilParOrigin(int execCp) {
	String requete = " select o.dest_grp , nom_dest_grp , o.nat_grp , "
		+ " nom_nat_grp, bp from budgofficielligne  o,budgdestinationgroupe d ,budgnaturegroupe  n "
		+ " where o.num_exec=? and o.dest_grp=d.dest_grp and o.nat_grp=n.nat_grp and o.dep_rec='R' and bp<>0 ";

List<ComplexEntity> returnedList = (List<ComplexEntity>) this.jdbcTemplate
		.query(requete, new Object[] { execCp },
				new RowMapper<ComplexEntity>() {
					public ComplexEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						ComplexEntity entity = new ComplexEntity(rs
								.getString("dest_grp"), rs
								.getString("nom_dest_grp"), rs
								.getString("nat_grp"), rs
								.getString("nom_nat_grp"));
						entity.getEntity1().setAdditionalValue(BudgetHelper.format(rs.getDouble("bp")));
						return entity;
					}
				});
return returnedList;
}

@Override
public void updateBudgetModifEtatCp(Integer exercice, String type,	Integer numero, String etatCp)
	{
	logger.info("updateBudgetModifEtatCp: exercice :"+exercice+" type: "+type+ " numero: "+numero+" etatCp: "+etatCp);
		String requete = "update BudgModification set etat_cp=? where num_exec=? and type_mb=? and no_mb=? ";
		jdbcTemplate.update(requete, new Object[] { etatCp, exercice,type,numero});
	}


	@Override
	public List<SimpleEntity> getNatureGroupeList() {

		String requete = "select distinct nat_grp, nom_nat_grp from BudgNatureGroupe where dep_rec='D' ";

		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate.query(requete, new Object[] {},
				new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("nat_grp"));
						entity.setDesignation(rs.getString("nom_nat_grp"));
						return entity;
					}
				});

		// if((returnedList==null)||(returnedList.isEmpty()))return null;
		return returnedList;
	}
	@Override
	public double getMontantBr(BudgModification selectedBudgModif, Integer noLbgCp) {
		logger.debug("getMontantBr : start : selectedBudgModif:("+selectedBudgModif.getExercice()+","+ selectedBudgModif.getType().getCode()+","+selectedBudgModif.getNumero()+"), noLbgCp="+noLbgCp);
		Double montantBr = 0d;		
		String sqlQuery = "select mnt_br from BudgModificationCpLigne where num_exec=? and code_budg=? and type_mb=? and no_mb =? and no_lbg_cp=?  ";
		try {
			montantBr = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { selectedBudgModif.getExercice(),selectedBudgModif.getCodeBudget(), selectedBudgModif.getType().getCode(),selectedBudgModif.getNumero(),noLbgCp },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double mnt = rst.getDouble("mnt_br");
							return mnt;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			//e.printStackTrace();
			montantBr = 0d;
		}
logger.debug("getMontantBr: end");
		return montantBr;
	}
	@Override
	public double getCumuleBrAvant(BudgModification selectedBudgModif, Integer noLbgCp) {
		logger.debug("getCumuleBrAvant : start : selectedBudgModif:("+selectedBudgModif.getExercice()+","+ selectedBudgModif.getType().getCode()+","+selectedBudgModif.getNumero()+"), noLbgCp="+noLbgCp);
		Double montantBr = 0d;		
		String sqlQuery = "select sum(mnt_br) as mnt from BudgModificationCpLigne where num_exec=? and code_budg=? and type_mb=? and no_mb <? and no_lbg_cp=?  ";
		try {
			montantBr = (Double) jdbcTemplate.queryForObject(sqlQuery,
					new Object[] { selectedBudgModif.getExercice(),selectedBudgModif.getCodeBudget(), selectedBudgModif.getType().getCode(),selectedBudgModif.getNumero(),noLbgCp },
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							Double mnt = rst.getDouble("mnt");
							return mnt;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			montantBr = 0d;
		}
logger.debug("getCumuleBrAvant: end");
		return montantBr;
	}
	
	@Override
	public List<ModificationCpLigne> getBrVisesList( int exercice, String codeBudg,String nat_grp ) {
		logger.debug("getBrVisesList : start : selectedBudgModif:("+exercice+","+codeBudg+"nat_grp="+nat_grp);
		String requete = " select m.no_mb , m.lib_mb , ml.mnt_br  from budgmodification m , budgcpgest g ,budgmodificationcpligne ml  where m.num_exec = ? and m.code_budg=? and m.type_mb='DM' and m.num_exec=g.num_exec and g.nat_grp=? and m.num_exec = ml.num_exec and m.no_mb=ml.no_mb and g.no_lbg_cp=ml.no_lbg_cp ";
		logger.info("requete getBrVisesList ");
		List<ModificationCpLigne> returnedList = (List<ModificationCpLigne>) this.jdbcTemplate
				.query(requete, new Object[] {exercice,codeBudg,nat_grp}, new RowMapper<ModificationCpLigne>() {
					public ModificationCpLigne mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						ModificationCpLigne entity = new ModificationCpLigne();
						
						entity.setNumero(rs.getInt("no_mb"));
						entity.setMontantBR(rs.getDouble("mnt_br"));
						entity.setLibObjetBr(rs.getString("lib_mb"));
						return entity;
					}
				});
		return returnedList;
}
	@Override
	public Boolean getEtatEditionBI(int exercice) {
		boolean editionBi=false;
		String requete="select edition_bi from budget where num_exec=?";
		editionBi=jdbcTemplate.queryForObject(requete, new Object[] {exercice}, Boolean.class);
		return editionBi;
	}
	@Override
	public void updateBudgetEditionBiFlag(int exercice, boolean biGenereted) {
		logger.info("updateBudgetEditionBiFlag: exercice: "+exercice+", biGenereted: "+biGenereted);
		String requete="update budget set edition_bi=? where num_exec=?";
		jdbcTemplate.update(requete,new Object[] {biGenereted,exercice});
		
	}
	@Override
	public List<SimpleEntity> getCompteList(Integer exercice, String codeBudget, String type) {
		String requete = "select distinct b.num_nomenc code ,c.nom_cpt nom from ctabalance b,cfgcompte c  where b. num_exec=c.num_exec and c.num_nomenc =b.num_nomenc and b.num_exec=? and b.code_budg=? and b.etat=?";
		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {exercice,codeBudget,type}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("code"));
						entity.setDesignation(rs.getString("nom"));
						return entity;
					}
				});
		return returnedList;
	}
	@Override
	public Map<String, Double> getBalance(Integer exercice, String codeBudget, String type) {
		logger.debug("getBalance: start ");
		 Map<String, Double> result=new HashMap<String, Double>();
		 String code=null;
		 Double mnt=null;
		String requete="select r.code_minefi code,sum(b.net) montant from ctabalance b,gbcpparamcf p, gbcprefcf r "
				+ "where p.etat=r.etat and p.code_minefi=r.code_minefi  and b.num_exec=p.num_exec and "
				+ "b.code_budg=p.code_budg and b.etat=p.etat and b.num_nomenc =p.num_nomenc "
				+ " and b.num_exec=? and b.code_budg=? and b.etat=? "
				+ "group by  r.code_minefi";
		
		List<Object[]> list=(List<Object[]>)this.jdbcTemplate.query(requete, new Object[] {exercice,codeBudget,type}, new RowMapper<Object[]>() {
					public Object[] mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Object[] row=new Object[2];
						row[0]=rs.getString("code");
						row[1]=rs.getDouble("montant");
						
						return row;
					}
				});
		for(Object[] row:list){
			code=(String)row[0];
			mnt=(Double) row[1];
			logger.debug("getBalance: {} --> {}",code,mnt);
			result.put(code,mnt);
		}
		return result;
	}
	@Override	
	public Boolean getEtatCF(Integer exercice, String codeBudget, String type){
		boolean etat=false;
		String requete="select "+getEtatCfColumnName(type);
				
		requete+=" from budget where num_exec=? and code_budg=?";
		etat=jdbcTemplate.queryForObject(requete, new Object[] {exercice,codeBudget}, Boolean.class);
		return etat;
		
	}
	@Override
	public void updateEtatCF(Integer exercice, String codeBudget, String type, boolean etat) {
		
		String columnName=getEtatCfColumnName(type);
		if(columnName==null)return;
		logger.info("updateEtatCF:type= {} , exercice= {} , etat= {}, columnName={} ",type,exercice,etat,columnName);
		String requete="update budget set "+columnName;
		requete+="=? where num_exec=? and code_budg=?";
		jdbcTemplate.update(requete,new Object[] {etat,exercice,codeBudget});
		
	}
	private String getEtatCfColumnName(String type){
		String name=null;
		if("B".equals(type))
			name="etat_bilan";
		else if("R".equals(type))
			name="etat_cr";
		return name;
	}
	@Override
	public Map<String, Double> getBpRecetByNatGrp(Integer exercice) {		
		final Map<String, Double> result=new HashMap<String, Double>();
		String sqlQuery = "select nat_grp, sum(bp) as recAe from budginterneenveloppe where num_exec=? and dest_grp='R' group by nat_grp";
		
			jdbcTemplate.query(sqlQuery,new Object[] { exercice},
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							result.put(rst.getString("nat_grp"), rst.getDouble("recAe"));
					
							return null;
						}
					});

		return result;
	}
	@Override
	public Map<String, Double> getBpDepAe(Integer exercice) {
		final Map<String, Double> result=new HashMap<String, Double>();
		String sqlQuery = "select nat_grp, sum(bp) as depAe from budginterneenveloppe where num_exec=? and dest_grp='D' group by nat_grp";
		
			jdbcTemplate.query(sqlQuery,new Object[] { exercice},
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							result.put(rst.getString("nat_grp"), rst.getDouble("depAe"));
					
							return null;
						}
					});

		return result;
	}
	@Override
	public Map<String, Double> getMontantDepCP(Integer exercice) {
		final Map<String, Double> result=new HashMap<String, Double>();
		String sqlQuery = "select nat_grp,sum(montant) as depCp from budgcp where num_exec_ae=? and num_exec_cp=? and dest_grp='D' group by nat_grp ";
		
			jdbcTemplate.query(sqlQuery,new Object[] { exercice,exercice},
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							result.put(rst.getString("nat_grp"), rst.getDouble("depCp"));
					
							return null;
						}
					});

		return result;
	}
	
	@Override
	public Map<String, Double> getBrDepAEByNatGrp(Integer exercice, Integer no_br) {
		final Map<String, Double> result=new HashMap<String, Double>();
		String sqlQuery = "select l.num_exec,l.nat_grp,sum(b.mt_mb) mt_br,sum(bp) bi "+
		"from  BudgModificationligne b,budginterneligne l where b.num_exec=? and l.dep_rec='D' and l.num_exec=b.num_exec and l.no_lbi=b.no_lbi and "+
		"( "+
			"(type_mb='DM' and no_mb<=? )"+
			"or ( type_mb <>'DM' and "+
			"no_mb in(select no_mb from budgmodification m where m.num_exec=? and m.no_mb_rattach<>0 and m.no_mb_rattach<=? and m.type_mb=b.type_mb) "+
		") "+
		") "+
		"group by  l.num_exec,l.nat_grp";
		
			jdbcTemplate.query(sqlQuery,new Object[] { exercice,no_br,exercice,no_br},
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							result.put(rst.getString("nat_grp"), rst.getDouble("mt_br")+rst.getDouble("bi"));
					
							return null;
						}
					});

		return result;
	}
	@Override
	public Map<String, Double> getBrDepCPByNatGrp(Integer exercice, Integer no_br) {
		final Map<String, Double> result=new HashMap<String, Double>();
		String sqlQuery = "select num_exec ,nat_grp,sum(br_encours) mt_br,sum(bi) bi  from BudgCPdest where num_exec=? and niveau<=? group by num_exec ,nat_grp";
		
			jdbcTemplate.query(sqlQuery,new Object[] { exercice,no_br},
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							result.put(rst.getString("nat_grp"), rst.getDouble("mt_br")+rst.getDouble("bi"));
					
							return null;
						}
					});

		return result;
	}
	@Override
	public Map<String, Double> getBrRecette(Integer exercice, Integer no_br) {
		final Map<String, Double> result=new HashMap<String, Double>();
		String sqlQuery = "select l.num_exec,l.nat_grp,sum(b.mt_mb) mt_br,sum(bp) bi "+
		"from  BudgModificationligne b,budginterneligne l where b.num_exec=? and l.dep_rec='R' and l.num_exec=b.num_exec and l.no_lbi=b.no_lbi and "+
		"( "+
			"(type_mb='DM' and no_mb<=? )"+
			"or ( type_mb <>'DM' and "+
			"no_mb in(select no_mb from budgmodification m where m.num_exec=? and m.no_mb_rattach<>0 and m.no_mb_rattach<=? and m.type_mb=b.type_mb) "+
		") "+
		") "+
		"group by  l.num_exec,l.nat_grp";
		
			jdbcTemplate.query(sqlQuery,new Object[] { exercice,no_br,exercice,no_br},
					new RowMapper<Object>() {
						public Object mapRow(ResultSet rst, int rowNum)
								throws SQLException {
							result.put(rst.getString("nat_grp"), rst.getDouble("mt_br")+rst.getDouble("bi"));
					
							return null;
						}
					});
			logger.debug("getBrRecette: size={}",result.size());

		return result;
	}
	@Override
	public List<Ventillation> getBrDepAEByDest(Integer exercice, Integer niveau,String dep_rec ) {
		
		String sqlQuery = "select l.num_exec,l.code_dest, d.nom_dest,l.nat_grp,sum(b.mt_mb) mt_br,sum(bp) bi "+
				"from  BudgModificationligne b,budginterneligne l, budgdestination d where b.num_exec=? and l.dep_rec=? and l.num_exec=b.num_exec and l.no_lbi=b.no_lbi "
				+ " and d.num_exec=l.num_exec and d.code_dest=l.code_dest and"+
				"( "+
					"(type_mb='DM' and no_mb<=? )"+
					"or ( type_mb <>'DM' and "+
					"no_mb in(select no_mb from budgmodification m where m.num_exec=? and m.no_mb_rattach<>0 and m.no_mb_rattach<=? and m.type_mb=b.type_mb) "+
				") "+
				") "+
				"group by  l.num_exec,l.code_dest, d.nom_dest,l.nat_grp";


						List<Ventillation> returnedList = (List<Ventillation>) this.jdbcTemplate
								.query(sqlQuery, new Object[] {exercice,dep_rec,niveau,exercice,niveau}, new RowMapper<Ventillation>() {
									public Ventillation mapRow(ResultSet rs, int rowNum)
											throws SQLException {
										Ventillation entity = new Ventillation(rs.getString("code_dest"),rs.getString("nom_dest"),rs.getString("nat_grp"),rs.getDouble("mt_br")+rs.getDouble("bi"));
										
										return entity;
									}
								});
						return returnedList;
	}
@Override	
public List<Ventillation> getBrDepCpByDest(Integer exercice, Integer niveau) {
		
	String sqlQuery = "select b.num_exec,b.code_dest, d.nom_dest  ,b.nat_grp,sum(b.br_encours) mt_br,sum(b.bi) bi "
			+ "from BudgCPdest b,budgdestination d "
			+ "where b.num_exec=d.num_exec and b.code_dest=d.code_dest and b.num_exec=? and b.niveau<=? "
			+ "group by b.num_exec,b.code_dest, d.nom_dest  ,b.nat_grp";

						List<Ventillation> returnedList = (List<Ventillation>) this.jdbcTemplate
								.query(sqlQuery, new Object[] {exercice,niveau}, new RowMapper<Ventillation>() {
									public Ventillation mapRow(ResultSet rs, int rowNum)
											throws SQLException {
										Ventillation entity = new Ventillation(rs.getString("code_dest"),rs.getString("nom_dest"),rs.getString("nat_grp"),rs.getDouble("mt_br")+rs.getDouble("bi"));
										
										return entity;
									}
								});
						return returnedList;
	}
@Override
public double getSoldeInitialJanvier(Integer exercice) {
	double solde_debit=getSolde(exercice, DebitCreditEnum.DEBIT);
	double solde_credit=getSolde(exercice, DebitCreditEnum.CREDIT);
	return solde_debit-solde_credit;
}
private double getSolde(Integer exercice,DebitCreditEnum sens) {
	Double solde = 0d;		
	String sqlQuery = "select sum(mt_ecr) solde from ctaecritureopcl5 where num_exec=? and type_ecr='F' and sens_ecr=?";
	try {
		solde = (Double) jdbcTemplate.queryForObject(sqlQuery,new Object[] { exercice,sens.getSens() },Double.class);
	} catch (EmptyResultDataAccessException e) {
		solde = 0d;
		e.printStackTrace();
	}
	if(solde==null)solde=Double.valueOf(0);
      logger.info("getSolde: {}, {} --> solde={}",exercice,sens.getSens(),solde);
	return solde;
}
@Override
public void cloturerPeriode(Integer exercice, Integer periode,String userName) {
	logger.info("cloturerPeriode : {}/{}/{} ",exercice,periode,userName);
	String update="update budgtresorerieplan set etat='C', visa_rea='VISAacc',aut_visa_rea ='"+userName+"', date_visa_rea=getdate(),aut_maj='"+userName+"',date_maj=getdate() where num_exec=? and periode=? ";
	jdbcTemplate.update(update,new Object[] {exercice,periode});	
	
	
	if(periode.equals(PeriodeEnum.December.getValue())) return;
	String requete="exec psCtaClotPeriodeMaj ?,?";
	jdbcTemplate.update(requete,new Object[] {exercice,periode});	

	
}
@Override
public void updateNextTresoreriePeriode(Integer exercice, Integer periode) {
	logger.info("createNextTresoreriePeriode : {}/{} by {} ",exercice,periode);
	int nextPeriode=periode+1;

updateCoTresoreriePeriode(exercice,nextPeriode);
updateEcartTresoreriePeriode(exercice,nextPeriode);
	
}
private void updateEcartTresoreriePeriode(Integer exercice, Integer periode) {
	String requete="update budgtresorerieplan set mt_13=mt_co - (";
	requete+=getSumMontantRequete(12, null)+")";
	requete+=" where num_exec=? and periode=? ";
	logger.debug("updateEcartTresoreriePeriode:{} ",requete);
	jdbcTemplate.update(requete,new Object[] {exercice,periode});	
	
	
}
private void updateCoTresoreriePeriode(Integer exercice, Integer periode) {
	String requete="update budgtresorerieplan set mt_co=bi+";
	requete+=getSumMontantRequete(periode, "v");
	requete+=" from plantresorerieventilco v where budgtresorerieplan.num_exec=? and budgtresorerieplan.periode=? and v.num_exec=budgtresorerieplan.num_exec and  v.no_ltr=budgtresorerieplan.no_ltr";
	logger.debug("updateCoTresoreriePeriode :{} ",requete);
	jdbcTemplate.update(requete,new Object[] {exercice,periode});	
	
}
private String getSumMontantRequete(Integer periode,String prefix){
	String r1="",r2="";
	if(periode>12) periode=12;
	for(int i=1;i<=periode;i++){
		if(i>9)break;
		if(i>1)r1+="+";
		r1+=(prefix!=null)?prefix+".":"";
		r1+="mt_0"+i;
	}
	if(periode<10){
		return r1;
	}
	for(int i=10;i<=periode;i++){
		r2+="+";
		r2+=(prefix!=null)?prefix+".":"";
		r2+="mt_"+i;
	}
	return r1+r2;
}
@Override
public List<Periode> getTresoreriePeriodeList(int exercice) {
	String sqlQuery = "select distinct periode,etat from budgtresorerieplan where num_exec=? ";
			

						List<Periode> returnedList = (List<Periode>) this.jdbcTemplate
								.query(sqlQuery, new Object[] {exercice}, new RowMapper<Periode>() {
									public Periode mapRow(ResultSet rs, int rowNum)
											throws SQLException {
										Periode p = new Periode(rs.getInt("periode"),rs.getString("etat"));
										
										return p;
									}
								});
						return returnedList;
}

@Override
public List<SimpleEntity> getDestinationTab3List(int exercice, String groupNat) {
	Object[] params=null;
	String requete = "select distinct d.code_dest ,d.nom_dest from budgdestination d,budginterneligne b where d.num_exec=b.num_exec "+
			" and d.num_exec=? and b.code_dest like d.code_dest+'%' ";

	if((groupNat!=null)&&(!groupNat.trim().isEmpty())){
		requete +="and b.nat_grp=? "	;
		params=new Object[] {exercice,groupNat};
	}
	else{
		params=new Object[] {exercice};
	}
			
			requete +=" order by d.code_dest";


	List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
			.query(requete, params, new RowMapper<SimpleEntity>() {
				public SimpleEntity mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					SimpleEntity entity = new SimpleEntity();
					entity.setCode(rs.getString("code_dest"));
					entity.setDesignation(rs.getString("nom_dest"));
					return entity;
				}
			});
	return returnedList;
}
@Override
public Map<String, Double> getBudgetPrevisionnel(Integer exercice) {
	final Map<String, Double> result=new HashMap<String, Double>();
	String sqlQuery = "select distinct nat_grp ,bp  from BudgOfficielLigne where num_exec=? and dep_rec=? order by nat_grp ";
	
		jdbcTemplate.query(sqlQuery,new Object[] { exercice,DepRecEnum.Recette.getValue()},
				new RowMapper<Object>() {
					public Object mapRow(ResultSet rst, int rowNum)
							throws SQLException {
						result.put(rst.getString("nat_grp"), rst.getDouble("bp"));
				
						return null;
					}
				});
		
		Map<String, Double> resultCp=getBudgetCP(exercice);
		result.putAll(resultCp);
	return result;
}
private Map<String, Double> getBudgetCP(Integer exercice) {
	final Map<String, Double> result=new HashMap<String, Double>();
	String sqlQuery = "select distinct nat_grp ,montant bp  from Budgcp where  num_exec_cp=? and num_exec_ae=? order by nat_grp ";
	
		jdbcTemplate.query(sqlQuery,new Object[] { exercice,exercice},
				new RowMapper<Object>() {
					public Object mapRow(ResultSet rst, int rowNum)
							throws SQLException {
						result.put(rst.getString("nat_grp"), rst.getDouble("bp"));
				
						return null;
					}
				});

	return result;
}
@Override
public Map<Integer, Double> getCoTresorerieVentille(Integer exercice, Integer periode) {
	final Map<Integer, Double> result=new HashMap<Integer, Double>();
	String sqlQuery = "select  no_ltr,(bi+";
	sqlQuery+=getSumMontantRequete(periode, null);
	sqlQuery+= ") co from plantresorerieventilco where num_exec=? ";
	logger.debug("getCoTresorerieVentille: {}/{} -> {}",exercice,periode,sqlQuery);
	
		jdbcTemplate.query(sqlQuery,new Object[] { exercice},
				new RowMapper<Object>() {
					public Object mapRow(ResultSet rst, int rowNum)
							throws SQLException {
						result.put(rst.getInt("no_ltr"), rst.getDouble("co"));
				
						return null;
					}
				});

	return result;
}
@Override
public Map<String, Double> getAeEngage(Integer exercice, Date dateFin, String tiersNeutralise) {
	logger.debug("getAeEngage: exercice={},dateFin={},tiersNeutralise={}",exercice,dateFin,tiersNeutralise);
	final Map<String, Double> result=new HashMap<String, Double>();
	String shortDate=null;
	if(dateFin!=null)	shortDate=Helper.toSimpleFormat2(dateFin);
	Object[] params=new Object[] { exercice};;
	if((!StringUtils.isBlank(tiersNeutralise))&&(dateFin!=null)){
		params=new Object[] { exercice,shortDate,tiersNeutralise};
	}
	else{
		if(!StringUtils.isBlank(tiersNeutralise))params=new Object[] { exercice,tiersNeutralise};
		if(dateFin!=null)params=new Object[] { exercice,shortDate};
	}
	logger.debug("getAeEngage : {} avant {}, tiersNeutralise {}",exercice,shortDate,tiersNeutralise);
	String sqlQuery = "select nat_grp,sum(mttot_ej) ae from depej j, budginterneligne b "+
			"where j.num_exec=? and j.num_exec=b.num_exec and j.no_lbi=b.no_lbi "+
			" and j.visa_resp in('VISAacc','VISAauto') ";

	if(dateFin!=null){
		sqlQuery+=" and j.date_resp<convert(datetime,?,103) ";
	}
	if(!StringUtils.isBlank(tiersNeutralise))
	{
		sqlQuery+=" and j.code_tiers <> ? ";

	}
	
	sqlQuery+="group by nat_grp ";
	logger.debug("getAeEngage: params={} --> sqlQuery={}",params,sqlQuery);
	
		jdbcTemplate.query(sqlQuery,params,
				new RowMapper<Object>() {
					public Object mapRow(ResultSet rst, int rowNum)
							throws SQLException {
						result.put(rst.getString("nat_grp"), rst.getDouble("ae"));
				
						return null;
					}
				});

	return result;
}
@Override
public Integer getMaxNumeroBr(Integer exercice, String dateFin) {
	Integer noBr = 0;	
	Object[] params=(dateFin!=null)?new Object[]{exercice, dateFin}:new Object[]{exercice};
	 logger.info("getMaxNumeroBr: exercice={}, dateFin={}",exercice,dateFin);
	String sqlQuery = "Select max(no_mb) no_br  from budgmodification where num_exec= ? and type_mb='DM' and etat_Cp ='Valide' ";
		if(dateFin!=null) sqlQuery+=" and date_cpt<=convert(datetime,?,103) ";
	try {
		noBr = (Integer) jdbcTemplate.queryForObject(sqlQuery,params,
				new RowMapper<Object>() {
					public Object mapRow(ResultSet rst, int rowNum)	throws SQLException {
						return rst.getInt("no_br");
						
					}
				});
	} catch (EmptyResultDataAccessException e) {
		noBr = 0;
		e.printStackTrace();
	}
      logger.info("getMaxNumeroBr: noBr={}",noBr);
	return noBr;
}
@Override
public List<Integer> getNiveauDestinationList(int exercice,DepRecEnum type) {
	
	
	Object[] params=new Object[] {exercice,type.getValue()};
	String requete = "select distinct CHAR_LENGTH(d.code_dest) niveau from budgdestination d,budginterneligne b where d.num_exec=b.num_exec "+
			" and d.num_exec=? and b.code_dest like d.code_dest+'%' and b.dep_rec=? ";
	
	List<Integer> returnedList = (List<Integer>) this.jdbcTemplate
			.query(requete, params, new RowMapper<Integer>() {
				public Integer mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					
					return rs.getInt("niveau");
					
				}
			});
	
	logger.info("getNiveauDestinationList :returnedList.size()= "+returnedList.size());	
	return returnedList;
}
@Override
public Integer getNumSeq(Integer exercice, String tableName,String typeJourn) {
	Integer cle=getCompteur(tableName, exercice,typeJourn);
	cle=cle+1;
	updateCompteur(exercice, tableName,typeJourn, cle);
	return cle;
	
}
@Override
public void updateCompteur(Integer exercice,String tableName,String typeJourn, int newCle) {
	Object[] param = new Object[4];
			//{ newCle,tableName, exercice };
	param[0] = newCle;
	int index=0;
	String query = "UPDATE CfgCompteur set cle=? WHERE 1=1  ";
	if(exercice!=null){
		index++;
		param[index]=exercice;
		query+=" and num_exec=? ";
		
	}
	if(tableName!=null){
		index++;
		param[index]=tableName;
		query+=" and nom_table=? ";
	}
	if(typeJourn!=null){
		index++;
		param[index]=typeJourn;
		query+=" and type_journ=? ";
	}

	this.jdbcTemplate.update(query,	param);
	
	
}
@Override
public Integer getCompteur(String tableName,Integer exercice,String typeJourn){
	Integer cle=0;
	Object[] param = new Object[3] ;
	int index=-1;
	String query = "SELECT cle from CfgCompteur c WHERE 1=1  ";
	if(exercice!=null){
		index++;
		param[index]=exercice;
		query+=" and num_exec=? ";
		
	}
	if(tableName!=null){
		index++;
		param[index]=tableName;
		query+=" and nom_table=? ";
	}
	if(typeJourn!=null){
		index++;
		param[index]=typeJourn;
		query+=" and type_journ=? ";
	}
			
	 cle = (Integer) this.jdbcTemplate.queryForObject(query, param,	new RowMapper<Integer>() {
				public Integer mapRow(ResultSet rst, int rowNum)
						throws SQLException {
					Integer cle = rst.getInt("cle");
					return cle;
				}
			});
	return cle;
}


@Override
public List<SimpleEntity> getGestionnairesList() {
	String requete = "select  d.code_gest,d.nom_gest from droitgest d ";

	List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
			.query(requete, new Object[] {},
					new RowMapper<SimpleEntity>() {
						public SimpleEntity mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							SimpleEntity entity = new SimpleEntity();
							entity.setCode(rs.getString("code_gest"));
							entity.setDesignation(rs.getString("nom_gest"));
							return entity;
						}
					});

	return returnedList;
}
@Override
public List<SimpleEntity> getCompteProduitList(Integer exercice, String codeDest, String codeNature, String codeProg) {
	Object[] params=new Object[4];
	params[0]=exercice;
	int i=1;
	String requete = "select distinct i.imput_htd from budginterneenveloppe e, budgenveloppeimput i where e.no_env=i.no_env and e.num_exec=i.num_exec "+
			"and e.num_exec=? ";
	if(!StringUtils.isBlank(codeDest)){
		requete+="and e.code_dest=? ";
		params[i]=codeDest;
		i++;
	}
	if(!StringUtils.isBlank(codeNature)){
		requete+="and e.code_nature=? ";
		params[i]=codeNature;
		i++;
	}
	if(!StringUtils.isBlank(codeProg)){
		requete+="and e.code_prog=? ";
		params[i]=codeProg;
		i++;
	}
	List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
			.query(requete, params,
					new RowMapper<SimpleEntity>() {
						public SimpleEntity mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							SimpleEntity entity = new SimpleEntity();
							entity.setCode(rs.getString("imput_htd"));
							
							return entity;
						}
					});

	return returnedList;
}

@Override
public List<SimpleEntity> getParamCompteList(int exercice) {
	String sqlQuery = "select distinct c1.num_nomenc,c2.nom_cpt from ctacompte c1,cfgcompte c2 where c1.num_exec=c2.num_exec and c1.num_nomenc=c2.num_nomenc and c1.num_exec=? and c1.mode_basc='D'";
			

	List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
			.query(sqlQuery, new Object[] {exercice}, new RowMapper<SimpleEntity>() {
				public SimpleEntity mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					SimpleEntity entity = new SimpleEntity();
					entity.setCode(rs.getString("num_nomenc"));
					entity.setDesignation(rs.getString("nom_cpt"));
					
					return entity;
				}
			});
	return returnedList;
}
@Override
public String[] getCtaCompteImput (Integer exercice,String imputHtd) {
	logger.debug("getCtaCompteImput: {}, {}",exercice,imputHtd);
	final String[] result=new String[2];
	String sqlQuery = "select imput_ttc, imput_tva from ctacompteimput where num_exec=? and imput_htd  =?";
	
		jdbcTemplate.query(sqlQuery,new Object[] { exercice,imputHtd},
				new RowMapper<Object>() {
					public Object mapRow(ResultSet rst, int rowNum)
							throws SQLException {
						result[0]=rst.getString("imput_ttc");
						result[1]=rst.getString("imput_tva");
						
				
						return null;
					}
				});
		logger.debug("getCtaCompteImput: {}, {}",result[0],result[1]);
	return result;
}

@Override
public void deleteGtsImport(Integer exercice, Integer periode) {
	logger.info("delete Import(exercice="+exercice+", periode"+ periode+")");
	String requete="delete from gts_import where num_exec=? and no_periode=?";
	jdbcTemplate.update(requete,new Object[] {exercice,periode});	
}
@Override
public 	GlobalSuiviStruct getGlobalSuiviRec(Boolean isTrAant, int numexec, String codeBudget, String codeDirection, String codeService,
		String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature
		,Integer noLtr, String codeTiers) {
	logger.debug("getGlobalSuiviRec: start ");
			
	final List<GlobalSuiviStruct>  result;
	
    boolean codeDirectionNull=(codeDirection==null)||(codeDirection.trim().isEmpty());
    boolean codeServiceNull=(codeService==null)||(codeService.trim().isEmpty());
  
    boolean codeDestNull=(codeDest==null)||(codeDest.trim().isEmpty());
    boolean codeProgNull=(codeProg==null)||(codeProg.trim().isEmpty());
    boolean codeNatureGrpNull=(codeNatureGrp==null)||(codeNatureGrp.trim().isEmpty());
   // boolean codeNatureNull=(codeNature==null)||(codeNature.trim().isEmpty());
    boolean noLtrNull=(noLtr==null);
    boolean codeTiersNull=(codeTiers==null)||(codeTiers.trim().isEmpty());

	 
	 String requete="select isnull(sum(r.rar_ini_ht),0) rar_ini_ht, "
			 	+"isnull(sum(r.rar_ini_ttc),0) rar_ini_ttc, "
				 +"isnull(sum(r.rar_net_ht),0) rar_net_ht, "
				 +"isnull(sum(r.rar_net_ttc),0) rar_net_ttc, "
				 +"isnull(sum(r.rec_ht),0) rec_ht, "
				 +"isnull(sum(r.solde_ttc),0) solde_ttc, "
				 +"isnull(sum(r.onb_ht),0) onb_ht, "
				 +"isnull(sum(r.onb_ttc),0) onb_ttc, "
				 +"isnull(sum(r.ar_ttc),0) ar_ttc, "
				 +"isnull(sum(r.ar_ht),0) ar_ht "
				 +"from rectitrerecouvexec r "
				 +"where num_exec_rec=? "	;			 		
				
	
	 if(isTrAant!=null)		 
	 requete+=isTrAant?" and r.exec_orig <"+numexec:" and r.exec_orig="+numexec;
	 if(!codeDirectionNull) requete+=" and r.code_direction ='"+codeDirection+"'";
	 if(!codeServiceNull) requete+=" and r.code_service ='"+codeService+"'" ;

	 if(!codeDestNull) requete+=" and r.code_ori ='"+codeDest+"'";
	 if(!codeProgNull) requete+=" and r.code_prog='"+codeProg+"'" ;
	 if(!codeNatureGrpNull) requete+=" and r.nat_grp='"+codeNatureGrp+"'" ;
	 if(!noLtrNull) requete+=" and r.no_ltr ="+noLtr ;
	 if(!codeTiersNull) requete+=" and r.tiers like '%"+codeTiers+"%'" ;
	 logger.debug("getGlobalSuiviRec: requete: {}",requete);
	
	 result=(List<GlobalSuiviStruct> )this.jdbcTemplate.query(requete, new Object[] {numexec}, new RowMapper<GlobalSuiviStruct>() {
				public GlobalSuiviStruct mapRow(ResultSet rs, int rowNum)
						throws SQLException {					
					
					GlobalSuiviStruct suiviStruct = new GlobalSuiviStruct();
					suiviStruct.setInitial(new MontantHtTtc(rs.getDouble("rar_ini_ht"),	rs.getDouble("rar_ini_ttc")));
					suiviStruct.setRar(new MontantHtTtc(rs.getDouble("rar_net_ht"),	rs.getDouble("rar_net_ttc")));					
					suiviStruct.setSolde(new MontantHtTtc(0d,rs.getDouble("solde_ttc")));
					suiviStruct.setRec_ht(new BigDecimal(rs.getDouble("rec_ht")));
					suiviStruct.setOnb(new MontantHtTtc(rs.getDouble("onb_ht"),rs.getDouble("onb_ttc")));
					suiviStruct.setAr(new MontantHtTtc(rs.getDouble("ar_ht"),rs.getDouble("ar_ttc")));
					return suiviStruct;
					
				}
			});
	logger.debug("getGlobalSuiviRec: end :  result zise={}",result==null?0:result.size());
	if(result==null)return null;
			if(result.isEmpty())return null;
	return result.get(0);
}
@Override
public Integer getMaxNoEcriture(Integer exercice) {
	Integer maxNoEcr=0;
	String requete="select max(no_ecr) from ctaEcriture where num_exec=? and type_ecr in ('U','A')";
	maxNoEcr=jdbcTemplate.queryForObject(requete, new Object[] {exercice}, Integer.class);
	return maxNoEcr;
}

@Override
public 	List<SuiviRecetStruct> getDetailSuiviRec(Boolean isTrAant, int numexec, String codeBudget, String codeDirection, String codeService,
		String codeFongDest, String codeDest, String codeProg, String codeNatureGrp, String codeNature
		,Integer noLtr, String codeTiers) {
	logger.debug("getDetailSuiviRec: start ");
			
	List<SuiviRecetStruct> result=null;
	
    boolean codeDirectionNull=(codeDirection==null)||(codeDirection.trim().isEmpty());
    boolean codeServiceNull=(codeService==null)||(codeService.trim().isEmpty());
  
    boolean codeDestNull=(codeDest==null)||(codeDest.trim().isEmpty());
    boolean codeProgNull=(codeProg==null)||(codeProg.trim().isEmpty());
    boolean codeNatureGrpNull=(codeNatureGrp==null)||(codeNatureGrp.trim().isEmpty());
  //  boolean codeNatureNull=(codeNature==null)||(codeNature.trim().isEmpty());
    boolean noLtrNull=(noLtr==null);
    boolean codeTiersNull=(codeTiers==null)||(codeTiers.trim().isEmpty());
    
	 String requete="select r.exec_orig, r.no_titrec,r.tiers,r.objet,r.code_ori, "
	 		+ "r.code_service, r.nat_grp , r.code_prog,"
	 		+ "isnull(r.rar_ini_ht,0) rar_ini_ht, "
	 		+ "isnull(r.rar_ini_ttc,0) rar_ini_ttc, "
			 +"isnull(r.rar_net_ht,0) rar_net_ht, "
			 +"isnull(r.rar_net_ttc,0) rar_net_ttc, "
			 +"isnull(r.rec_ht,0) rec_ht, "
			 +"isnull(r.solde_ttc,0) solde_ttc, "
			 
			 +"isnull(r.ar_ht,0) ar_ht, "
			 +"isnull(r.ar_ttc,0) ar_ttc, "
			 +"isnull(r.onb_ht,0) onb_ht, "
			 +"isnull(r.onb_ttc,0) onb_ttc "
			 
			 +"from rectitrerecouvexec r  "
			 +"where num_exec_rec=? "	;	
			
			 
	 if(isTrAant!=null)		 
		 requete+=isTrAant?" and r.exec_orig <"+numexec:" and r.exec_orig="+numexec;
	 if(!codeDirectionNull) requete+=" and r.code_direction ='"+codeDirection+"'";
	 if(!codeServiceNull) requete+=" and r.code_service ='"+codeService+"'" ;

	 if(!codeDestNull) requete+=" and r.code_ori ='"+codeDest+"'";
	 if(!codeProgNull) requete+=" and r.code_prog='"+codeProg+"'" ;
	 if(!codeNatureGrpNull) requete+=" and r.nat_grp='"+codeNatureGrp+"'" ;
	 if(!noLtrNull) requete+=" and r.no_ltr ="+noLtr ;	
	 if(!codeTiersNull) requete+=" and r.tiers like '%"+codeTiers+"%'" ;
	 logger.debug("getGlobalSuiviRec: requete: {}",requete);		
		
	 result = (List<SuiviRecetStruct>)this.jdbcTemplate
			 .query(requete, new Object[] {numexec}, new RowMapper<SuiviRecetStruct>() {
				public SuiviRecetStruct mapRow(ResultSet rs, int rowNum)
						throws SQLException {	
					
					SuiviRecetStruct suivi= new SuiviRecetStruct(rs.getInt("exec_orig"),
							rs.getInt("no_titrec"),rs.getString("tiers"),
							rs.getString("objet"),rs.getString("code_ori"),
							rs.getString("code_service"),
							rs.getString("nat_grp"),
							rs.getString("code_prog"),
							new MontantHtTtc(rs.getDouble("rar_ini_ht"), rs.getDouble("rar_ini_ttc")),
							new MontantHtTtc(rs.getDouble("rar_net_ht"), rs.getDouble("rar_net_ttc")),
							new MontantHtTtc(0d,rs.getDouble("solde_ttc")));
					suivi.setRec_ht(new BigDecimal(rs.getDouble("rec_ht")));
					suivi.setAr(new MontantHtTtc(rs.getDouble("ar_ht"), rs.getDouble("ar_ttc")));
					suivi.setOnb(new MontantHtTtc(rs.getDouble("onb_ht"), rs.getDouble("onb_ttc")));	

					return suivi;
					
				}
			});
	logger.debug("getDetailSuiviRec: end :  result zise={}",result==null?0:result.size());
	return result;
}
@Override
public void refreshSimulationData(Integer exercice, Integer periode) {
	String requete="exec psGbcpSimulationPLT ?,?";
	jdbcTemplate.update(requete,new Object[] {exercice,periode});	
	
}
@Override
public List<Ecriture> getSimulationTempData(final Integer exercice, final Integer periode) {
	String sqlQuery = "select * from gbcpsimulationTemp where num_exec=? and periode=? order by no_ltr";
	

	List<Ecriture> returnedList = (List<Ecriture>) this.jdbcTemplate
			.query(sqlQuery, new Object[] {exercice,periode}, new RowMapper<Ecriture>() {
				public Ecriture mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Ecriture ecr = new Ecriture(exercice,periode);
					ecr.setNumero(rs.getInt("no_ecr"));
					ecr.setType(rs.getString("type_ecr"));
					ecr.setTypePiece(rs.getString("type_piece"));
					ecr.setNumeroPiece(rs.getInt("no_piece"));
					ecr.setExerciceOrigine(rs.getInt("exec_orig"));
					ecr.setNumeroLigne1(rs.getInt("no_ltr"));
					ecr.setMontantLigne1(rs.getDouble("mt_ltr"));
					ecr.setNumeroLigne2(rs.getInt("no_ltr_regul"));
					ecr.setMontantLigne2(rs.getDouble("mt_ltr_regul"));
					ecr.setMontant(rs.getDouble("mt_ecr"));
					ecr.setNumeroOP(rs.getInt("no_op"));
					ecr.setLibelle(rs.getString("lib_ecr"));
					ecr.setSens(DebitCreditEnum.parse(rs.getString("sens_ecr")));
					ecr.setTiers(rs.getString("tiers"));
				//	ecr.setUpdated(rs.getBoolean("modifie"));
					
					return ecr;
				}
			});
	return returnedList;
}
@Override
public List<SimpleEntity> getRecetteByOrigine(Integer numexec, String codeBudget, String codeDirection,
		String codeService, String codeFongDest, String codeDest, String codeProg, String codeNatureGrp,
		String codeNature, Integer noLtr, String codeTiers, Integer niveau) {
	List<SimpleEntity> result=null;
	logger.debug("getRecetteByOrigine : niveau={}",niveau);
	
    boolean codeDirectionNull=(codeDirection==null)||(codeDirection.trim().isEmpty());
    boolean codeServiceNull=(codeService==null)||(codeService.trim().isEmpty());
  
    boolean codeDestNull=(codeDest==null)||(codeDest.trim().isEmpty());
    boolean codeProgNull=(codeProg==null)||(codeProg.trim().isEmpty());
    boolean codeNatureGrpNull=(codeNatureGrp==null)||(codeNatureGrp.trim().isEmpty());

    boolean noLtrNull=(noLtr==null);
    boolean codeTiersNull=(codeTiers==null)||(codeTiers.trim().isEmpty());
    
	 String requete="select distinct nat_grp,substring(code_ori,1,?) origine,sum(rec_ht) rec_ht " 
			 +"from rectitrerecouvexec r  "
			 +"where num_exec_rec=? "	;	
			
	 if(!codeDirectionNull) requete+=" and r.code_direction ='"+codeDirection+"'";
	 if(!codeServiceNull) requete+=" and r.code_service ='"+codeService+"'" ;

	 if(!codeDestNull) requete+=" and r.code_ori ='"+codeDest+"'";
	 if(!codeProgNull) requete+=" and r.code_prog='"+codeProg+"'" ;
	 if(!codeNatureGrpNull) requete+=" and r.nat_grp='"+codeNatureGrp+"'" ;
	 if(!noLtrNull) requete+=" and r.no_ltr ="+noLtr ;	
	 if(!codeTiersNull) requete+=" and r.tiers like '%"+codeTiers+"%'" ;
	 
	 requete+="group by nat_grp,substring(code_ori,1,?)  ";
	 logger.debug("getRecetteByOrigine: requete: {}",requete);		
		
	 result = (List<SimpleEntity>)this.jdbcTemplate
			 .query(requete, new Object[] {niveau,numexec,niveau}, new RowMapper<SimpleEntity>() {
				public SimpleEntity mapRow(ResultSet rs, int rowNum)
						throws SQLException {	
					SimpleEntity item=new SimpleEntity();
					item.setCode(rs.getString("origine"));
					item.setDesignation(rs.getString("nat_grp"));					
					item.getAdditionalValues().put("rec_ht", rs.getDouble("rec_ht"));

					return item;
					
				}
			});
	logger.debug("getRecetteByOrigine: end :  result zise={}",result==null?0:result.size());
	return result;
}
@Override
public void updateSimulationTemp(Ecriture ecriture) {
	logger.debug("updateEcriture : num_exec={}, no_ecr={}, no_op={}, periode={}",ecriture.getExercice(),ecriture.getNumero(),ecriture.getNumeroOP(),ecriture.getPeriode());
	String requete ="update gbcpsimulationTemp set no_ltr=?,no_ltr_regul=?,mt_ltr=?,mt_ltr_regul=?,modifie=1 where num_exec=? and periode=? and no_ecr=? and no_op=?";
	
	jdbcTemplate.update(requete,new Object[] {ecriture.getNumeroLigne1(),ecriture.getNumeroLigne2(),ecriture.getMontantLigne1(),ecriture.getMontantLigne2(),
			ecriture.getExercice(),ecriture.getPeriode(),ecriture.getNumero(),ecriture.getNumeroOP()});	
	
}
@Override
@Deprecated
public void loadTab6FromCtaBalance(Integer exercice,Tab6 tab6) {
	CRP crp=tab6.getCrp();
	crp.getDepPersCp().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '64%'"));
	crp.getSubvEtat().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '7411%'"));
	crp.getAutresSubv().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '74%' and num_nomenc not like '7411%'"));
	crp.getAutresProduit().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '7%' and num_nomenc not like '74%'"));
	crp.getAutreFoncPersonnel().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '6%'"));
	
	CAF cafp=tab6.getCaf();
	cafp.getData1().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '68%'"));
	cafp.getData2().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '78%'"));
	cafp.getData3().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '656%'"));
	cafp.getData4().setMontantDouble(getCtaBalanceValue(exercice,"num_nomenc like '756%'"));

	
}

public double getCtaBalanceValue(Integer exercice, String condition) {
	Double net=null;
	String requete="select sum(net) from ctabalance where num_exec=? and "+condition;
	
	net=jdbcTemplate.queryForObject(requete, new Object[] {exercice}, Double.class);
	if(net==null) net=Double.valueOf(0);
	logger.debug("getCtaBalanceValue: {} => {}",net,requete);
	return net;
}
private double getCtaEcritureOpValue(Integer exercice,String type, String condition) {
	Double solde = 0d;		
	String sqlQuery = "select sum(mt_ecr) solde from ctaecritureop where num_exec=? and type_ecr=? and "+condition;
	try {
		solde = (Double) jdbcTemplate.queryForObject(sqlQuery,new Object[] { exercice,type },Double.class);
	} catch (EmptyResultDataAccessException e) {
		solde = 0d;
		e.printStackTrace();
	}
	if(solde==null)solde=Double.valueOf(0);
      logger.info("getCtaEcritureOpValue: {}/{}:  {} => {}",exercice,type,solde,sqlQuery);
	return solde;
}
@Override
public double getFinaActifEtat(Integer exercice){
	double m1=getCtaEcritureOpValue(exercice,"U","num_nomenc like '10%'");
	double m2=getCtaEcritureOpValue(exercice,"A","num_nomenc like '10%'");
	return m1-m2;
}
@Override
public double getFinaActifTiers(Integer exercice){
	double m1=getCtaEcritureOpValue(exercice,"U","num_nomenc like '13%'");
	double m2=getCtaEcritureOpValue(exercice,"A","num_nomenc like '13%'");
	return m1-m2;
}
@Override
public  Double getAeEngage(Integer exercice,Integer noLtr, Date dateFin, String tiersNeutralise) {
	logger.debug("getAeEngage: exercice={},dateFin={},tiersNeutralise={}",exercice,dateFin,tiersNeutralise);
	 Double result=0d;
	String shortDate=null;
	if(dateFin!=null)	shortDate=Helper.toSimpleFormat2(dateFin);
	Object[] params=new Object[] { exercice,noLtr};;
	if((!StringUtils.isBlank(tiersNeutralise))&&(dateFin!=null)){
		params=new Object[] { exercice,noLtr,shortDate,tiersNeutralise};
	}
	else{
		if(!StringUtils.isBlank(tiersNeutralise))params=new Object[] { exercice,noLtr,tiersNeutralise};
		if(dateFin!=null)params=new Object[] { exercice,noLtr,shortDate};
	}
	logger.debug("getAeEngage : {} avant {}, tiersNeutralise {}",exercice,shortDate,tiersNeutralise);
	String sqlQuery = "select sum(mttot_ej) ae from depej j, budginterneligne b "+
			"where j.num_exec=? and j.num_exec=b.num_exec and j.no_lbi=b.no_lbi "+
			" and j.visa_resp in('VISAacc','VISAauto') "+
			" and b.no_ltr=? " ;

	if(dateFin!=null){
		sqlQuery+=" and j.date_resp<convert(datetime,?,103) ";
	}
	if(!StringUtils.isBlank(tiersNeutralise))
	{
		sqlQuery+=" and j.code_tiers <> ? ";

	}
	
	logger.debug("getAeEngage: params={} --> sqlQuery={}",params,sqlQuery);
	
	result = (Double) jdbcTemplate.queryForObject(sqlQuery,params,Double.class);

	return (result!=null)?result:Double.valueOf(0);
}

@Override
public double getTotCpvint(Integer exercice, String nat_grp) {
	Double cpv = 0d;		
	Object[] params=null;
	params=new Object[] { exercice,nat_grp};
	String sqlQuery = "exec psbudgsuiviaecp 2,?,?"; 
	cpv = (Double) jdbcTemplate.queryForObject(sqlQuery,params,Double.class);

	return (cpv!=null)?cpv:Double.valueOf(0);
}
@Override
public List<String> getDateEchangeList() {
	String sqlQuery ="exec psRegltBordDateEchangeListe 'Date echeance'";
	List<String> result=jdbcTemplate.query(sqlQuery, new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	});
	return result;
}
@Override
public List<String> getDateReglementList() {
//	String sqlQuery ="exec psRegltBordDateReglListe 1, 'Date reglement'";
	String sqlQuery ="exec psRegltBordDateReglListe 1, ''";
	List<String> result=jdbcTemplate.query(sqlQuery, new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	});
	return result;
}
@Override
public List<String> getPaysByProtocole(String modePaie) {
	String sqlQuery ="select distinct p.pays from ctaprotocolePays p, ctamodepaie m where m.protocole=p.protocole and m.mode_paie=?" ;
	List<String> result=jdbcTemplate.query(sqlQuery,new String[] {modePaie}, new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	});
	return result;
}
@Override
public Integer getLastNoVague() {
	Integer maxNoVague=0;
	String requete="select max(no_vague) from ctaRemb";
	maxNoVague=jdbcTemplate.queryForObject(requete, new Object[] {}, Integer.class);
	return maxNoVague;
}

	@Override
	public List<Integer> verifyEi(Integer exercice, List<Integer> inputEiList) {
		if(CollectionUtils.isEmpty(inputEiList)) return new ArrayList<Integer>();
		String inSql = String.join(",", Collections.nCopies(inputEiList.size(), "?"));
		String sqlQuery = String.format("select distinct no_ei from depEi where num_exec=%d and no_ei in(%s)", exercice,
				inSql);
		logger.debug("verifyEi: sqlQuery -> {} : {}", sqlQuery, inputEiList);
		List<Integer> returnedList = jdbcTemplate.queryForList(sqlQuery, inputEiList.toArray(), Integer.class);
		logger.debug("verifyEi: result -> {}", returnedList);
		return returnedList;
	}

	@Override
	public List<LbData> getLbData(Integer exercice, List<Integer> inputLbiList) {
		if(CollectionUtils.isEmpty(inputLbiList)) return new ArrayList<LbData>();
		
		String inSql = String.join(",", Collections.nCopies(inputLbiList.size(), "?"));
		String sqlQuery = String.format(
				"select distinct no_lbi,code_dest ,code_nature, code_prog, code_service,code_direction  from budgInterneLigne where num_exec=%d and no_lbi in(%s)",
				exercice, inSql);
		logger.debug("getLbData: sqlQuery -> {} : {}", sqlQuery, inputLbiList);
		return jdbcTemplate.query(sqlQuery, inputLbiList.toArray(),
				(rs, rowNum) -> new LbData(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6)));

	}
	@Override
	public Integer getMeta4DaiNoLbiStr(Integer exercice) {
		Integer noLbiStr=null;
		String requete="select no_lbi from PaieMeta4DAILBC where num_exec=?";
		try {
		noLbiStr=jdbcTemplate.queryForObject(requete, new Object[] {exercice}, Integer.class);
		}
		catch(EmptyResultDataAccessException e) {
			logger.error("getMeta4DaiNoLbiStr: NoLbiStr not found for {}",exercice);
		}
		return noLbiStr;
	}
	@Override
	public Integer requirementCheck(String dbOjectName, String dbOjectType) {
		Integer count=0;
		String requete="select count(*) from dbo.sysobjects  where name = ? and type = ?";
		count=jdbcTemplate.queryForObject(requete, new Object[] {dbOjectName,dbOjectType}, Integer.class);
		return count;
	}
	@Override
	public List<String> getCodeGestList(Integer exercice, String groupNat) {
		String requete="select distinct code_gest from budggestligne where num_exec=? and code_gest!='RESERVE' and nat_grp=?";
		List<String> result=jdbcTemplate.queryForList(requete, new Object[] {exercice,groupNat}, String.class);
		return result;
	}
	@Override
	public Integer getNumeroEo(Integer exercice, String groupNat) {
		Integer result = null;
		String requete="select no_eo from depeo where num_exec=? and no_env in(select no_env from budgofficielligne where num_exec=? and nat_grp=?)";
		try {
		result=jdbcTemplate.queryForObject(requete, new Object[] {exercice,exercice,groupNat}, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error("getNumeroEo: error: EmptyResultDataAccessException");
		}
		return result;
	}
	@Override
	public String getcodePRM(Integer exercice, String codeGest) {
		String result = null;
		String requete="select code_prm from prmgest where num_exec=? and code_gest=?";
		try {
		 result=jdbcTemplate.queryForObject(requete, new Object[] {exercice,codeGest}, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error("getcodePRM: error: EmptyResultDataAccessException");
		}
		return result;
	}

	@Override
	public String getModePaie(String countryCode) {
		List<String> result = null;
		String requete = "select mode_paie from ctamodepaie where protocole in"
				+ "(select protocole from ctaprotocolepays where pays=?)" + "and mode_paie not like 'RB%'";
		try {
			result = jdbcTemplate.queryForList(requete, new Object[] { countryCode }, String.class);
		} catch (EmptyResultDataAccessException e) {
			//logger.error("getModePaie: error: EmptyResultDataAccessException");
		}
		return (result!=null && !result.isEmpty())?result.get(0):null;
	}
	@Override
	public List<String> verifyEtudiant(List<String> inputKeyList) {
		if(CollectionUtils.isEmpty(inputKeyList)) return new ArrayList<String>();
		List<String> returnedList =null;
		try {
		//String inSql = String.join(",", Collections.nCopies(inputKeyList.size(), "?"));
			String inSql = String.join("','", inputKeyList);
		String sqlQuery = String.format("select distinct matricule from etudiant where  matricule in('%s') and emailstatus <> 'NON'", inSql);
		logger.debug("verifyEtudiant: sqlQuery -> {} ", sqlQuery);
	//	returnedList = jdbcTemplate.queryForList(sqlQuery, inputKeyList.toArray(), String.class);
		returnedList = jdbcTemplate.queryForList(sqlQuery, String.class);
		logger.debug("verifyEtudiant: result -> {}", returnedList);
		} catch (EmptyResultDataAccessException e) {
			returnedList=new ArrayList<String>();
			logger.error("verifyEtudiant: error: EmptyResultDataAccessException");
		}
		return returnedList;
	}
	
	@Override
	public List<SimpleEntity> getCompteChargeList(Integer exercice, Integer noEnv) {
		String sqlQuery = "select distinct i.imput_htd from budgenveloppeimput i where i.num_exec=? and i.no_env=? ";
			
		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(sqlQuery, new Object[] {exercice,noEnv},
						new RowMapper<SimpleEntity>() {
							public SimpleEntity mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								SimpleEntity entity = new SimpleEntity();
								entity.setCode(rs.getString("imput_htd"));
								
								return entity;
							}
						});

		return returnedList;
	}
	@Override
	public List<EtudiantPj> getEtudiantPjList(String matricule) {
		String sqlQuery = "select file_name,file_data from EtudiantPj where matricule=? ";
		List<EtudiantPj> returnedList = (List<EtudiantPj>) this.jdbcTemplate
				.query(sqlQuery, new Object[] {matricule},
						new RowMapper<EtudiantPj>() {
							public EtudiantPj mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								EtudiantPj entity = new EtudiantPj();
								entity.setMatricule(matricule);
								entity.setFileName(rs.getString(1));
								entity.setFileData(rs.getBytes(2));
								
								return entity;
							}
						});

		return returnedList;
		
	}
	@Override
	public void deleteTiers(String codeTiers, boolean deleteAdresse, boolean deleteIban) {
		if(StringUtils.isBlank(codeTiers))return;
		
		logger.info("deleteTiers codeTiers={} : start ",codeTiers);
		String deleteTiersSql=String.format("delete from tiers where code_tiers='%s'",codeTiers);
		String deleteAdresseSql=String.format("delete from tiersAdresse where code_tiers='%s'",codeTiers);
		String deleteIbanSql=String.format("delete from tiersIban where code_tiers='%s'",codeTiers);
		List<String> sqlList=new ArrayList<String>();
		if(deleteAdresse) sqlList.add(deleteAdresseSql);
		if(deleteIban)sqlList.add(deleteIbanSql);
		sqlList.add(deleteTiersSql);
		String[] sqlArray=sqlList.toArray(new String[0]);
		logger.debug("deleteTiers query: {}" ,(Object[])sqlArray);
		
		
		int[] result=jdbcTemplate.batchUpdate(sqlArray);
		
		logger.info("deleteTiers result={} : end ",result);
		
	}
	@Override
	public List<SimpleEntity> getCfgCompteList(Integer exercice) {
		String requete = "select distinct num_nomenc code ,nom_cpt nom  from cfgcompte where num_exec=? and substring(num_nomenc, 1, 1) not in('4','5') order by num_nomenc";
		List<SimpleEntity> returnedList = (List<SimpleEntity>) this.jdbcTemplate
				.query(requete, new Object[] {exercice}, new RowMapper<SimpleEntity>() {
					public SimpleEntity mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						SimpleEntity entity = new SimpleEntity();
						entity.setCode(rs.getString("code"));
						entity.setDesignation(rs.getString("nom"));
						return entity;
					}
				});
		return returnedList;
	}
	@Override
	public LbData loadLb(Integer exercice, String codeBudget, LbData lbData) {
		
		String sqlQuery = "select distinct no_lbi,code_dest ,code_nature, code_prog, code_service,code_direction  "
				+ "from budgInterneLigne "
				+ "where num_exec=? and code_budg=? and code_dest=? and code_nature=? and "
				+ "code_prog=? and code_service=?";
		if(lbData.getDirection()!=null) {
			sqlQuery=String.format(sqlQuery + " and code_direction='%s'", lbData.getDirection());
		}
				
		logger.debug("loadLb: sqlQuery -> {} : input {}", sqlQuery, lbData);
		List<LbData> list=jdbcTemplate.query(sqlQuery,
				new Object[] {exercice,codeBudget,lbData.getDestination(),lbData.getNature(),
						lbData.getProgramme(),lbData.getService()},
				(rs, rowNum) -> new LbData(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6)));
		LbData result =list.isEmpty()?null:list.get(0);
		logger.debug("loadLb: result : {}", result);
		return result;
	}
	@Override
	public void addMeta4DaiNoLbiStr(Integer exercice, Integer numeroLBC) {
		String sqlQuery="insert into PaieMeta4DAILBC select ?, ?";
		jdbcTemplate.update(sqlQuery, exercice,numeroLBC);
		
	}
	@Override
	public String getImputHtd(Integer exercice, Integer noLbi) {
		String sqlQuery = "select distinct i.imput_htd from budginterneligne e, budgenveloppeimput i "
				+ "where e.no_env=i.no_env and e.num_exec=i.num_exec  and e.num_exec=? and e.no_lbi=?";
			
		List<String> returnedList = (List<String>) this.jdbcTemplate
				.query(sqlQuery, new Object[] {exercice,noLbi},
						(rs, rowNum) -> rs.getString("imput_htd"));
					

		return returnedList.isEmpty()?null:returnedList.get(0);
	}
	
}
