package models.common;

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "non-lazy")
public class File extends Base {

	private Blob blob;
	private Base parent;

	@Column(name = "filedata", length = 1024 * 1024)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Base getParent() {
		return parent;
	}

	public void setParent(Base parent) {
		this.parent = parent;
	}
}