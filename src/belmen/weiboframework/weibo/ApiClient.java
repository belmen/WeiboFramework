package belmen.weiboframework.weibo;

import belmen.weiboframework.exception.HttpException;
import belmen.weiboframework.exception.ApiException;
import belmen.weiboframework.http.HttpKit;
import belmen.weiboframework.http.HttpResponse;


public class ApiClient {

	public static final String TAG = ApiClient.class.getSimpleName();
	
	public HttpResponse sendRequest(ApiRequest request) throws ApiException {
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
			throw new ApiException(e.getMessage(), e);
		}
		if(isErrorResponse(request, response)) {
			if(!handleErrorResponse(request, response)) {
				throw parseErrorResponse(request, response);
			}
		}
		return response;
	}
	
	protected ApiRequest signRequest(ApiRequest request) {
		return request;
	}
	
	/**
	 * Determine whether an response is an error response or not.
	 * If yes, it will call {@link handleErrorResponse} to allow the client to handle the error.
	 * </br></br>
	 * Default implementation checks whether the status code of the response is 200.
	 * Rewrite this method to provide your specific definition of an error response.
	 * @param request the request
	 * @param the response of the request
	 * @return true if the response is an error response, false otherwise.
	 */
	protected boolean isErrorResponse(ApiRequest request, HttpResponse response) {
		return response.getStatusCode() != 200;
	}
	
	/**
	 * Provides an opportunity for the client to handle an error response.
	 * If the error cannot be handled, then it will call {@link parseErrorResponse} to throw an exception
	 * to the application.
	 * </br></br>
	 * Default implementation makes no attempt to handle the error and simply returns false.
	 * Rewrite this method to provide your fail safe mechanism.
	 * @param request the request
	 * @param response the error response of the request
	 * @return true if the error response is handled and exceptions will not be thrown, false otherwise.
	 */
	protected boolean handleErrorResponse(ApiRequest request, HttpResponse response) {
		return false;
	}

	/**
	 * Parses the error response and compose some error message that you want to tell the application.
	 * The exception returned here will be caught by the application as it calls the client to send request.
	 * </br></br>
	 * Default implementation returns the response content as the error message.
	 * Rewrite this method to provide more detailed and human readable message about the error.
	 * You can also return a superclass of {@link ApiException} to allow the application to catch
	 * errors in special conditions.
	 * @param request the request
	 * @param response the error response of the request, which cannot be handled by the client
	 * @return an {@link ApiException} or its superclass containing some detailed message about the error.
	 */
	protected ApiException parseErrorResponse(ApiRequest request, HttpResponse response) {
		return new ApiException(response.getContent());
	}
}
