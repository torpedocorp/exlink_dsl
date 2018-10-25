package kr.co.bizframe.exlink;

public class ExlinkDslException extends Exception {

	protected int _code;

	public static final int READABLE_EXCEPTION = 9900;

	public ExlinkDslException() {
		super();
	}

	public ExlinkDslException(String msg) {
		super(msg);
	}

	public ExlinkDslException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ExlinkDslException(Throwable cause) {
		super(cause);
	}

	public ExlinkDslException(int code) {
		super();
		_code = code;
	}

	public ExlinkDslException(int code, String msg) {
		super(msg);
		_code = code;
	}
	public ExlinkDslException(int code, String msg, Throwable cause) {
		super(msg, cause);
		_code = code;
	}

	public int getCode() {
		return _code;
	}
}
