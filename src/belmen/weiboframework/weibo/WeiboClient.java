package belmen.weiboframework.weibo;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import belmen.weiboframework.exception.HttpException;
import belmen.weiboframework.exception.WeiboException;
import belmen.weiboframework.http.HttpKit;
import belmen.weiboframework.http.HttpRequest;
import belmen.weiboframework.http.HttpResponse;
import belmen.weiboframework.util.Codec;


public abstract class WeiboClient {

	public static final String TAG = WeiboClient.class.getSimpleName();
	
	protected abstract HttpRequest signRequest(HttpRequest request);
	
	protected HttpResponse sendRequest(HttpRequest request) throws WeiboException {
		if(request == null) {
			return null;
		}
		HttpResponse response = null;
		try {
			String url = getCompleteUrl(request.getUrl(), request.getQueryParams());
			switch (request.getMethod()) {
			case GET:
				response = HttpKit.get(url, request.getHeaders());
				break;
			case POST:
				break;
			}
		} catch (HttpException e) {
			throw new WeiboException(e.getMessage(), e);
		}
		return handleResponse(response);
	}
	
	protected HttpResponse handleResponse(HttpResponse response) throws WeiboException {
		return response;
	}
	
	private static final String getCompleteUrl(String url, Map<String, String> params) {
		if(params == null) {
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		sb.append(url.contains("?") ? "&" : "?");
		Iterator<Entry<String, String>> iter = params.entrySet().iterator();
		Entry<String, String> entry;
		String name, value;
		while(iter.hasNext()) {
			entry = iter.next();
			name = entry.getKey();
			value = entry.getValue();
			sb.append(Codec.urlEncode(name)).append("=").append(Codec.urlEncode(value));
		}
		return sb.toString();
	}
}
