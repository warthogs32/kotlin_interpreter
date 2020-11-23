class NumC (var n: Double) : ExprC 
{
	override fun equals(other: Any?): Boolean 
	{
		if (other is NumC) 
		{
			return other.n == this.n
		}
		return false
	}
}