package com.ayros.phonetowordsjava;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PhoneToWordsDB
{
	static char[] digitLookup;
	
	Map<String, ArrayList<String>> map;
	
	private PhoneToWordsDB()
	{
		this(0);
	}
	
	private PhoneToWordsDB(int initialCapacity)
	{
		map = new HashMap<String, ArrayList<String>>(initialCapacity);
	}
	
	public static PhoneToWordsDB fromDictionary(String dict)
	{
		try
		{
			Scanner s = new Scanner(new File(dict));
			
			List<String> words = new ArrayList<String>();
			while (s.hasNextLine())
			{
				words.add(s.nextLine());
			}
			
			s.close();
			
			String[] wordArray = new String[words.size()];
			words.toArray(wordArray);
			
			return fromWordList(wordArray);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Used to read in a list of words with maximal performance. Assumes:
	 * 1.) Words are separated by \n
	 * 2.) Words are all lowercase
	 * 3.) There are no duplicates
	 */
	public static PhoneToWordsDB fromProcessedWordList(String words, int initialCap)
	{
		PhoneToWordsDB db = new PhoneToWordsDB(initialCap);
		
		int posStart = 0;
		while (true)
		{
			int posEnd = words.indexOf('\n', posStart);
			if (posEnd <= 0)
			{
				break;
			}
			
			String word = words.substring(posStart, posEnd);
			
			String digits = wordToNums(word);
			
			ArrayList<String> digitWords = db.map.get(digits);
			if (digitWords == null)
			{
				digitWords = new ArrayList<String>();
				db.map.put(digits, digitWords);
			}
			digitWords.add(word);
			
			posStart = posEnd + 1;
		}
		
		return db;
	}
	
	public static PhoneToWordsDB fromWordList(String[] words)
	{
		PhoneToWordsDB db = new PhoneToWordsDB();
		
		for (String word : words)
		{
			word = word.toLowerCase();
			
			char[] letters = word.toCharArray();
			for (int i = word.length() - 1; i >= 0; i--)
			{
				if (letters[i] < 'a' || letters[i] > 'z')
				{
					word = word.replace(Character.toString(letters[i]), "");
				}
			}
			
			if (word.isEmpty())
			{
				continue;
			}
			
			String num = wordToNums(word);
			
			if (db.map.containsKey(num))
			{
				if (!db.map.get(num).contains(num))
				{
					db.map.get(num).add(word);
				}
			}
			else
			{
				ArrayList<String> wordList = new ArrayList<String>();
				wordList.add(word);
				
				db.map.put(num, wordList);
			}
		}
		
		return db;
	}
	
	@SuppressWarnings("unchecked")
	public static PhoneToWordsDB fromExported(byte[] bytes)
	{
		PhoneToWordsDB db = new PhoneToWordsDB();
		
		try
		{
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			
			Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>)ois.readObject();
			db.map = map;
			
			ois.close();
			
			return db;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	private static String wordToNums(String word)
	{
		if (digitLookup == null)
		{
			digitLookup = new char[256];
			for (int i = 0; i < digitLookup.length; i++)
			{
				char c = (char)i;
				
				if (c == 'a' || c == 'b' || c == 'c')
				{
					digitLookup[i] = '2';
				}
				else if (c == 'd' || c == 'e' || c == 'f')
				{
					digitLookup[i] = '3';
				}
				else if (c == 'g' || c == 'h' || c == 'i')
				{
					digitLookup[i] = '4';
				}
				else if (c == 'j' || c == 'k' || c == 'l')
				{
					digitLookup[i] = '5';
				}
				else if (c == 'm' || c == 'n' || c == 'o')
				{
					digitLookup[i] = '6';
				}
				else if (c == 'p' || c == 'q' || c == 'r' || c == 's')
				{
					digitLookup[i] = '7';
				}
				else if (c == 't' || c == 'u' || c == 'v')
				{
					digitLookup[i] = '8';
				}
				else if (c == 'w' || c == 'x' || c == 'y' || c == 'z')
				{
					digitLookup[i] = '9';
				}
				else
				{
					digitLookup[i] = ' ';
				}
			}
		}
		
		char[] digits = new char[word.length()];
		for (int i = 0; i < word.length(); i++)
		{
			digits[i] = digitLookup[(int)word.charAt(i)];
		}
		
		return new String(digits);
	}
	
	public byte[] exportToBytes()
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(map);
			byte[] data = bos.toByteArray();
			
			oos.close();
			
			return data;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public List<String> getWords(String number)
	{
		if (map.containsKey(number))
		{
			return map.get(number);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet())
		{
			sb.append(entry.getKey() + ":\n");
			
			for (String word : entry.getValue())
			{
				sb.append("    " + word + "\n");
			}
		}
		
		return sb.toString();
	}
}