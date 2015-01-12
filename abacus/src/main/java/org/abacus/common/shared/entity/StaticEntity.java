package org.abacus.common.shared.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
public class StaticEntity implements Serializable, Cloneable, Comparable<StaticEntity> {

	@Id
	@Column(name = "id", length=30)
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StaticEntity)) {
			return false;
		}
		final StaticEntity other = (StaticEntity) obj;
		if (this.id != null && other.id != null) {
			if (!this.id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString(){
		if (this.id!=null){
			return this.getClass().getSimpleName()+":"+this.id;
		}
		return this.getClass().getSimpleName()+":NEW";
	}
	
	public boolean isNew() {
		return (this.getId()==null);
	}

	@Override
	public int compareTo(StaticEntity o) {
		return this.id.compareTo(o.id);
	}

}
