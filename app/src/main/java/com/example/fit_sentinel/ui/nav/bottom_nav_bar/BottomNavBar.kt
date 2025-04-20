package com.example.fit_sentinel.ui.nav.bottom_nav_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.ui.nav.nav_graph.main_nav.HomeRoute
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun BottomNavBar(
    isSelected: (item: BottomNavItem<out Any>) -> Boolean,
    onItemClick: (item: BottomNavItem<out Any>) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.clip(CircleShape),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = isSelected(item),
                onClick = { onItemClick(item) },
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxHeight(.7f)
                            .aspectRatio(1f)
                            .background(if (isSelected(item)) MaterialTheme.colorScheme.background else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.icon),
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    Fit_SentinelTheme {
        BottomNavBar({ it.destination == HomeRoute }, {})
    }
}