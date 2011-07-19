package org.auto.comet.example.chat.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.AccessType;

@Entity
@Table(name = "students")
public class Student implements java.io.Serializable {

	static {
		System.out.println("asdf!@@@@@@@@@@@@@@@@@");
	}

	// Fields
	@Id
	@AccessType(value = "property")
	// 注意这里
	@Column(name = "s_id")
	// 实际做的时候没有这一行，用了其他技巧自动转换名字为group_id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer SId;

	@AccessType(value = "property")
	@Column(name = "s_name")
	private String SName;

	// Constructors

	/** default constructor */
	public Student() {
	}

	// Property accessors

	public Integer getSId() {
		return this.SId;
	}

	public void setSId(Integer SId) {
		this.SId = SId;
	}

	public String getSName() {
		return this.SName;
	}

	public void setSName(String SName) {
		this.SName = SName;
	}

}
