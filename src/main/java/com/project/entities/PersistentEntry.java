package com.project.entities;



import com.project.Helper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 30/01/13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = Helper.ENTITIES_CATALOG,name = "persistent_entry")
@NamedQuery(name = "PersistentEntry.FIND_BY_CATEGORY_AND_KEY", query = "SELECT pe FROM PersistentEntry pe WHERE pe.category = :category AND pe.key = :key")
public class PersistentEntry implements Serializable {
    public static final String FIND_BY_CATEGORY_AND_KEY = "PersistentEntry.FIND_BY_CATEGORY_AND_KEY";

    @Id
    @TableGenerator(name="PERSISTENTENTRY_SEQ", table="sequence",  catalog = "ingress_defence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="PERSISTENTENTRY_SEQ")
    @Column(name = "persistent_entry_id")
    private Long id;

    @NotNull
    @Size(max = 63)
    @Column(name = "category", nullable = false, length = 63)
    private String category;

    @NotNull
    @Size(max = 63)
    @Column(name = "key_value", nullable = false, length = 63)
    private String key;

    @Size(max = 2048)
    @Column(name = "value")
    private String value;

    @Column(name = "update_date_time")
    private Date updateDateTime;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.updateDateTime= new Date();
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistentEntry)) return false;

        PersistentEntry that = (PersistentEntry) o;

        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null)
            return false;
        if (updateDateTime != null ? !updateDateTime.equals(that.updateDateTime) : that.updateDateTime != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (updateDateTime != null ? updateDateTime.hashCode() : 0);
        return result;
    }
}