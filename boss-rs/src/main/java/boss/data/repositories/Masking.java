package boss.data.repositories;

public class Masking 
{
public String mask(String input)
{
	String output = "";
	for(int i=0;i<input.length()-4;i++)
	{ 
	output=output+"*";
	}
	output = output+input.substring(input.length()-4,input.length() );
	return output;
}
}
