package fr.symphonie.tools.common.model;

import fr.symphonie.util.model.Trace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileImportTrace {
	private Integer id;
	private Integer exercice;
	private String module;
	private String fileName;
	private long crc32;
	private Trace trace;

}
