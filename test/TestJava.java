public class TestJava
{
	public static void main(String[] args)
	{
		PhoneToWordsDB db = PhoneToWordsDB.fromDictionary("..\\src\\dictionary.txt");
		
		System.out.println(db.toString());
	}
}