package fr.symphonie.tools.sct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fr.symphonie.common.model.IFlatLine;
import fr.symphonie.common.util.Util;
import fr.symphonie.impor.AttributImport;
import fr.symphonie.impor.TypeEnum;
import fr.symphonie.tools.sct.model.RefundsItem;

public class PAYMEN_SEPA_993_FileModel {
	public static final String CODE_COL = "code_collectivite";
	public static final String CODE_NAT_DEP = "code_nature_depense";
	public static final String GEN_FILE_NAME = "PAYMEN.txt";
	
	private List<IFlatLine> body;
	
	public PAYMEN_SEPA_993_FileModel() {
		super();
		this.body=new ArrayList<IFlatLine>();
	}

	public PAYMEN_SEPA_993_FileModel(List<RefundsItem> transfertList,Map<String,Object> params) {
		this();
		for (RefundsItem item : transfertList) {
			
			this.body.add(createBodyItem(item, params));
		}
		
	}
	
	private IFlatLine createBodyItem(RefundsItem item,Map<String,Object> params) {
		String codeCollectivite=(String) params.get(CODE_COL);
		String codeNatureDepense=(String) params.get(CODE_NAT_DEP);
		return new IFlatLine() {
				
				@Override
				public AttributImport[] getAttributs() {
					int i=0;
					AttributImport[] attributs={
							new AttributImport(i++,TypeEnum.Alpha,7,codeCollectivite),
							new AttributImport(i++,TypeEnum.Alpha,3,"000"),
							new AttributImport(i++,TypeEnum.Alpha,3,codeNatureDepense),
							new AttributImport(i++,TypeEnum.Numeric,15,item.getActor().getCode()),
							new AttributImport(i++,TypeEnum.Alpha,1,"E"),
							new AttributImport(i++,TypeEnum.Numeric,16,Util.formatCurrencyWithoutSeparator(item.getAmount())),
							new AttributImport(i++,TypeEnum.Alpha,43,AttributImport.SPACE_SEPARATOR),
							new AttributImport(i++,TypeEnum.Alpha,11,item.getActor().getBic()),
							new AttributImport(i++,TypeEnum.Alpha,34,item.getActor().getIban()),
							new AttributImport(i++,TypeEnum.Alpha,70,item.getActor().getFullName()),
							new AttributImport(i++,TypeEnum.Alpha,177,AttributImport.SPACE_SEPARATOR),
							new AttributImport(i++,TypeEnum.Alpha,70,item.getActor().getFullName()),
							new AttributImport(i++,TypeEnum.Alpha,177,AttributImport.SPACE_SEPARATOR),
							new AttributImport(i++,TypeEnum.Alpha,140,Util.controlSpecial(item.getObject())),
							new AttributImport(i++,TypeEnum.Alpha,26,AttributImport.SPACE_SEPARATOR),
							new AttributImport(i++,TypeEnum.Alpha,200,AttributImport.SPACE_SEPARATOR),
							
					};
					return attributs;
				}
			};
		}

	public List<IFlatLine> getBody() {
		return body;
	}

	public void setBody(List<IFlatLine> body) {
		this.body = body;
	}
	public static String getSavedPath(String rootPath)  {
		Date date=new Date();
		StringBuilder path=new StringBuilder(rootPath);
		path.append(File.separator);
		path.append(Util.getYear(date));
		path.append(File.separator);
		path.append(Util.getDayOfYear(date));
		path.append(File.separator);
		 Path p =Paths.get(path.toString());
		if (Files.notExists(p)) {
			 try {
				// logger.debug("getGeneratePath: Files.createDirectories");
				Files.createDirectories(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		
		
		return path.toString();
	}
	public static String getTargetFile(String rootPath)  {
		return getSavedPath(rootPath)+GEN_FILE_NAME;
	}
	public static String display(IFlatLine enregistrement) {
		StringBuilder retour=new StringBuilder("");
		List<AttributImport> list=Arrays.asList(enregistrement.getAttributs());
		Comparator<AttributImport> c=new Comparator<AttributImport>(){
			@Override
			public int compare(AttributImport o1,AttributImport o2) {
				 return o1.getIndex().compareTo(o2.getIndex());
			}
			};			
		
		list.sort(c);
		for(AttributImport attr:list){
			retour.append(attr.display());
		}
		
		
		return retour.toString();
	}

}
