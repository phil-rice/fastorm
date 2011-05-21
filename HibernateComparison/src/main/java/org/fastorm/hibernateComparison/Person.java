package org.fastorm.hibernateComparison;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person {

	private long Id;
	private Collection<Address> addresses;
	private Collection<Telephone> telephones;

	@Id
	@Column(name = "id")
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	@OneToMany(targetEntity = org.fastorm.hibernateComparison.Address.class, cascade = CascadeType.ALL, mappedBy = "person")
	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "Person [Id=" + Id + ", addresses=" + addresses + ", telephones=" + telephones + "]";
	}

	@OneToMany(targetEntity = org.fastorm.hibernateComparison.Telephone.class, cascade = CascadeType.ALL, mappedBy = "person")
	public Collection<Telephone> getTelephones() {
		return telephones;
	}

	public void setTelephones(Collection<Telephone> telephones) {
		this.telephones = telephones;
	}

}
