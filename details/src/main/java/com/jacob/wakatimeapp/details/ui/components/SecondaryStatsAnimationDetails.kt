package com.jacob.wakatimeapp.details.ui.components

@Suppress("BooleanLiteralArgument")
internal enum class SecondaryStatsAnimationDetails(
    val statName: String,
    private val index: Int,
    private val pushDirection: AnimationDirections,
) {
    LANGUAGE("Language", 0, AnimationDirections(true, false)),
    EDITOR("Editor", 1, AnimationDirections(false, false)),
    OS("Operating System", 2, AnimationDirections(true, true)),
    MACHINE("Machine", 3, AnimationDirections(false, true)),
    NONE("None", -1, AnimationDirections(false, false)), ;

    internal class AnimationDirections(val xAxis: Boolean, val yAxis: Boolean)

    @Suppress("BooleanLiteralArgument")
    private val directionMap = mapOf(
        Pair(0, AnimationDirections(false, false)),
        Pair(1, AnimationDirections(true, false)),
        Pair(2, AnimationDirections(false, true)),
        Pair(3, AnimationDirections(true, true)),
    )

    private fun getDirectionRelativeTo(other: SecondaryStatsAnimationDetails): AnimationDirections {
        if (other == NONE) return directionMap[0]!!
        if (this == other) return directionMap[0]!!

        return directionMap[this xor other]!!
    }

    // other.pushDirection defines if the push force is positive or negative along the axis
    // canMoveAlongAxes defines if the chip should move along that particular axis (eg: won't move along X if both chips are on the same row)
    // if it can move along an axis, checks if it should move positive or negative
    fun directionToMoveWRT(other: SecondaryStatsAnimationDetails): Pair<Int, Int> {
        val canMoveAlongAxes = getDirectionRelativeTo(other)

        val finalXAxisMovement = if (canMoveAlongAxes.xAxis) if (other.pushDirection.xAxis) 1 else -1 else 0
        val finalYAxisMovement = if (canMoveAlongAxes.yAxis) if (other.pushDirection.yAxis) 1 else -1 else 0
        return Pair(finalXAxisMovement, finalYAxisMovement)
    }

    private infix fun xor(other: SecondaryStatsAnimationDetails): Int {
        return this.index xor other.index
    }
}
