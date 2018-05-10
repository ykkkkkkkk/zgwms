package ykk.xc.com.wms.comm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.solidfire.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Random;

import ykk.xc.com.wms.R;

/**
 * 父类Activity
 *
 * @author yu
 *
 */
public class BaseActivity extends AppCompatActivity {
	private BaseActivity mContext;
//	private boolean isExist;
	private long temptime;

//	private Handler mHandle = new Handler() {
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			isExist = false;
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCollector.addActivity(this);
	}

	/**
	 * 捕获返回按键的动作
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			exit();
//			return false;
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
		// 使用系统时间来判读两次点击的间隔
        if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN)) {
				if(System.currentTimeMillis() - temptime > 2000) {// 2s内再次选择back键有效
					System.out.println(Toast.LENGTH_LONG);
					print_fun(this, "再按一次返回键退出系统");
					temptime = System.currentTimeMillis();
				} else {
					ActivityCollector.finishAll();
					System.exit(0); //凡是非零都表示异常退出!0表示正常退出!
				}
				return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 执行HOME键方法
	 */
	public void execHOME() {
		// 按返回键不退出程序，程序最小化
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		startActivity(intent);
	}

	/**
	 * 按返回键不退出程序，程序最小化（好像不能用）
	 */
	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	// Intent intent = new Intent();
	// intent.setAction("android.intent.action.MAIN");
	// intent.addCategory("android.intent.category.HOME");
	// startActivity(intent);
	// }
	// return false;
	// };

	/**
	 * 退出的方法
	 */
	public void exit() {
//		if (!isExist) {
//			isExist = true;
//			Toast.makeText(getApplicationContext(), "再按一次返回键退出系统",
//					Toast.LENGTH_SHORT).show();
//			mHandle.sendEmptyMessageDelayed(0, 2000);
//		} else {
//			ExitApplication.getInstance().exit();
//			ActivityCollector.finishAll();
//			// 杀死进程
//        	android.os.Process.killProcess(android.os.Process.myPid());
//        	// 退出
//        	System.exit(0);
//        	// 释放资源
//        	System.gc();
//		}
	}

	/** ------------------自定义方法-------------------- */

	/**
	 * 说明:(得到TextView控件的值) 作者: y1
	 */
	public String getValues(TextView txt) {
		return txt.getText().toString();
	}

	/** 说明:(得到EditText控件的值) */
	public String getValues(EditText edit) {
		return edit.getText().toString();
	}

	/** 说明:(得到Button控件的值) */
	public String getValues(Button btn) {
		return btn.getText().toString();
	}

	/**
	 * 设置EditText 控件的值（把把光标显示到最前）
	 */
	public void setTexts(EditText edit, String value) {
		edit.setText(value);
		edit.setSelection(value.length());
	}

	/**
	 * 得到xml文件
	 */
	public SharedPreferences spf() {
		return getSharedPreferences(getResStr(R.string.saveUserInfo), Context.MODE_PRIVATE);
	}

	/**
	 * 得到xml文件
	 */
	public SharedPreferences spf(String xmlName) {
		return getSharedPreferences(xmlName, Context.MODE_PRIVATE);
	}

	/**
	 * 得到xml中的value
	 *
	 * @param key
	 * @return
	 */
	public String getXmlValues(String key) {
		if (key.equals("companyId")) {
			return spf().getString("path1", "");
		}

		if (key.equals("companyName")) {
			return spf().getString("companyName", "");
		}

		if (key.equals("userId")) {
			return spf().getString("userId", "");
		}

		if (key.equals("userName")) {
			return spf().getString("userName", "");
		}

		if (key.equals("phone")) {
			return spf().getString("phone", "");
		}

		if (key.equals("pwd")) {
			return spf().getString("pwd", "");
		}

		if (key.equals("ynManager")) {
			return spf().getString("ynManager", "");
		}

		if (key.equals("puId")) {
			return spf().getString("puId", "0");
		}

		if (key.equals("picture")) {
			return spf().getString("picture", "");
		}
		return "";
	}

	/**
	 * 得到xml中的value
	 *
	 * @param key
	 * @return
	 */
	public String getXmlValues(SharedPreferences spf, String key) {
		return spf.getString(key, "");
	}

	public boolean getXmlValues2(SharedPreferences spf, String key) {
		return spf.getBoolean(key, false);
	}

	/**
	 * 设置xml中的值
	 *
	 * @param spf
	 * @param putKey
	 * @param putValue
	 */
	public void setXmlValues(SharedPreferences spf, String putKey, String putValue) {
		spf.edit().putString(putKey, putValue).commit();
	}

    /**
     * 得到string.xml中的值
     */
    public String getResStr(int idName) {
        return getResources().getString(idName);
    }

	/**
	 * Toast打印
	 */
	public void print_fun(Activity activity, String str) {
		Toast.makeText(activity, str, Toast.LENGTH_LONG).show();
	}

	/**
	 * 字符串不能为空的终极判断 父方法
	 *
	 * @param obj
	 * @return
	 */
	public String isNULLS(Object obj) {
		return (obj != null && !obj.equals("null")) ? obj.toString() : "";
	}
	public String isNULLS(JSONObject json, String key) throws JSONException {
		return json.has(key) ? isNULLS(json.getString(key)) : "";
	}

	/**
	 * 说明:(字符串不能为空的终极判断 ) 作者: y1
	 */
	public String isNULL2(Object obj, String defaVal) {
		String result = isNULLS(obj);
		return result.length() > 0 ? result : defaVal;
	}

	/**
	 * 说明:(字符串不能为空的终极判断 ) 作者: y1
	 */
	public String isNULL2(String obj, String defaVal) {
		String result = isNULLS(obj);
		return result.length() > 0 ? result : defaVal;
	}

	/**
	 * 数字字符串转成Double 如果Double.parseDouble(null);这个是报空指针异常
	 */
	public double parseDouble(Object obj) {
		try {
			String result = isNULLS(obj);
			return result.length() > 0 ? Double.parseDouble(result) : 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public double parseDouble(JSONObject json, String key) {
		try {
			String result = isNULLS(json, key);
			return result.length() > 0 ? Double.parseDouble(result) : 0;
		} catch (Exception e) {
			return 0;
		}
	}
	public double parseDouble(Object obj, double defaVal) {
		try {
			String result = isNULLS(obj);
			return result.length() > 0 ? Double.parseDouble(result) : defaVal;
		} catch (NumberFormatException e) {
			return defaVal;
		}
	}

	/**
	 * 数字字符串转成Int 如果Integer.parseInt(null);这个是报空指针异常
	 */
	public int parseInt(Object obj) {
		try {
			String result = isNULLS(obj);
			return result.length() > 0 ? Integer.parseInt(result) : 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public int parseInt(JSONObject json, String key) {
		try {
			String result = isNULLS(json, key);
			return result.length() > 0 ? Integer.parseInt(result) : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 设置view的间距
	 */
	public void setMargins(View view, int left, int top, int right, int bottom) {
		if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (MarginLayoutParams) view.getLayoutParams();
			p.setMargins(left, top, right, bottom);
			view.requestLayout();
		}
	}

	/**
	 * 比UUID还牛的随机生成数
	 * 10位随机数，当前时间	组成
	 */
	public String randomUUID(Context context) {
		// 生产8位随机验证码
		StringBuffer code = new StringBuffer();
		Random rand = new Random();// 随机生成类
		for (int i = 0; i < 10; i++) {
			code.append(rand.nextInt(10));
		}
		rand = null;
		return code.toString()+Comm.getSysDate(8);
	}

	/**
	 * 打开页面的方法
	 * @param context 在哪个页面引用
	 * @param context2    打开的页面
	 * @param bundle  传值
	 */
	public void show(Activity context, Class<?> context2, Bundle bundle){
		Intent intent = new Intent();
		intent.setClass(context, context2);
		if(bundle!=null){
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}

	/**
	 * 打开页面然后可以得到回调的值
	 * @param activity 在哪个页面引用
	 * @param context2    打开的页面
	 * @param code 返回值的状态码
	 * @param bundle  传值
	 */
	public void showForResult(Activity activity,Class<?> context2 , int code , Bundle bundle){
		Intent intent = new Intent();
		intent.setClass(activity, context2);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if(bundle!=null){
			intent.putExtras(bundle);
		}
		activity.startActivityForResult(intent, code);
	}

	/**
	 * 刷新	发起跳转的onActivity
	 */
	public void setResults(Activity context) {
		Intent intent = new Intent();
		intent.putExtra("isRefresh", true);
		context.setResult(RESULT_OK, intent);
	}
	public void setResults(Activity context, String str) {
		Intent intent = new Intent();
		intent.putExtra("resultValue", str);
		context.setResult(RESULT_OK, intent);
	}
	public void setResults(Activity context, Class<?> t) {
		Intent intent = new Intent();
		intent.putExtra("obj", t);
		context.setResult(RESULT_OK, intent);
	}
	public void setResults(Activity context, Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		context.setResult(RESULT_OK, intent);
	}

	/**
	 * 显示输入法
	 */
	public void showKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 隐藏输入法
	 */
	public void hideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
	}

	/**
	 * 得到焦点后不谈出软键盘 只显示光标不显示软键盘
	 */
	public void hideSoftInputMode(Activity context, EditText edit) {
		if (android.os.Build.VERSION.SDK_INT <= 10) {// 3.0以下使用
			edit.setInputType(InputType.TYPE_NULL);
		} else {// 3.0以上使用
			context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setFocus;
				setFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setFocus.setAccessible(true);
				setFocus.invoke(edit, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭Handler
	 */
	public void closeHandler(Handler mHandle) {
		if(mHandle != null) {
			mHandle.removeCallbacksAndMessages(null);
		}
	}

	public Gson gGson() {
		return new Gson();
	}

}
