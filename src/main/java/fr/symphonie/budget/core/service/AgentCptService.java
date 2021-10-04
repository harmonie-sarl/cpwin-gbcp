package fr.symphonie.budget.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import fr.symphonie.budget.core.dao.IAgentCptDao;
import fr.symphonie.cpwin.model.Banque;
import fr.symphonie.cpwin.model.Iban;
import fr.symphonie.cpwin.model.Tiers;
import fr.symphonie.cpwin.model.pk.BanquePK;
@Service
public class AgentCptService implements IAgentCptService {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(AgentCptService.class);	
	private IAgentCptDao agentCptDao;
	
	@Inject
	public void setAgentCptDao(IAgentCptDao agentCptDao) {
		this.agentCptDao = agentCptDao;
	}

	public IAgentCptDao getAgentCptDao() {
		return agentCptDao;
	}

	@Override
	@Transactional
	public void addBanque(Banque banque) {
		agentCptDao.insert(banque);
	}

	@Override
	@Transactional
	public void updateBanque(Banque banque) {
		agentCptDao.update(banque);
	}

	@Override
	public List<Banque> findBanquesAbsorbes(String codePays, String codeEtab, Date dateFinDiffusion, boolean onlyWithOpenedIban) {
		List<Banque> result=new ArrayList<Banque>();
		List<Banque> banqueList=agentCptDao.searchBanquesAbsorbes(codePays, codeEtab, dateFinDiffusion,false);
		int openedIban=0;
		if(banqueList!=null){
			for (Banque banque : banqueList) {
				BanquePK pk=new BanquePK();
				pk.setCodePays(banque.getCodePays());
				pk.setCodeBanque(banque.getCodeBanque());
				openedIban=getOpenedIban(pk);
				banque.setOpenedIbans(openedIban);
				if(!onlyWithOpenedIban)	result.add(banque);
				else if(openedIban>0)	result.add(banque);
			}
		}
		return result;
	}
	@Override
	public int getOpenedIban(BanquePK pk){
//		if (logger.isDebugEnabled()) {
//			logger.debug("getOpenedIban("+pk.getCodePays()+pk.getCodeBanque()+") - start"); //$NON-NLS-1$
//		}
		int returnint = agentCptDao.getOpenedIban(pk);
//		if (logger.isDebugEnabled()) {
//			logger.debug("getOpenedIban(BanquePK)returnint="+returnint+" - end"); //$NON-NLS-1$
//		}
		return returnint;
	}

	@Override
	public List<Banque> findBanqueWithTiers(Banque searchCriteria,
			boolean onlyWithOpenedIban, boolean notifie) {
		if (logger.isDebugEnabled()) {
			logger.debug("findBanqueWithTiers(" + searchCriteria.getCodePays() +"-"+ searchCriteria.getCodeBanque() + ", " + onlyWithOpenedIban + ") - start"); //$NON-NLS-1$
		}

		List<Banque> banqueList = new ArrayList<Banque>();
		List<Banque> copy = agentCptDao.searchBanquesAbsorbes(
				searchCriteria.getCodePays(), searchCriteria.getCodeBanque(),
				searchCriteria.getFinDiffusion(),true);
		for (Banque banque : copy) {
			List<Tiers> tiersList = getTiers(banque);
			if (!tiersList.isEmpty()) {

				for (Tiers tiers : tiersList) {
					tiers.setIbans(null);
					tiers.setBanque(banque);
					tiers.setOpenedIbans(agentCptDao.getTiersOpenedIban(new BanquePK(banque.getCodePays(), banque.getCodeBanque()), tiers.getCode()));
					if(notifie){
						if(tiers.getNotifications().isEmpty()) continue;
					}
					if (!onlyWithOpenedIban) {
						banque.getTiersList().add(tiers);
					} else if (tiers.getOpenedIbans() > 0)
						banque.getTiersList().add(tiers);
				}
				if (!banque.getTiersList().isEmpty())
					banqueList.add(banque);
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("findBanqueWithTiers size=" + banqueList.size() + " - end"); //$NON-NLS-1$
		}
		return banqueList;
	}

	private List<Tiers> getTiers(Banque banque) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTiers(Banque: "+banque.getCodePays()+"-"+banque.getCodeBanque()+") - start"); //$NON-NLS-1$
		}
		

		List<Tiers> returnList = agentCptDao.getTiers(new BanquePK(banque.getCodePays(), banque.getCodeBanque()));
		
		if (logger.isDebugEnabled()) {
			logger.debug("getTiers size="+returnList.size()+" - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Override
	@Transactional
	public void updateObject(Object obj) {
		agentCptDao.update(obj);
		
	}

	@Override
	public List<Iban> getTiersIbanList(BanquePK pk,String codeTiers) {
		// TODO Auto-generated method stub
		return agentCptDao.getTiersIbanList(pk,codeTiers);
	}
	@Override
	public List<String> loadPaysList() {
		return agentCptDao.loadPaysList();
	}
}
