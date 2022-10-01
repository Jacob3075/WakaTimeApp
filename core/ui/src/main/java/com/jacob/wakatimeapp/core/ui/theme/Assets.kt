package com.jacob.wakatimeapp.core.ui.theme

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.jacob.wakatimeapp.core.ui.R

object Assets {
    val animations: Animations = Animations
    val illustrations: Illustrations = Illustrations
    val icons: Icons = Icons
}

object Animations {
    @RawRes
    private val blueLoading = R.raw.blue_loading

    @RawRes
    private val dotsAndLinesLoadingPlexus = R.raw.dots_and_lines_loading_plexus

    @RawRes
    private val emptyBox1 = R.raw.empty_box_1

    @RawRes
    private val emptyBox2 = R.raw.empty_box_2

    @RawRes
    private val empty = R.raw.empty

    @RawRes
    private val error1 = R.raw.error_1

    @RawRes
    private val error2 = R.raw.error_2

    @RawRes
    private val errorAnimation = R.raw.error_animation

    @RawRes
    private val expandingUiElements = R.raw.expanding_ui_elements

    @RawRes
    private val internet = R.raw.internet

    @RawRes
    private val jumpingBoxOnRopeLoadingAnimation = R.raw.jumping_box_on_rope_loading_animation

    @RawRes
    private val liquidBlobLoaderGreen = R.raw.liquid_blob_loader_green

    @RawRes
    private val loading1 = R.raw.loading_1

    @RawRes
    private val loading2 = R.raw.loading_2

    @RawRes
    private val loadingAnimation = R.raw.loading_animation

    @RawRes
    private val loadingBlob = R.raw.loading_blob

    @RawRes
    private val loadingPaperPlane1 = R.raw.loading_paper_plane_1

    @RawRes
    private val loopLoadingAnimation = R.raw.loop_loading_animation

    @RawRes
    private val noMobileInternet = R.raw.no_mobile_internet

    @RawRes
    private val worldLocations = R.raw.world_locations

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

object Illustrations {
    @DrawableRes
    private val astronaut01 = R.drawable.il_astronaut_01

    @DrawableRes
    private val bookReading = R.drawable.il_book_reading

    @DrawableRes
    private val emptyBox1 = R.drawable.il_empty_box_1

    @DrawableRes
    private val emptyBox2 = R.drawable.il_empty_box_2

    @DrawableRes
    private val emptyBox3 = R.drawable.il_empty_box_3

    @DrawableRes
    private val closedBox = R.drawable.il_closed_box

    @DrawableRes
    private val emptyEnvelop = R.drawable.il_empty_envelop

    @DrawableRes
    private val noConnection1 = R.drawable.il_no_connection_1

    @DrawableRes
    private val noConnection2 = R.drawable.il_no_connection_2

    @DrawableRes
    private val noConnection3 = R.drawable.il_no_connection_3

    @DrawableRes
    private val noConnection4 = R.drawable.il_no_connection_4

    @DrawableRes
    private val search = R.drawable.il_search

    @DrawableRes
    private val intoTheNight = R.drawable.il_into_the_night

    @DrawableRes
    private val loading = R.drawable.il_loading

    @DrawableRes
    private val meditation = R.drawable.il_meditation

    @DrawableRes
    private val noData = R.drawable.il_no_data

    @DrawableRes
    private val notFound = R.drawable.il_not_found

    private val loadingIllustrations = listOf(
        loading,
        intoTheNight
    )

    private val noConnectionIllustrations = listOf(
        noConnection1,
        noConnection2,
        noConnection3,
        noConnection4,
    )
    private val emptyIllustrations = listOf(
        astronaut01,
        bookReading,
        emptyBox1,
        emptyBox2,
        emptyBox3,
        emptyEnvelop,
        noData,
        notFound,
        meditation, // FIXME: CHANGE COLOR
    )

    val randomEmptyIllustration
        @DrawableRes get() = emptyIllustrations.random()

    val randomNoConnectionIllustration
        @DrawableRes get() = noConnectionIllustrations.random()

    val randomLoadingIllustration
        @DrawableRes get() = loadingIllustrations.random()
}

object Icons {
    @DrawableRes
    val time = R.drawable.ic_time

    @DrawableRes
    val code = R.drawable.ic_code

    @DrawableRes
    val codeFile = R.drawable.ic_code_file

    @DrawableRes
    val git = R.drawable.ic_git

    @DrawableRes
    val laptop = R.drawable.ic_laptop

    @DrawableRes
    val arrow = R.drawable.ic_arrow
}
