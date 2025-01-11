package main;

public class Utils {
	private static boolean captcha = false;
	
	private static int intentosFallidosCaptcha=0;

	public static int getIntentosFallidosCaptcha() {
		return intentosFallidosCaptcha;
	}

	public static void setIntentosFallidosCaptcha(int intentosFallidosCaptcha) {
		Utils.intentosFallidosCaptcha += intentosFallidosCaptcha;
	}

	public static boolean isCaptcha() {
		return captcha;
	}

	public static void setCaptcha(boolean captcha) {
		Utils.captcha = captcha;
	}

}
