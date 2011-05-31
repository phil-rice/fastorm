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
	private Collection<Favourite> favourites;
	private String number;
	private String telephoneType;

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

	@OneToMany(targetEntity = org.fastorm.hibernateComparison.Favourite.class, cascade = CascadeType.ALL, mappedBy = "telephone")
	public Collection<Favourite> getFavourites() {
		return favourites;
	}

	public void setFavourites(Collection<Favourite> favourites) {
		this.favourites = favourites;
	}

	@Column(name = "number")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "telephoneType")
	public String getTelephoneType() {
		return telephoneType;
	}

	public void setTelephoneType(String telephoneType) {
		this.telephoneType = telephoneType;
	}

}
