package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.screens.on_boarding.components.OnboardingProgressBar
import com.example.fit_sentinel.ui.screens.on_boarding.model.OnboardingScreen
import com.example.fit_sentinel.ui.screens.on_boarding.model.SmokingOption
import com.example.fit_sentinel.ui.screens.on_boarding.pages.AgePage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.GenderPage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.HeightPage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.IllnessPage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.IntroPage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.NamePage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.SmokingPage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.TargetWeightPage
import com.example.fit_sentinel.ui.screens.on_boarding.pages.WeightPage
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = OnboardingScreen.INTRO.ordinal,
        pageCount = { OnboardingScreen.entries.size }
    )

    val uiState by viewModel.uiState.collectAsState()

    val isCurrentPageValid by viewModel.isCurrentPageValid(pagerState.currentPage).collectAsState()
    val showPreviousButton by viewModel.showPreviousButton(pagerState.currentPage).collectAsState()
    val isLastPage by viewModel.isLastPage(pagerState.currentPage).collectAsState()

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showPreviousButton) {
                OnboardingProgressBar(
                    currentStep = pagerState.currentPage,
                    totalSteps = pagerState.pageCount,
                    onBackClick = {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (OnboardingScreen.entries.getOrNull(page)) {
                    OnboardingScreen.INTRO -> {
                        IntroPage {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }

                    OnboardingScreen.NAME -> {
                        NamePage(
                            name = uiState.name,
                            onNameChange = viewModel::onNameChange
                        )
                    }

                    OnboardingScreen.GENDER -> {
                        GenderPage(
                            selectedGender = uiState.gender,
                            onGenderSelected = viewModel::onGenderSelected
                        )
                    }

                    OnboardingScreen.SMOKING -> {
                        SmokingPage(
                            selectedSmokingOption = uiState.smokingOption,
                            onSmokingOptionClick = { viewModel.onSmokingOptionClick(it as SmokingOption) }
                        )
                    }

                    OnboardingScreen.ILLNESS -> {
                        IllnessPage(
                            illnessDescription = uiState.illnessDescription,
                            onDescriptionChange = viewModel::onDescriptionChange
                        )
                    }

                    OnboardingScreen.WEIGHT -> {
                        WeightPage(
                            selectedUnit = uiState.selectedWeightUnit,
                            units = uiState.weightUnits,
                            selectedWeightValue = uiState.selectedWeightValue,
                            onUnitSelected = viewModel::onWeightUnitSelected,
                            onValueChange = viewModel::onWeightValueChange
                        )
                    }

                    OnboardingScreen.HEIGHT -> {
                        HeightPage(
                            selectedUnit = uiState.selectedHeightUnit,
                            units = uiState.heightUnits,
                            selectedHeightValue = uiState.selectedHeightValue,
                            onUnitSelected = viewModel::onHeightUnitSelected,
                            onValueChange = viewModel::onHeightValueChange
                        )
                    }

                    OnboardingScreen.AGE -> {
                        AgePage(
                            selectedAge = uiState.selectedAge,
                            onAgeChange = viewModel::onAgeChange
                        )
                    }

                    OnboardingScreen.TARGET_WEIGHT -> {
                        TargetWeightPage(
                            targetWeight = uiState.targetWeightPlaceholder,
                            targetWeightPlaceholder = uiState.targetWeightPlaceholder,
                            weightUnit = uiState.selectedWeightUnit.name.lowercase(),
                            onTargetWeightChange = viewModel::onTargetWeightChange
                        )
                    }

                    null -> Unit
                }
            }

            AnimatedVisibility(
                pagerState.currentPage > OnboardingScreen.INTRO.ordinal,
                enter = slideInHorizontally(initialOffsetX = { it })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MainButton(
                        onClick = {
                            if (isLastPage) {
                                viewModel.onOnboardingComplete()
                                onOnboardingComplete()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        text = if (isLastPage) "Finish" else "Continue",
                        showIcon = false,
                        enabled = isCurrentPageValid
                    )
                }
            }
        }
    }
}