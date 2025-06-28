package quebec.artm.breweryco.extension

import android.os.Parcelable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp

@Composable
fun <T : Parcelable> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver =
            listSaver(
                save = { it.toList() },
                restore = { it.toMutableStateList() },
            ),
    ) {
        elements.toList().toMutableStateList()
    }
}

fun Modifier.placeholder(
    isLoading: Boolean,
    backgroundColor: Color = Color.Unspecified,
    shape: Shape = RoundedCornerShape(4.dp),
    showShimmerAnimation: Boolean = true
): Modifier = composed {
    val highlight = if (showShimmerAnimation) {
        PlaceholderHighlight.shimmer(highlightColor = MaterialTheme.colorScheme.onTertiary)
    } else {
        null
    }
    val specifiedBackgroundColor = backgroundColor.takeOrElse { Color(0xFFDBD6D1).copy(0.6f) }
    Modifier.placeholder(
        color = specifiedBackgroundColor,
        visible = isLoading,
        shape = shape,
        highlight = highlight
    )
}

