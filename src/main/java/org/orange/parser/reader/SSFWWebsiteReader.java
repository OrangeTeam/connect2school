package org.orange.parser.reader;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.orange.parser.parser.Constant;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SSFWWebsiteReader extends AbstractLoginReader {

	public SSFWWebsiteReader() {
		mLoginConnection = Jsoup.connect(Constant.url.LOGIN_PAGE);
		mReadConnection = Jsoup.connect(Constant.url.PERSONAL_INFORMATION);
		init();
	}
	private void init() {
		mLoginConnection.followRedirects(false).ignoreHttpErrors(true);
	}


	@Override
	public void setAccount(String accountName, String password) {
		super.setAccount(accountName, password);
		mLoginConnection.request().data().clear();
		mLoginConnection.data("Login.Token1",accountName,"Login.Token2", password);
	}

	@Override
	public boolean doLogin() throws IOException {
		Connection.Response response =
				mLoginConnection.method(Connection.Method.POST).execute();
		String body = response.body();
		int status = response.statusCode();
		if(status == HttpURLConnection.HTTP_OK) {
			if(body.matches("(?si:.*(success|succeed).*)")) { //成功登录
				setCookiesFromResponse(response);
				setCookiesFromResponse(mLoginConnection, response);
				fetchSessionCookie();
				return true;
			} else { //用户名或密码错误
				return false;
			}
		} else {
			throw new HttpStatusException(
					String.format("收到未知的服务器响应，请确认与教务处网站的连通性。Header:\n%s\nBody:\n%s",
							response.headers(), body),
					status, mLoginConnection.request().url().toString());
		}
	}

	/**
	 * 取得教务处网站（含课程信息等）的会话cookie
	 * <p>前置条件：已{@link #doLogin()}</p>
	 * @throws IOException
	 */
	private void fetchSessionCookie() throws IOException {
		Connection.Response response = mLoginConnection
				.url(Constant.url.TEACHING_AFFAIRS_SESSION_PAGE)
				.method(Connection.Method.GET).execute();
		setCookiesFromResponse(response);
	}
}
