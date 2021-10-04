package fr.symphonie.budget.core.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.symphonie.budget.core.dao.IGestionTiersDao;
import fr.symphonie.budget.core.dao.IJdbcHelper;
import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.FormeSociale;
import fr.symphonie.cpwin.model.ModePaiement;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.TiersDocument;
import fr.symphonie.util.model.SimpleEntity;
import fr.symphonie.util.model.User;

@Service
public class GestionTiersService implements IGestionTiersService {

	private IGestionTiersDao gestionTiers;
	
	private IJdbcHelper jdbcHelper;

	public IGestionTiersDao getGestionTiers() {
		return gestionTiers;
	}

	@Inject
	public void setGestionTiers(IGestionTiersDao gestionTiers) {
		this.gestionTiers = gestionTiers;
	}
	@Inject
	public void setJdbcHelper(IJdbcHelper jdbcHelper) {
		this.jdbcHelper = jdbcHelper;
	}
	
	@Override
	@Transactional
	public void insert(Object entity){
		gestionTiers.insert(entity);
	}
	
	@Override
	@Transactional
	public Object update(Object entity){
		return gestionTiers.update(entity);
	}
	
	@Override
	@Transactional
	public void remove(Object entity) {
		gestionTiers.remove(entity);
	}

	@Override
	public List<Tiers> searchTiers(String codeTiers, String numSiren,
			String etat, Date dateDebut, Date dateFin) {
		return gestionTiers.searchTiers(codeTiers, numSiren, etat, dateDebut,
				dateFin);
	}
	
	@Override
	public Tiers getTiersWithIban(String codeTiers) {
		return gestionTiers.getTiersWithIban(codeTiers);
	}
	
	@Override
	public List<TiersDocument> searchTiersDoc(String codeTiers) {
		return gestionTiers.searchTiersDoc(codeTiers);
	}
	
	@Override
	@Transactional
	public void deleteDocument(int id){
		gestionTiers.deleteDocument(id);
	}
	
	@Override
	@Transactional
	public void deleteAdresse(String codeTiers, int ordre){
		gestionTiers.deleteAdresse( codeTiers,  ordre);
	}
	
	@Override
	public  List<ModePaiement> getAllModePaiement()
	{
		return gestionTiers.getAllModePaiement();
	}
	
	@Override
	public  List<fr.symphonie.cpwin.model.Service> getService(String chaine, boolean searchAtBegin)
	{
		return gestionTiers.getService( chaine,  searchAtBegin);
	}
	
	@Override
	public  List<FormeSociale> getAllFormeSociale()
	{
		return gestionTiers.getAllFormeSociale();
	}
	
	@Override
	public fr.symphonie.cpwin.model.Service getService(String code)
	{
		return gestionTiers.getService(code);
	}
	@Override
	public fr.symphonie.cpwin.model.Service  getTiersService(String codeTiers)
	{
		return gestionTiers.getTiersService(codeTiers);
	}
	
	@Override
	public 	User getUser(String login)
	{
		return gestionTiers.getUser(login);
	}
	
	@Override
	public  Tiers getTiersWoIban(String codeTiers)
	{
		return gestionTiers.getTiersWoIban(codeTiers);
	}
	@Override
	public List<SimpleEntity> getListFormeSocial()
	{
		return jdbcHelper.getListFormeSocial();
	}

//	@Override
//	public String getTiersDematPath() {
//		return jdbcHelper.getCfgconfigValue(Constant.tiersDematPath,null);
//	}
	@Override
	public List<Tiers> findLightTiersList(boolean searchAtBegin, String formatedPrefix) {

		return gestionTiers.findLightTiersList(searchAtBegin, formatedPrefix);
	}
	public Tiers findTiers(String codeTiers) {
		return gestionTiers.findTiers(codeTiers);
		
	}

	@Override
	public Adresse getAdressCpwinTiers(int noAdress, String codeTiers) {
		return gestionTiers.getAdressCpwinTiers(noAdress,codeTiers);
	}
}
