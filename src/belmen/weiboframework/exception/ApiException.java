package belmen.weiboframework.exception;

public class ApiException extends HttpException {

	private static final long serialVersionUID = 1L;

	public ApiException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public ApiException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public ApiException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
}
