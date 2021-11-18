package fr.symphonie.common.util;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import fr.symphonie.util.FileHelper;

public class Test {
	//private static final Logger logger = LoggerFactory.getLogger(Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//readPainXml();
		getFeverier();
	}
	private static void getFeverier() {
		Calendar calendar =  Calendar.getInstance();
		int exercice=2021;
		calendar.set(exercice, Calendar.FEBRUARY, 1);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, maxDay);
		System.out.print(calendar.getTime());
	}
//	public static void readPainXml() {
//		JAXBContext jaxbContext;
//		try {
//			jaxbContext = JAXBContext.newInstance(symphonie.fr.sepa.model.pain.sct.fr.ObjectFactory.class);
//		
//		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//		symphonie.fr.sepa.model.pain.sct.fr.Document sepaObj = ((JAXBElement<symphonie.fr.sepa.model.pain.sct.fr.Document>) jaxbUnmarshaller.unmarshal(new File("E:\\Travail\\Demandes\\GBCP\\2021\\03-LaCasemate\\P_CCSTI-VirS02_12.sepa_03.xml"))).getValue();
//		System.out.printf("size %d", sepaObj.getCstmrCdtTrfInitn().getPmtInf().size());
//		System.out.printf("MsgId %s", sepaObj.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public static void relirPDF() {
		Path pdfPath = Paths.get("E:\\Travail\\Demandes\\GBCP\\2020\\10-CRC\\test\\CRC\\MDT2020A841_1.pdf");
		try {
			byte[] bArray =Files.readAllBytes(pdfPath);
			OutputStream out = new FileOutputStream("E:\\Travail\\Demandes\\GBCP\\2020\\10-CRC\\test\\CRC\\out.pdf");
			out.write(bArray);
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 static void getImageDimension() {
		String path="C:\\Users\\pc\\Pictures\\";
		String[] fileNames= new String[] {"ennahdha1.jfif","ennahdha2.jfif","ennahdha3.jfif","images.jpg"};
		 Arrays.stream(fileNames).forEach(s ->{
			 try {
				 BufferedImage bimg = ImageIO.read(new File(path+s));
					int width          = bimg.getWidth();
					int height         = bimg.getHeight(); 
					//logger.debug("fileUploadHandler: width={} ,height={} ",width,height);
					System.out.println(String.format("image=%s -> (%d,%d)",s, width,height));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
		}		
				);
	
	}
	 static void mergesFiles() {
		String sourceFile1Path = "C:\\Perso\\MinEdu\\2020-2021\\ROUAA.pdf";
		String sourceFile2Path = "C:\\Perso\\MinEdu\\rayhan-2020-2021 .pdf";
 
		String mergedFilePath = "C:\\Perso\\MinEdu\\fusion-test1.pdf";		
		
		try {
	        //Prepare input pdf file list as list of input stream.
	        List<InputStream> inputPdfList = new ArrayList<InputStream>();
	        inputPdfList.add(new FileInputStream(sourceFile1Path));
	        inputPdfList.add(new FileInputStream(sourceFile2Path));
//	        inputPdfList.add(new FileInputStream("..\\pdf\\pdf_3.pdf"));
//	        inputPdfList.add(new FileInputStream("..\\pdf\\pdf_4.pdf"));


	        //Prepare output stream for merged pdf file.
	        OutputStream outputStream = 
	                new FileOutputStream(mergedFilePath);

	        //call method to merge pdf files.
	        mergePdfFiles(inputPdfList, outputStream);     
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	static void mergePdfFiles(List<InputStream> inputPdfList,
	        OutputStream outputStream) throws Exception{
	    //Create document and pdfReader objects.
	    Document document = new Document();
	    List<PdfReader> readers = 
	            new ArrayList<PdfReader>();
	    int totalPages = 0;

	    //Create pdf Iterator object using inputPdfList.
	    Iterator<InputStream> pdfIterator = 
	            inputPdfList.iterator();

	    // Create reader list for the input pdf files.
	    while (pdfIterator.hasNext()) {
	            InputStream pdf = pdfIterator.next();
	            PdfReader pdfReader = new PdfReader(pdf);
	            readers.add(pdfReader);
	            totalPages = totalPages + pdfReader.getNumberOfPages();
	    }

	    // Create writer for the outputStream
	    PdfWriter writer = PdfWriter.getInstance(document, outputStream);

	    //Open document.
	    document.open();

	    //Contain the pdf data.
	    PdfContentByte pageContentByte = writer.getDirectContent();

	    PdfImportedPage pdfImportedPage;
	    int currentPdfReaderPage = 1;
	    Iterator<PdfReader> iteratorPDFReader = readers.iterator();

	    // Iterate and process the reader list.
	    while (iteratorPDFReader.hasNext()) {
	            PdfReader pdfReader = iteratorPDFReader.next();
	            //Create page and add content.
	            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
	                  document.newPage();
	                  pdfImportedPage = writer.getImportedPage(
	                          pdfReader,currentPdfReaderPage);
	                  pageContentByte.addTemplate(pdfImportedPage, 0, 0);
	                  currentPdfReaderPage++;
	            }
	            currentPdfReaderPage = 1;
	    }

	    //Close document and outputStream.
	    outputStream.flush();
	    document.close();
	    outputStream.close();

	    System.out.println("Pdf files merged successfully.");
	}

	 static void printPDF()
	{
	    PrinterJob printerJob = PrinterJob.getPrinterJob();

	    PrintService printService = null;
	    if(printerJob.printDialog())
	    {
	        printService = printerJob.getPrintService();
	    }
	    DocFlavor docType = DocFlavor.INPUT_STREAM.AUTOSENSE;

//	    for ( //fetch documents to be printed)
//	    {
	        DocPrintJob printJob = printService.createPrintJob();
	        try {
	        final byte[] byteStream =FileHelper.loadFile("C:\\Perso\\steg\\harmonie\\2020\\sur releve\\STEG - Facture ref _ 642521400.pdf"); // fetch content in byte array;
	            Doc documentToBePrinted = new SimpleDoc(new ByteArrayInputStream(byteStream), docType, null);
	        
				printJob.print(documentToBePrinted, null);
			} catch (PrintException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//	    }
	}
	
	 static void mergeFiles(File[] files, File mergedFile) {
		 
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(mergedFile, true);
			 out = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
 
		for (File f : files) {
			System.out.println("merging: " + f.getName());
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
				String aLine;
				while ((aLine = in.readLine()) != null) {
					out.write(aLine);
					out.newLine();
				}
				out.flush();
				in.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
 
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
	
	 static void parsePeriode(String strValue ) {
		String execStr=strValue.substring(0, 4);
		System.out.println("execStr: "+execStr);
		String periodeStr=strValue.substring(4);
		System.out.println("periodeStr: "+periodeStr);
		
	}
	 static void findDuplicates() {
		List<Integer> duplicates =
		IntStream.of(1,4,2,3)
		.boxed()
		.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
		.entrySet()
		.stream()
		.filter( p -> p.getValue()>1)
		.map( Map.Entry::getKey )
	       .collect( Collectors.toList() );
		System.out.println("duplicates: "+duplicates);
	}
//	private static String BigDecimalToString(BigDecimal val){
//		return val.toString();
//	}
//	private static String getSumMontantRequete(Integer periode,String prefix){
//		String r1="",r2="";
//		for(int i=1;i<=periode;i++){
//			if(i>9)break;
//			if(i>1)r1+=",";
//			r1+=prefix+".mt_0"+i;
//		}
//		if(periode<10){
//			System.out.println("getSumMontantRequete: periode="+periode+" -> " +r1);
//			return r1;
//		}
//		for(int i=10;i<=periode;i++){
//			r2+=","+prefix+".mt_"+i;
//		}
//		System.out.println("getSumMontantRequete:periode="+periode+" -> " +r1+r2);
//		return r1+r2;
//	}
	public static void pack(final Path folder, final Path zipFilePath) throws IOException {
	    try (
	            FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
	            ZipOutputStream zos = new ZipOutputStream(fos)
	    ) {
	        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	                zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
	                Files.copy(file, zos);
	                zos.closeEntry();
	                return FileVisitResult.CONTINUE;
	            }

	            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
	                zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
	                zos.closeEntry();
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
	}

}
