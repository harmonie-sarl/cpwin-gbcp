package fr.symphonie.tools.lemans.bt.model;

import java.math.BigDecimal;
import java.util.Date;

import fr.symphonie.util.model.Trace;
import lombok.Data;
@Data
public class VenteItem {
	private Long 		id;
	private Integer 	exercice;
	private Integer		periodeNumero;
	private Integer 	encaissementNumero;
	private String 		modePaiement;
	private Date		datePaiement;
	private BigDecimal 	amount;
	private Trace 		trace;
	//RelationShip
	private Vente		vente;
}
