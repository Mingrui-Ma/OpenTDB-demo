package com.quiz;

import java.util.ArrayList;
import java.util.Random;

public class Randomizer {
	/**
	 * 
	 * @return [0,1,2,3] in a random order.
	 */
	static int[] randomArray()	{
		ArrayList<Integer> remaining = new ArrayList<>();
		for(int i=1;i<=4;i++)
			remaining.add(i);
		Random random = new Random();
		
		int randomInt,
			idx = 0;
		int[] randomArr = new int[4];
		for(int i=4;i>0;i--)	{
			randomInt = random.nextInt(i);
			randomArr[idx++] = remaining.get(randomInt)-1;
			remaining.remove((Object) remaining.get(randomInt));
		}
		return randomArr;
	}
	
	/**
	 * 
	 * @param ar A String array with <em>exactly 4</em> elements.
	 * @return <code>ar</code> randomly scrambled.
	 */
	static String[] scrambleArray(String[] ar)	{
		int[] order = randomArray();
		String[] newArr = new String[4];
		for(int i=0;i<4;i++)	{
			newArr[order[i]] = ar[i];
		}
		return newArr;
	}
	
	static <V> void printAr(V[] arr)	{
		System.out.print("[");
		for(V i:arr)
			System.out.print(i + " ");
		System.out.println("]");
	}
	public static void main(String[] args) {
		String[] s = {
				"a", "b", "c", "d"
		};
		printAr(scrambleArray(s));
	}

}
