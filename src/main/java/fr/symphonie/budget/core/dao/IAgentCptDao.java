package fr.symphonie.budget.core.dao;

import java.util.Date;
import java.util.List;

import fr.symphonie.cpwin.model.Banque;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.pk.BanquePK;

public interface IAgentCptDao {

	void insert(Object entity);

	void update(Object entity);

	List<Banque> searchBanquesAbsorbes(String codePays, String codeEtab, Date dateFinDiffusion, boolean notNullDateFD);

	int getOpenedIban(BanquePK pk);

	List<Tiers> getTiers(BanquePK banquePK);
	List<String> loadPaysList();

	List<Iban> getTiersIbanList(BanquePK pk, String codeTiers);
	
	int getTiersOpenedIban(BanquePK pk, String codeTiers);
}
