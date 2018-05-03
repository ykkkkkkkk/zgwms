package ykk.xc.com.wms.comm;

import android.app.Application;

/**
 * 主Application
 * @author ykk
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 全局捕获异常
		UncaughtException mUncaughtException = UncaughtException.getInstance();
		mUncaughtException.init(this);

	}


}
