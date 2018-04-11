package com.aidan.basetools.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * The Class DeviceUtils.
 */
public class DeviceUtils {
	
	/** The Constant NETWORK_WIFI. */
	public static final int NETWORK_WIFI = 0;
	
	/** The Constant NETWORK_3G. */
	public static final int NETWORK_3G = 1;
	
	/** The Constant NETWORK_NONE. */
	public static final int NETWORK_NONE = 2;
	
	/** The instance. */
	private static DeviceUtils instance = null;
	
	/** The screen density. */
	private static float SCREEN_DENSITY = 0.0f;
	
	/** The screen width. */
	public int SCREEN_WIDTH = 0;
	
	/** The screen height. */
	public int SCREEN_HEIGHT = 0;

	private static final String REGEX_NOT_DIGITAL = "[^0-9]+";

	/**
	 * Gets the single instance of DeviceUtils.
	 *
	 * @param context the context
	 * @return single instance of DeviceUtils
	 */
	public static DeviceUtils getInstance(Context context) {
		if (instance == null) {
			synchronized (DeviceUtils.class) {
				if (instance == null) {
					instance = new DeviceUtils(context);
				}
			}
		}
		return instance;
	}

/**
 * Instantiates a new device utils.
 *
 * @param mContext the m context
 */
//	 }
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private DeviceUtils(Context mContext) {
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Resources resources = mContext.getResources();
		int sdk = Build.VERSION.SDK_INT;
		if (sdk >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			setScreenWidthAndHeight(disp);
		} else{
			SCREEN_WIDTH = disp.getWidth();
			SCREEN_HEIGHT = disp.getHeight();
		}
		SCREEN_DENSITY = resources.getDisplayMetrics().density;
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void setScreenWidthAndHeight(Display disp){
		Point size = new Point();
		disp.getSize(size);
		SCREEN_WIDTH = size.x;
		SCREEN_HEIGHT = size.y;
	}
	/**
	 * Gets the version.
	 *
	 * @param mContext the m context
	 * @return the version
	 */
	public static String getVersion(Context mContext) {
		try {
			return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "1.0";
		}
	}
    
    /**
     * Gets the version code.
     *
     * @param context the context
     * @return the version code
     */
    public static int getVersionCode(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().
					getPackageInfo(context.getPackageName(),0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

    /**
     * Gets the version name.
     *
     * @param context the context
     * @return the version name
     */
    public static String getVersionName(Context context) {
    	String versionName = null;
 		try {
 			PackageInfo packageInfo = context.getPackageManager().
 					getPackageInfo(context.getPackageName(),0);
 			versionName = packageInfo.versionName;
 		} catch (NameNotFoundException e) {
 		}
 		return versionName;
 	}


	/**
	 * Checks if is app installed.
	 *
	 * @param packageName the package name
	 * @return true, if is app installed
	 */
	private boolean isAppInstalled(Context mContext, String packageName) {
		PackageManager pm = mContext.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	/**
	 * Package exist.
	 *
	 * @param targetPackage the target package
	 * @return true, if successful
	 */
	public boolean packageExist(Context mContext, String targetPackage) {
		List<ApplicationInfo> packages;
		PackageManager pm;
		pm = mContext.getPackageManager();
		packages = pm.getInstalledApplications(0);

		for (ApplicationInfo packageInfo : packages) {
			if (packageInfo.packageName.equals(targetPackage))
				return true;
		}

		return false;
	}

	/**
	 * SH a1.
	 *
	 * @param text the text
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static String SHA1(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		byte[] sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	/**
	 * Convert to hex.
	 *
	 * @param data the data
	 * @return the string
	 */
	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			int halfbyte = (b >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
						: (char) ('a' + (halfbyte - 10)));
				halfbyte = b & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * External storage available.
	 *
	 * @return true, if successful
	 */
	/* check if external sdcard is mounted */
	public static boolean externalStorageAvailable() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state))
			return true;
		else
			return false;
	}

	/**
	 * Dp to pixel.
	 *
	 * @param x the x
	 * @return the int
	 */
	public int dpToPixel(int x) {
		return (int) (SCREEN_DENSITY * (float) x);
	}

	/**
	 * Dp to pixel.
	 *
	 * @param x the x
	 * @return the int
	 */
	public int dpToPixel(float x) {
		return (int) (SCREEN_DENSITY * x);
	}

	/**
	 * Pixel to dp.
	 *
	 * @param x the x
	 * @return the int
	 */
	public int pixelToDp(int x) {
		return (int) ((float) x / SCREEN_DENSITY);
	}

	/**
	 * Checks if is tablet.
	 *
	 * @return true, if is tablet
	 */
	public boolean isTablet(Context mContext) {
		return (mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Checks if is pad.
	 *
	 * @return true, if is pad
	 */
	public boolean isPad() {
		if (((float) SCREEN_WIDTH / SCREEN_DENSITY) > 400.0f) {
			return true;
		} else {
			return false;
		}
	}





	/**
	 * Dir size.
	 *
	 * @param dir the dir
	 * @return the long
	 */
	public static long dirSize(File dir) {
		if (dir.exists()) {
			long result = 0;
			File[] fileList = dir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// Recursive call if it's a directory
				if (fileList[i].isDirectory()) {
					result += dirSize(fileList[i]);
				} else {
					// Sum the file size in bytes
					result += fileList[i].length();
				}
			}
			return result; // return the file size
		}

		return 0;
	}



	/**
	 * Checks if is intent available.
	 *
	 * @param context the context
	 * @param action the action
	 * @return true, if is intent available
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/*
	 * OS Version
	 *
	 * eg: 4.1.1
	 */

	public static String getOSVersion(){
		return Build.VERSION.RELEASE;
	}

	/*
	 * Device Name
	 *
	 * eg: HTC Desire
	 */
	public static String getDeviceName(){
		return Build.MODEL;
	}
	
	/**
	 * Log.
	 *
	 * @param log the log
	 */
	private static void log(String log){
		LogHelper.log(log);
	}
}
