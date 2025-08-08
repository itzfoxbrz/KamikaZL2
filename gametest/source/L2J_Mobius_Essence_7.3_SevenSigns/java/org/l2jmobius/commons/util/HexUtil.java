/*
 * Copyright (c) 2013 L2jMobius
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.l2jmobius.commons.util;

/**
 * Utility class for handling operations related to hexadecimal data.
 * @author Mobius
 */
public class HexUtil
{
	/**
	 * Generates a byte array of the specified size filled with random non-zero values.
	 * @param size the size of the byte array to generate
	 * @return a byte array filled with random non-zero values
	 */
	public static byte[] generateHexBytes(int size)
	{
		final byte[] array = new byte[size];
		Rnd.nextBytes(array);
		
		// Ensure no zero values are in the array.
		for (int i = 0; i < array.length; i++)
		{
			while (array[i] == 0)
			{
				array[i] = (byte) Rnd.get(Byte.MAX_VALUE);
			}
		}
		
		return array;
	}
}
