class Verdict(var observed: Any, var expected: Any, var result: Boolean)
{
	var pf: String
	init
	{
		pf = if (result) "pass" else {"fail"}
	}
}