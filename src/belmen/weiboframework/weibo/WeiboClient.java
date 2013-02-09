package belmen.weiboframework.weibo;

import belmen.weiboframework.exception.HttpException;
import belmen.weiboframework.exception.WeiboException;
import belmen.weiboframework.http.HttpKit;
import belmen.weiboframework.http.HttpResponse;


public abstract class WeiboClient {

	public static final String TAG = WeiboClient.class.getSimpleName();
	
	public HttpResponse sendRequest(WeiboRequest request) throws WeiboException {
		if(request == null) {
			return null;
		}
		signRequest(request);
		HttpResponse response = null;
		try {
			String url = request.getCompleteUrl();
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
		if(isErrorResponse(request, response)) {
			throw parseErrorResponse(request, response);
		}
		return response;
	}
	
	protected abstract WeiboRequest signRequest(WeiboRequest request);
	
	protected boolean isErrorResponse(WeiboRequest request, HttpResponse response) {
		return response.getStatusCode() != 200;
	}
	
	protected WeiboException parseErrorResponse(WeiboRequest request, HttpResponse response) {
		return new WeiboException(response.getContent());
	}
}
