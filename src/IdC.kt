class IdC (var s: String) : ExprC 
{
	override fun equals(other: Any?): Boolean 
	{
		if (other is IdC) 
		{
			return other.s == this.s
		}
		return false
	}
}
