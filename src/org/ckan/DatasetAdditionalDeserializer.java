package org.ckan;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DatasetAdditionalDeserializer implements JsonDeserializer<Dataset>{

	@Override
	public Dataset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		Dataset res = Client.gson.fromJson(json, Dataset.class);
		JSONObject j = new JSONObject(json.toString());
		List<String> extraKeys = res.getExtras().stream().map(x -> x.getKey()).collect(Collectors.toList()); 
		for(String k : j.keySet()) {
			
			if("private".equals(k)) {
				res.setPriv(j.optBoolean(k, false));
			}else if(!doesObjectContainField(k)) {
				if(!extraKeys.contains(k)) {
					System.out.println(k+" "+j.get(k).toString());
					res.getExtras().add(new Extra(k, j.get(k).toString()));
				}
			}
		}
		//System.out.println(res.getExtras().toString());
		return res;
	}

	public boolean doesObjectContainField(String fieldName) {
	    return Arrays.stream(Dataset.class.getFields())
	            .anyMatch(f -> f.getName().equals(fieldName));
	}
	
}
