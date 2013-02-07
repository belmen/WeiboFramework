package belmen.weiboframework.http;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HttpResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TAG = HttpResponse.class.getSimpleName();
	
	private final int statusCode;
	private final String content;
	private final Map<String, List<String>> headerFields;
	
	public HttpResponse(int statusCode, String content) {
		this.statusCode = statusCode;
		this.content = content;
		this.headerFields = null;
	}
	
	public HttpResponse(int statusCode, String content,
			Map<String, List<String>> headerFields) {
		this.statusCode = statusCode;
		this.content = content;
		this.headerFields = headerFields;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getContent() {
		return content;
	}

	public Map<String, List<String>> getHeaderFields() {
		return headerFields;
	}
}
