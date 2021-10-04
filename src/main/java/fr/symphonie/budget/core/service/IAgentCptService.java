package fr.symphonie.budget.core.service;

import java.util.Date;
import java.util.List;

import fr.symphonie.cpwin.model.Banque;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.pk.BanquePK;

public interface IAgentCptService {

	void addBanque(Banque banque);
	void updateBanque(Banque banque);
	List<Banque> findBanquesAbsorbes(String codePays, String codeEtab, Date dateFinDiffusion, boolean onlyWithOpenedIban);
	public int getOpenedIban(BanquePK pk);
	public List<Iban> getTiersIbanList(BanquePK pk,String codeTiers);
	List<Banque> findBanqueWithTiers(Banque searchCriteria,boolean onlyWithOpenedIban, boolean notifie);
	void updateObject(Object obj);
	List<String> loadPaysList();
	
}
