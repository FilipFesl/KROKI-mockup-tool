package adapt.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;

   /** 
   Class generated using Kroki EJBGenerator 
   @Author mrd 
   Creation date: 02.04.2013  15:17:20h
   **/

@Entity
@Table(name = "ADAPT_RESOURCE")
public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private java.lang.Long id;

	@Column(name = "RES_NAME", unique = true, nullable = false)
	private java.lang.String name;
	
	@Column(name = "RES_LINK", unique = true, nullable = false)
	private java.lang.String link;
	
	
	
	@OneToMany(cascade = { ALL }, fetch = FetchType.LAZY, mappedBy = "resource")
	private Set<UserRights> UserRightsSet = new HashSet<UserRights>();
	
	public Resource(){
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getLink() {
		return this.link;
	}
	
	public void setLink(java.lang.String link) {
		this.link = link;
	}
	
	public Set<UserRights> getUserRightsSet() {
		return this.UserRightsSet;
	}

	public void setUserRightsSet(Set<UserRights> UserRightsSet) {
		this.UserRightsSet = UserRightsSet;
	}
	
}