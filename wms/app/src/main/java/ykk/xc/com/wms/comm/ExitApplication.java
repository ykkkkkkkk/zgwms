package ykk.xc.com.wms.comm;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * 完全退出系统的父类
 * @author y1
 *
 */
public class ExitApplication extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitApplication instance;

	private ExitApplication() {

	}
	//单例模式中获取唯一的ExitApplication实例
	public static ExitApplication getInstance() {
		if(instance==null){
			instance=new ExitApplication();
		}
		return instance;
	}
	public void addActivity(Activity activity){
		activityList.add(activity);
		//遍历所有Activity,并finish();
	}
	public void exit(){
		for(Activity activity:activityList){
			if(activity != null) {
				activity.finish();
			}
		}
		// 杀死进程
		android.os.Process.killProcess(android.os.Process.myPid());
		// 退出
		System.exit(0);
		// 释放资源
		System.gc();
	}


}
