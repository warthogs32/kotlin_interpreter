interface ExprC {}

class StringC (var s: String) : ExprC {}

class NumC (var n: Double) : ExprC {}

class IfC (var a: ExprC, var b: ExprC, var c: ExprC) : ExprC {}

class IdC (var s: String) : ExprC {}

class AppC (var fn: ExprC, var argmt: ArrayList<ExprC>) : ExprC {}

class LamC(var arguement: ArrayList<String>, var body: ArrayList<ExprC>) : ExprC {}

interface Value {}

class StringV (var s: String) : Value {}

class NumV (var n: Double) : Value {}

class LamV (var arguement: ArrayList<String>, var body: ArrayList<ExprC>) : Value {}

class CloV (var param: ArrayList<String>, var body: ExprC, var CloEnv: HashMap<String, Value>) : Value {}

class PrimV (var op: (Any)->Value) : Value {}

class BoolV (var b: Boolean) : Value {}    

class Env (var bindings: HashMap<String, Value>) {}

fun main(args: Array<String>) 
{
    println("Hello, World!")
}