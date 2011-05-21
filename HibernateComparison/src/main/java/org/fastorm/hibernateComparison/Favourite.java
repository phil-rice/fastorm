package org.fastorm.hibernateComparison;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "favourites")
public class Favourite {

	private long Id;
	private long telephone;

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
		return "Favourites [Id=" + Id + ", person=" + telephone + "]";
	}

	@Column(name = "f_telephone")
	public long getTelephone() {
		return telephone;
	}

	public void setTelephone(long telephone) {
		this.telephone = telephone;
	}


}
