import kotlin.test.Test
import kotlin.test.assertEquals

class asgnTest
{
	@Test
	fun serializeNum()
	{
		assertEquals(serialize(interp(NumC(1.0), topEnv)), "1.0")
	}
}