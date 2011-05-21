package org.fastorm.hibernateComparison;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "telephone")
public class Telephone {

	private long Id;
	private long person;
//	private Collection<Favourite> favourites;
	@Id
	@Column(name = "id")
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	@Override
	public String toString() {
		return "Telephone [Id=" + Id + ", person=" + person + "]";
	}

	@Column(name = "t_person")
	public long getPerson() {
		return person;
	}

	public void setPerson(long person) {
		this.person = person;
	}
//	@OneToMany(targetEntity = org.fastorm.hibernateComparison.Favourite.class, cascade = CascadeType.ALL, mappedBy = "Favourite")
//	public Collection<Favourite> getTelephones() {
//		return favourites;
//	}
//
//	public void setTelephones(Collection<Favourite> favourites) {
//		this.favourites = favourites;
//	}

}
