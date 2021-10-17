package io.morfly.client.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.morfly.client.ui.theme.AndroidClientTheme


@Composable
fun MenuScreen(navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("rest/posts") }) {
            Text("REST")
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate("graphql/posts") }) {
            Text("GraphQL")
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate("grpc/posts") }) {
            Text("gRPC")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidClientTheme {
        MenuScreen(rememberNavController())
    }
}