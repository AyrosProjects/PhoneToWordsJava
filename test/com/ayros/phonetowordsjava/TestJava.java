package com.ayros.phonetowordsjava;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class TestJava
{
	public static void main(String[] args)
	{
		PhoneToWordsDB db = PhoneToWordsDB.fromDictionary("test/resources/dictionary.txt");

		byte[] serializedDB = db.exportToBytes();

		writeBytesToFile("test/resources/ptw.db", serializedDB);

		serializedDB = readBytesFromFile("test/resources/ptw.db");

		db = PhoneToWordsDB.fromExported(serializedDB);
		PhoneToWords ptw = new PhoneToWords(db, 1);

		List<String> matches = ptw.getWords("74663686237");

		System.out.println("Found " + matches.size() + " matches:");
		for (String match : matches)
		{
			System.out.println(match);
		}
	}

	private static boolean writeBytesToFile(String fileName, byte[] data)
	{
		try
		{
			File outFile = new File(fileName);

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
			bos.write(data);
			bos.flush();
			bos.close();

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private static byte[] readBytesFromFile(String fileName)
	{
		try
		{
			File inFile = new File(fileName);

			byte[] fileData = new byte[(int)inFile.length()];
			FileInputStream fis = new FileInputStream(inFile);
			fis.read(fileData);

			fis.close();

			return fileData;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}