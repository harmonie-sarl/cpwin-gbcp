package fr.symphonie.tools.common.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.symphonie.util.model.Trace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "import_vague")
public class FileImportTrace {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "Numeric")
	private Integer id;
	@Column(name = "num_exec")
	private Integer exercice;
	@Column
	private String module;
	@Column
	private String fileName;
	@Column
	private long crc32;
	@Embedded
	private Trace trace;

}
