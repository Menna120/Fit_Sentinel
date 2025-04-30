package com.example.fit_sentinel.ui.screens.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun DataEditRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)) // Using a semi-transparent purple from the image
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground, // Or a suitable text color
                modifier = Modifier.weight(1f) // Allow the text to take up available space
            )
            Icon(
                painter = painterResource(id = R.drawable.edit_settings), // Replace with your edit icon,
                contentDescription = "Edit $label",
                tint = MaterialTheme.colorScheme.primary, // Or a suitable icon color
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onEditClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Space between rows
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDataEditRow() {
    Fit_SentinelTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            DataEditRow(
                label = "Name",
                value = "Ahmed Mohamed",
                modifier = Modifier.padding(16.dp),
            ) {}
        }

    }
}