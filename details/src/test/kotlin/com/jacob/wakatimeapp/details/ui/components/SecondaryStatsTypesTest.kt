package com.jacob.wakatimeapp.details.ui.components

import com.jacob.wakatimeapp.details.ui.components.SecondaryStatsTypes.EDITOR
import com.jacob.wakatimeapp.details.ui.components.SecondaryStatsTypes.LANGUAGE
import com.jacob.wakatimeapp.details.ui.components.SecondaryStatsTypes.MACHINE
import com.jacob.wakatimeapp.details.ui.components.SecondaryStatsTypes.OS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// SecondaryStatsTypes.LANGUAGE, SecondaryStatsTypes.EDITOR,
// SecondaryStatsTypes.OS, SecondaryStatsTypes.MACHINE
class SecondaryStatsTypesTest {
    @Test
    fun `chips should move in correct direction WRT language `() {
        assertEquals(Pair(0, 0), LANGUAGE.directionToMoveWRT(LANGUAGE))
        assertEquals(Pair(1, 0), EDITOR.directionToMoveWRT(LANGUAGE))
        assertEquals(Pair(0, -1), OS.directionToMoveWRT(LANGUAGE))
        assertEquals(Pair(1, -1), MACHINE.directionToMoveWRT(LANGUAGE))
    }

    @Test
    fun `chips should move in correct direction WRT editor`() {
        assertEquals(Pair(-1, 0), LANGUAGE.directionToMoveWRT(EDITOR))
        assertEquals(Pair(0, 0), EDITOR.directionToMoveWRT(EDITOR))
        assertEquals(Pair(-1, -1), OS.directionToMoveWRT(EDITOR))
        assertEquals(Pair(0, -1), MACHINE.directionToMoveWRT(EDITOR))
    }

    @Test
    fun `chips should move in correct direction WRT os`() {
        assertEquals(Pair(0, 1), LANGUAGE.directionToMoveWRT(OS))
        assertEquals(Pair(1, 1), EDITOR.directionToMoveWRT(OS))
        assertEquals(Pair(0, 0), OS.directionToMoveWRT(OS))
        assertEquals(Pair(1, 0), MACHINE.directionToMoveWRT(OS))
    }

    @Test
    fun `chips should move in correct direction WRT machine`() {
        assertEquals(Pair(-1, 1), LANGUAGE.directionToMoveWRT(MACHINE))
        assertEquals(Pair(0, 1), EDITOR.directionToMoveWRT(MACHINE))
        assertEquals(Pair(-1, 0), OS.directionToMoveWRT(MACHINE))
        assertEquals(Pair(0, 0), MACHINE.directionToMoveWRT(MACHINE))
    }
}
