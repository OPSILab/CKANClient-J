package org.ckan;



import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Represents a CKAN Dataset (previously a Package)
 *
 * @author      Ross Jones <ross.jones@okfn.org>
 * @version     1.7
 * @since       2012-05-01
 */
public class Dataset {

    public class Response {
        public boolean success;
        public Dataset result;
    }

    public class SearchResponse {
        public boolean success;
        public SearchResults result;
    }
    
    
    
    public class IDListResponse{
    	public boolean success;
    	public String[] result;
    }

    /**
     * Represents the results of a search on a CKAN instance, and shows the
     * count and provides a list of dataset objects which match the term.
     *
     * @author      Ross Jones <ross.jones@okfn.org>
     * @version     1.7
     * @since       2012-05-01
     */
    public class SearchResults {
        //{"count": 4, "search_facets": {}, "facets": {}, "results":
    	
        public int count;
        public List<Dataset> results;
    }
    
    public class FirstSynchResults {
    	public boolean success;
        public List<Dataset> result;
    }
    
    
    
    
    

    public String id;
    public String revision_id;
    
    public String name;
    public String title;
    public String author;
    public String author_email;
    
    public String maintainer;
    public String maintainer_email;
    
    public String license_id;
    
    public String notes;
    public String url;
    
    public String version;
    public String state;
    public String type;

    public String owner_org;
    public String log_message;
    
    //Dovrebbe essere public;
    public Boolean privat;
    
    public List<Resource> resources;
    public List<Tag> tags;
    
    public String tag_string;
    
    public List<Extra> extras;
    
    public List<Relationship> relationships_as_object;
    public List<Relationship> relationships_as_subject;
   
    public List<Group> groups;
    
    public String license;
    public String license_title;
    public String license_url;
    
    public String metadata_created;
    public String metadata_modified;
    public String download_url;
    
    public String creator_user_id;
    
    public int num_resources;
    public int num_tags;
    
    public boolean isopen;
   
    public Group organization;
        
    //private String ckan_url;
    


    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRevision_id(String revision_id) {
        this.revision_id = revision_id;
    }

    public String getRevision_id() {
        return revision_id;
    }

    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer_email(String maintainer_email) {
        this.maintainer_email = maintainer_email;
    }

    public String getMaintainer_email() {
        return maintainer_email;
    }

    public void setLicense_id(String license_id) {
        this.license_id = license_id;
    }

    public String getLicense_id() {
        return license_id;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense_title(String license_title) {
        this.license_title = license_title;
    }

    public String getLicense_title() {
        return license_title;
    }

    public void setLicense_url(String license_url) {
        this.license_url = license_url;
    }

    public String getLicense_url() {
        return license_url;
    }

    public void setMetadata_created(String metadata_created) {
        this.metadata_created = metadata_created;
    }

    public String getMetadata_created() {
        return metadata_created;
    }

    public void setMetadata_modified(String metadata_modified) {
        this.metadata_modified = metadata_modified;
    }

    public String getMetadata_modified() {
        return metadata_modified;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    public boolean isIsopen() {
        return isopen;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

//    public void setCkan_url(String ckan_url) {
//        this.ckan_url = ckan_url;
//    }
//
//    public String getCkan_url() {
//        return ckan_url;
//    }

    public List<Extra> getExtras() {
    	if(extras==null)
    		return new ArrayList<Extra>();
    	else 
    		return extras;
    }

    public void setExtras( List<Extra> extras ) {
        this.extras = extras;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources( List<Resource> resources ) {
        this.resources = resources;
    }

    public String getOwner_org() {
		return owner_org;
	}
	public void setOwner_org(String owner_org) {
		this.owner_org = owner_org;
	}
	public String getLog_message() {
		return log_message;
	}
	public void setLog_message(String log_message) {
		this.log_message = log_message;
	}
	public Boolean getPriv() {
		return privat;
	}
	public void setPriv(Boolean priv) {
		this.privat = priv;
	}
	public String getTag_string() {
		return tag_string;
	}
	public void setTag_string(String tag_string) {
		this.tag_string = tag_string;
	}

	public List<Relationship> getRelationships_as_object() {
		return relationships_as_object;
	}
	public void setRelationships_as_object(List<Relationship> relationships_as_object) {
		this.relationships_as_object = relationships_as_object;
	}
	public List<Relationship> getRelationships_as_subject() {
		return relationships_as_subject;
	}
	public void setRelationships_as_subject(List<Relationship> relationships_as_subject) {
		this.relationships_as_subject = relationships_as_subject;
	}
	public String getCreator_user_id() {
		return creator_user_id;
	}
	public void setCreator_user_id(String creator_user_id) {
		this.creator_user_id = creator_user_id;
	}
	public int getNum_resources() {
		return num_resources;
	}
	public void setNum_resources(int num_resources) {
		this.num_resources = num_resources;
	}
	public int getNum_tags() {
		return num_tags;
	}
	public void setNum_tags(int num_tags) {
		this.num_tags = num_tags;
	}
	public Group getOrganization() {
		return organization;
	}
	public void setOrganization(Group organization) {
		this.organization = organization;
	}
	
	public String toString() {
        return "<Dataset:" + this.getName() + " ," + this.getTitle() + "," + this.getAuthor() + ", " + this.getUrl() + ">";
    }
}






