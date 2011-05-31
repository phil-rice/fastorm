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
	private String line1;
	private String line2;
	private String line3;
	private String postcode;

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

	@Column(name = "line1")
	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	@Column(name = "line2")
	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	@Column(name = "line3")
	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	@Column(name = "postcode")
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

}
