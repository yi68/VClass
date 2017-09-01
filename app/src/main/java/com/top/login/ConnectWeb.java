package com.top.login;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.CookieManager;
import java.net.CookiePolicy;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.util.Log;

/**
 * @main LoginActivity所使用的方法
 * @author zym
 */

/**
 * @url http://139.199.68.51/vclass/user/login/
 * @url http://www.54lxb.cn/vclass/user/login/
 * @url http://139.199.68.51/vclass/client/connect/
 * @url http://139.199.68.51/vclass/passcode/
 * @url http://139.199.68.51/vclass/resources/default/heads/head.png
 * @HttpClient getHttpClient()单例多线
 */

public class ConnectWeb {
	private String url = "http://139.199.68.51/vclass/user/login/";
	private static DefaultHttpClient httpClient;
	public static OkHttpClient okHttpClient;
	private String body, body2;

	/**
	 * @main 初始化DefaultHttpClient
	 * @return
	 */
	public static DefaultHttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}

	// okHttp
	public static OkHttpClient getOkhttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient();
			okHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		}
		return okHttpClient;
	}

	/**
	 * @main 验证码操作
	 * @return HttpEntity
	 * @throws Exception
	 */
	// public HttpEntity getPasscode() throws Exception {
	// HttpEntity entity = null;
	// httpClient = getHttpClient();
	// HttpGet get = new HttpGet("http://139.199.68.51/vclass/passcode/");
	// HttpResponse response = httpClient.execute(get);
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// System.out.println("PassCodeCookie----->" +
	// httpClient.getCookieStore().toString());
	// entity = response.getEntity();
	// }
	// return entity;
	//
	// }

	/**
	 * @main 发送Post请求
	 * @param uname
	 * @param upawd
	 * @param passcode
	 * @return String
	 */
	// public String byPost(String uname, String upawd, String passcode) {
	// String result = "";
	// httpClient = getHttpClient();
	// HttpPost post = new HttpPost(url);
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("uname", uname));
	// params.add(new BasicNameValuePair("upawd", upawd));
	// params.add(new BasicNameValuePair("passcode", passcode));
	// try {
	// post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	// HttpResponse response = httpClient.execute(post);
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// CookieStore cookieStore = httpClient.getCookieStore();
	// System.out.println("CookieStore----->" + cookieStore.toString());
	// HttpEntity entity = response.getEntity();
	// post.abort();
	// result = EntityUtils.toString(entity);
	// System.out.println(result);
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// Log.i("UrlEncodedFormEntity", "编码实体内容错误：" + e.toString());
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// Log.i("execute", "请求失败：" + e.toString());
	// } catch (IOException e) {
	// e.printStackTrace();
	// Log.i("IO", "IO相关错误" + e.toString());
	// }
	// return result;
	//
	// }

}
