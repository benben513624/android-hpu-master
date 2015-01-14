package com.hpu.commun.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpu.commun.Constant;
import com.hpu.commun.domain.KeCheng;

/**
 * @author LeeLay 2014-9-25
 */

public class TableUtil {

	/**
	 * 将教务课表转成Map
	 * 
	 * @param stunum
	 *            学号
	 * 
	 * @param str
	 *            课表源字符串
	 * 
	 * @return
	 */
	public static Map<Integer, KeCheng[]> getTable(String stunum, String str) {
		String[] split = str.trim().split("\n");
		List<KeCheng> list = new ArrayList<KeCheng>();
		int count = 0;
		int colorCnt = 0;
		for (int i = 0; i < split.length; i++) {
			// System.out.println(split[i] + "是在这里吗");
			String[] split2 = split[i].split("#");
			if (split2.length == 17) {
				// System.out.println(i + "等于17的个数");
			} else if (split2.length == 10) {
				// System.out.println(i + "等于10的个数");
			} else if (split2.length == 7) {
				// System.out.println(i + "等于7的个数");
			}

			if (split2.length > 10) {// 长的
				count = 0;
				colorCnt++;
				int parseInt = Integer.parseInt(split2[13].trim());
				for (int k = 0; k < parseInt / 2; k++) {
					int x = Integer.parseInt(split2[11].trim());// 星期
					int y = (Integer.parseInt(split2[12].trim()) + 1) / 2 + k;// 节次
					KeCheng kecheng = new KeCheng();
					kecheng.setStunum(stunum);
					kecheng.setName(split2[2].trim());
					kecheng.setCredit(split2[4].trim());
					kecheng.setKind(split2[6].trim());
					kecheng.setTeacher(split2[7].replace("*", "").trim());
					kecheng.setColor(Constant.COLORS[colorCnt
							% Constant.COLORS.length]);
					kecheng.setWeekly(CharUtil.replaceCN(split2[10].trim()));
					kecheng.setWeek(split2[11].trim());
					kecheng.setSection(y);
					kecheng.setBuilding(split2[15].trim());
					kecheng.setClassroom(split2[16].trim());
					int position = (y - 1) * 7 + x - 1;
					kecheng.setPosition(position);
					list.add(kecheng);
				}
			} else if (split2.length == 7) {
				int parseInt = Integer.parseInt(split2[3].trim());
				for (int k = 0; k < parseInt / 2; k++) {
					KeCheng kecheng = new KeCheng();
					int x = Integer.parseInt(split2[1].trim());
					int y = (Integer.parseInt(split2[2]) + 1) / 2 + k;
					kecheng.setStunum(stunum);
					kecheng.setColor(Constant.COLORS[(colorCnt)
							% Constant.COLORS.length]);
					kecheng.setName(split[i - count - 1].split("#")[2].trim());
					kecheng.setCredit(split[i - count - 1].split("#")[4].trim());
					kecheng.setKind(split[i - count - 1].split("#")[6].trim());
					kecheng.setTeacher(split[i - count - 1].split("#")[7]
							.replace("*", "").trim());
					kecheng.setWeekly(CharUtil.replaceCN(split2[0].trim()));
					kecheng.setWeek(split2[1].trim());
					kecheng.setSection(y);
					kecheng.setBuilding(split2[5].trim());
					kecheng.setClassroom(split2[6].trim());
					int position = (y - 1) * 7 + x - 1;
					kecheng.setPosition(position);
					list.add(kecheng);
				}
				count++;
			} else if (split2.length == 10) {
				KeCheng kecheng = new KeCheng();
				kecheng.setStunum(stunum);
				kecheng.setName(split2[2].trim());
				kecheng.setCredit(split2[4].trim());
				kecheng.setKind(split2[6].trim());
				kecheng.setTeacher(split2[7].replace("*", "").trim());
				kecheng.setColor(Constant.COLORS[colorCnt
						% Constant.COLORS.length]);
				kecheng.setPosition(-1);
				list.add(kecheng);
			}
		}
		Map<Integer, KeCheng[]> map = new HashMap<Integer, KeCheng[]>();
		for (int i = 0; i < list.size(); i++) {
			if (map.entrySet() != null
					&& map.containsKey(list.get(i).getPosition())) {
				KeCheng[] keChengs = map.get(list.get(i).getPosition());
				KeCheng[] keChengs1 = { keChengs[0], list.get(i) };
				map.put(list.get(i).getPosition(), keChengs1);
			} else {
				KeCheng[] keChengs = new KeCheng[] { list.get(i) };
				map.put(list.get(i).getPosition(), keChengs);
			}
		}
		return map;
	}
}
