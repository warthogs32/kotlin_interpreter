class StringV (var s: String) : Value {
	override fun equals(other: Any?): Boolean {
		if (other is StringV) {
			return other.s == this.s
		}
		return false
	}
}