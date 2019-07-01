package net.primeux.primedropenchant.util;

import java.util.Random;

public class NumberUtil
{

	public static int randomInt(int min, int max)
	{
		return ((int)(Math.random() * ((max-min) + 1)) + min);
	}

	public static float randomFloat(float min, float max)
	{
		Random rand = new Random(); // sooo random !!!
		return ((float)(rand.nextFloat() * (max - min) + min));
	}

	public static double round(double value, int precision)
	{
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	public static float round(float value, int precision)
	{
		int scale = (int) Math.pow(10, precision);
		return (float) Math.round(value * scale) / scale;
	}

	public static boolean isInteger(String num)
	{
		try {
			Integer.parseInt(num);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

}
