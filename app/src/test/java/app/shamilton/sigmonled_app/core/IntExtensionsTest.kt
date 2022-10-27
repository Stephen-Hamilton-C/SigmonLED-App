package app.shamilton.common.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntExtensionsTest {
    @Test fun testToHex() {
        assertEquals("FF", 255.toHex())
        assertEquals("0", 0.toHex())
        assertEquals("7F", 127.toHex())
        assertEquals("100", 256.toHex())
        assertEquals("A", 10.toHex())
        assertEquals("FF0", 4080.toHex())
        assertEquals("-1", (-1).toHex())
        assertEquals("-FF", (-255).toHex())
    }

    @Test fun testToHexPadded() {
        assertEquals("FF", 255.toHexPadded(2))
        assertEquals("00", 0.toHexPadded(2))
        assertEquals("007F", 127.toHexPadded(4))
        assertEquals("0000000100", 256.toHexPadded(10))
        assertEquals("A", 10.toHexPadded(1))
        assertEquals("00FF0", 4080.toHexPadded(5))
        assertEquals("-FF", (-255).toHexPadded(2))
        assertEquals("-007F", (-127).toHexPadded(4))
        assertFailsWith<IllegalArgumentException> {
            64.toHexPadded(0)
        }
        assertFailsWith<IllegalArgumentException> {
            128.toHexPadded(-1)
        }
        assertFailsWith<IllegalArgumentException> {
            255.toHexPadded(1)
        }
        assertFailsWith<IllegalArgumentException> {
            (-64).toHexPadded(0)
        }
        assertFailsWith<IllegalArgumentException> {
            (-128).toHexPadded(-1)
        }
        assertFailsWith<IllegalArgumentException> {
            (-255).toHexPadded(1)
        }
    }
}
