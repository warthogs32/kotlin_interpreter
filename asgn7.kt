interface ExprC
{
}
class StringC : ExprC
{
	var s: String
}
class NumC : ExprC
{
    var n: Double
}
class IfC : ExprC
{
    var a: ExprC
    var b: ExprC
    var c: ExprC
}
class IdC : ExprC
{
	var s: String
}
class AppC : ExprC
{
	var fn: ExprC 
	var argmt: ArrayList<ExprC>
}
class LamC : ExprC
{
	var argmtx: ArrayList<String>
	var body: ArrayList<ExprC>
}

interface Value
{
}
class StringV : Value
{
	var s: String
}
class NumV : Value
{
    var n: Double
}
}
class LamV : Value
{
	var argmtx: ArrayList<String>
	var body: ArrayList<ExprC>
}
class CloV : Value
{
	var param: ArrayList<String>
    var body: ExprC
	var CloEnv: HashMap<String, Value>
}
class PrimV : Value
{
	var op: (Any)->Value
}
class BoolV : Value
{
	var b: Boolean
}    

class Env
{
	var bindings: HashMap<String, Value>
}

