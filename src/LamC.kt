class LamC(var arguement: Array<String>, var body: ExprC) : ExprC 
{
	override fun equals(other: Any?): Boolean 
	{
		if (other is LamC) 
		{
			return other.arguement contentEquals this.arguement && other.body == this.body
		}
		return false
	}
}

