package com.hpu.commun.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

import com.hpu.commun.domain.Notice;
import com.hpu.commun.domain.Report;

public class CommonServer {
	// ѧ������·��
	private final static String REPORT_PATH = "http://my.hpu.edu.cn/AcademicReportList.jsp";
	// ���¹���·��
	private final static String NOTE_PATH = "http://my.hpu.edu.cn/LatestNewsList.jsp";

	/**
	 * ���ó�ʱʱ��
	 * 
	 * @param httpclient
	 */
	private static void setConnTime(DefaultHttpClient httpclient) {
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// ����ʱ3s
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				60000);// ���ݴ���ʱ��60s
	}

	/**
	 * ��ȡ���н���
	 * 
	 * @param dqweek
	 *            ��ǰ���ڼ�(1-7)
	 * @param dqzhou
	 *            ��ǰ�ܴ�(1-20)
	 * @param jxzhxh
	 *            ��ǰѧ��2014-2015-1-1
	 * @param lh
	 *            ��ѧ¥��
	 * @return
	 * @throws Exception
	 */
	public static void getClassRoom(final Activity activity,
			final String dqweek, final String dqzhou, final String jxzhxh,
			final String lh, final OnResultListener<String> onResultListener) {
		new Thread() {
			public void run() {
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					setConnTime(client);
					HttpPost post = new HttpPost(
							"http://my.hpu.edu.cn/jw/kxjschx.jsp");
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("dqweek", dqweek));
					parameters.add(new BasicNameValuePair("dqzhou", dqzhou));
					parameters.add(new BasicNameValuePair("jxzhxh", jxzhxh));
					parameters.add(new BasicNameValuePair("lh", lh));
					post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
					HttpResponse response = client.execute(post);
					String rusult = EntityUtils.toString(response.getEntity(),
							"utf-8");
					Document document = Jsoup.parse(rusult);
					Elements elements = document.getElementsByTag("tr");
					final StringBuffer sb = new StringBuffer();
					for (int i = 0; i < elements.size() && elements.size() > 11; i++) {
						if (i == 1 || i == 3 || i == 6 || i == 8 || i == 11) {
							// ֻ��ȡ����
							if (i == elements.size() - 1) {
								sb.append(elements.get(i)
										.getElementsByTag("td").get(1).text());
							} else {
								sb.append(elements.get(i)
										.getElementsByTag("td").get(1).text()
										+ "#");
							}
						}
					}
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onResultListener.onSuccessed(sb.toString().trim());

						}
					});
				} catch (Exception e) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onResultListener.onError(404, "�������");
						}
					});
				}

			};
		}.start();

	}

	/**
	 * ��ȡѧ������
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void getReport(final Activity activity,
			final OnResultListener<List<Report>> onResultListener) {

		new Thread() {
			public void run() {
				try {

					DefaultHttpClient client = new DefaultHttpClient();
					setConnTime(client);
					HttpGet get = new HttpGet(REPORT_PATH);
					HttpResponse response = client.execute(get);
					String rusult = EntityUtils.toString(response.getEntity(),
							"utf-8");
					Document document = Jsoup.parse(rusult);
					Elements elements = document.getElementsByTag("ul");
					final List<Report> list = new ArrayList<Report>();
					for (int i = 0; i < elements.size(); i++) {
						String href = elements.get(i).getElementsByTag("a")
								.get(0).attr("href");
						Elements lis = elements.get(i).getElementsByTag("li");
						String title = lis.get(0).text();
						String author = lis.get(1).text();
						String date = lis.get(2).text();
						String address = lis.get(3).text();
						list.add(new Report(href, title, author, date, address));
					}
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onResultListener.onSuccessed(list);
						}
					});

				} catch (Exception e) {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onResultListener.onError(404, "�������");
						}
					});
				}

			};

		}.start();

	}

	/**
	 * ��ȡ���¹���
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void getNote(final Activity activity,
			final OnResultListener<List<Notice>> onResultListener) {

		new Thread() {
			public void run() {
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					setConnTime(client);
					HttpGet get = new HttpGet(NOTE_PATH);
					HttpResponse response = client.execute(get);
					String string = EntityUtils.toString(response.getEntity(),
							"utf-8");
					Document document = Jsoup.parse(string);
					Elements elements = document.getElementsByTag("ul").get(0)
							.getElementsByTag("li");
					final List<Notice> list = new ArrayList<Notice>();
					for (int i = 0; i < elements.size(); i++) {
						Element a = elements.get(i).getElementsByTag("a")
								.get(0);
						String href = a.attr("href");
						String title = a.attr("title");
						String date = elements.get(i).getElementsByTag("span")
								.get(0).text();
						list.add(new Notice(href, title, date));
					}
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onResultListener.onSuccessed(list);
						}
					});
				} catch (Exception e) {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onResultListener.onError(404, "�������");
						}
					});
					e.printStackTrace();
				}

			};
		}.start();

	}

	public interface OnResultListener<T> {
		void onSuccessed(T result);

		void onError(int errorCode, String error);
	}
}
