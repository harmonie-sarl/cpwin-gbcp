package fr.symphonie.budget.core.dao;

import java.util.Date;
import java.util.List;

import fr.symphonie.cpwin.model.Adresse;
import fr.symphonie.cpwin.model.FormeSociale;
import fr.symphonie.cpwin.model.ModePaiement;
import fr.symphonie.cpwin.model.Service;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.TiersDocument;
import fr.symphonie.util.model.User;


/**
 * 
 * @author JEDIDI SOUHAIB(HARMONIE)
 *
 */

public interface IGestionTiersDao {

	void insert(Object entity);
	Object update(Object entity);
	void remove(Object entity);

	List<Tiers> searchTiers(String codeTiers, String numSiren, String etat,
			Date dateDebut, Date dateFin);
	Tiers getTiersWithIban(String codeTiers);
	List<TiersDocument> searchTiersDoc(String codeTiers);
	void deleteDocument(int id);
	public void deleteAdresse(String codeTiers, int ordre) ;
	List<ModePaiement> getAllModePaiement();
	List<Service> getService(String chaine, boolean searchAtBegin);
	List<FormeSociale> getAllFormeSociale();
	Service getService(String code);
	Service getTiersService(String codeTiers);
	User getUser(String login);
	 Tiers getTiersWoIban(String codeTiers);
	 List<Tiers> findLightTiersList(boolean searchAtBegin, String formatedPrefix);
	Tiers findTiers(String codeTiers);
	Adresse getAdressCpwinTiers(int noAdress, String codeTiers);

}
