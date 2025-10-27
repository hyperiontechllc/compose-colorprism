package dev.hyperiontech.composecolorprism.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.hyperiontech.composecolorprism.sample.page.ColorPickerOrbitPage
import dev.hyperiontech.composecolorprism.sample.page.ColorPickerSpectrumPage
import dev.hyperiontech.composecolorprism.sample.page.ColorPickerSwatchesPage
import dev.hyperiontech.composecolorprism.sample.page.ColorPickerWheelPage
import dev.hyperiontech.composecolorprism.sample.shared.ColorPickerPageType
import kotlinx.coroutines.launch

@Composable
fun ColorPrismSampleScreen(modifier: Modifier = Modifier) {
    val pageTypes = ColorPickerPageType.entries
    val pagerState = rememberPagerState(initialPage = 0, pageCount = pageTypes::size)
    val coroutineScope = rememberCoroutineScope()
    val maxWidth = 400.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
        ) {
            pageTypes.forEachIndexed { index, page ->
                Tab(
                    text = {
                        Text(
                            text = page.pageName,
                            maxLines = 1,
                            overflow = TextOverflow.Visible,
                            softWrap = false,
                            textAlign = TextAlign.Center,
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page = index)
                        }
                    },
                )
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { pageIndex ->
            val page = ColorPickerPageType.fromValue(value = pageIndex)

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                when (page) {
                    ColorPickerPageType.WHEEL ->
                        ColorPickerWheelPage(
                            modifier = Modifier.widthIn(max = maxWidth),
                        )

                    ColorPickerPageType.ORBIT ->
                        ColorPickerOrbitPage(
                            modifier = Modifier.widthIn(max = maxWidth),
                        )

                    ColorPickerPageType.SPECTRUM ->
                        ColorPickerSpectrumPage(
                            modifier = Modifier.widthIn(max = maxWidth),
                        )

                    ColorPickerPageType.SWATCHES ->
                        ColorPickerSwatchesPage(
                            modifier = Modifier.widthIn(max = maxWidth),
                        )

                    else -> Unit
                }
            }
        }
    }
}

@Preview
@Composable
private fun TabbedPagerScreenPreview() {
    MaterialTheme {
        ColorPrismSampleScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
