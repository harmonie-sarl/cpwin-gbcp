package fr.symphonie.tools.das.model;

public enum HonoraireStruct {
	CODE_TIERS(0),
	honoraires(1),
	commissions(2),
	courtages(3),
	ristournes(4),
	jetonsPresence(5),
	droitsAuteur(6),
	droitsInventeur(7),
	autresRemunerations(8),
	indemnitesRemb(9),
	avantagesEnNature(10),
	retenueAlaSource(11),
	nourriture(12,"N"),
	logement(13,"L"),
	voiture(14,"V"),
	autres(15,"A"),
	outilsNTIC(16,"T"),
	allocForfaitaire(17,"F"),
	remboursements(18,"R"),
	priseEnChargeDirecteEmployeur(19,"P"),
	tauxReduit(20,"R"),
	dispense(21,"D"),
	tvaNetteDroitsAuteur(22);
	
	
	int position;
	String valeurAutorise;
	HonoraireStruct(int pos){
		this.position=pos;
	}
	HonoraireStruct(int pos,String valeurAutorise){
		this(pos);
		this.valeurAutorise=valeurAutorise;
	}
	public static HonoraireStruct parse(int pos){
		for(HonoraireStruct s:HonoraireStruct.values()){
			if(s.position==pos) return s;
		}
		
		return null;
	}
}
