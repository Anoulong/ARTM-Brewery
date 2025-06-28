package quebec.artm.breweryco.presentation.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import quebec.artm.breweryco.R
import quebec.artm.breweryco.ui.theme.BreweryCoTheme


@Composable
fun ErrorDialog(
    message: String = "",
    onCancelClicked: () -> Unit,
) {
    Dialog(
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
        content = {
            BreweryCoTheme {
                Surface(
                    modifier = Modifier.wrapContentSize(),
                    color = Color.Transparent,
                ) {
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(16.dp),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_radius)),
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.White)
                                    .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = message,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Button(
                                onClick = {
                                    onCancelClicked()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Ok")
                            }
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onCancelClicked()
        },
    )
}
