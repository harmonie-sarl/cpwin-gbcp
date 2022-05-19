package fr.symphonie.tools.common.model;

import fr.symphonie.util.model.Trace;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class FileImportTrace {
	private Integer id;
	private Integer exercice;
	private String module;
	private String fileName;
	private long crc32;
	private Trace trace;

}
