import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mike.widgetind.R

@Composable
fun AddWidgetDialog(
    pageNumber: Int,
    iconRes: Int,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp), // Remove rounded corners for full screen
                color = MaterialTheme.colorScheme.background
            ) {
                WidgetSettingsScreen(
                    pageNumber,
                    iconRes,
                    onConfirm = { selectedPage, selectedIcon ->
                        onConfirm(selectedPage, selectedIcon)
                        onDismiss()
                    },
                    onCancel = onDismiss
                )
            }
        }
    }
}

@Composable
fun WidgetSettingsScreen(
    pageNumber: Int,
    iconRes: Int,
    onConfirm: (Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_car,
        R.drawable.ic_directions_run_24,
        R.drawable.ic_sailing_24,
        R.drawable.ic_shopping_cart_24,
        R.drawable.ic_sports_esports_24,
        R.drawable.ic_temple_buddhist_24,
        R.drawable.ic_train_24,
        R.drawable.ic_work_24
    )
    var selectedPage by remember { mutableStateOf(pageNumber) }
    var selectedIconIndex by remember { mutableStateOf(iconRes) }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Configure your widget", style = MaterialTheme.typography.headlineMedium)

        // Dropdown for Page Number
        DropdownMenuBox(
            label = "Select Page Number",
            items = (1..10).toList(),
            selectedItem = selectedPage,
            onItemSelected = { selectedPage = it }
        )

        // Dropdown for Icon Selection
        DropdownMenuBox(
            label = "Select Icon",
            items = icons,
            selectedItem = selectedIconIndex,
            onItemSelected = { selectedIconIndex = it },
            isIcon = true
        )

        // Confirm and Cancel buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onConfirm(selectedPage, selectedIconIndex) }) {
                Text("Confirm")
            }
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun <T> DropdownMenuBox(
    label: String,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    isIcon: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                .padding(8.dp)
        ) {
            if (isIcon && selectedItem is Int) {
                Image(
                    painter = painterResource(id = selectedItem),
                    contentDescription = "Selected Icon",
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(text = selectedItem.toString())
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (isIcon) {
                items.forEach { iconResId ->
                    DropdownMenuItem(
                        text = {
                            Image(
                                painter = painterResource(id = iconResId as Int),
                                contentDescription = "Icon",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        },
                        onClick = {
                            onItemSelected(iconResId)
                            expanded = false
                        }
                    )
                }
            } else {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.toString()) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
