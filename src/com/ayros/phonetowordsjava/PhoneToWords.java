package com.ayros.phonetowordsjava;

import java.util.List;
import java.util.ArrayList;

public class PhoneToWords
{
	private PhoneToWordsDB db;
	private int maxDigits;

	public PhoneToWords(PhoneToWordsDB db, int maxDigits)
	{
		this.db = db;
		this.maxDigits = maxDigits;
	}

	public List<String> getWords(String phoneNum)
	{
		if (db == null)
		{
			return new ArrayList<String>();
		}

		List<String> results = new ArrayList<String>();

		getWordsRecursive(phoneNum, "", this.maxDigits, results);

		for (int i = 0; i < results.size(); i++)
		{
			results.set(i, results.get(i).substring(1));
		}

		return results;
	}

	private void getWordsRecursive(String phoneNum, String progress, int digitsAllowed,
			List<String> words)
	{
		if (phoneNum.length() == 0)
		{
			words.add(progress);
			return;
		}

		if (phoneNum.charAt(0) == '1')
		{
			getWordsRecursive(phoneNum.substring(1), progress + "-1", digitsAllowed, words);
			return;
		}
		else if (phoneNum.charAt(0) == '9')
		{
			getWordsRecursive(phoneNum.substring(1), progress + "-9", digitsAllowed, words);
			return;
		}

		for (int i = phoneNum.length(); i >= 1; i--)
		{
			String sub = phoneNum.substring(0, i);
			List<String> matches = db.getWords(sub);

			if (matches != null)
			{
				for (String match : matches)
				{
					getWordsRecursive(phoneNum.substring(i), progress + "-" + match, digitsAllowed,
							words);
				}
			}
			else
			{
				if (sub.length() == 1 && digitsAllowed > 0)
				{
					getWordsRecursive(phoneNum.substring(1), progress + "-" + sub,
							digitsAllowed - 1, words);
				}
			}
		}
	}
}