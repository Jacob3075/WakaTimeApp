package com.jacob.wakatimeapp.core.ui.theme.assets

import androidx.annotation.RawRes
import androidx.compose.runtime.Immutable
import com.jacob.wakatimeapp.core.ui.R.raw

@Immutable
object Animations {
    @RawRes
    private val blueLoading = raw.blue_loading

    @RawRes
    private val dotsAndLinesLoadingPlexus = raw.dots_and_lines_loading_plexus

    @RawRes
    private val emptyBox1 = raw.empty_box_1

    @RawRes
    private val emptyBox2 = raw.empty_box_2

    @RawRes
    private val empty = raw.empty

    @RawRes
    private val error1 = raw.error_1

    @RawRes
    private val error2 = raw.error_2

    @RawRes
    private val errorAnimation = raw.error_animation

    @RawRes
    private val expandingUiElements = raw.expanding_ui_elements

    @RawRes
    private val internet = raw.internet

    @RawRes
    private val jumpingBoxOnRopeLoadingAnimation = raw.jumping_box_on_rope_loading_animation

    @RawRes
    private val liquidBlobLoaderGreen = raw.liquid_blob_loader_green

    @RawRes
    private val loading1 = raw.loading_1

    @RawRes
    private val loading2 = raw.loading_2

    @RawRes
    private val loadingAnimation = raw.loading_animation

    @RawRes
    private val loadingBlob = raw.loading_blob

    @RawRes
    private val loadingPaperPlane1 = raw.loading_paper_plane_1

    @RawRes
    private val loopLoadingAnimation = raw.loop_loading_animation

    @RawRes
    private val noMobileInternet = raw.no_mobile_internet

    @RawRes
    private val worldLocations = raw.world_locations

    private val otherAnimations = listOf(
        emptyBox1,
        empty,
        internet, // noMobileInternet,
        // emptyBox2,
    )

    private val errorAnimations = listOf(
        error1,
        error2,
        errorAnimation,
    )

    private val loadingAnimations = listOf(
        loadingBlob,
        loadingPaperPlane1,
        liquidBlobLoaderGreen,
        blueLoading,
        loading1,
        loopLoadingAnimation, // FIXME: CHANGE COLOR
        jumpingBoxOnRopeLoadingAnimation, // FIXME: CHANGE COLOR
        // dotsAndLinesLoadingPlexus,
        // loadingAnimation,
        // expandingUiElements,
        // worldLocations,
        // loading2,
    )

    val randomLoadingAnimation
        @RawRes get() = loadingAnimations.random()

    val randomErrorAnimation
        @RawRes get() = errorAnimations.random()

    val randomOtherAnimation
        @RawRes get() = otherAnimations.random()
}
