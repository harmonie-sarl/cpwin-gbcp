package fr.symphonie.tools.lemans.bt.model;

import fr.symphonie.util.model.Trace;
import lombok.Data;

@Data
public class ModePaiement {
	
	private String Code;
	
	private String designation;
	
	private String tiers;
	
	private Boolean frais;
	
	private Trace trace;

}
