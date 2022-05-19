package fr.symphonie.tools.lemans.bt.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
public class Vente {
	// Attributs
	private Long id;
	private Integer exercice;
	private Integer	periodeNumero;
	private Integer encaissementNumero;
	private Date	dateVente;
	private BigDecimal amount;
	private Trace trace;
	// Relationship
	private Client client;
	private Spectacle spectacle;
	private List<VenteItem> details;
	
	

}
