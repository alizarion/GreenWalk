package com.project.entities;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 29/09/11
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */

import com.project.Helper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: selim.bensenouci
 * Date: 23/06/11
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema= Helper.ENTITIES_CATALOG, name="uploadedfile")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQuery(name= UploadedFile.FIND_ALL,
        query="SELECT upfile FROM UploadedFile upfile")
@DiscriminatorColumn(name = "type")
public abstract class UploadedFile implements Serializable{

    static public final String FIND_ALL = "UploadedFile.FIND_ALL";

    @Id
    @TableGenerator(name="UploadedFile_SEQ", table="sequence",catalog = Helper.ENTITIES_CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="UploadedFile_SEQ")
    @Column(name="upfile_id")
    private Long id;

    @NotNull
    @Column
    @Size(max = 255)
    private String fileName;


    @Column
    @Size(max = 255)
    private String hashControl;

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @NotNull
    @Column
    private Boolean preSelected;

    @Column
    private Boolean temporary;

    @Column(insertable = false,updatable = false )
    private String type;

    @Column
    private String weight;

    public UploadedFile(){
        this.creationDateTime = new Date();
        this.preSelected = false;
        this.temporary = true;
        this.hashControl = UUID.randomUUID().toString();

    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public abstract File getFullPath();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHashControl() {
        return hashControl;
    }

    public void setHashControl(String hashControl) {
        this.hashControl = hashControl;
    }

    public UploadedFile(String fileName){
        this.fileName = fileName;
        this.creationDateTime = new Date();
        this.temporary = true;
        this.hashControl= UUID.randomUUID().toString();
        this.preSelected = false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return fileName;
    }

    public Boolean getPreSelected() {
        return preSelected;
    }

    public void setPreSelected(Boolean preSelected) {
        this.preSelected = preSelected;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
               this.hashControl = UUID.randomUUID().toString();
    }

    public void setName(String name) {
        this.fileName = name;
         this.hashControl = UUID.randomUUID().toString();
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public abstract void onPreRemove();




    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UploadedFile other = (UploadedFile) obj;
        if (this.id != other.id &&
                (this.id == null || !(this.id ==other.id))) {
            return false;
        }
        return true;
    }


}
