package org.ckan;

import java.net.URL;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.logging.log4j.*;
import org.apache.http.entity.StringEntity;

/**
 * Connection holds the connection details for this session
 *
 * @author Ross Jones <ross.jones@okfn.org>
 * @version 1.7
 * @since 2012-05-01
 */
public final class Connection {

	private String m_host;
	private int m_port;
	private String _apikey = null;
	private static Properties proxyProps;
	private static Logger logger = LogManager.getLogger(Connection.class);

	static {
		proxyProps = new Properties();
		try {
			proxyProps.load(Connection.class.getClassLoader().getResourceAsStream("configuration.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Connection() {
	}

	public Connection(String host) {
		this.m_host = host;
	}

	public Connection(String host, int port) {
		if (!Pattern.matches(".*:(\\d.*)", host))
			this.m_host = host;
		else
			this.m_host = host.split(".*:(\\d.*)")[0];
		this.m_port = port;
		
		try {
			URL u = new URL(this.m_host + ":" + this.m_port + "/api");
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
			logger.info(mue);
		}

	}

	public void setApiKey(String key) {
		this._apikey = key;
	}

	/**
	 * Makes a POST request
	 *
	 * Submits a POST HTTP request to the CKAN instance configured within the
	 * constructor, returning tne entire contents of the response.
	 *
	 * @param path
	 *            The URL path to make the POST request to
	 * @param data
	 *            The data to be posted to the URL
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 * @returns The String contents of the response
	 * @throws A
	 *             CKANException if the request fails
	 */
	protected String Post(String path, String data) throws MalformedURLException, SocketTimeoutException {
		URL url = null;
		String body = "";

		logger.info("CONNECTION: OPEN");
		System.out.println(this.m_host + path + "\t" + data);
		url = new URL(this.m_host + path);

		/*
		 * TEST NUOVO HTTP CLIENT // RequestConfig requestConfig =
		 * RequestConfig.custom().setConnectTimeout(300 * 1000).build(); // int
		 * CONNECTION_TIMEOUT = 300 * 1000; // timeout in millis //
		 * RequestConfig requestConfig = RequestConfig.custom() //
		 * .setConnectionRequestTimeout(CONNECTION_TIMEOUT) //
		 * .setConnectTimeout(CONNECTION_TIMEOUT) //
		 * .setSocketTimeout(CONNECTION_TIMEOUT) // .build(); // HttpClient
		 * httpclient =
		 * HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).
		 * build();
		 */
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 300000);
		// if(!(data.contains("\"rows\":\"1\"") ||
		// data.contains("\"rows\":\"0\"") || path.contains("package_list")) )
		// HttpConnectionParams.setSoTimeout(httpParams, 6);
		// else
		HttpConnectionParams.setSoTimeout(httpParams, 900000);

		HttpClient httpclient = new DefaultHttpClient(httpParams);

		/*
		 * Set an HTTP proxy if it is specified in system properties.
		 * 
		 * http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.
		 * html
		 * http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org
		 * /apache/http/examples/client/ClientExecuteProxy.java
		 */

		if (Boolean.parseBoolean(proxyProps.getProperty("http.proxyEnabled").trim()) && StringUtils.isNotBlank(proxyProps.getProperty("http.proxyHost").trim())) {

			int port = 80;
			if (isSet(proxyProps.getProperty("http.proxyPort"))) {
				port = Integer.parseInt(proxyProps.getProperty("http.proxyPort"));
			}
			HttpHost proxy = new HttpHost(proxyProps.getProperty("http.proxyHost"), port, "http");
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			if (isSet(proxyProps.getProperty("http.proxyUser"))) {
				((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(
						new AuthScope(proxyProps.getProperty("http.proxyHost"), port),
						(Credentials) new UsernamePasswordCredentials(proxyProps.getProperty("http.proxyUser"),
								proxyProps.getProperty("http.proxyPassword")));
			}
		}
		try {

			HttpPost postRequest = new HttpPost(url.toString());

			// postRequest.setConfig(requestConfig);
			postRequest.setHeader("X-CKAN-API-Key", this._apikey);

			StringEntity input = new StringEntity(data,"UTF-8");

			input.setContentType("application/json");
			input.setContentEncoding("UTF-8");
			postRequest.setEntity(input);

			HttpResponse response = httpclient.execute(postRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String result = "";
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			body = result.toString();

		} catch (UnsupportedOperationException | IOException e) {
			if (e.getClass().equals(SocketTimeoutException.class) || e.getClass().equals(ConnectException.class))
				throw new SocketTimeoutException(e.getMessage());
			e.printStackTrace();
		}

		return body;
	}

	private static boolean isSet(String string) {
		return string != null && string.length() > 0;
	}
}
