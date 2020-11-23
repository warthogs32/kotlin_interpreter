class TestFramework
{
	fun AssertEqual(observed: Any, expected: Any): Verdict
	{
		return Verdict(observed, expected, (observed::class.simpleName == expected::class.simpleName) && (observed == expected)) 
	}
	fun AssertNotEqual(observed: Any, expected: Any): Verdict
	{
		return Verdict(observed, expected, (observed::class.simpleName == expected::class.simpleName) && (observed != expected)) 
	}
	fun AssertLEQ(observed: Any, expected: Any): Verdict
	{
		if(observed is String && expected is String)
		{
			var observedDouble: Double = (observed as String).toDouble()
			var expectedDouble: Double = (expected as String).toDouble()
			return Verdict(observedDouble, expectedDouble, (observedDouble <= expectedDouble))
		}
		return Verdict(observed, expected, observed as Double <= expected as Double)
	}
	fun AssertTrue(observed: Any): Verdict
	{
		return Verdict(observed, true, (observed is Boolean) && observed)
	}
	fun AssertFalse(observed: Any): Verdict
	{
		return Verdict(observed, false, ((observed is Boolean) && !(observed)))
	}
	fun RunTests(testSetName: String, lov: List<Verdict>)
	{
		println(testSetName)
		var pass: Int = 0
		var fail: Int = 0
		for (vd in lov)
		{
			println("observed: ${vd.observed}\n expected: ${vd.expected}\nverdict: ${vd.pf}\n---------------\n")
			if (vd.result)
			{
				pass++
			}
			else
			{
				fail++
			}
		}
		println("pass: $pass\nfail: $fail\n")
	}
}
