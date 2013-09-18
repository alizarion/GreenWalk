package com.project.entities;

/**
 * Created by IntelliJ IDEA.
 * User: sphinx
 * Date: 29/09/11
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */

import com.project.Helper;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.*;
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
    private final static Logger LOG = Logger.getLogger(UploadedFile.class);


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

    public abstract File getFileFullPath();

    public abstract File getResizedFileFullPath(Integer witdh, Integer weight);

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


    public Boolean writeFile(InputStream inputStream) throws IOException {

        File targetFile = getFileFullPath();
        FileOutputStream out = new FileOutputStream(targetFile);
        int BUFFER_SIZE = 8192;
        byte[] buffer = new byte[BUFFER_SIZE];
        int a;
        while (true) {
            a = inputStream.read(buffer);
            if (a < 0)
                break;
            out.write(buffer, 0, a);
            out.flush();
        }
        out.close();
        inputStream.close();
        return true;
    }


    public InputStream readFile() throws FileNotFoundException {
        InputStream in = new FileInputStream(getFileFullPath());
        return in;
    }

    public InputStream readResizedFile(Integer width,
                                       Integer height) throws FileNotFoundException {
        InputStream in = new FileInputStream(getResizedFileFullPath(width,height));
        return in;
    }


    public Boolean writeResizedFile(InputStream inputStream,
                                    Integer width,Integer height) throws IOException {

        File targetFile = getResizedFileFullPath(width,height);
        FileOutputStream out = new FileOutputStream(targetFile);
        int BUFFER_SIZE = 8192;
        byte[] buffer = new byte[BUFFER_SIZE];
        int a;
        while (true) {
            a = inputStream.read(buffer);
            if (a < 0)
                break;
            out.write(buffer, 0, a);
            out.flush();
        }
        out.close();
        inputStream.close();
        return true;
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

    @PostUpdate
    public void  postUpdate(){
        if (!getFileFullPath().exists()){
            File toFile =this.getFileFullPath();
            File toDirectory = getFileFullPath().getParentFile();
            this.temporary = !this.temporary;
            File fromFile = this.getFileFullPath();
            if (fromFile != null){
                if (fromFile.exists()){
                    if (!toDirectory.exists()){
                        toDirectory.mkdirs();
                    }
                    if (fromFile.renameTo(toFile)){
                        LOG.info("File moved from : "+
                                fromFile.getAbsolutePath() +
                                ", to " +toFile.getAbsolutePath());
                    }else {
                        LOG.error("Error in moving file from : "+
                                fromFile.getAbsolutePath() +
                                ", to " +toFile.getAbsolutePath());
                    }
                }
            } else {

            }
            this.temporary = !this.temporary;
        }
    }
  //  @PostPersist
    public void  postPersist(){
        if (!getFileFullPath().exists()){
            this.temporary = !this.temporary;
            File toFile =this.getFileFullPath();
            File toDirectory = getFileFullPath().getParentFile();
            this.temporary = !this.temporary;
            File fromFile = this.getFileFullPath();
            if (fromFile != null){
                if (fromFile.exists()){
                    if (!toDirectory.exists()){
                        toDirectory.mkdirs();
                    }
                    if (fromFile.renameTo(toFile)){
                        LOG.info("File moved from : "+
                                fromFile.getAbsolutePath() +
                                ", to " +toFile.getAbsolutePath());
                    }else {
                        LOG.error("Error in moving file from : "+
                                fromFile.getAbsolutePath() +
                                ", to " +toFile.getAbsolutePath());
                    }
                }
            } else {

            }
            this.temporary = !this.temporary;
        }
    }
}
