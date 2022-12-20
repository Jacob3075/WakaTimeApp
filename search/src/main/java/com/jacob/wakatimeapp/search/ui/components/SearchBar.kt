package com.jacob.wakatimeapp.search.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.jacob.wakatimeapp.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchBar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.labelMedium,
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
            )
        },
        trailingIcon = {
            if (value == TextFieldValue("")) return@TextField

            IconButton(
                onClick = { onValueChange(TextFieldValue("")) },
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(percent = 25),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )
}
