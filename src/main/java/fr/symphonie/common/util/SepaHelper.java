package fr.symphonie.common.util;

import org.iban4j.BicUtil;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.Iban4jException;
import org.iban4j.IbanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SepaHelper {
	private static final Logger logger =LoggerFactory.getLogger(SepaHelper.class);
	public static boolean isValidIban(String iban) {
		try {
		IbanUtil.validate(iban);
		}
		catch(Iban4jException e) {
			logger.error("isValidIban ERROR : {} -> {} : ",iban,e.getMessage());
			return false;
		}
		return true;
	}
	public static boolean isValidBic(String bic) {
		logger.debug("isValidBic: {}",bic);
		try {
		BicUtil.validate(bic);
		}
		catch(Iban4jException  e) {
			logger.error("isValidBic ERROR:  {} -> {}: ",bic,e.getMessage());
			return false;
		}
		return true;
	}
	public static String getIbanBranchCode(String ibanStr) {
		Iban iban=Iban.valueOf(ibanStr);
		return iban.getBranchCode();
	}
	public static boolean isFrenchIban(String ibanStr) {
		//logger.debug("isFrenchIban:{} -> {} ",ibanStr,IbanUtil.getCountryCode(ibanStr));
		return IbanUtil.getCountryCode(ibanStr).equals(CountryCode.FR.getAlpha2());
	}
	public static String getIbanCountryCode(String ibanStr) {		
		return IbanUtil.getCountryCode(ibanStr);
	}

}
