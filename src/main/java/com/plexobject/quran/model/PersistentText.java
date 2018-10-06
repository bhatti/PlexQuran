package com.plexobject.quran.model;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity(version = 0)
public class PersistentText extends PersistentEntity {
    private static final long serialVersionUID = 1L;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String name;
    private String value;

    PersistentText() {
    }

    public PersistentText(String name, String value) {
        this.setName(name);
        if (null == value) {
            throw new IllegalArgumentException("value name not specified");
        }
        this.value = value;
    }

    public void setName(String name) {
        setId(new Long(name.hashCode()));
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PersistentEntity [name=" + name + ", value=" + value + "]";
    }

}
