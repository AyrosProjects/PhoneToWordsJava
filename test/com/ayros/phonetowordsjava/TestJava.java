package com.ayros.phonetowordsjava;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class TestJava
{
	private static long timer;
	
	public static void main(String[] args)
	{
		// Dictionary -> DB
		timerStart();
		PhoneToWordsDB db = PhoneToWordsDB.fromDictionary("test/resources/dictionary.txt");
		System.out.println("Constructed database from dictionary in " + timerMS() + "ms");
		
		// DB -> Serialized
		timerStart();
		byte[] serializedDB = db.exportToBytes();
		System.out.println("Serialized database in " + timerMS() + "ms");
		
		// Serialized -> File
		writeBytesToFile("test/resources/ptw.db", serializedDB);
		
		// File -> Serialized
		serializedDB = readBytesFromFile("test/resources/ptw.db");
		
		// Serialized -> DB
		timerStart();
		db = PhoneToWordsDB.fromExported(serializedDB);
		System.out.println("Unserialized database in " + timerMS() + "ms");
		
		// Create PhoneToWords
		PhoneToWords ptw = new PhoneToWords(db, 1);
		
		// Lookup number
		timerStart();
		List<String> matches = ptw.getWords("74663686237");
		System.out.println("Looked up number in " + timerMS() + "ms");
		
		System.out.println("Found " + matches.size() + " matches:");
		for (String match : matches)
		{
			System.out.println(match);
		}
	}
	
	private static void timerStart()
	{
		timer = System.nanoTime();
	}
	
	private static double timerMS()
	{
		return (System.nanoTime() - timer) / 1000000.0;
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