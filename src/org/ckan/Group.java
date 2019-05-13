package org.ckan;

import java.net.URL;
import java.util.List;

/**
 * Represents a CKAN group
 *
 * @author      Ross Jones <ross.jones@okfn.org>
 * @version     1.7
 * @since       2012-05-01
 */
public class Group {

    public class Response {
        public boolean success;
        public Group result;
    }


    private String id;
    private String revision_id;
    private String name;
    private String title;
    private String description;
    private String image_url;
    private String image_display_url;
    private String type;
    private String state;
    private String created;
    private Boolean is_organization;
    private String approval_status;
    
    private List<Extra> extras;
    
    private String capacity;
    private List<Package> packages;
    

    public Group() {}

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

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public String getRevision_id() {
		return revision_id;
	}

	public void setRevision_id(String revision_id) {
		this.revision_id = revision_id;
	}

	public String getImage_display_url() {
		return image_display_url;
	}

	public void setImage_display_url(String image_display_url) {
		this.image_display_url = image_display_url;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getIs_organization() {
		return is_organization;
	}

	public void setIs_organization(Boolean is_organization) {
		this.is_organization = is_organization;
	}

	public String toString() {
        return "<Group: " + this.getName() + ", " + this.getTitle() + "  (" + this.getType()+ ")>";
    }

}






