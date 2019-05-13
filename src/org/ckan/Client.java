package org.ckan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
//import it.eng.rspa.odf.beans.ODMSNodeForbiddenException;
//import it.eng.rspa.odf.beans.ODMSNodeNotFoundException;
//import it.eng.rspa.odf.beans.ODMSNodeOfflineException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.*;

/**
 * The primary interface to this package the Client class is responsible for
 * managing all interactions with a given connection.
 *
 * @author Ross Jones <ross.jones@okfn.org>
 * @version 1.7
 * @since 2012-05-01
 */
public final class Client {

	private Connection _connection = null;
	private static Logger logger = LogManager.getLogger(Client.class);
	public static Gson gson = new Gson();
	private Gson gsonExtras = new GsonBuilder().registerTypeAdapter(Dataset.class, new DatasetAdditionalDeserializer()).create();
	/**
	 * Constructs a new Client for making requests to a remote CKAN instance.
	 *
	 * @param c
	 *            A Connection object containing info on the location of the
	 *            CKAN Instance.
	 * @param apikey
	 *            A user's API Key sent with every request.
	 */
	public Client(Connection c, String apikey) {
		this._connection = c;
		this._connection.setApiKey(apikey);
	}

	/**
	 * Loads a JSON string into a class of the specified type.
	 */
	protected <T> T LoadClass(Class<T> cls, String data) {
		return gsonExtras.fromJson(data, cls);
	}

	/**
	 * Handles error responses from CKAN
	 *
	 * When given a JSON string it will generate a valid CKANException
	 * containing all of the error messages from the JSON.
	 *
	 * @param json
	 *            The JSON response
	 * @param action
	 *            The name of the action calling this for the primary error
	 *            message.
	 * @throws A
	 *             CKANException containing the error messages contained in the
	 *             provided JSON.
	 */
	private void HandleError(String json, String action) throws CKANException {

		CKANException exception = new CKANException("Errors occured performing: " + action);

		HashMap hm = LoadClass(HashMap.class, json);
		Map<String, Object> m = (Map<String, Object>) hm.get("error");
		for (Map.Entry<String, Object> entry : m.entrySet()) {
			if (entry.getKey().startsWith("_"))
				continue;

			exception.addError(entry.getValue() + " - " + entry.getKey());
		}
		throw exception;
	}

	/**
	 * Retrieves a dataset
	 *
	 * Retrieves the dataset with the given name, or ID, from the CKAN
	 * connection specified in the Client constructor.
	 *
	 * @param name
	 *            The name or ID of the dataset to fetch
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @returns The Dataset for the provided name.
	 * @throws A
	 *             CKANException if the request fails
	 */
	public Dataset getDataset(String name) throws CKANException, MalformedURLException {

		String returned_json = null;
		try {
			returned_json = this._connection.Post("/api/action/package_show", "{\"id\":\"" + name + "\"}");
		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}
		// logger.info(returned_json);
		if (!returned_json.startsWith("{")) {

			if (returned_json.matches(".*The requested URL could not be retrieved.*")) {
				// throw new ODMSNodeNotFoundException(" The ODMS host does not
				// exist");
				throw new CKANException("The ODMS host does not exist");
			} else if (returned_json.contains("403")) {
				// throw new ODMSNodeForbiddenException(" The ODMS node is
				// forbidden");
				throw new CKANException(" The ODMS node is forbidden");
			} else {
				// throw new ODMSNodeOfflineException(" The ODMS node is
				// currently unreachable");
				throw new CKANException(" The ODMS node is currently unreachable");
			}
		}

		Dataset.Response r = LoadClass(Dataset.Response.class, returned_json);
		if (!r.success) {
			if (!returned_json.matches(".*Access denied.*"))
				HandleError(returned_json, "getDataset");
			else
				return null;
		}
		return r.result;
	}

	/**
	 * Deletes a dataset
	 *
	 * Deletes the dataset specified with the provided name/id
	 *
	 * @param name
	 *            The name or ID of the dataset to delete
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @throws A
	 *             CKANException if the request fails
	 */
	public void deleteDataset(String name) throws CKANException, MalformedURLException {

		String returned_json = null;

		try {
			returned_json = this._connection.Post("/api/action/package_delete", "{\"id\":\"" + name + "\"}");
		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}

		Dataset.Response r = LoadClass(Dataset.Response.class, returned_json);
		if (!r.success) {
			HandleError(returned_json, "deleteDataset");
		}
	}

	/**
	 * Creates a dataset on the server
	 *
	 * Takes the provided dataset and sends it to the server to perform an
	 * create, and then returns the newly created dataset.
	 *
	 * @param dataset
	 *            A dataset instance
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @returns The Dataset as it now exists
	 * @throws A
	 *             CKANException if the request fails
	 */
	public Dataset createDataset(Dataset dataset) throws CKANException, MalformedURLException {

		Gson gson = new Gson();
		String data = gson.toJson(dataset);
		String returned_json = null;

		try {
			returned_json = this._connection.Post("/api/action/package_create", data);
		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}

		Dataset.Response r = LoadClass(Dataset.Response.class, returned_json);
		if (!r.success) {
			// This will always throw an exception
			HandleError(returned_json, "createDataset");
		}
		return r.result;
	}

	/**
	 * Retrieves a group
	 *
	 * Retrieves the group with the given name, or ID, from the CKAN connection
	 * specified in the Client constructor.
	 *
	 * @param name
	 *            The name or ID of the group to fetch
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @returns The Group instance for the provided name.
	 * @throws A
	 *             CKANException if the request fails
	 */
	public Group getGroup(String name) throws CKANException, MalformedURLException {

		String returned_json = null;

		try {

			returned_json = this._connection.Post("/api/action/group_show", "{\"id\":\"" + name + "\"}");

		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}

		Group.Response r = LoadClass(Group.Response.class, returned_json);
		if (!r.success) {
			HandleError(returned_json, "getGroup");
		}
		return r.result;
	}

	/**
	 * Deletes a Group
	 *
	 * Deletes the group specified with the provided name/id
	 *
	 * @param name
	 *            The name or ID of the group to delete
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @throws A
	 *             CKANException if the request fails
	 */
	public void deleteGroup(String name) throws CKANException, MalformedURLException {

		String returned_json = null;

		try {
			returned_json = this._connection.Post("/api/action/group_delete", "{\"id\":\"" + name + "\"}");
		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}

		Group.Response r = LoadClass(Group.Response.class, returned_json);
		if (!r.success) {
			HandleError(returned_json, "deleteGroup");
		}
	}

	/**
	 * Creates a Group on the server
	 *
	 * Takes the provided Group and sends it to the server to perform an create,
	 * and then returns the newly created Group.
	 *
	 * @param group
	 *            A Group instance
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @returns The Group as it now exists on the server
	 * @throws A
	 *             CKANException if the request fails
	 */
	public Group createGroup(Group group) throws CKANException, MalformedURLException {
		Gson gson = new Gson();
		String data = gson.toJson(group);
		String returned_json = null;

		try {
			returned_json = this._connection.Post("/api/action/package_create", data);
		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}

		Group.Response r = LoadClass(Group.Response.class, returned_json);
		if (!r.success) {
			// This will always throw an exception
			HandleError(returned_json, "createGroup");
		}
		return r.result;
	}

	/**
	 * Uses the provided search term to find datasets on the server
	 *
	 * Takes the provided query and locates those datasets that match the query
	 *
	 * @param query
	 *            The search terms
	 * @throws MalformedURLException
	 * @throws ODMSNodeOfflineException
	 * @throws ODMSNodeNotFoundException
	 * @throws ODMSNodeForbiddenException
	 * @throws SocketTimeoutException
	 * @returns A SearchResults object that contains a count and the objects
	 * @throws A
	 *             CKANException if the request fails
	 */
	public Dataset.SearchResults findDatasets(String query, String start, String rows, String sort)
			throws CKANException, MalformedURLException {

		String payload = "{";
		if (!(sort.trim().equals("") || sort == null))
			payload += "\"sort\":\"" + sort + "\",";
		if (!(rows.trim().equals("") || rows == null))
			payload += "\"rows\":\"" + rows + "\",";
		if (!(start.trim().equals("") || start == null))
			payload += "\"start\":\"" + start + "\",";
		payload += "\"fq\":\"private:false\",";
		payload += "\"q\":\"" + query + "\"}";

		String returned_json = null;
		int attempts = 0;
		boolean stop = true;
		do {
			try {
				returned_json = this._connection.Post("/api/action/package_search", payload);

			} catch (UnknownHostException e) {
				throw new CKANException(" The ODMS host does not exist");
			} catch (SocketTimeoutException e) {
				throw new CKANException(" The ODMS node is currently unreachable");
			} catch (IOException e) {
				throw new CKANException(e.getMessage());
			}

			if (returned_json.startsWith("{")) {
				stop = false;
			} else {
				if (query.contains("metadata_modified") && sort.equals("metadata_modified asc")
						&& rows.equals("10000000")) {
					attempts++;
					logger.info("Exception attempt n: " + attempts);
					if (attempts == 5)
						stop = false;
				} else {
					stop = false;
				}
			}

		} while (stop);

		if (!returned_json.startsWith("{")) {

			if (returned_json.matches(".*The requested URL could not be retrieved.*")) {
				// throw new ODMSNodeNotFoundException(" The ODMS host does not
				// exist");
				throw new CKANException(" The ODMS host does not exist");
			} else if (returned_json.contains("403")) {
				// throw new ODMSNodeForbiddenException(" The ODMS node is
				// forbidden");
				throw new CKANException(" The ODMS node is forbidden");
			} else {
				// throw new ODMSNodeOfflineException(" The ODMS node is
				// currently unreachable");
				throw new CKANException(" The ODMS node is currently unreachable");
			}
		}

		Dataset.SearchResponse sr = LoadClass(Dataset.SearchResponse.class, returned_json);
		if (!sr.success) {
			// This will always throw an exception
			HandleError(returned_json, "findDatasets");
		}

		return sr.result;
	}

	/**
	 * Retrieves all datasets ID
	 *
	 * 
	 *
	 * @param name
	 *            The name or ID of the dataset to fetch
	 * @throws MalformedURLException
	 * @throws ODMSNodeOfflineException
	 * @throws SocketTimeoutException
	 * @returns The Dataset for the provided name.
	 * @throws A
	 *             CKANException if the request fails
	 */
	public String[] getAllDatasetsID() throws CKANException, MalformedURLException {

		String returned_json = null;
		try {
			returned_json = this._connection.Post("/api/action/package_list", "");
		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}
		if (!returned_json.startsWith("{")) {
			// throw new ODMSNodeOfflineException(" The ODMS node is currently
			// unreachable");
			throw new CKANException(" The ODMS node is currently unreachable");
		}

		Dataset.IDListResponse r = LoadClass(Dataset.IDListResponse.class, returned_json);
		if (!r.success) {
			HandleError(returned_json, "getAllDatasetsID");
		}
		return r.result;
	}

	/**
	 * Retrieves all datasets metadata
	 *
	 * 
	 *
	 * @param name
	 *            The name or ID of the dataset to fetch
	 * @throws MalformedURLException
	 * @throws ODMSNodeOfflineException
	 * @throws ODMSNodeNotFoundException
	 * @throws ODMSNodeForbiddenException
	 * @returns The Dataset for the provided name.
	 * @throws A
	 *             CKANException if the request fails
	 */
	public List<Dataset> getAllDatasets(String offset, String limit)
			throws CKANException, MalformedURLException, SocketTimeoutException {
		logger.info("GET ALL DATASET CLIENT");

		String returned_json = null;

		
		try {
			returned_json = this._connection.Post("/api/action/current_package_list_with_resources",
					"{\"limit\":\"" + limit + "\",\"offset\":\"" + offset + "\"}");
			logger.info("GET ALL DATASET CLIENT ------- END");

		} catch (UnknownHostException e) {
			throw new CKANException(" The ODMS host does not exist");
		} catch (SocketTimeoutException e) {
			throw new CKANException(" The ODMS node is currently unreachable");
		} catch (IOException e) {
			throw new CKANException(e.getMessage());
		}

		if (!returned_json.startsWith("{")) {
			if (returned_json.matches(".*The requested URL could not be retrieved.*")
					|| returned_json.matches(".*does not exist.*")) {
				// throw new ODMSNodeNotFoundException(" The ODMS host does not
				// exist");
				throw new CKANException(" The ODMS host does not exist");
			} else if (returned_json.contains("403")) {
				// throw new ODMSNodeForbiddenException(" The ODMS node is
				// forbidden");
				throw new CKANException(" The ODMS node is forbidden");
			} else {
				// throw new ODMSNodeOfflineException(" The ODMS node is
				// currently unreachable");
				throw new CKANException(" The ODMS node is currently unreachable");
			}
		}

		logger.info("CLIENT -> Load CLASS");
		Dataset.FirstSynchResults r = LoadClass(Dataset.FirstSynchResults.class, returned_json);
		if (!r.success) {
			logger.info("ERROR");
			HandleError(returned_json, "getAllDatasets");
		}
		logger.info("CLIENT -> RETURN");
		return r.result;
	}

	/*
	 * OLD APPROACH WITH RECENTLY CHANGED PACKAGE ACTIVITY LIST /** Retrieves
	 * all recent activities on datasets of a node
	 *
	 * Makes an Hashmap where every key-value pair is a corrispondence between
	 * Dataset ID and related activity on it, starting from a passed starting
	 * Date.
	 * 
	 * @param name The string representing the starting date compliant to ISO
	 * 8601 standard
	 * 
	 * @throws ParseException
	 * 
	 * @throws JSONException
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws ODMSNodeOfflineException
	 * 
	 * @throws SocketTimeoutException
	 * 
	 * @returns HashMap<String,String> a Map of ID,activity_type pairs
	 * 
	 * @throws A CKANException if the request fails
	 *//*
		 * public HashMap<String,String> getChangedDatasetsID(String
		 * startingDateString) throws CKANException, JSONException,
		 * ParseException, MalformedURLException, ODMSNodeOfflineException,
		 * SocketTimeoutException {
		 * 
		 * System.out.println(startingDateString); HashMap<String,String>
		 * changedDatasets = new HashMap<String, String>(); SimpleDateFormat sdf
		 * = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //
		 * sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // GregorianCalendar
		 * startingDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		 * GregorianCalendar startingDate = new GregorianCalendar();
		 * startingDate.setTime(sdf.parse(startingDateString)); JSONArray
		 * changedArray; String packageID, packageActivity;
		 * 
		 * 
		 * 
		 * int offset=0; boolean stop=false; // stop flag for the global request
		 * boolean retry; // retry flag for each API request int retryNum = 0;
		 * // number of retry for each API request String returned_json;
		 * 
		 * while(!stop){
		 * 
		 * retry = true; // Try to perform the API request, for a max number set
		 * by retryNum do{ // Send the API request returned_json =
		 * this._connection.Post(
		 * "/api/action/recently_changed_packages_activity_list",
		 * "{\"limit\":10000,\"offset\":"+offset+"}");
		 * if(returned_json.startsWith("{")) retry=false; else{ retryNum++;
		 * logger.info("Exception attempt n: "+ retryNum); if(retryNum==5)
		 * retry=false; } }while(retry);
		 * 
		 * 
		 * if(!returned_json.startsWith("{")) throw new
		 * ODMSNodeOfflineException(" The ODMS node is currently unreachable");
		 * 
		 * 
		 * changedArray = new JSONObject(returned_json).getJSONArray("result");
		 * System.out.println(changedArray.length()); for(int
		 * i=0;i<changedArray.length();i++){ JSONObject obj =
		 * changedArray.getJSONObject(i); String timestampObj =
		 * obj.getJSONObject("data").getJSONObject("package").getString(
		 * "metadata_modified"); // String timestampObj =
		 * obj.getString("timestamp");//.getJSONObject("package").getString(
		 * "metadata_modified"); JSONObject packageObj =
		 * obj.getJSONObject("data").getJSONObject("package"); GregorianCalendar
		 * objectDate = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		 * objectDate.setLenient(false);
		 * objectDate.setTimeInMillis(sdf.parse(timestampObj.substring(0,
		 * timestampObj.length()-7)+"Z").getTime());
		 * 
		 * packageID = packageObj.getString("id"); packageActivity =
		 * obj.getString("activity_type");
		 * 
		 * 
		 * if(objectDate.after(startingDate) &&
		 * !packageActivity.contains("deleted")){
		 * 
		 * 
		 * // Checks if changedDataset was created and then deleted from
		 * startingDate // If true this dataset is not to be added to (or
		 * removed from) changedDatasets. // In addition, because retrieved
		 * actions are ordered from newest to oldest, // if a dataset is marked
		 * as deleted, previous actions should not to be added // in changed
		 * datasets list. // In addition if a dataset is newly created, only
		 * this action is to be added // except for deletion
		 * 
		 * if ( changedDatasets.containsKey(packageID)){ if(
		 * changedDatasets.get(packageID).equals("deleted package") &&
		 * packageActivity.equals("new package")){
		 * changedDatasets.remove(packageID); continue; } else if (
		 * changedDatasets.get(packageID).equals("deleted package") &&
		 * packageActivity.equals("changed package") ) continue; else if (
		 * changedDatasets.get(packageID).equals("changed package") &&
		 * packageActivity.equals("new package") ) continue; else if (
		 * changedDatasets.get(packageID).equals("changed package") &&
		 * packageActivity.equals("changed package") ) continue; }
		 * changedDatasets.put(packageID,packageActivity); }else if
		 * (packageActivity.contains("deleted")){
		 * changedDatasets.put(packageID,packageActivity); }else{ //I dataset
		 * sono ordinati per metadata_modified desc // quindi al primo dataset
		 * con objectDate.before(startingDate) possiamo fermarci stop=true;
		 * break; } }
		 * 
		 * if(changedArray.length()==0){ stop=true; break; }
		 * 
		 * offset+=changedArray.length(); changedArray = null; } System.gc();
		 * return changedDatasets;
		 * 
		 * }
		 * 
		 * 
		 */

}
