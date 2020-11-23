class StringC (var s: String) : ExprC 
{
	override fun equals(other: Any?): Boolean 
	{
		if (other is StringC) 
		{
			return other.s == this.s
		}
		return false
	}
}