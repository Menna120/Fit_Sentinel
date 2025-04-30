package com.example.fit_sentinel.ui.common.user_data_input_cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun EditDataCardContainer(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(onDismissRequest = onDismiss) {
        MainCard(modifier = modifier) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 36.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Preview
@Composable
private fun EditDataCardContainerPreview() {
    Fit_SentinelTheme {
        EditDataCardContainer({}) {}
    }
}