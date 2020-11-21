var topEnv = Env(hashMapOf("+" to PrimV({a: Value, b: Value -> NumV((a as NumV).n + (b as NumV).n)}),
			"-" to PrimV({a: Value, b: Value -> NumV((a as NumV).n - (b as NumV).n)}),
			"/" to PrimV({a: Value, b: Value -> NumV((a as NumV).n / (b as NumV).n)}),
			"*" to PrimV({a: Value, b: Value -> NumV((a as NumV).n * (b as NumV).n)}),
			"<=" to PrimV({a : Value, b : Value -> BoolV((a as NumV).n <= (b as NumV).n)}),
			"equal?" to PrimV({a : Value, b : Value -> BoolV(a == b)}),
			"true" to BoolV(true),
			"false" to BoolV(false)))

fun parse(expression: Array<Any>): ExprC {
	if (expression.size == 1 && expression[0] is String) { // single token base cases
		if ((expression[0] as String).matches("-?\\d+(\\.\\d+)?".toRegex())) // token is a number
			return NumC((expression[0] as String).toDouble())
		if ((expression[0] as String).matches("'\\w*'".toRegex())) // token is a string
			return StringC((expression[0] as String))
		return IdC((expression[0] as String)) // else, token is an id
	}

	if (expression.size == 4 && (expression[0] as String) == "if") { // if case
		return IfC(parse(arrayOf(expression[1])), parse(arrayOf(expression[2])), parse(arrayOf(expression[3])))
	}

	if (expression.isNotEmpty()) {
		if (expression.size == 1 && expression[0] is Array<*>) {
			var exp = (expression[0] as Array<Any>)
			return AppC(parse(arrayOf(exp[0])), (exp.toList().subList(1, exp.size)).map { parse(arrayOf(it)) }.toTypedArray())
		}
		return AppC(parse(arrayOf(expression[0])), (expression.toList().subList(1,expression.size)).map{parse(arrayOf(it))}.toTypedArray())
	}
	throw Exception("DXUQ: Bad input")
}

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

	// Interp tests
	println(serialize(interp(NumC(1.0), topEnv)))
	println(serialize(interp(AppC(IdC("<="), arrayOf<ExprC>(NumC(1.0), NumC(2.0))), topEnv)))
	println(serialize(interp(AppC(LamC(arrayOf("x", "y"), AppC(IdC("+"), arrayOf<ExprC>(IdC("x"), IdC("y")))), arrayOf<ExprC>(NumC(2.0), NumC(1.0))), topEnv)))

	// Parse tests
	println(parse(arrayOf("7.0")))
	println(parse(arrayOf("id")))
	println(parse(arrayOf("'string'")))
	println(serialize(interp(parse(arrayOf("if", "true", "1", "2")), topEnv)))
	println(serialize(interp(parse(arrayOf("if", "false", "1", "2")), topEnv)))
	println(serialize(interp(parse(arrayOf("+", "1", "2")), topEnv)))
	println(serialize(interp(parse(arrayOf("+", "1", arrayOf("+", "2", "3"))), topEnv)))
	println(serialize(interp(parse(arrayOf("+", "1", arrayOf("+", "2", arrayOf("+", "3", "4")))), topEnv)))
}