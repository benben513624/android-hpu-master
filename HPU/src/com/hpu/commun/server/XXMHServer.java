package com.hpu.commun.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;

import com.hpu.commun.domain.BorrowBook;
import com.hpu.commun.domain.Chengji;
import com.hpu.commun.domain.KeCheng;
import com.hpu.commun.utils.L;
import com.hpu.commun.utils.ListCache;
import com.hpu.commun.utils.TableUtil;

public class XXMHServer {

	// 信息门户网登录地址
	private final static String LOGIN_XXMH = "http://my.hpu.edu.cn/userPasswordValidate.portal";
	// 图书借阅网址
	private final static String BORROW_BOOK = "http://my.hpu.edu.cn/alone.portal?.pen=pe961&.pmn=view";
	// 信息门户网首页地址
	private final static String INDEX_XXMH = "http://my.hpu.edu.cn/index.portal";

	private static CookieStore cookies = null;

	/**
	 * 设置超时时间
	 * 
	 * @param httpclient
	 */
	private static void setConnTime(DefaultHttpClient httpclient) {
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 请求超时3s
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				60000);// 数据传输时间60s
	}

	/**
	 * 登录信息门户网
	 * 
	 * @param username
	 *            学号
	 * @param password
	 *            密码
	 * @return 200成功,400学号密码错误,404网络错误
	 */

	private static int LoginXxmh(String username, String password) {
		int result = 400;
		DefaultHttpClient client = new DefaultHttpClient();
		setConnTime(client);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("goto",
				"http://my.hpu.edu.cn/loginSuccess.portal"));
		parameters.add(new BasicNameValuePair("gotoOnFail",
				"http://my.hpu.edu.cn/loginFailure.portal"));
		parameters.add(new BasicNameValuePair("Login.Token1", username));
		parameters.add(new BasicNameValuePair("Login.Token2", password));
		HttpPost post = new HttpPost(LOGIN_XXMH);
		try {
			post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
			HttpResponse response = client.execute(post);
			cookies = client.getCookieStore();
			String str = EntityUtils.toString(response.getEntity(), "utf-8");
			if (str.contains("handleLoginSuccessed")) {
				result = 200;
			} else {
				result = 400;
			}
		} catch (Exception e) {
			result = 404;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 信息门户首页
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void index(final Activity activity, final String username,
			final String password,
			final OnResultListener<String> onResultListener) {
		new Thread() {
			public void run() {
				final int login = LoginXxmh(username, password);
				if (login == 200) {
					DefaultHttpClient client = new DefaultHttpClient();
					setConnTime(client);
					client.setCookieStore(cookies);
					HttpGet get = new HttpGet(INDEX_XXMH);
					HttpResponse response;
					try {
						response = client.execute(get);
						final String result = EntityUtils.toString(
								response.getEntity(), "utf-8");
						if (result.contains("我的首页")) {
							Document document = Jsoup.parse(result);
							Element element = document
									.getElementById("welcomeMsg");
							final String replace = element.text().replace(
									"欢迎您：", "");
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onSuccessed(replace);
									}
								});
							}
						} else {
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onError(400, "学号密码错误");
									}
								});
							}
						}
					} catch (Exception e) {
						if (onResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onResultListener.onError(404, "网络错误");
								}
							});
						}
						e.printStackTrace();
					}
				} else if (login == 400) {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								onResultListener.onError(login, "学号密码错误");
							}
						});
					}
				} else {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onResultListener.onError(login, "网络错误");
							}
						});
					}
				}
			};
		}.start();
	}

	/**
	 * 图书借阅查询
	 * 
	 * @param <TT>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void borrowBook(final Activity activity,
			final String username, final String password,
			final OnResultListener<List<BorrowBook>> onResultListener) {
		new Thread() {
			public void run() {
				final int login = LoginXxmh(username, password);
				if (login == 200) {
					DefaultHttpClient client = new DefaultHttpClient();
					setConnTime(client);
					client.setCookieStore(cookies);
					HttpGet get = new HttpGet(BORROW_BOOK);
					HttpResponse response;
					try {
						response = client.execute(get);
						final String result = EntityUtils.toString(
								response.getEntity(), "utf-8");
						if (result.contains("图书借阅信息")) {
							final List<BorrowBook> list = new ArrayList<BorrowBook>();
							// 解析字符串
							Document document = Jsoup.parse(result);
							Elements elements = document.getElementsByTag("tr");
							for (int i = 2; i < elements.size()
									&& elements.size() > 2; i++) {
								Elements elements2 = elements.get(i)
										.getElementsByTag("td");
								String num = elements2.get(0).text();
								String barcode = elements2.get(1).text();
								String name = elements2.get(2).text();
								String startdate = elements2.get(3).text();
								String borrowday = elements2.get(4).text();
								String renewday = elements2.get(5).text();
								String enddate = elements2.get(6).text();
								String isextended = elements2.get(7).text();
								L.i(num + barcode + name + startdate
										+ borrowday + renewday + enddate
										+ isextended);
								list.add(new BorrowBook(num, barcode, name,
										startdate, borrowday, renewday,
										enddate, isextended));
							}
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onSuccessed(list);
									}
								});
							}
						} else {
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onError(400, "学号密码错误");
									}
								});
							}

						}
					} catch (Exception e) {
						if (onResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onResultListener.onError(404, "暂无借阅图书");
								}
							});
						}
						e.printStackTrace();
					}
				} else if (login == 400) {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								onResultListener.onError(login, "学号密码错误");
							}
						});
					}
				} else {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onResultListener.onError(login, "网络错误");
							}
						});
					}
				}
			};
		}.start();
	}

	/**
	 * 获取成绩
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void getChengji(final Activity activity,
			final String username, final String password,
			final OnResultListener<List<Chengji>> onResultListener) {
		new Thread() {
			public void run() {
				final int login = LoginXxmh(username, password);
				if (login == 200) {
					L.i("登上了");
					DefaultHttpClient httpclient = new DefaultHttpClient();
					httpclient.setCookieStore(cookies);
					setConnTime(httpclient);
					HttpGet get = new HttpGet(
							"http://jw.hpu.edu.cn/szxyssoA.jsp?type=1");
					try {
						HttpResponse response = httpclient.execute(get);
						// **********此处得重新设置cookies
						cookies = httpclient.getCookieStore();
						String html = EntityUtils.toString(
								response.getEntity(), "GBK");
						L.i(html);
						if (html.contains("学分制综合教务")) {
							// 说明登上了教务处
							L.i("登上了教务处");
							httpclient.setCookieStore(cookies);
							setConnTime(httpclient);
							// get请求
							get = new HttpGet(
									"http://jw.hpu.edu.cn/gradeLnAllAction.do?type=ln&oper=qbinfo&lnxndm=2013-2014学年秋(两学期)");
							response = httpclient.execute(get);
							// 得到成绩源码
							String html2 = EntityUtils.toString(
									response.getEntity(), "GBK");
							L.i(html2);
							L.i("得到成绩网页");
							Document doc = Jsoup.parse(html2);
							String resString = "";
							Elements element1 = doc.getElementsByTag("table")
									.select("#user,#tblHead");
							Elements rows = element1.select("tr");
							for (Element element : rows) {
								resString += element
										.select("td")
										.text()
										.replaceAll(
												Jsoup.parse("&nbsp;").text(),
												" ");
								resString += "\n";
							}
							final ArrayList<Chengji> list = getChengjiList(resString);
							ListCache.save(activity.getFilesDir()
									.getAbsolutePath() + "/chengji", list);
							L.i("解析成绩成功");
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onSuccessed(list);
									}
								});
							}
						} else {
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onError(404, "网络错误");
									}
								});
							}
						}

					} catch (Exception e) {
						if (onResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onResultListener.onError(404, "网络错误");
								}
							});
						}

					}

				} else if (login == 400) {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								onResultListener.onError(login, "学号密码错误");
							}
						});
					}
				} else {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onResultListener.onError(login, "网络错误");
							}
						});
					}
				}

			};
		}.start();

	}

	/**
	 * 解析成绩
	 * 
	 * @param cjString
	 * @return
	 */
	private static ArrayList<Chengji> getChengjiList(String cjString) {

		ArrayList<Chengji> list = new ArrayList<Chengji>();

		if (cjString == null)
			return list;
		// System.out.println(cjString);
		String[] s1 = cjString.split("\n");
		// System.out.println(s1.length);
		for (String s2 : s1) {
			String[] s3 = s2.split(" ");
			int cnt = 0;
			if (s3.length > 0) {
				for (String s4 : s3) {
					if (s4.length() > 0)
						cnt++;
					// System.out.print(s4 + "*");
				}
				// cnt = 1,6的
				// System.out.println("cnt:" + cnt);
				if (cnt == 1) {// 学期
					Chengji chengji = new Chengji();
					chengji.setKcsx("");
					chengji.setXf("");
					chengji.setChengji("");
					int k = 0;
					for (String s4 : s3) {
						if (s4.length() > 0) {
							// System.out.println("----->>>" + s4);
							k++;
							switch (k) {
							case 1:
								chengji.setCname(s4);
								break;
							}
						}
					}
					list.add(chengji);
				} else if (cnt == 6) { // 成绩
					Chengji chengji = new Chengji();
					int k = 0;
					for (String s4 : s3) {
						if (s4.length() > 0) {
							// System.out.println("----->>>" + s4);
							k++;
							switch (k) {
							case 3:
								chengji.setCname(s4);
								break;
							case 4:
								chengji.setXf(s4);
								break;
							case 5:
								chengji.setKcsx(s4);
								break;
							case 6:
								chengji.setChengji(s4);
								break;
							}
						}
					}
					list.add(chengji);
				} else if (cnt == 7) { // 成绩
					Chengji chengji = new Chengji();
					int k = 0;
					String nn = "";
					for (String s4 : s3) {
						if (s4.length() > 0) {
							// System.out.println("----->>>" + s4);
							k++;
							switch (k) {
							case 3:
								nn = s4;
								break;
							case 4:
								chengji.setCname(nn + s4);
								break;
							case 5:
								chengji.setXf(s4);

								break;
							case 6:
								chengji.setKcsx(s4);
								break;
							case 7:
								chengji.setChengji(s4);
								break;
							}
						}
					}
					list.add(chengji);
				}
			}
		}
		return list;
	}

	/**
	 * 获取课表
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void getTable(final Activity activity, final String username,
			final String password,
			final OnResultListener<Map<Integer, KeCheng[]>> onResultListener) {
		new Thread() {
			public void run() {
				final int login = LoginXxmh(username, password);
				if (login == 200) {
					// System.out.println("登录成功");
					try {
						DefaultHttpClient httpclient = new DefaultHttpClient();
						setConnTime(httpclient);
						httpclient.setCookieStore(cookies);
						HttpGet get = new HttpGet(
								"http://jw.hpu.edu.cn/szxyssoA.jsp?type=1");
						HttpResponse response = httpclient.execute(get);
						cookies = httpclient.getCookieStore();
						String html = EntityUtils.toString(
								response.getEntity(), "GBK");
						if (html.contains("学分制综合教务")) {
							httpclient.setCookieStore(cookies);
							List<NameValuePair> parameters = new ArrayList<NameValuePair>();
							parameters.add(new BasicNameValuePair("zxjxjhh",
									"2014-2015-1-1"));
							HttpPost post = new HttpPost(
									"http://jw.hpu.edu.cn/lnkbcxAction.do");
							post.setEntity(new UrlEncodedFormEntity(parameters,
									"GBK"));
							HttpResponse response2 = httpclient.execute(post);
							// 得到网页源码
							html = EntityUtils.toString(response2.getEntity(),
									"GBK");
							// System.out.println("获取课表源码");
							Document document = Jsoup.parse(html);
							Element element = document
									.getElementsByTag("table").select("#user")
									.get(1);
							final StringBuffer sb = new StringBuffer();
							Elements rows = element.select("tr");
							for (Element element1 : rows) {
								Elements jcs = element1.select("td");
								for (int i = 0; i < jcs.size(); i++) {
									String text = jcs.get(i).text();
									if (!"".equals(text))
										sb.append(text + "#");
								}
								sb.append("\n");
							}
							ListCache.save(activity.getFilesDir()
									.getAbsolutePath() + "/table", sb
									.toString().trim());
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onSuccessed(TableUtil
												.getTable(username, sb
														.toString().trim()));
									}
								});
							}
						} else {
							if (onResultListener != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onResultListener.onError(404, "网络错误");
									}
								});
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						if (onResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onResultListener.onError(404, "网络错误");
								}
							});
						}
					}
				} else if (login == 400) {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								onResultListener.onError(login, "学号密码错误");
							}
						});
					}
				} else {
					if (onResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onResultListener.onError(login, "网络错误");
							}
						});
					}
				}

			};
		}.start();

	}

	/**
	 * 登录一卡通
	 * 
	 * @param username
	 * @param password
	 * @return 返回cookie
	 */
	public static CookieStore getXYW(final String username,
			final String password) {
		CookieStore cookieStore = null;
		// http://ecard.hpu.edu.cn/ids/GotoSelfSearch.aspx
		int loginXxmh = LoginXxmh(username, password);
		if (loginXxmh == 200) {
			// System.out.println("登录成功");
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient();
				setConnTime(httpclient);
				httpclient.setCookieStore(cookies);
				HttpGet get = new HttpGet(
						"http://ecard.hpu.edu.cn/ids/GotoSelfSearch.aspx");
				HttpResponse response = httpclient.execute(get);
				cookies = httpclient.getCookieStore();
				String html = EntityUtils.toString(response.getEntity(),
						"utf-8");
				// System.out.println(html);
				/**
				 * <form id='form' action=
				 * 'http://192.168.17.181/Selfplat/SSOLogin.aspx' METHOD=POST>
				 * <input type=hidden name='username' id='username'
				 * value='321309010323'/> <input type=hidden name='timestamp'
				 * id='timestamp' value='1418484674'/> <input type=hidden
				 * name='auid' id='auid'
				 * value='1552B3CE41F1C3DB3FF8076C1D4C6A3F'/> </form><script
				 * type= 'text/javascript'>document.getElementById('form').sub m
				 * i t ( ) ; < / s c r i p t >
				 */
				Document document = Jsoup.parse(html);
				String auid = document.getElementById("auid").val();
				String timestamp = document.getElementById("timestamp").val();
				String username2 = document.getElementById("username").val();
				/**
				 * 这里需要重新创建一个DefaultHttpClient
				 */
				DefaultHttpClient httpclient2 = new DefaultHttpClient();
				setConnTime(httpclient);
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("auid", auid));
				parameters.add(new BasicNameValuePair("timestamp", timestamp));
				parameters.add(new BasicNameValuePair("username", username2));
				HttpPost post = new HttpPost(
						"http://192.168.17.181/Selfplat/SSOLogin.aspx");
				post.setEntity(new UrlEncodedFormEntity(parameters));
				HttpResponse response2 = httpclient2.execute(post);
				EntityUtils.toString(response2.getEntity(), "utf-8");
				cookieStore = httpclient2.getCookieStore();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}
		return cookieStore;
	}

	public interface OnResultListener<T> {
		void onSuccessed(T result);

		void onError(int errorCode, String error);
	}
}
