class BoolV (var b: Boolean) : Value {
	override fun equals(other: Any?): Boolean {
		if (other is BoolV) {
			return other.b == this.b
		}
		return false
	}
}