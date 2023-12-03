@file:Suppress("unused")

package com.jacob.wakatimeapp.core.ui.theme.assets

import androidx.annotation.RawRes
import androidx.compose.runtime.Immutable
import com.jacob.wakatimeapp.core.ui.R.raw

@Immutable
object Animations {

    /**
     * [Source](https://lottiefiles.com/81813-blue-loading)
     */
    @RawRes
    private val blueLoading = raw.blue_loading

    /**
     * [Source](https://lottiefiles.com/629-empty-box)
     */
    @RawRes
    private val emptyBox1 = raw.empty_box_1

    /**
     * [Source](https://lottiefiles.com/38463-error)
     */
    @RawRes
    private val error1 = raw.error_1

    /**
     * [Source](https://lottiefiles.com/51382-astronaut-light-theme)
     */
    @RawRes
    private val error2 = raw.error_2

    /**
     * [Source](https://lottiefiles.com/67012-404-error-animation)
     */
    @RawRes
    private val errorAnimation = raw.error_animation

    /**
     * [Source](https://lottiefiles.com/87491-liquid-blobby-loader-green)
     */
    @RawRes
    private val liquidBlobLoaderGreen = raw.liquid_blob_loader_green

    /**
     * [Source](https://lottiefiles.com/60867-waiting)
     */
    @RawRes
    private val loading1 = raw.loading_1

    /**
     * [Source](https://lottiefiles.com/9305-loading-bloob)
     */
    @RawRes
    private val loadingBlob = raw.loading_blob

    /**
     * [Source](https://lottiefiles.com/9844-loading-40-paperplane)
     */
    @RawRes
    private val loadingPaperPlane = raw.loading_paper_plane

    /**
     * [Source](https://lottiefiles.com/64095-no-mobile-internet)
     */
    @RawRes
    private val noMobileInternet = raw.no_mobile_internet

    @RawRes
    /**
     * [Source](https://lottiefiles.com/78255-background-looping-animation)
     */
    private val backgroundLooping = raw.background_looping_animation

    /**
     * [Source](https://lottiefiles.com/98312-empty)
     */
    @RawRes
    private val empty = raw.empty

    /**
     * [Source](https://lottiefiles.com/8572-liquid-blobby-loader)
     */
    @RawRes
    private val liquidBlobLoader = raw.liquid_blobby_loader

    /**
     * [Source](https://lottiefiles.com/28-loading)
     */
    @RawRes
    private val loading2 = raw.loading_2

    /**
     * [Source](https://lottiefiles.com/101961-non-data-found)
     */
    @RawRes
    private val noDataFound = raw.no_data_found

    /**
     * [Source](https://lottiefiles.com/94905-404-not-found)
     */
    @RawRes
    private val notFound = raw.not_found

    /**
     * [Source](https://lottiefiles.com/448-ripple-loading-animation)
     */
    @RawRes
    private val rippleLoading = raw.ripple_loading_animation

    /**
     * [Source](https://lottiefiles.com/66934-tumbleweed-rolling)
     */
    @RawRes
    private val tumbleweed = raw.tumbleweed_rolling

    /**
     * Note: Need to view from collection, direct link does not work for some reason.
     * [Source](https://lottiefiles.com/animations/cloud-file-access-male-qUtUwFHikB)
     */
    @RawRes
    private val cloudFileAccess = raw.cloud_file_access

    /**
     * Note: Need to view from collection, direct link does not work for some reason.
     * [Source](https://lottiefiles.com/animations/cloud-reporting-syncing-female-txDx0gf1L8)
     */
    @RawRes
    private val cloudReporting = raw.cloud_reporting

    /**
     * Note: Need to view from collection, direct link does not work for some reason.
     * [Source](https://lottiefiles.com/animations/big-data-centre-isomatric-animation-json-qR4Mn2mMjL)
     */
    @RawRes
    private val dataCenter = raw.data_center

    /**
     * Note: Need to view from collection, direct link does not work for some reason.
     * [Source](https://lottiefiles.com/animations/network-folder-female-5gZ430mXWb)
     */
    @RawRes
    private val networkFolder = raw.network_folder

    /**
     * [Source](https://lottiefiles.com/animations/transfer-files-2GvcNPrxhZ)
     */
    @RawRes
    private val transferFiles = raw.transfer_files

    private val emptyAnimations = listOf(
        emptyBox1,
        empty,
    )

    private val errorAnimations = listOf(
        error1,
        error2,
        errorAnimation,
    )

    private val loadingAnimations = listOf(
        loadingBlob,
        loadingPaperPlane,
        liquidBlobLoaderGreen,
        liquidBlobLoader,
        blueLoading,
        loading1,
        loading2,
        tumbleweed,
        rippleLoading,
        backgroundLooping,
    )

    private val noDataAnimations = listOf(
        notFound,
        noDataFound,
        noMobileInternet,
    )

    private val dataAnimations = listOf(
        //        cloudFileAccess,
        //        cloudReporting,
        //        dataCenter,
        //        networkFolder,
        transferFiles,
    )

    val randomLoadingAnimation
        @RawRes get() = loadingAnimations.random()

    val randomErrorAnimation
        @RawRes get() = errorAnimations.random()

    val randomEmptyAnimation
        @RawRes get() = emptyAnimations.random()

    val randomNoDataAnimation
        @RawRes get() = noDataAnimations.random()

    val randomDataTransferAnimations
        @RawRes get() = dataAnimations.random()
}
