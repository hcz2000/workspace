package com.zhan.myreader.util;

import com.zhan.myreader.callback.HttpCallback;
import com.zhan.myreader.callback.ResultCallback;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.String.valueOf;

/**
 * Created by zhao on 2016/4/16.
 */

public class HttpUtil {

	private static String sessionid;
	private static OkHttpClient mClient;

	static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	} };

	private static SSLSocketFactory createSSLSocketFactory() {
		SSLSocketFactory ssfFactory = null;

		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			ssfFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
		}

		return ssfFactory;
	}

	private static synchronized OkHttpClient getOkHttpClient() {
		if (mClient == null) {
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(30000, TimeUnit.SECONDS);
			builder.sslSocketFactory(createSSLSocketFactory(), (X509TrustManager) trustAllCerts[0]);
			builder.hostnameVerifier((hostname, session) -> true);
			mClient = builder.build();
		}
		return mClient;
	}

	public static String makeURL(String p_url, Map<String, Object> params) {
		if (params == null)
			return p_url;
		StringBuilder url = new StringBuilder(p_url);

		if (url.indexOf("?") < 0)
			url.append('?');
		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			try {
				url.append(valueOf(params.get(name)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return url.toString().replace("?&", "?");
	}

	/**
	 * Trust every server - dont check for any certificate
	 */
	public static void trustAllHosts() {
		final String TAG = "trustAllHosts";
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void httpGet_Async(final String address, final HttpCallback callback) {
		new Thread(() -> {
			try {
				OkHttpClient client = getOkHttpClient();
				Request request = new Request.Builder().url(address).build();
				Response response = client.newCall(request).execute();
				callback.onFinish(response.body().byteStream());
			} catch (Exception e) {
				e.printStackTrace();
				callback.onError(e);
			}
		}).start();
	}

	public static String httpGet_Sync(final String address) {

		try {
			OkHttpClient client = getOkHttpClient();
			Request request = new Request.Builder().url(address).build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post请求
	 * 
	 * @param address
	 * @param output
	 * @param callback
	 */
	public static void httpPost_Async(final String address, final String output, final HttpCallback callback) {
		new Thread(new Runnable() {
			HttpURLConnection connection = null;

			@Override
			public void run() {
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setConnectTimeout(60 * 1000);
					connection.setReadTimeout(60 * 1000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					if (output != null) {
						DataOutputStream out = new DataOutputStream(connection.getOutputStream());
						out.writeBytes(output);
					}
					InputStream in = connection.getInputStream();
					if (callback != null) {
						callback.onFinish(in);
					}
				} catch (Exception e) {
					if (callback != null) {
						callback.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * http请求 (get)
	 * 
	 * @param url
	 * @param callback
	 */
	public static void httpGet_Async(String url, final String charsetName, final ResultCallback callback) {
		httpGet_Async(url, new HttpCallback() {
			@Override
			public void onFinish(InputStream in) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, charsetName));
					StringBuilder response = new StringBuilder();
					String line = reader.readLine();
					while (line != null) {
						response.append(line);
						line = reader.readLine();
					}
					if (callback != null) {
						callback.onFinish(response.toString(), 0);
					}
				} catch (Exception e) {
					callback.onError(e);
				}
			}

			@Override
			public void onError(Exception e) {
				if (callback != null) {
					callback.onError(e);
				}
			}

		});
	}

	/**
	 * http请求 (post)
	 * 
	 * @param url
	 * @param output
	 * @param callback
	 */
	public static void httpPost_Async(String url, String output, final ResultCallback callback) {
		httpPost_Async(url, output, new HttpCallback() {
			@Override
			public void onFinish(InputStream in) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					StringBuilder response = new StringBuilder();
					String line = reader.readLine();
					while (line != null) {
						response.append(line);
						line = reader.readLine();
					}
					if (callback != null) {
						callback.onFinish(response.toString(), 0);
					}
				} catch (Exception e) {
					callback.onError(e);
				}
			}

			@Override
			public void onError(Exception e) {
				if (callback != null) {
					callback.onError(e);
				}
			}
		});
	}

	public static void httpGet_Async(String url, Map<String, Object> params, String charsetName,
			final ResultCallback callback) {
		HttpUtil.httpGet_Async(HttpUtil.makeURL(url, params), charsetName, new ResultCallback() {
			@Override
			public void onFinish(Object o, int code) {
				callback.onFinish(o, code);
			}

			@Override
			public void onError(Exception e) {
				callback.onError(e);
			}
		});
	}

}
