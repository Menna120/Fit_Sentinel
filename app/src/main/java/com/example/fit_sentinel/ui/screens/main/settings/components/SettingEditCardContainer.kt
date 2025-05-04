package com.example.fit_sentinel.ui.screens.main.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.common.user_data_input_cards.EditDataCardContainer

@Composable
fun SettingEditCardContainer(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    EditDataCardContainer(onDismiss, modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {

            content()

            MainButton(
                "Submit",
                true,
                Modifier
                    .fillMaxWidth(.6f)
                    .align(Alignment.CenterHorizontally),
                onClick = onSubmit
            )
        }
    }
}