package com.plexobject.quran.model;

import java.io.Serializable;
import java.util.Date;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Persistent
public abstract class PersistentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private Long id;
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private int hashCode;
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private long createdAt = System.currentTimeMillis() / 1000;
	private long updatedAt = System.currentTimeMillis() / 1000;
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private boolean syncToDB;
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private int userId;

	protected PersistentEntity() {
	}

	public PersistentEntity(Long id) {
		this.id = id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public Date getCreatedDate() {
		return new Date(createdAt * 1000);
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (getId() == null ? super.hashCode() : getId().intValue());
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
		PersistentEntity other = (PersistentEntity) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "PersistentEntity [id=" + id + "]";
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setUpdatedDate(Date date) {
		this.updatedAt = date.getTime() / 1000;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public Date getUpdatedDate() {
		return new Date(updatedAt * 1000);
	}

	public void setSyncToDB(boolean syncToDB) {
		this.syncToDB = syncToDB;
	}

	public boolean isSyncToDB() {
		return syncToDB;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public void setHashCode() {
		this.hashCode = hashCode();
	}

	public int getHashCode() {
		return hashCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
