package com.capital.composesample.ui.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class MultiFabParam(
    val tag: Int,
    val buttonSize: Dp = 50.dp,
    val iconSize: Dp = 20.dp,
    val icon: ImageVector,
    val iconTintColor: Color = Color.White,
    val iconBackgroundColor: Color = Color.Unspecified,
    val description: String,
)

enum class MultiFabState {
    Collapsed,
    Expanded
}

@Composable
fun MultiFloatingActionButton(
    modifier: Modifier = Modifier,
    mainButtonParam: MultiFabParam,
    subButtonsParam: List<MultiFabParam>,
    onItemClicked: (item: MultiFabParam) -> Unit
) {
    val currentState = remember { mutableStateOf(MultiFabState.Collapsed) }
    val transition = updateTransition(targetState = currentState, label = "")

    val rotateAnim: Float by transition.animateFloat(
        transitionSpec = {
            spring(dampingRatio = 0.5F, stiffness = Spring.StiffnessLow)
        }, label = ""
    ) { state ->
        when (state.value) {
            MultiFabState.Collapsed -> 0F
            MultiFabState.Expanded -> 45F
        }
    }
    val alphaAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 200)
    }, label = "") { state ->
        when (state.value) {
            MultiFabState.Collapsed -> 0F
            MultiFabState.Expanded -> 1F
        }
    }

    val expandListAnim: MutableList<Float> = mutableListOf()
    val space = 10F
    subButtonsParam.forEachIndexed { index, item ->
        val expandAnim by transition.animateFloat(targetValueByState = { state ->
            when (state.value) {
                MultiFabState.Collapsed -> 0F
                MultiFabState.Expanded -> mainButtonParam.buttonSize.value + index * item.buttonSize.value + (index + 1) * space
            }
        }, label = "", transitionSpec = {
            when (targetState.value) {
                MultiFabState.Collapsed -> spring(stiffness = Spring.StiffnessLow)
                MultiFabState.Expanded -> spring(dampingRatio = 0.5F, stiffness = Spring.StiffnessLow)
            }
        })
        expandListAnim.add(index, expandAnim)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        subButtonsParam.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .padding(bottom = expandListAnim[index].dp)
                    .alpha(animateFloatAsState(alphaAnim).value)
            ) {
                FloatingActionButton(
                    backgroundColor =
                    if (item.iconBackgroundColor == Color.Unspecified)
                        MaterialTheme.colors.primary
                    else
                        item.iconBackgroundColor,
                    modifier = Modifier.size(item.buttonSize),
                    onClick = {
                        currentState.value = MultiFabState.Collapsed
                        onItemClicked(item)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 1.dp
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(item.iconSize),
                        imageVector = item.icon,
                        tint = item.iconTintColor,
                        contentDescription = item.description
                    )
                }
            }
        }

        FloatingActionButton(
            backgroundColor =
            if(mainButtonParam.iconBackgroundColor == Color.Unspecified) {
                MaterialTheme.colors.primary
            } else {
                mainButtonParam.iconBackgroundColor
            },
            modifier = Modifier.size(mainButtonParam.buttonSize).rotate(rotateAnim),
            onClick = {
                currentState.value =
                    when (currentState.value) {
                        MultiFabState.Collapsed -> MultiFabState.Expanded
                        MultiFabState.Expanded -> MultiFabState.Collapsed
                    }
                onItemClicked(mainButtonParam)
            }) {
            Icon(
                modifier = Modifier.size(mainButtonParam.iconSize),
                imageVector =
                when (currentState.value) {
                    MultiFabState.Collapsed -> mainButtonParam.icon
                    MultiFabState.Expanded -> Icons.Filled.Add
                },
                tint = mainButtonParam.iconTintColor,
                contentDescription = mainButtonParam.description
            )
        }
    }
}