@file:Suppress("ForbiddenComment")

package com.jacob.wakatimeapp.core.ui.theme.assets

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.jacob.wakatimeapp.core.ui.R.drawable

@Immutable
object Illustrations {
    @DrawableRes
    private val astronaut01 = drawable.il_astronaut_01

    @DrawableRes
    private val bookReading = drawable.il_book_reading

    @DrawableRes
    private val emptyBox1 = drawable.il_empty_box_1

    @DrawableRes
    private val emptyBox2 = drawable.il_empty_box_2

    @DrawableRes
    private val emptyBox3 = drawable.il_empty_box_3

    @DrawableRes
    private val closedBox = drawable.il_closed_box

    @DrawableRes
    private val emptyEnvelop = drawable.il_empty_envelop

    @DrawableRes
    private val noConnection1 = drawable.il_no_connection_1

    @DrawableRes
    private val noConnection2 = drawable.il_no_connection_2

    @DrawableRes
    private val noConnection3 = drawable.il_no_connection_3

    @DrawableRes
    private val noConnection4 = drawable.il_no_connection_4

    @DrawableRes
    private val search = drawable.il_search

    @DrawableRes
    private val intoTheNight = drawable.il_into_the_night

    @DrawableRes
    private val loading = drawable.il_loading

    @DrawableRes
    private val meditation = drawable.il_meditation

    @DrawableRes
    private val noData = drawable.il_no_data

    @DrawableRes
    private val notFound = drawable.il_not_found

    private val loadingIllustrations = listOf(
        loading,
        intoTheNight,
        search,
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
        closedBox,
    )

    val randomEmptyIllustration
        @DrawableRes get() = emptyIllustrations.random()

    val randomNoConnectionIllustration
        @DrawableRes get() = noConnectionIllustrations.random()

    val randomLoadingIllustration
        @DrawableRes get() = loadingIllustrations.random()
}
