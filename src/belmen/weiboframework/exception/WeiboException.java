package belmen.weiboframework.exception;

public class WeiboException extends HttpException {

	private static final long serialVersionUID = 1L;

	public WeiboException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeiboException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public WeiboException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public WeiboException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
}
