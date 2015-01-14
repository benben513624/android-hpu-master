package com.hpu.commun.server;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
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
import android.text.TextUtils;

import com.hpu.commun.domain.ECardUserInfo;
import com.hpu.commun.domain.EConsume;
import com.hpu.commun.domain.EDeposit;
import com.hpu.commun.domain.ETotal;
import com.hpu.commun.domain.EZcRecord;
import com.hpu.commun.utils.HPUtil;
import com.hpu.commun.utils.SDUtil;

public class ECardServer {

	private static CookieStore cookies = null;
	private static String viewState = null;
	private static String eventaltdation = null;
	private static SimpleDateFormat format;
	private static Calendar calendar;
	private static DecimalFormat df;

	/**
	 * ���ó�ʱʱ��
	 * 
	 * @param httpclient
	 */
	private static void setConnTime(DefaultHttpClient httpclient) {
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// ����ʱ3s
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				20000);// ���ݴ���ʱ��20s
	}

	/**
	 * ��Ȩ��¼һ��ͨ(�ڲ�����)
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private static int loginECard(String username, String password) {
		CookieStore cookieStore = XXMHServer.getXYW(username, password);
		if (cookieStore != null) {
			cookies = cookieStore;
			return 200;
		} else {
			return 400;
		}
	}

	/**
	 * ��ȡһ��ͨ��ҳ(����Ϣ)
	 * 
	 * @param username
	 * @param password
	 */
	public static void HomePage(final Activity activity, final String username,
			final String password,
			final OnEcardResultListener<ECardUserInfo> onEcardResultListener) {
		new Thread() {
			public void run() {
				int loginECard = loginECard(username, password);
				if (loginECard == 200 && cookies != null) {
					try {
						DefaultHttpClient httpclient = new DefaultHttpClient();
						httpclient.setCookieStore(cookies);
						setConnTime(httpclient);
						HttpGet get = new HttpGet(
								"http://192.168.17.181/Selfplat/User/Home.aspx");
						HttpResponse response = httpclient.execute(get);
						String html = EntityUtils.toString(
								response.getEntity(), "utf-8");
						Document document2 = Jsoup.parse(html);
						Elements elements = document2.getElementsByTag("span");
						String num = elements.get(0).text().replace("���ţ�", "");
						;// ѧ��
						String estate = elements.get(1).text();// ��״̬
						String name = elements.get(2).text().replace("������", "");// ����
						String emoney = elements.get(3).text()
								.replace("��Ǯ��", "").replace("��", "");// ���
						String sex = elements.get(4).text();// �Ա�
						String eassistance = elements.get(5).text();// �������
						String status = elements.get(6).text();// �������
						get = new HttpGet(
								"http://192.168.17.181/Selfplat/User/Photo.ashx");
						response = httpclient.execute(get);
						InputStream inputStream = response.getEntity()
								.getContent();
						FileOutputStream fos = new FileOutputStream(
								SDUtil.getProjectDir() + "/photo.jpg");
						byte[] temp = new byte[1024];
						int len = 0;
						while ((len = inputStream.read(temp)) != -1) {
							fos.write(temp, 0, len);
						}
						if (fos != null) {
							fos.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
						final ECardUserInfo eCardUserInfo = new ECardUserInfo(
								num, name, sex, status, emoney, estate,
								eassistance);
						if (onEcardResultListener != null) {
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									onEcardResultListener
											.onSuccessed(eCardUserInfo);
								}
							});
						}

					} catch (Exception e) {
						e.printStackTrace();
						if (onEcardResultListener != null) {
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									onEcardResultListener.onError(401, "��¼��ʱ");
								}
							});
						}
					} finally {

					}
				} else {
					if (onEcardResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onEcardResultListener.onError(400, "��¼ʧ��");
							}
						});
					}
				}
			};
		}.start();
	}

	private static int getZC(String startdate, String enddate, int index,
			List<EZcRecord> list) throws Exception {
		int count = 0;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);
		if (index == 1) {
			HttpGet get = new HttpGet(
					"http://192.168.17.181/Selfplat/User/DoorInfo.aspx");
			HttpResponse response = httpclient.execute(get);
			String string = EntityUtils.toString(response.getEntity(), "utf-8");
			Document document = Jsoup.parse(string);
			if (response.getStatusLine().getStatusCode() == 302) {
				return 302;
			}
			viewState = document.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document.getElementById("__EVENTVALIDATION")
					.attributes().get("value");

		}
		HttpPost post = new HttpPost(
				"http://192.168.17.181/Selfplat/User/DoorInfo.aspx");
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		if (index > 1) {
			parameters
					.add(new BasicNameValuePair("__EVENTARGUMENT", index + ""));
			parameters.add(new BasicNameValuePair("__EVENTTARGET",
					"ctl00$ContentPlaceHolder1$AspNetPager1"));
		}
		parameters.add(new BasicNameValuePair("__EVENTVALIDATION",
				eventaltdation));
		parameters.add(new BasicNameValuePair("__VIEWSTATE", viewState));
		if (index == 1) {
			parameters.add(new BasicNameValuePair(
					"ctl00$ContentPlaceHolder1$btnSearch", "��  ѯ"));
		}
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$rbtnType", "2"));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtEndDate", enddate));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtStartDate", startdate));
		post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		String result = EntityUtils.toString(httpclient.execute(post)
				.getEntity(), "utf-8");
		Document document2 = Jsoup.parse(result);
		Element element = document2
				.getElementById("ContentPlaceHolder1_gridView");
		if (element.text().contains("û�з��ַ�������������")) {
			// ����Ϊ��
			count = 0;
		} else {
			viewState = document2.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document2.getElementById("__EVENTVALIDATION")
					.attributes().get("value");
			Elements elements = element.getElementsByTag("tr");
			int temp = 0;
			for (int i = 1; i < elements.size(); i++) {
				if (!TextUtils.isEmpty(elements.get(i).text().trim())) {
					String datestr = elements.get(i).getElementsByTag("td")
							.get(1).text();
					format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date2 = format.parse(datestr);
					calendar = Calendar.getInstance();

					calendar.setTime(date2);
					df = new java.text.DecimalFormat("00");
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					String date = df.format(calendar.get(Calendar.MONTH) + 1)
							+ "-" + df.format(calendar.get(Calendar.DATE))
							+ HPUtil.WEEK[(week - 2) == -1 ? 6 : week - 2];
					String time = df.format(calendar.get(Calendar.HOUR_OF_DAY))
							+ ":" + df.format(calendar.get(Calendar.MINUTE))
							+ ":" + df.format(calendar.get(Calendar.SECOND));
					String num = elements.get(i).getElementsByTag("td").get(2)
							.text();
					System.out.println(date + time + num);
					list.add(new EZcRecord(date, time, num));
					temp++;
				}
			}
			count = temp;
			Element element2 = document2
					.getElementById("ContentPlaceHolder1_AspNetPager1");
			if (element2 == null) {
				// û����һҳ
				count = 0;
			}
		}
		return count;

	}

	/**
	 * ��ٿ���(��ʵ�ַ�ҳ)
	 * 
	 * @param activity
	 * @param startdate
	 * @param enddate
	 * @param onEcardResultListener
	 */
	public static void ZCRecord(final Activity activity,
			final String startdate, final String enddate,
			final OnEcardResultListener<List<EZcRecord>> onEcardResultListener) {
		new Thread() {
			public void run() {
				if (cookies != null) {
					try {
						final List<EZcRecord> list = new ArrayList<EZcRecord>();
						int i = 1;
						int count = 0;
						while ((count = getZC(startdate, enddate, i, list)) == 10) {
							i++;
						}
						if (count == 302) {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onError(302,
												"��½��ʱ");
									}
								});
						} else {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onSuccessed(list);
									}
								});
						}
					} catch (Exception e) {
						e.printStackTrace();
						// ����ʲô��ȡ��¼Ϊ��
						if (onEcardResultListener != null) {
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									onEcardResultListener.onError(400, "��ѯʧ��");
								}
							});
						}
					}
				} else {
					// cookieΪ��
					if (onEcardResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onEcardResultListener.onError(400, "��ѯʧ��");
							}
						});
					}
				}
			};
		}.start();
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param startdate
	 * @param enddate
	 * @param index
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private static int getXF(String startdate, String enddate, int index,
			List<EConsume> list) throws Exception {
		int count = 0;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);
		if (index == 1) {
			HttpGet get = new HttpGet(
					"http://192.168.17.181/Selfplat/User/ConsumeInfo.aspx");
			HttpResponse response = httpclient.execute(get);
			if (response.getStatusLine().getStatusCode() == 302) {
				return 302;
			}
			String string = EntityUtils.toString(response.getEntity(), "utf-8");
			Document document = Jsoup.parse(string);
			viewState = document.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document.getElementById("__EVENTVALIDATION")
					.attributes().get("value");

		}
		HttpPost post = new HttpPost(
				"http://192.168.17.181/Selfplat/User/ConsumeInfo.aspx");
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		if (index > 1) {
			parameters
					.add(new BasicNameValuePair("__EVENTARGUMENT", index + ""));
			parameters.add(new BasicNameValuePair("__EVENTTARGET",
					"ctl00$ContentPlaceHolder1$AspNetPager1"));
		}

		parameters.add(new BasicNameValuePair("__EVENTVALIDATION",
				eventaltdation));
		parameters.add(new BasicNameValuePair("__VIEWSTATE", viewState));
		if (index == 1) {
			parameters.add(new BasicNameValuePair(
					"ctl00$ContentPlaceHolder1$btnSearch", "��  ѯ"));
		}
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$rbtnType", "0"));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtEndDate", enddate));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtStartDate", startdate));
		post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		String result = EntityUtils.toString(httpclient.execute(post)
				.getEntity(), "utf-8");
		Document document2 = Jsoup.parse(result);
		Element element = document2
				.getElementById("ContentPlaceHolder1_gridView");
		if (element.text().contains("û�з��ַ�������������")) {
			// ����Ϊ��
			count = 0;
		} else {
			viewState = document2.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document2.getElementById("__EVENTVALIDATION")
					.attributes().get("value");

			Elements elements = element.getElementsByTag("tr");
			int temp = 0;
			for (int i = 1; i < elements.size(); i++) {
				if (!TextUtils.isEmpty(elements.get(i).text().trim())) {
					Elements tag = elements.get(i).getElementsByTag("td");
					String time = tag.get(0).text();
					String type = tag.get(1).text();
					String money = tag.get(2).text();
					String restmoney = tag.get(3).text();
					String place = tag.get(5).text();
					String terminal = tag.get(6).text();
					list.add(new EConsume(time, type, money, restmoney, place,
							terminal));
					temp++;
				}
			}
			count = temp;
			Element element2 = document2
					.getElementById("ContentPlaceHolder1_AspNetPager1");
			if (element2 == null) {
				// û����һҳ
				count = 0;
			}
		}
		return count;

	}

	/**
	 * ���Ѽ�¼
	 * 
	 * @param startdate
	 * @param enddate
	 * @param onEcardResultListener
	 */
	public static void XFRecord(final Activity activity,
			final String startdate, final String enddate,
			final OnEcardResultListener<List<EConsume>> onEcardResultListener) {
		new Thread() {
			public void run() {
				if (cookies != null) {
					try {
						final List<EConsume> list = new ArrayList<EConsume>();
						int i = 1;
						int count = 0;
						while ((count = getXF(startdate, enddate, i, list)) == 10) {
							i++;
						}
						if (count == 302) {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onError(302,
												"��½��ʱ");
									}
								});
						} else {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onSuccessed(list);
									}
								});
						}
					} catch (Exception e) {
						e.printStackTrace();
						// ����ʲô��ȡ��¼Ϊ��
						if (onEcardResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onEcardResultListener.onError(400, "��ѯʧ��");
								}
							});
						}
					}
				} else {
					// cookieΪ��
					if (onEcardResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onEcardResultListener.onError(400, "��ѯʧ��");
							}
						});
					}
				}
			};
		}.start();
	}

	/**
	 * ��ȡ��ҳ����
	 * 
	 * @param startdate
	 * @param enddate
	 * @param index
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private static int getTT(String startdate, String enddate, int index,
			List<ETotal> list) throws Exception {
		int count = 0;
		if (index == 1) {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			setConnTime(httpclient);
			httpclient.setCookieStore(cookies);
			HttpGet get = new HttpGet(
					"http://192.168.17.181/Selfplat/User/CustStateInfo.aspx");
			HttpResponse response = httpclient.execute(get);
			if (response.getStatusLine().getStatusCode() == 302) {
				return 302;
			}
			String string = EntityUtils.toString(response.getEntity(), "utf-8");
			Document document = Jsoup.parse(string);
			viewState = document.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document.getElementById("__EVENTVALIDATION")
					.attributes().get("value");

		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);
		HttpPost post = new HttpPost(
				"http://192.168.17.181/Selfplat/User/CustStateInfo.aspx");
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		if (index > 1) {
			parameters
					.add(new BasicNameValuePair("__EVENTARGUMENT", index + ""));
			parameters.add(new BasicNameValuePair("__EVENTTARGET",
					"ctl00$ContentPlaceHolder1$AspNetPager1"));
		}

		parameters.add(new BasicNameValuePair("__EVENTVALIDATION",
				eventaltdation));
		parameters.add(new BasicNameValuePair("__VIEWSTATE", viewState));
		if (index == 1) {
			parameters.add(new BasicNameValuePair(
					"ctl00$ContentPlaceHolder1$btnSearch", "��  ѯ"));
		}
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$rbtnType", "0"));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtEndDate", enddate));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtStartDate", startdate));
		post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		String result = EntityUtils.toString(httpclient.execute(post)
				.getEntity(), "utf-8");
		Document document2 = Jsoup.parse(result);
		Element element = document2
				.getElementById("ContentPlaceHolder1_gridView");
		if (element.text().contains("û�з��ַ�������������")) {
			// ����Ϊ��
			count = 0;
		} else {
			// ��һ������Ҫ
			viewState = document2.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document2.getElementById("__EVENTVALIDATION")
					.attributes().get("value");
			Elements elements = element.getElementsByTag("tr");
			int temp = 0;
			for (int i = 1; i < elements.size(); i++) {
				if (!TextUtils.isEmpty(elements.get(i).text().trim())) {
					Elements tag = elements.get(i).getElementsByTag("td");
					String type = tag.get(1).text();
					String money = tag.get(2).text();
					list.add(new ETotal(type, money));
					temp++;
				}
			}
			count = temp;
			Element element2 = document2
					.getElementById("ContentPlaceHolder1_AspNetPager1");
			if (element2 == null) {
				// û����һҳ
				count = 0;
			}
		}
		return count;

	}

	/**
	 * ��ȡ���ѻ���(��ʵ�ַ�ҳ)
	 * 
	 * @param startdate
	 * @param enddate
	 * @param onEcardResultListener
	 */
	public static void TTRecord(final Activity activity,
			final String startdate, final String enddate,
			final OnEcardResultListener<List<ETotal>> onEcardResultListener) {
		new Thread() {
			public void run() {
				if (cookies != null) {
					try {
						final List<ETotal> list = new ArrayList<ETotal>();
						int i = 1;
						int count = 0;
						while ((count = getTT(startdate, enddate, i, list)) == 10) {
							i++;
						}
						if (count == 302) {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onError(302,
												"��½��ʱ");
									}
								});
						} else {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onSuccessed(list);
									}
								});
						}
					} catch (Exception e) {
						e.printStackTrace();
						// ����ʲô��ȡ��¼Ϊ��
						if (onEcardResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onEcardResultListener.onError(400, "��ѯʧ��");
								}
							});
						}
					}
				} else {
					// cookieΪ��
					if (onEcardResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onEcardResultListener.onError(400, "��ѯʧ��");
							}
						});
					}
				}
			};
		}.start();
	}

	/**
	 * ��ȡ��ֵ����
	 * 
	 * @param startdate
	 * @param enddate
	 * @param index
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private static int getDeposit(String startdate, String enddate, int index,
			List<EDeposit> list) throws Exception {
		int count = 0;
		if (index == 1) {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			setConnTime(httpclient);
			httpclient.setCookieStore(cookies);
			HttpGet get = new HttpGet(
					"http://192.168.17.181/Selfplat/User/depositinfo.ASPX");
			HttpResponse response = httpclient.execute(get);
			if (response.getStatusLine().getStatusCode() == 302) {
				return 302;
			}
			String string = EntityUtils.toString(response.getEntity(), "utf-8");
			Document document = Jsoup.parse(string);
			viewState = document.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document.getElementById("__EVENTVALIDATION")
					.attributes().get("value");

		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		setConnTime(httpclient);
		httpclient.setCookieStore(cookies);
		HttpPost post = new HttpPost(
				"http://192.168.17.181/Selfplat/User/depositinfo.ASPX");
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		if (index > 1) {
			parameters
					.add(new BasicNameValuePair("__EVENTARGUMENT", index + ""));
			parameters.add(new BasicNameValuePair("__EVENTTARGET",
					"ctl00$ContentPlaceHolder1$AspNetPager1"));
		}

		parameters.add(new BasicNameValuePair("__EVENTVALIDATION",
				eventaltdation));
		parameters.add(new BasicNameValuePair("__VIEWSTATE", viewState));
		if (index == 1) {
			parameters.add(new BasicNameValuePair(
					"ctl00$ContentPlaceHolder1$btnSearch", "��  ѯ"));
		}
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$rbtnType", "0"));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtEndDate", enddate));
		parameters.add(new BasicNameValuePair(
				"ctl00$ContentPlaceHolder1$txtStartDate", startdate));
		post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		String result = EntityUtils.toString(httpclient.execute(post)
				.getEntity(), "utf-8");
		Document document2 = Jsoup.parse(result);
		Element element = document2
				.getElementById("ContentPlaceHolder1_gridView");
		if (element.text().contains("û�з��ַ�������������")) {
			// ����Ϊ��
			count = 0;
		} else {
			// ��һ������Ҫ
			viewState = document2.getElementById("__VIEWSTATE").attributes()
					.get("value");
			eventaltdation = document2.getElementById("__EVENTVALIDATION")
					.attributes().get("value");
			Elements elements = element.getElementsByTag("tr");
			int temp = 0;
			for (int i = 1; i < elements.size(); i++) {
				if (!TextUtils.isEmpty(elements.get(i).text().trim())) {
					Elements tag = elements.get(i).getElementsByTag("td");
					String datestr = tag.get(0).text();
					String money = tag.get(2).text();
					String re_money = tag.get(3).text();
					String terminal = tag.get(5).text();

					format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date2 = format.parse(datestr);
					calendar = Calendar.getInstance();
					calendar.setTime(date2);
					df = new java.text.DecimalFormat("00");
					String date = df.format(calendar.get(Calendar.MONTH) + 1)
							+ "-" + df.format(calendar.get(Calendar.DATE));
					list.add(new EDeposit(date, money, re_money, terminal));
					temp++;
				}
			}
			count = temp;
			Element element2 = document2
					.getElementById("ContentPlaceHolder1_AspNetPager1");
			if (element2 == null) {
				// û����һҳ
				count = 0;
			}
		}
		return count;

	}

	/**
	 * ��ȡ��ֵ����(��ʵ�ַ�ҳ)
	 * 
	 * @param startdate
	 * @param enddate
	 * @param onEcardResultListener
	 */
	public static void DepositRecord(final Activity activity,
			final String startdate, final String enddate,
			final OnEcardResultListener<List<EDeposit>> onEcardResultListener) {
		new Thread() {
			public void run() {
				if (cookies != null) {
					try {
						final List<EDeposit> list = new ArrayList<EDeposit>();
						int i = 1;
						int count = 0;
						while ((count = getDeposit(startdate, enddate, i, list)) == 10) {
							i++;
						}
						if (count == 302) {
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onError(302,
												"��½��ʱ");
									}
								});
						} else {
							// �������
							Collections.reverse(list);
							if (onEcardResultListener != null)
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										onEcardResultListener.onSuccessed(list);
									}
								});
						}
					} catch (Exception e) {
						e.printStackTrace();
						// ����ʲô��ȡ��¼Ϊ��
						if (onEcardResultListener != null) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onEcardResultListener.onError(400, "��ѯʧ��");
								}
							});
						}
					}
				} else {
					// cookieΪ��
					if (onEcardResultListener != null) {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								onEcardResultListener.onError(400, "��ѯʧ��");
							}
						});
					}
				}
			};
		}.start();
	}

	public interface OnEcardResultListener<T> {
		void onSuccessed(T result);

		void onError(int errorCode, String error);

	}
}
