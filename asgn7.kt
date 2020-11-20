interface ExprC {}

class StringC (var s: String) : ExprC {}

class NumC (var n: Double) : ExprC {}

class IfC (var a: ExprC, var b: ExprC, var c: ExprC) : ExprC {}

class IdC (var s: String) : ExprC {}

class AppC (var fn: ExprC, var argmt: ArrayList<ExprC>) : ExprC {}

class LamC(var arguement: ArrayList<String>, var body: ExprC) : ExprC {}

interface Value {}

class StringV (var s: String) : Value {}

class NumV (var n: Double) : Value {}

class LamV (var arguement: ArrayList<String>, var body: ArrayList<ExprC>) : Value {}

class CloV (var param: ArrayList<String>, var body: ExprC, var CloEnv: HashMap<String, Value>) : Value {}

class PrimV (var op: (Any)->Value) : Value {}

class BoolV (var b: Boolean) : Value {}    

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
		else -> {throw Exception("interp DXUQ: bad input")}
	}
}

fun main(args: Array<String>) 
{
    println("Hello, World!")
}