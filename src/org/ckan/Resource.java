package org.ckan;

import java.net.URL;

/**
 * Represents a single resource within a Dataset
 *
 * @author      Ross Jones <ross.jones@okfn.org>
 * @version     1.7
 * @since       2012-05-01
 */
public class Resource{


    private String id;
    private String revision_id;
    private String package_id;
    private String url;
    private String description;
    private String format;
    private String hash;
    private String state;
    private int position;
    private String name;
    private String resource_type;
    private String url_type;
    private String mimetype;
    private String mimetype_inner;
    private String cache_url;
    private int size;
    private String created;
    private String last_modified;
    private String cache_last_updated;
    //private String tracking_summary;
    private Boolean datastore_active;
    
    //Non ci sono in https://github.com/ckan/ckan/blob/master/ckan/logic/schema.py
//    private String resource_group_id;
//    private String webstore_url;
//    private String webstore_last_updated;
//    
    
    
    
    
    
    
        
/*tracking_summary: {
total: 3,
recent: 3
},*/
    
    public Resource() {}

//    public String getResource_group_id() {
//        return resource_group_id;
//    }
//
//    public void setResource_group_id(String resource_group_id) {
//        this.resource_group_id = resource_group_id;
//    }
    
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

//    public void setWebstore_url(String webstore_url) {
//        this.webstore_url = webstore_url;
//    }
//
//    public String getWebstore_url() {
//        return webstore_url;
//    }

    public void setCache_last_updated(String cache_last_updated) {
        this.cache_last_updated = cache_last_updated;
    }

    public String getCache_last_updated() {
        return cache_last_updated;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_id() {
        return package_id;
    }

//    public void setWebstore_last_updated(String webstore_last_updated) {
//        this.webstore_last_updated = webstore_last_updated;
//    }
//
//    public String getWebstore_last_updated() {
//        return webstore_last_updated;
//    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype_inner(String mimetype_inner) {
        this.mimetype_inner = mimetype_inner;
    }

    public String getMimetype_inner() {
        return mimetype_inner;
    }

    public void setCache_url(String cache_url) {
        this.cache_url = cache_url;
    }

    public String getCache_url() {
        return cache_url;
    }

	public String getRevision_id() {
		return revision_id;
	}

	public void setRevision_id(String revision_id) {
		this.revision_id = revision_id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl_type() {
		return url_type;
	}

	public void setUrl_type(String url_type) {
		this.url_type = url_type;
	}

//	public String getTracking_summary() {
//		return tracking_summary;
//	}
//
//	public void setTracking_summary(String tracking_summary) {
//		this.tracking_summary = tracking_summary;
//	}

	public Boolean getDatastore_active() {
		return datastore_active;
	}

	public void setDatastore_active(Boolean datastore_active) {
		this.datastore_active = datastore_active;
	}

    
}






