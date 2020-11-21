class NumV (var n: Double) : Value {
	override fun equals(other: Any?): Boolean {
		if (other is NumV) {
			return other.n == this.n
		}
		return false
	}
}