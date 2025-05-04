package com.example.fit_sentinel.nav.bottom_nav_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.nav.nav_graph.MainScreen
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun BottomNavBar(
    isSelected: (item: BottomNavItem<out Any>) -> Boolean,
    onItemClick: (item: BottomNavItem<out Any>) -> Unit,
    modifier: Modifier = Modifier
) {
    MainCard(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        NavigationBar(
            modifier = Modifier.heightIn(max = 60.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            navItems.forEach { item ->
                val selected = isSelected(item)

                val indicatorColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.background else Color.Transparent,
                    label = "indicator background color animation"
                )

                NavigationBarItem(
                    selected = isSelected(item),
                    onClick = { onItemClick(item) },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.icon),
                            contentDescription = "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(indicatorColor)
                                .padding(8.dp)
                        )
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
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    Fit_SentinelTheme {
        BottomNavBar({ it.destination == MainScreen.Home }, {})
    }
}