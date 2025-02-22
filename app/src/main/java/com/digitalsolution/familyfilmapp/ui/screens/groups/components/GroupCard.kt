package com.digitalsolution.familyfilmapp.ui.screens.groups.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEditOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.digitalsolution.familyfilmapp.R
import com.digitalsolution.familyfilmapp.model.local.Group
import com.digitalsolution.familyfilmapp.ui.screens.groups.states.GroupUiState
import com.digitalsolution.familyfilmapp.ui.theme.bold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCard(
    groupTitle: String,
    groupUiState: GroupUiState,
    members: List<Group>,
    onRemoveMemberClick: (Group) -> Unit,
    onSwipeDelete: (Group) -> Unit,
    onAddMemberClick: () -> Unit,
    onDeleteGroupClick: () -> Unit,
    onChangeGroupName: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Cambiado a SpaceBetween
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (!groupUiState.checkedEditGroupName.value) {
                    Text(
                        text = groupTitle,
                        style = MaterialTheme.typography.titleLarge.bold(),
                        modifier = Modifier
                            .weight(1f) // Asigna un peso al texto
                            .padding(3.dp),
                    )
                } else {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp),
                        value = groupUiState.groupTitleChange.value,
                        onValueChange = { groupUiState.groupTitleChange.value = it },
                        shape = RoundedCornerShape(10.dp),
                        label = {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 4.dp),
                                text = stringResource(id = R.string.groups_text_edit_group_name),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { onChangeGroupName(groupUiState.groupTitleChange.value) }) {
                                Text(text = stringResource(id = R.string.send_text))
                            }
                        },
                    )
                }
                OutlinedIconToggleButton(
                    modifier = Modifier
                        .wrapContentWidth() // Asigna un peso al botón
                        .padding(end = 8.dp), // Añade un padding al final (derecha) del botón
                    checked = groupUiState.checkedEditGroupName.value,
                    onCheckedChange = { groupUiState.checkedEditGroupName.value = it },
                    enabled = true,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ModeEditOutline,
                        contentDescription = "Edit",
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(onClick = { onAddMemberClick() }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    Text(text = stringResource(id = R.string.groups_text_add_member))
                }
                OutlinedButton(onClick = { onDeleteGroupClick() }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    Text(text = stringResource(id = R.string.groups_text_delete_group))
                }
            }
            LazyColumn {
                items(members.toMutableList()) { item ->
                    val state = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart) {
                                onSwipeDelete(item)
                            }
                            true
                        },
                    )
                    AnimatedVisibility(
                        visible = state.currentValue != DismissValue.DismissedToEnd,
                        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                    ) {
                        SwipeToDismiss(
                            state = state,
                            background = {
                                val color = when (state.dismissDirection) {
                                    DismissDirection.StartToEnd -> Color.Transparent
                                    DismissDirection.EndToStart -> Color(0xFFFF1744)
                                    null -> Color.Transparent
                                }
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    shape = MaterialTheme.shapes.small,
                                    colors = CardDefaults.cardColors(
                                        containerColor = color,
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 3.dp,
                                    ),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color)
                                            .padding(12.dp, 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "",
                                        )
                                    }
                                }
                            },
                            dismissContent = {
                                GroupCard(
                                    group = item,
                                    onRemoveMemberClick = onRemoveMemberClick,
                                )
                            },
                            directions = setOf(DismissDirection.EndToStart),
                        )
                    }
                }
            }
        }
    }
}
