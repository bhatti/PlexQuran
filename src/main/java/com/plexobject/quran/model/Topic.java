package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Comparable<Topic> {
	private String id;
	private Topic parent;
	private String name;
	private List<Ayat> ayats = new ArrayList<Ayat>();

	public Topic() {

	}

	public Topic(String id, Topic parent, String name) {
		this.id = id;
		this.parent = parent;
		this.name = name;
	}

	public Topic(String id, Topic parent, String name, List<Ayat> ayats) {
		this.id = id;
		this.parent = parent;
		this.name = name;
		this.ayats = ayats;
	}

	public String getFullName() {
		StringBuilder sb = new StringBuilder(name);
		Topic p = parent;
		while (p != null) {
			sb.insert(0, p.getName() + " ");
			p = p.parent;
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name);
		Topic p = parent;
		while (p != null) {
			sb.insert(0, "  ");
			p = p.parent;
		}
		return sb.toString();
	}

	public String getParentId() {
		return parent == null ? null : parent.getId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Topic getParent() {
		return parent;
	}

	public void setParent(Topic parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Ayat> getAyats() {
		return ayats;
	}

	public void setAyats(List<Ayat> ayats) {
		this.ayats = ayats;
	}

	@Override
	public int compareTo(Topic o) {
		return getFullName().toLowerCase().compareTo(
		        o.getFullName().toLowerCase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
