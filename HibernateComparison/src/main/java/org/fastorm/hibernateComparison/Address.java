package org.fastorm.hibernateComparison;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Address {

	private long Id;
	private long person;

	@Id
	@Column(name = "a_id")
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	@Column(name = "a_person")
	public long getPerson() {
		return person;
	}

	@Override
	public String toString() {
		return "Address [Id=" + Id + ", person=" + person + "]";
	}

	public void setPerson(long person) {
		this.person = person;
	}

}
