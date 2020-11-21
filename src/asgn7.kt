var topEnv = Env(hashMapOf("+" to PrimV({a: Value, b: Value -> NumV((a as NumV).n + (b as NumV).n)}),
			"-" to PrimV({a: Value, b: Value -> NumV((a as NumV).n - (b as NumV).n)}),
			"/" to PrimV({a: Value, b: Value -> NumV((a as NumV).n / (b as NumV).n)}),
			"*" to PrimV({a: Value, b: Value -> NumV((a as NumV).n * (b as NumV).n)}),
			"<=" to PrimV({a : Value, b : Value -> BoolV((a as NumV).n <= (b as NumV).n)}),
			"equal?" to PrimV({a : Value, b : Value -> BoolV(a == b)}),
			"true" to BoolV(true),
			"false" to BoolV(false)))

fun parse(expression: String): ExprC {}

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
					return interp(funval.body, environment)
				}
				else -> {throw Exception("interp DXUQ: bad input")}
			}
		}
		else -> {throw Exception("interp DXUQ: bad input")}
	}
}

fun serialize(valu: Value): String
{
	when (valu)
	{
		is NumV -> return valu.n.toString()
		is StringV -> return valu.s
		is BoolV -> if (valu.b) {return "true"} else {return "false"}
		is Procedure -> return """#<procedure>"""
		is PrimV -> return "#<primop>"
		else -> {throw Exception("interp DXUQ: bad input")}
	}
}

fun main() 
{
	/*println(((interp(NumC(1.0), topEnv)) as NumV).n)
	println(((interp(AppC(IdC("+"), arrayOf<ExprC>(NumC(1.0), NumC(2.0))), topEnv)) as NumV).n)
	println(((interp(AppC(IdC("<="), arrayOf<ExprC>(NumC(1.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("<="), arrayOf<ExprC>(NumC(3.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(NumC(3.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(NumC(2.0), NumC(2.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(NumC(2.0), NumC(1.0))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(IdC("true"), IdC("true"))), topEnv)) as BoolV).b)
	println(((interp(AppC(IdC("equal?"), arrayOf<ExprC>(IdC("true"), NumC(1.0))), topEnv)) as BoolV).b)*/

	println(serialize(interp(NumC(1.0), topEnv)))
	println(serialize(interp(AppC(IdC("<="), arrayOf<ExprC>(NumC(1.0), NumC(2.0))), topEnv)))

	println(serialize(interp(AppC(LamC(arrayOf("x", "y"), AppC(IdC("+"), arrayOf<ExprC>(IdC("x"), IdC("y")))), arrayOf<ExprC>(NumC(2.0), NumC(1.0))), topEnv)))
}