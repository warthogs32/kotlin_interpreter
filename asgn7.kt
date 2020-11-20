interface ExprC {}

class StringC (var s: String) : ExprC {}

class NumC (var n: Double) : ExprC {}

class IfC (var a: ExprC, var b: ExprC, var c: ExprC) : ExprC {}

class IdC (var s: String) : ExprC {}

class AppC (var fn: ExprC, var argmt: Array<ExprC>) : ExprC {}

class LamC(var arguement: Array<String>, var body: ExprC) : ExprC {}

interface Value {}

class StringV (var s: String) : Value {
	override fun equals(other: Any?): Boolean {
		if (other is StringV) {
			return other.s == this.s
		}
		return false
	}
}

class NumV (var n: Double) : Value {
	override fun equals(other: Any?): Boolean {
		if (other is NumV) {
			return other.n == this.n
		}
		return false
	}
}

class LamV (var arguement: Array<String>, var body: Array<ExprC>) : Value {}

class CloV (var param: Array<String>, var body: ExprC, var CloEnv: HashMap<String, Value>) : Value {}

class PrimV (var op: (Value, Value)->Value) : Value {}

class BoolV (var b: Boolean) : Value {
	override fun equals(other: Any?): Boolean {
		if (other is BoolV) {
			return other.b == this.b
		}
		return false
	}
}

class Env (var bindings: HashMap<String, Value>) {}

fun interp(expression: ExprC, environment: Env): Value
{
	when (expression)
	{
		is NumC -> return NumV(expression.n)
		is StringC -> return StringV(expression.s)
		is IdC -> {
					var idSymbol = environment.bindings.get(expression.s)
					if (idSymbol != null) {return idSymbol} else {throw Exception("DXUQ: Unbound identifier: ~e")}
		}
		is LamC -> return CloV(expression.arguement, expression.body, environment.bindings)
		is IfC -> {
					var condition = interp(expression.a, environment)
					if (condition is BoolV) 
						{if (condition.b) {return interp(expression.b, environment)} else {return interp(expression.c, environment)}} else {throw Exception("DXUQ: IfC does not contain a conditional")}
		}
		is AppC -> {
			var funval = interp(expression.fn, environment)
			var args = expression.argmt.map{interp(it, environment)}
			when (funval) {
				is PrimV -> return funval.op(args[0], args[1])
				is CloV -> {
					environment.bindings.putAll(funval.param.zip(args).toMap())
					println(environment.bindings.entries)
					return interp(funval.body, environment)
				}
				else -> {throw Exception("interp DXUQ: bad input")}
			}
		}
		else -> {throw Exception("interp DXUQ: bad input")}
	}
}

fun main(args: Array<String>) 
{
	var topEnv = Env(hashMapOf("+" to PrimV({a: Value, b: Value -> NumV((a as NumV).n + (b as NumV).n)}),
			"-" to PrimV({a: Value, b: Value -> NumV((a as NumV).n - (b as NumV).n)}),
			"/" to PrimV({a: Value, b: Value -> NumV((a as NumV).n / (b as NumV).n)}),
			"*" to PrimV({a: Value, b: Value -> NumV((a as NumV).n * (b as NumV).n)}),
			"<=" to PrimV({a : Value, b : Value -> BoolV((a as NumV).n <= (b as NumV).n)}),
			"equal?" to PrimV({a : Value, b : Value -> BoolV(a == b)}),
			"true" to BoolV(true),
			"false" to BoolV(false)))

	println(((interp(NumC(1.0), topEnv)) as NumV).n)
	println(((interp(AppC(IdC("+"), arrayOf<ExprC>(NumC(1.0), NumC(2.0))), topEnv)) as NumV).n)
	println(((interp(AppC(IdC("<="), arrayOf<ExprC>(NumC(1.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("<="), arrayOf<ExprC>(NumC(3.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(NumC(3.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(NumC(2.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(NumC(2.0), NumC(1.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(IdC("true"), IdC("true"))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(IdC("true"), NumC(1.0))), topEnv)) as BoolV).b)

	println(((interp(AppC(LamC(arrayOf("x", "y"), AppC(IdC("+"), arrayOf<ExprC>(IdC("x"), IdC("y")))), arrayOf<ExprC>(NumC(2.0), NumC(1.0))), topEnv)) as NumV).n)
}