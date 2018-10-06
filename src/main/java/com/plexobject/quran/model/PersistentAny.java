package com.plexobject.quran.model;

import java.io.Serializable;

import com.sleepycat.persist.model.Entity;

@Entity(version = 0)
public class PersistentAny extends PersistentEntity {
	private static final long serialVersionUID = 1L;
	private Serializable value;

	PersistentAny() {
	}

	public PersistentAny(Long id, Serializable value) {
		super(id);
		if (null == value) {
			throw new IllegalArgumentException("value name not specified");
		}
		this.value = value;
	}

	public void setValue(final Serializable value) {
		this.value = value;
	}

	public Serializable getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "PersistentEntity [id=" + getId() + ", value=" + value + "]";
	}
}
