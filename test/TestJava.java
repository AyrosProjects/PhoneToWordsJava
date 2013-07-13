import java.util.List;
import java.util.ArrayList;

public class TestJava
{
	public static void main(String[] args)
	{
		PhoneToWordsDB db = PhoneToWordsDB.fromDictionary("..\\src\\dictionary.txt");
		
		PhoneToWords ptw = new PhoneToWords(db, 1);
		
		List<String> matches = ptw.getWords("74663686237");
		
		System.out.println("Found " + matches.size() + " matches:");
		for (String match : matches)
		{
			System.out.println(match);
		}
	}
}